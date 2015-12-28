package baylon.controllers;


import baylon.app.TableHelper;
import baylon.models.Users;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TouchEvent;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by troll173 on 11/25/15.
 */

public class UserMaintenanceController {
    /**
     * Initializes the controller class.
     */
    @FXML
    TableView usersMaintenanceTable;

    Users tblusers = new Users();
    ResultSet users;
     ObservableList<ObservableList> data;


    public void initialize() throws SQLException {

        users = tblusers.get();
        data = FXCollections.observableArrayList();

        ArrayList<String> cols = new ArrayList<String>();
        ArrayList rows = new ArrayList();
        cols.add("Firstname");
        rows.add("firstname");
        cols.add("Middlename");
        rows.add("middlename");
        cols.add("Lastname");
        rows.add("lastname");
        cols.add("Contact");
        rows.add("mobile");
        cols.add("Email");
        rows.add("email");
        cols.add("Last Login");
        rows.add("last_login");


        TableHelper.setColumns(cols, usersMaintenanceTable);
        TableHelper.populateTable(rows, users, usersMaintenanceTable);


        usersMaintenanceTable.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                    try {
                        editUser(usersMaintenanceTable.getSelectionModel().getSelectedIndex());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        usersMaintenanceTable.setOnTouchPressed(new EventHandler<TouchEvent>() {
            public void handle(TouchEvent event) {
                try {
                    editUser(usersMaintenanceTable.getSelectionModel().getSelectedIndex());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        });


    }

    private void editUser(int index) throws SQLException {
       users.first();
        int i = 0;
        while(i < index){
            users.next();
            i++;
        }
        System.out.println(users.getString("name"));
    }

}
