package baylon.controllers;


import baylon.app.TableHelper;
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


    public void initialize() throws SQLException {


        populateTable("online",PendingOnlineOrdersTable);
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
        nvp.add(new BasicNameValuePair("status","Pending"));
        nvp.add(new BasicNameValuePair("order_type",type));
        orders = tblorders.get(nvp);
        data = FXCollections.observableArrayList();

        ArrayList<String> cols = new ArrayList<String>();
        ArrayList rows = new ArrayList();
        cols.add("Order Code");
        rows.add("ordercode");
        cols.add("Date/Time Ordered");
        rows.add("created_at");
        cols.add("Bill");
        rows.add("price");


        TableHelper.setColumns(cols, tbl);
        TableHelper.populateTable(rows, orders, tbl);
    }




    private void processOrder(int index,String type) throws SQLException, IOException {
        ArrayList<NameValuePair> nvp = new ArrayList<NameValuePair>();
        nvp.add(new BasicNameValuePair("status","Pending"));
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
