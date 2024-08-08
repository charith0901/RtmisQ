package com.example.rtmisq;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ToolBar;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class ViewAlert {
    private boolean Choice;
    public boolean setAlert(String Warning, boolean okay, boolean yesNo, ToolBar toolBar)  {
      try {
          FXMLLoader loader = new FXMLLoader(getClass().getResource("AlertPop.fxml"));
          Parent root =loader.load();
          Stage stage = new Stage();
          stage.setScene(new Scene(root, Color.TRANSPARENT));
          root.setStyle("-fx-background-color: transparent");
          stage.initStyle(StageStyle.TRANSPARENT);
          AlterPopController alterPopController =loader.getController();
          alterPopController.SetAlterPop(Warning,okay,yesNo);
          stage.initModality(Modality.APPLICATION_MODAL);
          Stage ParentStage =(Stage)toolBar.getScene().getWindow();
          ParentStage.centerOnScreen();
          ParentStage.setOpacity(0.9);
          stage.centerOnScreen();
          stage.showAndWait();
          ParentStage.setOpacity(1.0);
          Choice = alterPopController.isChoice();
      }catch(IOException e){
          e.printStackTrace();
      }
        return Choice;
    }
}
