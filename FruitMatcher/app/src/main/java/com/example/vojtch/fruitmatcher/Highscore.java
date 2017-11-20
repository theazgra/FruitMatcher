package com.example.vojtch.fruitmatcher;


import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;

import com.example.vojtch.fruitmatcher.Database.DBHandler;
import com.example.vojtch.fruitmatcher.Database.DatabaseEntity.LevelInfo;

import java.util.ArrayList;
import java.util.List;

public class Highscore extends Activity {

    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_highscore);

        this.listView = (ListView)findViewById(R.id.highscoteListView);

        DBHandler db = new DBHandler(this);
        List<LevelInfo> levels = db.getLevels();
        ArrayList<String> levelNames = new ArrayList<String>();
        for (LevelInfo lvl : levels){
            levelNames.add("Level " + String.valueOf(lvl.getLevelId()));
        }

        this.listView.setAdapter(new HighscoreAdapter(this, R.layout.highscore_holder, levelNames));
    }
}
