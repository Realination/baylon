/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package baylon.models;

import baylon.app.Model;

/**
 *
 * @author DarkMatter
 */
public class Caskets extends Model{
    private static String tblName = "caskets";
    private static String primaryKey = "id";

    private static Caskets instance;

    public static Caskets getInstance() {
        if (instance == null) {
            synchronized (Caskets.class) {
                if (instance == null) {
                    instance = new Caskets();

                }
            }
        }
        return instance;
    }


    public String tblName() {
       return tblName;
    }

   
    public String primaryKey() {
        return primaryKey;
    }
}
