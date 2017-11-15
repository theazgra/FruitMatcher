package com.example.vojtch.fruitmatcher;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.vojtch.fruitmatcher.Database.DatabaseEntity.PlayerInfo;

import org.w3c.dom.Text;

import java.util.List;


public class PlayerAdapter extends ArrayAdapter<PlayerInfo> {

    private List<PlayerInfo> players;
    private Context context;
    private int layoutResourceId;

    private PlayerInfo activePlayer;

    public PlayerAdapter(Context context, int layoutResourceId, List<PlayerInfo> data, PlayerInfo activePlayer) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.players = data;
        this.activePlayer = activePlayer;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        PlayerHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new PlayerHolder();
            holder.lbName = (TextView) row.findViewById(R.id.lbPlayerName);
            holder.lbMaxLevel = (TextView) row.findViewById(R.id.lbMaxLbl);
            holder.lbLastPlayed = (TextView) row.findViewById(R.id.lbLastPlayed);
            holder.bg = (LinearLayout)row.findViewById(R.id.playerBG);
            holder.playerImg = (ImageView)row.findViewById(R.id.imgPlayer);

            row.setTag(holder);
        }
        else
        {
            holder = (PlayerHolder) row.getTag();
        }

        PlayerInfo playerInfo = players.get(position);
        holder.lbName.setText("Hřáč: " + playerInfo.getName());
        holder.lbMaxLevel.setText("Maximální dosažená úroveň: " + playerInfo.getMaxLevel());
        holder.lbLastPlayed.setText("Naposledy hráno: " + playerInfo.getPlayed());
        holder.playerImg.setImageBitmap(playerInfo.getPlayerImg());


        if (playerInfo.getId() == this.activePlayer.getId()){
            holder.bg.setBackgroundColor(Color.CYAN);
        }
        else {
            holder.bg.setBackgroundColor(Color.WHITE);
        }

        return row;
    }


    static class PlayerHolder
    {
        TextView lbName;
        TextView lbMaxLevel;
        TextView lbLastPlayed;
        LinearLayout bg;
        ImageView playerImg;
    }
}