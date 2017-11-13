package com.example.vojtch.fruitmatcher.Database.DatabaseEntity;


import android.os.Parcel;
import android.os.Parcelable;

import com.example.vojtch.fruitmatcher.RenderEngine.FruitType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LevelInfo implements Parcelable {

    private int levelId;
    private String timeLimit;
    private int tileCount;
    private int appleCount;
    private int bananaCount;
    private int blueberryCount;
    private int lemonCount;
    private int orangeCount;
    private int strawberryCount;

    private Date timeLimitDateFormat;

    public LevelInfo(int level, String limit, int tileCount, int apple, int banana, int blueberry, int lemon, int orange, int strawberry){
        this.levelId = level;
        this.tileCount = tileCount;
        this.appleCount = apple;
        this.bananaCount = banana;
        this.blueberryCount = blueberry;
        this.lemonCount = lemon;
        this.orangeCount = orange;
        this.strawberryCount = strawberry;
        this.timeLimit = limit;

        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

        this.timeLimitDateFormat = null;
        try {
            this.timeLimitDateFormat = format.parse(this.timeLimit);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public Date getTimeLimitDateFormat(){
        return this.timeLimitDateFormat;
    }

    public void setAppleCount(int count){
        this.appleCount = count;
    }

    public void setBananaCount(int count){
        this.bananaCount = count;
    }

    public void setBlueberryCount(int count){
        this.blueberryCount = count;
    }

    public void setLemonCount(int count){
        this.lemonCount = count;
    }

    public void setOrangeCount(int count){
        this.orangeCount = count;
    }

    public void setStrawberryCount(int count){
        this.strawberryCount = count;
    }

    public int getFruitCount(FruitType fruitType){
        switch (fruitType) {
            case Apple:
                return this.appleCount;
            case Banana:
                return this.bananaCount;
            case Bluberry:
                return this.blueberryCount;
            case Lemon:
                return this.lemonCount;
            case Orange:
                return this.orangeCount;
            case Strawberry:
                return this.strawberryCount;
        }
        return 0;
    }

    public void setFruitCount(FruitType fruitType, int count){
        if (count < 0){
            count = 0;
        }

        switch (fruitType) {
            case Apple:
                this.appleCount = count;
                break;
            case Banana:
                this.bananaCount = count;
                break;
            case Bluberry:
                this.blueberryCount = count;
                break;
            case Lemon:
                this.lemonCount = count;
                break;
            case Orange:
                this.orangeCount = count;
                break;
            case Strawberry:
                this.strawberryCount = count;
                break;
        }
    }

    public int getLevelId() {
        return levelId;
    }

    public String getTimeLimit() {
        return timeLimit;
    }

    public int getTileCount() {
        return tileCount;
    }

    public int getAppleCount() {
        return appleCount;
    }

    public int getBananaCount() {
        return bananaCount;
    }

    public int getBlueberryCount() {
        return blueberryCount;
    }

    public int getLemonCount() {
        return lemonCount;
    }

    public int getOrangeCount() {
        return orangeCount;
    }

    public int getStrawberryCount() {
        return strawberryCount;
    }

    protected LevelInfo(Parcel in) {
        levelId = in.readInt();
        timeLimit = in.readString();
        tileCount = in.readInt();
        appleCount = in.readInt();
        bananaCount = in.readInt();
        blueberryCount = in.readInt();
        lemonCount = in.readInt();
        orangeCount = in.readInt();
        strawberryCount = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(levelId);
        dest.writeString(timeLimit);
        dest.writeInt(tileCount);
        dest.writeInt(appleCount);
        dest.writeInt(bananaCount);
        dest.writeInt(blueberryCount);
        dest.writeInt(lemonCount);
        dest.writeInt(orangeCount);
        dest.writeInt(strawberryCount);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<LevelInfo> CREATOR = new Parcelable.Creator<LevelInfo>() {
        @Override
        public LevelInfo createFromParcel(Parcel in) {
            return new LevelInfo(in);
        }

        @Override
        public LevelInfo[] newArray(int size) {
            return new LevelInfo[size];
        }
    };
}