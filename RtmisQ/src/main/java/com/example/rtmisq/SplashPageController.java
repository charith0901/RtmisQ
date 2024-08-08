package com.example.rtmisq;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SplashPageController implements Initializable {

    @FXML
    private ImageView splashImage;

    public  void initialize(URL arg0, ResourceBundle arg1){


        simulateLoading();
    }
    private void simulateLoading() {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                // Simulate loading by updating the ProgressBar
                for (int i = 0; i <= 40; i++) {
                    try {
                        Thread.sleep(50); // Simulate some work being done
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }
        };


        task.setOnSucceeded(e -> {
            Stage currentstage;
            currentstage = (Stage) splashImage.getScene().getWindow();

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginForm.fxml"));
                Parent root = loader.load();


                LoginFormController loginFormController=loader.getController();
                loginFormController.setMovablePage(currentstage);

                currentstage.setScene(new Scene(root));
                currentstage.centerOnScreen();
                currentstage.show();


            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }


        });


        // Start the task in a new thread
        new Thread(task).start();
    }
}
