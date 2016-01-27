package baylon.controllers;

import baylon.app.Constants;
import baylon.models.Orders;
import com.sun.glass.ui.Screen;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Troll173 on 1/17/2016.
 */
public class PaymentMethodController {

    Constants constants;

    Orders tblorders = new Orders();
    ArrayList<NameValuePair> nvp = new ArrayList<NameValuePair>();
    @FXML
    Button btnPaypal;

    public void initialize(){
        constants = Constants.getInstance();
    }

    @FXML
    void selectCash(){
        nvp.add(new BasicNameValuePair("payment_method","Cash"));
        reviewOrder();
    }

    @FXML
    void selectBank(){
        nvp.add(new BasicNameValuePair("payment_method","Bank"));
        reviewOrder();
    }

    @FXML
    void selectPaypal(){
        nvp.add(new BasicNameValuePair("payment_method","Paypal"));
        reviewOrder();
    }


    void reviewOrder(){
        tblorders.save(nvp,constants.getValue("orderid"));


        Stage stage = (Stage) btnPaypal.getScene().getWindow();
        stage.close();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/baylon/views/ReviewOrder.fxml"));
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
        stage.setTitle("Baylon | Review Order");

        stage.show();
    }
}
