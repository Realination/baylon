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
public class Misc extends Model{
    private static String tblName = "misc";
    private static String primaryKey = "id";

    private static Misc instance;

    public static Misc getInstance() {
        if (instance == null) {
            synchronized (Misc.class) {
                if (instance == null) {
                    instance = new Misc();

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
