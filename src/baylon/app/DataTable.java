package baylon.app;

import com.sun.glass.ui.Screen;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Troll173 on 1/30/2016.
 */
public class DataTable {

    private TableView table;
    private Boolean hasRecordCount = true;
    private AnchorPane parent;
    private Double x,y;
    private Label lblCount = new Label();


    public DataTable(TableView table) {
        this.table = table;
    }



    public void getOnDataSelected(EventHandler<Event> var){
        table.onMousePressedProperty().set(var);
        table.onTouchPressedProperty().set(var);
    }


    public void init(ObservableList data,String searchPrompt,boolean hasRecCount) {

        this.hasRecordCount = hasRecCount;

        TextField txtSearch = new TextField();
        txtSearch.setPromptText(searchPrompt);
        txtSearch.setPrefWidth(200);



        parent = (AnchorPane) table.getParent();
        x = table.getLayoutX();
        y = table.getLayoutY();
        Double leftDistance = Screen.getMainScreen().getWidth() - txtSearch.getPrefWidth() - (table.boundsInParentProperty().getValue().getMinX() * 2);



        txtSearch.setLayoutX(leftDistance);
        txtSearch.setLayoutY(y - 40);
//        txtSearch.widthProperty().


        System.out.println(table.boundsInParentProperty().getValue());

        if(searchPrompt.equalsIgnoreCase("")){
            txtSearch.setVisible(false);
        }else{
            txtSearch.setVisible(true);
        }

        parent.getChildren().add(txtSearch);
        ObservableList oldData = data;
        table.getItems().removeAll();
        table.setItems(oldData);
        txtSearch.textProperty().addListener(new ChangeListener<String>() {
            public void changed(final ObservableValue<? extends String> observable, final String oldValue, final String newValue) {
                ObservableList newData = FXCollections.observableArrayList();
                for (Object datum : oldData) {

                    if (datum.toString().toUpperCase().contains(newValue.toUpperCase())) {
                        newData.addAll(datum);
                    }
                }
                System.out.println(oldData + " =" + newValue);
                table.getItems().removeAll();
                table.setItems(newData);
                setRecordCout(newData.size());

            }
        });
        
        setRecordCout(oldData.size());
    }

    private void setRecordCout(int count) {
        if(hasRecordCount){
            lblCount.setText("Showing "+count+" Records.");
            lblCount.getStyleClass().add("h4");
            lblCount.setVisible(true);
            lblCount.setLayoutX(x);
            lblCount.setLayoutY(y - 50);
            if(!parent.getChildren().contains(lblCount)){
                parent.getChildren().remove(lblCount);
            }
              try{
                  parent.getChildren().add(lblCount);
              }catch (Exception e){

              }

        }
    }


    public void setColumns(ArrayList<String> columnNames){
        table.getColumns().clear();
        for(int i=0 ; i<columnNames.size(); i++){
            //We are using non property style for making dynamic table
            final int j = i;
            TableColumn col = new TableColumn(columnNames.get(i));
            col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){
                public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                    return new SimpleStringProperty(param.getValue().get(j).toString());
                }
            });
            col.setPrefWidth(200);

            table.getColumns().addAll(col);

        }
    }

    public int getSelected(){
        return table.getSelectionModel().getSelectedIndex();
    }

    public String getSelected(ResultSet rs,String primaryKey){
        String selId = "";
        try {
            rs.beforeFirst();
            int c = 0;
            while (c <= table.getSelectionModel().getSelectedIndex()){
                rs.next();
                c++;
            }
            selId = rs.getString(primaryKey);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return selId;
    }



}
