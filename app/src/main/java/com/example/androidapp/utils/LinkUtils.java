package com.example.androidapp.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.example.androidapp.LinkGame.LinkModel.Kernel.UNBLOCKED;
import static com.example.androidapp.LinkGame.LinkModel.Kernel.findLink;

import android.content.Context;
import android.util.Log;

import com.example.androidapp.constant.Constant;
import com.example.androidapp.constant.Enum.LevelMode;
import com.example.androidapp.constant.LinkConstant;
import com.example.androidapp.LinkGame.LinkModel.Kernel;
import com.example.androidapp.LinkGame.LinkModel.LinkInfoList;
import com.example.androidapp.LinkGame.LinkModel.Point;
import com.example.androidapp.manager.GameManager;


public class LinkUtils {
    /**
     * 判断连连看是否全部消除
     * @param LinkBoard
     * @return
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
     * 让计算机判断当前连连看是否可解决
     * @param LinkBoard
     * 返回List(L1(src,..,des),L2,L3)
     */
    private static boolean Auto(int[][] LinkBoard)
    {
        int [][] cloneBoard = LinkUtils.Clone(LinkBoard);
        LinkInfoList linkInfo = new LinkInfoList();
        int row = cloneBoard.length - 1;
        int col = cloneBoard[0].length - 1;
        while (isCleared(cloneBoard)) {
            int start = linkInfo.getLink().size();
            for (int i = 1; i < row; i++) {
                for (int j = 1; j < col; j++) {
                    Point src = new Point(i, j);
                    if (cloneBoard[i][j] != UNBLOCKED) {
                        boolean find = false;
                        for (int m = 1; m < row && !find; m++) {
                            for (int n = 1; n < col && !find; n++) {
                                Point des = new Point(m, n);
                                if (cloneBoard[m][n] != UNBLOCKED && !src.isEqual(des)) {
                                    if (Kernel.findLink(LinkBoard, src, des, linkInfo)) {
                                        cloneBoard[i][j] = cloneBoard[m][n] = UNBLOCKED;
                                        find = true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            int end = linkInfo.getLink().size();
            if(end==start)
                return false;
        }
        return true;
    }
    private static int[][] generate(int level)
    {
        int sourceNum=0;
        int row=0;
        int col=0;
        switch (level)
        {
            case 1:
                row=6;
                col=6;
                sourceNum=col-2;
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
        for(int i=0;i<row;i++)
            LinkBoard[i][0]=LinkBoard[i][col-1]=UNBLOCKED;
        for(int j=0;j<col;j++)
            LinkBoard[0][j]=LinkBoard[row-1][j]=UNBLOCKED;
//        int total=(row-2)*(col-2);
        ArrayList<Integer> sourceList = new ArrayList<>();
        for(int i =0 ;i<sourceNum;i++) {
            int k = (int) (Math.random() * LinkConstant.RESOURCE.length);
            while (sourceList.contains(k)) {
                k = (int) (Math.random() * LinkConstant.RESOURCE.length);
            }
            sourceList.add(k);
        }
        ArrayList<Integer> dataList = new ArrayList<>();
        for(int i=0;i<col-2;i++)
        {
            for(int j=0;j<sourceNum;j++){
                dataList.add(sourceList.get(j));
            }
        }
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
        int[][] LinkBoard = generate(level);
        Auto(LinkBoard);
//        while(!Auto(LinkBoard))
//            LinkBoard=generate(level);

        return LinkBoard;
    }

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

    public static List<Integer> loadPictureResourceWithBoard(int[][] board){
        //初始化存储集合
        List<Integer> list = new ArrayList<>();

        //求出二维数组中的最大值
        int max = getMaxData(board);

        //随机加载图片
        Random random = new Random();
        while (list.size() != max){
            //产生指定范围内的随机数
            int var = random.nextInt(5);

            //重复的随机数不再加入
            int flag = 0;
            for (Integer integer : list) {
                if (integer == var) flag = 1;
            }

            //满足条件的加入
            if (flag == 0) list.add(var);
        }

        return list;
    }
    public static int getMaxData(int[][] matrix) {
        int row = matrix.length;
        int col = matrix[0].length;

        int[] data = new int[row * col];
        int index = 0;
        for (int[] ints : matrix)
        {
            for (int j = 0; j < col; j++)
            {
                data[index++] = ints[j];
            }
        }

        //除开特殊情况
        if (data.length == 0){
            return 0;
        }

        return getMaxDataBySort(data);
    }
    private static int getMaxDataBySort(int[] data){
        //排序
        mergeSort(data,0,data.length-1);

        return data[data.length-1];
    }
    private static void mergeSort(int[] nums,int start,int end){
        //只要划分的区间长度仍然不为1
        if (start != end){
            int middle = (start+end) / 2;

            //分
            mergeSort(nums,start,middle);
            mergeSort(nums,middle+1,end);

            //治
            merge(nums,start,end,middle);
        }
    }
    private static void merge(int[] nums,int start,int end,int middle){
        //模拟第一个序列的头指针
        int i = start;

        //模拟第二个序列的头指针
        int j = middle+1;

        //模拟临时数组的头指针
        int t = 0;

        //创建临时数组
        int[] temp = new int[end-start+1];

        //从头比较两个序列，小的放入临时数组temp
        while (i <= middle && j <= end){
            //比较大小
            if (nums[i] < nums[j]){
                //前一个序列
                temp[t++] = nums[i++];
            }else {
                //后一个序列
                temp[t++] = nums[j++];
            }
        }

        //单独处理没有处理完的第一个序列
        while (i <= middle){
            temp[t++] = nums[i++];
        }

        //单独处理没有处理完的第二个序列
        while (j <= end){
            temp[t++] = nums[j++];
        }

        //将临时数组的值赋值到原数组
        System.arraycopy(temp, 0, nums, start, temp.length);
    }

    public static Point getRealAnimalPoint(Point point, Context context){
        GameManager manager = GameManager.getManager();
        return new Point(
                manager.getPadding_hor() + PxUtil.dpToPx(manager.getSize(),context) / 2 + point.getY()  * PxUtil.dpToPx(manager.getSize(),context),
                manager.getPadding_ver() + PxUtil.dpToPx(manager.getSize(),context) / 2 + point.getX()  * PxUtil.dpToPx(manager.getSize(),context)
        );
    }

    public static int[][] loadLevelWithIdAndMode(int level_id, char level_mode){

        //判断是什么类型的关卡
        int [][][] BOARD;
        if (level_mode == LevelMode.LEVEL_MODE_EASY.getValue()){
            BOARD = LinkConstant.BOARD_EASY;
        }else if (level_mode == LevelMode.LEVEL_MODE_NORMAL.getValue()){
            BOARD = LinkConstant.BOARD_NORMAL;
        }else
        {
            BOARD = LinkConstant.BOARD_HARD;
        }

        //拷贝模板
        int[][] board = BOARD[level_id];
        int row = board.length;
        int col = board[0].length;
        int[][] clone = new int[row][col];
        for (int i = 0; i < row; i++) {
            System.arraycopy(board[i], 0, clone[i], 0, col);
        }

        return clone;
    }


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
     * @return
     */
    public static int getScoreByTime(int time){
        return (LinkConstant.TIME -time) * LinkConstant.BASE_SCORE / LinkConstant.TIME;
    }

    /**
     * 获取连击数
     */
    public static int getSerialClick(){
        //获取布局
        int[][] board = GameManager.getManager().getBoard();

        return (board.length-2) * (board[0].length-2) / 2;
    }

    public static Point[] getDoubleRemove(){
        //获得模板
        int[][] board = GameManager.getManager().getBoard();

        //存储两个相对坐标
        Point point1 = null;
        Point point2 = null;

        //找到两个点
        while (point1 == null && point2 == null){
            //寻找第一个点
            for (int i = 1; i < board.length-1; i++) {
                for (int j = 1; j < board[0].length-1; j++) {
                    //产生第一个点
                    point1 = new Point(i,j);

                    for (int p = 1; p < board.length-1; p++) {
                        for (int q = 1; q < board[0].length-1; q++) {
                            //产生第二个点
                            point2 = new Point(p,q);

                            //如果两个点不是同一个点
                            if (point1.getX() != point2.getX() || point1.getY() != point2.getY()){
                                //并且该两点图案相同 且不为0
                                if (board[point1.getX()][point1.getY()] == board[point2.getX()][point2.getY()]
                                        && board[point1.getX()][point1.getY()] != 0){
                                    //并且可以被消除
                                    if (findLink(board,point1,point2,null)){
                                        return new Point[]{point1,point2};
                                    }
                                }
                            }

                        }
                    }

                }
            }

        }

        return new Point[]{point1,point2};
    }

    /**
     * 返回一个布局存在的AnimalView
     * @return
     */
    public static int getExistAnimal(){
        //获取布局
        int[][] board = GameManager.getManager().getBoard();

        //产生随机数
        int random = 0;
        while (!LinkUtils.isCleared(board)){
            //产生一个随机数
            random = new Random().nextInt(LinkUtils.getMaxData(board))+1;
            Log.d(Constant.TAG,"测试消除"+random);

            //判断该布局中是否有
            for (int[] ints : board)
            {
                for (int j = 0; j < board[0].length; j++)
                {
                    if (ints[j] == random)
                    {
                        return random;
                    }
                }
            }
        }

        return random;
    }
}
