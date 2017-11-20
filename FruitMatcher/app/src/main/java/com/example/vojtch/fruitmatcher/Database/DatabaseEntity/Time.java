package com.example.vojtch.fruitmatcher.Database.DatabaseEntity;

public class Time {
    private int hours;
    private int minutes;
    private int seconds;

    public Time(long miliseconds){

        this.hours = (int)(((miliseconds / 1000) / 60) / 60);
        miliseconds -= this.hours * 1000 * 60 * 60;
        this.minutes = (int)((miliseconds / 1000) / 60);
        miliseconds -= this.minutes * 1000 * 60;
        this.seconds = (int)(miliseconds / 1000);
    }

    public String toString(){
        return String.valueOf(this.hours) + ":" + String.valueOf(this.minutes) + ":" + String.valueOf(this.seconds);
    }

    public long toLong(){
        return (this.hours * 1000 * 60 * 60) + (this.minutes * 1000 * 60) + (this.seconds * 1000);
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }
}
