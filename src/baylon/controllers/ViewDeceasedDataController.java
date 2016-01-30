package baylon.controllers;

import baylon.models.Deceased;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Troll173 on 1/17/2016.
 */
public class ViewDeceasedDataController {
    @FXML
    Label lblHeader,lblName,lblOrderCode,lblSex,lblDoB,lblAge,lblHeight,lblWeight,lblContainer,lblContainerDesc,lblCasket,lblCasketDesc,lblCasketSize,lblDateDeath,lblTimeDeath,lblAgeDeath,lblPlaceDeath,lblEmbalming,lblAutopsied,lblAutopsiedBy,lblDeathCert,lblServiceCont,lblCasketNum,lblInstructions;


    Deceased tbldeceased = new Deceased();
    ResultSet deceased;

    public void initialize(String deceasedId){
        try {
            deceased = tbldeceased.get(deceasedId);
            deceased.first();
            lblHeader.setText(deceased.getString("lname")+ ","+deceased.getString("fname")+" "+deceased.getString("mname") +" ");
            lblName.setText(deceased.getString("lname")+ ","+deceased.getString("fname")+" "+deceased.getString("mname") +" ");
            lblOrderCode.setText(deceased.getString("ordercode") + " ");
            lblSex.setText(deceased.getString("sex") + " ");
            lblDoB.setText(deceased.getString("date_of_birth") + " ");
            lblAge.setText(deceased.getString("age") + " ");
            lblHeight.setText(deceased.getString("height") + " ");
            lblWeight.setText(deceased.getString("weight") + " ");
            lblContainer.setText(deceased.getString("container") + " ");
            lblContainerDesc.setText(deceased.getString("container_desc") + " ");
            lblCasket.setText(deceased.getString("casket") + " ");
            lblCasketDesc.setText(deceased.getString("casket_desc") + " ");
            lblCasketSize.setText(deceased.getString("casket_size") + " ");
            lblDateDeath.setText(deceased.getString("date_death") + " ");
            lblTimeDeath.setText(deceased.getString("time_death") + " ");
            lblAgeDeath.setText(deceased.getString("age_at_the_time_of_death") + " ");
            lblPlaceDeath.setText(deceased.getString("place_of_death") + " ");
            lblEmbalming.setText(deceased.getString("duration_embalming") + " ");
            lblAutopsied.setText(deceased.getString("autopsied") + " ");
            lblAutopsiedBy.setText(deceased.getString("person_institution") + " ");
            lblDeathCert.setText(deceased.getString("death_certificate") + " ");
            lblServiceCont.setText(deceased.getString("service_contract") + " ");
            lblCasketNum.setText(deceased.getString("casket_inventory_no") + " ");
            lblInstructions.setText(deceased.getString("other_instruction") + " ");


        } catch (SQLException e) {
            e.printStackTrace();
        }


    }


}
