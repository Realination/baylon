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
public class Packages extends Model{
    private static String tblName = "packages";
    private static String primaryKey = "id";
    private static Packages instance;

    public static Packages getInstance() {
        if (instance == null) {
            synchronized (Packages.class) {
                if (instance == null) {
                    instance = new Packages();

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
