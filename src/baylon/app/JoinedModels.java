package baylon.app;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Troll173 on 2/3/2016.
 */
public class JoinedModels {

    public Model model;
    private ArrayList<NameValuePair> withModels = new ArrayList<NameValuePair>();
    private ArrayList<NameValuePair> where = new ArrayList<NameValuePair>();
    private ArrayList<Model> models = new ArrayList<Model>();
    Connection conn;

    public JoinedModels(Model model) {
        this.model = model;
        try {
            conn=DBConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void with(Model withModel,String on){
        if(!withModels.contains(withModel.tblName())){
            withModels.add(new BasicNameValuePair(withModel.tblName(),on));
            models.add(withModel);
        }
    }

    public void where(NameValuePair newWhere){
        where.add(newWhere);
    }

    public ResultSet get(){
        String joinString = "";
        String whereString = "";

        int j=0;
        for (NameValuePair withModel : withModels) {
            Model cmodel = models.get(j);
            joinString += " LEFT JOIN `"+withModel.getName()+"` ON "+model.tblName()+"."+withModel.getValue()+" = "+withModel.getName()+"."+cmodel.primaryKey()+"";
            if(withModels.size()-1 > j){
                joinString += " ";
            }
            j++;
        }

        if(where.size() > 0){
            whereString += " WHERE ";
        }

        int i=0;
        for (NameValuePair datum : where) {
            whereString += " `"+datum.getName()+"` LIKE ?";
            if(where.size()-1 > i){
                whereString += " AND";
            }
            i++;
        }


        String query = "SELECT * FROM "+model.tblName()+joinString+whereString;
        ResultSet rs = null;
        try {
            PreparedStatement ps = conn.prepareStatement(query);


            int i2=0;
            for (NameValuePair datum : where) {
                ps.setString(i2, datum.getValue());
                i2++;
            }
            rs = ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return  rs;
    }



}
