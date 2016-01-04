package baylon.models;

import baylon.app.Model;

/**
 * @author DarkMatter
 */
public class Customers extends Model {
    private static String tblName = "customers";
    private static String primaryKey = "id";

    @Override
    public String tblName() {
        return tblName;
    }

    @Override
    public String primaryKey() {
        return primaryKey;
    }
}