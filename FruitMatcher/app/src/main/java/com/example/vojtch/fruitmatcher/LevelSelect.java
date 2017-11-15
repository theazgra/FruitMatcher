package com.example.vojtch.fruitmatcher;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.example.vojtch.fruitmatcher.Database.DBHandler;
import com.example.vojtch.fruitmatcher.Database.DatabaseEntity.LevelInfo;
import com.example.vojtch.fruitmatcher.Database.DatabaseEntity.PlayerInfo;
import com.example.vojtch.fruitmatcher.GameEngine.CanvasActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LevelSelect extends Activity {

    private ExpandableListView expListView;
    private ExpandableLevelSelectAdapter listAdapter;
    private int lastExpandedGroupPosition = -1;
    private List<String> levelHeaders;
    private HashMap<String, LevelInfo> listDetail;
    private PlayerInfo playerInfo;
    private int GAME_ACTIVITY_CODE = 357;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);

        //set applicattion full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_level_select);

        expListView = (ExpandableListView)findViewById(R.id.expListView);

        loadList();

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long id) {


                LevelInfo lvl = listDetail.get(levelHeaders.get(groupPosition));
                startLevel(lvl);

                return false;
            }
        });

        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                if (lastExpandedGroupPosition != -1 && groupPosition != lastExpandedGroupPosition){
                    expListView.collapseGroup(lastExpandedGroupPosition);
                }
                lastExpandedGroupPosition = groupPosition;

                LevelInfo lvl = listDetail.get(levelHeaders.get(groupPosition));
                if (lvl.getLevelId() > playerInfo.getMaxLevel() + 1){


                    if (((FruitMatcherApp)getApplication()).isSoundOn()){
                        SoundFactory.playSound(getApplication(), R.raw.not_prepared);
                    }

                    expListView.collapseGroup(groupPosition);
                    lastExpandedGroupPosition = -1;
                }
            }
        });
    }

    private void loadList(){
        this.playerInfo = ((FruitMatcherApp)this.getApplication()).getPlayerInfo();
        levelHeaders = new ArrayList<String>();
        listDetail = new HashMap<String, LevelInfo>();

        DBHandler db = new DBHandler(this);
        List<LevelInfo> levels = db.getLevels();

        for (LevelInfo lvl : levels){
            String lvlName = lvl.getLevelId() + ". Úroveň";
            levelHeaders.add(lvlName);
            listDetail.put(lvlName, lvl);
        }

        listAdapter = new ExpandableLevelSelectAdapter(this, this.levelHeaders, this.listDetail, this.playerInfo);

        expListView.setAdapter(listAdapter);
    }

    private void startLevel(LevelInfo level){
        if (level.getLevelId() > playerInfo.getMaxLevel() + 1){
            Toast.makeText(this, "Nepřístupné", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent gameActivity = new Intent(this, CanvasActivity.class);
        gameActivity.putExtra("levelInfo", level);

        startActivityForResult(gameActivity, this.GAME_ACTIVITY_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == this.GAME_ACTIVITY_CODE && resultCode == RESULT_OK){
            loadList();
        }
    }
}
