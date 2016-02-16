package baylon.controllers;

import baylon.app.BaylonFunctions;
import baylon.app.DataSelect;
import baylon.app.DataTable;
import baylon.app.Functions;
import baylon.models.Customers;
import baylon.models.Pickup;
import de.jensd.shichimifx.utils.SplitPaneDividerSlider;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.apache.http.HttpException;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Created by Troll173 on 2/8/2016.
 */
public class Pickups {

    @FXML
    TableView tablePickups;
    @FXML
    ComboBox customer;
    @FXML
    SplitPane pickupSplitPane;
    @FXML
    TextField deceased;
    @FXML
    TextArea txtlocation;
    @FXML
    DatePicker dateofpickup;

    DataSelect dcustomer;

    public static SplitPaneDividerSlider leftSplitPaneDividerSlider;


    Pickup tblpickups = Pickup.getInstance();
    Customers tblcustomers = Customers.getInstance();
    ResultSet pickups,customers;
    DataTable dataTable;
    BaylonFunctions funcs = new BaylonFunctions();

    public void initialize() throws SQLException, IOException {



        dcustomer = new DataSelect(customer);

        leftSplitPaneDividerSlider = new SplitPaneDividerSlider(pickupSplitPane, 0, SplitPaneDividerSlider.Direction.UP);
        pickupSplitPane.setMinWidth(com.sun.glass.ui.Screen.getMainScreen().getWidth());


        dataTable = new DataTable(tablePickups);

        customers = tblcustomers.get();
        customers.beforeFirst();

        ObservableList<String> customersList = FXCollections.observableArrayList();
        while (customers.next()){
            dcustomer.addOption(customers.getString("lastname")+", "+customers.getString("middlename")+" "+customers.getString("firstname"),customers.getString("uid"));
        }


        ArrayList<String> cols = new ArrayList<String>();
        cols.add("Name of Deceased");
        cols.add("Location");
        cols.add("Date of Pickup");
        cols.add("Contact Person");
        cols.add("Contact #");
        dataTable.setColumns(cols);

        initTable();

    }

    private void initTable() throws SQLException {
        ArrayList<NameValuePair> where = new ArrayList<>();
        where.add(new BasicNameValuePair("status","Pending"));
        pickups = tblpickups.get(where);
        pickups.beforeFirst();
        ObservableList data = FXCollections.observableArrayList();
        while(pickups.next()){
            ResultSet ccustomer = tblcustomers.get(pickups.getString("customer")+"");
            ccustomer.first();
            //Iterate Row
            ObservableList row = FXCollections.observableArrayList();
            row.add(pickups.getString("name_of_deceased")+" ");
            row.add(pickups.getString("location")+" ");
            row.add(pickups.getString("date_of_pickup")+" ");
            row.add(ccustomer.getString("lastname")+", "+ccustomer.getString("middlename")+" "+ccustomer.getString("firstname")+" ");
            row.add(ccustomer.getString("mobile")+" ");

            data.add(row);

        }
        dataTable.init(data,"Search Pickups..",true);

        dataTable.getOnDataSelected(event ->{
            try {
                pickups.first();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            String pickupId = dataTable.getSelected(pickups,"id");



                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Pickup Cadaver");
                    alert.setHeaderText("Pickup Cadaver?");
                    ButtonType btnProcess = new ButtonType("Yes");
                    ButtonType buttonTypeCancel = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

                    alert.getButtonTypes().setAll(btnProcess, buttonTypeCancel);

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == btnProcess) {
                        try {
                            pickupDeceased(pickupId);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }


        });
    }

    private void pickupDeceased(String pickupId) throws SQLException {
        ResultSet pickups2 = tblpickups.get(pickupId);
        pickups2.first();
        if(pickups2.getString("deceased").equalsIgnoreCase("0")){
            addDeceased(pickupId);
        }else{
            ArrayList<NameValuePair> nvp = new ArrayList<>();
            nvp.add(new BasicNameValuePair("status","complete"));
            tblpickups.save(nvp,pickupId);
            initTable();
        }
    }

    private void addDeceased(String pickupId) {

    }

    @FXML
    public void switchToNew(ActionEvent actionEvent) {
        leftSplitPaneDividerSlider.setAimContentVisible(false);
    }

    @FXML
    public void switchToExisting(ActionEvent actionEvent) {
        leftSplitPaneDividerSlider.setAimContentVisible(true);
    }

    @FXML
    void registerPickup() throws SQLException, URISyntaxException, HttpException {
        ArrayList<NameValuePair> nvp = new ArrayList<NameValuePair>();

        String selId =  dcustomer.getSelectedKey();

        ResultSet cccustomer = tblcustomers.get(selId);
        cccustomer.first();



        nvp.add(new BasicNameValuePair("name_of_deceased",deceased.getText()+""));
        nvp.add(new BasicNameValuePair("location",txtlocation.getText()+""));
        nvp.add(new BasicNameValuePair("date_of_pickup",dateofpickup.getValue().toString()));
        nvp.add(new BasicNameValuePair("customer",cccustomer.getString("uid")));
        nvp.add(new BasicNameValuePair("status","Pending"));

        int pid = tblpickups.save(nvp);
        funcs.SyncAdd(nvp, "pickup", pid+"");
        leftSplitPaneDividerSlider.setAimContentVisible(true);
            initTable();

    }
}
