package com.example.androidapp.Model;

import org.litepal.crud.LitePalSupport;

public class XLScore extends LitePalSupport{

    //用户得分
    private int one_score;

    private int two_score;

    private int three_score;

    private int four_score;

    private int five_score;

    //用户通关数量
    private int do_num;

    //setter,getter方法
    public int getOne_score() {
        return one_score;
    }

    public void setOne_score(int one_score) {
        this.one_score = one_score;
    }

    public int getTwo_score() {
        return two_score;
    }

    public void setTwo_score(int two_score) {
        this.two_score = two_score;
    }

    public int getThree_score() {
        return three_score;
    }

    public void setThree_score(int three_score) {
        this.three_score = three_score;
    }

    public int getFour_score() {
        return four_score;
    }

    public void setFour_score(int four_score) {
        this.four_score = four_score;
    }

    public int getFive_score() {
        return five_score;
    }

    public void setFive_score(int five_score) {
        this.five_score = five_score;
    }

    public int getDo_num() { return do_num; }

    public void setDo_num(int do_num) { this.do_num = do_num; }

    @Override
    public String toString() {
        return "XLScore{" +
                "one_score=" + one_score +
                ", two_score=" + two_score +
                ", three_score=" + three_score +
                ", four_score=" + four_score +
                ", five_score=" + five_score +
                ", do_num=" + do_num +
                '}';
    }
}
