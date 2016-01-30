package baylon.app;

/**
 * Created by Troll173 on 1/30/2016.
 */

import javafx.scene.*;
import javafx.scene.control.*;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class Form {

    private Parent parent;
    private List<Node> txtFields;
    private ArrayList<NameValuePair> data = new ArrayList<NameValuePair>();
    private boolean validated = true;
    public ArrayList<String> errorFields = new ArrayList<String>();
    private Label lblError;

    public Form(Parent parent) {
        this.parent = parent;
        txtFields = parent.getChildrenUnmodifiable();

    }


    public ArrayList<NameValuePair> getData(){
        for (Node txtField: txtFields){
            if(txtField.getStyleClass().contains("required")){
                if (txtField.getClass().toString().equalsIgnoreCase("class javafx.scene.control.TextField")){
                    TextField txt = (TextField)txtField;
                    if (txt.getText().toString().isEmpty()) {
                        errorFields.add(txt.getId() + "");
                        validated = false;
                    }else{
                        data.add(new BasicNameValuePair(txt.getId() + "", txt.getText().toString()));
                    }
                }else if (txtField.getClass().toString().equalsIgnoreCase("class javafx.scene.control.ComboBox")){
                    ComboBox txt = (ComboBox)txtField;
                    if (txt.getValue().toString().isEmpty()) {
                       errorFields.add(txt.getId() + "");
                        validated = false;
                    }else{
                        data.add(new BasicNameValuePair(txt.getId()+"",txt.getValue().toString()));
                    }
                }else if (txtField.getClass().toString().equalsIgnoreCase("class javafx.scene.control.DatePicker")){
                    DatePicker txt = (DatePicker)txtField;
                    if (txt.getValue().toString().isEmpty()) {
                       errorFields.add(txt.getId() + "");
                        validated = false;
                    }else{
                        data.add(new BasicNameValuePair(txt.getId()+"",txt.getValue().toString()));
                    }
                } else if (txtField.getClass().toString().equalsIgnoreCase("class javafx.scene.control.TextArea")){
                    TextArea txt = (TextArea)txtField;
                    if (txt.getText().toString().isEmpty()) {
                       errorFields.add(txt.getId() + "");
                        validated = false;
                    }else{
                        data.add(new BasicNameValuePair(txt.getId()+"",txt.getText().toString()));
                    }

                }
            }else{
                if (txtField.getClass().toString().equalsIgnoreCase("class javafx.scene.control.TextField")){
                    TextField txt = (TextField)txtField;
                    data.add(new BasicNameValuePair(txt.getId()+"",txt.getText().toString()));
                }else if (txtField.getClass().toString().equalsIgnoreCase("class javafx.scene.control.ComboBox")){
                    ComboBox txt = (ComboBox)txtField;
                    data.add(new BasicNameValuePair(txt.getId()+"",txt.getValue().toString()));
                }else if (txtField.getClass().toString().equalsIgnoreCase("class javafx.scene.control.DatePicker")){
                    DatePicker txt = (DatePicker)txtField;
                    data.add(new BasicNameValuePair(txt.getId()+"",txt.getValue().toString()));
                } else if (txtField.getClass().toString().equalsIgnoreCase("class javafx.scene.control.TextArea")){
                    TextArea txt = (TextArea)txtField;
                    data.add(new BasicNameValuePair(txt.getId()+"",txt.getText().toString()));
                }

            }

        }


        if (validated){
            lblError.setText("");
            return data;
        }else{
            displayError();
            validated = true;
            return null;
        }


    }

    private void displayError(){
        String errorMsg = "Invalid value for: "+errorFields.toString();
        lblError.setText(errorMsg);
    }

    public void setAsErrorDisplay(Label lbl){
        this.lblError = lbl;
    }

}
