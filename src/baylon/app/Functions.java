package baylon.app;

import com.sun.glass.ui.Screen;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.print.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;

import java.io.IOException;
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


    public static void openScene(FXMLLoader fxmlLoader,Stage stage,String title,Boolean fullscreen,Boolean maximized){
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setScene(new Scene(root));
        stage.setWidth(Screen.getMainScreen().getWidth());
        stage.setHeight(Screen.getMainScreen().getHeight());
        stage.setMaximized(maximized);
        stage.setFullScreen(fullscreen);
        stage.setTitle(title);

        stage.show();
    }


    public static void dataTable(TableView table,ObservableList data,String searchPrompt,Boolean clickable){
        TextField txtSearch = new TextField();
        txtSearch.setPromptText(searchPrompt);
        txtSearch.setPrefWidth(200);

        AnchorPane parent = (AnchorPane)table.getParent();

        Double x = table.getLayoutX();
        Double y = table.getLayoutY();
        Double leftDistance = (Screen.getMainScreen().getWidth() - table.getLayoutX() * 2) - txtSearch.getPrefWidth();
        txtSearch.setLayoutX(leftDistance + table.getWidth());
        txtSearch.setLayoutY(y - 40);
        txtSearch.setVisible(true);
        parent.getChildren().add(txtSearch);

        ObservableList oldData = data;
        table.setItems(oldData);
        txtSearch.textProperty().addListener(new ChangeListener<String>() {
            public void changed(final ObservableValue<? extends String> observable, final String oldValue, final String newValue) {
                ObservableList newData = FXCollections.observableArrayList();
                for(Object datum: oldData){

                    if(datum.toString().toUpperCase().contains(newValue.toUpperCase())){
                        newData.addAll(datum);
                    }
                }
                System.out.println(oldData + " ="+newValue);
                table.setItems(newData);

            }
        });

    }


    public static void print(WritableImage writableImage, Stage primaryStage) {


        ImageView imageView =new ImageView(writableImage);
        Printer printer = Printer.getDefaultPrinter();
        PageLayout pageLayout = printer.createPageLayout(Paper.A4, PageOrientation.PORTRAIT, Printer.MarginType.HARDWARE_MINIMUM);
        double scaleX = pageLayout.getPrintableWidth() / imageView.getBoundsInParent().getWidth();
        double scaleY = pageLayout.getPrintableHeight() / imageView.getBoundsInParent().getHeight();
        imageView.getTransforms().add(new Scale(scaleX, scaleY));

        PrinterJob job = PrinterJob.createPrinterJob();

        if (job != null) {
            boolean successPrintDialog = job.showPrintDialog(primaryStage.getOwner());
            if(successPrintDialog){
                boolean success = job.printPage(pageLayout,imageView);
                if (success) {
                    job.endJob();
                }
            }
        }

    }

}
