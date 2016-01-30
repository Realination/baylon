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
import javafx.print.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.transform.Scale;
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
    public void init(String ordecode) throws SQLException {

            NumberBox numberBox = new NumberBox(txtPayment);
        this.ordercode = ordecode;

        leftSplitPaneDividerSlider = new SplitPaneDividerSlider(mainSplit, 0, SplitPaneDividerSlider.Direction.UP);
        mainSplit.setMinWidth(com.sun.glass.ui.Screen.getMainScreen().getWidth());


        dataTable = new DataTable(tablePayments);
        load();
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

            if(amountPaid > remaining){
                double change = amountPaid - remaining;
                nvp.add(new BasicNameValuePair("amount",remaining+""));
                lblChange.setVisible(true);
                lblChangeData.setText(Functions.toMoney(change+""));
            }else{
                nvp.add(new BasicNameValuePair("amount",amountPaid+""));
                lblChange.setVisible(false);
                lblChangeData.setText("Payment Received");
            }
            nvp.add(new BasicNameValuePair("method","Cash"));
            tblPayments.save(nvp);

            try {
                load();
                lblChange.setVisible(false);
                lblChangeData.setText("");
                txtPayment.setText("");
                printReceipt();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }else{
            lblChange.setVisible(false);
            lblChangeData.setText("Insufficient Payment");
        }



    }


    void printReceipt(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/baylon/views/Receipt.fxml"));
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Receipt controller = fxmlLoader.getController();
        controller.initialize();
        Scene scene = new Scene(root);
        WritableImage snapshot = scene.snapshot(null);

        Stage stage = new Stage();
        print(snapshot,stage);
    }


    public void print(WritableImage writableImage, Stage primaryStage) {
        ImageView imageView =new ImageView(writableImage);
        Printer printer = Printer.getDefaultPrinter();
        PageLayout pageLayout = printer.createPageLayout(Paper.A4, PageOrientation.PORTRAIT, Printer.MarginType.HARDWARE_MINIMUM);
        double scaleX = pageLayout.getPrintableWidth() / imageView.getBoundsInParent().getWidth();
        double scaleY = pageLayout.getPrintableHeight() / imageView.getBoundsInParent().getHeight();
        imageView.getTransforms().add(new Scale(scaleX, scaleY));

        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null) {
            boolean successPrintDialog = job.showPrintDialog(primaryStage.getOwner());
            if(successPrintDialog){
                boolean success = job.printPage(pageLayout,imageView);
                if (success) {
                    job.endJob();
                }
            }
        }
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
