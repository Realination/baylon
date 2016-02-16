/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package baylon.app;

import org.apache.http.NameValuePair;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author DarkMatter
 */
public abstract class Model {
    
    public abstract String tblName();
    public abstract String primaryKey();
    BaylonFunctions funcs = new BaylonFunctions();

     public int save(ArrayList<NameValuePair> data) {
        Connection conn;
         /////
         int generatedKey = 0;
         data =  funcs.addCreatedAt(data);


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
            conn=DBConnection.getConnection();
            String query = "INSERT INTO `"+tblName()+"` ("+fields+")VALUES("+fillers+")";
            System.out.println(query);
            PreparedStatement preparedStmt = conn.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
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


    public void save(ArrayList<NameValuePair> data,String id) {
        Connection conn;
        String fields = "";

        data =  funcs.addUpdatedAt(data);

        int c = 0;
        for (NameValuePair datum : data) {
            fields += "`"+datum.getName()+"` = ?";
            if(data.size()-1 > c){
                fields += ",";
            }
            c++;
        }

        try{
            conn=DBConnection.getConnection();
            String query = "UPDATE `"+tblName()+"` SET  "+fields+" WHERE "+primaryKey()+"='"+id+"'";
            System.out.println(query);
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            int i = 1;
            for (NameValuePair datum : data) {
                preparedStmt.setString(i, datum.getValue());
                i++;
            }


            // execute the java preparedstatement
            preparedStmt.execute();


        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Error");

        }

    }


    public void save(ArrayList<NameValuePair> data,ArrayList<NameValuePair> where) {
        Connection conn;
        String fields = "";
        String strWhere = "";

        data =  funcs.addUpdatedAt(data);

        int c = 0;
        for (NameValuePair datum : data) {
            fields += "`"+datum.getName()+"` = ?";
            if(data.size()-1 > c){
                fields += ",";
            }
            c++;
        }

        int w = 0;
        for (NameValuePair wher : where) {
            strWhere += " `"+wher.getName()+"` LIKE '"+wher.getValue()+"' ";
            if(where.size()-1 > w){
                strWhere += "AND";
            }
            w++;
        }

        try{
            conn=DBConnection.getConnection();
            String query = "UPDATE `"+tblName()+"` SET  "+fields+" WHERE "+strWhere;
            System.out.println(query);
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            int i = 1;
            for (NameValuePair datum : data) {
                preparedStmt.setString(i, datum.getValue());
                i++;
            }


            // execute the java preparedstatement
            preparedStmt.execute();


        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Error");

        }

    }

    public ResultSet getAllAndSort(String fld){
        ResultSet result = null;
        Connection conn;
        try{
            conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();
            result = stmt.executeQuery("SELECT * FROM "+tblName()+" ORDER BY `"+fld+"` asc");
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Error");
        }
        return result;
    }



     public ResultSet get(){
         ResultSet result = null;

        Connection conn;
        try{
            conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();
            result = stmt.executeQuery("SELECT * FROM "+tblName());
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Error");
        }
        return result;
     }

     public ResultSet get(String id){
         ResultSet result = null;
        Connection conn;
        try{
            conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM "+tblName()+" WHERE "+primaryKey()+" = '"+id+"'";
            result = stmt.executeQuery(sql);
            System.out.println(sql);
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Error");
        }
        return result;
     }


     public ResultSet get(ArrayList<NameValuePair> data){
          ResultSet result = null;
           String where="";

           int i=0;
           for (NameValuePair datum : data) {
                where += " `"+datum.getName()+"` LIKE ?";
                    if(data.size()-1 > i){
                        where += " AND";
                    }
                i++;
             }

        Connection conn;
        try{

            conn = DBConnection.getConnection();
//            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM `"+tblName()+"` WHERE "+where;
            System.out.println(sql);
            PreparedStatement ps = conn.prepareStatement(sql);
//            result = stmt.executeQuery(sql);

            int c=1;
            for (NameValuePair datum : data) {
                ps.setString(c, datum.getValue());
                c++;
            }

            result = ps.executeQuery();

        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Error");
        }
        return result;
     }



    public Double getSum(ArrayList<NameValuePair> data,String field){
        ResultSet result = null;
        String where="";
        Double total = 0.0;

        int i=0;
        for (NameValuePair datum : data) {
            where += " `"+datum.getName()+"` LIKE '"+datum.getValue()+"'";
            if(data.size()-1 > i){
                where += " AND";
            }
            i++;
        }

        Connection conn;
        try{
            conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();
            String sql = "SELECT SUM("+field+") as total FROM `"+tblName()+"` WHERE "+where;
            result = stmt.executeQuery(sql);
            System.out.println(sql);
            result.first();
            total = result.getDouble("total");
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Error");
        }

        return total;
    }

    public ResultSet getNot(ArrayList<NameValuePair> data){
        ResultSet result = null;
        String where="";

        int i=0;
        for (NameValuePair datum : data) {
            where += " `"+datum.getName()+"` <> '"+datum.getValue()+"'";
            if(data.size()-1 > i){
                where += " AND";
            }
            i++;
        }

        Connection conn;
        try{
            conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM `"+tblName()+"` WHERE "+where;
            result = stmt.executeQuery(sql);
            System.out.println(sql);
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Error");
        }
        return result;
    }


    public ResultSet get(ArrayList<NameValuePair> data,String order){
        ResultSet result = null;
        String where="";

        int i=0;
        for (NameValuePair datum : data) {
            where += " `"+datum.getName()+"` LIKE '"+datum.getValue()+"'";
            if(data.size()-1 > i){
                where += " AND";
            }
            i++;
        }

        Connection conn;
        try{
            conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM `"+tblName()+"` WHERE "+where+" ORDER BY "+order;
            result = stmt.executeQuery(sql);
            System.out.println(sql);
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Error");
        }
        return result;
    }

    public void remove(String id){
        Connection conn;
        try{
            conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();
            String sql= "DELETE FROM "+tblName()+" WHERE "+primaryKey()+" LIKE '"+id+"'";
            stmt.executeUpdate(sql);
            System.out.println(sql);
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Error");
        }
    }

    public void remove(ArrayList<NameValuePair> data){
        ResultSet result = null;
        String where="";

        int i=0;
        for (NameValuePair datum : data) {
            where += " `"+datum.getName()+"` LIKE '"+datum.getValue()+"'";
            if(data.size()-1 > i){
                where += " AND";
            }
            i++;
        }

        Connection conn;
        try{
            conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();
            String sql = "DELETE FROM `"+tblName()+"` WHERE "+where;
            System.out.println(sql);
            stmt.executeUpdate(sql);
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Error");
        }
    }
     
    
}
