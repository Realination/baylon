package baylon.controllers;

import baylon.app.Functions;
import baylon.models.Customers;
import baylon.models.Orders;
import baylon.models.Services;
import baylon.models.Suboptions;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Troll173 on 1/30/2016.
 */
public class Receipt {
    @FXML
    AnchorPane anchorMain;
    @FXML
    VBox vQty,vChanges,vServices,vType,vPrice;
    @FXML
    Orders tblOrders = new Orders();
    Customers tblCustomers = new Customers();
    Services tblServices = new Services();
    Suboptions tblSubs = new Suboptions();
    ResultSet orders,customers,services,suboptions;

    @FXML
    Label lblname,lbladdress,lblmobile,lbltotal,lblrecieve,lblchange,lblcompany;

    public void initialize(String ordercode,String total,String received,String change){
        ArrayList<NameValuePair> where = new ArrayList<NameValuePair>();
        where.add(new BasicNameValuePair("ordercode",ordercode));
        orders = tblOrders.get(where);



        try {
            orders.first();
            String customerId = orders.getString("customer");
            customers = tblCustomers.get(customerId);
            customers.first();

            String strServices = orders.getString("services");
            String customerName = customers.getString("lastname")+", "+customers.getString("firstname")+" "+customers.getString("middlename");
            String address = customers.getString("address");
            String mobile = customers.getString("mobile");
            String company = customers.getString("company");

            lblname.setText(customerName);
            lbladdress.setText(address);
            lblmobile.setText(mobile);
            lblcompany.setText(company);
            lbltotal.setText(total);
            lblrecieve.setText(received);
            lblchange.setText(change);

            String[] servArr = strServices.split(",");
            for (String servData: servArr){
                String[] servDataArr = servData.split("-");
                services = tblServices.get(servDataArr[2]);
                services.first();
                vQty.getChildren().add(new Label(servDataArr[0] + " " + services.getString("unit")));
                vChanges.getChildren().add(new Label(servDataArr[1]+" "));
                vServices.getChildren().add(new Label(services.getString("name")+""));

                if(servDataArr[3].equalsIgnoreCase("0")){
                    vType.getChildren().add(new Label("N/A"));
                    vPrice.getChildren().add(new Label(Functions.toMoney(services.getString("price"))+""));
                }else{
                    suboptions = tblSubs.get(servDataArr[3]);
                    suboptions.first();
                    vType.getChildren().add(new Label(suboptions.getString("name")+""));
                    vPrice.getChildren().add(new Label(Functions.toMoney(suboptions.getString("price")+"")));
                }

            }



        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
