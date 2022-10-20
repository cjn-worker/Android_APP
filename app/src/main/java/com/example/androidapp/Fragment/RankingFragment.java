package com.example.androidapp.Fragment;



import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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

import com.example.androidapp.Constant.Constant;
import com.example.androidapp.Constant.Enum.PropMode;
import com.example.androidapp.Model.XLProp;
import com.example.androidapp.Model.XLScore;
import com.example.androidapp.Model.XLUser;
import com.example.androidapp.Music.SoundPlayUtil;
import com.example.androidapp.R;
import swu.xl.numberitem.NumberOfItem;

public class RankingFragment extends Fragment {
    //存储数据
    private int one_score = 0;
    private int two_score = 0;
    private int three_score = 0;
    private int four_score = 0;
    private int five_score = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //加载布局
        final View inflate = inflater.inflate(R.layout.rank_view, container, false);

        //拦截事件
        inflate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });


        //查询用户数据
        List<XLScore> scores = LitePal.findAll(XLScore.class);
        XLScore score = scores.get(0);
        //存储用户数据
        one_score = score.getOne_score();
        two_score = score.getTwo_score();
        three_score = score.getThree_score();
        four_score = score.getFour_score();
        five_score = score.getFive_score();



        //找到显示道具价值的文本
        TextView one_score_text = inflate.findViewById(R.id.one_score6);
        one_score_text.setText(String.valueOf(one_score));

        TextView two_score_text = inflate.findViewById(R.id.one_score5);
        two_score_text.setText(String.valueOf(two_score));

        TextView three_score_text = inflate.findViewById(R.id.one_score4);
        three_score_text.setText(String.valueOf(three_score));

        TextView four_score_text = inflate.findViewById(R.id.one_score3);
        four_score_text.setText(String.valueOf(four_score));

        TextView five_score_text = inflate.findViewById(R.id.one_score2);
        five_score_text.setText(String.valueOf(five_score));


        //移除该视图
        inflate.findViewById(R.id.rank_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //播放点击音效
                SoundPlayUtil.getInstance(getContext()).play(3);

                if (getActivity() != null){
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.remove(RankingFragment.this);
                    transaction.commit();
                }else {
                    System.out.println("空的Activity");
                }
            }
        });

        return inflate;
    }

}


