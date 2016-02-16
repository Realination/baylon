package baylon.controllers;

import baylon.app.DataTable;
import baylon.models.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Troll173 on 1/30/2016.
 */
public class ViewWakeDetails {

    @FXML
    Label lblDeceased,lblCustomer,lblType,lblService,lblCasket,lblCasketCount;
    @FXML
    TableView tableServices;
    @FXML
    ImageView imgService;
    @FXML
    Pane detailsPane;
    @FXML
    Button btnProvide,btnProvideCasket;

    DataTable dataTable;

    Orders tblOrders = Orders.getInstance();
    Deceased tblDeceased = Deceased.getInstance();
    Customers tblCustomers = Customers.getInstance();
    Services tblServices = Services.getInstance();
    Suboptions tblSubs = Suboptions.getInstance();
    Caskets tblCaskets = Caskets.getInstance();
    Packages tblPackages = Packages.getInstance();
    ResultSet orders,deceased,customers,caskets,packages;

    String selectedService = "";
    String serviceString = "";
    String ordercode = "";
    int casketId = 0;

    public void init(String ordercode) throws SQLException {
        this.ordercode = ordercode;
        ArrayList<NameValuePair> where = new ArrayList<NameValuePair>();
        where.add(new BasicNameValuePair("ordercode",ordercode));
        orders = tblOrders.get(where);
        orders.first();
        deceased = tblDeceased.get(orders.getString("deceased"));
        deceased.first();
        customers = tblCustomers.get(orders.getString("customer"));
        customers.first();
        packages = tblPackages.get(orders.getString("package"));
        packages.first();
        caskets = tblCaskets.get(packages.getString("casket"));
        dataTable = new DataTable(tableServices);

        lblDeceased.setText(deceased.getString("lname") + "," + deceased.getString("fname") + " " + deceased.getString("mname") +" ");
        lblCustomer.setText(customers.getString("lastname") + "," + customers.getString("firstname") + " " + customers.getString("middlename") +" ");


        serviceString = orders.getString("services");
        String[] services = serviceString.split(",");
        loadServices(services);
    }

    private void loadServices(String[] services) throws SQLException {

        ArrayList<String> cols = new ArrayList<String>();
        cols.add("Service");
        cols.add("Type");
        cols.add("Required Qty");
        cols.add("Required Changes");
        cols.add("Remaining Changes");
        dataTable.setColumns(cols);
        caskets.first();
        casketId = Integer.parseInt(caskets.getString("id"));
        lblCasket.setText(caskets.getString("name"));
        lblCasketCount.setText(caskets.getString("qty"));
        orders.first();

        if(orders.getString("casket_provided").equalsIgnoreCase("1")){
            btnProvideCasket.setVisible(false);
        }else{
            btnProvideCasket.setVisible(true);
        }


        ObservableList data = FXCollections.observableArrayList();
        for (String service : services){
            String[] servData = service.split("-");
            ObservableList row = FXCollections.observableArrayList();
            ResultSet rService = tblServices.get(servData[2]);
            rService.first();
            ResultSet rType = tblSubs.get(servData[3]);
            rType.first();
            row.add(rService.getString("name"));
            if(servData[3].equalsIgnoreCase("0")){
                row.add("N/A");
            }else{
                row.add(rType.getString("name"));
            }
            row.add(servData[0] +" "+ rService.getString("unit")+"");
            row.add(servData[1]);
            row.add(servData[5]);
            data.add(row);
        }
        dataTable.init(data,"",false);


        dataTable.getOnDataSelected(event -> {
            selectedService = services[dataTable.getSelected()];
            String[] servData = selectedService.split("-");
            ResultSet rSubs = tblSubs.get(servData[3]);
            ResultSet rService = tblServices.get(servData[2]);
            Image img = new Image("/baylon/media/logopink.png");
            try {
                rService.first();
                rSubs.first();
                if(servData[3].equalsIgnoreCase("0")){
                    lblType.setText("N/A");
                }else{
                    img = new Image("/baylon/media/suboption/"+rSubs.getString("name")+".jpg");
                    lblType.setText(rSubs.getString("name"));
                }

                if(servData[5].equalsIgnoreCase("0")){
                    btnProvide.setVisible(false);
                }else{
                    btnProvide.setVisible(true);
                }

                lblService.setText(rService.getString("name"));
            } catch (SQLException e) {
                e.printStackTrace();
            }
            imgService.setImage(img);
            detailsPane.setVisible(true);
        });
    }


    @FXML
    void proviceService() throws SQLException {
        System.out.println(serviceString);
        String[] selServArr = selectedService.split("-");
        int newChanges = Integer.parseInt(selServArr[5]) - 1;
        String newSelService = selServArr[0]+"-"+selServArr[1]+"-"+selServArr[2]+"-"+selServArr[3]+"-"+selServArr[4]+"-"+newChanges+"-";

        String newServices = serviceString.replace(selectedService,newSelService);
        System.out.println(newServices);

        ArrayList<NameValuePair> data = new ArrayList<NameValuePair>();
        ArrayList<NameValuePair> where = new ArrayList<NameValuePair>();
        data.add(new BasicNameValuePair("services",newServices));
        where.add(new BasicNameValuePair("ordercode",ordercode));
        tblOrders.save(data,where);
        loadServices(newServices.split(","));
        detailsPane.setVisible(false);
    }


    @FXML
    public void provideCasket() throws SQLException {
        btnProvideCasket.setVisible(false);
        caskets.first();
        int newqty = caskets.getInt("qty") - 1;
        lblCasketCount.setText(newqty+"");
        ArrayList<NameValuePair> nvp = new ArrayList<>();
        nvp.add(new BasicNameValuePair("qty",newqty+""));
        ArrayList<NameValuePair> where = new ArrayList<>();
        where.add(new BasicNameValuePair("ordercode",ordercode));
        tblCaskets.save(nvp, casketId + "");
        nvp.clear();
        nvp.add(new BasicNameValuePair("casket_provided","1"));
        tblOrders.save(nvp,where);
    }

}
