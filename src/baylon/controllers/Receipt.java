package baylon.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

/**
 * Created by Troll173 on 1/30/2016.
 */
public class Receipt {
    @FXML
    AnchorPane anchorMain;
    public void initialize(){

    }

    public AnchorPane getParent(){
        return this.anchorMain;
    }
}
