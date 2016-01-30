package baylon.controllers;


import baylon.app.Constants;
import baylon.app.Functions;
import baylon.models.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static java.lang.Integer.*;


public class ProcessPaymentController {
    /**
     * Initializes the controller class.
     */
    @FXML
    Label lblHeader,lblCustomer,lblOrderType,lblDateOrdered,lblSecretary,lblBill,lblChange,lbllblChange,lblDown,lblDeceased;
    @FXML
    TextField txtPaid;
    @FXML
    Button btnPay;

    private ResultSet record;
    Users tblusers = new Users();
    Admin tbladmin = new Admin();
    Orders orders = new Orders();
    Payments tblpayments = new Payments();
    Deceased tbldeceased = new Deceased();
    ResultSet users,admins,deceased;

    Constants constants = Constants.getInstance();

    Double down;

    public void initialize() throws SQLException {
//

        txtPaid.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {
            public void handle(KeyEvent keyEvent) {
                if (!"0123456789.".contains(keyEvent.getCharacter())) {
                    keyEvent.consume();
                }
            }
        });
    }


    public void setRecord(ResultSet record) throws SQLException {
        this.record = record;
        System.out.println(record);
        lblHeader.setText("Process Order: "+record.getString("ordercode"));

        if(record.getString("secretary") != null){
            if(!record.getString("secretary").equalsIgnoreCase("NULL")){
            if(parseInt(record.getString("secretary"))>0 ){
                admins = tbladmin.get(record.getString("secretary"));
                admins.first();
                lblSecretary.setText(admins.getString("name"));
            }

            }
        }else{
            lblSecretary.setText("N/A");
        }

        if(record.getString("customer") != null){
           if(parseInt(record.getString("customer"))>0 ){
               users = tblusers.get(record.getString("customer"));
               users.first();
               lblCustomer.setText(users.getString("lastname")+", "+users.getString("firstname"));
           }
        }else{
            lblCustomer.setText("N/A");
        }

        deceased = tbldeceased.get(record.getString("deceased"));
        deceased.first();
        lblOrderType.setText(record.getString("order_type"));
        lblDateOrdered.setText(record.getString("created_at"));
        lblBill.setText(Functions.toMoney(record.getString("price")));
        down = record.getDouble("price")/2;
        lblDown.setText(Functions.toMoney(down+""));
        lblDeceased.setText(deceased.getString("lname")+", "+deceased.getString("fname")+" "+deceased.getString("mname"));
    }

    public void receivePayment(ActionEvent actionEvent) throws SQLException {
        System.out.println(txtPaid.getText());
        double paid = Double.parseDouble(txtPaid.getText());
        double bill = Double.parseDouble(record.getString("price"));
        if(down > paid){
            lblChange.setText("Not Enough Payment!!!");
            lbllblChange.setVisible(false);
            lblChange.setVisible(true);
        }else{
            double change =  paid - bill;
            lblChange.setText(Functions.toMoney(""+change));
            lbllblChange.setVisible(true);
            lblChange.setVisible(true);
            txtPaid.setDisable(true);
            btnPay.setDisable(true);
            ArrayList<NameValuePair> nvp = new ArrayList<NameValuePair>();

            Double amountPaid = 0.0;
            if(paid >= bill){
                amountPaid = bill;
                nvp.add(new BasicNameValuePair("status","Paid"));
            }else{
                amountPaid = paid;
                nvp.add(new BasicNameValuePair("status","Partial"));
            }

            orders.save(nvp,record.getString("id"));
            nvp.clear();


            nvp.add(new BasicNameValuePair("ordercode",record.getString("ordercode")));
            nvp.add(new BasicNameValuePair("amount",amountPaid+""));
            tblpayments.save(nvp);

        }

    }
}
