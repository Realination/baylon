package baylon.app;

import baylon.models.Deceased;
import baylon.models.Orders;
import baylon.models.Packages;
import baylon.models.Users;
import org.apache.http.HttpException;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TimerTask;

/**
 * Created by troll173 on 12/6/15.
 */
public class Synch extends TimerTask{
    HttpRequest httpq = new HttpRequest();
    ArrayList<NameValuePair> nvp = new ArrayList<NameValuePair>();
    HashMap<String,Model> models = new HashMap<String, Model>();

    @Override
    public void run() {
        System.out.println("check");
        models.put("customers",new Users());
        models.put("custpack",new Orders());
        models.put("deceased",new Deceased());
        models.put("packages",new Packages());
        try {
            checkTables();
        }catch (Exception e){
            System.out.println("no net!!!!");
        }
    }

    private void checkTables() throws URISyntaxException, HttpException {
        try {
            //customers
            checkAdd("customers");
            checkEdit("customers");
            //custpack
            checkAdd("custpack");
            checkEdit("custpack");
//            //deceased
            checkAdd("payments");
            checkEdit("payments");
            //packages
//            checkAdd("packages");
//            checkEdit("packages");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("nonnet!!!!");
        }

    }

    private void checkEdit(String table) throws SQLException, URISyntaxException, HttpException {
        Model model = models.get(table);
        ResultSet users = model.getAllAndSort("updated_at");
        String cdate = "0000-00-00 00:00:00";
        if(users.next()) {
            users.last();
           cdate = users.getString("updated_at");
        }

            nvp.clear();
            nvp.add(new BasicNameValuePair("updated_at", cdate));
            String response = httpq.ServicePost("ApiCheck.php?tbl=" + table + "&fld=updated_at", nvp);
            System.out.println(response);
            if (!response.trim().equalsIgnoreCase("[]")) {
                try {
                    editToDb(table, response);
                    System.out.println("editdb");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

    }


    void checkAdd(String table) throws SQLException, URISyntaxException, HttpException {
        Model model = models.get(table);
        ResultSet users = model.getAllAndSort("created_at");
        String cdate = "0000-00-00 00:00:00";
        if(users.next()) {
            users.last();
            cdate = users.getString("created_at");
        }
            nvp.clear();
            nvp.add(new BasicNameValuePair("created_at", cdate));
            String response = httpq.ServicePost("ApiCheck.php?tbl=" + table + "&fld=created_at", nvp);
            System.out.println(response);
            if (!response.trim().equalsIgnoreCase("[]")) {
                try {
                    if(!response.equalsIgnoreCase("error")) {
                        addToDb(table, response);
                    }
                    System.out.println("addtodb"+"-"+response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

    }

    private void addToDb(String tbl,String jsonString) throws JSONException, SQLException {
        Model model = models.get(tbl);
        String s = "";
        System.out.println("reply"+jsonString);
        try {


            JSONArray jArray = new JSONArray(jsonString);

            for(int i=0; i<jArray.length(); i++){
                ArrayList<NameValuePair> nvp = new ArrayList<NameValuePair>();

                JSONObject json = jArray.getJSONObject(i);

                for(int o = 0; o<json.names().length(); o++){

                   if(json.names().getString(o).length() > 2){
                       if(!json.names().getString(o).equalsIgnoreCase("offline_id")){
                           s += "key = " + json.names().getString(o) + " value = " + json.get(json.names().getString(o))+",";
                           nvp.add(new BasicNameValuePair(json.names().getString(o),json.get(json.names().getString(o)).toString()));

                       }
                   }else{
                       if(json.names().getString(o).equalsIgnoreCase("id")){
                           nvp.add(new BasicNameValuePair("offline_id",json.get(json.names().getString(o)).toString()));
                       }
                   }

                }
                ResultSet current = model.get(nvp);

                if (!current.isBeforeFirst() ) {
                    model.save(nvp);
                }

                nvp.clear();
                System.out.println(s);
            }
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    private void editToDb(String tbl,String jsonString) throws JSONException {
        Model model = models.get(tbl);
        String s = "";
        System.out.println("reply"+jsonString);
        try {


        JSONArray jArray = new JSONArray(jsonString);
        String id = "";
        for (int i = 0; i < jArray.length(); i++) {
            ArrayList<NameValuePair> nvp = new ArrayList<NameValuePair>();

            JSONObject json = jArray.getJSONObject(i);

            for (int o = 0; o < json.names().length(); o++) {

                if (json.names().getString(o).length() > 2) {
                    if(!json.names().getString(o).equalsIgnoreCase("offline_id")){
                        s += "key = " + json.names().getString(o) + " value = " + json.get(json.names().getString(o)) + ",";
                        nvp.add(new BasicNameValuePair(json.names().getString(o), json.get(json.names().getString(o)).toString()));
                    }
                }else{
                    id = json.get("id").toString();
                }

            }
            model.save(nvp, id);
            nvp.clear();
            System.out.println(s + "--id=" + id);
        }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
