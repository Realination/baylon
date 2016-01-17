package baylon.app;

import org.apache.http.HttpException;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by troll173 on 12/7/15.
 */
public class BaylonFunctions {
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    HttpRequest http = new HttpRequest();



    public ArrayList<NameValuePair> addCreatedAt(ArrayList<NameValuePair> data){
         Date date = new Date();
        boolean has = false;
        for (NameValuePair datum : data){
            if(datum.getName().equalsIgnoreCase("created_at")){
                has = true;
            }
        }
        if(!has){
            data.add(new BasicNameValuePair("created_at",dateFormat.format(date)));
        }

        return data;
    }


    public ArrayList<NameValuePair> addUpdatedAt(ArrayList<NameValuePair> data){
        Date date = new Date();
        boolean has = false;
        int hasIndex = -1;
        int i = 0;
        for (NameValuePair datum : data){
            if(datum.getName().equalsIgnoreCase("updated_at")){
                has = true;
                hasIndex = i;
            }
            i++;
        }
        if(!has){
            data.add(new BasicNameValuePair("updated_at",dateFormat.format(date)));
        }else{
            data.remove(hasIndex);
            data.add(new BasicNameValuePair("updated_at",dateFormat.format(date)));
        }

        return data;
    }


    public boolean SyncAdd(ArrayList<NameValuePair> data,String table,String id) throws URISyntaxException, HttpException {
        data.add(new BasicNameValuePair("offline_id",id));
        if(http.ServicePost("SyncUp.php?table="+table,data).equalsIgnoreCase("error")){
            return false;
        }else{
            return true;
        }
    }

    public boolean SyncEdit(ArrayList<NameValuePair> data,String table,String id) throws URISyntaxException, HttpException {
        if(http.ServicePost("SyncUp.php?table="+table+"&id="+id,data).equalsIgnoreCase("error")){
            return false;
        }else{
            return true;
        }
    }

    public boolean SyncDelete(ArrayList<NameValuePair> data,String table) throws URISyntaxException, HttpException {
        if(http.ServicePost("SyncUp.php?table="+table+"&delete=true",data).equalsIgnoreCase("error")){
            return false;
        }else{
            return true;
        }
    }

}
