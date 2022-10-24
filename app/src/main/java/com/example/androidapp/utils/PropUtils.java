package com.example.androidapp.utils;

import com.example.androidapp.LinkGame.LinkModel.Kernel;
import com.example.androidapp.LinkGame.LinkModel.Point;

import java.util.ArrayList;
import java.util.List;

import static com.example.androidapp.LinkGame.LinkModel.Kernel.UNBLOCKED;

public class PropUtils {
    /**
     * 刷新道具
     * 对连连看剩下的内容进行随机的重新放置
     * @param LinkBoard
     */
    public static void Refresh(int[][] LinkBoard) {
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
    }

    /**
     * 获得提示
     * 返回一对可消除的点:
     * @param  LinkBoard
     * @return List(P1,P2)||null
     */
    public static List<Point> Tip(int[][] LinkBoard)
    {
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

    /**
     * 炸弹道具，消除连连看面板上同一类型的图片
     * 返回被炸掉的坐标的合集
     * @param LinkBoard
     * @return List(P1,P2,...)
     */
    public static List<Point> Bomb(int[][] LinkBoard)
    {
        ArrayList<Integer> resource=new ArrayList<>();
//        int resourceNum = 0;
        for(int i =1;i< LinkBoard.length-1;i++)
        {
            for(int j=1;j<LinkBoard[0].length-1;j++)
            {
                if(!resource.contains(LinkBoard[i][j])&&LinkBoard[i][j]!=UNBLOCKED) {
                    resource.add(LinkBoard[i][j]);
                }
            }
        }
//        resourceNum=resource.size();
        List<Point> points = new ArrayList<>();
        int bomb = resource.get((int) (Math.random() * resource.size()));
        for (int i = 0; i < LinkBoard.length; i++) {
            for (int j = 0; j < LinkBoard[0].length; j++) {
                if (LinkBoard[i][j] == bomb) {
//                       LinkBoard[i][j] = UNBLOCKED;//是否改变面板
                    points.add(new Point(i, j));
                }
            }
        }
        return points;
    }

}
