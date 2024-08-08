package com.example.rtmisq;


import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.Objects;
import java.util.ResourceBundle;

public class SignupFormController extends CommonRtmisQ implements Initializable {
    public MFXTextField txtLastName;
    public MFXTextField txtFirstName;
    public MFXPasswordField txtPassword;
    public AnchorPane context;
    public MFXTextField txtUserName;
    public MFXPasswordField txtPasswordAgain;
    public MFXDatePicker BirthDay;
    @FXML
    private MFXComboBox<String> LanguageChoice;
    @FXML
    private ToolBar toolbarL;
    private final String[] Languages={"සිංහල","English","தமிழ்"};

    Connection conn=null;



    public void Signup() throws SQLException {
        conn=new DBConnection().Connectiondb();
        ViewAlert viewAlert=new ViewAlert();
        String FirstName=txtFirstName.getText();
        String LastName=txtLastName.getText();
        String UserName=txtUserName.getText();
        String Password=txtPassword.getText();
        String PasswordAgain=txtPasswordAgain.getText();
        String LanguagePick =LanguageChoice.getValue();
        LocalDate localDate=BirthDay.getValue();

        String CheckQuery = "select * from Users where username like ? ;";
        assert conn != null;
        PreparedStatement stmt = conn.prepareStatement(CheckQuery);
        stmt.setString(1, UserName);
        ResultSet SignupSet = stmt.executeQuery();
        if(Objects.equals(FirstName, "") || Objects.equals(LastName, "") || Objects.equals(UserName, "") || Objects.equals(Password, "") || Objects.equals(PasswordAgain, "") || Objects.equals(localDate, null) || Objects.equals(LanguagePick, "") )
        {
            viewAlert.setAlert("Fill All",true,false,toolbarL);
        } else if (SignupSet.next()) {
            viewAlert.setAlert("User Exist",true,false,toolbarL);
            SignupSet.close();
        } else if (!Objects.equals(Password, PasswordAgain)) {
            viewAlert.setAlert("Passwords mis-match",true,false,toolbarL);
        }else{
            String Signupquery ="insert into Users values(?,?,?,?,?,0,0,0,0,0,0,0,0,0,0,0,0,0,?,0,0,0);";
            assert conn != null;
            PreparedStatement SignStmt =conn.prepareStatement(Signupquery);
            SignStmt.setString(1,UserName);
            SignStmt.setString(2,FirstName);
            SignStmt.setString(3,LastName);
            SignStmt.setString(4,Password);
            SignStmt.setString(5,LanguagePick);
            SignStmt.setDate(6, Date.valueOf(localDate));
            try {
                SignStmt.executeUpdate();
                viewAlert.setAlert("Success",true,false,toolbarL);
                setUi();
                conn=null;
            }catch (Exception e){
                viewAlert.setAlert(e.getMessage(),true,false,toolbarL);
            }
        }

    }

    public void alreadyHaveAnAccountOnAction(ActionEvent actionEvent)throws IOException {
        setUi();
    }
    private void setUi() throws IOException {
        Stage stage= (Stage) context.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("LoginForm" + ".fxml")))));
        stage.centerOnScreen();

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        LanguageChoice.getItems().addAll(Languages);
        super.setToolbarL(toolbarL);
    }
}
