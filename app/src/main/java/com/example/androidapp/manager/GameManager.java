package com.example.androidapp.manager;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.androidapp.Constant.LinkConstant;
import com.example.androidapp.LinkGame.LinkModel.Point;
import com.example.androidapp.LinkGame.Utils.PxUtil;
import com.example.androidapp.R;
import com.example.androidapp.view.ImgView;

import java.util.ArrayList;
import java.util.List;

import tyrantgit.explosionfield.ExplosionField;

public class GameManager
{
    private int[][] board;
    private List<ImgView> imgViews = new ArrayList<>();
    private ImgView lastView;
    private int size;
    private Context mContext;


    //水平方向偏移间距
    private int padding_hor;

    //竖直方向偏移间距
    private int padding_ver;

    private ExplosionField explosionField;
    public void startGame(Context context, RelativeLayout layout, int width, int height)
    {
        this.mContext = context;
        clearLastGame();
        //产生二维数组布局模板
        //setBoard(LinkUtils.generateBoard_dynamically(1));
        int[][] a = {
                {-1, -1, -1, -1},
                {-1, 1, 1, -1},
                {-1, 1, 1, -1},
                {-1, -1, -1, -1}};
        //界面布局
        setBoard(a);
        addViewToLayout(context, layout, width, height);

    }

    private void clearLastGame() {
        board = null;
        //time = LinkConstant.TIME;
        padding_hor = 0;
        padding_ver = 0;
        imgViews.clear();
        lastView = null;
        size = 0;
        //isPause = false;
    }
    private void addViewToLayout(Context context, RelativeLayout layout, int width, int height){
        //随机加载AnimalView的显示图片
//        List<Integer> resources = LinkUtils.loadPictureResourceWithBoard(getBoard());

        //横竖方向的个数
        int row_animal_num = getBoard().length;
        int col_animal_num = getBoard()[0].length;

        //循环找到适合的大小
        for (int size = LinkConstant.ANIMAL_SIZE; size >= 10; size--) {

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
                            R.drawable.score,
//                            resources.get(LinkConstant.ANIMAL_WOOD),//((getBoard()[i][j] > 0) ? LinkConstant.ANIMAL_RESOURCE[resources.get(getBoard()[i][j]-1)] : LinkConstant.ANIMAL_WOOD),
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
                        PxUtil.dpToPx(LinkConstant.ANIMAL_PADDING, context),
                        PxUtil.dpToPx(LinkConstant.ANIMAL_PADDING, context),
                        PxUtil.dpToPx(LinkConstant.ANIMAL_PADDING, context),
                        PxUtil.dpToPx(LinkConstant.ANIMAL_PADDING, context)
                );
//                //添加视图
                layout.addView(imgView,layoutParams);
                if (imgView.getFlag() != 0){
                    imgViews.add(imgView);
                }
            }
        }

    }

    public Context getmContext()
    {
        return mContext;
    }

    public int getSize()
    {
        return size;
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
}
