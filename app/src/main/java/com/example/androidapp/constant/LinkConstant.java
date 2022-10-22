package com.example.androidapp.constant;

import com.example.androidapp.R;

public interface LinkConstant
{
    //图标大小
    int IMAGE_SIZE = 70;

    //内间距
    int IMAGE_PADDING = 8;

    //图标
    int[] RESOURCE = {
            R.drawable.emoji1,
            R.drawable.emoji2,
            R.drawable.emoji3,
            R.drawable.emoji4,
            R.drawable.emoji5,
            R.drawable.emoji6,
            R.drawable.emoji7,
            R.drawable.emoji8,
            R.drawable.emoji9,
            R.drawable.emoji10,
            R.drawable.emoji11,
            R.drawable.emoji12,
            R.drawable.emoji13,
            R.drawable.emoji14,
            R.drawable.emoji15,
            R.drawable.emoji16,
            R.drawable.emoji17,
            R.drawable.emoji18,
            R.drawable.emoji19,
            R.drawable.emoji20,
            R.drawable.emoji21,
            R.drawable.emoji22,
            R.drawable.emoji23,
            R.drawable.emoji24,
            R.drawable.emoji25,
            R.drawable.emoji26,
            R.drawable.emoji27,
            R.drawable.emoji28,
            R.drawable.emoji29,
            R.drawable.emoji30,
    };


    //游戏时间
    int TIME = 90;

    //基础得分
    int BASE_SCORE = 500;


    int[][][] BOARD_EASY = {
            {{}},
            LinkBoard.board_easy_1,
            LinkBoard.board_easy_2,
            LinkBoard.board_easy_3,
            LinkBoard.board_easy_4,
            LinkBoard.board_easy_5,
            LinkBoard.board_easy_6,
            LinkBoard.board_easy_7,
            LinkBoard.board_easy_8,
            LinkBoard.board_easy_9,
            LinkBoard.board_easy_10,
            LinkBoard.board_easy_11,
            LinkBoard.board_easy_12,
            LinkBoard.board_easy_13,
    };
    int[][][] BOARD_NORMAL = {
            {{}},
            LinkBoard.board_normal_1,
            LinkBoard.board_normal_2,
            LinkBoard.board_normal_3,
            LinkBoard.board_normal_4,
            LinkBoard.board_normal_5,

    };
    int[][][] BOARD_HARD = {
            {{}},
            LinkBoard.board_hard_1,
            LinkBoard.board_hard_2,
            LinkBoard.board_hard_3,
            LinkBoard.board_hard_4,
            LinkBoard.board_hard_5,
    };
}