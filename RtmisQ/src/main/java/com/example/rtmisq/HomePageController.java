package com.example.rtmisq;

import com.example.rtmisq.StaticData.CurrentUser;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXNotificationCenter;
import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class HomePageController extends CommonRtmisQ implements Initializable {
    //Set Variables from FXML
    public MFXButton Account,Logout,About,Help,Settings;
    public Label welcomeLabel;
    @FXML
    private ImageView leaves;
    @FXML
    private VBox vBoxMenu;
    @FXML
    private ToolBar toolbarL;
    @FXML
    private AnchorPane CurrentAnchorPane;
    private long totalScreenTime;

    //Set Local Variables
    GetConfigure getConfigure =new GetConfigure();
    ResourceBundle bundle=getConfigure.PropertiesConnection();
    Connection conn =null;


    //Code for what to do when open HomePage.fxml
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        conn=new DBConnection().Connectiondb();
        totalScreenTime= CurrentUser.getTime();
        super.setToolbarL(toolbarL);
        welcomeLabel.setText("Welcome Back,"+ CurrentUser.getUserFirstName());


        Timeline timelineScreen = new Timeline(new KeyFrame(Duration.seconds(60), this::updateScreenTime));
        timelineScreen.setCycleCount(Timeline.INDEFINITE);
        timelineScreen.play();


        try {
            Account.setText(bundle.getString("Account"));
            Logout.setText(bundle.getString("Logout"));
            About.setText(bundle.getString("About"));
            Settings.setText(bundle.getString("Settings"));
            Help.setText(bundle.getString("Help"));
            changeAnchor();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    private void updateDatabase(){
        System.out.println("i am in function");
        try {
            String ResultQuery = "Update Users SET time=? where username=?;";
            PreparedStatement resultstmt = conn.prepareStatement(ResultQuery);
            resultstmt.setLong(1, (int) totalScreenTime);
            resultstmt.setString(2, CurrentUser.getLoggedUserName());
            resultstmt.executeUpdate();
        }catch (SQLException e){
            System.out.println(e);
        }
    }
    protected void updateScreenTime(ActionEvent event) {
        totalScreenTime += 1;
        updateDatabase();
    }

    //Code for Change the Current AnchorPane to  HomeTab
    private void changeAnchor() throws IOException {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("HomeTab" +".fxml"));
            Parent root =loader.load();
            HomeTabController controller = loader.getController();
            controller.setLeaves(leaves);
            controller.setToolBarL(toolbarL);
            controller.setCurrentAnchorPane(CurrentAnchorPane);
            CurrentAnchorPane.getChildren().clear();
            CurrentAnchorPane.getChildren().add(root);
            getConfigure=null;
    }


    //Code for HamburgerMenu
    @FXML
    public void HamburgerMenu() {
        vBoxMenu.setVisible(!vBoxMenu.isVisible());
    }

    @FXML
    public void JustClickBackground() {
        vBoxMenu.setVisible(false);
    }
    @FXML
    private void aboutFunction() {
        ViewAlert  viewAlert =new ViewAlert();
        boolean Choice =viewAlert.setAlert("RtmisQ\nversion: 0.0.1",true,false,toolbarL);

    }
    public void Logout() {
        CurrentAnchorPane.setOpacity(0.3);
        ViewAlert  viewAlert =new ViewAlert();
        boolean Choice =viewAlert.setAlert(bundle.getString("closetext"), false,true,toolbarL);

        if(Choice){
            CurrentUser.setLoggedUserName(null);
            CurrentUser.setTime(0);
            CurrentUser.setUserLanguage(null);
            CurrentUser.setUserFirstName(null);
            LoadingPageController.mediaPlayer.stop();
            ChangeFXML changeFXML=new ChangeFXML();
            changeFXML.SetFXML(toolbarL,"LoginForm");
        }else{
            CurrentAnchorPane.setOpacity(1);
        }
    }

}
