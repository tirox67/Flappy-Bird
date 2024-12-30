package com.example.flappybird;
import javafx.animation.AnimationTimer;
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
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;


public class Controller implements Initializable {

    static javafx.scene.Node[] Start_screen_container = new javafx.scene.Node[4];

    @FXML
    private Button Start_Button;
    @FXML
    private Button Close_Game_Button;
    @FXML
    private Label Title_Label;
    @FXML
    private ImageView Bird;
    @FXML
    private ImageView Ball;
    @FXML
    private ImageView Background_Flappy_Bird;
    @FXML
    private ImageView Background_Dunk_Mode;
    @FXML
    private Canvas canvas;
    private GraphicsContext gc;

    @FXML
    private Label Points_Label;

    private AnimationTimer gameloop;

    private boolean Game = false;

    @FXML
    private Button Change_Mode_Button;

    // false = Flappydunk, true = Flappybird
    private static boolean mode_flag = true;

    public static boolean getMode_flag()
    {return mode_flag;}

    public static void setMode_flag(boolean flag)
    {mode_flag = flag;}


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



    private void Start_Flappy_mode(){
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
                if (now - lastPipeSpawnTime >= 2*pipeSpawnInterval) {
                    Pipe.addPipe(new Pipe(800, 0, 50,top,true));   // Top pipe
                    Pipe.addPipe(new Pipe(800, top+270, 50, 600,false)); // Bottom pipe
                    counter++;
                    if(counter >= 6) {
                        Pipe.getPipes().removeFirst();
                        Pipe.getPipes().removeFirst();
                    }
                    System.out.println(Pipe.getPipes().size());
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
                            collision = birdBounds.intersects(pipe.getX(), pipe.getY(), pipe.getWidth() - 15, pipe.getHeight() - 15);
                        }else{ collision = birdBounds.intersects(pipe.getX(), pipe.getY()+15, pipe.getWidth() - 15, pipe.getHeight());}
                        if(birdBounds.getMaxX() >= pipe.getX()){Points+=1; Points_Label.setText(String.valueOf(Points));}
                        if (collision) {gameloop.stop(); Lost();}
                    }
                }



                for(Pipe p : Pipe.getPipes()){

                    p.setX(p.getX()-1);

                }


            }
        };


        // Start the game loop
        gameloop.start();

    }//end of Start_Flappy_mode

    private void Start_Dunk_mode(){
        gameloop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                //bird behavior
                if (Bird.getRotate() <= 90) {
                    Bird.setRotate(Bird.getRotate() + 3)
                    ;Bird.setY(Bird.getY() + 4);
                }
                if (Bird.getY() >= 600) {
                    Lost();
                }
                if (Bird.getY() <= -250) {
                    Bird.setY(-250);
                }


                gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                Bounds birdBounds = Bird.getBoundsInParent(); // Get the current bounds of the ImageView



            }
        };


        // Start the game loop
        gameloop.start();

    }//end of Start_Dunk_mode
    @FXML
    private void Main(){
        System.out.println(mode_flag);
        if(!mode_flag) {
            Ball.setVisible(true);
            Bird.setVisible(false);
            Background_Flappy_Bird.setVisible(false);
            Background_Dunk_Mode.setVisible(true);
        }else{
            Bird.setVisible(true);
            Ball.setVisible(false);
            Background_Flappy_Bird.setVisible(true);
            Background_Dunk_Mode.setVisible(false);
            Start_Flappy_mode();
        }
    }//end of Main

    //reset game and go back to start screen
    @FXML
    private void Lost(){
        Bird.setRotate(0);
        Bird.setY(0);
        Bird.setX(0);
        Game = false;
        gameloop.stop();
        for(javafx.scene.Node i : Start_screen_container){i.setVisible(true);}

        //remove the pipes from the screen
        for(int i =0; i< Pipe.getPipes().size();i++){
            Pipe.getPipes().removeFirst();
        }
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        Points_Label.setVisible(false);
    }//end of Lost

    private static Stage stage_Change_Mode;

    public static Stage getStage_Change_Mode()
    {return stage_Change_Mode;}


    //logic to open the popup for the mode selection. The Window got his own Controller/FXML
    @FXML
    private void Change_Mode() {

        try {
            // Load the FXML for the new window (Whiteboard.fxml)
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ChangeModePopup.fxml"));
            Parent root1 = fxmlLoader.load();

            // Create a new Stage (window)
            stage_Change_Mode = new Stage();

            // Set the scene and show the new window
            stage_Change_Mode.setScene(new Scene(root1));
            stage_Change_Mode.initStyle(StageStyle.TRANSPARENT);
            stage_Change_Mode.show();

        } catch (IOException e) {
            // If an error occurs while loading the FXML, print the error message
            e.printStackTrace();
            System.out.println("Error loading FXML for the new window.");
        }//end of try-catch
    }//end of Change-Mode


    //just the init method
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        gc = canvas.getGraphicsContext2D();
        Background_Dunk_Mode.setVisible(true);

        //add the objects in the Start_screen_container to change the visibility easier
        Start_screen_container[0] = Start_Button;
        Start_screen_container[1] = Title_Label;
        Start_screen_container[2] = Change_Mode_Button;
        Start_screen_container[3] = Close_Game_Button;

        //move the Bird on Mouseclick
       canvas.setOnMouseClicked(event -> {
           if(Game) {
               Bird.setY(Bird.getY() - 100);
               Bird.setRotate(-30);
           }
       });
    }
}