package com.example.androidapp.activity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.androidapp.constant.Constant;
import com.example.androidapp.model.XLLevel;
import com.example.androidapp.model.XLProp;
import com.example.androidapp.model.XLScore;
import com.example.androidapp.music.SoundPlayUtil;
import com.example.androidapp.R;
import com.example.androidapp.utils.LinkUtils;
import com.example.androidapp.view.XLTextView;


import org.litepal.LitePal;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class SuccessActivity extends BaseActivity implements View.OnClickListener {
    //星星
    ImageView left_star;
    ImageView middle_star;
    ImageView right_star;
    private List<ImageView> stars;

    //关卡文本
    XLTextView level_text;
    //分数文本
    XLTextView score_text;
    //时间文本
    XLTextView time_text;
    //连击文本
    XLTextView batter_text;

    //关卡菜单按钮
    Button menu_btn;
    Button refresh_btn;
    Button next_btn;

    //关卡
    XLLevel level;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);


        initView();

        //数据加载配置
        initData();

        //继续播放背景音乐
        //BackgroundMusicManager.getInstance(this).resumeBackgroundMusic();
    }

    /**
     * 加载数据
     */
    @SuppressLint("SetTextI18n")
    private void initData() {
        //获取数据
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        assert bundle != null;
        level = bundle.getParcelable("level");
        int serial_click = bundle.getInt("serial_click");

        //设置关卡数据
        level_text.setText("第"+level.getL_id()+"关");

        //设置星星
        int star_size = level.getL_new() - '0';
        Log.d(Constant.TAG,"星星个数："+level.getL_new()+" "+star_size);
        for (int i = 1; i <= star_size; i++) {
            stars.get(i-1).setVisibility(View.VISIBLE);
        }

        //设置时间
        time_text.findViewById(R.id.success_time);
        time_text.setText(level.getL_time()+"秒");


        //设置分数
        score_text.setText(LinkUtils.getScoreByTime(level.getL_time())+"分");

        //设置连击
        batter_text.setText(serial_click+"次");

        int rank=LinkUtils.getScoreByTime(level.getL_time());
        List<XLScore> scores = LitePal.findAll(XLScore.class);
        XLScore score=scores.get(0);

        ArrayList<Integer> a= new ArrayList<>();
        a.add(score.getOne_score());
        a.add(score.getTwo_score());
        a.add(score.getThree_score());
        a.add(score.getFour_score());
        a.add(score.getFive_score());
        a.add(rank);

        a.sort(Comparator.reverseOrder());
        score.setOne_score(a.get(0));
        score.setTwo_score(a.get(1));
        score.setThree_score(a.get(2));
        score.setFour_score(a.get(3));
        score.setFive_score(a.get(4));
        score.update(1);

    }

    /**
     * 找到相关的控件
     */

    //按钮的监听回调
    @Override
    public void onClick(View v) {
        //播放点击音效
        SoundPlayUtil.getInstance(getBaseContext()).play(3);

        switch (v.getId()){
            case R.id.success_level_btn:
                //关卡菜单按钮
                Log.d(Constant.TAG,"关卡菜单按钮");

                jumpToActivity(0);

                break;
            case R.id.success_restart_btn:
                //重新加载按钮
                Log.d(Constant.TAG,"重新加载按钮");

                jumpToActivity(1);

                break;
            case R.id.success_next_btn:
                //下一关按钮
                Log.d(Constant.TAG,"下一个关卡按钮");

                jumpToActivity(2);

                break;
        }
    }
    private void initView() {
        stars = new ArrayList<>();
        left_star = findViewById(R.id.success_crown1);
        left_star.setVisibility(View.INVISIBLE);
        middle_star = findViewById(R.id.success_crown2);
        middle_star.setVisibility(View.INVISIBLE);
        right_star = findViewById(R.id.success_crown3);
        right_star.setVisibility(View.INVISIBLE);
        stars.add(left_star);
        stars.add(middle_star);
        stars.add(right_star);

        level_text = findViewById(R.id.success_level_text);
        score_text = findViewById(R.id.success_score);
        time_text = findViewById(R.id.success_time);
        batter_text = findViewById(R.id.success_number);

        menu_btn = findViewById(R.id.success_level_btn);
        menu_btn.setOnClickListener(this);
        refresh_btn = findViewById(R.id.success_restart_btn);
        refresh_btn.setOnClickListener(this);
        next_btn = findViewById(R.id.success_next_btn);
        next_btn.setOnClickListener(this);
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
        }else if (flag == 1){
            //跳转界面
            Intent intent = new Intent(this, LinkActivity.class);
            //加入数据
            Bundle bundle = new Bundle();
            //加入关卡数据
            bundle.putParcelable("level",level);
            intent.putExtras(bundle);
            startActivity(intent);
        }else {
            //下一关
            //跳转界面
            Intent intent = new Intent(this, LinkActivity.class);
            //加入数据
            Bundle bundle = new Bundle();
            //加入关卡数据
            XLLevel next_level = LitePal.find(XLLevel.class, level.getId() + 1);
            bundle.putParcelable("level",next_level);
            intent.putExtras(bundle);
            //跳转
            startActivity(intent);
        }
    }
}
