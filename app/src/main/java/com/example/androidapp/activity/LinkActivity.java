package com.example.androidapp.activity;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.BounceInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.fragment.app.FragmentTransaction;

import com.example.androidapp.constant.Enum.PropMode;
import com.example.androidapp.constant.LinkConstant;
import com.example.androidapp.fragment.PauseFragment;
import com.example.androidapp.game.link.Kernel;
import com.example.androidapp.game.link.LinkInfoList;
import com.example.androidapp.model.LinkProp;
import com.example.androidapp.model.LinkUser;
import com.example.androidapp.utils.LinkUtils;
import com.example.androidapp.utils.ScreenUtil;
import com.example.androidapp.model.LinkLevel;
import com.example.androidapp.music.SoundPlayUtil;
import com.example.androidapp.R;
import com.example.androidapp.view.MyButton;
import com.example.androidapp.view.MyTextView;
import com.example.androidapp.game.manager.GameManager;
import com.example.androidapp.view.ImgView;
import com.example.androidapp.view.MyRelativeLayout;

import org.litepal.LitePal;

import java.util.List;

import swu.xl.numberitem.NumberOfItem;
import tyrantgit.explosionfield.ExplosionField;

public class LinkActivity extends BaseActivity implements View.OnClickListener
{
    //引入的第三方提供爆炸效果的类
    private ExplosionField explosionField;
    //游戏管理者
    private final GameManager manager = GameManager.getManager();
    //屏幕宽度,高度
    private int screenWidth;
    private int screenHeight;
    //游戏的布局，存放imgviews
    private MyRelativeLayout layout;
    //关卡
    private LinkLevel level;
    //用户
    private LinkUser user;
    //记录金币的变量
    int money;
    //时间条
    private SeekBar time_bar;
    //是否暂停的标志
    private final boolean isPause = false;
    //显示关卡的文本
    private MyTextView level_text;
    //显示金币的文本
    private MyTextView money_text;
    //显示分数的文本
    private MyTextView score_text;
    //暂停按钮
    private MyButton pause;
    //记录分数的变量
    private int score;

    //提示道具
    NumberOfItem prop_tip;
    //炸弹道具
    NumberOfItem prop_bomb;
    //刷新道具
    NumberOfItem prop_refresh;

    //记录提示道具的数量
    int tip_num;
    //记录炸弹道具的数量
    int bomb_num;
    //记录刷新道具的数量
    int refresh_num;

    //道具
    List<LinkProp> props;

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
                screenHeight - 150 - ScreenUtil.getNavigationBarHeight(getApplicationContext()),
                level.getL_id(),
                level.getL_mode()
        );
        manager.setListener(LinkActivity.this);
    }

    private void initData()
    {
        //获取数据
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        assert bundle != null;
        level = bundle.getParcelable("level");

        //查询用户数据
        List<LinkUser> users = LitePal.findAll(LinkUser.class);
        user = users.get(0);
        money = user.getU_money();

        level_text = findViewById(R.id.link_level_text);
        level_text.setText(String.valueOf(level.getL_id()));
        money_text = findViewById(R.id.link_money_text);
        money_text.setText(String.valueOf(money));
        score_text = findViewById(R.id.link_score_text);
        score_text.setText("0");
        score = 0;

        //查询道具数据
        props = LitePal.findAll(LinkProp.class);
        for (LinkProp prop : props)
        {
            if (prop.getP_kind() == PropMode.PROP_TIP.getValue())
            {
                //提示道具
                tip_num = prop.getP_number();
            }
            else if (prop.getP_kind() == PropMode.PROP_BOMB.getValue())
            {
                //炸弹道具
                bomb_num = prop.getP_number();
            }
            else
            {
                //刷新道具
                refresh_num = prop.getP_number();
            }
        }

        prop_tip = findViewById(R.id.prop_tip);
        prop_tip.setOnClickListener(this);
        prop_bomb = findViewById(R.id.prop_bomb);
        prop_bomb.setOnClickListener(this);
        prop_refresh = findViewById(R.id.prop_refresh);
        prop_refresh.setOnClickListener(this);
        prop_tip.setCount(tip_num);
        prop_bomb.setCount(bomb_num);
        prop_refresh.setCount(refresh_num);
        time_bar = findViewById(R.id.link_time_bar);
    }

    private void initView()
    {
        initLayout();
        initPauseButton();
    }

    //初始化布局
    @SuppressLint("ClickableViewAccessibility")
    private void initLayout()
    {
        layout = new MyRelativeLayout(this);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layout.setLayoutParams(layoutParams);

        screenWidth = ScreenUtil.getScreenWidth(getApplicationContext());
        screenHeight = ScreenUtil.getScreenHeight(getApplicationContext());
        //初始化爆炸类
        explosionField = ExplosionField.attach2Window(this);
        layout.setAlpha(1.0f);
        RelativeLayout link_layout = findViewById(R.id.root_link);
        layout.setOnTouchListener((v, event) -> {
            //获取触摸点相对于布局的坐标
            int x = (int) event.getX();
            int y = (int) event.getY();

            //触摸事件
            if (event.getAction() == MotionEvent.ACTION_DOWN)
            {
                for (final ImgView imgView : manager.getImgViews())
                {
                    //获取ImgView实例的rect
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
                            LinkInfoList linkInfoList = new LinkInfoList();
                            //如果两者的图片相同，且两者可以连接
                            if (imgView.getFlag() == lastView.getFlag() &&
                                    Kernel.findLink(
                                            manager.getBoard(),
                                            lastView.getPoint(),
                                            imgView.getPoint(),
                                            linkInfoList
                                    ))
                            {
                                startViewAnimation(imgView);
                                layout.setLinkInfo(linkInfoList.Unseal(0));
                                //设置所有的图标不可以点击
                                layout.setEnabled(false);
                                SoundPlayUtil.getInstance(getBaseContext()).play(4);
                                //修改板子
                                manager.getBoard()[lastView.getPoint().getX()][lastView.getPoint().getY()] = -1;
                                manager.getBoard()[imgView.getPoint().getX()][imgView.getPoint().getY()] = -1;

                                //粉碎
                                explosionField.explode(lastView);
                                explosionField.explode(imgView);
                                changeMoneyAndScore();
                                new Handler().postDelayed(() -> {
                                    //隐藏
                                    lastView.setVisibility(View.INVISIBLE);
                                    lastView.clearAnimation();
                                    imgView.setVisibility(View.INVISIBLE);
                                    imgView.clearAnimation();
                                    manager.setLastView(null);
                                    layout.setLinkInfo(null);
                                    layout.setEnabled(true);
                                }, 500);
                            }
                            else
                            {
                                if (lastView.getAnimation() != null)
                                {
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
                            manager.setLastView(null);
                        }
                    }
                }
            }
            return false;
        });
        link_layout.addView(layout);
    }

    private void changeMoneyAndScore()
    {
        money += 1;
        score += 2;
        money_text.setText(String.valueOf(money));
        score_text.setText(String.valueOf(score));
    }

    //初始化暂停按钮
    private void initPauseButton()
    {
        pause = findViewById(R.id.link_pause_btn);
        pause.setOnClickListener(view -> {
            manager.pauseGame();
            //跳转暂停的fragment
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            final PauseFragment pause = new PauseFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable("level", level);
            pause.setArguments(bundle);
            transaction.replace(R.id.root_link, pause, "pause");
            transaction.commit();
        });
    }

    //选中动画
    private void startViewAnimation(ImgView imgView)
    {
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


    //时间发生改变的时间
    public void onTimeChanged(float time)
    {
        //如果时间小于0
        if (time <= 0.0)
        {
            manager.pauseGame();
            manager.endGame(this, level, time);
        }
        else
        {
            //保留小数后一位
            time_bar.setProgress((int) time);
        }

        //如果board全部清除了
        if (LinkUtils.isCleared(manager.getBoard()))
        {
            //结束游戏
            manager.pauseGame();
            level.setL_time((int) (LinkConstant.TIME - time));
            level.setL_new(LinkUtils.getStarByTime((int) time));
            manager.endGame(this, level, time);

            //关卡结算
            level.update(level.getId());

            //下一关判断
            LinkLevel next_level = LitePal.find(LinkLevel.class, level.getId() + 1);
            if (next_level.getL_new() == '0')
            {
                next_level.setL_new('4');
                next_level.update(level.getId() + 1);
            }

            //金币道具清算
            user.setU_money(money);
            user.update(1);
        }
    }


    @SuppressLint("NonConstantResourceId")
    public void onClick(View v)
    {
        //播放点击音效
        SoundPlayUtil.getInstance(getBaseContext()).play(3);

        switch (v.getId())
        {
            case R.id.prop_tip:

                if (tip_num > 0)
                {
                    manager.tipGame(LinkActivity.this);

                    //数量减1
                    tip_num--;
                    prop_tip.setCount(tip_num);

                    //数据库处理
                    ContentValues values = new ContentValues();
                    values.put("p_number", tip_num);
                    LitePal.update(LinkProp.class, values, 1);
                }
                else
                {
                    Toast.makeText(this, "道具已经用完", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.prop_bomb:

                if (bomb_num > 0)
                {
                    manager.bombGame(LinkActivity.this);

                    //数量减1
                    bomb_num--;
                    prop_bomb.setCount(bomb_num);

                    //数据库处理
                    ContentValues values = new ContentValues();
                    values.put("p_number", bomb_num);
                    LitePal.update(LinkProp.class, values, 2);
                }
                else
                {
                    Toast.makeText(this, "道具已经用完", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.prop_refresh:

                if (refresh_num > 0)
                {
                    //刷新游戏
                    manager.refreshGame(
                            getApplicationContext(),
                            layout,
                            screenWidth,
                            screenHeight - 350 - ScreenUtil.getNavigationBarHeight(getApplicationContext()),
                            level.getL_id(),
                            level.getL_mode(),
                            LinkActivity.this
                    );

                    //数量减1
                    refresh_num--;
                    prop_refresh.setCount(refresh_num);

                    //数据库处理
                    ContentValues values = new ContentValues();
                    values.put("p_number", refresh_num);
                    LitePal.update(LinkProp.class, values, 3);
                }
                else
                {
                    Toast.makeText(this, "道具已经用完", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }


    @Override
    protected void onPause()
    {
        super.onPause();

        //暂停游戏
        manager.pauseGame();
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        //开启游戏
        if (manager.isPause())
        {
            manager.pauseGame();
        }
    }
}
