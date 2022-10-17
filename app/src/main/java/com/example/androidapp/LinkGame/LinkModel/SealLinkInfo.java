package com.example.androidapp.LinkGame.LinkModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 对含有关于两个点的连接信息的列表进行封装
 * 封装后数据类型为列表List:[I1,I2,...],I1:[Point:src,...,Point:des]
 */
public class SealLinkInfo {
    private List<List<Point>> Link = new ArrayList<>();

    public SealLinkInfo(){}

    public void Seal(LinkInfo info){
        Link.add(info.getPointsList());
    }

    public List<List<Point>> getLink() {
        return Link;
    }

    public void Unseal()
    {
        for (List<Point> points : Link) {
            for (Point point : points) {
                System.out.print(point.toString());
            }
            System.out.println("");
        }

    }
}
