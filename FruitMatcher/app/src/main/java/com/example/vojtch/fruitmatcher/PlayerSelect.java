package com.example.vojtch.fruitmatcher;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Activity;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.vojtch.fruitmatcher.Database.DBHandler;
import com.example.vojtch.fruitmatcher.Database.DatabaseEntity.PlayerInfo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class PlayerSelect extends Activity {

    private ListView listView;

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
                ((FruitMatcherApp)this.getApplication()).getPlayerInfo());

        this.listView.setAdapter(adapter);
    }

    private void updateActivePlayerLastPlayed(){
        //update last player last played
        PlayerInfo activePlayer = ((FruitMatcherApp)this.getApplication()).getPlayerInfo();
        if (activePlayer != null){
            //String lastPlayed = Calendar.get


            SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy");
            String lastPlayed = dateFormatter.format(Calendar.getInstance().getTime());

            activePlayer.setPlayed(lastPlayed);
            DBHandler db = new DBHandler(this);
            db.updatePlayerInfo(activePlayer);
        }
    }

    private void addNewPlayer(String newPlayerName){
        PlayerInfo newPlayer = new PlayerInfo(newPlayerName);
        DBHandler db = new DBHandler(this);
        db.addPlayerInfo(newPlayer);

        setListViewData();
    }

    public void onAddPlayerClick(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Nový hráč");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("Přidat", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                addNewPlayer(input.getText().toString());

            }
        });
        builder.setNegativeButton("Storno", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}
