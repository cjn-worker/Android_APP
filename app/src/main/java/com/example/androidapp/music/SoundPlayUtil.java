package com.example.androidapp.music;

import android.content.Context;
import android.media.SoundPool;
import android.util.Log;

import com.example.androidapp.constant.Constant;
import com.example.androidapp.R;

public class SoundPlayUtil {
    //及时音频播放类
    private SoundPool soundPool;

    //声音大小
    private float voice = 1;

    //单例模式
    private static SoundPlayUtil instance;
    private SoundPlayUtil(Context context){
        if (soundPool == null){
            Log.d(Constant.TAG,"初始化及时播放音频类");
            //设置同时播放流的最大数量
            soundPool = new SoundPool.Builder().setMaxStreams(3).build();
            //加载音频文件
            soundPool.load(context,R.raw.game_victory,1);   // 1 游戏闯关成功
            soundPool.load(context,R.raw.game_failure,1);   // 2 游戏闯关失败
            soundPool.load(context,R.raw.game_click_btn,1); // 3 点击按钮的音效
            soundPool.load(context,R.raw.game_remove,1);    // 4 消除方块的音效
            soundPool.load(context,R.raw.game_un_click,1);  // 5 无法点击按钮的音效
            soundPool.load(context,R.raw.game_buy,1);       // 6 购买道具的音效
        }
    }
    public static synchronized SoundPlayUtil getInstance(Context context){
        if (instance == null){
            instance = new SoundPlayUtil(context);
        }
        return instance;
    }

    //播放指定音乐
    public void play(int soundID){
        soundPool.play(soundID,voice,voice,0,0,1);
    }

    //setter，getter
    public float getVoice() {
        return voice;
    }

    public void setVoice(float voice) {
        this.voice = voice;
    }
}

