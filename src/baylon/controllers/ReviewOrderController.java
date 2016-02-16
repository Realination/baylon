package baylon.controllers;

import baylon.app.BaylonFunctions;
import baylon.app.Constants;
import baylon.app.Functions;
import baylon.app.TableHelper;
import baylon.models.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;
import org.apache.http.HttpException;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Troll173 on 1/17/2016.
 */
public class ReviewOrderController {

    @FXML
    Label lblOrdercode,lblPmet,lblCustomer,lblBill,lblDeceased;
    @FXML
    TableView tablePackage;

    BaylonFunctions funcs = new BaylonFunctions();
    Orders tblorders = new Orders();
    Customers tblcustomers = new Customers();
    Deceased tbldeceased = new Deceased();
    Services tblservices = new Services();
    Suboptions tblsubs = new Suboptions();

    ResultSet orders;
    ResultSet customers;
    ResultSet deceased;


    Constants constants;
    Functions func = new Functions();

    public void initialize() throws SQLException {
        constants = Constants.getInstance();
        orders = tblorders.get(constants.getValue("orderid"));
        orders.first();
        customers = tblcustomers.get(orders.getString("customer"));
        customers.first();
        deceased = tbldeceased.get(orders.getString("deceased"));
        deceased.first();

        lblOrdercode.setText(orders.getString("ordercode")+"");
        lblPmet.setText(orders.getString("payment_method")+"");
        lblCustomer.setText(customers.getString("lastname")+", "+customers.getString("firstname")+" "+customers.getString("middlename")+"");
        lblBill.setText(func.toMoney(orders.getString("custom_price")+""));
        lblDeceased.setText(deceased.getString("lname")+", "+deceased.getString("fname")+" "+deceased.getString("mname"));

        populateTable(orders.getString("services"));
    }

    private void populateTable(String strServices) throws SQLException {
        ArrayList<String> cols = new ArrayList<String>();
        cols.add("Service");
        cols.add("Qty");
        cols.add("Changes");
        cols.add("Type");

        TableHelper.setColumns(cols, tablePackage);


        String[] services = strServices.split(",");

        ObservableList data = FXCollections.observableArrayList();

        for (String service : services){
            ObservableList row = FXCollections.observableArrayList();
            String[] servData = service.split("-");

            ResultSet rservices;
            ResultSet suboptions;

            rservices = tblservices.get(servData[2]+"");
            rservices.first();

            suboptions = tblsubs.get(servData[3]+"");
            suboptions.first();


            String type;
            if(servData[3].equalsIgnoreCase("0")){
                type = "N/A";
            }else{
                type = suboptions.getString("name")+"";
            }


            row.add(rservices.getString("name")+"");
            row.add(servData[0]+" "+rservices.getString("unit")+"");
            if(rservices.getString("unit").equalsIgnoreCase("")){
                row.add("N/A");
            }else{
                row.add(servData[1]+"");
            }

            row.add(type);
            data.add(row);
        }
        tablePackage.getItems().setAll(data);

    }


    @FXML
    void transferToCashier(){
        ArrayList<NameValuePair> nvp = new ArrayList<NameValuePair>();
        nvp.add(new BasicNameValuePair("status","Transfered"));
        tblorders.save(nvp, constants.getValue("orderid"));
        billingStatement(constants.getValue("ordercode"));
        Stage stage = (Stage) lblOrdercode.getScene().getWindow();
        stage.close();
    }


    void billingStatement(String ordercode){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/baylon/views/BillingStatement.fxml"));
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BillingStatement controller = fxmlLoader.getController();
        controller.init(ordercode);
        Scene scene = new Scene(root);
        WritableImage snapshot = scene.snapshot(null);
        Stage stage = new Stage();
//        stage.setScene(scene);
//        stage.show();
        Functions.print(snapshot, stage);
    }


    @FXML
    void cancelOrder() throws URISyntaxException, HttpException {
        tblorders.remove(constants.getValue("orderid"));
        ArrayList<NameValuePair> where = new ArrayList<NameValuePair>();
        where.add(new BasicNameValuePair("ordercode",constants.getValue("ordercode")));
        funcs.SyncDelete(where, "custpack");
        Stage stage = (Stage) lblOrdercode.getScene().getWindow();
        stage.close();
    }

}
