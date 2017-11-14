package com.example.vojtch.fruitmatcher;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.example.vojtch.fruitmatcher.Database.DBHandler;
import com.example.vojtch.fruitmatcher.Database.DatabaseEntity.PlayerInfo;

public class GameActivity extends Activity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sharedPrefEditor;
    private int PLAYER_RESULT_CODE = 886;

    ImageButton soundButton;
    TextView lbPlayer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);

        //set applicattion full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game);

        this.soundButton = (ImageButton) findViewById(R.id.soundSwitch);
        this.lbPlayer = (TextView) findViewById(R.id.lbPlayer);

        loadPreferences();



        DBHandler db = new DBHandler(this);
        int lvlCount = db.getLevelCount();

        if (lvlCount == 0){
            db.insertLevels();
        }
    }

    private void loadPreferences(){
        sharedPreferences = getSharedPreferences("fruitPrefenreces", Context.MODE_PRIVATE);

        ((FruitMatcherApp)this.getApplication()).setSoundOn(sharedPreferences.getBoolean("sound", true));
        ((FruitMatcherApp)this.getApplication()).setPlayerId(sharedPreferences.getInt("playerId", -1));

        if (!((FruitMatcherApp)this.getApplication()).isSoundOn()){
            soundButton.setImageResource(R.drawable.speaker_off);
        }

        PlayerInfo player = ((FruitMatcherApp)this.getApplication()).getPlayerInfo();
        if (player != null){
            lbPlayer.setText("Hráč " + player.getName());
        }
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
        PlayerInfo player = ((FruitMatcherApp)this.getApplication()).getPlayerInfo();
        if (player == null){
            Toast.makeText(this, "Není zvolen hráč", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent levelSelect = new Intent(this, LevelSelect.class);
        startActivity(levelSelect);
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

    public void onSoundClick(View v){
        if (((FruitMatcherApp)this.getApplication()).isSoundOn()){
            ((FruitMatcherApp)this.getApplication()).setSoundOn(false);
            this.soundButton.setImageResource(R.drawable.speaker_off);
        }
        else {
            ((FruitMatcherApp)this.getApplication()).setSoundOn(true);
            this.soundButton.setImageResource(R.drawable.speaker);
        }

        sharedPrefEditor = sharedPreferences.edit();
        sharedPrefEditor.putBoolean("sound", ((FruitMatcherApp)this.getApplication()).isSoundOn());
        sharedPrefEditor.apply();
    }

    public void onPlayersClick(View v){

        Intent playersIntent  = new Intent(this, PlayerSelect.class);
        startActivityForResult(playersIntent, this.PLAYER_RESULT_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == this.PLAYER_RESULT_CODE){
            sharedPrefEditor = sharedPreferences.edit();
            sharedPrefEditor.putInt("playerId", ((FruitMatcherApp)this.getApplication()).getPlayerId());
            sharedPrefEditor.apply();

            PlayerInfo player = ((FruitMatcherApp)this.getApplication()).getPlayerInfo();
            if (player != null){
                lbPlayer.setText("Hráč " + player.getName());
            }
        }
    }
}
