package com.example.rtmisq;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ToolBar;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;


public class HelloController extends CommonRtmisQ implements Initializable {

    @FXML
    private ImageView strimg;
    @FXML
    private ImageView greenimg;
    @FXML
    private ImageView rtmisimg;
    @FXML
    private ImageView Yellowimg;
    @FXML
    private ToolBar toolbarL;



    private void initialize(){
        ScaleTransition setBackground =new ScaleTransition(Duration.seconds(0.5),greenimg);
        {
            setBackground.setFromX(5);
            setBackground.setFromY(5);
            setBackground.setToY(1);
            setBackground.setToX(1);
            setBackground.play();
        }
        TranslateTransition translateTransitionYellow =new TranslateTransition(Duration.seconds(0.5),Yellowimg);
        {
            translateTransitionYellow.setFromY(200);
            translateTransitionYellow.setToY(0);
            translateTransitionYellow.play();
        }
        FadeTransition fadeTransitionbtn =new FadeTransition(Duration.seconds(0.5),strimg);
        {
            fadeTransitionbtn.setFromValue(0);
            fadeTransitionbtn.setToValue(1);
            fadeTransitionbtn.play();
        }
    }
    public void startNow() {
        FadeTransition fadeTransitionbtn =new FadeTransition(Duration.seconds(0.5),strimg);
        {
            fadeTransitionbtn.setFromValue(1);
            fadeTransitionbtn.setToValue(0);
            fadeTransitionbtn.play();
        }
        ScaleTransition setBackground =new ScaleTransition(Duration.seconds(0.5),greenimg);
        {
                setBackground.setFromX(1);
                setBackground.setFromY(1);
                setBackground.setToY(5);
                setBackground.setToX(5);
                setBackground.play();
        }

        ScaleTransition scaleRtimsQ =new ScaleTransition(Duration.seconds(0.5),rtmisimg);
        {       scaleRtimsQ.setFromX(1);
                scaleRtimsQ.setFromY(1);
                scaleRtimsQ.setToX(0.5);
                scaleRtimsQ.setToY(0.5);
                scaleRtimsQ.play();
        }

        TranslateTransition translateTransitionRtmisQ =new TranslateTransition(Duration.seconds(0.5),rtmisimg);
        {
            translateTransitionRtmisQ.setByY(-150);
            translateTransitionRtmisQ.play();
        }


        PauseTransition delay=new PauseTransition(Duration.seconds(0.5));
        delay.setOnFinished(e ->{
            ChangeFXML changeFXML =new ChangeFXML();
            changeFXML.SetFXML(toolbarL,"HomePage");
        } );
        delay.play();

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
       initialize();
       super.setToolbarL(toolbarL);
    }
}