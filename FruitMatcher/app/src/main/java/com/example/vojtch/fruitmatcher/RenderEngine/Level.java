package com.example.vojtch.fruitmatcher.RenderEngine;

import com.example.vojtch.fruitmatcher.R;

public class Level
{
    private final int levelId;
    //All informations are saved in arrays, index of arrya is equal to given level. First index {0} is default value

    private static int[] bgImage = {
            R.drawable.bg,
            R.drawable.bg
    };

    private static int[] appleCount = {
            0,
            2
    };

    private static int[] bananaCount = {
            0,
            2
    };

    private static int[] blueberryCount = {
            0,
            2
    };

    private static int[] lemonCount = {
            0,
            2
    };

    private static int[] orangeCount = {
            0,
            2
    };

    private static int[] strawberryCount = {
            0,
            2
    };

    private static String[] timeLimit = {
            "00:00:00",
            "00:05:00"
    };

    private static int[] tileCount = {
            100,
            150
    };

    public String getLimit(){
        return timeLimit[this.levelId];
    }


    public Level(int level) {
        this.levelId = level;
    }

    public int getTileCount() { return tileCount[this.levelId]; }

    public int getBgResId(){
        return bgImage[this.levelId];
    }

    public int getAppleCount(){
        return appleCount[this.levelId];
    }

    public int getBananaCount(){
        return bananaCount[this.levelId];
    }

    public int getBlueberryCount(){
        return blueberryCount[this.levelId];
    }

    public int getLemonCount(){
        return lemonCount[this.levelId];
    }

    public int getOrangeCount(){
        return orangeCount[this.levelId];
    }

    public int getStrawberryCount(){
        return strawberryCount[this.levelId];
    }


}
