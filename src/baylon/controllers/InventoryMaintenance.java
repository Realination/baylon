package baylon.controllers;

import baylon.app.DataTable;
import baylon.models.Caskets;
import baylon.models.Inventory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Troll173 on 2/5/2016.
 */
public class InventoryMaintenance {

    @FXML
    TableView tableInventory,tableCaskets;

    Inventory tblInventory = Inventory.getInstance();
    Caskets tblCaskets = Caskets.getInstance();
    ResultSet inventory,caskets;

    DataTable dtInventory,dtCaskets;

    public void initialize() throws SQLException {
        dtInventory = new DataTable(tableInventory);
        dtCaskets =  new DataTable(tableCaskets);

        ArrayList<String> cols = new ArrayList<String>();
        cols.add("Item Name");
        cols.add("Quantity");
        dtInventory.setColumns(cols);


        ArrayList<String> cols2 = new ArrayList<String>();
        cols2.add("Name");
        cols2.add("Category");
        cols2.add("Price");
        cols2.add("Quantity");
        dtCaskets.setColumns(cols2);

        initMaterials();
        initCaskets();
    }

    private void initCaskets() throws SQLException{
        caskets = tblCaskets.get();
        ObservableList data = FXCollections.observableArrayList();
        caskets.beforeFirst();
        while (caskets.next()){
            ObservableList row = FXCollections.observableArrayList();
            row.add(caskets.getString("name"));
            row.add(caskets.getString("category"));
            row.add(caskets.getString("price"));
            row.add(caskets.getString("qty"));
            data.add(row);
        }
        dtCaskets.init(data,"Search Caskets",true);

        caskets.beforeFirst();
        dtCaskets.getOnDataSelected(event -> {
            String selected = dtCaskets.getSelected(caskets,"id");
            System.out.println(selected);
        });


    }

    private void initMaterials() throws SQLException{
        inventory = tblInventory.get();
        ObservableList data = FXCollections.observableArrayList();
        inventory.beforeFirst();
        while (inventory.next()){
            ObservableList row = FXCollections.observableArrayList();
            row.add(inventory.getString("name"));
            row.add(inventory.getString("qty"));
            data.add(row);
        }
        dtInventory.init(data,"Search Inventory",true);
        dtInventory.getOnDataSelected(event ->{

        });
    }

    public void switchToNew(ActionEvent actionEvent) {
    }

    public void switchToExisting(ActionEvent actionEvent) {

    }
}
