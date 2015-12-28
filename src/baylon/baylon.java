package baylon;
/**
 * Created by troll173 on 11/24/15.
 */

import baylon.app.Constants;
import baylon.app.Synch;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Timer;

public class baylon extends Application {

    public static void main(String[] args) {
        launch(args);
    }
   Constants constants = Constants.getInstance();
    @Override
    public void start(Stage primaryStage) {
        Parent root = null;
        try {
            Timer timer = new Timer();
            timer.schedule(new Synch(), 0, 15000);
        }catch (Exception e){
            e.printStackTrace();
        }

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
