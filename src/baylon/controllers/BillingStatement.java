package baylon.controllers;

import baylon.app.Functions;
import baylon.models.Customers;
import baylon.models.Orders;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Troll173 on 2/12/2016.
 */
public class BillingStatement {
    Orders tblorders = Orders.getInstance();
    Customers tblcustomers = Customers.getInstance();
    ResultSet orders,customers;

    @FXML
    Label lblAccount,lblCustomer,lblAddress,lblAddress2,lblAddress3,lblBill,lblTotalBill,lblTotalBill1;
    public void init(String ordercode){
        ArrayList<NameValuePair> where = new ArrayList<NameValuePair>();
        where.add(new BasicNameValuePair("ordercode",ordercode));
        orders = tblorders.get(where);
        try {
            orders.first();
            customers = tblcustomers.get(orders.getString("customer"));
            customers.first();
            lblAccount.setText(customers.getString("uid")+"");
            lblCustomer.setText(customers.getString("firstname")+" "+customers.getString("middlename")+" "+customers.getString("lastname"));
            lblAddress.setText(customers.getString("address")+" ");
            lblAddress2.setText(customers.getString("municipality")+" "+customers.getString("province"));
            lblAddress3.setText(customers.getString("region")+" ");

            lblBill.setText(Functions.toMoney(orders.getString("custom_price")));
            lblTotalBill.setText(Functions.toMoney(orders.getString("custom_price")));
            lblTotalBill1.setText(Functions.toMoney(orders.getString("custom_price")));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
