package com.example.rtmisq;

import io.github.palexdev.materialfx.controls.MFXProgressBar;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoadingPageController extends CommonRtmisQ implements Initializable {

    public ImageView lion;
    @FXML
    private MFXProgressBar mfxProgressBar;
    @FXML
    private Label progressLabel;
    @FXML
    private ToolBar toolbarL;
    public static MediaPlayer mediaPlayer;

    public void initialize(URL arg0, ResourceBundle arg1) {
        simulateLoading();
        toolbarL.setVisible(false);
        playIntro();
    }

    private void simulateLoading() {
        double lionposition=lion.getLayoutX();
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                // Simulate loading by updating the ProgressBar
                for (int i = 0; i <= 100; i++) {
                    final double progress = i / 100.0;
                    updateProgress(progress, 1.0);
                    updateMessage((int)(progress*100)+"%");
                    final double moveX =lionposition+ i * 7;

                    // Update UI components in the JavaFX application thread
                    Platform.runLater(() -> {
                        lion.setLayoutX(moveX);
                    });
                    try {
                        Thread.sleep(50); // Simulate some work being done
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }
        };

        // Bind the ProgressBar to the Task
        //lion.layoutXProperty().bind(task.progressProperty().multiply(mfxProgressBar.widthProperty()));
        mfxProgressBar.progressProperty().bind(task.progressProperty());
        progressLabel.textProperty().bind(task.messageProperty());

        task.setOnSucceeded(e -> {
            ChangeFXML changeFXML = new ChangeFXML();
            changeFXML.SetFXML(toolbarL, "StartPage");

            // Adding background music
            try {
                Media media = new Media(Objects.requireNonNull(getClass().getResource("Sounds/background.mp3")).toString());
                mediaPlayer = new MediaPlayer(media);
                mediaPlayer.setVolume(0.1);
                mediaPlayer.play();
                mediaPlayer.setOnEndOfMedia(() -> {
                    mediaPlayer.seek(Duration.ZERO);
                    mediaPlayer.play();
                });
            } catch (NullPointerException | IllegalArgumentException ex) {
                System.err.println("Error loading background music: " + ex.getMessage());
            }
        });

        // Start the task in a new thread
        new Thread(task).start();
    }

    public void setMovablePage(Stage currentStage) {
        MovablePage movablePage = new MovablePage(0.0, 0.0, toolbarL, currentStage);
        movablePage.movePage();
    }
}
