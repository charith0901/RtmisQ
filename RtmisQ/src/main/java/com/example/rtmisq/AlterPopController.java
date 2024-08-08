package com.example.rtmisq;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AlterPopController extends CommonRtmisQ implements Initializable {

    // FXML elements
    @FXML
    private Text WarningText;
    @FXML
    private ImageView CloseBtn, YesBtn, NoBtn;

    // Variable to store the user's choice
    private boolean Choice;

    // Method called during initialization
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Play alert sound during initialization
        playAlert();
    }

    // Method to set up the alert pop-up
    public void SetAlterPop(String Warning, boolean okay, boolean yesNo) {
        // Set the warning text
        WarningText.setText(Warning);

        // Set the visibility of buttons based on parameters
        CloseBtn.setVisible(okay);
        YesBtn.setVisible(yesNo);
        NoBtn.setVisible(yesNo);
    }

    // Event handler for Yes button
    @FXML
    public void YesConfirm() {
        Choice = true;
        closeAlertStage();
    }

    // Event handler for No button
    @FXML
    public void NoConfirm() {
        Choice = false;
        closeAlertStage();
    }

    // Event handler for Close button
    @FXML
    public void CloseConfirm() {
        Choice = true;
        closeAlertStage();
    }

    // Getter method to retrieve the user's choice
    public boolean isChoice() {
        return Choice;
    }

    // Method to close the alert stage
    private void closeAlertStage() {
        Stage alertStage = (Stage) WarningText.getScene().getWindow();
        alertStage.close();
    }
}
