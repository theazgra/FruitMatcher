package com.example.vojtch.fruitmatcher;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.vojtch.fruitmatcher.Database.DBHandler;
import com.example.vojtch.fruitmatcher.Database.DatabaseEntity.LevelInfo;

import java.util.ArrayList;
import java.util.List;

public class HighscoreAdapter extends ArrayAdapter<String> {

    private Context context;
    private int layoutResourceId;
    private List<String> levelNames;

    public HighscoreAdapter(Context context, int resource, List<String> data) {
        super(context, resource, data);
        this.context = context;
        this.layoutResourceId = resource;
        this.levelNames = data;
    }

    private String getBestPlayer(int levelId){
        DBHandler db = new DBHandler(this.context);
        return db.getBestPlayerForLevel(levelId);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        HighscoreHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new HighscoreHolder();
            holder.level = (TextView) row.findViewById(R.id.lbHighLevel);
            holder.player = (TextView) row.findViewById(R.id.lbHighPlayer);

            row.setTag(holder);
        }
        else
        {
            holder = (HighscoreHolder) row.getTag();
        }

        holder.level.setText(this.levelNames.get(position));
        holder.player.setText(getBestPlayer(position + 1));

        return row;
    }

    static class HighscoreHolder
    {
        TextView level;
        TextView player;

    }
}
