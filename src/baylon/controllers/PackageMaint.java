package baylon.controllers;

import baylon.app.DataTable;
import baylon.app.Functions;
import baylon.models.Packages;
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
 * Created by Troll173 on 2/5/2016.
 */
public class PackageMaint {

    @FXML
    TableView tablePackages;

    @FXML
    SplitPane PackageSplit;

    DataTable dataTable;

    Packages tblpackages = Packages.getInstance();
    ResultSet packages;

    public static SplitPaneDividerSlider leftSplitPaneDividerSlider;



    public void initialize() throws SQLException {
        dataTable = new DataTable(tablePackages);

        leftSplitPaneDividerSlider = new SplitPaneDividerSlider(PackageSplit, 0, SplitPaneDividerSlider.Direction.UP);
        PackageSplit.setMinWidth(com.sun.glass.ui.Screen.getMainScreen().getWidth());



        packages = tblpackages.get();

        ArrayList<String> cols = new ArrayList<String>();
        cols.add("Name");
        cols.add("Price");
        cols.add("Description");
        dataTable.setColumns(cols);

        ObservableList data = FXCollections.observableArrayList();
        packages.first();

        while (packages.next()){
            ObservableList row = FXCollections.observableArrayList();
            row.add(packages.getString("name"));
            row.add(Functions.toMoney(packages.getString("price")));
            row.add(packages.getString("description"));
            data.add(row);
        }
        dataTable.init(data,"Search Package..",true);





    }


}
