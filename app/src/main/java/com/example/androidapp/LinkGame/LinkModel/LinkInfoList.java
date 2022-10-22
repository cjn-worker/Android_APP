package com.example.androidapp.LinkGame.LinkModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 对含有关于两个点的连接信息的列表进行封装
 * 封装后数据类型为列表List:[I1,I2,...],I1:[Point:src,...,Point:des]
 */
public class LinkInfoList {
    private final List<List<Point>> Link = new ArrayList<>();

    public LinkInfoList(){}

    public void Seal(LinkInfo Info){
        Link.add(Info.getPointsList());
    }

    public List<List<Point>> getLink() {
        return Link;
    }

    public LinkInfo Unseal(int index)
    {
        if(Link!=null&&index<Link.size())
        {
            return new LinkInfo(Link.get(index));
        }
        return null;
    }

}
