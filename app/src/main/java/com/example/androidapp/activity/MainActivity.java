package com.example.androidapp.activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.text.TextUtils;
import android.view.View;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Button;
import android.database.sqlite.SQLiteDatabase;

import com.example.androidapp.fragment.HelpFragment;
import com.example.androidapp.fragment.RankingFragment;
import com.example.androidapp.model.LinkLevel;
import com.example.androidapp.model.LinkProp;
import com.example.androidapp.model.LinkScore;
import com.example.androidapp.model.LinkUser;
import com.example.androidapp.constant.Constant;
import com.example.androidapp.fragment.SettingFragment;
import com.example.androidapp.R;
import com.example.androidapp.music.SoundPlayUtil;
import com.example.androidapp.music.BackgroundMusicManager;
import com.example.androidapp.fragment.StoreFragment;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity implements View.OnClickListener{
    Button easy_mode;
    Button normal_mode;
    Button hard_mode;
    Button setting;
    Button score;
    Button store;
    Button help;
    Button root_main;
    private BroadcastReceiver mBroadcastReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //资源预加载，防止没有声音
        SoundPlayUtil.getInstance(this);

        //数据库 LitePal
        LitePal.initialize(this);
        SQLiteDatabase db = LitePal.getDatabase();

        //向数据库装入数据
        initSQLite3();

        //初始化数据
        initView();

        //播放音乐
        playMusic();

        //广播接受者
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (!TextUtils.isEmpty(action)) {
                    switch (action) {
                        case Intent.ACTION_SCREEN_OFF:
                            Log.d(Constant.TAG, "屏幕关闭，变黑");

                            if (BackgroundMusicManager.getInstance(getBaseContext()).isBackgroundMusicPlaying()) {
                                Log.d(Constant.TAG, "正在播放音乐，关闭");

                                //暂停播放
                                BackgroundMusicManager.getInstance(getBaseContext()).pauseBackgroundMusic();
                            }

                            break;
                        case Intent.ACTION_SCREEN_ON:
                            Log.d(Constant.TAG, "屏幕开启，变亮");
                            break;
                        case Intent.ACTION_USER_PRESENT:
                            Log.d(Constant.TAG, "解锁成功");
                            break;
                        default:
                            break;
                    }
                }
            }
        };
        registerReceiver(mBroadcastReceiver, new IntentFilter(Intent.ACTION_SCREEN_OFF));
        registerReceiver(mBroadcastReceiver, new IntentFilter(Intent.ACTION_SCREEN_ON));
        registerReceiver(mBroadcastReceiver, new IntentFilter(Intent.ACTION_USER_PRESENT));

    }




    /**
     * 初始化数据库
     */
    private void initSQLite3() {
        //查找当前数据库的内容
        List<LinkUser> users = LitePal.findAll(LinkUser.class);
        List<LinkLevel> levels = LitePal.findAll(LinkLevel.class);
        List<LinkProp> props = LitePal.findAll(LinkProp.class);
        List<LinkScore> scores = LitePal.findAll(LinkScore.class);

        //如果用户数据为空，装入数据
        if (users.size() == 0){
            LinkUser user = new LinkUser();
            user.setU_money(1000);
            user.setU_background(0);
            user.save();
        }

        //如果关卡数据为空，装入数据
        if (levels.size() == 0){
            //简单模式
            for(int i = 1; i <= Constant.LEVEL_NUM; i++){
                LinkLevel level = new LinkLevel();
                //设置关卡号
                level.setL_id(i);
                //设置关卡模式
                level.setL_mode('1');
                //设置关卡的闯关状态
                if (i == 1){
                    level.setL_new('4');
                }else {
                    level.setL_new('0');
                }
                //设置关卡的闯关时间
                level.setL_time(0);

                //插入
                level.save();
            }

            //普通模式
            for(int i = 1; i <= Constant.LEVEL_NUM; i++){
                LinkLevel level = new LinkLevel();
                //设置关卡号
                level.setL_id(i);
                //设置关卡模式
                level.setL_mode('2');
                //设置关卡的闯关状态
                if (i == 1){
                    level.setL_new('4');
                }else {
                    level.setL_new('0');
                }
                //设置关卡的闯关时间
                level.setL_time(0);

                //插入
                level.save();
            }

            //困难模式
            for(int i = 1; i <= Constant.LEVEL_NUM; i++) {
                LinkLevel level = new LinkLevel();
                //设置关卡号
                level.setL_id(i);
                //设置关卡模式
                level.setL_mode('3');
                //设置关卡的闯关状态
                if (i == 1) {
                    level.setL_new('4');
                } else {
                    level.setL_new('0');
                }
                //设置关卡的闯关时间
                level.setL_time(0);

                //插入
                level.save();
            }
        }
        //如果道具数据为空，装入数据
        if (props.size() == 0){
            //1.装入提示道具
            LinkProp prop_tip = new LinkProp();
            prop_tip.setP_kind('1');
            prop_tip.setP_number(50);
            prop_tip.setP_price(10);
            prop_tip.save();

            //2.装入炸弹道具
            LinkProp prop_bomb = new LinkProp();
            prop_bomb.setP_kind('2');
            prop_bomb.setP_number(50);
            prop_bomb.setP_price(50);
            prop_bomb.save();

            //3.装入刷新道具
            LinkProp prop_refresh = new LinkProp();
            prop_refresh.setP_kind('3');
            prop_refresh.setP_number(50);
            prop_refresh.setP_price(20);
            prop_refresh.save();
        }

        //如果分数数据为空，装入数据
        if(scores.size() == 0){

            LinkScore score=new LinkScore();
            //1.装入得分
            score.setOne_score(0);
            score.setTwo_score(0);
            score.setThree_score(0);
            score.setFour_score(0);
            score.setFive_score(0);
            score.save();

            //2.装入通关数量
            score.setDo_num(0);
            score.save();

        }
    }

    /**
     * 数据的初始化
     */
    private void initView() {
        easy_mode = findViewById(R.id.mode_easy_btn);
        easy_mode.setOnClickListener(this);
        normal_mode = findViewById(R.id.mode_normal_btn);
        normal_mode.setOnClickListener(this);
        hard_mode = findViewById(R.id.mode_hard_btn);
        hard_mode.setOnClickListener(this);
        setting = findViewById(R.id.main_setting_btn);
        setting.setOnClickListener(this);
        score = findViewById(R.id.main_rank_btn);
        score.setOnClickListener(this);
        store = findViewById(R.id.main_store_btn);
        store.setOnClickListener(this);
        help=findViewById(R.id.main_help_button);
        help.setOnClickListener(this);
        //root_main = findViewById(R.id.root_main);
    }




    /**
     * 播放背景音乐
     */
    private void playMusic() {
        //判断是否正在播放
        if (!BackgroundMusicManager.getInstance(this).isBackgroundMusicPlaying()) {

            //播放
            BackgroundMusicManager.getInstance(this).playBackgroundMusic(
                    R.raw.game_bg_music,
                    true
            );
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(@NonNull View v) {
        //播放点击音效
        SoundPlayUtil.getInstance(getBaseContext()).play(3);

        //fragment事务
        final FragmentManager manager = getSupportFragmentManager();
        final FragmentTransaction transaction = manager.beginTransaction();

        //区分点击
        switch (v.getId()){
            case R.id.mode_easy_btn:
                Log.d(Constant.TAG,"简单模式按钮");

                //查询简单模式的数据
                List<LinkLevel> LinkLevels1 = LitePal.where("l_mode == ?", "1").find(LinkLevel.class);
                Log.d(Constant.TAG, LinkLevels1.size()+"");

                //依次查询每一个内容
                for (LinkLevel linkLevel : LinkLevels1) {
                    Log.d(Constant.TAG, linkLevel.toString());
                }

                //跳转界面
                Intent intent_easy = new Intent(this, LevelActivity.class);
                //加入数据
                Bundle bundle_easy = new Bundle();
                //加入关卡模式数据
                bundle_easy.putString("mode","简单");
                //加入关卡数据
                bundle_easy.putParcelableArrayList("levels", (ArrayList<? extends Parcelable>) LinkLevels1);
                intent_easy.putExtras(bundle_easy);
                //跳转
                startActivity(intent_easy);

                break;
            case R.id.mode_normal_btn:
                Log.d(Constant.TAG,"普通模式按钮");

                //查询简单模式的数据
                List<LinkLevel> LinkLevels2 = LitePal.where("l_mode == ?", "2").find(LinkLevel.class);
                Log.d(Constant.TAG, LinkLevels2.size()+"");

                //依次查询每一个内容
                for (LinkLevel linkLevel : LinkLevels2) {
                    Log.d(Constant.TAG, linkLevel.toString());
                }

                //跳转界面
                Intent intent_normal = new Intent(this, LevelActivity.class);
                //加入数据
                Bundle bundle_normal = new Bundle();
                //加入关卡模式数据
                bundle_normal.putString("mode","简单");
                //加入关卡数据
                bundle_normal.putParcelableArrayList("levels", (ArrayList<? extends Parcelable>) LinkLevels2);
                intent_normal.putExtras(bundle_normal);
                //跳转
                startActivity(intent_normal);

                break;
            case R.id.mode_hard_btn:
                Log.d(Constant.TAG,"困难模式按钮");

                //查询简单模式的数据
                List<LinkLevel> LinkLevels3 = LitePal.where("l_mode == ?", "3").find(LinkLevel.class);
                Log.d(Constant.TAG, LinkLevels3.size()+"");

                //依次查询每一个内容
                for (LinkLevel linkLevel : LinkLevels3) {
                    Log.d(Constant.TAG, linkLevel.toString());
                }

                //跳转界面
                Intent intent_hard = new Intent(this, LevelActivity.class);
                //加入数据
                Bundle bundle_hard = new Bundle();
                //加入关卡模式数据
                bundle_hard.putString("mode","简单");
                //加入关卡数据
                bundle_hard.putParcelableArrayList("levels", (ArrayList<? extends Parcelable>) LinkLevels3);
                intent_hard.putExtras(bundle_hard);
                //跳转
                startActivity(intent_hard);

                break;
            case R.id.main_setting_btn:
                Log.d(Constant.TAG,"设置");

                //添加一个fragment
                final SettingFragment setting = new SettingFragment();
                transaction.replace(R.id.root_main,setting,"setting");
                transaction.commit();

                break;
            case R.id.main_store_btn:
                Log.d(Constant.TAG,"商店按钮");

                //添加一个fragment
                final StoreFragment store = new StoreFragment();
                transaction.replace(R.id.root_main,store,"store");
                transaction.commit();

                break;
            case R.id.main_rank_btn:
                Log.d(Constant.TAG,",排行榜按钮");

                //添加一个fragment
                final RankingFragment rank = new RankingFragment();
                transaction.replace(R.id.root_main,rank,"rank");
                transaction.commit();

                break;
            case R.id.main_help_button:
                Log.d(Constant.TAG,",帮助按钮");

                //添加一个fragment
                final HelpFragment help = new HelpFragment();
                transaction.replace(R.id.root_main,help,"help");
                transaction.commit();

                break;
        }
    }

        @Override
        protected void onDestroy() {
            super.onDestroy();

            unregisterReceiver(mBroadcastReceiver);
        }
    }




