package com.example.androidapp.view;

import android.content.Context;

import com.example.androidapp.constant.Enum.LevelState;
import com.example.androidapp.R;

public class XLImageView extends androidx.appcompat.widget.AppCompatImageView {
    //四套显示样式
    public static final int[] resources = {
            R.drawable.levle_undo_pic,      //关卡没有被打开
            R.drawable.level_one_crown_pic,   //关卡闯关成功 一星
            R.drawable.level_two_crowns_pic,   //关卡闯关成功 二星
            R.drawable.level_three_crowns_pic, //关卡闯关成功 三星
            R.drawable.level_doing_doing      //关卡正在被闯
    };

    /**
     * 构造方法
     * @param context
     */
    public XLImageView(Context context, LevelState levelState) {
        super(context);

        this.changeLevelState(levelState);
    }

    /**
     * 设置背景颜色
     */
    public void changeLevelState(LevelState levelState){
        //切换到对应状态的图片
        this.setBackgroundResource(resources[Integer.parseInt(String.valueOf(levelState.getValue()))]);
    }
}
