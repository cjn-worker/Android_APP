package com.example.androidapp.activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;


import com.example.androidapp.constant.Constant;
import com.example.androidapp.model.LinkLevel;
import com.example.androidapp.music.SoundPlayUtil;
import com.example.androidapp.R;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;


public class FailureActivity extends BaseActivity
{
    //关卡
    LinkLevel level;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
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
            jumpToActivity(0);
        });

        //刷新按钮
        findViewById(R.id.btn_failure_restart).setOnClickListener(v -> {
            //播放点击音效
            SoundPlayUtil.getInstance(getBaseContext()).play(3);
            jumpToActivity(1);
        });

    }

    /**
     * 跳转界面
     *
     * @param flag
     */
    private void jumpToActivity(int flag)
    {
        if (flag == 0)
        {
            //查询对应模式的数据
            List<LinkLevel> LinkLevels = LitePal.where("l_mode == ?", String.valueOf(level.getL_mode())).find(LinkLevel.class);
            Log.d(Constant.TAG, LinkLevels.size() + "");
            //依次查询每一个内容
            for (LinkLevel linkLevel : LinkLevels)
            {
                Log.d(Constant.TAG, linkLevel.toString());
            }

            //跳转界面
            Intent intent = new Intent(this, LevelActivity.class);
            //加入数据
            Bundle bundle = new Bundle();
            //加入关卡模式数据
            bundle.putString("mode", "简单");
            //加入关卡数据
            bundle.putParcelableArrayList("levels", (ArrayList<? extends Parcelable>) LinkLevels);
            intent.putExtras(bundle);
            startActivity(intent);
        }
        else
        {
            //跳转界面
            Intent intent = new Intent(this, LinkActivity.class);
            //加入数据
            Bundle bundle = new Bundle();
            //加入关卡数据
            bundle.putParcelable("level", level);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
}

