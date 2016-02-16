package baylon.app;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Troll173 on 2/9/2016.
 */
public class DataSelect {

    public ComboBox cb;
    public HashMap<String,String> options = new HashMap<String,String>();
    ObservableList optList = FXCollections.observableArrayList();

    public DataSelect(ComboBox cb) {
        this.cb = cb;
    }

    private ObservableList getOL(){
        ObservableList ol = FXCollections.observableArrayList();
        Iterator entries = options.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry thisEntry = (Map.Entry) entries.next();
            Object key = thisEntry.getKey();
            ol.add(key);
        }
        return ol;
    }



    public void addOption(String key,String value){
        options.putIfAbsent(key,value);
        cb.getItems().removeAll();
        cb.setItems(getOL());
    }

    public void setOptions(HashMap<String,String> hm){
        options.clear();
        cb.getItems().removeAll();
        Iterator entries = hm.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry thisEntry = (Map.Entry) entries.next();
            String key = thisEntry.getKey().toString();
            String value = thisEntry.getValue().toString();
            options.put(key,value);
        }
        cb.getItems().removeAll();
        cb.setItems(getOL());
    }

    public String getSelectedKey(){
        String rValue = options.get(cb.getValue().toString());
        return rValue;
    }

    public String getSelectedValue(){
        return cb.getValue().toString();
    }



}
