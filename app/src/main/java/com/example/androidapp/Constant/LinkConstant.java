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
//    int[] ANIMAL_RESOURCE = {
//            R.drawable.animal_0, R.drawable.animal_1,
//            R.drawable.animal_2, R.drawable.animal_3,
//            R.drawable.animal_4, R.drawable.animal_5,
//            R.drawable.animal_6, R.drawable.animal_7,
//            R.drawable.animal_8, R.drawable.animal_9,
//            R.drawable.animal_10, R.drawable.animal_11,
//            R.drawable.animal_12, R.drawable.animal_13,
//            R.drawable.animal_14, R.drawable.animal_15,
//            R.drawable.animal_16
//    };
    //木头图标
    int ANIMAL_WOOD = R.drawable.score;

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