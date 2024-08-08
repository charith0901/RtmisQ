package com.example.rtmisq;

import animatefx.animation.AnimationFX;
import animatefx.animation.Bounce;
import animatefx.animation.Pulse;
import animatefx.animation.Shake;
import com.example.rtmisq.StaticData.CurrentUser;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;


public class LevelWindowController extends CommonRtmisQ implements Initializable {
    public Label CategoryLabel;
    public ImageView bgImage;
    @FXML
    private ImageView btnLevel1;
    @FXML
    private ImageView btnLevel2;
    @FXML
    private ImageView btnLevel3;
    @FXML
    private ImageView btnLevel4;
    @FXML
    public AnchorPane levelWindowPane;
    @FXML
    private ToolBar toolbarL;
    private String Category;
    private int CurrentLevel;
    Connection conn = new DBConnection().Connectiondb();



    @FXML
    public void btnLevelAction(MouseEvent event){
        super.playClick();
        ImageView[] imageViews={btnLevel1,btnLevel2,btnLevel3,btnLevel4};
        ImageView clickedImageView= (ImageView) event.getSource();
        for (int i = 0; i < imageViews.length ; i++) {
            if (clickedImageView==imageViews[i]){

                try {
                    Stage currectstage=(Stage)levelWindowPane.getScene().getWindow();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("Level1Window.fxml"));
                    Parent root = loader.load();

                    //change values of fxml
                    Level1WindowController level1WindowController=loader.getController();
                    level1WindowController.SetValues((i));
                    level1WindowController.setMovablePage(currectstage);
                    level1WindowController.setQuestionNumber(Category);


                    currectstage.setScene(new Scene(root));
                    currectstage.show();

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }


            }

        }

    }

    public void backBtnOnAction(MouseEvent event) throws SQLException {
        super.playClick();
        ChangeFXML changeFXML=new ChangeFXML();
        changeFXML.SetFXML(toolbarL,"HomePage");
        conn.close();
    }


    public void setCategory(String category) {
        Category = category;
        CategoryLabel.setText(category);
        bgImage.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("HomePage/"+category+".jfif"))));
        try {
            String LevelQuery = "select * from Users where username = ? ;";
            assert conn != null;
            PreparedStatement stmt = conn.prepareStatement(LevelQuery);
            stmt.setString(1, CurrentUser.getLoggedUserName());
            ResultSet levelRes = stmt.executeQuery();
            CurrentLevel=levelRes.getInt(Category);
            levelRes.close();
        }catch(SQLException e){
            System.out.println(e);
        }
        ImageView[] imageViews={btnLevel1,btnLevel2,btnLevel3,btnLevel4};
        for (int i = 0; i < 4; i++) {
            if(i==CurrentLevel){
                Pulse pulse=new Pulse(imageViews[i]);
                pulse.setCycleCount(AnimationFX.INDEFINITE);
                pulse.play();
            }else{
                imageViews[i].setDisable(true);
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        super.setToolbarL(toolbarL);
    }


}
