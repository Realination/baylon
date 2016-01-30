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
public class Admin extends Model{
    private static String tblName = "admin";
    private static String primaryKey = "id";

    private static Admin instance;

    public static Admin getInstance() {
        if (instance == null) {
            synchronized (Admin.class) {
                if (instance == null) {
                    instance = new Admin();

                }
            }
        }
        return instance;
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
