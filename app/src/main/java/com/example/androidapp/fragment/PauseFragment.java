package com.example.androidapp.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.androidapp.activity.LevelActivity;
import com.example.androidapp.activity.LinkActivity;
import com.example.androidapp.constant.Enum.LevelState;
import com.example.androidapp.model.LinkLevel;
import com.example.androidapp.music.SoundPlayUtil;
import com.example.androidapp.R;
import com.example.androidapp.game.manager.GameManager;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;


public class PauseFragment extends Fragment
{
    LinkLevel level;

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        //接受数据
        level = getArguments().getParcelable("level");

        //加载布局
        View inflate = inflater.inflate(R.layout.fragment_pause, container, false);

        //拦截事件
        inflate.setOnTouchListener((v, event) -> true);

        //处理事件
        inflate.findViewById(R.id.pause_level_btn).setOnClickListener(v -> {
            //播放点击音效
            SoundPlayUtil.getInstance(getContext()).play(3);
            //回到关卡
            //查询对应模式的数据
            List<LinkLevel> LinkLevels = LitePal.where("l_mode == ?", String.valueOf(level.getL_mode())).find(LinkLevel.class);
            //跳转界面
            Intent intent = new Intent(getActivity(), LevelActivity.class);
            //加入数据
            Bundle bundle = new Bundle();
            //加入关卡模式数据
            bundle.putString("mode", "简单");
            //加入关卡数据
            bundle.putParcelableArrayList("levels", (ArrayList<? extends Parcelable>) LinkLevels);
            intent.putExtras(bundle);
            startActivity(intent);
        });
        inflate.findViewById(R.id.pause_restart_btn).setOnClickListener(v -> {
            //播放点击音效
            SoundPlayUtil.getInstance(getContext()).play(3);
            //回到游戏
            GameManager.getManager().pauseGame();
            //移除自己
            if (getActivity() != null)
            {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.remove(PauseFragment.this);
                transaction.commit();
            }
        });
        inflate.findViewById(R.id.pause_next_btn).setOnClickListener(v -> {
            //播放点击音效
            SoundPlayUtil.getInstance(getContext()).play(3);

            //下一关
            //加入关卡数据
            LinkLevel next_level = LitePal.find(LinkLevel.class, level.getId() + 1);
            //判断是否开启
            if (next_level.getL_new() != LevelState.LEVEL_STATE_NO.getValue())
            {
                //跳转界面
                Intent intent = new Intent(getActivity(), LinkActivity.class);
                //加入数据
                Bundle bundle = new Bundle();
                bundle.putParcelable("level", next_level);
                intent.putExtras(bundle);
                //跳转
                startActivity(intent);
            }
            else
            {
                Toast.makeText(getActivity(), "下一关还没有开启", Toast.LENGTH_SHORT).show();
            }
        });

        return inflate;
    }
}
