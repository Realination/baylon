package baylon.controllers;

import baylon.app.Constants;
import baylon.app.TableHelper;
import baylon.models.Deceased;
import baylon.models.Orders;
import com.sun.glass.ui.Screen;
import de.jensd.shichimifx.utils.SplitPaneDividerSlider;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Created by Troll173 on 1/15/2016.
 */
public class SelectDeceasedController {

    Deceased tbldeceased = new Deceased();
    Orders tblorders = new Orders();
    ResultSet deceased;
    @FXML
    TableView tableDeceased;
    @FXML
    SplitPane deceasedSplitPane;
    Constants constants;

    public static SplitPaneDividerSlider leftSplitPaneDividerSlider;

    public void initialize() throws SQLException {
        constants = Constants.getInstance();
        System.out.println("Init");
        deceased = tbldeceased.get();
        deceased.first();

        leftSplitPaneDividerSlider = new SplitPaneDividerSlider(deceasedSplitPane, 0, SplitPaneDividerSlider.Direction.LEFT);
        deceasedSplitPane.setMinWidth(com.sun.glass.ui.Screen.getMainScreen().getWidth());
        deceasedSplitPane.setMinWidth(com.sun.glass.ui.Screen.getMainScreen().getWidth());
        deceasedSplitPane.setDividerPosition(1,1);

        ArrayList<String> cols = new ArrayList<String>();
        cols.add("Name");
        cols.add("Sex");
        cols.add("Age");
        cols.add("Height");
        cols.add("Weight");
        cols.add("Date of Death");
        cols.add("Cause of Death");
        TableHelper.setColumns(cols,tableDeceased);

        ObservableList data = FXCollections.observableArrayList();

        while (deceased.next()){
            ObservableList row = FXCollections.observableArrayList();
            row.add(deceased.getString("lname")+ ","+deceased.getString("fname")+" "+deceased.getString("mname") +" ");
            row.add(deceased.getString("sex")+" ");
            row.add(deceased.getString("age")+" ");
            row.add(deceased.getString("height")+" ");
            row.add(deceased.getString("weight")+" ");
            row.add(deceased.getString("date_death") + " " + deceased.getString("time_death")+" ");
            row.add(deceased.getString("cause_of_death")+" ");
            data.add(row);
        }
        tableDeceased.getItems().setAll(data);

        tableDeceased.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                    selectDeceased(tableDeceased.getSelectionModel().getSelectedIndex());
                }
            }
        });

        tableDeceased.setOnTouchPressed(event -> selectDeceased(tableDeceased.getSelectionModel().getSelectedIndex()));
    }

    private void selectDeceased(int index) {
        try {
            deceased.first();
            int c = 0;
            while (c <= index){
                deceased.next();
                c++;
            }

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle(deceased.getString("lname")+ ","+deceased.getString("fname")+" "+deceased.getString("mname") +" ");
            alert.setHeaderText("What do you want to do?");

            ButtonType btnSelectDead = new ButtonType("Select Deceased");
            ButtonType btnViewDead = new ButtonType("View Data");
            ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(btnSelectDead, btnViewDead, buttonTypeCancel);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == btnSelectDead){
                selectDeceased(deceased.getString("id"));
            } else if (result.get() == btnViewDead) {
                viewDeceased(deceased.getString("id"));
           }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

   void selectDeceased(String deceasedId){
        ArrayList<NameValuePair> nvp = new ArrayList<NameValuePair>();
        nvp.add(new BasicNameValuePair("deceased",deceasedId));
       tblorders.save(nvp,constants.getValue("orderid"));


       Stage stage = (Stage) tableDeceased.getScene().getWindow();
       stage.close();

       FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/baylon/views/PaymentMethod.fxml"));
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
       stage.setTitle("Baylon | Select Payment Method");
       stage.show();

   }



    void viewDeceased(String deceasedId){
        Stage stage = new Stage();
        stage.close();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/baylon/views/ViewDeceasedData.fxml"));
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ViewDeceasedDataController controller = fxmlLoader.getController();

        stage.setScene(new Scene(root));

        controller.initialize(deceasedId);

        stage.setWidth(Screen.getMainScreen().getWidth() * 0.70);
        stage.setHeight(Screen.getMainScreen().getHeight() * 0.70);
        stage.setMaximized(true);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Baylon | View Deceased");
        stage.show();

        stage.show();
    }


    public void switchToNew(ActionEvent actionEvent) {
        leftSplitPaneDividerSlider.setAimContentVisible(false);
    }

    public void switchToExisting(ActionEvent actionEvent) {
        leftSplitPaneDividerSlider.setAimContentVisible(true);

    }
}
