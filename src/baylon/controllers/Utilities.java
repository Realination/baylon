package baylon.controllers;

import baylon.models.Misc;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Troll173 on 2/10/2016.
 */
public class Utilities {

    @FXML
    TextField txtDir;

    Misc tblmisc = Misc.getInstance();

    String path = "";

    public void initialize() throws SQLException {
        ResultSet misc = tblmisc.get("2");
        misc.first();

        path = misc.getString("data");
        txtDir.setText(path);
    }



    @FXML
    void browseDir() {

        DirectoryChooser directoryChooser = new DirectoryChooser();

        directoryChooser.setTitle("Select Backup Directory");
        //Show open file dialog
        File backupDir = new File(path);
        directoryChooser.setInitialDirectory(backupDir);
        File file = directoryChooser.showDialog(null);

        if (file != null) {

            txtDir.setText(file.getPath());
            ArrayList<NameValuePair> nvp = new ArrayList<>();
            nvp.add(new BasicNameValuePair("data",file.getPath()));
            tblmisc.save(nvp,"2");
        }
    }

}
