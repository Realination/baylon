package baylon.controllers;


import baylon.app.Constants;
import baylon.app.TableHelper;
import baylon.models.Customers;
import baylon.models.Orders;
import com.sun.glass.ui.Screen;
import de.jensd.shichimifx.utils.SplitPaneDividerSlider;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.http.message.BasicNameValuePair;
import org.omg.CORBA.NameValuePair;

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

    public static SplitPaneDividerSlider leftSplitPaneDividerSlider;
    Customers tblcustomers = new Customers();
    Orders tblorders = new Orders();
    ResultSet customers;
    Constants constants;

    public void initialize() throws SQLException {
        leftSplitPaneDividerSlider = new SplitPaneDividerSlider(customerSplitPane, 0, SplitPaneDividerSlider.Direction.LEFT);
        customerSplitPane.setMinWidth(com.sun.glass.ui.Screen.getMainScreen().getWidth());
        customerSplitPane.setMinWidth(com.sun.glass.ui.Screen.getMainScreen().getWidth());
        customerSplitPane.setDividerPosition(1,1);
        constants = Constants.getInstance();

        customers = tblcustomers.get();
        customers.first();
        ArrayList<String> cols = new ArrayList<String>();
        cols.add("Firstname");
        cols.add("Middlename");
        cols.add("Lastname");
        cols.add("Address");
        cols.add("Citizenship");

        TableHelper.setColumns(cols,tableCustomers);
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
        tableCustomers.getItems().setAll(data);

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
            String custId = customers.getString("id");
            constants.addValue("customer_id",custId);
            ArrayList<org.apache.http.NameValuePair> nvp = new ArrayList<org.apache.http.NameValuePair>();
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
}
