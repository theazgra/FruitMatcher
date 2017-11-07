package com.example.vojtch.fruitmatcher.RenderEngine;

import com.example.vojtch.fruitmatcher.R;

public class Level
{
    private final int levelId;
    //All informations are saved in arrays, index of arrya is equal to given level. First index {0} is default value

    public static int[] bgImage = {
            R.drawable.bg,
            R.drawable.bg
    };

    public static int[] appleCount = {
            0,
            2
    };

    public static int[] bananaCount = {
            0,
            2
    };

    public static int[] blueberryCount = {
            0,
            2
    };

    public static int[] lemonCount = {
            0,
            2
    };

    public static int[] orangeCount = {
            0,
            2
    };

    public static int[] strawberryCount = {
            0,
            2
    };

    public static String[] timeLimit = {
            "00:00:00",
            "00:05:00"
    };

    public String getLimit(){
        return this.timeLimit[this.levelId];
    }


    public Level(int level) {
        this.levelId = level;
    }

    public int getBgResId(){
        return this.bgImage[this.levelId];
    }

    public int getAppleCount(){
        return this.appleCount[this.levelId];
    }

    public int getBananaCount(){
        return this.bananaCount[this.levelId];
    }

    public int getBlueberryCount(){
        return this.blueberryCount[this.levelId];
    }

    public int getLemonCount(){
        return this.lemonCount[this.levelId];
    }

    public int getOrangeCount(){
        return this.orangeCount[this.levelId];
    }

    public int getStrawberryCount(){
        return this.strawberryCount[this.levelId];
    }


}
