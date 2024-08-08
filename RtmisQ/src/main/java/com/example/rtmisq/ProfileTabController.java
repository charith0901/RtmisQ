package com.example.rtmisq;

import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ToolBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ProfileTabController implements Initializable {
    private ToolBar toolbarL;
    private ImageView leaves;
    private AnchorPane CurrentAnchorPane;
    private int current;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    @FXML
    public void ViewProfile(){
        ChangeFXML changeFXML=new ChangeFXML();
        changeFXML.SetFXML(toolbarL,"UserProfile");
    }


    public void backToHome() {
        changeHome(0);
        animateAnchorPane(CurrentAnchorPane);
    }
    private void changeHome(int number) {
        RotateThis(number);
    }
    private void RotateThis(int newCurrent) {
        RotateTransition rotateTransitionBranch = new RotateTransition(Duration.seconds(0.5), leaves);
        rotateTransitionBranch.setFromAngle(current);
        rotateTransitionBranch.setToAngle(newCurrent);
        current = newCurrent;
        rotateTransitionBranch.play();
    }
    private void changeAnchor() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("HomeTab" +".fxml"));
        Parent root =loader.load();
        HomeTabController controller = loader.getController();
        controller.setCurrentAnchorPane(CurrentAnchorPane);
        controller.setToolBarL(toolbarL);
        controller.setLeaves(leaves);
        CurrentAnchorPane.getChildren().clear();
        CurrentAnchorPane.getChildren().add(root);
    }
    private void animateAnchorPane(AnchorPane anchorPane){
        TranslateTransition transition = new TranslateTransition(Duration.millis(400), anchorPane);
        transition.setToY(anchorPane.getHeight());
        transition.setOnFinished(event -> {
            try {
                changeAnchor();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            TranslateTransition resetTransition = new TranslateTransition(Duration.millis(400), anchorPane);
            resetTransition.setToY(0);
            resetTransition.play();
        });



        transition.play();
    }




    public void setCurrentAnchorPane(AnchorPane currentAnchorPane) {
        this.CurrentAnchorPane = currentAnchorPane;
    }

    public void setLeaves(ImageView leaves) {
        this.leaves = leaves;
    }

    public void setCurrent(int current) {
        this.current = current;
    }
    public void setToolBarL(ToolBar toolbarL) {
        this.toolbarL = toolbarL;
    }
}
