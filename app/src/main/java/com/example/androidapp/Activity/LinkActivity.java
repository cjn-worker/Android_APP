package com.example.androidapp.Activity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.BounceInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidapp.LinkGame.LinkModel.Kernel;
import com.example.androidapp.LinkGame.LinkModel.LinkInfo;
import com.example.androidapp.LinkGame.LinkModel.SealLinkInfo;
import com.example.androidapp.LinkGame.Utils.ScreenUtil;
import com.example.androidapp.Model.XLLevel;
import com.example.androidapp.Music.SoundPlayUtil;
import com.example.androidapp.R;
import com.example.androidapp.manager.GameManager;
import com.example.androidapp.view.ImgView;
import com.example.androidapp.view.XLRelativeLayout;

import org.litepal.LitePal;

import java.util.List;

import tyrantgit.explosionfield.ExplosionField;

public class LinkActivity extends AppCompatActivity
{
    private ExplosionField explosionField;
    private GameManager manager = GameManager.getManager();
    //屏幕宽度,高度
    private int screenWidth;
    private int screenHeight;
    //游戏的布局，存放imgviews
    private XLRelativeLayout layout;
    private XLLevel level;

    @SuppressLint("ClickableViewAccessibility")
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link);
        initData();
        initView();


        manager.startGame(this,
                layout,
                screenWidth,
                screenHeight -500- ScreenUtil.getNavigationBarHeight(getApplicationContext()),
                level.getL_id(),
                level.getL_mode()
        );
    }

    private void initData() {
        //获取数据
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        assert bundle != null;
        level = bundle.getParcelable("level");

//        Log.d(Constant.TAG,"--------");
//        Log.d(Constant.TAG, String.valueOf(level));
//
//        //查询用户数据
//        List<XLUser> users = LitePal.findAll(XLUser.class);
//        user = users.get(0);
//        money = user.getU_money();
//
//        //查询道具数据
//        props = LitePal.findAll(XLProp.class);
//        for (XLProp prop : props) {
//            if (prop.getP_kind() == PropMode.PROP_FIGHT.getValue()){
//                //拳头道具
//                fight_num = prop.getP_number();
//                Log.d(Constant.TAG,"查询的消除道具数量："+fight_num);
//            }else if (prop.getP_kind() == PropMode.PROP_BOMB.getValue()){
//                //炸弹道具
//                bomb_num = prop.getP_number();
//                Log.d(Constant.TAG,"查询的炸弹道具数量："+bomb_num);
//            }else {
//                //刷新道具
//                refresh_num = prop.getP_number();
//                Log.d(Constant.TAG,"查询的刷新道具数量："+refresh_num);
//            }
//        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initView()
    {
        layout = new XLRelativeLayout(this);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layout.setLayoutParams(layoutParams);
        screenWidth = ScreenUtil.getScreenWidth(getApplicationContext());
        screenHeight = ScreenUtil.getScreenHeight(getApplicationContext());
        explosionField = ExplosionField.attach2Window(this);
        layout.setAlpha(1.0f);
        RelativeLayout link_layout = findViewById(R.id.root_link);
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
                        if (rectF.contains(x, y) && imgView.getVisibility() == View.VISIBLE)
                        {
                            final ImgView lastView = manager.getLastView();

                            //如果不是第一次触摸 且 触摸的不是同一个点
                            if (lastView != null && lastView != imgView)
                            {
                                SealLinkInfo sealLinkInfo = new SealLinkInfo();
                                //如果两者的图片相同，且两者可以连接
                                if (imgView.getFlag() == lastView.getFlag() &&
                                        Kernel.findLink(
                                                manager.getBoard(),
                                                lastView.getPoint(),
                                                imgView.getPoint(),
                                                sealLinkInfo
                                        ))
                                {
                                    startViewAnimation(imgView);
                                    layout.setLinkInfo(new LinkInfo(sealLinkInfo.getLink().get(0)));
                                    //设置所有的宝可梦不可以点击
                                    layout.setEnabled(false);
                                    SoundPlayUtil.getInstance(getBaseContext()).play(4);
                                    //修改模板
                                    manager.getBoard()[lastView.getPoint().getX()][lastView.getPoint().getY()] = -1;
                                    manager.getBoard()[imgView.getPoint().getX()][imgView.getPoint().getY()] = -1;

                                    //粉碎
                                    explosionField.explode(lastView);
                                    explosionField.explode(imgView);
                                    new Handler().postDelayed(new Runnable()
                                    {
                                        @Override
                                        public void run()
                                        {
                                            //隐藏
                                            lastView.setVisibility(View.INVISIBLE);
                                            lastView.clearAnimation();
                                            imgView.setVisibility(View.INVISIBLE);
                                            imgView.clearAnimation();
                                            manager.setLastView(null);
                                            layout.setLinkInfo(null);
                                            layout.setEnabled(true);
                                        }
                                    }, 500);
                                }
                                else
                                {
                                    if (lastView.getAnimation() != null){
                                        //清楚所有动画
                                        lastView.clearAnimation();
                                    }
                                    SoundPlayUtil.getInstance(getBaseContext()).play(3);
                                    manager.setLastView(null);
                                }
                            }
                            else if (lastView == null)
                            {
                                startViewAnimation(imgView);
                                SoundPlayUtil.getInstance(getBaseContext()).play(3);
                                manager.setLastView(imgView);
                                break;
                            }
                            else
                            {
                                lastView.clearAnimation();
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

    private void startViewAnimation(ImgView imgView){
        //缩放动画
        ScaleAnimation scaleAnimation = new ScaleAnimation(
                1.0f, 1.05f,
                1.0f, 1.05f,
                Animation.RELATIVE_TO_SELF,
                0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f
        );
        scaleAnimation.setDuration(100);
        scaleAnimation.setRepeatCount(0);
        scaleAnimation.setFillAfter(true);

        //旋转动画
        RotateAnimation rotateAnimation = new RotateAnimation(
                -20f,
                20f,
                Animation.RELATIVE_TO_SELF,
                0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f
        );
        rotateAnimation.setDuration(500);
        rotateAnimation.setStartOffset(100);
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        rotateAnimation.setRepeatMode(Animation.REVERSE);
        rotateAnimation.setInterpolator(new BounceInterpolator());

        //组合动画
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(rotateAnimation);

        //开启动画
        imgView.startAnimation(animationSet);
        animationSet.startNow();
    }
}
