package baylon.controllers;

import baylon.models.Deceased;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Troll173 on 1/17/2016.
 */
public class ViewDeceasedDataController {
    @FXML
    Label lblHeader;
    @FXML
    Label lblName;

    Deceased tbldeceased = new Deceased();
    ResultSet deceased;

    public void initialize(String deceasedId){
        try {
            deceased = tbldeceased.get(deceasedId);
            deceased.first();
            lblHeader.setText(deceased.getString("lname")+ ","+deceased.getString("fname")+" "+deceased.getString("mname") +" ");
            lblName.setText(deceased.getString("lname")+ ","+deceased.getString("fname")+" "+deceased.getString("mname") +" ");

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }


}
