package com.example.flappybird;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Change_Mode_Window_Controller {

    @FXML
    private Button Button_close;

    @FXML
    private ImageView Flappy_icon;

    @FXML
    private ImageView Dunk_icon;

    @FXML
    private void on_Dunk_icon_entered(){
        animate_icon(Dunk_icon);
    }//end of on_Dunk_icon_entered

    @FXML
    private void on_Flappy_icon_entered(){
        animate_icon(Flappy_icon);
    }//end of on_Flappy_icon_entered


    // Logic to animate the icon (grow and shrink)
    private void animate_icon(ImageView iv) {
        // Define the timeline for scaling animation
        Timeline timeline_animate_icon = new Timeline(
                new KeyFrame(Duration.seconds(0), // Initial state
                        new javafx.animation.KeyValue(iv.scaleXProperty(), 1.0),
                        new javafx.animation.KeyValue(iv.scaleYProperty(), 1.0)
                ),
                new KeyFrame(Duration.seconds(0.5), // Grow phase
                        new javafx.animation.KeyValue(iv.scaleXProperty(), 1.2),
                        new javafx.animation.KeyValue(iv.scaleYProperty(), 1.2)
                ),
                new KeyFrame(Duration.seconds(1.0), // Shrink back
                        new javafx.animation.KeyValue(iv.scaleXProperty(), 1.0),
                        new javafx.animation.KeyValue(iv.scaleYProperty(), 1.0)
                )
        );

        timeline_animate_icon.setCycleCount(1); // One complete cycle
        timeline_animate_icon.setRate(1);      // Animation rate
        timeline_animate_icon.play();          // Start the animation
    }


    @FXML
    private void on_Dunk_icon_clicked(){
        Controller.setMode_flag(false);
        System.out.println(Controller.getMode_flag());

    }//end of on_Dunk_icon_clicked

    @FXML
    private void on_Flappy_icon_clicked(){
        Controller.setMode_flag(true);
        System.out.println(Controller.getMode_flag());
    }//end of on_Flappy_icon_clicked


    @FXML
    private void close(){
        Controller.getStage_Change_Mode().close();
    }//end of Exit

}