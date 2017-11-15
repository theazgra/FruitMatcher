package com.example.vojtch.fruitmatcher;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.vojtch.fruitmatcher.Database.DBHandler;
import com.example.vojtch.fruitmatcher.Database.DatabaseEntity.PlayerInfo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class PlayerSelect extends Activity {

    private ListView listView;
    private int NEW_PLAYER_CODE = 512;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_players);

        this.listView = (ListView)findViewById(R.id.playerList);

        setListViewData();

        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                updateActivePlayerLastPlayed();

                PlayerInfo player = (PlayerInfo)adapterView.getItemAtPosition(position);
                ((FruitMatcherApp)getApplication()).setPlayerId(player.getId());
                Toast.makeText(getApplicationContext(), "Zvolen hráč " + player.getName(), Toast.LENGTH_SHORT).show();

                setResult(RESULT_OK);
                finish();
            }
        });
    }

    private void setListViewData(){
        DBHandler db = new DBHandler(this);
        List<PlayerInfo> players = db.getPlayers();

        PlayerAdapter adapter = new PlayerAdapter(
                this,
                R.layout.player_holder,
                players,
                ((FruitMatcherApp)this.getApplication()).getPlayerInfo(),
                getResources());

        this.listView.setAdapter(adapter);
    }

    private void updateActivePlayerLastPlayed(){
        //update last player last played
        PlayerInfo activePlayer = ((FruitMatcherApp)this.getApplication()).getPlayerInfo();
        if (activePlayer != null){

            SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy");
            String lastPlayed = dateFormatter.format(Calendar.getInstance().getTime());

            activePlayer.setPlayed(lastPlayed);
            DBHandler db = new DBHandler(this);
            db.updatePlayerInfo(activePlayer);
        }
    }

    public void onAddPlayerClick(View v){
        Intent newPlayer = new Intent(this, AddPlayer.class);
        startActivityForResult(newPlayer, this.NEW_PLAYER_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == this.NEW_PLAYER_CODE && resultCode == RESULT_OK){
            setListViewData();
        }
    }
}
