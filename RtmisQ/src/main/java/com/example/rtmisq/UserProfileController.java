package com.example.rtmisq;

import com.example.Main;
import com.example.rtmisq.StaticData.CurrentUser;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.sql.*;
import java.time.LocalDate;
import java.util.Objects;



public class UserProfileController {

    public MFXComboBox<String> LanguagePick;
    public MFXPasswordField PasswordField;
    @FXML
    private ImageView profileImageView;

    @FXML
    private Button profilePictureButton;

    @FXML
    private Label userNameLabel;

    @FXML
    private MFXTextField firstNameTextField;

    @FXML
    private MFXTextField lastNameTextField;
    @FXML
    private ToolBar toolbarL;
    private final String[] Languages={"සිංහල","English","தமிழ்"};

    @FXML
    private void initialize() throws SQLException {
        LanguagePick.getItems().addAll(Languages);
       Connection conn=new DBConnection().Connectiondb();
       String Query="select * from Users where username=?;";
       PreparedStatement preparedStatement= conn.prepareStatement(Query);
       preparedStatement.setString(1,CurrentUser.getLoggedUserName());
       ResultSet resultSet=preparedStatement.executeQuery();
       firstNameTextField.setText(resultSet.getString("firstname"));
       LanguagePick.setValue(resultSet.getString("language"));
       LanguagePick.setText(resultSet.getString("language"));
       userNameLabel.setText(CurrentUser.getLoggedUserName());
       lastNameTextField.setText(resultSet.getString("lastname"));
       resultSet.close();
       conn.close();

    }

    @FXML
    private void handleSaveChanges() {
        ViewAlert viewAlert=new ViewAlert();
        String firstName = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();
        String password =PasswordField.getText();
        String language =LanguagePick.getValue();

    if (!Objects.equals(firstNameTextField.getText(), "") && !Objects.equals(lastNameTextField.getText(), "") && !Objects.equals(PasswordField.getText(), "") && !Objects.equals(LanguagePick.getText(), "")){
        String updatedUserName = firstName + " " + lastName;
        userNameLabel.setText(updatedUserName);

        try {
            Connection conn = new DBConnection().Connectiondb();
            String Signupquery = "update Users SET firstname=?,lastname=?,language=?,password=? where username=?;";
            assert conn != null;
            PreparedStatement SignStmt = conn.prepareStatement(Signupquery);
            SignStmt.setString(1, firstName);
            SignStmt.setString(2, lastName);
            SignStmt.setString(3, language);
            SignStmt.setString(4, password);
            SignStmt.setString(5, CurrentUser.getLoggedUserName());
            SignStmt.executeUpdate();
            viewAlert.setAlert("Success", true, false, toolbarL);
    }catch (SQLException e){
            e.printStackTrace();
            //viewAlert.setAlert(e.getMessage(),true,false,toolbarL);
        }
    }else {
            viewAlert.setAlert("Fill All",true,false,toolbarL);
    }
    }

  public void handleUploadProfilePicture() {
//        FileChooser fileChooser = new FileChooser();
//        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
//        Stage stage = new Stage();
//        stage.setTitle("Select Profile Picture");
//        File selectedFile = fileChooser.showOpenDialog(stage);
//
//        if (selectedFile != null) {
//            // Update the profileImageView with the selected image
//            Image selectedImage = new Image(selectedFile.toURI().toString());
//            profileImageView.setImage(selectedImage);
//        }
    }
    public void minimizewindow(){
        Stage stage= (Stage) toolbarL.getScene().getWindow();
        stage.setIconified(true);
    }
    public void closewindow(){
        Stage stage= (Stage) toolbarL.getScene().getWindow();
        stage.close();
    }
    public void setMovablePage(Stage currentStage){
        MovablePage movablePage=new MovablePage(0.0,0.0,toolbarL,currentStage);
        movablePage.movePage();
    }

}