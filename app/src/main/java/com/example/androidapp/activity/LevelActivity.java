package com.example.androidapp.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.util.List;

import com.example.androidapp.constant.Constant;
import com.example.androidapp.constant.Enum.LevelState;
import com.example.androidapp.model.LinkLevel;
import com.example.androidapp.music.SoundPlayUtil;
import com.example.androidapp.R;
import com.example.androidapp.view.MyImageView;
import com.example.androidapp.utils.PxUtil;
import com.example.androidapp.utils.ScreenUtil;

public class LevelActivity extends BaseActivity implements View.OnClickListener {
    //屏幕宽度
    int screenWidth;

    //记录屏幕当前的偏移程度
    int offset = 0;

    //关卡模式数据
    String mode;

    //关卡数据
    List<LinkLevel> levels;

    //按钮
    ImageButton home;
    Button go_left;
    Button go_right;

    //确定总页数
    int page;

    //文本
    TextView page_info;

    //页面控制器
    HorizontalScrollView level_page_info;

    //关卡根布局
    RelativeLayout level_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_level);
        setContentView(R.layout.activity_level);


        //加载数据
        initData();

        //加载视图
        initView();

        //加载关卡
        initLevel();
    }

    /**
     * 加载数据
     */
    private void initData() {
        //获取数据
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        assert bundle != null;
        mode = bundle.getString("mode");
        levels = bundle.getParcelableArrayList("levels");

        //依次查询每一个内容
        Log.d(Constant.TAG,"--------");
        Log.d(Constant.TAG,mode);
        assert levels != null;
        Log.d(Constant.TAG,levels.size()+"");
        for (LinkLevel linkLevel : levels) {
            Log.d(Constant.TAG, linkLevel.toString());
        }
    }

    /**
     * 加载视图
     */
    @SuppressLint("ClickableViewAccessibility")
    private void initView() {
        //屏幕的宽高
        screenWidth = ScreenUtil.getScreenWidth(getApplicationContext());
        Log.d(Constant.TAG,"屏幕宽度："+screenWidth);

        home = findViewById(R.id.page_home);
        home.setOnClickListener(this);
        go_left = findViewById(R.id.page_left);
        go_left.setOnClickListener(this);
        go_right = findViewById(R.id.page_right);
        go_right.setOnClickListener(this);

        page_info = findViewById(R.id.page_text);

        level_page_info = findViewById(R.id.level_page);
        level_page_info.setOnTouchListener((v, event) -> {
            //禁止HorizontalScrollView滑动
            //滑动会影响页面控制器
            //HorizontalScrollView滑动时也没有回调方法
            return true;
        });

        level_layout = findViewById(R.id.level_root);
    }

    /**
     * 加载关卡
     */
    @SuppressLint("SetTextI18n")
    private void initLevel() {
        level_layout.post(() -> {
            //循环展示
            for (int i = 0; i < levels.size(); i++){
                //确定页数
                page = i / Constant.LEVEL_PAGE_COUNT;
                page_info.setText("1/"+(page+1));
                //确定在当前页数的第几行
                int pager_row = i % Constant.LEVEL_PAGE_COUNT / Constant.LEVEL_ROW_COUNT;
                //确定在当前页数的第几列
                int pager_col = i % Constant.LEVEL_PAGE_COUNT % Constant.LEVEL_ROW_COUNT;
                //边距
                int level_padding = (screenWidth - Constant.LEVEL_ROW_COUNT *
                        PxUtil.dpToPx(Constant.LEVEL_SIZE,getApplicationContext())) /
                        (Constant.LEVEL_ROW_COUNT + 1);

                //创建视图
                MyImageView myImageView = new MyImageView(
                        getApplicationContext(),
                        LevelState.getState(levels.get(i).getL_new()));

                //设置id
                myImageView.setId(i);

                //布局参数
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                        PxUtil.dpToPx(Constant.LEVEL_SIZE,getApplicationContext()),
                        PxUtil.dpToPx(Constant.LEVEL_SIZE,getApplicationContext())
                );

                //添加约束
                layoutParams.leftMargin = screenWidth * page + level_padding +
                        (level_padding + PxUtil.dpToPx(Constant.LEVEL_SIZE,getApplicationContext())) * pager_col;
                layoutParams.topMargin = ScreenUtil.getStateBarHeight(getApplicationContext()) +
                        PxUtil.dpToPx(Constant.LEVEL_TOP,getApplicationContext()) +
                        level_padding + (level_padding + PxUtil.dpToPx(Constant.LEVEL_SIZE,getApplicationContext())) * pager_row;

                //最后一位需要添加右边距
                if (i == levels.size()-1) {
                    layoutParams.rightMargin = level_padding;
                }

                //添加控件到容器中
                level_layout.addView(myImageView,layoutParams);

                //点击事件
                myImageView.setOnClickListener(v -> {
                    //播放点击音效
                    SoundPlayUtil.getInstance(getBaseContext()).play(3);
//                            判断是否可以进入该关卡
                    if (LevelState.getState(levels.get(v.getId()).getL_new()) != LevelState.LEVEL_STATE_NO)
                    {
                        jumpToLinkActivity(levels.get(v.getId()));

                    }
                    else
                    {
                        Toast.makeText(LevelActivity.this, "当前关卡不可进入", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    /**
     * 界面跳转
     */
    public void jumpToLinkActivity(LinkLevel level){
        //跳转界面
        Intent intent = new Intent(this, LinkActivity.class);
        //加入数据
        Bundle bundle = new Bundle();
        //加入关卡数据
        bundle.putParcelable("level",level);
        intent.putExtras(bundle);
        //跳转
        startActivity(intent);
    }


    /**
     * 左右滑动一个屏幕关卡视图
     * @param direction 1:右滑 -1:左滑
     * @return 返回值表示是否滑动
     */
    public boolean scrollLevelsOfDirection(int direction){

        if ((direction == 1 && offset == (page * screenWidth)) || (direction == -1 && offset == 0)){
            //如果当前向右滑动 且 当前已经处于最后一页 或
            //如果当前向左滑动 且 当前已经处于第一页
            return false;
        }else if(direction == 1 && offset == ((page -1) * screenWidth)) {
            //如果当前向右滑动 且 滑动后处于最后一页
            //右边的按钮设置不可用
            go_right.setEnabled(false);
        }else if(direction == -1 && offset == screenWidth){
            //如果当前向左滑动 且 滑动后处于第一页
            //左边的按钮设置不可用
            go_left.setEnabled(false);
        }else{
            //恢复
            go_left.setEnabled(true);
            go_right.setEnabled(true);
        }

        //滑动视图
        level_page_info.smoothScrollTo(offset + screenWidth * direction,0);

        //修改偏移值
        offset = offset + screenWidth * direction;

        //修改显示内容
        page_info.setText((offset / screenWidth+1) + "/" + (page +1));

        return true;
    }


    /**
     * 处理按钮的点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {

        //播放点击音效
        SoundPlayUtil.getInstance(getBaseContext()).play(3);

        switch (v.getId()){
            case R.id.page_home:
                Log.d(Constant.TAG,"返回按钮");

                startActivity(new Intent(LevelActivity.this,MainActivity.class));

                //淡入淡出切换动画
                //overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);

                //从左向右滑动动画
                //overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);

                //自定义动画
                overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
                break;
            case R.id.page_left:
                Log.d(Constant.TAG,"上一页");

                //左滑
                scrollLevelsOfDirection(-1);
                break;
            case R.id.page_right:
                Log.d(Constant.TAG,"下一页");

                //右滑
                scrollLevelsOfDirection(1);
                break;
        }
    }

}
