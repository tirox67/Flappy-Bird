package com.example.flappybird;

import java.util.Stack;

public class Basket {

    private int cord_x;
    private int cord_y;

    private double width_edge;
    private double height_edge;
    private double width_basket;
    private int points;

    private static Stack<Basket> baskets = new Stack<Basket>();

    public Basket(int x ,int y, int points, double height_edge, double width_basket) {

        this.width_edge = points * 4.37 ;
        this.height_edge = height_edge;
        this.width_basket = width_basket;

        this.cord_x = x;
        this.cord_y = y;

    }//end of constructor

    public double getWidth_edge() {

        return this.width_edge;

    }

    public double getHeight_edge() {

        return this.height_edge;

    }

    public double getWidth_basket() {

        return this.width_basket;

    }

    public int getPoints() {

        return this.points;

    }

    public static Stack<Basket> getBaskets() {

        return baskets;

    }

    public void clearBaskets() {

        baskets.clear();

    }

    public int getCord_x() {

        return this.cord_x;

    }

    public int getCord_y() {

        return this.cord_y;

    }

    public void setCord_x(int x) {

        this.cord_x = x;

    }



}//end of class
