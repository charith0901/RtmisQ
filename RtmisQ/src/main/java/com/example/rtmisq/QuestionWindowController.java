package com.example.rtmisq;

import com.example.rtmisq.StaticData.CurrentUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

public class QuestionWindowController extends CommonRtmisQ implements Initializable {

    // FXML elements defined in the corresponding FXML file
    // (Note: These annotations are used by JavaFX to inject the UI components)
    public TextArea QuestionText;
    public RadioButton Ans1;
    public ToggleGroup Answers;
    public RadioButton Ans3;
    public RadioButton Ans2;
    public RadioButton Ans4;
    public AnchorPane ResultAnchorPane;
    public AnchorPane BaseAnchorPane;
    public ImageView celebrateIMG;
    public ImageView ShineIMG;
    public ImageView ResultIMG;
    public TextArea DescriptionTxt;
    public AnchorPane DescriptionAnchor;
    @FXML
    private Label QuestionLabel;
    @FXML
    private ToolBar toolbarL;
    public Button HomeBtn;
    public Button LookBackBtn;
    public Button BackBtn;
    public Button NextBtn;

    // SQL query for updating user marks
    protected String ResultQuery = "Update Users SET marks=marks+? where username=?;";

    // Variables to store question information
    private int Qmark;
    private String Category;
    private String QAnswer;

    // Database connection and related objects
    DBConnection dbConnection = new DBConnection();
    Connection conn = dbConnection.Connectiondb();

    // Variable to store the next question number
    private int NextQuestionNumber;

    // Initialization method for the controller
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.setToolbarL(toolbarL);
    }

    // Method to set the value of the current question
    public void setQuestionValue() {
        ChangeQuestion();
        NextQuestionNumber = getCurrentNumber();
    }

    // Method to change the current question
    public void ChangeQuestion() {
        // SQL query to retrieve the next question based on language and category
        String GetQuesQuery = (Objects.equals(CurrentUser.getUserLanguage(), "English")) ?
                "select * from " + Category + " order by qnumber LIMIT 1 OFFSET ?;" :
                "select * from " + Category + "_s" + " order by qnumber LIMIT 1 OFFSET ?;";

        int levelNumber = 0;

        // Retrieve the user's level for the current category
        try {
            String LevelQuery = "select * from Users where username = ? ;";
            assert conn != null;
            PreparedStatement stmt = conn.prepareStatement(LevelQuery);
            stmt.setString(1, CurrentUser.getLoggedUserName());
            ResultSet levelRes = stmt.executeQuery();
            levelNumber = levelRes.getInt(Category);
            levelRes.close();
        } catch (SQLException e) {
            System.out.println(e);
        }

        // Retrieve the next question from the database
        try {
            PreparedStatement stmt = conn.prepareStatement(GetQuesQuery);
            stmt.setInt(1, (getCurrentNumber() + levelNumber * 9));
            ResultSet res = stmt.executeQuery();
            if (res.next()) {
                String Question =res.getString("question");
                QuestionText.setText(Question);
                Ans1.setText(res.getString("ans1"));
                Ans2.setText(res.getString("ans2"));
                Ans3.setText(res.getString("ans3"));
                Ans4.setText(res.getString("ans4"));
                QAnswer = res.getString("answer");
                Qmark = res.getInt("marks");
                String QDescription = res.getString("description");
                DescriptionTxt.setText("Question:"+Question+"\nAnswer:"+QAnswer+"\nExplanation:"+QDescription);
            }
            res.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
        QuestionLabel.setText("Question " + (getCurrentNumber() + 1));
    }

    // Method to set the category for the current question
    public void setCategory(String category) {
        Category = category;
    }

    // Method to handle submission of the answer
    public void submit() {
        RadioButton SelectedValue = (RadioButton) Answers.getSelectedToggle();
        if (SelectedValue == null) {
            // Show an alert if no answer is selected
            ViewAlert viewAlert = new ViewAlert();
            viewAlert.setAlert("select an answer", true, false,toolbarL);
        } else {
            //update database that a question is answered by user
            updateanswered();

            // Disable the base anchor pane and show the result anchor pane
            BaseAnchorPane.setDisable(true);
            ResultAnchorPane.setVisible(true);

            // Check if the selected answer is correct
            if (Objects.equals(QAnswer, SelectedValue.getText())) {
                playSuccess();
                celebrateIMG.setVisible(true);
                ShineIMG.setVisible(true);
                ResultIMG.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("QuestionWindowImages/victory.png"))));
                System.out.println(true);
                try {
                    // Update the user's marks in the database
                    PreparedStatement resultstmt = conn.prepareStatement(ResultQuery);
                    resultstmt.setInt(1, Qmark);
                    resultstmt.setString(2, CurrentUser.getLoggedUserName());
                    resultstmt.executeUpdate();
                } catch (SQLException e) {

                    System.out.println(e);
                }
            }else{
                playFail();
                //update database that the user has a wrong answer
                updatewrong();

                celebrateIMG.setVisible(false);
                ShineIMG.setVisible(false);
                ResultIMG.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("QuestionWindowImages/defeat.png"))));
                System.out.println(false);
            }

                // Check if it's the last question in the level
                if (getCurrentNumber() == 8) {
                    NextQuestionNumber = 0;
                    try {
                        // Update the user's level for the category
                        String BaseLevelQuery = "Update Users SET " + Category + "=" + Category + "+1 where username=?;";
                        PreparedStatement resultstmt = conn.prepareStatement(BaseLevelQuery);
                        resultstmt.setString(1, CurrentUser.getLoggedUserName());
                        resultstmt.executeUpdate();
                    } catch (SQLException e) {
                        System.out.println(e);
                    }
                } else {
                    // Increment the question number for the next round
                    NextQuestionNumber++;
                }

                // Update the user's level for the current category
                try {
                    String LevelQuery = "Update Users SET " + Category + "_l=? where username like ? ;";
                    PreparedStatement resultstmt = conn.prepareStatement(LevelQuery);
                    resultstmt.setInt(1, NextQuestionNumber);
                    resultstmt.setString(2, CurrentUser.getLoggedUserName());
                    resultstmt.executeUpdate();
                } catch (SQLException e) {
                    System.out.println(e + "hi");
                }

        }
    }
    //update database that a question is answered by user
    public void updateanswered(){
        String updateanswers="Update Users SET done=done+1 where username like ?";
        try {
            PreparedStatement updanswer=conn.prepareStatement(updateanswers);
            updanswer.setString(1,CurrentUser.getLoggedUserName());
            updanswer.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    //update database that the user has a wrong answer
    public void updatewrong(){
        String updatewrongs="Update Users SET wrong=wrong+1 where username like ?";
        try {
            PreparedStatement updwrong=conn.prepareStatement(updatewrongs);
            updwrong.setString(1,CurrentUser.getLoggedUserName());
            updwrong.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    // Method to get the current question number
    public int getCurrentNumber() {
        int questionLevelNumber = 0;
        try {
            String LevelQuery1 = "select * from Users where username = ? ;";
            assert conn != null;
            PreparedStatement stmt = conn.prepareStatement(LevelQuery1);
            stmt.setString(1, CurrentUser.getLoggedUserName());
            ResultSet levelRes = stmt.executeQuery();
            questionLevelNumber = levelRes.getInt(Category + "_l");
            levelRes.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
        return questionLevelNumber;
    }

    // Method to clear radio buttons
    public void ClearRadio(boolean bool) {
        RadioButton[] radioButtons = {Ans1, Ans2, Ans3, Ans4};
        for (RadioButton radioButton : radioButtons) {
            radioButton.setSelected(bool);
        }
    }

    // Method to handle button actions on congrats page
    public void setAction(ActionEvent event) {
        ResultAnchorPane.setVisible(false);
        BaseAnchorPane.setDisable(false);
        DescriptionAnchor.setVisible(false);
        Button clickedBtn = (Button) event.getSource();
        if (clickedBtn == HomeBtn) {
            // Navigate to the home page
            ChangeFXML changeFXML = new ChangeFXML();
            changeFXML.SetFXML(toolbarL, "HomePage");
            try {
                conn.close();
            } catch (SQLException e) {
                System.out.println(e);
            }
        } else if (clickedBtn == LookBackBtn) {
            ResultAnchorPane.setVisible(true);
            BaseAnchorPane.setDisable(true);
            DescriptionAnchor.setVisible(true);

        } else if (clickedBtn == BackBtn) {
            // Navigate to the previous level
            int levelNumber = 0;
            try {
                String LevelQuery = "select * from Users where username = ? ;";
                assert conn != null;
                PreparedStatement stmt = conn.prepareStatement(LevelQuery);
                stmt.setString(1, CurrentUser.getLoggedUserName());
                ResultSet levelRes = stmt.executeQuery();
                levelNumber = levelRes.getInt(Category);
                levelRes.close();
            } catch (SQLException e) {
                System.out.println(e);
            }
            try {
                // Load the FXML for the previous level
                Stage currectstage = (Stage) QuestionText.getScene().getWindow();
                FXMLLoader loader2 = new FXMLLoader(getClass().getResource("Level1Window.fxml"));
                Parent root2 = loader2.load();

                // Set values for the FXML elements
                Level1WindowController level1WindowController = loader2.getController();
                level1WindowController.SetValues((levelNumber));
                level1WindowController.setMovablePage(currectstage);
                level1WindowController.setQuestionNumber(Category);

                currectstage.setScene(new Scene(root2));
                currectstage.show();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                conn.close();
            } catch (SQLException e) {
                System.out.println(e);
            }
        } else if (clickedBtn == NextBtn) {
            // Change the current question
            ChangeQuestion();
            ClearRadio(false);
        }

    }

    // Method to handle window close action
    public void closewindow() {
        // Show an alert indicating that the window cannot be closed
        ViewAlert viewAlert = new ViewAlert();
        viewAlert.setAlert("you cant exit", true, false,toolbarL);
    }
}
