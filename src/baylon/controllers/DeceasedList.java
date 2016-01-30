package baylon.controllers;

import baylon.app.DataTable;
import baylon.app.TableHelper;
import baylon.models.Deceased;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Thomas on 29/01/2016.
 */
public class DeceasedList
{
    Deceased tbldeceased = new Deceased();
    ResultSet deceased;
    @FXML
    TableView tabledeceased;

    DataTable dataTable;

    public void initialize() throws SQLException {
        deceased = tbldeceased.get();
        dataTable = new DataTable(tabledeceased);
        ArrayList<String> cols = new ArrayList<String>();
        cols.add("Name of Deceased");
        cols.add("Gender");
        cols.add("Age");
        cols.add("Height");
        cols.add("Weight");
        cols.add("Date of Death");
        cols.add("Cause of Death");

        dataTable.setColumns(cols);
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
        dataTable.init(data,"Search Deceased",true);


    }
}
