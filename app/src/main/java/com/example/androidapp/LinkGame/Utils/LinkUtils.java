package com.example.androidapp.LinkGame.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.example.androidapp.LinkGame.LinkModel.Kernel.UNBLOCKED;

import android.content.Context;
import android.util.Log;

import com.example.androidapp.Constant.Constant;
import com.example.androidapp.Constant.Enum.LevelMode;
import com.example.androidapp.Constant.LinkConstant;
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
     * 根据关卡难度生成连连看
     * @param level
     *
     */
    public static int[][] generateBoard(int level)
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
                sourceNum=col-2;
                break;
            case 3:
                row=12;
                col=8;
                sourceNum=col-2;
                break;
        }
        int [][]LinkBoard=new int[row][col];
        for(int i=0;i<LinkBoard.length;i++)
            LinkBoard[i][0]=LinkBoard[i][LinkBoard[0].length-1]=UNBLOCKED;
        for(int j=0;j<LinkBoard[0].length;j++)
            LinkBoard[0][j]=LinkBoard[LinkBoard.length-1][j]=UNBLOCKED;
        int total=(LinkBoard.length-2)*(LinkBoard[0].length-2);
        ArrayList<Integer> dataList = new ArrayList<>();
        for(int i=0;i<total/sourceNum;i++)
        {
            for(int j=0;j<sourceNum;j++)
                dataList.add(j);
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
    public static int[][] generateBoard_statically(int level)
    {
        return null;
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
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                data[index++] = matrix[i][j];
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
        if (temp.length >= 0) System.arraycopy(temp, 0, nums, start, temp.length);
    }

    public static Point getRealAnimalPoint(Point point, Context context){
        GameManager manager = GameManager.getManager();
        return new Point(
                manager.getPadding_hor() + PxUtil.dpToPx(manager.getSize(),context) / 2 + point.getY()  * PxUtil.dpToPx(manager.getSize(),context),
                manager.getPadding_ver() + PxUtil.dpToPx(manager.getSize(),context) / 2 + point.getX()  * PxUtil.dpToPx(manager.getSize(),context)
        );
    }

    public static int[][] loadLevelWithIdAndMode(int level_id, char level_mode){

        //1.先判断是什么类型的关卡
        int [][][] BOARD;
        //if (level_mode == LevelMode.LEVEL_MODE_EASY.getValue()){
            BOARD = LinkConstant.BOARD_EASY;
        //}//else if (level_mode == LevelMode.LEVEL_MODE_NORMAL.getValue()){
//            BOARD = LinkConstant.BOARD_NORMAL;
//        }else {
//            BOARD = LinkConstant.BOARD_HARD;
//        }

//        //2.获取需要加载的资源数量
//        int resourceID = new Random().nextInt(BOARD.length);
//
//        //3.获取随机产生的模板
//        int[][] board = BOARD[resourceID];
//        Log.d(Constant.TAG,"加载的board"+resourceID);

        //4.拷贝模板
        int[][] board = BOARD[level_id];
        Log.d("1",String.valueOf(level_id));
        int row = board.length;
        int col = board[0].length;
        int[][] clone = new int[row][col];
        for (int i = 0; i < row; i++) {
            System.arraycopy(board[i], 0, clone[i], 0, col);
        }

        return clone;
    }
}
