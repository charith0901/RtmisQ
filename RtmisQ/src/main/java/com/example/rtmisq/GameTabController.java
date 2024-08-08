package com.example.rtmisq;

import animatefx.animation.*;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class GameTabController extends CommonRtmisQ implements Initializable {

    public ImageView leftarrow;
    public ImageView rightarrow;
    public MFXButton continuebtn;
    // FXML elements
    @FXML
    private ImageView gameImage, gameImage1, gameImage2;
    @FXML
    private Label gameName;

    // Other variables
    private int currentIndex = 0;
    private ToolBar toolbarL;
    private ImageView leaves;
    private AnchorPane CurrentAnchorPane;
    private int current;
    private final String[] imagePaths = {"HomePage/birds.jfif", "HomePage/habitats.jfif", "HomePage/insects.jfif",
            "HomePage/mammal.jfif", "HomePage/marine.jfif", "HomePage/tree.jfif"};
    private final String[] gameNames = {"birds", "habitats", "insects", "mammal", "marine", "tree"};

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Pulse inLeft=new Pulse(leftarrow);
        inLeft.setCycleCount(AnimationFX.INDEFINITE);
        inLeft.play();


        Pulse inRight =new Pulse(rightarrow);
        inRight.setCycleCount(AnimationFX.INDEFINITE);
        inRight.play();

        Pulse contibtn =new Pulse(continuebtn);
        contibtn.setCycleCount(AnimationFX.INDEFINITE);
        contibtn.play();
        // Initialize the controller
        showImage();
    }

    // Button actions
    @FXML
    private void previousImage() {
        // Handle previous image button click
        playSlide();
        currentIndex = (currentIndex - 1 + imagePaths.length) % imagePaths.length;
        showImageWithAnimation(-gameImage.getFitWidth() / 2.00);
    }

    @FXML
    private void nextImage() {
        // Handle next image button click
        playSlide();
        currentIndex = (currentIndex + 1) % imagePaths.length;
        showImageWithAnimation(gameImage.getFitWidth() / 2.00);
    }

    // Image transition methods
    private void showImageWithAnimation(double initialTranslate) {
        // Show image with animation
        TranslateTransition transition = new TranslateTransition(Duration.millis(100), gameImage);
        transition.setToX(initialTranslate);
        transition.setOnFinished(event -> {
            showImage();
            gameImage.setTranslateX(-initialTranslate);
            TranslateTransition resetTransition = new TranslateTransition(Duration.millis(100), gameImage);
            resetTransition.setToX(0);
            resetTransition.play();
        });
        transition.play();
    }

    private void showImage() {
        // Show the current image and update game name
        gameImage1.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePaths[(currentIndex - 1 + imagePaths.length) % imagePaths.length]))));
        gameImage.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePaths[currentIndex]))));
        gameImage2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePaths[(currentIndex + 1) % imagePaths.length]))));
        gameName.setText(gameNames[currentIndex]);
    }

    // Handle key press events
    @FXML
    private void handleKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.RIGHT) {
            nextImage();
        } else if (event.getCode() == KeyCode.LEFT) {
            previousImage();
        }
    }

    // Start game button action
    @FXML
    public void StartGame() throws IOException {
        try {
            // Start the game
            Stage currentstage = (Stage) toolbarL.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("levelWindow.fxml"));
            Parent root = loader.load();
            Object controller = loader.getController();
            try {
                // Set movable page and category
                Method method = controller.getClass().getMethod("setMovablePage", Stage.class);
                method.invoke(controller, currentstage);
                Method method2 = controller.getClass().getMethod("setCategory", String.class);
                method2.invoke(controller, gameNames[currentIndex]);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace(); // Handle exceptions appropriately
            }
            currentstage.setScene(new Scene(root));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    // Back to home button action
    public void backToHome() {
        changeHome(0);
        animateAnchorPane(CurrentAnchorPane);
    }

    // Change home tab
    private void changeHome(int number) {
        RotateThis(number);
    }

    // Rotate animation for changing tabs
    private void RotateThis(int newCurrent) {
        RotateTransition rotateTransitionBranch = new RotateTransition(Duration.seconds(0.5), leaves);
        rotateTransitionBranch.setFromAngle(current);
        rotateTransitionBranch.setToAngle(newCurrent);
        current = newCurrent;
        rotateTransitionBranch.play();
    }

    // Change anchor pane for tab transition
    private void changeAnchor(String TabName) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(TabName + ".fxml"));
        Parent root = loader.load();
        HomeTabController controller = loader.getController();
        controller.setCurrentAnchorPane(CurrentAnchorPane);
        controller.setToolBarL(toolbarL);
        controller.setLeaves(leaves);
        CurrentAnchorPane.getChildren().clear();
        CurrentAnchorPane.getChildren().add(root);
    }

    // Animate anchor pane for tab transition
    private void animateAnchorPane(AnchorPane anchorPane) {
        TranslateTransition transition = new TranslateTransition(Duration.millis(400), anchorPane);
        transition.setToY(anchorPane.getHeight());
        transition.setOnFinished(event -> {
            try {
                changeAnchor("HomeTab");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            TranslateTransition resetTransition = new TranslateTransition(Duration.millis(400), anchorPane);
            resetTransition.setToY(0);
            resetTransition.play();
        });

        transition.play();
    }

    // Setters for initializing values
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
