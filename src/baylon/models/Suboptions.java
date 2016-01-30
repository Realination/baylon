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
public class Suboptions extends Model{
    private static String tblName = "sub_options";
    private static String primaryKey = "id";
    private static Suboptions instance;

    public static Suboptions getInstance() {
        if (instance == null) {
            synchronized (Suboptions.class) {
                if (instance == null) {
                    instance = new Suboptions();

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
