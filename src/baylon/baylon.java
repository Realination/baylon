package baylon;
/**
 * Created by troll173 on 11/24/15.
 */

import baylon.app.App;
import baylon.app.Constants;
import baylon.app.Model;
import baylon.app.Synch;
import com.sun.org.apache.xml.internal.resolver.helpers.FileURL;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.Objects;
import java.util.Timer;
import java.util.stream.Stream;

public class baylon extends Application {

    public static void main(String[] args) {
        launch(args);
    }
   Constants constants = Constants.getInstance();
    App app;
    @Override
    public void start(Stage primaryStage) throws IOException {


        app = App.getInstance();






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
