package baylon.app;

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
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Troll173 on 2/1/2016.
 */
public class Synch {

    private Model model;
    private String tblname = "";
    private HttpRequest httpq = new HttpRequest();
    private ArrayList<NameValuePair> nvp = new ArrayList<NameValuePair>();

    public Synch(Model model,long interval) {
        this.model = model;
        this.tblname = model.tblName();
        try {
            Timer timer = new Timer();
            timer.schedule(checkSynch(), 0, interval);
        }catch (Exception e){
            e.printStackTrace();
        }



    }

    private TimerTask checkSynch() {
        TimerTask checkSynch = new TimerTask() {
            @Override
            public void run() {

                try {
                    checkEdit();
                    checkAdd();
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (HttpException e) {
                    e.printStackTrace();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        };
        return checkSynch;
    }

    private void checkAdd() throws SQLException, URISyntaxException, HttpException {
        ResultSet records = model.getAllAndSort("created_at");
        String cdate = "2015-01-01 01:01:01";
        records.beforeFirst();
        if(records.next()) {
            records.last();
            cdate = records.getString("created_at");
        }
        nvp.clear();
        nvp.add(new BasicNameValuePair("created_at", cdate));
        String response = httpq.ServicePost("ApiCheck.php?tbl=" + tblname + "&fld=created_at", nvp);
        System.out.println(response);
        if (!response.trim().equalsIgnoreCase("[]")) {
                if(!response.equalsIgnoreCase("error")) {
                    addToDb(response);
                }
                System.out.println("addtodb"+"-"+response);

        }
    }



    private void checkEdit() throws SQLException, URISyntaxException, HttpException {
        ResultSet users = model.getAllAndSort("updated_at");
        String cdate = "2015-01-01 01:01:01";
        if(users.next()) {
            users.last();
            if(users.getString("updated_at").equalsIgnoreCase("0000-00-00 00:00:00")){
                cdate = "2015-01-01 01:01:01";
            }else{
                cdate = users.getString("updated_at");
            }
        }

        nvp.clear();
        nvp.add(new BasicNameValuePair("updated_at", cdate));
        String response = httpq.ServicePost("ApiCheck.php?tbl=" + tblname + "&fld=updated_at", nvp);
        System.out.println(response);
        if (!response.trim().equalsIgnoreCase("[]")) {
                editToDb(response);
                System.out.println("editdb"+"-"+response);
        }
    }


    private void addToDb(String jsonString) {
        try {
            String s = "";

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



    private void editToDb(String jsonString) {
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
                ArrayList<NameValuePair> where = new ArrayList<NameValuePair>();
                where.add(new BasicNameValuePair("offline_id",id));
                model.save(nvp, where);

                nvp.clear();
                System.out.println(s + "--id=" + id);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
