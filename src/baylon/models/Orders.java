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
public class Orders extends Model{
    private static String tblName = "custpack";
    private static String primaryKey = "id";
    private static Orders instance;

    public static Orders getInstance() {
        if (instance == null) {
            synchronized (Orders.class) {
                if (instance == null) {
                    instance = new Orders();

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
