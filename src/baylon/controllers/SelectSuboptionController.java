package baylon.controllers;


import baylon.app.Constants;
import baylon.app.Functions;
import baylon.models.Orders;
import baylon.models.Packages;
import baylon.models.Services;
import baylon.models.Suboptions;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by troll173 on 11/25/15.
 */

public class SelectSuboptionController {
    Suboptions tblsubs = new Suboptions();
    Services tblservices = new Services();
    Orders tblorders = new Orders();
    Packages tblpackages = new Packages();
     String data = "";
    Constants constants;
    ResultSet services,supoptions;
    Functions funcs = new Functions();
    String[] dataArr;
    boolean firstLoad = true;
    String[] loadedArr;

    @FXML
    Label lblHeader;
    @FXML
    public Label lbltype,lblunit,lblprice,lbltprice;
    @FXML
    GridPane gridTypes;
    @FXML
    public TextField txtqty,txtchanges;
    @FXML
    ImageView selectedImage;

    private static int serviceIndex;
    double alterval = 0;
    String currentSuboption;
    Timer timerQty;



    public void initialize(String data,int selectedIndex) throws SQLException {
        constants = Constants.getInstance();
        this.serviceIndex = selectedIndex;
        this.data = data;
        this.dataArr = data.split("-");
        this.loadedArr = data.split("-");
        initForm(data.split("-"));
    }





    void initForm(final String[] dataArr) throws SQLException {
        supoptions = tblsubs.get(dataArr[3]);
        supoptions.first();
        services = tblservices.get(dataArr[2]);
        services.first();
        ResultSet order = tblorders.get(constants.getValue("orderid"));
        order.first();
        
        lblHeader.setText(services.getString("name"));
        lbltype.setText(supoptions.getString("name"));
        this.currentSuboption = supoptions.getString("id");
        if(services.getString("type").equalsIgnoreCase("1")){
            txtqty.setText("1");
            txtchanges.setText("1");
            txtqty.setDisable(true);
            txtchanges.setDisable(true);
        }else{
            txtqty.setText(dataArr[0]);
            if(Integer.parseInt(dataArr[1]) < 1){
                txtchanges.setText("1");
            }else{
                txtchanges.setText(dataArr[1]);
            }

        }
        lblunit.setText(services.getString("unit"));
        lblprice.setText(funcs.toMoney(supoptions.getString("price")));

        double tprice = (Double.parseDouble(supoptions.getString("price")) * Double.parseDouble(dataArr[0])) * Double.parseDouble(dataArr[1]);
        System.out.println(Double.parseDouble(supoptions.getString("price")));
        lbltprice.setText(funcs.toMoney(tprice+""));

        Image image;
        try {
            image = new Image(getClass().getResourceAsStream("/baylon/media/suboption/"+supoptions.getString("name").trim()+".jpg"));
        }catch (Exception e){
            image = new Image(getClass().getResourceAsStream("/baylon/media/products/Bubble Top Flexy Glass.jpg"));
        }
        selectedImage.setImage(image);

        generateAlternateTypes(dataArr[2],dataArr[3]);


        String packageId = order.getString("package");
        ResultSet defPackage = tblpackages.get(packageId);
        defPackage.first();
        String[] oldServ = defPackage.getString("services").split(",");

        String[] dServData;
        try {
            String currServ = oldServ[serviceIndex];
            dServData = currServ.split("-");
        }catch (Exception e){
            String CustomServiceString = "1-1-"+loadedArr[2]+"-"+loadedArr[3];
            dServData = CustomServiceString.split("-");
        }

        final String[] servData = dServData;
        timerQty = new Timer();

        txtqty.textProperty().addListener(new ChangeListener<String>() {
            public void changed(final ObservableValue<? extends String> observable, final String oldValue, final String newValue) {
                timerQty.cancel();
                timerQty = new Timer();
                System.out.println("changed- "+newValue);
                timerQty.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        checkQty(newValue,servData[0]);
                    }
                },400);

            }
        });

        txtchanges.textProperty().addListener(new ChangeListener<String>() {
            public void changed(final ObservableValue<? extends String> observable, final String oldValue, final String newValue) {
                timerQty.cancel();
                timerQty = new Timer();
                timerQty.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        checkChanges(newValue,servData[1]);
                    }
                },400);
            }
        });




    }

    void checkQty(String newValue,String minValue){
        if(Integer.parseInt(newValue) < Integer.parseInt(minValue)){
            txtqty.setText(minValue);
        }else{
            try {
                qtychange();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    void checkChanges(String newValue,String minValue){
        if(Integer.parseInt(newValue) < Integer.parseInt(minValue)){
            txtchanges.setText(minValue);
        }else{
            try {
                qtychange();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }


    private void generateAlternateTypes(String id,String type) throws SQLException {
        ArrayList<NameValuePair> where = new ArrayList<NameValuePair>();
        where.add(new BasicNameValuePair("service",id));
        ResultSet alternateTypes = tblsubs.get(where);

        int col = 0;
        int row = 0;
        while (alternateTypes.next()){
            if(!alternateTypes.getString("id").equalsIgnoreCase(type)){
            Image image;
            Label lblprice = new Label(funcs.toMoney(alternateTypes.getString("price")));
                lblprice.setPrefWidth(109);
                lblprice.setPrefHeight(10);
            Button btn = new Button(alternateTypes.getString("name"));

            try {
                image = new Image(getClass().getResourceAsStream("/baylon/media/suboption/"+alternateTypes.getString("name").trim()+".jpg"));
            }catch (Exception e){
                image = new Image(getClass().getResourceAsStream("/baylon/media/products/Bubble Top Flexy Glass.jpg"));
            }
            ImageView imgview = new ImageView(image);
            imgview.setFitWidth(109);
            imgview.setFitHeight(98);
            btn.setGraphic(imgview);
                final String tempid = alternateTypes.getString("id");
                btn.setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent e) {
                        try {
                            changeType(tempid);
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
                    }
                });
            btn.setPrefWidth(gridTypes.getPrefWidth());
            btn.setPrefHeight(gridTypes.getPrefHeight());
            btn.setMnemonicParsing(false);
            gridTypes.add(lblprice,col,row);
            gridTypes.add(btn,col,row);
            System.out.println(col+"-"+row);
            if(col == 1){
                col = 0;
                row++;
            }else{
                col++;
            }
        }
        }
        qtychange();
    }

    private void changeType(String tempid) throws SQLException {
//        System.out.println(tempid);
        String qty = txtqty.getText().trim();
        String changes = txtchanges.getText().trim();
        String service = services.getString("id");
        String suboption = tempid;
        this.currentSuboption = tempid;
        String newdata = qty+"-"+changes+"-"+service+"-"+suboption+"-"+qty+"-"+changes;
//        System.out.println(newdata);
        String[] mydataArr = newdata.split("-");
        this.dataArr = mydataArr;
        this.data = newdata;
//        addPrice(newdata);
        initForm(mydataArr);
    }




    @FXML
    void confirmChange() throws IOException, SQLException {
        System.out.println(data);

        editService();

        Stage stage = (Stage) lblHeader.getScene().getWindow();
        stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
        stage.close();
    }


    void editService() throws SQLException {
        ResultSet order = tblorders.get(constants.getValue("orderid"));
        order.first();
        String[] oldServicesArr = order.getString("services").split(",");
        oldServicesArr[serviceIndex] = data;
        String newdata = funcs.implode(",",oldServicesArr);
        ArrayList<NameValuePair> ndata = new ArrayList<NameValuePair>();
        ndata.add(new BasicNameValuePair("services",newdata));

        tblorders.save(ndata,constants.getValue("orderid"));

    }




    void qtychange() throws SQLException {

        if(txtqty.getText().equalsIgnoreCase("")){
            txtqty.setText("1");
        }
        if(txtchanges.getText().equalsIgnoreCase("")){
            txtchanges.setText("1");
        }
        String qty = txtqty.getText().trim();
        String changes = txtchanges.getText().trim();
        String service = services.getString("id");
        String suboption = this.currentSuboption;
        String newdata = qty+"-"+changes+"-"+service+"-"+suboption+"-"+qty+"-"+changes;
        String[] mydataArr = newdata.split("-");
        this.dataArr = mydataArr;
        this.data = newdata;
        System.out.println("call qtychange");
        if(firstLoad){
            firstLoad = false;
        }else{
            addPrice(newdata);
        }

//        editService();
    }


    void addPrice(String newdata) throws SQLException {
        String[] newdArr = newdata.split("-");
        double displayServicePrice = 0;
        ResultSet order = tblorders.get(constants.getValue("orderid"));
        order.first();
        ResultSet suboption = tblsubs.get(newdArr[3]);
        suboption.first();
        ResultSet packages = tblpackages.get(order.getString("package"));
        packages.first();


        String[] packServices = packages.getString("services").split(",");

        String[] packData;
        try {
             packData = packServices[serviceIndex].split("-");
        }catch (Exception e){
            String CustomServiceString = "1-1-"+loadedArr[2]+"-"+loadedArr[3];
            packData = CustomServiceString.split("-");
        }


        ResultSet oldSuboption = tblsubs.get(packData[3]);
        oldSuboption.first();


        int oldQty = 1;
        int oldChanges = 1;



        double servicePrice = 0;
        System.out.println("packageSub="+packData[3]+" newSub="+newdArr[3]);
        ResultSet loadedSuboption = tblsubs.get(loadedArr[3]);
        loadedSuboption.first();
        if(packData[3].equalsIgnoreCase(newdArr[3])){
            if(loadedArr[3].equalsIgnoreCase(packData[3])){
                servicePrice = suboption.getDouble("price");
            }else{

                if(loadedSuboption.getDouble("price") > oldSuboption.getDouble("price")){
                    servicePrice = Double.parseDouble("-"+loadedSuboption.getDouble("price"));
                }else{
                    servicePrice = suboption.getDouble("price");
                }

            }

        }else{
            if(oldSuboption.getDouble("price") < suboption.getDouble("price")){
                servicePrice = suboption.getDouble("price");
            }else{
                servicePrice = oldSuboption.getDouble("price") - suboption.getDouble("price");
            }

        }
        System.out.println("servicePrice="+servicePrice);
        displayServicePrice = suboption.getDouble("price");
        System.out.println("oldPrice="+oldSuboption.getDouble("price"));
        System.out.println("newPrice="+suboption.getDouble("price"));
        try {
            if(loadedArr[1].equals("0")){
                oldChanges = 1;
            }else{
                oldChanges = Integer.parseInt(loadedArr[1]);
            }

            if(loadedArr[0].equals("0")){
                oldQty = 1;
            }else{
                oldQty = Integer.parseInt(loadedArr[0]);
            }



        }catch (Exception e){
             oldChanges = 1;
        }
        int oldTotal = oldQty * oldChanges;
        System.out.println("Suboptions ="+packData[3]+"-"+newdArr[3]);

        int addedTotal = 0;
        if(packData[3].equalsIgnoreCase(newdArr[3])){
            if(loadedArr[3].equalsIgnoreCase(newdArr[3])){
                addedTotal = (Integer.parseInt(newdArr[0]) *  Integer.parseInt(newdArr[1])) - oldTotal;
            }else{
                addedTotal = (Integer.parseInt(newdArr[0]) *  Integer.parseInt(newdArr[1]));
            }
        }else{
            addedTotal = Integer.parseInt(txtqty.getText()) *  Integer.parseInt(txtchanges.getText());
        }
        System.out.println("addedTotal="+addedTotal);

        final double displayPrice =  displayServicePrice * Integer.parseInt(newdArr[0]) *  Integer.parseInt(newdArr[1]);
        double addedPrice = servicePrice * addedTotal;



        Platform.runLater(new Runnable() {
            public void run() {
                lbltprice.setText(funcs.toMoney(displayPrice+""));
            }
        });

        double packagePrice = order.getDouble("custom_price");


        System.out.println("addedprice="+addedPrice);
        System.out.println("packagePrice="+packagePrice);

        double newPrice = 0;
//        try {
//            String dummy = packServices[serviceIndex];
            newPrice = packagePrice + addedPrice;
//        }catch (Exception e){
//            newPrice = packagePrice + addedPrice - servicePrice;
//        }

        System.out.println("newprice="+newPrice);
        ArrayList<NameValuePair> nvp = new ArrayList<NameValuePair>();
        nvp.add(new BasicNameValuePair("custom_price",newPrice+""));
        tblorders.save(nvp,constants.getValue("orderid"));

    }


}
