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
    VBox vOnlinePayments;
    @FXML
    TableView tablePayments;
    @FXML
    Label lblPaid,totalBill,lbldeceased,lblRemaining,lblChange,lblChangeData;
//    @FXML
//    AnchorPane ChangeDisplay;
    @FXML
    TextField txtPayment;
    @FXML
    SplitPane mainSplit;
    DataTable dataTable;
    public static SplitPaneDividerSlider leftSplitPaneDividerSlider;
    double remaining = 0;
    String ordercode = "";
    double total = 0;
    public void init(String ordecode) throws SQLException {

            NumberBox numberBox = new NumberBox(txtPayment);
        this.ordercode = ordecode;

        leftSplitPaneDividerSlider = new SplitPaneDividerSlider(mainSplit, 0, SplitPaneDividerSlider.Direction.UP);
        mainSplit.setMinWidth(com.sun.glass.ui.Screen.getMainScreen().getWidth());


        loadOnlinePayments();

        dataTable = new DataTable(tablePayments);
        load();
    }

    private void loadOnlinePayments() {
        ArrayList<NameValuePair> where = new ArrayList<NameValuePair>();
        where.add(new BasicNameValuePair("ordercode",ordercode));
        where.add(new BasicNameValuePair("status","Pending"));
        payments = tblPayments.get(where);
        try {
            payments.first();
            while (payments.next()){
                Label transNum = new Label(payments.getString("transnum")+" ("+payments.getString("method")+")");
                Button btnConfirm = new Button("Confirm");
                btnConfirm.getStyleClass().add("btn-success");
                HBox hbox = new HBox();
                hbox.getChildren().add(transNum);
                hbox.getChildren().add(btnConfirm);
                vOnlinePayments.getChildren().add(hbox);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    void load() throws SQLException {
        ArrayList<NameValuePair> where = new ArrayList<NameValuePair>();
        where.add(new BasicNameValuePair("ordercode",ordercode));
        payments = tblPayments.get(where);
        orders = tblorders.get(where);
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
            try {
                newOrd.first();
                if(newOrd.getString("status").equalsIgnoreCase("Partial")){
                    nvp.add(new BasicNameValuePair("status","Paid"));
                }else if(newOrd.getString("status").equalsIgnoreCase("Balance")){
                    nvp.add(new BasicNameValuePair("status","Complete"));
                }
                tblorders.save(nvp,newOrd.getString("id"));
            } catch (SQLException e) {
                e.printStackTrace();
            }


            try {
                load();
                lblChange.setVisible(false);
                lblChangeData.setText("");
                txtPayment.setText("");
                printReceipt(ordercode,total+"",amountPaid+"",change+"");
            } catch (SQLException e) {
                e.printStackTrace();
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
