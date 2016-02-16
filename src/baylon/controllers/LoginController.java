/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package baylon.controllers;

import baylon.app.Constants;
import baylon.app.md5;
import baylon.models.Admin;
import com.sun.glass.ui.Screen;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author DarkMatter
 */
public class LoginController implements Initializable {

    @FXML
    TextField txtuser,txtpass;
    @FXML
    Button btnlogin,btncancel;
    @FXML
     Label lblError;
    @FXML
     Pane panelLogin;       
    @FXML 
    AnchorPane mainPanel;
    @FXML
    ImageView imgLogo;
    @FXML
    ImageView gifBG;

    Admin admin = new Admin();
    Constants contstants;
    /**
     * Initializes the controller class.
     */
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        panelLogin.setLayoutX((Screen.getMainScreen().getWidth() / 2 + 124) - (mainPanel.getPrefWidth() / 2));
        System.out.println(mainPanel.getPrefWidth() / 2);
        panelLogin.setLayoutY(Screen.getMainScreen().getHeight()/ 2 - 150);

        gifBG.fitWidthProperty().bind(mainPanel.widthProperty());
        gifBG.fitHeightProperty().bind(mainPanel.heightProperty());


        contstants = Constants.getInstance();
    }    
 
    public void login() throws SQLException, IOException{
        String pass = md5.encode(txtpass.getText());
        String user = txtuser.getText();
        ArrayList<NameValuePair> nvp = new ArrayList<NameValuePair>();
        nvp.add(new BasicNameValuePair("username", user));
        nvp.add(new BasicNameValuePair("password", pass));
        ResultSet result = admin.get(nvp);
        
                int size = 0;
       
              result.last();
              size = result.getRow();
              result.first();
         
        if(size > 0){
            System.out.println("login");
            contstants.addValue("logged_user",result.getString("name"));
            contstants.addValue("admin_id",result.getString("id"));
            contstants.addValue("user_level",result.getString("position"));
             openDashboard();
        }else{
            System.out.println("error");
            lblError.setVisible(true);
        }
    }
    
    
    public void openDashboard() throws IOException{


                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/baylon/views/Dashboard.fxml"));
                Parent root = fxmlLoader.load();
                Stage stage = (Stage) btnlogin.getScene().getWindow();
                stage.close();
                stage.setScene(new Scene(root));
                stage.setWidth(Screen.getMainScreen().getWidth());
                stage.setHeight(Screen.getMainScreen().getHeight());
                stage.setMaximized(true);
//                stage.setFullScreen(true);
                stage.show();
    }
    
    public void cancel(){
         Stage stage = (Stage) btncancel.getScene().getWindow();
    // do what you have to do
         stage.close();
    }
}
