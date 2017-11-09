package com.example.vojtch.fruitmatcher;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;


import com.example.vojtch.fruitmatcher.Database.DBHandler;
import com.example.vojtch.fruitmatcher.Database.DatabaseEntity.LevelInfo;
import com.example.vojtch.fruitmatcher.Database.DatabaseEntity.PlayerInfo;
import com.example.vojtch.fruitmatcher.RenderEngine.CanvasActivity;

import java.util.ArrayList;

public class GameActivity extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);

        //set applicattion full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);



        setContentView(R.layout.activity_game);

        //RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.relativeLayout);

        //RelativeLayout.LayoutParams glParams = new RelativeLayout.LayoutParams(
        //        RelativeLayout.LayoutParams.MATCH_PARENT,
        //        RelativeLayout.LayoutParams.MATCH_PARENT
        //);


        //Intent mainGameActivity = new Intent(this, CanvasActivity.class);
        //startActivity(mainGameActivity);
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void onPlayClick(View v){
        Intent mainGameActivity = new Intent(this, CanvasActivity.class);
        startActivity(mainGameActivity);
    }

    public void onHighScoreClick(View v){
        Intent highScoreIntent = new Intent(this, Highscore.class);
        startActivity(highScoreIntent);
    }

    public void onExitClick(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Opravdu chcete ukončit aplikaci?");

        builder.setPositiveButton("Ano", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });

        builder.setNegativeButton("Ne", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });


        builder.show();
    }
}
