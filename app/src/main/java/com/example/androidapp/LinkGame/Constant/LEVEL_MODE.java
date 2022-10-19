package com.example.androidapp.LinkGame.Constant;

public enum LEVEL_MODE {
    SIMPLE_MODE(1),
    NORMAL_MODE(2),
    HARD_MODE(3);
    private final int value;
    LEVEL_MODE(int value) {
        this.value=value;
    }
    public int getValue()
    {
        return value;
    }
}
