package com.example.androidapp.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

public class MyTextView extends androidx.appcompat.widget.AppCompatTextView {
    public MyTextView(Context context) {
        super(context);
    }

    public MyTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    //重写设置字体方法
    @Override
    public void setTypeface(@Nullable Typeface tf) {
        tf = Typeface.createFromAsset(getContext().getAssets(),"fonts/造字工房乐真体.ttf");

        super.setTypeface(tf);
    }
}
