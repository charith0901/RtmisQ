package com.example;

import animatefx.animation.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;
import java.util.Objects;

public class Main extends Application {


    @Override
    public  void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("rtmisq/SplashPage.fxml")));
        Scene scene = new Scene(root, Color.TRANSPARENT);
        root.setStyle("-fx-background-color: transparent");
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(scene);
        stage.show();


    }
   

    public static void main(String[] args) {
        launch();
    }
}