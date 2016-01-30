package baylon.controllers;


import baylon.app.Constants;
import baylon.app.Functions;
import baylon.app.TableHelper;
import baylon.models.Orders;
import baylon.models.Services;
import baylon.models.Suboptions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TouchEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by troll173 on 11/25/15.
 */



public class AddServiceController {

    @FXML
    Button btnClose;
    @FXML
    TableView tblServices;
    ResultSet services;

    Constants constants;

    Services recservices = new Services();
    Suboptions recsubs = new Suboptions();
    Orders recorders = new Orders();
    public void initialize() throws SQLException {
        constants = Constants.getInstance();
        ResultSet orders = recorders.get(constants.getValue("orderid"));
        orders.first();
        String[] servs = orders.getString("services").split(",");
        ArrayList<NameValuePair> whereNot = new ArrayList<NameValuePair>();
        for (String servz : servs){
            String[] servzArr = servz.split("-");
            ResultSet tmpservs = recservices.get(servzArr[2]);
            tmpservs.first();
            if(tmpservs.getString("type").equalsIgnoreCase("1")){
                whereNot.add(new BasicNameValuePair("id",servzArr[2]));
            }
        }
        String[] xservices = orders.getString("services").split(",");
        services = recservices.getNot(whereNot);
        services.first();
        ArrayList<String> cols = new ArrayList<String>();
        cols.add("Name");
        cols.add("Type");
        cols.add("Price");
        TableHelper.setColumns(cols,tblServices);
        ObservableList data = FXCollections.observableArrayList();
        while (services.next()){
            ArrayList<NameValuePair> where2 = new ArrayList<NameValuePair>();
            where2.add(new BasicNameValuePair("service",services.getString("id")));
            ResultSet subs = recsubs.get(where2,"'price' asc");
            ObservableList row = FXCollections.observableArrayList();
            row.add(services.getString("name"));
            if(subs.first()){
                row.add(subs.getString("name"));
                row.add(Functions.toMoney(subs.getString("price")));
            }else{
                row.add("N/A");
                row.add(Functions.toMoney(services.getString("price")));
            }

            data.add(row);
        }
        tblServices.setItems(data);
        initListeners();
    }



    private void initListeners() {
        tblServices.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                    try {
                        selectService(tblServices.getSelectionModel().getSelectedIndex());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    tblServices.setOnTouchPressed(new EventHandler<TouchEvent>() {
            public void handle(TouchEvent event) {
                try {
                    selectService(tblServices.getSelectionModel().getSelectedIndex());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        });


    }



    void selectService(int index) throws SQLException {
        services.first();
        int i = 0;
        while (i <= index){
            services.next();
            i++;
        }
        int id = Integer.parseInt(services.getString("id"));
        ResultSet selectedService = recservices.get(id+"");
        selectedService.first();
        ArrayList<NameValuePair> where3 = new ArrayList<NameValuePair>();
        where3.add(new BasicNameValuePair("service",id+""));
        ResultSet subs = recsubs.get(where3,"'price' asc");
        String subid = "0";
        double subprice = 0;
        if(subs.first()){
            subid = subs.getString("id");
            subprice = subs.getDouble("price");
        }else{
            subprice = selectedService.getDouble("price");
        }
        String addedService = "";
        if(selectedService.getString("unit").equalsIgnoreCase("")){
            addedService = ",0-0-"+id+"-"+subid+"-0-0";
        }else{
            addedService = ",1-0-"+id+"-"+subid+"-1-0";
        }
        System.out.println(addedService);

        ResultSet orders = recorders.get(constants.getValue("orderid"));
        orders.first();
        double newPrice = orders.getDouble("custom_price") + subprice;
        String newServices = orders.getString("services")+ addedService;
       ArrayList<NameValuePair> newdata = new ArrayList<NameValuePair>();
        newdata.add(new BasicNameValuePair("custom_price",newPrice+""));
        newdata.add(new BasicNameValuePair("services",newServices+""));
        recorders.save(newdata,constants.getValue("orderid"));
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
        stage.close();
    }

    @FXML
    void closeStage(){
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }
}
