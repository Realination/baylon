package baylon.app;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Random;

/**
 * Created by troll173 on 11/27/15.
 */
public class Functions {
    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    static Random rnd = new Random();


    public static String toMoney(String doublePayment) {
        Locale locale = new Locale("ph", "PH");
        NumberFormat formatter = NumberFormat.getCurrencyInstance(locale);
        String peso = formatter.format(Double.parseDouble(doublePayment));
      return peso;
    }





    public static String randomString( int len ){
        StringBuilder sb = new StringBuilder( len );
        for( int i = 0; i < len; i++ )
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
        return sb.toString();
    }


    public static String implode(String delimeter,String[] array){
        String retString = "";
        int i = 0;
        for (String elem : array){
            retString += elem;

            if (i <= array.length){
                retString +=delimeter;
            }
           i++;
        }
        System.out.println(retString);
        return retString;
    }


}
