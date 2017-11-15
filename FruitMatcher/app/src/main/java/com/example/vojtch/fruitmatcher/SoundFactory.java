package com.example.vojtch.fruitmatcher;


import android.content.Context;
import android.media.MediaPlayer;

public class SoundFactory {
    public static void playSound(Context context, int sound){

        MediaPlayer player = MediaPlayer.create(context, sound);
        player.start();

    }
}
