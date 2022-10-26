package com.example.androidapp.utils;

import com.example.androidapp.game.link.Kernel;
import com.example.androidapp.game.link.Point;

import java.util.ArrayList;
import java.util.List;

import static com.example.androidapp.game.link.Kernel.UNBLOCKED;

public class PropUtils {
    /**
     * 刷新道具
     * 对连连看剩下的内容进行随机的重新放置
     * @param LinkBoard
     */
    public static void Refresh(int[][] LinkBoard) {
        //首先将面板上所有还剩的资源收集，并将面板全部位置UNBLOCKED
        ArrayList<Integer> dataList = new ArrayList<>();
        for (int i = 1; i < LinkBoard.length - 1; i++) {
            for (int j = 1; j < LinkBoard[0].length - 1; j++) {
                if (LinkBoard[i][j] != UNBLOCKED) {
                    dataList.add(LinkBoard[i][j]);
                    LinkBoard[i][j] = UNBLOCKED;
                }
            }
        }
        //将获得的所剩资源随机放在面板上
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
     * @return List<Point>
     */
    public static List<Point> Tip(int[][] LinkBoard)
    {
        List<Point> points = new ArrayList<>();
        int row = LinkBoard.length - 1;
        int col = LinkBoard[0].length - 1;
        boolean find = false;
        //循环遍历，找到一对可消除的
        for (int i = 1; i < row && !find; i++) {
            for (int j = 1; j < col && !find; j++) {
                Point src = new Point(i, j);
                    for (int m = 1; m < row && !find; m++) {
                        for (int n = 1; n < col && !find; n++) {
                            Point des = new Point(m, n);
                            if (!src.isEqual(des)
                                    &&Kernel.findLink(LinkBoard, src, des, null)) {
                                points.add(src);
                                points.add(des);
                                find = true;
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
     * @return List<Point>
     */
    public static List<Point> Bomb(int[][] LinkBoard)
    {
        ArrayList<Integer> resource=new ArrayList<>();
        //循环遍历，获得当前面板上所有的资源ID，ID不重复添加
        for(int i =1;i< LinkBoard.length-1;i++)
        {
            for(int j=1;j<LinkBoard[0].length-1;j++)
            {
                if(!resource.contains(LinkBoard[i][j])&&LinkBoard[i][j]!=UNBLOCKED) {
                    resource.add(LinkBoard[i][j]);
                }
            }
        }
        //从资源ID中随机选一个作为炸弹道具的作用对象，不改变面板
        List<Point> points = new ArrayList<>();
        int bomb = resource.get((int) (Math.random() * resource.size()));
        //收集面板上所有和炸弹值相同的点
        for (int i = 0; i < LinkBoard.length; i++) {
            for (int j = 0; j < LinkBoard[0].length; j++) {
                if (LinkBoard[i][j] == bomb) {
                    points.add(new Point(i, j));
                }
            }
        }
        return points;
    }

}
