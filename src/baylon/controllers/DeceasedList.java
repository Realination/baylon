package baylon.controllers;

import baylon.app.DataTable;
import baylon.models.Deceased;
import de.jensd.shichimifx.utils.SplitPaneDividerSlider;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

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
    @FXML
    SplitPane DeceasedSplit;
    @FXML
    AnchorPane newDeceasedAnchor;

    DataTable dataTable;
    public static SplitPaneDividerSlider leftSplitPaneDividerSlider;

    public void initialize() throws SQLException {

        leftSplitPaneDividerSlider = new SplitPaneDividerSlider(DeceasedSplit, 0, SplitPaneDividerSlider.Direction.UP);
        DeceasedSplit.setMinWidth(com.sun.glass.ui.Screen.getMainScreen().getWidth());
        newDeceasedAnchor.setMinWidth(com.sun.glass.ui.Screen.getMainScreen().getWidth());
        newDeceasedAnchor.setMinHeight(com.sun.glass.ui.Screen.getMainScreen().getHeight());

//        DeceasedSplit.setDividerPosition(1);
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

    @FXML
    void addNewDeceased(){
        leftSplitPaneDividerSlider.setAimContentVisible(false);
    }

    @FXML
    void ViewDeceased(){
        leftSplitPaneDividerSlider.setAimContentVisible(true);
    }

}
