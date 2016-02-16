package baylon.controllers;

import baylon.app.DataTable;
import baylon.app.Functions;
import baylon.app.NumberBox;
import baylon.models.Deceased;
import baylon.models.Orders;
import baylon.models.Payments;
import de.jensd.shichimifx.utils.SplitPaneDividerSlider;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Created by Troll173 on 1/30/2016.
 */
public class WakePayment {
    Payments tblPayments = Payments.getInstance();
    Orders tblorders = Orders.getInstance();
    Deceased tbldeceased = Deceased.getInstance();
    ResultSet payments,orders,deceased;

    @FXML
    ProgressBar progPrint;
    @FXML
    AnchorPane anchorPrint;
    @FXML
    TableView tablePayments,tableOnline;
    @FXML
    Label lblPaid,totalBill,lbldeceased,lblRemaining,lblChange,lblChangeData;
//    @FXML
//    AnchorPane ChangeDisplay;
    @FXML
    TextField txtPayment;
    @FXML
    SplitPane mainSplit;
    DataTable dataTable,dataOnline;
    public static SplitPaneDividerSlider leftSplitPaneDividerSlider;
    double remaining = 0;
    String ordercode = "";
    double total = 0;
    public void init(String ordecode) throws SQLException {

            NumberBox numberBox = new NumberBox(txtPayment);
        this.ordercode = ordecode;

        leftSplitPaneDividerSlider = new SplitPaneDividerSlider(mainSplit, 0, SplitPaneDividerSlider.Direction.UP);
        mainSplit.setMinWidth(com.sun.glass.ui.Screen.getMainScreen().getWidth());

        System.out.println("wake Payments");

        dataTable = new DataTable(tablePayments);
        dataOnline = new DataTable(tableOnline);
        loadOnlinePayments();
        load();
    }

    private void loadOnlinePayments() {
        ArrayList<NameValuePair> where = new ArrayList<NameValuePair>();
        where.add(new BasicNameValuePair("ordercode",ordercode));
        where.add(new BasicNameValuePair("status","Pending"));
        payments = tblPayments.get(where);
        ArrayList<String> ocols = new ArrayList<String>();
        ocols.add("Transaction #");
        ocols.add("Method");
        ocols.add("Date");
        dataOnline.setColumns(ocols);
        try {
            payments.beforeFirst();
            ObservableList data = FXCollections.observableArrayList();
            while (payments.next()){
                ObservableList row = FXCollections.observableArrayList();
                System.out.println("Payments= "+payments.getString("transnum"));

                row.add(payments.getString("transnum")+"");
                row.add(payments.getString("method")+"");
                row.add(payments.getString("datePaid")+"");
                data.add(row);
            }
            dataOnline.init(data,"",false);

        } catch (SQLException e) {
            e.printStackTrace();
        }




        dataOnline.getOnDataSelected(event -> {
            ArrayList<NameValuePair> where2 = new ArrayList<NameValuePair>();
            where2.add(new BasicNameValuePair("ordercode",ordercode));
            where2.add(new BasicNameValuePair("status","Pending"));
            payments = tblPayments.get(where2);
            String selected = dataOnline.getSelected(payments, "uid");
            ResultSet selectedPay = tblPayments.get(selected);

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            try {
                selectedPay.first();
                alert.setTitle(selectedPay.getString("transnum")+"");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            alert.setHeaderText("What do you want to do?");

            ButtonType btnValidate = new ButtonType("Validate");
            ButtonType btnConfirm = new ButtonType("Confirm");
            ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(btnValidate, btnConfirm, buttonTypeCancel);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == btnValidate) {

            } else if (result.get() == btnConfirm) {
                ArrayList<NameValuePair> data = new ArrayList<NameValuePair>();
                data.add(new BasicNameValuePair("status","Confirmed"));
                tblPayments.save(data,selected);
                loadOnlinePayments();
                try {
                    load();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        });

    }


    void load() throws SQLException {

        ArrayList<NameValuePair> where = new ArrayList<NameValuePair>();
        where.add(new BasicNameValuePair("ordercode",ordercode));
        orders = tblorders.get(where);
        where.add(new BasicNameValuePair("status","Confirmed"));
        payments = tblPayments.get(where);

        orders.first();
        deceased = tbldeceased.get(orders.getString("deceased"));
        deceased.first();

        lbldeceased.setText(deceased.getString("lname")+ ","+deceased.getString("fname")+" "+deceased.getString("mname") +" ");
        Double paid = tblPayments.getSum(where,"amount");
        lblPaid.setText(Functions.toMoney(paid+""));
        ArrayList<String> cols = new ArrayList<String>();
        cols.add("Amount");
        cols.add("Payment Method");
        cols.add("Date Paid");

        dataTable.setColumns(cols);

        totalBill.setText(Functions.toMoney(orders.getString("custom_price")));
        total = orders.getDouble("custom_price");
        remaining = orders.getDouble("custom_price") - paid;
        lblRemaining.setText(Functions.toMoney(remaining+""));
        payments.beforeFirst();
        ObservableList data = FXCollections.observableArrayList();
        while(payments.next()){
            ObservableList row = FXCollections.observableArrayList();
            row.add(Functions.toMoney(payments.getString("amount")));
            row.add(payments.getString("method"));
            row.add(payments.getString("datePaid"));
            data.add(row);
        }

        dataTable.init(data,"",false);
    }


    @FXML
    void AcceptPayment(){

        if(!txtPayment.getText().isEmpty()){


            ArrayList<NameValuePair> nvp = new ArrayList<NameValuePair>();
            nvp.add(new BasicNameValuePair("ordercode",ordercode));
            double amountPaid = Double.parseDouble(txtPayment.getText().toString());
            double change = 0;
            if(amountPaid > remaining){
                 change = amountPaid - remaining;
                nvp.add(new BasicNameValuePair("amount",remaining+""));
                lblChange.setVisible(true);
                lblChangeData.setText(Functions.toMoney(change+""));
            }else{

                nvp.add(new BasicNameValuePair("amount",amountPaid+""));
                lblChange.setVisible(false);
                lblChangeData.setText("Payment Received");
            }
            nvp.add(new BasicNameValuePair("method","Cash"));
            nvp.add(new BasicNameValuePair("status","Confirmed"));
            tblPayments.save(nvp);

            nvp.clear();
            nvp.add(new BasicNameValuePair("ordercode",ordercode));
            ResultSet newOrd = tblorders.get(nvp);
            nvp.clear();
            if(amountPaid >= remaining) {
                try {
                    newOrd.first();
                    if (newOrd.getString("status").equalsIgnoreCase("Partial")) {
                        nvp.add(new BasicNameValuePair("status", "Paid"));
                    } else if (newOrd.getString("status").equalsIgnoreCase("Balance")) {
                        nvp.add(new BasicNameValuePair("status", "Complete"));
                    }
                    tblorders.save(nvp, newOrd.getString("id"));
                } catch (SQLException e) {
                    e.printStackTrace();
                }


                try {
                    load();
                    lblChange.setVisible(false);
                    lblChangeData.setText("");
                    txtPayment.setText("");
                    printReceipt(ordercode, total + "", amountPaid + "", change + "");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }else{
                try {
                    load();
                    lblChange.setVisible(true);
                    printReceipt(ordercode, total + "", amountPaid + "", change + "");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }else{
            lblChange.setVisible(false);
            lblChangeData.setText("Insufficient Payment");
        }



    }


    void printReceipt(String ordercode,String total,String paid,String change){
        anchorPrint.setVisible(true);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/baylon/views/Receipt.fxml"));
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Receipt controller = fxmlLoader.getController();
        controller.initialize(ordercode,total,paid,change);
        Scene scene = new Scene(root);
        WritableImage snapshot = scene.snapshot(null);
        progPrint.setProgress(snapshot.getProgress());
        Stage stage = new Stage();
//        stage.setScene(scene);
//        stage.show();
        Functions.print(snapshot, stage);

        anchorPrint.setVisible(false);
        progPrint.setProgress(0.0);
    }




    @FXML
    void OnlinePayments(){
        leftSplitPaneDividerSlider.setAimContentVisible(false);
    }

    @FXML
    void  CashPayments(){
        leftSplitPaneDividerSlider.setAimContentVisible(true);
    }

}
