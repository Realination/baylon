package baylon.controllers;

import baylon.app.DataTable;
import baylon.app.Form;
import baylon.models.Deceased;
import de.jensd.shichimifx.utils.SplitPaneDividerSlider;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import org.apache.http.NameValuePair;

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
    AnchorPane newDeceasedAnchor,AnchorForm;
    @FXML
    ComboBox sex,autopsied,death_certificate,service_contract;

    DataTable dataTable;
    Form form;
    public static SplitPaneDividerSlider leftSplitPaneDividerSlider;

    public void initialize() throws SQLException {
        form = new Form(AnchorForm);


        ObservableList<String> yesORNo = FXCollections.observableArrayList("Yes", "No");
        ObservableList<String> Gender = FXCollections.observableArrayList("Male", "Female");
        sex.setItems(Gender);
        autopsied.setItems(yesORNo);
        death_certificate.setItems(yesORNo);
        service_contract.setItems(yesORNo);

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

        tableInit();


    }

    void tableInit() throws SQLException {
        ObservableList data = FXCollections.observableArrayList();
        deceased.first();
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

    @FXML
    void saveDeceased(){

        ArrayList<NameValuePair> data = form.getData();
        System.out.println(data);
        tbldeceased.save(data);

        leftSplitPaneDividerSlider.setAimContentVisible(true);
        try {
            tableInit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
