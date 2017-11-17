package com.example.vojtch.fruitmatcher;

import android.content.Context;
import android.media.MediaPlayer;

public class SoundFactory {

    private static MediaPlayer mediaPlayer = null;

    public static void playSound(Context context, int sound){

        if (mediaPlayer == null){
            mediaPlayer = MediaPlayer.create(context, sound);

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mediaPlayer = null;
                }
            });

            mediaPlayer.start();
        }
    }
}
