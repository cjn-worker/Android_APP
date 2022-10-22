package com.example.androidapp.view;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.androidapp.LinkGame.LinkModel.Point;

public class ImgView extends androidx.appcompat.widget.AppCompatImageView
{
    //图片的标志代号，用来判断两个图片是否相等
    private final int flag;

    //图片的坐标
    private final Point point;

    /**
     * 构造方法
     * @param context
     */
    public ImgView(Context context, int flag, Point point) {
        super(context);
        this.flag = flag;
        this.point = point;
    }
    public ImgView(Context context, int resource_src, int flag, Point point) {
        super(context);

        //设置背景颜色
        //this.setBackgroundResource(LinkConstant.ANIMAL_BG);
        //设置显示的图片
        this.setImageResource(resource_src);

        //保存标志
        this.flag = flag;

        //保存坐标
        this.point = point;
    }

    //选中时的背景
    public void changeAnimalBackground(int resource){
        this.setBackgroundResource(resource);
    }

    //setter,getter
    public int getFlag() {
        return flag;
    }

    public Point getPoint() {
        return point;
    }

    @NonNull
    @Override
    public String toString() {
        return "AnimalView{" +
                "flag=" + flag +
                ", point=" + point +
                '}';
    }
}
