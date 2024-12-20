package com.example.flappybird;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.Stack;

public class Pipe {
    private static Stack<Pipe> Pipes = new Stack<>();
    private double x;
    private double y;
    private double width;
    private double height;
    private boolean top;

    //constructor
    public Pipe(double x, double y, double width, double height,boolean top) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.top = top;


    }//end of constructor

    public boolean gettop()
    {return this.top;}

    public double getX()
    {return this.x;}

    public double getY()
    {return this.y;}

    public double getWidth()
    {return this.width;}

    public double getHeight()
    {return this.height;}


    public static Stack<Pipe> getPipes()
    {return Pipes;}

    public void setX(double x){
        this.x = x;
    }
    public void setY(double y){
        this.y = y;
    }
    public void setWidth(double width){this.width = width;}
    public void setHeight(double height){this.height = height;}


    public static void addPipe(Pipe x){
        Pipes.add(x);
    }



}//
