package com.example.androidapp.activity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androidapp.constant.Constant;
import com.example.androidapp.music.BackgroundMusicManager;
import com.example.androidapp.utils.StateUtil;

public class BaseActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//始终竖屏
    }

    @Override
    protected void onStop() {
        super.onStop();

        //判断是否进入后台
        if (StateUtil.isBackground(this)) {
            //暂停播放
            BackgroundMusicManager.getInstance(this).pauseBackgroundMusic();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        //继续播放背景音乐
        if (!BackgroundMusicManager.getInstance(this).isBackgroundMusicPlaying()) {
            BackgroundMusicManager.getInstance(this).resumeBackgroundMusic();
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Log.d(Constant.TAG,"系统返回");
    }
}
