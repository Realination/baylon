package baylon;
/**
 * Created by troll173 on 11/24/15.
 */

import baylon.app.*;
import baylon.models.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Timer;

public class baylon extends Application {

    public static void main(String[] args) {
        launch(args);
    }
   Constants constants = Constants.getInstance();

    Misc tblmisc = Misc.getInstance();

    @Override
    public void start(Stage primaryStage) throws IOException, SQLException {
        ResultSet misc = tblmisc.get("2");
        misc.first();
        Backup backup = new Backup(misc.getString("data"),15000);

        Parent root = null;

        Synch orders = new Synch(Orders.getInstance(),15000);
        Synch deceased = new Synch(Deceased.getInstance(),15000);
        Synch customers = new Synch(Customers.getInstance(),15000);
        Synch payments = new Synch(Payments.getInstance(),15000);
        Synch pickups = new Synch(Pickup.getInstance(),15000);


        constants.addValue("netStat","Offline");


        try {
            root = FXMLLoader.load(getClass().getResource("views/Login.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scene scene = new Scene(root);
        primaryStage.setFullScreen(true);
        primaryStage.setMaximized(true);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Baylon | Admin Application");
        primaryStage.show();
    }





}
