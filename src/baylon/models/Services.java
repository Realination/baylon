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
public class Services extends Model{
    private static String tblName = "services";
    private static String primaryKey = "id";
    private static Services instance;

    public static Services getInstance() {
        if (instance == null) {
            synchronized (Services.class) {
                if (instance == null) {
                    instance = new Services();

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
