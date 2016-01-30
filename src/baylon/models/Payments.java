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
public class Payments extends Model{
    private static String tblName = "payments";
    private static String primaryKey = "id";
    private static Payments instance;

    public static Payments getInstance() {
        if (instance == null) {
            synchronized (Payments.class) {
                if (instance == null) {
                    instance = new Payments();

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
