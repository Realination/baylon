package baylon.controllers;

import baylon.app.TableHelper;
import baylon.models.Deceased;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Troll173 on 1/15/2016.
 */
public class SelectDeceasedController {

    Deceased tbldeceased = new Deceased();
    ResultSet deceased;
    @FXML
    TableView tableDeceased;

    public void initialize() throws SQLException {
        System.out.println("Init");
        deceased = tbldeceased.get();
        deceased.first();

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
    }


    public void switchToNew(ActionEvent actionEvent) {
    }

    public void switchToExisting(ActionEvent actionEvent) {

    }
}
