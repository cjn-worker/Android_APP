package com.example.androidapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import org.litepal.LitePal;

import java.util.List;

import com.example.androidapp.constant.Enum.PropMode;
import com.example.androidapp.model.LinkProp;
import com.example.androidapp.model.LinkUser;
import com.example.androidapp.music.SoundPlayUtil;
import com.example.androidapp.R;

import swu.xl.numberitem.NumberOfItem;

public class StoreFragment extends Fragment
{
    //存储数据
    private int user_money = 0;
    private int tip_money = 0;
    private int tip_num = 0;
    private int bomb_money = 0;
    private int bomb_num = 0;
    private int refresh_money = 0;
    private int refresh_num = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        //加载布局
        final View inflate = inflater.inflate(R.layout.fragment_store, container, false);

        //拦截事件
        inflate.setOnTouchListener((v, event) -> true);


        //查询用户数据
        List<LinkUser> users = LitePal.findAll(LinkUser.class);
        LinkUser user = users.get(0);
        //存储用户数据
        user_money = user.getU_money();

        //查询道具数据
        List<LinkProp> props = LitePal.findAll(LinkProp.class);
        for (LinkProp prop : props)
        {
            if (prop.getP_kind() == PropMode.PROP_TIP.getValue())
            {
                //拳头道具
                tip_money = prop.getP_price();
                tip_num = prop.getP_number();
            }
            else if (prop.getP_kind() == PropMode.PROP_BOMB.getValue())
            {
                //炸弹道具
                bomb_money = prop.getP_price();
                bomb_num = prop.getP_number();
            }
            else
            {
                //刷新道具
                refresh_money = prop.getP_price();
                refresh_num = prop.getP_number();
            }
        }

        //找到显示道具数量的文本
        NumberOfItem prop_fight = inflate.findViewById(R.id.prop_tip);
        prop_fight.setCount(tip_num);
        NumberOfItem prop_bomb = inflate.findViewById(R.id.prop_bomb);
        prop_bomb.setCount(bomb_num);
        NumberOfItem prop_refresh = inflate.findViewById(R.id.prop_refresh);
        prop_refresh.setCount(refresh_num);

        //找到显示道具价值的文本
        TextView user_money_text = inflate.findViewById(R.id.store_user_money);
        user_money_text.setText(String.valueOf(user_money));
        TextView fight_money_text = inflate.findViewById(R.id.store_fight_money);
        fight_money_text.setText(String.valueOf(tip_money));
        TextView bomb_money_text = inflate.findViewById(R.id.store_bomb_money);
        bomb_money_text.setText(String.valueOf(bomb_money));
        TextView refresh_money_text = inflate.findViewById(R.id.store_refresh_money);
        refresh_money_text.setText(String.valueOf(refresh_money));

        //购买拳头
        LinearLayout fight = inflate.findViewById(R.id.store_tip);

        fight.setOnClickListener(v -> {
            SoundPlayUtil.getInstance(getContext()).play(6);
            refreshSQLite(PropMode.PROP_TIP, inflate);
        });

        //购买炸弹
        inflate.findViewById(R.id.store_bomb).setOnClickListener(v -> {
            //播放点击音效
            SoundPlayUtil.getInstance(getContext()).play(6);
            refreshSQLite(PropMode.PROP_BOMB, inflate);
        });

        //购买刷新
        inflate.findViewById(R.id.store_refresh).setOnClickListener(v -> {
            //播放点击音效
            SoundPlayUtil.getInstance(getContext()).play(6);
            refreshSQLite(PropMode.PROP_REFRESH, inflate);
        });

        //移除该视图
        inflate.findViewById(R.id.store_delete).setOnClickListener(v -> {
            //播放点击音效
            SoundPlayUtil.getInstance(getContext()).play(3);

            if (getActivity() != null)
            {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.remove(StoreFragment.this);
                transaction.commit();
            }
        });

        return inflate;
    }

    //刷新数据库
    private void refreshSQLite(PropMode mode, View inflate)
    {
        //道具对象
        LinkProp prop = new LinkProp();

        switch (mode)
        {
            case PROP_TIP:
                if(user_money>= tip_money) {
                    user_money -= tip_money;
                    tip_num++;

                    prop.setP_number(tip_num);
                    prop.update(1);

                    //道具购买提示
                    Toast.makeText(getContext(), "成功购买一个锤子道具，消耗" + tip_money + "金币", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(getContext(), "金币不足" , Toast.LENGTH_SHORT).show();
                break;
            case PROP_BOMB:
                if(user_money>=bomb_money) {
                    user_money -= bomb_money;
                    bomb_num++;

                    prop.setP_number(bomb_num);
                    prop.update(2);

                    //道具购买提示
                    Toast.makeText(getContext(), "成功购买一个炸弹道具，消耗" + bomb_money + "金币", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(getContext(), "金币不足" , Toast.LENGTH_SHORT).show();
                break;
            case PROP_REFRESH:
                if(user_money>=refresh_money) {
                    user_money -= refresh_money;
                    refresh_num++;

                    prop.setP_number(refresh_num);
                    prop.update(3);

                    //道具购买提示
                    Toast.makeText(getContext(), "成功购买一个重排道具，消耗" + refresh_money + "金币", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(getContext(), "金币不足" , Toast.LENGTH_SHORT).show();
                break;
        }

        //刷新用户数据
        LinkUser user = new LinkUser();
        user.setU_money(user_money);
        user.update(1);

        //重新设置金币
        TextView user_money_text = inflate.findViewById(R.id.store_user_money);
        user_money_text.setText(String.valueOf(user_money));

        //找到显示道具数量的文本
        NumberOfItem prop_fight = inflate.findViewById(R.id.prop_tip);
        prop_fight.setCount(tip_num);
        NumberOfItem prop_bomb = inflate.findViewById(R.id.prop_bomb);
        prop_bomb.setCount(bomb_num);
        NumberOfItem prop_refresh = inflate.findViewById(R.id.prop_refresh);
        prop_refresh.setCount(refresh_num);
    }
}
