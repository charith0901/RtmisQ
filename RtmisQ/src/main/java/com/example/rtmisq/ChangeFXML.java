package com.example.rtmisq;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ToolBar;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ChangeFXML {

    // Method to set a new FXML file for the given ToolBar
    public void SetFXML(ToolBar toolBar, String fxmlFile) {
        try {
            // Get the current stage from the ToolBar's scene
            Stage currentStage = (Stage) toolBar.getScene().getWindow();

            // Load the new FXML file using FXMLLoader
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile + ".fxml"));
            Parent root = loader.load();

            // Get the controller instance associated with the loaded FXML
            Object controller = loader.getController();

            // Invoke the "setMovablePage" method on the controller using reflection
            try {
                Method method = controller.getClass().getMethod("setMovablePage", Stage.class);
                method.invoke(controller, currentStage);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace(); // Handle exceptions appropriately
            }

            // Set the new scene with the loaded FXML

            currentStage.setScene(new Scene(root, Color.TRANSPARENT));
            root.setStyle("-fx-background-color: transparent");
            currentStage.centerOnScreen();
        } catch (IOException ex) {
            // Throw a runtime exception if there is an IOException
            throw new RuntimeException(ex);
        }
    }
}
