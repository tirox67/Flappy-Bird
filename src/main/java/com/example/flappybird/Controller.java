package com.example.flappybird;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;


public class Controller implements Initializable {

    javafx.scene.Node[] Start_screen_container = new javafx.scene.Node[2];

    @FXML
    private Button Start_Button;
    @FXML
    private Label Title_Label;
    @FXML
    private ImageView Bird;
    @FXML
    private ImageView Background;
    @FXML
    private Canvas canvas;
    private GraphicsContext gc;

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
    @FXML
    private void Main(){

        gameloop = new AnimationTimer() {

            @Override
            public void handle(long now) {
                //bird behavior
                if(Bird.getRotate() <= 90){Bird.setRotate(Bird.getRotate()+3);}
                if(Bird.getY() >= 600){Lost();}
                if(Bird.getY() <= -250){Bird.setY(-250);}
                Bird.setY(Bird.getY() + 4);




                Random rand = new Random();
                int top = rand.nextInt(300)+70;
                int bot = rand.nextInt(300)+500;

                //create new pipes
                if (now - lastPipeSpawnTime >= 2*pipeSpawnInterval) {
                    Pipe.addPipe(new Pipe(800, 0, 50,top ));   // Top pipe
                    Pipe.addPipe(new Pipe(800, bot, 50, 900-bot)); // Bottom pipe
                    counter++;
                    if(counter >= 4) {
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


                    for(Pipe pipe : Pipe.getPipes()) {
                        boolean collision = birdBounds.intersects(pipe.getX(),pipe.getY(), pipe.getWidth(), pipe.getHeight());
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


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        gc = canvas.getGraphicsContext2D();


        //create initial  pipes

        Pipe.addPipe(new Pipe(200*4,0,50,300));
        Pipe.addPipe(new Pipe(200 * 4, 600, 50, 300));



        //add the objects in the Start_screen_container to change the visibility easier
        Start_screen_container[0] = Start_Button;
        Start_screen_container[1] = Title_Label;

        //move the Bird on Mouseclick
       canvas.setOnMouseClicked(event -> {
           if(Game) {
               Bird.setY(Bird.getY() - 100);
               Bird.setRotate(-30);
           }
       });
    }
}