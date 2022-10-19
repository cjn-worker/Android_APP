package com.example.androidapp.manager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

import com.example.androidapp.Activity.LinkActivity;
import com.example.androidapp.Constant.Constant;
import com.example.androidapp.Constant.LinkConstant;
import com.example.androidapp.LinkGame.LinkModel.Point;
import com.example.androidapp.LinkGame.Utils.LinkUtils;
import com.example.androidapp.LinkGame.Utils.PxUtil;
import com.example.androidapp.Model.XLLevel;
import com.example.androidapp.Music.BackgroundMusicManager;
import com.example.androidapp.Music.SoundPlayUtil;
import com.example.androidapp.view.ImgView;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import tyrantgit.explosionfield.ExplosionField;

public class GameManager
{
    private int[][] board;
    private List<ImgView> imgViews = new ArrayList<>();
    private ImgView lastView;
    private int size;
    private Context mContext;
    private Timer timer;
    private float time = LinkConstant.TIME;
    private LinkActivity listener;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){

        @Override
        public void handleMessage(@NonNull Message msg) {
            //判断消息来源
            if (msg.what == Constant.TIMER){
                //Log.d(Constant.TAG,"测试处理");

                //时间减少
                time -= 0.1;

                //设置文本
                listener.onTimeChanged(time);
            }
        }
    };

    private boolean isPause = false;
    //单例模式
    private static GameManager instance;
    public static synchronized GameManager getManager(){
        if (instance == null){
            instance = new GameManager();
        }
        return instance;
    }


    //水平方向偏移间距
    private int padding_hor;



    //竖直方向偏移间距
    private int padding_ver;

    private ExplosionField explosionField;





    public void startGame(Context context, RelativeLayout layout, int width, int height,int level_id, char level_mode)
    {
        this.mContext = context;
        clearLastGame();
        //产生二维数组布局模板
        setBoard(LinkUtils.loadLevelWithIdAndMode(level_id,level_mode));
//        int[][] a = {
//                {-1, -1, -1, -1},
//                {-1, 1, 1, -1},
//                {-1, 1, 1, -1},
//                {-1, -1, -1, -1}};
//        setBoard(a);

        //界面布局
        addViewToLayout(context, layout, width, height);
        //开启定时器
        startTimer(time);
    }

    private void clearLastGame() {
        board = null;
        time = LinkConstant.TIME;
        padding_hor = 0;
        padding_ver = 0;
        imgViews.clear();
        lastView = null;
        size = 0;
        isPause = false;
    }
    private void addViewToLayout(Context context, RelativeLayout layout, int width, int height){
        //随机加载AnimalView的显示图片
        List<Integer> resources = LinkUtils.loadPictureResourceWithBoard(getBoard());

        //横竖方向的个数
        int row_animal_num = getBoard().length;
        int col_animal_num = getBoard()[0].length;

        //循环找到适合的大小
        for (int size = LinkConstant.IMAGE_SIZE; size >= 10; size--) {

            //如果宽度高度都满足条件
            if (size * col_animal_num < PxUtil.pxToDp(width,context) &&
                    size * row_animal_num < PxUtil.pxToDp(height,context)){
                this.size = size;
                break;
            }
        }
        //计算两边的间距
        padding_hor = (width - col_animal_num * PxUtil.dpToPx(size, context)) / 2;
        padding_ver = (height - row_animal_num * PxUtil.dpToPx(size, context)) / 2;

        //依次添加到布局中
        for (int i = 0; i < row_animal_num; i++)
        {
            for (int j = 0; j < col_animal_num; j++)
            {
                //判断当前位置是否需要显示内容
                ImgView imgView;
                if (getBoard()[i][j] == -1)
                {
                    imgView = new ImgView(
                            context,
                            getBoard()[i][j],
                            new Point(i, j));
                    imgView.setVisibility(View.INVISIBLE);
                }
                else
                {
                    //创建一个AnimalView
                    imgView = new ImgView(
                            context,
                            //R.drawable.score,
                            LinkConstant.RESOURCE[getBoard()[i][j]],
                            getBoard()[i][j],
                            new Point(i, j)
                    );
                }
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                        PxUtil.dpToPx(size, context),
                        PxUtil.dpToPx(size, context)
                );

                //左上间距
                layoutParams.leftMargin = padding_hor + PxUtil.dpToPx(size, context) * j;
                layoutParams.topMargin = padding_ver + PxUtil.dpToPx(size, context) * i;

                //设置内间距
                imgView.setPadding(
                        PxUtil.dpToPx(LinkConstant.IMAGE_PADDING, context),
                        PxUtil.dpToPx(LinkConstant.IMAGE_PADDING, context),
                        PxUtil.dpToPx(LinkConstant.IMAGE_PADDING, context),
                        PxUtil.dpToPx(LinkConstant.IMAGE_PADDING, context)
                );
//                //添加视图
                layout.addView(imgView,layoutParams);
                if (imgView.getFlag() != -1){
                    imgViews.add(imgView);
                }
            }
        }

    }

    //开启定时器
    private void startTimer(float time){
        //取消之前的定时器
        if (timer != null){
            stopTimer();
        }

        //以游戏时间开启定时器
        timer = new Timer();

        //启动定时器每隔一秒，发送一次消息
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(Constant.TIMER);
            }
        },0,100);

    }

    public void setListener(LinkActivity listener)
    {
        this.listener = listener;
    }

    //关闭定时器
    private void stopTimer() {
        if (timer != null){
            timer.cancel();
            timer = null;
        }
    }

    /**
     * 暂停游戏
     */
    public void pauseGame(){
        //判断是打开还是关闭
        if (!isPause){
            stopTimer();
        }else {
            startTimer(time);
        }
        //切换状态
        isPause = !isPause;
    }

    public void endGame(final Context context, XLLevel level, float time) {
        if (time < 0.1){
            Log.d(Constant.TAG, "失败啦");

            //界面跳转
            Intent intent = new Intent(context, LinkActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable("level",level);
            intent.putExtras(bundle);
            context.startActivity(intent);

            //暂停背景音乐
            BackgroundMusicManager.getInstance(context).pauseBackgroundMusic();
            //播放失败音效
            SoundPlayUtil.getInstance(context).play(2);

            //继续播放
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    BackgroundMusicManager.getInstance(context).resumeBackgroundMusic();
                }
            },5000);

        }else {
            Log.d(Constant.TAG, "成功啦");

            Intent intent = new Intent(context, LinkActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable("level",level);
            //bundle.putInt("serial_click",LinkUtils.getSerialClick());
            intent.putExtras(bundle);
            context.startActivity(intent);

            //暂停背景音乐
            BackgroundMusicManager.getInstance(context).pauseBackgroundMusic();
            //播放成功音效
            SoundPlayUtil.getInstance(context).play(1);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    BackgroundMusicManager.getInstance(context).resumeBackgroundMusic();
                }
            },5000);
        }

        //自定义 从右向左滑动的效果
        //((Activity)context).overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        // 自定义的淡入淡出动画效果
        //((Activity)context).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        //清除上一场游戏
        clearLastGame();
    }

    public interface LinkGame{
        //时间改变了
        void onTimeChanged(float time);
    }
    public List<ImgView> getImgViews()
    {
        return imgViews;
    }

    public void setLastView(ImgView lastView)
    {
        this.lastView = lastView;
    }

    public ImgView getLastView()
    {
        return lastView;
    }

    public int[][] getBoard() {
        return board;
    }

    public void setBoard(int[][] board) {
        this.board = board;
    }

    public int getPadding_hor()
    {
        return padding_hor;
    }

    public int getPadding_ver()
    {
        return padding_ver;
    }

    public int getSize()
    {
        return size;
    }

    public boolean isPause()
    {
        return isPause;
    }
}
