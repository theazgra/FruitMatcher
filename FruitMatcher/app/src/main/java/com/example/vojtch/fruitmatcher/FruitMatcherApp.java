package com.example.vojtch.fruitmatcher;

import android.app.Application;

import com.example.vojtch.fruitmatcher.Database.DBHandler;
import com.example.vojtch.fruitmatcher.Database.DatabaseEntity.PlayerInfo;


public class FruitMatcherApp extends Application {

    private boolean soundOn = true;
    private int playerId = -1;

    public PlayerInfo getPlayerInfo(){

        if (this.playerId == -1){
            return null;
        }

        DBHandler db = new DBHandler(this);
        return db.getPlayerInfo(this.playerId);
    }

    public boolean isSoundOn() {
        return soundOn;
    }

    public void setSoundOn(boolean soundOn) {
        this.soundOn = soundOn;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }
}
