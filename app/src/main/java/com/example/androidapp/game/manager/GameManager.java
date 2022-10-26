package com.example.androidapp.game.manager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

import com.example.androidapp.activity.FailureActivity;
import com.example.androidapp.activity.LinkActivity;
import com.example.androidapp.activity.SuccessActivity;
import com.example.androidapp.constant.Constant;
import com.example.androidapp.constant.Enum.LevelMode;
import com.example.androidapp.constant.LinkConstant;
import com.example.androidapp.game.link.Point;
import com.example.androidapp.utils.LinkUtils;
import com.example.androidapp.utils.PropUtils;
import com.example.androidapp.utils.PxUtil;
import com.example.androidapp.model.LinkLevel;
import com.example.androidapp.music.BackgroundMusicManager;
import com.example.androidapp.music.SoundPlayUtil;
import com.example.androidapp.view.ImgView;
import static com.example.androidapp.game.link.Kernel.UNBLOCKED;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import tyrantgit.explosionfield.ExplosionField;

public class GameManager
{
    //板子,存放游戏数组
    private int[][] board;
    //存放图标的list
    private final List<ImgView> imgViews = new ArrayList<>();
    //上一个被选中的图标
    private ImgView lastView;
    //图标大小
    private int size;
    private Context mContext;
    //定时器
    private Timer timer;
    //给定的游戏时间
    private float time = LinkConstant.TIME;
    private LinkActivity listener;
    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler()
    {

        @Override
        public void handleMessage(@NonNull Message msg)
        {
            //判断消息来源
            if (msg.what == Constant.TIMER)
            {
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

    public static synchronized GameManager getManager()
    {
        if (instance == null)
        {
            instance = new GameManager();
        }
        return instance;
    }


    //水平方向偏移间距
    private int padding_hor;
    //竖直方向偏移间距
    private int padding_ver;


    //开始游戏
    public void startGame(Context context, RelativeLayout layout, int width, int height, int level_id, char level_mode)
    {
        this.mContext = context;
        clearLastGame();
        //产生二维数组布局模板
//        setBoard(LinkUtils.loadLevelWithIdAndMode(level_id,level_mode));
        if (level_mode == LevelMode.LEVEL_MODE_EASY.getValue())
        {
            setBoard(LinkUtils.generateBoard(1));
        }
        else if (level_mode == LevelMode.LEVEL_MODE_NORMAL.getValue())
        {
            setBoard(LinkUtils.generateBoard(2));
        }
        else
        {
            setBoard(LinkUtils.generateBoard(3));
        }

        //界面布局
        addViewToLayout(context, layout, width, height);
        //开启定时器
        startTimer(time);
    }

    //清除上一场游戏
    private void clearLastGame()
    {
        board = null;
        time = LinkConstant.TIME;
        padding_hor = 0;
        padding_ver = 0;
        imgViews.clear();
        lastView = null;
        size = 0;
        isPause = false;
    }

    //添加游戏视图
    private void addViewToLayout(Context context, RelativeLayout layout, int width, int height)
    {
        //随机加载图片
        //List<Integer> resources = LinkUtils.loadPictureResourceWithBoard(getBoard());

        //横竖方向的个数
        int row_animal_num = getBoard().length;
        int col_animal_num = getBoard()[0].length;

        //循环找到适合的大小
        for (int size = LinkConstant.IMAGE_SIZE; size >= 10; size--)
        {

            //如果宽度高度都满足条件
            if (size * col_animal_num < PxUtil.pxToDp(width, context) &&
                    size * row_animal_num < PxUtil.pxToDp(height, context))
            {
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
                    //创建一个ImgView
                    imgView = new ImgView(
                            context,
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
                layout.addView(imgView, layoutParams);
                if (imgView.getFlag() != -1)
                {
                    imgViews.add(imgView);
                }
            }
        }

    }

    //开启定时器
    private void startTimer(float time)
    {
        //取消之前的定时器
        if (timer != null)
        {
            stopTimer();
        }

        //以游戏时间开启定时器
        timer = new Timer();

        //启动定时器每隔一秒，发送一次消息
        timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                handler.sendEmptyMessage(Constant.TIMER);
            }
        }, 0, 100);

    }

    public void setListener(LinkActivity listener)
    {
        this.listener = listener;
    }

    //关闭定时器
    private void stopTimer()
    {
        if (timer != null)
        {
            timer.cancel();
            timer = null;
        }
    }

    //暂停游戏
    public void pauseGame()
    {
        //判断是打开还是关闭
        if (!isPause)
        {
            stopTimer();
        }
        else
        {
            startTimer(time);
        }
        //切换状态
        isPause = !isPause;
    }

    //结束游戏
    public void endGame(final Context context, LinkLevel level, float time)
    {
        if (time < 0.1)
        {
            //跳转失败界面
            Intent intent = new Intent(context, FailureActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable("level", level);
            intent.putExtras(bundle);
            context.startActivity(intent);

            //暂停背景音乐
            BackgroundMusicManager.getInstance(context).pauseBackgroundMusic();
            //播放失败音效
            SoundPlayUtil.getInstance(context).play(2);

            //继续播放
            new Handler().postDelayed(() -> BackgroundMusicManager.getInstance(context).resumeBackgroundMusic(), 5000);

        }
        else
        {
            //跳转成功界面
            Intent intent = new Intent(context, SuccessActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable("level", level);
            bundle.putInt("serial_click", LinkUtils.getSerialClick());
            intent.putExtras(bundle);
            context.startActivity(intent);

            //暂停背景音乐
            BackgroundMusicManager.getInstance(context).pauseBackgroundMusic();
            //播放成功音效
            SoundPlayUtil.getInstance(context).play(1);

            new Handler().postDelayed(() -> BackgroundMusicManager.getInstance(context).resumeBackgroundMusic(), 5000);
        }


        //清除上一场游戏
        clearLastGame();
    }

    //提示道具
    public void tipGame(Activity link_activity)
    {
        //1.产生一对消除的点
//        Point[] doubleRemove = LinkUtils.getDoubleRemove();
        List<Point> toRemove = PropUtils.Tip(getBoard());
        System.out.println(toRemove);
        //2.board修改
        for(int i =0;i<toRemove.size();i++)
        {
            board[toRemove.get(i).getX()][toRemove.get(i).getY()]=UNBLOCKED;
        }
        //3.播放消除音效以及粉碎
        SoundPlayUtil.getInstance(mContext).play(4);
        //粉碎、
        ExplosionField explosionField = ExplosionField.attach2Window(link_activity);

        //4.ImgView隐藏
        for (ImgView imgView : imgViews) {
            for (int i = 0; i < toRemove.size(); i++) {
                System.out.println(imgView.getPoint());
                if (imgView.getPoint().isEqual(toRemove.get(i))) {
                    //恢复背景颜色和清除动画
                    if(imgView.getVisibility()==View.VISIBLE) {
                        if (imgView.getAnimation() != null) {
                            imgView.clearAnimation();
                        }

                        //粉碎
                        explosionField.explode(imgView);
                        //隐藏
                        imgView.setVisibility(View.INVISIBLE);
                    }
                }
            }
        }
    }

    //炸弹道具
    public void bombGame(Activity link_activity)
    {
        //获得消除的点
        List<Point> toBomb = PropUtils.Bomb(getBoard());
        int value = board[toBomb.get(0).getX()][toBomb.get(0).getY()];
        //修改board
        for(int i =0;i<toBomb.size();i++)
        {
            board[toBomb.get(i).getX()][toBomb.get(i).getY()]=UNBLOCKED;
        }

        //3.播放消除音效以及粉碎
        SoundPlayUtil.getInstance(mContext).play(4);
        //粉碎、
        ExplosionField explosionField = ExplosionField.attach2Window(link_activity);

        //4.ImgView隐藏
        for (ImgView imgView : imgViews) {
            if(imgView.getFlag()==value){
                //恢复背景颜色和清除动画
                if(imgView.getVisibility()==View.VISIBLE) {
                    if (imgView.getAnimation() != null) {
                        imgView.clearAnimation();
                    }

                    //粉碎
                    explosionField.explode(imgView);
                    //隐藏
                    imgView.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    //刷新道具
    public void refreshGame(final Context context, final RelativeLayout layout, final int width, final int height, final int level_id, final char level_mode, Activity link_activity)
    {
        //0.播放消除音效以及粉碎
        SoundPlayUtil.getInstance(mContext).play(4);
        //粉碎、
        ExplosionField explosionField = ExplosionField.attach2Window(link_activity);

        //1.所有的ImgView消失
        for (ImgView imgView : imgViews)
        {
            //恢复背景颜色和清除动画
            if(imgView.getVisibility()==View.VISIBLE) {
                if (imgView.getAnimation() != null) {
                    imgView.clearAnimation();
                }

                //粉碎
//                explosionField.explode(imgView);
                //隐藏
                imgView.setVisibility(View.INVISIBLE);
            }
        }
        PropUtils.Refresh(board);
        imgViews.clear();
//        while(imgViews.size()!=0)
//            imgViews.remove(0);

        addViewToLayout(context, layout, width, height);
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

    public int[][] getBoard()
    {
        return board;
    }

    public void setBoard(int[][] board)
    {
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
