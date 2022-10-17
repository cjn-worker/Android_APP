package com.example.androidapp.LinkGame.PropModel;

import com.example.androidapp.LinkGame.LinkModel.Kernel;
import com.example.androidapp.LinkGame.LinkModel.Point;
import com.example.androidapp.LinkGame.LinkModel.SealLinkInfo;

import java.util.ArrayList;
import java.util.List;

import static com.example.androidapp.LinkGame.LinkModel.Kernel.UNBLOCKED;
import static com.example.androidapp.LinkGame.Utils.LinkUtils.isBoardCleared;

public class Props {
    private static int refreshNum;
    private static int tipNum;
    private static int bombNum;
    private static int autoNum;

    public Props()
    {
     refreshNum=0;
     tipNum =0;
     bombNum=0;
     autoNum=0;
    }

    public Props(int r,int g,int b,int a)
    {
        refreshNum = r;
        tipNum = g;
        bombNum = b;
        autoNum=a;
    }


    /**
     * 刷新道具
     * 对连连看剩下的内容进行随机的重新放置
     * @param LinkBoard
     */
    public  boolean Refresh(int[][] LinkBoard) {
        if (refreshNum != 0) {
            ArrayList<Integer> dataList = new ArrayList<>();
            for (int i = 1; i < LinkBoard.length - 1; i++) {
                for (int j = 1; j < LinkBoard[0].length - 1; j++) {
                    if (LinkBoard[i][j] != UNBLOCKED) {
                        dataList.add(LinkBoard[i][j]);
                        LinkBoard[i][j] = UNBLOCKED;
                    }
                }
            }

            while (dataList.size() != 0) {
                int index = (int) (Math.random() * dataList.size());
                int x_place;
                int y_place;
                do {
                    x_place = (int) (Math.random() * (LinkBoard.length - 2)) + 1;
                    y_place = (int) (Math.random() * (LinkBoard[0].length - 2)) + 1;
                } while (LinkBoard[x_place][y_place] != UNBLOCKED);
                LinkBoard[x_place][y_place] = dataList.get(index);
                dataList.remove(index);
            }
            return true;
        }
        return false;
    }

    /**
     * 获得提示
     * 返回一对可消除的点:
     * @param  LinkBoard
     * @return List(P1,P2)||null
     */
    public  List<Point> Tip(int[][] LinkBoard)
    {
        if(tipNum !=0) {
            List<Point> points = new ArrayList<>();
            int row = LinkBoard.length - 1;
            int col = LinkBoard[0].length - 1;
            boolean find = false;
            for (int i = 1; i < row && !find; i++) {
                for (int j = 1; j < col && !find; j++) {
                    Point src = new Point(i, j);
                    if (LinkBoard[i][j] != UNBLOCKED) {
                        for (int m = 1; m < row && !find; m++) {
                            for (int n = 1; n < col && !find; n++) {
                                Point des = new Point(m, n);
                                if (LinkBoard[m][n] != UNBLOCKED && !src.isEqual(des)) {
                                    if (Kernel.findLink(LinkBoard, src, des, null)) {
                                        points.add(new Point(i, j));
                                        points.add(new Point(m, n));
                                        find = true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return points;
        }
        return null;
    }

    /**
     * 炸弹道具，消除连连看面板上同一类型的图片
     * 返回被炸掉的坐标的合集
     * @param LinkBoard
     * @return List(P1,P2,...)
     */
    public  List<Point> Bomb(int[][] LinkBoard)
    {
        int resourceNum = 0;
        for(int i =0;i< LinkBoard.length;i++)
        {
            for(int j=0;j<LinkBoard[0].length;j++)
            {
                if(LinkBoard[i][j]>resourceNum) {
                    resourceNum = LinkBoard[i][j];
                }
            }
        }
        resourceNum+=1;
        if(bombNum!=0) {
            List<Point> points = new ArrayList<>();
            int bomb = (int) (Math.random() * resourceNum);
            for (int i = 0; i < LinkBoard.length; i++) {
                for (int j = 0; j < LinkBoard[0].length; j++) {
                    if (LinkBoard[i][j] == bomb) {
                        LinkBoard[i][j] = UNBLOCKED;
                        points.add(new Point(i, j));
                    }
                }
            }
            return points;
        }
        return null;
    }

    /**
     * 让计算机自动求解连连看
     * @param LinkBoard
     * 返回List(L1(src,..,des),L2,L3)
     */
    public  List<List<Point>> Auto(int[][] LinkBoard)
    {
        if(autoNum!=0) {
            SealLinkInfo linkInfo = new SealLinkInfo();
            int row = LinkBoard.length - 1;
            int col = LinkBoard[0].length - 1;

            while (!isBoardCleared(LinkBoard)) {
                for (int i = 1; i < row; i++) {
                    for (int j = 1; j < col; j++) {
                        Point src = new Point(i, j);
                        if (LinkBoard[i][j] != UNBLOCKED) {
                            boolean find = false;
                            for (int m = 1; m < row && !find; m++) {
                                for (int n = 1; n < col && !find; n++) {
                                    Point des = new Point(m, n);
                                    if (LinkBoard[m][n] != UNBLOCKED && !src.isEqual(des)) {
                                        if (Kernel.findLink(LinkBoard, src, des, linkInfo)) {
                                            LinkBoard[i][j] = LinkBoard[m][n] = UNBLOCKED;
                                            find = true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return linkInfo.getLink();
        }
        return null;
    }


    public  int getRefreshNum() {
        return refreshNum;
    }

    public  int getTipNum() {
        return tipNum;
    }

    public  int getBombNum() {
        return bombNum;
    }

    public  void setBombNum(int bombNum) {
        Props.bombNum = bombNum;
    }

    public  void setTipNum(int getTipsNum) {
        Props.tipNum = getTipsNum;
    }

    public  void setRefreshNum(int refreshNum) {
        Props.refreshNum = refreshNum;
    }

    public static int getAutoNum() {
        return autoNum;
    }

    public static void setAutoNum(int autoNum) {
        Props.autoNum = autoNum;
    }

}