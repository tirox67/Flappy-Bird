package com.example.flappybird;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;


public class Controller implements Initializable {

    static javafx.scene.Node[] Start_screen_container = new javafx.scene.Node[3];

    public static Controller instance;

    @FXML
    private Button Start_Button;
    @FXML
    private Button Close_Game_Button;
    @FXML
    private Label Title_Label;
    @FXML
    public ImageView Bird;

    @FXML
    private ImageView Background_Flappy_Bird;

    @FXML
    private Canvas canvas;
    private GraphicsContext gc;

    private double Bird_Speed = 1.0;

    @FXML
    private Label Points_Label;

    private AnimationTimer gameloop;

    private boolean Game = false;








    //start game
    @FXML
    private void Start_game(){
        counter =0;
        Points =0;
        gc.clearRect(0,0,canvas.getWidth(),canvas.getHeight());
        Game = true;
        Points_Label.setVisible(true);
        for(javafx.scene.Node i : Start_screen_container){i.setVisible(false);}
        Main();
    }//end of Start_game


    long lastPipeSpawnTime = 0; // Tracks time of last pipe spawn
    long pipeSpawnInterval = 2_000_000_000; // Interval to spawn pipes (2 seconds)
    int counter =0;
    int Points =0;

    MediaPlayer FlappyMediaPlayer;
    private void Play_Music(){

       String Flappysong = getClass().getResource("/SOUNDS/FlappyBanger.mp3").toExternalForm();
       Media FlappyMedia = new Media(Flappysong);
       FlappyMediaPlayer = new MediaPlayer(FlappyMedia);
       FlappyMediaPlayer.play();
       FlappyMediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
    }//end of Play_Music


    private void Play_Flap_Sound(){
        MediaPlayer FlapMediaPlayer;
        String Flap = getClass().getResource("/SOUNDS/Flap.mp3").toExternalForm();
        Media FlapMedia = new Media(Flap);
        FlapMediaPlayer = new MediaPlayer(FlapMedia);
        FlapMediaPlayer.play();
    }//end of Play_Flap_Sound


    private void Play_Incresepoints_Sound(){
        MediaPlayer PointsMediaPlayer;
        String Points = getClass().getResource("/SOUNDS/IncreasePoints.mp3").toExternalForm();
        Media PointsMedia = new Media(Points);
        PointsMediaPlayer = new MediaPlayer(PointsMedia);
        PointsMediaPlayer.play();
    }//end of Play_Incresepoint_Sound

    @FXML
    private void Play_Mouse_Hover_Sound(){
        MediaPlayer Mouse_HoverMediaPlayer;
        String Mouse_Hover = getClass().getResource("/SOUNDS/Mouse_Hover.mp3").toExternalForm();
        Media Mouse_HoverMedia = new Media(Mouse_Hover);
        Mouse_HoverMediaPlayer = new MediaPlayer(Mouse_HoverMedia);
        Mouse_HoverMediaPlayer.play();
    }//end of Play_Mouse_Hover_Sound

    private void Play_Death_Sound(){
        MediaPlayer DeathMediaPlayer;
        String Death = getClass().getResource("/SOUNDS/Death.mp3").toExternalForm();
        Media DeathMedia = new Media(Death);
        DeathMediaPlayer = new MediaPlayer(DeathMedia);
        DeathMediaPlayer.play();
    }//end of Play_Death_Sound



    private void Start_Flappy_mode(){
        Play_Music();
        gameloop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                //bird behavior

                Bird.setY(Bird.getY() + 4);

                if (Bird.getY() >= 600) {
                    Lost();
                }
                if (Bird.getY() <= -250) {
                    Bird.setY(-250);
                }

                //randomize the length of the top pipes.
                Random rand = new Random();
                int top = rand.nextInt(300)+70;



                //create new pipes | The top pipes are randomized in length, while the bottom pipes are adjusted to the length of the top-pipes.
                if (now - lastPipeSpawnTime >= 1.7*pipeSpawnInterval) {
                    Pipe.addPipe(new Pipe(800, 0, 50,top,true));   // Top pipe
                    Pipe.addPipe(new Pipe(800, top+230, 50, 600,false)); // Bottom pipe
                    counter++;
                    if(counter >= 6) {
                        Pipe.getPipes().removeFirst();
                        Pipe.getPipes().removeFirst();
                    }
                    lastPipeSpawnTime = now; // Reset timer

                }


                gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                Bounds birdBounds = Bird.getBoundsInParent(); // Get the current bounds of the ImageView

                for (Pipe p : Pipe.getPipes()) {
                    // Draw the pipes
                    gc.setFill(Color.GREEN);
                    gc.fillRect(p.getX(), p.getY(), p.getWidth(), p.getHeight());


                    //comment this line in to see the hitbox of the bird
                    //gc.strokeRect(birdBounds.getMinX(), birdBounds.getMinY(), birdBounds.getWidth(), birdBounds.getHeight());

                    //detect collisions and stop the game if necesarry. SOME WORK HERE TO DO
                    for(Pipe pipe : Pipe.getPipes()) {
                        boolean collision;
                        if(pipe.isTop()) {
                            collision = birdBounds.intersects(pipe.getX()+20, pipe.getY(), pipe.getWidth() - 20, pipe.getHeight() - 20);
                        }else{
                            collision = birdBounds.intersects(pipe.getX()+20, pipe.getY()+20, pipe.getWidth() - 20, pipe.getHeight());
                        }
                        if(birdBounds.getMaxX() >= pipe.getX() && birdBounds.getMaxX()< pipe.getX()+4){
                            if(!pipe.getWasChecked() && pipe.isTop()){

                                Points+=1;
                                Play_Incresepoints_Sound();
                                Points_Label.setText("Points: " + String.valueOf(Points));
                                pipe.setWasChecked(true);


                            }
                        }
                        if (collision) {
                            gameloop.stop();
                            Lost();
                        }

                    }
                }



                //move the pipes with a speed, according to the reached score
                for(Pipe p : Pipe.getPipes()){

                    p.setX(p.getX()-Bird_Speed);

                }


            }
        };


        // Start the game loop
        gameloop.start();

    }//end of Start_Flappy_mode



    @FXML
    private void Main(){


        Bird.setVisible(true);
        Background_Flappy_Bird.setVisible(true);

        Start_Flappy_mode();

    }//end of Main

    //reset game and go back to start screen
    @FXML
    private void Lost(){

        //delete the obstacles and reset the player

        Bird.setRotate(0);
        Bird.setY(0);
        Bird.setX(0);
        Pipe.clearPipes();
        FlappyMediaPlayer.stop();

        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        //stop the game and show the titlescreen and play deathsound
        Game = false;
        gameloop.stop();
        Play_Death_Sound();
        for(javafx.scene.Node i : Start_screen_container){i.setVisible(true);}

        //reset the points and the Points_Label
        Points = 0;
        Points_Label.setText("Points: 0");
        Points_Label.setVisible(false);

    }//end of Lost

    private static Stage stage_Change_Mode;

    public static Stage getStage_Change_Mode()
    {return stage_Change_Mode;}



    //logic to leave the game
    @FXML
    private void Exit(){
        System.exit(0);
    }//end of Exit

    //just the init method
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        instance = this;

        gc = canvas.getGraphicsContext2D();


        //add the objects in the Start_screen_container to change the visibility easier
        Start_screen_container[0] = Start_Button;
        Start_screen_container[1] = Title_Label;
        Start_screen_container[2] = Close_Game_Button;


        //move the Bird on Mouseclick
        canvas.setOnMouseClicked(event -> {


            if(Game) {

                // Create Timeline for the Bird's upward movement
                Timeline birdTimeline = new Timeline(
                        new KeyFrame(
                                Duration.millis(200), // Duration of the movement
                                new KeyValue(Bird.yProperty(), Bird.getY() - 70), // Move up by 100
                                new KeyValue(Bird.rotateProperty(), -30) // Rotate to -30 degrees
                        )
                );
                birdTimeline.play(); // Start the animation
                Play_Flap_Sound(); // Play the flap sound


           }
       });
    }//end of init

    public static ImageView getBird() {
        return instance.Bird; // Access  via the instance
    }//end of getBird
    public static ImageView getBackground_Flappy_Mode() {
        return instance.Background_Flappy_Bird; // Access via the instance
    }//end of getBackground_Flappy_Mode
    public static Label getTitle_Label() {
        return instance.Title_Label;
    }//end of getTitle_Label


}//end of class


