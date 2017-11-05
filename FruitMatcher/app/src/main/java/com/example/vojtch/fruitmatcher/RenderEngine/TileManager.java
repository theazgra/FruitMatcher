package com.example.vojtch.fruitmatcher.RenderEngine;

import android.graphics.Point;

import com.example.vojtch.fruitmatcher.R;

import java.util.ArrayList;
import java.util.Random;


public class TileManager {
    private ArrayList<Tile> gameTiles;
    private ArrayList<Tile> decorationTiles;
    private ArrayList<Tile> effectTiles;

    private static int[] GameTileIds = {
            R.drawable.tile_1,
            R.drawable.tile_2,
            R.drawable.tile_3,
            R.drawable.tile_4,
            R.drawable.tile_5,
            R.drawable.tile_6,
            R.drawable.tile_7,
            R.drawable.tile_8
    };

    private static int[] DecorationTileIds = {
            R.drawable.flag_cz
    };

    private static int GameTileCount = GameTileIds.length;



    public TileManager(){
        gameTiles = new ArrayList<Tile>();
        decorationTiles = new ArrayList<Tile>();
        effectTiles = new ArrayList<Tile>();
    }

    private int randomGameTileId(){
        Random randomGenerator = new Random();
        int randomIndex = randomGenerator.nextInt(GameTileCount);
        return GameTileIds[randomIndex];
    }

    public void generateGameTileGrid(int gridSize){
        gameTiles.clear();

        //switch x and y?
        for (int x = 0; x < gridSize; x++) {
            for (int y = 0; y < gridSize; y++) {
                Point position = new Point(x * Constants.TILE_SIZE, y * Constants.TILE_SIZE);
                addTile(new Tile(position, randomGameTileId(), TileType.GameTile));
            }
        }
    }

    //What to pass that will define where and how much decoration tiles should be generated.
    //I think game tile grid size will be fixed. So decoration grid can be 'hardcoded'
    public void generateDecorationTiles(int decaratioTileId){

    }

    public void addTile(Tile tile) {
        switch (tile.getTileType()){
            case GameTile:
                gameTiles.add(tile);
                break;
            case DecorationTile:
                decorationTiles.add(tile);
                break;
            case EffectTile:
                effectTiles.add(tile);
                break;
        }
    }


    public ArrayList<Tile> getGameTiles() {
        return gameTiles;
    }

    public ArrayList<Tile> getEffectTiles() {
        return effectTiles;
    }

    public ArrayList<Tile> getDecorationTiles() {
        return decorationTiles;
    }
}
