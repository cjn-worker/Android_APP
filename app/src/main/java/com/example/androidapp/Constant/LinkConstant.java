package com.example.androidapp.Constant;

import com.example.androidapp.R;

public interface LinkConstant
{
    /**
     * 连连看图标大小
     */
    int ANIMAL_SIZE = 70;

    /**
     * 连连看AnimalView的内间距
     */
    int ANIMAL_PADDING = 8;

    /**
     * 连连看图标
     */
    int[] RESOURCE = {
            R.drawable.emoji1,
            R.drawable.emoji2,
            R.drawable.emoji3,
            R.drawable.emoji4,
            R.drawable.emoji5,
            R.drawable.emoji6,
            R.drawable.emoji7,

    };


    /**
     * 连连看图标背景
     */
//    int ANIMAL_BG = R.drawable.animal_bg1;
//    int ANIMAL_SELECT_BG = R.drawable.animal_select_bg1;

    /**
     * 连连看默认的时间
     */
    int TIME = 90;

    /**
     * 基础分数
     */
    int BASE_SCORE = 500;

    /**
     * 连连看测试模板
     * 0：空白
     * 1~∞：神奇宝贝图片
     */
    int[][] board_test1 = {
            {0, 0, 0, 0, 0, 0},
            {0, 1, 2, 3, 4, 0},
            {0, 2, 4, 1, 4, 0},
            {0, 3, 1, 3, 2, 0},
            {0, 1, 4, 2, 3, 0},
            {0, 0, 0, 0, 0, 0}
    };

    int[][] board_test2 = {
            {0, 0, 0, 0, 0, 0},
            {0, 1, 0, 3, 4, 0},
            {0, 2, 0, 1, 4, 0},
            {0, 3, 1, 3, 2, 0},
            {0, 1, 5, 2, 3, 0},
            {0, 0, 0, 0, 0, 0}
    };

    int[][] board_test3 = {
            {0, 0, 0, 0, 0, 0},
            {0, 0, 0, 3, 4, 0},
            {0, 0, 4, 1, 4, 0},
            {0, 3, 1, 3, 2, 0},
            {0, 0, 4, 2, 3, 0},
            {0, 0, 0, 0, 0, 0}
    };

}