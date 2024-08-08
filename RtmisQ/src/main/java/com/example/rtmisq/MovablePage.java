package com.example.rtmisq;

import javafx.scene.control.ToolBar;
import javafx.stage.Stage;

public class MovablePage implements Movable{

    // Initial offsets for the mouse cursor
    private double xOffset;
    private double yOffset;

    // ToolBar associated with the movable page
    private final ToolBar toolBar;

    // Stage associated with the movable page
    private final Stage stage;

    // Constructor to initialize the movable page with offsets, ToolBar, and Stage
    public  MovablePage(double xOffset, double yOffset, ToolBar toolBar, Stage stage) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.toolBar = toolBar;
        this.stage = stage;
    }

    // Method to make the page movable
    public void movePage() {
        // Set mouse pressed event handler to capture initial cursor position
        toolBar.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        // Set mouse dragged event handler to move the stage based on cursor movements
        toolBar.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
    }
}
