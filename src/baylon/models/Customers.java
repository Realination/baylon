package baylon.models;

import baylon.app.Model;

/**
 * @author DarkMatter
 */
public class Customers extends Model {
    private static String tblName = "customers";
    private static String primaryKey = "id";

    private static Customers instance;

    public static Customers getInstance() {
        if (instance == null) {
            synchronized (Customers.class) {
                if (instance == null) {
                    instance = new Customers();

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