package com.example.vojtch.fruitmatcher.Database.DatabaseEntity;

public class PlayerInfo {

    private int id;
    private String name;
    private int maxLevel;
    private String played;

    public PlayerInfo(String name){
        this.name = name;
    }
    public PlayerInfo(int id, String name, int maxLevel, String played){
        this.id = id;
        this.name = name;
        this.maxLevel = maxLevel;
        this.played = played;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }

    public String getPlayed() {
        return played;
    }

    public void setPlayed(String played) {
        this.played = played;
    }

    public int getId() {
        return id;
    }
}
