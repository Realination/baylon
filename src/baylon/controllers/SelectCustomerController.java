package baylon.controllers;


import baylon.models.Customers;
import de.jensd.shichimifx.utils.SplitPaneDividerSlider;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by troll173 on 11/25/15.
 */

public class SelectCustomerController {

    @FXML
    SplitPane customerSplitPane;
    @FXML
    TableView tableCustomers;

    public static SplitPaneDividerSlider leftSplitPaneDividerSlider;
    Customers tblcustomers = new Customers();
    ResultSet customers;

    public void initialize() throws SQLException {
        leftSplitPaneDividerSlider = new SplitPaneDividerSlider(customerSplitPane, 0, SplitPaneDividerSlider.Direction.LEFT);
        customerSplitPane.setMinWidth(com.sun.glass.ui.Screen.getMainScreen().getWidth());
        customerSplitPane.setMinWidth(com.sun.glass.ui.Screen.getMainScreen().getWidth());
        customerSplitPane.setDividerPosition(1,1);



        ArrayList<String> cols = new ArrayList<String>();
        cols.add("Firstname");
        cols.add("Middlename");
        cols.add("Lastname");
        cols.add("Address");
        cols.add("Citizenship");

        TableColumn[] col = new TableColumn[5];
        col[0] = new TableColumn("Firstname");
        col[1] = new TableColumn("Firstname");
        col[2] = new TableColumn("Firstname");
        col[3] = new TableColumn("Firstname");
        col[4] = new TableColumn("Firstname");
        tableCustomers.getColumns().addAll(col);


        ObservableList data = FXCollections.observableArrayList();
        customers = tblcustomers.get();
        customers.first();
        while (customers.next()){
            ObservableList row = FXCollections.observableArrayList();
            row.add(customers.getString("firstname"));
            row.add(customers.getString("middlename"));
            row.add(customers.getString("lastname"));
            row.add(customers.getString("address")+" "+customers.getString("municipality")+ ", "+ customers.getString("province"));
            row.add(customers.getString("citizenship"));
            data.add(row);
        }
        System.out.println(data);
        tableCustomers.setItems(data);


    }

    @FXML
    void switchToNew(){
        leftSplitPaneDividerSlider.setAimContentVisible(false);
    }

    @FXML
    void switchToExisting(){
        leftSplitPaneDividerSlider.setAimContentVisible(true);
    }
}
