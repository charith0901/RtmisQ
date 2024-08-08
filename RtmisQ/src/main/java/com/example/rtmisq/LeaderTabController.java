package com.example.rtmisq;

import io.github.palexdev.materialfx.controls.MFXPagination;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class LeaderTabController implements Initializable {
    @FXML
    private Label P1, P2, P3, P4, P5, P6, P7, M1, M2, M3, M4, M5, M6, M7, N1, N2, N3, N4, N5, N6, N7;
    @FXML
    private MFXPagination mfxPagination;
    private ImageView leaves;
    private AnchorPane CurrentAnchorPane;
    private ToolBar toolbarL;
    private int current;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateLeaderBoard(0);
        mfxPagination.currentPageProperty().addListener((observable, oldValue, newValue) -> onPageChange(newValue.intValue()));
    }
    private void onPageChange(int pageNumber) {
        Label[] labelArrayN = new Label[]{N1, N2, N3, N4, N5, N6, N7};
        for (int i = (7 * (pageNumber - 1)) + 1, j = 0; i <= (pageNumber * 7); i++, j++) {
            labelArrayN[j].setText(String.valueOf(i));
            updateLeaderBoard(pageNumber-1);
        }
    }
    protected void updateLeaderBoard(int PageNumber) {
        Label[] labelArrayP = new Label[]{P1, P2, P3, P4, P5, P6, P7};
        Label[] labelArrayM = new Label[]{M1, M2, M3, M4, M5, M6, M7};
        for (Label label : labelArrayM) {
            label.setText("");
        }
        for (Label label : labelArrayP) {
            label.setText("");
        }
        // Example code for database connection and query
        try {
            Connection conn = new DBConnection().Connectiondb();
            String Squery = "select * from Users order by Users.marks desc LIMIT 7 OFFSET ?;";
            assert conn != null;
            PreparedStatement stmt = conn.prepareStatement(Squery);
            stmt.setInt(1,PageNumber*7);
            ResultSet res = stmt.executeQuery();
            int i = 0;
            while (res.next()) {
                String s = res.getString("firstname");
                int num = res.getInt("marks");
                labelArrayP[i].setText(s);
                labelArrayM[i].setText(String.valueOf(num));
                i++;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }


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
    private void changeAnchor(String TabName) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(TabName+".fxml"));
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


    public void setToolBarL(ToolBar toolbarL) {
        this.toolbarL=toolbarL;
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
}
