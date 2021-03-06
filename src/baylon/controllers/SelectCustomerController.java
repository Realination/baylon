package baylon.controllers;


import baylon.app.*;
import baylon.models.Customers;
import baylon.models.Orders;
import com.sun.glass.ui.Screen;
import de.jensd.shichimifx.utils.SplitPaneDividerSlider;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by troll173 on 11/25/15.
 */

public class SelectCustomerController {

    @FXML
    SplitPane customerSplitPane;
    @FXML
    TableView tableCustomers;
    @FXML
    AnchorPane imageAnchor,customerRegForm;
    @FXML
    Label lblError;
    @FXML
    ComboBox region,province,municipality,marital;

    public static SplitPaneDividerSlider leftSplitPaneDividerSlider;
    Customers tblcustomers = new Customers();
    Orders tblorders = new Orders();
    ResultSet customers;
    Constants constants;
    DataTable dataTable;
    Form frmCustomer;

    public void initialize() throws SQLException {
        dataTable = new DataTable(tableCustomers);
        frmCustomer = new Form(customerRegForm);
        frmCustomer.setAsErrorDisplay(lblError);

        leftSplitPaneDividerSlider = new SplitPaneDividerSlider(customerSplitPane, 0, SplitPaneDividerSlider.Direction.LEFT);
        customerSplitPane.setMinWidth(com.sun.glass.ui.Screen.getMainScreen().getWidth());
        customerSplitPane.setMinWidth(com.sun.glass.ui.Screen.getMainScreen().getWidth());
        customerSplitPane.setDividerPosition(1,1);
        constants = Constants.getInstance();

        ObservableList<String> maritalStats = FXCollections.observableArrayList("Single", "Married", "Widow/Widower");
        marital.setItems(maritalStats);


        JSONArray jsonRegions = Functions.parseJson("/baylon/media/api/regions.js");
        ObservableList<String> regions = FXCollections.observableArrayList();
        for(int i=0; i<jsonRegions.length(); i++){
            try {
                String strRegion = jsonRegions.getString(i);
                regions.add(strRegion);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        region.setItems(regions);

        region.setOnAction(event -> {
              String strRegion = region.getValue().toString();
            JSONArray jsonProvinces = Functions.parseJson("/baylon/media/api/regions/"+strRegion+"/provinces.js");
                    ObservableList < String > provinces = FXCollections.observableArrayList();
            for(int i=0; i<jsonProvinces.length(); i++){
                try {
                    String strProvince = jsonProvinces.getString(i);
                    provinces.add(strProvince);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            province.setItems(provinces);
        });


        province.setOnAction(event -> {
            String strProvince = province.getValue().toString();
            JSONArray jsonMunicipalities = Functions.parseJson("/baylon/media/api/provinces/"+strProvince+"/cities_and_municipalities.js");
            ObservableList < String > municipalities = FXCollections.observableArrayList();
            for(int i=0; i<jsonMunicipalities.length(); i++){
                try {
                    String strMunicipality = jsonMunicipalities.getString(i);
                    municipalities.add(strMunicipality);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            municipality.setItems(municipalities);
        });



        customers = tblcustomers.get();
        customers.first();
        ArrayList<String> cols = new ArrayList<String>();
        cols.add("Firstname");
        cols.add("Middlename");
        cols.add("Lastname");
        cols.add("Address");
        cols.add("Citizenship");

        dataTable.setColumns(cols);
        ObservableList data = FXCollections.observableArrayList();

        while(customers.next()){
            //Iterate Row
            ObservableList row = FXCollections.observableArrayList();
            System.out.println(customers.getString("firstname"));
            row.add(customers.getString("firstname")+" ");
            row.add(customers.getString("middlename")+" ");
            row.add(customers.getString("lastname")+" ");
            row.add(customers.getString("address")+" ");
            row.add(customers.getString("citizenship")+" ");

            data.add(row);

        }
        dataTable.init(data,"Search Customer",true);

        tableCustomers.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                    selectCustomer(tableCustomers.getSelectionModel().getSelectedIndex());
                }
            }
        });

        tableCustomers.setOnTouchPressed(event -> selectCustomer(tableCustomers.getSelectionModel().getSelectedIndex()));


    }


    void selectCustomer(int index){
        try {
            customers.first();
            int c = 0;
            while (c <= index){
                customers.next();
                c++;
            }
            String custId = customers.getString("uid");
            constants.addValue("customer_id",custId);
            ArrayList<NameValuePair> nvp = new ArrayList<NameValuePair>();
            nvp.add(new BasicNameValuePair("customer",custId));
            tblorders.save(nvp,constants.getValue("orderid"));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Stage stage = (Stage) tableCustomers.getScene().getWindow();
        stage.close();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/baylon/views/SelectDeceased.fxml"));
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        stage.setScene(new Scene(root));
        stage.setWidth(Screen.getMainScreen().getWidth());
        stage.setHeight(Screen.getMainScreen().getHeight());
        stage.setMaximized(true);
        stage.setFullScreen(true);
        stage.setTitle("Baylon | Select Deceased");

        stage.show();
    }


    @FXML
    void switchToNew(){
        leftSplitPaneDividerSlider.setAimContentVisible(false);
    }

    @FXML
    void switchToExisting(){
        leftSplitPaneDividerSlider.setAimContentVisible(true);
    }


    @FXML
    void registerCustomer(){
      ArrayList<NameValuePair> nvp = frmCustomer.getData();
      System.out.println(nvp);
        tblcustomers.save(nvp);
        frmCustomer.clear();
    }


    @FXML
    void takePhoto(){
     

    }


    private boolean validateInput() {
        return false;
    }
}
