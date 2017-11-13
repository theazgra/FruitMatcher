package com.example.vojtch.fruitmatcher;


import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.vojtch.fruitmatcher.Database.DBHandler;
import com.example.vojtch.fruitmatcher.Database.DatabaseEntity.LevelInfo;

import java.util.HashMap;
import java.util.List;

public class ExpandableLevelSelectAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> levelHeaders;
    private HashMap<String, LevelInfo> listDetail;

    public ExpandableLevelSelectAdapter(Context context, List<String> headers, HashMap<String, LevelInfo> levelDetails){
        this.context = context;
        listDetail = new HashMap<String, LevelInfo>();

        this.levelHeaders = headers;
        this.listDetail = levelDetails;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.listDetail.get(this.levelHeaders.get(groupPosition));
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return  childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        LevelInfo lvl = (LevelInfo) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.level_detail, null);
        }

        TextView timeLimit = (TextView) convertView.findViewById(R.id.lbLevelTimeLimit);
        TextView tileLimit = (TextView) convertView.findViewById(R.id.lbLevelTileLimit);
        TextView apple = (TextView) convertView.findViewById(R.id.lbApple);
        TextView banana = (TextView) convertView.findViewById(R.id.lbBanana);
        TextView blueberry = (TextView) convertView.findViewById(R.id.lbBlueberry);
        TextView lemon = (TextView) convertView.findViewById(R.id.lbLemon);
        TextView orange = (TextView) convertView.findViewById(R.id.lbOrange);
        TextView strawberry = (TextView) convertView.findViewById(R.id.lbStrawberry);

        timeLimit.setText("Časový limit: " + String.valueOf(lvl.getTimeLimit()));
        tileLimit.setText("Počet políček: " + String.valueOf(lvl.getTileCount()));
        apple.setText(String.valueOf(lvl.getAppleCount()));
        banana.setText(String.valueOf(lvl.getBananaCount()));
        blueberry.setText(String.valueOf(lvl.getBlueberryCount()));
        lemon.setText(String.valueOf(lvl.getLemonCount()));
        orange.setText(String.valueOf(lvl.getOrangeCount()));
        strawberry.setText(String.valueOf(lvl.getStrawberryCount()));

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.levelHeaders.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.levelHeaders.size();
    }


    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.level_header, null);
        }

        TextView header = (TextView) convertView.findViewById(R.id.lbLevelHeader);
        header.setTypeface(null, Typeface.BOLD);
        header.setText(headerTitle);

        return convertView;
    }
    @Override
    public boolean hasStableIds() {
        return false;
    }





    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
