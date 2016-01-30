/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package baylon.controllers;

import baylon.app.Constants;
import de.jensd.shichimifx.utils.SplitPaneDividerSlider;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

/**
 * FXML Controller class
 *
 * @author DarkMatter
 */
public class DashboardController implements Initializable {

 
    @FXML
    SplitPane dashoardSplit;
    @FXML 
    GridPane mmenuPane;
    @FXML
     Pane contentPane;
    @FXML
    Button btnBackToMenu;
    @FXML
    AnchorPane contentContainer;
    @FXML
    AnchorPane bannerPane;
    @FXML
    ComboBox cboxUser;
    @FXML
    AnchorPane menuAnchorPane;
    @FXML
    AnchorPane dashboardPanel;
    @FXML
    Label lblnetStats;
    @FXML
    Circle statsCircle;
    public static SplitPaneDividerSlider leftSplitPaneDividerSlider;
    Constants constants;
    /**
     * Initializes the controller class.
     */

    Timer t = new Timer();

    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                constants = Constants.getInstance();
                if(constants.getValue("netStat").equalsIgnoreCase("Online")){
                    statsCircle.setFill(Color.GREEN);
//                    lblnetStats.setText("Online");
                }else{
                    statsCircle.setFill(Color.RED);
//                    lblnetStats.setText("Offline");
                }
                System.out.println("checkstats-"+constants.getValue("netStat"));
            }
        },0,15000);


        leftSplitPaneDividerSlider = new SplitPaneDividerSlider(dashoardSplit, 0, SplitPaneDividerSlider.Direction.LEFT);
        dashoardSplit.setMinWidth(com.sun.glass.ui.Screen.getMainScreen().getWidth());
        dashoardSplit.setMinWidth(com.sun.glass.ui.Screen.getMainScreen().getWidth());
        contentPane.setPrefWidth(0.0);
        dashoardSplit.setDividerPosition(1,1);
        menuAnchorPane.setMinWidth(com.sun.glass.ui.Screen.getMainScreen().getWidth());
        constants = Constants.getInstance();
        String me = "";

        ObservableList<String> options = FXCollections.observableArrayList("Profile", "Settings", "Logout");

        cboxUser.setPromptText(constants.getValue("logged_user"));
        cboxUser.setMinWidth(Region.USE_PREF_SIZE);
        cboxUser.setItems(options);

        cboxUser.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {

            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                System.out.println(cboxUser.getItems().get((Integer) number2));
            }
        });
    }



    @FXML
    public void close(){
        Stage stage = (Stage) mmenuPane.getScene().getWindow();
        // do what you have to do
        stage.close();
    }

    @FXML
    private void Ordering(ActionEvent event) throws IOException {
        leftSplitPaneDividerSlider.setAimContentVisible(false);
        OpenPage("/baylon/views/Ordering.fxml");
    }

    @FXML
    private void userMaintenance(){
        leftSplitPaneDividerSlider.setAimContentVisible(false);
        OpenPage("/baylon/views/UserMaintenance.fxml");
    }



    @FXML
    public void personelMaintenance(){
        leftSplitPaneDividerSlider.setAimContentVisible(false);
        OpenPage("/baylon/views/UserMaintenance.fxml");
    }


    @FXML
    public void pendingOrders(){
        leftSplitPaneDividerSlider.setAimContentVisible(false);
        OpenPage("/baylon/views/PendingOrders.fxml");
    }


    @FXML
    public void currentWakes(){
        leftSplitPaneDividerSlider.setAimContentVisible(false);
        OpenPage("/baylon/views/CurrentWakes.fxml");
    }

    @FXML
    public void DeceasedList()
    {
        leftSplitPaneDividerSlider.setAimContentVisible(false);
        OpenPage("/baylon/views/DeceasedList.fxml");
    }

    @FXML
    public void OpenPage(String FXMLFile) {

        //ChildNode child;
        try {

            URL url = getClass().getResource(FXMLFile);
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(url);
            fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
            AnchorPane page = fxmlLoader.load(url.openStream());
            page.setMinHeight(com.sun.glass.ui.Screen.getMainScreen().getHeight());
            page.setMinWidth(com.sun.glass.ui.Screen.getMainScreen().getWidth());
            contentPane.getChildren().clear();///name of pane where you want to put the fxml.
            contentPane.getChildren().add(page);
            btnBackToMenu.setVisible(true);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }



    @FXML
    public void closePane(){
        btnBackToMenu.setVisible(false);
        leftSplitPaneDividerSlider.setAimContentVisible(true);
    }



}
