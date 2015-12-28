package baylon.controllers;


import baylon.app.BaylonFunctions;
import baylon.app.Constants;
import baylon.app.Functions;
import baylon.app.TableHelper;
import baylon.models.Orders;
import baylon.models.Packages;
import baylon.models.Services;
import baylon.models.Suboptions;
import com.sun.glass.ui.Screen;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TouchEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by troll173 on 11/25/15.
 */

public class ViewPackageController {
    String id;
    Packages tblpackages = new Packages();
    Services tblservices = new Services();
    Suboptions tblsubs = new Suboptions();
    Orders tblorders = new Orders();
    ResultSet packages,rservices,suboptions;
    Constants constants;
    @FXML
    Label lblHeader,lblpname,lbldesc,lblprice;
    @FXML
    TableView tblServices;
    @FXML
    Button btnConfirm,btnAddService;
    String newServices =  "";
    BaylonFunctions funcs = new BaylonFunctions();
    public void initialize() {

    }

    public void reload() throws SQLException {
        constants = Constants.getInstance();
        ResultSet currentOrder = tblorders.get(constants.getValue("orderid"));
        currentOrder.first();
//        setColumns();
        this.newServices = currentOrder.getString("services");
        populateTable(newServices);


        lblprice.setText(Functions.toMoney(currentOrder.getString("custom_price")));

        System.out.println("reloaded"+currentOrder.getString("services"));
        initListeners();
    }

    public void setId(String id) throws SQLException {

        this.id = id;
        packages = tblpackages.get(id);
        constants = Constants.getInstance();
        try {
            packages.first();
            lblHeader.setText(packages.getString("name"));
            lblpname.setText(packages.getString("name"));
            lbldesc.setText(packages.getString("description"));
            lblprice.setText(Functions.toMoney(packages.getString("price")));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        setColumns();
        addToOrders();
        populateTable(newServices);
        initListeners();
    }

    private void initListeners() {
        tblServices.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                    try {
                        editService(tblServices.getSelectionModel().getSelectedIndex());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        tblServices.setOnTouchPressed(new EventHandler<TouchEvent>() {
            public void handle(TouchEvent event) {
                try {
                    editService(tblServices.getSelectionModel().getSelectedIndex());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });


    }

    private void editService(int selectedIndex) throws IOException {
        String[] servArr = newServices.split(",");

        String[] servdata = servArr[selectedIndex].split("-");
        if(!servdata[3].equalsIgnoreCase("0")){


            Stage stage = new Stage();


            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/baylon/views/SelectSuboption.fxml"));
            Parent root = fxmlLoader.load();
            SelectSuboptionController controller = fxmlLoader.getController();

            stage.setScene(new Scene(root));

            System.out.println(servArr[selectedIndex]);
            try {
                controller.initialize(servArr[selectedIndex],selectedIndex);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        stage.setWidth(Screen.getMainScreen().getWidth()*0.70);
        stage.setHeight(Screen.getMainScreen().getHeight()*0.70);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Baylon | Select Type");
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent event) {
                System.out.println("editing closed!");
                try {
                    reload();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        stage.show();

        }
    }

    private void addToOrders() throws SQLException {
        try {
            packages.first();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String ordercode = "MGB-"+ Calendar.getInstance().get(Calendar.YEAR)+"-"+Functions.randomString(9);

        String[] services = packages.getString("services").split(",");
        int si = 0;
        for (String service: services){
            String[] vals = service.split("-");
            newServices += service+"-"+vals[0]+"-"+vals[1];
            if(si < services.length-1){
                newServices+=",";
            }
            si++;
        }


        ArrayList<NameValuePair> nvp = new ArrayList<NameValuePair>();
        nvp.add(new BasicNameValuePair("ordercode",ordercode));
        nvp.add(new BasicNameValuePair("order_type","Walk-In"));
        nvp.add(new BasicNameValuePair("package",packages.getString("id")));
        nvp.add(new BasicNameValuePair("status","Processing"));
        nvp.add(new BasicNameValuePair("price",packages.getString("price")));
        nvp.add(new BasicNameValuePair("custom_price",packages.getString("price")));
        nvp.add(new BasicNameValuePair("secretary",constants.getValue("admin_id")));
        nvp.add(new BasicNameValuePair("services",newServices));
        int recId = tblorders.save(nvp);
            if(funcs.SyncAdd(nvp, "custpack", "" + recId)){

            }

        System.out.println("Add Package");
        constants.addValue("ordercode",ordercode);
        constants.addValue("orderid",recId+"");
    }

    private void setColumns() {
        ArrayList<String> cols = new ArrayList<String>();
        cols.add("Service");
        cols.add("Qty");
        cols.add("Changes");
        cols.add("Type");

        TableHelper.setColumns(cols,tblServices);
    }


    private void populateTable(String services) throws SQLException {
        System.out.println(services);
        String[] sArray = services.split(",");
        ObservableList data = FXCollections.observableArrayList();
        for (String service : sArray){
            ObservableList row = FXCollections.observableArrayList();
            String[] det = service.split("-");
            rservices = tblservices.get(det[2]);
            rservices.first();

            if(!det[2].equalsIgnoreCase("0")){
                row.add(rservices.getString("name"));
            }else{
                row.add("N/A");
            }
            if(!det[0].equalsIgnoreCase("0")){
                row.add(det[0]+" "+rservices.getString("unit"));
            }else{
                row.add("N/A");
            }
            if(!det[1].equalsIgnoreCase("0")){
                row.add(det[1]);
            }else{
                row.add("N/A");
            }
            if(!det[3].equalsIgnoreCase("0")){
                suboptions = tblsubs.get(det[3]);
                suboptions.first();
                row.add(suboptions.getString("name"));
            }else{
                row.add("N/A");
            }


            data.add(row);
        }
        tblServices.setItems(data);
    }


    @FXML
    void addService() throws IOException {

        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/baylon/views/AddService.fxml"));
        Parent root = fxmlLoader.load();
        AddServiceController controller = fxmlLoader.<AddServiceController>getController();
        stage.setScene(new Scene(root));
//        controller.initialize();
        stage.setWidth(Screen.getMainScreen().getWidth()*0.70);
        stage.setHeight(Screen.getMainScreen().getHeight()*0.70);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Baylon | Add Service");
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent event) {
                System.out.println("editing closed!");
                try {
                    reload();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        stage.show();
    }



    @FXML
    void cancelOrder(){
        ArrayList<NameValuePair> where = new ArrayList<NameValuePair>();
        where.add(new BasicNameValuePair("ordercode",constants.getValue("ordercode")));
        tblorders.remove(where);
        try {
            funcs.SyncDelete(where, "custpack");
        }catch (Exception e){
            e.printStackTrace();
        }
        Stage stage = (Stage) lblpname.getScene().getWindow();
        stage.close();
    }

    @FXML
    void confirmOrder(){

    }
}
