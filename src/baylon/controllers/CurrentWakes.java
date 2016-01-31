package baylon.controllers;

import baylon.app.App;
import baylon.app.DataTable;
import baylon.app.Functions;
import baylon.models.Orders;
import baylon.models.Payments;
import com.sun.glass.ui.Screen;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Created by Troll173 on 1/29/2016.
 */
public class CurrentWakes {

    Orders tblorders = Orders.getInstance();
    Payments tblpayments = Payments.getInstance();
    ResultSet orders;
    App app = App.getInstance();

    @FXML
    TableView tableWakes;

    public void initialize() throws SQLException {
        ArrayList<NameValuePair> nvp = new ArrayList<NameValuePair>();
        nvp.add(new BasicNameValuePair("status","Processing"));
        nvp.add(new BasicNameValuePair("status","Paid"));
        nvp.add(new BasicNameValuePair("status","Transfered"));
        nvp.add(new BasicNameValuePair("status","Complete"));
        orders = tblorders.getNot(nvp);
        orders.first();

        DataTable dataTable = new DataTable(tableWakes);

        ArrayList<String> cols = new ArrayList<String>();
        cols.add("Ordercode");
        cols.add("Deceased");
        cols.add("Total Bill");
        cols.add("Paid");
        cols.add("Balance");
        cols.add("Order Status");
        dataTable.setColumns(cols);

        ObservableList data = FXCollections.observableArrayList();
        while (orders.next()){
            ObservableList row = FXCollections.observableArrayList();
            ArrayList<NameValuePair> nvpo = new ArrayList<NameValuePair>();
            nvpo.add(new BasicNameValuePair("ordercode",orders.getString("ordercode")+""));
            Double sum = tblpayments.getSum(nvpo,"amount");
            row.add(orders.getString("ordercode")+"");
            row.add(orders.getString("deceased")+"");
            row.add(Functions.toMoney(orders.getString("custom_price")+""));
            row.add(Functions.toMoney(sum+""));
            row.add("");
            row.add(orders.getString("status")+"");
            data.add(row);
        }

        dataTable.init(data, "Search Wake",true);

        dataTable.getOnDataSelected(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                selectOptions(tableWakes.getSelectionModel().getSelectedIndex());
            }
        });



}




    private void selectOptions(int selectedIndex) {
        System.out.println("selected index="+selectedIndex);
        try {
            orders.first();
            int c = 0;
            while (c <= selectedIndex){
                orders.next();
                c++;
            }
            String ordId = orders.getString("id");

        if(orders.getString("status").equalsIgnoreCase("Partial") || orders.getString("status").equalsIgnoreCase("Balance")){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle(orders.getString("ordercode")+"");
            alert.setHeaderText("What do you want to do?");

            ButtonType btnProcessWake = new ButtonType("Process Wake");
            ButtonType btnViewWake = new ButtonType("Add Payment");
            ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(btnProcessWake, btnViewWake, buttonTypeCancel);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == btnProcessWake) {
                wakeProcess(orders.getString("ordercode")+"");
            } else if (result.get() == btnViewWake) {
                wakePayment(orders.getString("ordercode")+"");
            }
        }else{
            wakePayment(orders.getString("ordercode")+"");
        }

        } catch (SQLException e) {
            e.printStackTrace();
        }



    }

    private void wakeProcess(String ordercode) {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/baylon/views/ViewWakeDetails.fxml"));
        Functions.openScene(fxmlLoader,stage,"Baylon | Process  Wake",true,true);
        System.out.println("Select");
    }

    private void wakePayment(String ordercode) {


        Stage stage = new Stage();


        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/baylon/views/WakePayment.fxml"));
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        WakePayment controller = fxmlLoader.getController();

        stage.setScene(new Scene(root));
        try {
            System.out.print(ordercode);
            controller.init(ordercode);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        stage.setWidth(Screen.getMainScreen().getWidth());
        stage.setHeight(Screen.getMainScreen().getHeight());
//        stage.initStyle(StageStyle.UNDECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setMaximized(true);
        stage.setTitle("Baylon | Wake Payment");
        stage.show();

    }


}
