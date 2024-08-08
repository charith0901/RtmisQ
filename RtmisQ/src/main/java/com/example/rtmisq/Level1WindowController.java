package com.example.rtmisq;


import animatefx.animation.AnimationFX;
import animatefx.animation.Bounce;
import animatefx.animation.Pulse;
import animatefx.animation.Shake;
import com.example.rtmisq.StaticData.CurrentUser;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.input.MouseEvent;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;


public class Level1WindowController extends CommonRtmisQ implements Initializable {

    @FXML
    private ImageView btn1Image;
    @FXML
    private ImageView btn2Image;
    @FXML
    private ImageView btn3Image;
    @FXML
    private ImageView btn4Image;
    @FXML
    private ImageView btn5Image;
    @FXML
    private ImageView btn6Image;
    @FXML
    private ImageView btn7Image;
    @FXML
    private ImageView btn8Image;
    @FXML
    private ImageView btn9Image;
    @FXML
    private ImageView btnBackImage;
    @FXML
    public AnchorPane level1WindowPane;
    @FXML
    private Label levelLabel;
    @FXML
    private ToolBar toolbarL;
    private int QuestionNumber;
    private String Category;
    Connection conn = new DBConnection().Connectiondb();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setToolbarL(toolbarL);
    }

    public void SetValues(int number){
        levelLabel.setText("Level"+(number+1));
    }

    public void setQuestionNumber(String Category) {
        this.Category=Category;
        try {
            String LevelQuery = "select * from Users where username = ? ;";
            assert conn != null;
            PreparedStatement stmt = conn.prepareStatement(LevelQuery);
            stmt.setString(1, CurrentUser.getLoggedUserName());
            ResultSet levelRes = stmt.executeQuery();
            QuestionNumber=levelRes.getInt(Category+"_l");
            levelRes.close();
        }catch(SQLException e){
            System.out.println(e);
        }
        ImageView[] imageViews={btn1Image,btn2Image,btn3Image,btn4Image,btn5Image,btn6Image,btn7Image,btn8Image,btn9Image};
        for (int i = 0; i < 9; i++) {
            if(i==QuestionNumber){
                Pulse pulse=new Pulse(imageViews[i]);
                pulse.setCycleCount(AnimationFX.INDEFINITE);
                pulse.play();
            }else{
                imageViews[i].setDisable(true);
            }
        }
        try{
            conn.close();
        }catch (SQLException e){
            System.out.println(e);
        }
    }

    @FXML
    public void backBtnOnAction(MouseEvent event) throws SQLException {
        try {
            Stage currentstage =(Stage)toolbarL.getScene().getWindow();
            FXMLLoader loader =new FXMLLoader(getClass().getResource("levelWindow.fxml"));
            Parent root =loader.load();
            Object controller = loader.getController();
            try {
                Method method = controller.getClass().getMethod("setMovablePage", Stage.class);
                method.invoke(controller,currentstage);
                Method method2 = controller.getClass().getMethod("setCategory", String.class);
                method2.invoke(controller,Category);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace(); // Handle exceptions appropriately
            }
            currentstage.setScene(new Scene(root));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        conn.close();

    }
    @FXML
    public void Btn1OnAction(MouseEvent event){
        ImageView[] imageViews={btn1Image,btn2Image,btn3Image,btn4Image,btn5Image,btn6Image,btn7Image,btn8Image,btn9Image};
        ImageView clickedImageView= (ImageView) event.getSource();
        for (int i = 0; i < imageViews.length ; i++) {
            if (clickedImageView==imageViews[i]){

                try {
                    Stage currectstage=(Stage)level1WindowPane.getScene().getWindow();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("QuestionWindow.fxml"));
                    Parent root = loader.load();

                    //change values of fxml
                    QuestionWindowController questionWindowController=loader.getController();
                    questionWindowController.setCategory(Category);
                    questionWindowController.setQuestionValue();
                    questionWindowController.setMovablePage(currectstage);



                    currectstage.setScene(new Scene(root));
                    currectstage.show();

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }


            }

        }

    }
    //code for ToolBar




}
