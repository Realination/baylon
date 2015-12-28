package baylon.controllers;

import baylon.models.Packages;
import com.sun.glass.ui.Screen;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by troll173 on 11/25/15.
 */
public class OrderingController {
    /**
     * Initializes the controller class.
     */
    @FXML
    GridPane packagesGrid;

    private Packages tblpackages = new Packages();

    public void initialize() throws SQLException {
        // TODO

        final ResultSet packages = tblpackages.get();

        int col = 0;
        int row = 0;
        while(packages.next()){
            Button btn = new Button(packages.getString("name"));
            Image image;
            try {
                image = new Image(getClass().getResourceAsStream("/baylon/media/products/"+packages.getString("name").trim()+".jpg"));
            }catch (Exception e){
                image = new Image(getClass().getResourceAsStream("/baylon/media/products/Bubble Top Flexy Glass.jpg"));
            }
            ImageView imgview = new ImageView(image);
            System.out.println("/baylon/media/products/"+packages.getString("name")+".jpg");
            imgview.setFitWidth(157);
            imgview.setFitHeight(140);
            btn.setGraphic(imgview);
            final String tempid = packages.getString("id");
            btn.setOnAction(new EventHandler<ActionEvent>() {
                 public void handle(ActionEvent e) {
                     try {
                         openPackage(tempid);
                     } catch (IOException e1) {
                         e1.printStackTrace();
                     }
                 }
            });
            btn.setMnemonicParsing(false);
            btn.setPrefWidth(packagesGrid.getPrefWidth());
            btn.setPrefHeight(packagesGrid.getPrefHeight());

            packagesGrid.add(btn,col,row);
//            System.out.println(Settings.api+"assets/img/products/stainless.jpg");tprice
            if(col == 3){
                col = 0;
                row++;
            }else{
                col++;
            }
        }




    }


    private void openPackage(String id) throws IOException {
        System.out.println(id);
        Stage stage = new Stage();


        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/baylon/views/ViewPackage.fxml"));
        Parent root = fxmlLoader.load();
        ViewPackageController controller = fxmlLoader.getController();

        stage.setScene(new Scene(root));
        try {
            controller.setId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        stage.setWidth(Screen.getMainScreen().getWidth()*0.70);
        stage.setHeight(Screen.getMainScreen().getHeight()*0.70);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Baylon | View Package");
        stage.show();


    }

}
