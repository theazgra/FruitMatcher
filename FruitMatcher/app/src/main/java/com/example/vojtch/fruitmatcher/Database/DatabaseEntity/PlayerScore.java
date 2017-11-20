package com.example.vojtch.fruitmatcher.Database.DatabaseEntity;

import java.util.Date;



public class PlayerScore {

    private int     playerId;
    private int     levelId;
    private Time    time;

    public PlayerScore(int playerId, int levelId, Time time){
        this.playerId = playerId;
        this.levelId = levelId;
        this.time = time;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public int getLevelId() {
        return levelId;
    }

    public void setLevelId(int levelId) {
        this.levelId = levelId;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }
}
