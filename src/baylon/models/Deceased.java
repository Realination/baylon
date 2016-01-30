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
public class Deceased extends Model{
    private static String tblName = "deceased";
    private static String primaryKey = "id";
    private static Deceased instance;

    public static Deceased getInstance() {
        if (instance == null) {
            synchronized (Deceased.class) {
                if (instance == null) {
                    instance = new Deceased();

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
