package com.example.androidapp.Activity;


import android.annotation.SuppressLint;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.androidapp.LinkGame.LinkModel.Kernel;
import com.example.androidapp.LinkGame.LinkModel.SealLinkInfo;
import com.example.androidapp.LinkGame.Utils.ScreenUtil;
import com.example.androidapp.R;
import com.example.androidapp.manager.GameManager;
import com.example.androidapp.view.ImgView;

import tyrantgit.explosionfield.ExplosionField;

public class LinkActivity extends AppCompatActivity
{
    private ExplosionField explosionField;
    GameManager manager = new GameManager();
    @SuppressLint("ClickableViewAccessibility")
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);
        //屏幕宽度,高度
        int screenWidth;
        int screenHeight;
        RelativeLayout layout = new RelativeLayout(this);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layout.setLayoutParams(layoutParams);
        screenWidth = ScreenUtil.getScreenWidth(getApplicationContext());
        screenHeight = ScreenUtil.getScreenHeight(getApplicationContext());
        explosionField = ExplosionField.attach2Window(this);
        manager.startGame(this,
                layout,
                screenWidth,
                screenHeight -500- ScreenUtil.getNavigationBarHeight(getApplicationContext())
        );
        layout.setAlpha(1.0f);
        ConstraintLayout link_layout=findViewById(R.id.link_layout);
        layout.setOnTouchListener(new View.OnTouchListener()
        {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, final MotionEvent event)
            {

                //获取触摸点相对于布局的坐标
                int x = (int) event.getX();
                int y = (int) event.getY();

                //触摸事件
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    for (final ImgView imgView : manager.getImgViews())
                    {
                        //获取AnimalView实例的rect
                        RectF rectF = new RectF(
                                imgView.getLeft(),
                                imgView.getTop(),
                                imgView.getRight(),
                                imgView.getBottom());

                        //判断是否包含
                        if (rectF.contains(x, y)&& imgView.getVisibility() == View.VISIBLE)
                        {
                            final ImgView lastView = manager.getLastView();

                            //如果不是第一次触摸 且 触摸的不是同一个点
                            if (lastView != null && lastView != imgView)
                            {
                                //如果两者的图片相同，且两者可以连接
                                if (imgView.getFlag() == lastView.getFlag() &&
                                        Kernel.findLink(
                                                manager.getBoard(),
                                                lastView.getPoint(),
                                                imgView.getPoint(),
                                                new SealLinkInfo()
                                        ))
                                {
                                    new Handler().postDelayed(new Runnable()
                                    {
                                        @Override
                                        public void run()
                                        {

                                            //修改模板
                                            manager.getBoard()[lastView.getPoint().getX()][lastView.getPoint().getY()] = 0;
                                            manager.getBoard()[imgView.getPoint().getX()][imgView.getPoint().getY()] = 0;

                                            //粉碎
                                            explosionField.explode(lastView);
                                            explosionField.explode(imgView);

                                            //隐藏
                                            lastView.setVisibility(View.INVISIBLE);
                                            lastView.clearAnimation();
                                            imgView.setVisibility(View.INVISIBLE);
                                            imgView.clearAnimation();


                                        }
                                    }, 100);
                                    manager.setLastView(null);
                                }
                                else
                                {
                                    manager.setLastView(null);
                                }
                            }
                            else if (lastView == null)
                            {

                                manager.setLastView(imgView);
                                break;
                            }
                            else
                            {
                                //取消框框
                                manager.setLastView(null);
                            }
                        }
                    }
                }
                return false;
            }
        });
        link_layout.addView(layout);
    }
}
