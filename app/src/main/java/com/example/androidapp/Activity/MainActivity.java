package com.example.androidapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.database.sqlite.SQLiteDatabase;

import com.example.androidapp.Constant.Constant;
import com.example.androidapp.Fragment.SettingFragment;
import com.example.androidapp.R;
import com.example.androidapp.Music.SoundPlayUtil;
import com.example.androidapp.Music.BackgroundMusicManager;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    Button easy_mode;
    Button normal_mode;
    Button hard_mode;
    Button setting;
    Button help;
    Button root_main;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //资源预加载，防止没有声音
        SoundPlayUtil.getInstance(this);

        //初始化数据
        initView();
        //播放音乐
        playMusic();





    }

    /**
     * 数据的初始化
     */
    private void initView() {
        easy_mode = findViewById(R.id.start_easy);
        easy_mode.setOnClickListener(this);
        normal_mode = findViewById(R.id.start_normal);
        normal_mode.setOnClickListener(this);
        hard_mode = findViewById(R.id.start_hard);
        hard_mode.setOnClickListener(this);
        setting = findViewById(R.id.main_setting);
        setting.setOnClickListener(this);
        help = findViewById(R.id.score);
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
                    R.raw.bg_music,
                    true
            );
        }
    }

    @Override
    public void onClick(@NonNull View v) {
        //播放点击音效
        SoundPlayUtil.getInstance(getBaseContext()).play(3);

        //fragment事务
        final FragmentManager manager = getSupportFragmentManager();
        final FragmentTransaction transaction = manager.beginTransaction();

        //区分点击
        switch (v.getId()){
            case R.id.start_easy:
                Log.d(Constant.TAG,"简单模式按钮");

                //查询简单模式的数据


                //依次查询每一个内容


                //跳转界面
                Intent intent_easy = new Intent(this, LevelActivity.class);
                //加入数据
                Bundle bundle_easy = new Bundle();
                //加入关卡模式数据
                bundle_easy.putString("mode","简单");
                //加入关卡数据

                //跳转
                startActivity(intent_easy);

                break;
            case R.id.start_normal:
                Log.d(Constant.TAG,"普通模式按钮");

                //查询简单模式的数据


                //依次查询每一个内容



                //跳转界面
                Intent intent_normal = new Intent(this, LevelActivity.class);
                //加入数据
                Bundle bundle_normal = new Bundle();
                //加入关卡模式数据
                bundle_normal.putString("mode","简单");
                //加入关卡数据

                //跳转
                startActivity(intent_normal);

                break;
            case R.id.start_hard:
                Log.d(Constant.TAG,"困难模式按钮");

                //查询简单模式的数据

                //依次查询每一个内容


                //跳转界面
                Intent intent_hard = new Intent(this, LevelActivity.class);
                //加入数据
                Bundle bundle_hard = new Bundle();
                //加入关卡模式数据
                bundle_hard.putString("mode","简单");
                //加入关卡数据

                //跳转
                startActivity(intent_hard);

                break;
            case R.id.main_setting:
                Log.d(Constant.TAG,"设置");

                //添加一个fragment
                final SettingFragment setting = new SettingFragment();
                transaction.replace(R.id.root_main,setting,"setting");
                transaction.commit();

                break;
//            case R.id.main_help:
//                Log.d(Constant.TAG,"帮助按钮");
//
//                //添加一个fragment
//                final HelpFragment help = new HelpFragment();
//                transaction.replace(R.id.root_main,help,"help");
//                transaction.commit();
//
//                break;
//            case R.id.main_store:
//                Log.d(Constant.TAG,"商店按钮");
//
//                //添加一个fragment
//                final StoreFragment store = new StoreFragment();
//                transaction.replace(R.id.root_main,store,"store");
//                transaction.commit();

        }
    }

//        @Override
//        protected void onDestroy() {
//            super.onDestroy();
//
//            unregisterReceiver(mBroadcastReceiver);
//        }
    }




