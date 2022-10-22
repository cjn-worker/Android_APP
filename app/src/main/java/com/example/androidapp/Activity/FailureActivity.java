package com.example.androidapp.Activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;


import com.example.androidapp.Constant.Constant;
import com.example.androidapp.Model.XLLevel;
import com.example.androidapp.Music.SoundPlayUtil;
import com.example.androidapp.R;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;



public class FailureActivity extends BaseActivity {

    //level
    XLLevel level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_failure);

        //获取数据
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        assert bundle != null;
        level = bundle.getParcelable("level");

        //关卡菜单按钮
        findViewById(R.id.btn_failure_level).setOnClickListener(v -> {
            //播放点击音效
            SoundPlayUtil.getInstance(getBaseContext()).play(3);

            Log.d(Constant.TAG,"关卡菜单按钮被点击了");

            jumpToActivity(0);
        });

        //刷新按钮
        findViewById(R.id.btn_failure_restart).setOnClickListener(v -> {
            //播放点击音效
            SoundPlayUtil.getInstance(getBaseContext()).play(3);

            Log.d(Constant.TAG,"刷新按钮被点击了");

            jumpToActivity(1);
        });

        //继续播放背景音乐
        //BackgroundMusicManager.getInstance(this).resumeBackgroundMusic();
    }

    /**
     * 跳转界面
     * @param flag
     */
    private void jumpToActivity(int flag){
        if (flag == 0){
            //查询对应模式的数据
            List<XLLevel> XLLevels = LitePal.where("l_mode == ?", String.valueOf(level.getL_mode())).find(XLLevel.class);
            Log.d(Constant.TAG,XLLevels.size()+"");
            //依次查询每一个内容
            for (XLLevel xlLevel : XLLevels) {
                Log.d(Constant.TAG, xlLevel.toString());
            }

            //跳转界面
            Intent intent = new Intent(this, LevelActivity.class);
            //加入数据
            Bundle bundle = new Bundle();
            //加入关卡模式数据
            bundle.putString("mode","简单");
            //加入关卡数据
            bundle.putParcelableArrayList("levels", (ArrayList<? extends Parcelable>) XLLevels);
            intent.putExtras(bundle);
            startActivity(intent);
        }else {
            //跳转界面
            Intent intent = new Intent(this, LinkActivity.class);
            //加入数据
            Bundle bundle = new Bundle();
            //加入关卡数据
            bundle.putParcelable("level",level);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
}
