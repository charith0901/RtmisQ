package com.example.rtmisq;

//import com.developersstack.wildlife.db.Database;
//import com.developersstack.wildlife.model.User;
import animatefx.animation.FadeIn;
import com.example.rtmisq.StaticData.CurrentUser;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoginFormController extends CommonRtmisQ implements Initializable {
    public MFXTextField txtUserName;
    @FXML
    private MFXPasswordField txtPassword;
    @FXML
    private AnchorPane context;
    @FXML
    private TextField txtEmail;
    @FXML
    private ToolBar toolbarL;
    Connection conn = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.setToolbarL(toolbarL);
    }


    public void loginOnAction() throws SQLException {
        ViewAlert viewAlert =new ViewAlert();
        if(!Objects.equals(txtPassword.getText(), "") && !Objects.equals(txtUserName.getText(), "")) {
            conn = new DBConnection().Connectiondb();
            String Logquery = "select * from Users where username = ? and password = ? ;";
            assert conn != null;
            PreparedStatement stmt = conn.prepareStatement(Logquery);
            stmt.setString(1, txtUserName.getText());
            stmt.setString(2, txtPassword.getText());
            ResultSet Logset = stmt.executeQuery();
            if (Logset.next()) {
                CurrentUser.setLoggedUserName(Logset.getString("username"));
                CurrentUser.setUserFirstName(Logset.getString("firstname"));
                CurrentUser.setTime(Logset.getInt("time"));
                if (Objects.equals(Logset.getString("language"), "English")) {
                    CurrentUser.setUserLanguage("English");
                } else if (Objects.equals(Logset.getString("language"), "සිංහල")) {
                    CurrentUser.setUserLanguage("Sinhala");
                } else if (Objects.equals(Logset.getString("language"), "தமிழ்")) {
                    CurrentUser.setUserLanguage("Tamil");
                }
                setUi("LoadingPage");
                conn = null;
            } else {
                viewAlert.setAlert("Wrong Inputs",true,false,toolbarL);
            }
        }else{
            viewAlert.setAlert("Fill All",true,false,toolbarL);
        }


    }

    public void createAnAccountOnAction(ActionEvent actionEvent) {
        setUi("SignupForm");
    }

    public void frogotPasswordOnAction(ActionEvent actionEvent) {
    }
    private void setUi(String location) {
        try {
            Stage currentstage =(Stage)toolbarL.getScene().getWindow();
            currentstage.close();
            FXMLLoader loader =new FXMLLoader(getClass().getResource(location+".fxml"));
            Parent root =loader.load();
            Object controller = loader.getController();
            try {
                Method method = controller.getClass().getMethod("setMovablePage", Stage.class);
                method.invoke(controller,currentstage);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace(); // Handle exceptions appropriately
            }
            Stage stage=new Stage();
            stage.setScene(new Scene(root));
            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    public void closewindow(){

        ViewAlert  viewAlert =new ViewAlert();
        boolean Choice =viewAlert.setAlert("really want to close",false,true,toolbarL);

        if(Choice) {
            Stage currentStage = (Stage) toolbarL.getScene().getWindow();
            currentStage.close();
        }
    }


}
