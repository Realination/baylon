/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package baylon.models;

import baylon.app.BaylonFunctions;
import baylon.app.DBConnection;
import baylon.app.Functions;
import baylon.app.Model;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author DarkMatter
 */
public class Pickup extends Model{
    private static String tblName = "pickup";
    private static String primaryKey = "uid";

    private static Pickup instance;

    public static Pickup getInstance() {
        if (instance == null) {
            synchronized (Pickup.class) {
                if (instance == null) {
                    instance = new Pickup();

                }
            }
        }
        return instance;
    }


    @Override
    public int save(ArrayList<NameValuePair> data) {
        Connection conn;
        /////
        BaylonFunctions funcs = new BaylonFunctions();
        int generatedKey = 0;
        data =  funcs.addCreatedAt(data);

        boolean found = false;
        for (NameValuePair dtum: data){
            if(dtum.getName().equalsIgnoreCase("uid")){
                found = true;
            }
        }

        if(!found){
            data.add(new BasicNameValuePair("uid", "Walkin-"+ Functions.randomString(9)));
        }

        String fields = "";
        String fillers = "";
        int c = 0;
        for (NameValuePair datum : data) {
            fields += "`"+datum.getName()+"`";
            fillers += "?";
            if(data.size()-1 > c){
                fields += ",";
                fillers +=",";
            }
            c++;
        }

        try{
            conn= DBConnection.getConnection();
            String query = "INSERT INTO `"+tblName()+"` ("+fields+")VALUES("+fillers+")";
            System.out.println(query);
            PreparedStatement preparedStmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            int i = 1;
            for (NameValuePair datum : data) {
                preparedStmt.setString(i, datum.getValue());
                i++;
            }


            // execute the java preparedstatement
            preparedStmt.execute();

            ResultSet rs = preparedStmt.getGeneratedKeys();

            if (rs.next()) {
                generatedKey = rs.getInt(1);
            }

        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Error");

        }
        return generatedKey;
    }


    @Override
    public String tblName() {
       return tblName;
    }

    @Override
    public String primaryKey() {
        return primaryKey;
    }
}
