package com.example.flappybird;


import java.util.Stack;

public class Pipe {
    private static Stack<Pipe> Pipes = new Stack<>();
    private double x;
    private double y;
    private double width;
    private double height;
   ;

    //constructor
    public Pipe(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;



    }//end of constructor



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

    public static void addPipe(Pipe x){
        Pipes.add(x);
    }



}//
