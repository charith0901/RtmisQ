package com.example.rtmisq;

import javafx.scene.control.ToolBar;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.util.Objects;
import java.util.ResourceBundle;

public class CommonRtmisQ {

    // ToolBar for the common functionality
    private ToolBar toolbarL;

    // Instance of GetConfigure to retrieve properties
    private GetConfigure getConfigure = new GetConfigure();

    // ResourceBundle for localization
    private ResourceBundle bundle = getConfigure.PropertiesConnection();

    // MediaPlayers for different sound effects
    private MediaPlayer mediaPlayerClick;
    private MediaPlayer mediaPlayerAlert;
    private MediaPlayer mediaPlayerFail;
    private MediaPlayer mediaPlayerSlide;
    private MediaPlayer mediaPlayerSuccess;
    private MediaPlayer mediaPlayerIntro;

    // Constructor to initialize MediaPlayers
    public CommonRtmisQ() {
        initializeMediaPlayers();
    }

    // Method to initialize MediaPlayers with sound files
    private void initializeMediaPlayers() {
        mediaPlayerClick = createAndLoadMediaPlayer("Sounds/click.wav");
        mediaPlayerAlert = createAndLoadMediaPlayer("Sounds/alert.wav");
        mediaPlayerFail = createAndLoadMediaPlayer("Sounds/fail.wav");
        mediaPlayerSlide = createAndLoadMediaPlayer("Sounds/slide.wav");
        mediaPlayerSuccess = createAndLoadMediaPlayer("Sounds/success.wav");
        mediaPlayerIntro = createAndLoadMediaPlayer("Sounds/intro.wav");
    }

    // Method to create and load MediaPlayer with a specified media path
    private MediaPlayer createAndLoadMediaPlayer(String mediaPath) {
        Media media = new Media(Objects.requireNonNull(getClass().getResource(mediaPath)).toString());
        return new MediaPlayer(media);
    }

    // Method to minimize the window
    public void minimizewindow() {
        Stage stage = (Stage) toolbarL.getScene().getWindow();
        stage.setIconified(true);
    }

    // Method to close the window with confirmation
    public void closewindow() {
        ViewAlert viewAlert = new ViewAlert();
        boolean choice = viewAlert.setAlert(bundle.getString("closetext"), false, true,toolbarL);

        if (choice) {
            Stage currentStage = (Stage) toolbarL.getScene().getWindow();
            currentStage.close();
        }
    }

    // Method to set a movable page
    public void setMovablePage(Stage currentStage) {
        MovablePage movablePage = new MovablePage(0.0, 0.0, toolbarL, currentStage);
        movablePage.movePage();
    }

    // Setter method for toolbarL
    public void setToolbarL(ToolBar toolbarL) {
        this.toolbarL = toolbarL;
    }

    // Method to play the click sound
    private void play(MediaPlayer mediaPlayer) {
        mediaPlayer.stop();
        mediaPlayer.play();
    }

    // Methods to play different sound effects
    public void playClick() {
        play(mediaPlayerClick);
    }

    public void playAlert() {
        play(mediaPlayerAlert);
    }

    public void playFail() {
        play(mediaPlayerFail);
    }

    public void playSlide() {
        play(mediaPlayerSlide);
    }

    public void playSuccess() {
        play(mediaPlayerSuccess);
    }

    public void playIntro() {
        play(mediaPlayerIntro);
    }




    public void exitclass(){
        this.toolbarL=null;
        this.bundle=null;
        this.getConfigure=null;
    }
}
