package com.example.androidapp.LinkGame.LinkModel;

/**
 * 坐标类，用来表示二维中的一个点
 */
public class Point {
    private final int x;
    private final int y;

    public Point(){this.x=this.y=-1;}

    public Point(int x, int y)
    {
        this.x = x;
        this.y = y;

    }

    public int getX(){return x;}

    public int getY(){return y;}

    public boolean isEqual(Point A)
    {
        return this.x == A.x && this.y == A.y;
    }

//
//    public void Copy(Point A)
//    {
//        this.x=A.x;
//        this.y=A.y;
//    }
    @Override
    public String toString()
    {

        return "("+this.x+","+this.y+")";
    }
}
