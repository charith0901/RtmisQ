package com.example.rtmisq;

import animatefx.animation.AnimationFX;
import animatefx.animation.Pulse;
import com.example.rtmisq.StaticData.CurrentUser;
import io.github.palexdev.materialfx.controls.MFXProgressSpinner;
import javafx.animation.*;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;


public class HomeTabController extends CommonRtmisQ implements Initializable {

    public Label learnLabel;
    public Label playLabel;
    @FXML
    private MFXProgressSpinner mfxProgressSpinnerSuccess, mfxProgressSpinnerCompleted;
    @FXML
    private Pane paneLearn,paneGame,paneLeader,tempbtn,PaneSetting;

    @FXML
    private Label hrs, mins,SuccessRate,ScreenTime,Completed;
    private long totalScreenTime;
    protected int current = 0;
    private AnchorPane CurrentAnchorPane;
    private ToolBar toolbarL;
    private ImageView  leaves;

    //define instances of connections
    GetConfigure getConfigure =new GetConfigure();
    ResourceBundle bundle=getConfigure.PropertiesConnection();


    Connection conn =null;
    Timeline timelineScreen,timeline1;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        conn=new DBConnection().Connectiondb();
        int done=getdone();
        double doneRate=((double) done /216)*100.0;
        double wrongRate=((double) (done-getwrongs()) /done)*100.0;
        System.out.println(doneRate+""+getwrongs());
        simulateLoading(doneRate, mfxProgressSpinnerCompleted);
        simulateLoading(wrongRate, mfxProgressSpinnerSuccess);
        try {
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        timelineScreen = new Timeline(new KeyFrame(Duration.seconds(60), this::updateScreenTime));
        timelineScreen.setCycleCount(Timeline.INDEFINITE);
        timelineScreen.play();

        HomeAttribute();
        changeHome(0);
        totalScreenTime= CurrentUser.getTime();
        updateTimegrid();
        SuccessRate.setText(bundle.getString("SuccessRate"));
        Completed.setText(bundle.getString("Complete"));
        ScreenTime.setText(bundle.getString("ScreenTime"));
        learnLabel.setText(bundle.getString("Learn"));
        playLabel.setText(bundle.getString("Play"));
    }
    public int getdone(){
        int done=0;
        try {
            String donestmt = "Select done from Users where username=?";
            PreparedStatement donepstmt = conn.prepareStatement(donestmt);
            donepstmt.setString(1,CurrentUser.getLoggedUserName());
            ResultSet rs=donepstmt.executeQuery();
            done=rs.getInt(1);
            rs.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return done;
    }
    public int getwrongs(){
        int wrongs=0;
        try {
            String donestmt = "Select wrong from Users where username=?";
            PreparedStatement donepstmt = conn.prepareStatement(donestmt);
            donepstmt.setString(1,CurrentUser.getLoggedUserName());
            ResultSet rs=donepstmt.executeQuery();
            wrongs=rs.getInt(1);
            rs.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return wrongs;
    }



    private void simulateLoading(double QValue, MFXProgressSpinner spinner) {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                for (double i = 0; i <= QValue; i=i+1) {
                    final double progress = i / 100.0;
                    updateProgress(progress, 1.0);
                    try {
                        Thread.sleep(25);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }
        };
        spinner.progressProperty().bind(task.progressProperty());
        new Thread(task).start();
    }
    private void HomeAttribute(){
        //call pulse animation
        Pulse pulse =new Pulse(paneGame);
        pulse.setCycleCount(AnimationFX.INDEFINITE);
        pulse.play();



        timeline1 = new Timeline();
        KeyValue kv1,kv2,kv3,kv4;




            // Add keyframes for preferred heights
            kv1 = new KeyValue(paneLearn.prefHeightProperty(), 400);
            kv2 = new KeyValue(paneGame.prefHeightProperty(), 400);
            kv3 = new KeyValue(paneLeader.opacityProperty(),1);
            kv4 = new KeyValue(tempbtn.opacityProperty(),1);


            // Add keyframes to the timeline


        KeyFrame keyFrame = new KeyFrame(Duration.seconds(1.2), kv1, kv2,kv3,kv4);
        timeline1.getKeyFrames().add(keyFrame);

        // Play the animation when the stage is shown
        timeline1.play();
        timeline1=null;
    }
    protected void updateScreenTime(ActionEvent event) {
        totalScreenTime += 1;
        updateTimegrid();
    }
    protected void updateTimegrid(){
        int hours = (int) (totalScreenTime / 60);
        int minutes = (int) (totalScreenTime % 60);
        hrs.setText(hours +"-"+ bundle.getString("Hours"));
        mins.setText(minutes + "-"+bundle.getString("Minutes"));
    }

    @FXML
    private void ButtonEvent(MouseEvent event) throws SQLException {
       Pane[] panes = {paneGame,paneLeader,tempbtn,PaneSetting};

        Pane clickedButton = (Pane)event.getSource();
        // clickedButton.setStyle("-fx-background-color:lightgreen");
        super.playClick();
        if (clickedButton == paneLeader) {
            changeHome(30);
            animateAnchorPane(CurrentAnchorPane,"LeaderTab");

        } else if (clickedButton == paneGame) {
            changeHome(60);
            animateAnchorPane(CurrentAnchorPane,"GameTab");


        } else if (clickedButton == paneLearn) {
            changeHome(90);

            animateAnchorPane(CurrentAnchorPane,"LearnTab");
        } else if (clickedButton == PaneSetting) {
            changeHome(120);
            animateAnchorPane(CurrentAnchorPane,"ProfileTab");

        }
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
        Object controller = loader.getController();
        CurrentAnchorPane.getChildren().clear();
        CurrentAnchorPane.getChildren().add(root);
        try {
            Method methodtoolbar = controller.getClass().getMethod("setToolBarL", ToolBar.class);
            methodtoolbar.invoke(controller,toolbarL);
            Method methodanchor = controller.getClass().getMethod("setCurrentAnchorPane", AnchorPane.class);
            methodanchor.invoke(controller,CurrentAnchorPane);
            Method methodimage = controller.getClass().getMethod("setLeaves", ImageView.class);
            methodimage.invoke(controller,leaves);
            Method methodcurrent = controller.getClass().getMethod("setCurrent", int.class);
            methodcurrent.invoke(controller,current);
            CurrentAnchorPane=null;
            leaves=null;
            toolbarL=null;
            timelineScreen=null;
            conn=null;
            getConfigure=null;
            exitclass();
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace(); // Handle exceptions appropriately
        }

    }
    private void animateAnchorPane(AnchorPane anchorPane,String TabName){
        TranslateTransition transition = new TranslateTransition(Duration.millis(400), anchorPane);
        transition.setToY(anchorPane.getHeight());
        transition.setOnFinished(event -> {
                    try {
                        changeAnchor(TabName);
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
        CurrentAnchorPane = currentAnchorPane;
    }

    public void setLeaves(ImageView leaves) {
        this.leaves = leaves;
    }
}