package com.example.androidapp.Model;

import org.litepal.crud.LitePalSupport;

public class XLScore extends LitePalSupport{

    //用户得分
    private int score;

    //用户通关数量
    private int do_num;

    //setter,getter方法
    public int getScore() { return score; }

    public void setScore(int score) { this.score = score; }

    public int getDo_num() { return do_num; }

    public void setDo_num(int do_num) { this.do_num = do_num; }

    @Override
    public String toString() {
        return "XLScore{" +
                "score=" + score +
                ", do_num=" + do_num +
                '}';
    }
}
