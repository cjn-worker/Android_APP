package com.example.androidapp.game.link;

import java.util.ArrayList;
import java.util.List;

/**
 * 存储转折点的信息
 */
public class LinkInfo {
    private List<Point> points = new ArrayList<>();

    public LinkInfo(List<Point> p)
    {
        points = p;
    }

    public LinkInfo(Point src, Point des)
    {
        points.add(src);
        points.add(des);
    }

    public LinkInfo(Point src, Point tmp, Point des)
    {
        points.add(src);
        points.add(tmp);
        points.add(des);
    }

    public LinkInfo(Point src, Point tmp1, Point tmp2, Point des)
    {
        points.add(src);
        points.add(tmp1);
        points.add(tmp2);
        points.add(des);
    }

    public List<Point> getPointsList(){return points;}


}
