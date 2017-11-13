package com.example.vojtch.fruitmatcher;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.example.vojtch.fruitmatcher.Database.DBHandler;
import com.example.vojtch.fruitmatcher.Database.DatabaseEntity.LevelInfo;
import com.example.vojtch.fruitmatcher.RenderEngine.CanvasActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LevelSelect extends Activity {

    private ExpandableListView expListView;
    private ExpandableLevelSelectAdapter listAdapter;
    private int lastExpandedGroupPosition = -1;
    private List<String> levelHeaders;
    private HashMap<String, LevelInfo> listDetail;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);

        //set applicattion full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_level_select);

        expListView = (ExpandableListView)findViewById(R.id.expListView);

        levelHeaders = new ArrayList<String>();
        listDetail = new HashMap<String, LevelInfo>();

        DBHandler db = new DBHandler(this);
        List<LevelInfo> levels = db.getLevels();

        for (LevelInfo lvl : levels){
            String lvlName = lvl.getLevelId() + ". Úroveň";
            levelHeaders.add(lvlName);
            listDetail.put(lvlName, lvl);
        }

        listAdapter = new ExpandableLevelSelectAdapter(this, this.levelHeaders, this.listDetail);

        expListView.setAdapter(listAdapter);

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long id) {
                    //Log.d("group pos", String.valueOf(groupPosition));
                    //Log.d("child pos", String.valueOf(childPosition));

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
            }
        });
    }

    private void startLevel(LevelInfo level){
        Intent gameActivity = new Intent(this, CanvasActivity.class);
        gameActivity.putExtra("levelInfo", level);
        startActivity(gameActivity);
    }
}
