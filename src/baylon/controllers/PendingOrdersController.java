package baylon.controllers;


import baylon.app.BaylonFunctions;
import baylon.app.Functions;
import baylon.app.TableHelper;
import baylon.models.Customers;
import baylon.models.Deceased;
import baylon.models.Orders;
import com.sun.glass.ui.Screen;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TouchEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by troll173 on 11/25/15.
 */

public class PendingOrdersController {
    /**
     * Initializes the controller class.
     */
    @FXML
    TableView PendingOnlineOrdersTable,PendingOfflineOrdersTable;

    

    Orders tblorders = new Orders();
    ResultSet orders;
     ObservableList<ObservableList> data;
    Functions funcs = new Functions();


    public void initialize() throws SQLException {


        populateTable("Online",PendingOnlineOrdersTable);
        populateTable("Walk-In",PendingOfflineOrdersTable);


//ONLINE
        PendingOnlineOrdersTable.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                    try {
                        processOrder(PendingOnlineOrdersTable.getSelectionModel().getSelectedIndex(),"Online");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        PendingOnlineOrdersTable.setOnTouchPressed(new EventHandler<TouchEvent>() {
            public void handle(TouchEvent event) {
                try {
                    processOrder(PendingOnlineOrdersTable.getSelectionModel().getSelectedIndex(),"Online");
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });

//      OFFLINE

        PendingOfflineOrdersTable.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                    try {
                        processOrder(PendingOfflineOrdersTable.getSelectionModel().getSelectedIndex(),"Walk-In");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        PendingOfflineOrdersTable.setOnTouchPressed(new EventHandler<TouchEvent>() {
            public void handle(TouchEvent event) {
                try {
                    processOrder(PendingOfflineOrdersTable.getSelectionModel().getSelectedIndex(),"Walk-In");
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });


    }


    private void populateTable(String type,TableView tbl) throws SQLException {
        ArrayList<NameValuePair> nvp = new ArrayList<NameValuePair>();
        nvp.add(new BasicNameValuePair("status","Transfered"));
        nvp.add(new BasicNameValuePair("order_type",type));
        orders = tblorders.get(nvp);
        data = FXCollections.observableArrayList();

        ArrayList<String> cols = new ArrayList<String>();
        cols.add("Order Code");
        cols.add("Customer");
        cols.add("Deceased");
        cols.add("Payment Method");
        cols.add("Date/Time Ordered");
        cols.add("Bill");

        TableHelper.setColumns(cols, tbl);

        ObservableList data = FXCollections.observableArrayList();
        orders.beforeFirst();
        while (orders.next()){
            Customers tblcustomer = new Customers();
            ResultSet customers = tblcustomer.get(orders.getString("customer")+"");
            customers.first();
            Deceased tbldeceased = new Deceased();
            ResultSet deceased = tbldeceased.get(orders.getString("deceased")+"");
            deceased.first();

            ObservableList row = FXCollections.observableArrayList();
            row.add(orders.getString("ordercode")+"");
            row.add(customers.getString("lastname")+", "+customers.getString("firstname")+" "+customers.getString("middlename")+"");
            row.add(deceased.getString("lname")+", "+deceased.getString("fname")+" "+deceased.getString("mname")+"");
            row.add(orders.getString("payment_method")+"");
            row.add(orders.getString("created_at")+"");
            row.add(funcs.toMoney(orders.getString("price")+""));
            data.add(row);
        }
        System.out.println(data);
         tbl.setItems(data);
    }




    private void processOrder(int index,String type) throws SQLException, IOException {
        ArrayList<NameValuePair> nvp = new ArrayList<NameValuePair>();
        nvp.add(new BasicNameValuePair("status","Transfered"));
        nvp.add(new BasicNameValuePair("order_type",type));
        ResultSet ords  = tblorders.get(nvp);
        ords.first();
        int i = 0;
        while(i < index){
            ords.next();
            i++;
        }
//        System.out.println(orders.getString("ordercode"));


        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/baylon/views/ProcessPayment.fxml"));
        Parent root = fxmlLoader.load();
        ProcessPaymentController controller = fxmlLoader.<ProcessPaymentController>getController();
        controller.setRecord(ords);
        stage.setScene(new Scene(root));
        stage.setWidth(Screen.getMainScreen().getWidth()*0.70);
        stage.setHeight(Screen.getMainScreen().getHeight()*0.70);
//        stage.initStyle(StageStyle.UNDECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();


    }

}
