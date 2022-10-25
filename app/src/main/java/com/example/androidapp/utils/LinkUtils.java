package com.example.androidapp.utils;

import java.util.ArrayList;
import static com.example.androidapp.game.link.Kernel.UNBLOCKED;
import android.content.Context;
import com.example.androidapp.constant.LinkConstant;
import com.example.androidapp.game.link.Kernel;
import com.example.androidapp.game.link.LinkInfoList;
import com.example.androidapp.game.link.Point;
import com.example.androidapp.game.manager.GameManager;


public class LinkUtils {
    /**
     * 判断连连看是否全部消除
     * @param LinkBoard
     * @return boolean
     */
    public static boolean isCleared(int[][] LinkBoard)
    {
        if (LinkBoard == null){
            return false;
        }
        int col = LinkBoard[0].length;
        for (int[] ints : LinkBoard) {
            for (int j = 0; j < col; j++) {
                if (ints[j] != UNBLOCKED)
                    return false;
            }
        }
        return true;
    }
    /**
     * 让计算机判断当前连连看是否是存在解的
     * @param LinkBoard
     * @return boolean
     */
    private static boolean Auto(int[][] LinkBoard)
    {
        int [][] cloneBoard = LinkUtils.Clone(LinkBoard);
        LinkInfoList linkInfo = new LinkInfoList();//用来存放所有获得的连接对
        int row = cloneBoard.length - 1;
        int col = cloneBoard[0].length - 1;
        while (isCleared(cloneBoard)) {
            int start = linkInfo.getLink().size();//获取连接对目前的大小
            //循环遍历，找到当前所有可以消除的连接对
            for (int i = 1; i < row; i++) {
                for (int j = 1; j < col; j++) {
                    Point src = new Point(i, j);
                    boolean find = false;
                    for (int m = 1; m < row && !find; m++) {
                        for (int n = 1; n < col && !find; n++) {
                            Point des = new Point(m, n);
                            if (!src.isEqual(des)&&Kernel.findLink(LinkBoard, src, des, linkInfo)) {
                                cloneBoard[i][j] = cloneBoard[m][n] = UNBLOCKED;
                                find = true;
                            }
                        }
                    }
                }
            }
            int end = linkInfo.getLink().size();//结束循环，获得所有连接对的大小
            if(end==start)//如果此次循环没有新添可消除的连接对，说明板子无解，返回false
                return false;
        }
        return true;
    }

    /**
     * 根据难度生成面板
     * @param level
     * @return int[][]
     */
    private static int[][] generate(int level)
    {
        int sourceNum=0;
        int row=0;
        int col=0;
        switch (level)
        {
            case 1:
                row=8;
                col=6;
                sourceNum=row-2;
                break;
            case 2:
                row=10;
                col=8;
                sourceNum=row-2;
                break;
            case 3:
                row=12;
                col=10;
                sourceNum=row-2;
                break;
        }
        int [][]LinkBoard=new int[row][col];
        //将板子最外层一圈置为UNBLOCKED，表示没有阻碍
        for(int i=0;i<row;i++)
            LinkBoard[i][0]=LinkBoard[i][col-1]=UNBLOCKED;
        for(int j=0;j<col;j++)
            LinkBoard[0][j]=LinkBoard[row-1][j]=UNBLOCKED;
        //从可选的资源中，随机选择不重复的sourceNum个所需的资源
        ArrayList<Integer> sourceList = new ArrayList<>();
        for(int i =0 ;i<sourceNum;i++) {
            int k = (int) (Math.random() * LinkConstant.RESOURCE.length);
            while (sourceList.contains(k)) {
                k = (int) (Math.random() * LinkConstant.RESOURCE.length);
            }
            sourceList.add(k);
        }
        //分为col-2组，每组的资源相同
        ArrayList<Integer> dataList = new ArrayList<>();
        for(int i=0;i<col-2;i++)
        {
            for(int j=0;j<sourceNum;j++){
                dataList.add(sourceList.get(j));
            }
        }
        //将获得的所有资源随机放在板子上
        for(int i=1;i<LinkBoard.length-1;i++)
        {
            for(int j=1;j<LinkBoard[0].length-1;j++)
            {
                int index = (int)(Math.random()* dataList.size());
                LinkBoard[i][j]=dataList.get(index);
                dataList.remove(index);
            }
        }
        return LinkBoard;
    }

    /**
     * 根据关卡难度生成连连看
     * @param level
     *
     */
    public static int[][] generateBoard(int level)
    {
        int[][] LinkBoard;
        //判断当前函数有没有一个可行解
        do
        {
            LinkBoard = generate(level);
        }while(!Auto(LinkBoard));
        return LinkBoard;
    }

    /**
     * 对面板进行克隆
     * @param LinkBoard
     * @return
     */
    public static int [][] Clone(int[][] LinkBoard)
    {
        int[][] cloneBoard=new int[LinkBoard.length][LinkBoard[0].length];
        for(int i=0;i< cloneBoard.length;i++)
        {
            for(int j=0;j<cloneBoard[0].length;j++)
            {
                cloneBoard[i][j]=LinkBoard[i][j];
            }
        }
        return cloneBoard;
    }

    /**
     * 获得面板上素材的坐标
     * @param point
     * @param context
     * @return
     */
    public static Point getRealAnimalPoint(Point point, Context context){
        GameManager manager = GameManager.getManager();
        return new Point(
                manager.getPadding_hor() + PxUtil.dpToPx(manager.getSize(),context) / 2 + point.getY()  * PxUtil.dpToPx(manager.getSize(),context),
                manager.getPadding_ver() + PxUtil.dpToPx(manager.getSize(),context) / 2 + point.getX()  * PxUtil.dpToPx(manager.getSize(),context)
        );
    }

    /**
     * 根据游戏时间获得星
     * @param time
     * @return char
     */
    public static char getStarByTime(int time){
        if (time <= 40){
            return '1';
        }else if (time <= 60){
            return '2';
        }else {
            return '3';
        }
    }

    /**
     * 根据游戏时间获取相关分数评价
     * @param time
     * @return int
     */
    public static int getScoreByTime(int time){
        return (LinkConstant.TIME -time) * LinkConstant.BASE_SCORE / LinkConstant.TIME;
    }

    /**
     * 获取连击次数
     * @return int
     */
    public static int getSerialClick(){
        //获取布局
        int[][] board = GameManager.getManager().getBoard();

        return (board.length-2) * (board[0].length-2) / 2;
    }
}
