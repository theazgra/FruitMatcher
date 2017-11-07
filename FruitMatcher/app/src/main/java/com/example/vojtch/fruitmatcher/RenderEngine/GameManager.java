package com.example.vojtch.fruitmatcher.RenderEngine;

import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;

import com.example.vojtch.fruitmatcher.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.FormatFlagsConversionMismatchException;
import java.util.HashMap;
import java.util.Random;


public class GameManager {
    private HashMap<Point, Tile> gameTiles;
    private HashMap<Point, Tile> effectTiles;
    private int level;

    private int bgId;
    private HashMap<String, Integer> quest;
    private Date timeLimit;

    private boolean handleTouch = true;
    private int selectingId = -1;
    private Direction lastMotionDirection = Direction.None;
    private Point startingPosition;

    private Context context;

    private static int[] GameTileIds = {
            R.drawable.apple,
            R.drawable.banana,
            R.drawable.blueberry,
            R.drawable.lemon,
            R.drawable.orange,
            R.drawable.strawberry
    };

    private int effectTile = R.drawable.selected;

    private static final int bgImage = R.drawable.bg;

    private static int GameTileCount = GameTileIds.length;


    private void deselectAllTiles(){
        for (Tile tile : this.gameTiles.values()){
            tile.setSelected(false);
        }
        this.effectTiles.clear();
    }


    public GameManager(int level, Context context){
        gameTiles = new HashMap<Point, Tile>();
        effectTiles = new HashMap<Point, Tile>();
        this.level = level;
        this.context = context;

        loadLevel(this.level);
    }

    public void onTouch(MotionEvent e){
        //generateGameTileGrid();
        int x = (int) e.getRawX();
        int y = (int) e.getRawY();

        switch (e.getAction()){
            case MotionEvent.ACTION_DOWN:
                if (handleTouch){ setSelectedTiles(x, y, true); }
                break;
            case MotionEvent.ACTION_MOVE:
                if (handleTouch){ setSelectedTiles(x, y, false); }
                break;
            case MotionEvent.ACTION_UP:
                if (handleTouch){ setSelectedTiles(x, y, false); }
                deselectAllTiles();
                break;

        }



    }

    private void setSelectedTiles(int x, int y, boolean firstTouch){

        if (x > Constants.GRID_X_OFFSET && x < Constants.GAME_GRID_WIDTH + Constants.GRID_X_OFFSET &&
            y > Constants.GRID_Y_OFFSET && y < Constants.GAME_GRID_HEIGHT + Constants.GRID_Y_OFFSET){

            x -= Constants.GRID_X_OFFSET;
            x /= 100;
            y -= Constants.GRID_Y_OFFSET;
            y /= 100;
            //Log.d("Touching at", "x: " + String.valueOf(x) + " ;y: " + String.valueOf(y));

            Point effectPosition = new Point(x * Constants.TILE_SIZE, y * Constants.TILE_SIZE);
            Tile tile = getTileAt(effectPosition);

            if (tile != null){

                if (firstTouch){
                    this.selectingId = tile.getDrawableId();
                }

                startingPosition = new Point(x, y);
                Direction currentDirection = determineTouchDirection(x, y);

                tile.setSelected(true);
                addTile(new Tile(effectPosition, this.effectTile, TileType.EffectTile));


                if (isInverseDirection(currentDirection)){

                }

                if (this.selectingId == tile.getDrawableId()) {
                    tile.setSelected(true);
                    addTile(new Tile(effectPosition, this.effectTile, TileType.EffectTile));
                }
                this.lastMotionDirection = currentDirection;

            }
        }
    }

    private void markTile(Tile tile, boolean selected){
        tile.setSelected(selected);
        if (selected)
            addTile(new Tile(tile.getPosition(), this.effectTile, TileType.EffectTile));
        else
            this.effectTiles.remove(tile.getPosition());
    }

    private Direction determineTouchDirection(int x, int y){

        if (startingPosition.x == x && startingPosition.y == y)
            return Direction.None;

        if (startingPosition.x == x){
            if (startingPosition.y > y)
                return Direction.Up;
            else
                return Direction.Down;
        }

        if (startingPosition.y == y){
            if (startingPosition.x > x)
                return Direction.Left;
            else
                return Direction.Right;
        }

        if (startingPosition.x > x && startingPosition.y > y)
            return Direction.UpLeft;
        if (startingPosition.x < x && startingPosition.y > y)
            return Direction.UpRight;
        if (startingPosition.x > x && startingPosition.y < y)
            return Direction.DownLeft;
        if (startingPosition.x < x && startingPosition.y < y)
            return Direction.DownRight;

        return Direction.None;
    }

    private boolean isInverseDirection(Direction currentDirection){

        if (this.lastMotionDirection == Direction.None)
            return false;

        if (this.lastMotionDirection == Direction.Up && currentDirection == Direction.Down)
            return true;
        if (this.lastMotionDirection == Direction.Down && currentDirection == Direction.Up)
            return true;
        if (this.lastMotionDirection == Direction.Left && currentDirection == Direction.Right)
            return true;
        if (this.lastMotionDirection == Direction.Right && currentDirection == Direction.Left)
            return true;
        if (this.lastMotionDirection == Direction.DownLeft && currentDirection == Direction.UpRight)
            return true;
        if (this.lastMotionDirection == Direction.DownRight && currentDirection == Direction.UpLeft)
            return true;
        if (this.lastMotionDirection == Direction.UpLeft && currentDirection == Direction.DownRight)
            return true;
        if (this.lastMotionDirection == Direction.UpRight && currentDirection == Direction.DownLeft)
            return true;



        return false;
    }

    private void loadLevel(int level){
        Level lvl = new Level(level);

        this.bgId = lvl.getBgResId();

        this.quest = new HashMap<String, Integer>();
        this.quest.put("apple", lvl.getAppleCount());
        this.quest.put("banana", lvl.getBananaCount());
        this.quest.put("blueberry", lvl.getBlueberryCount());
        this.quest.put("lemon", lvl.getLemonCount());
        this.quest.put("orange", lvl.getOrangeCount());
        this.quest.put("strawberry", lvl.getStrawberryCount());

        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

        this.timeLimit = null;
        try {
            this.timeLimit = format.parse(lvl.getLimit());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        generateGameTileGrid();
    }

    private int randomGameTileId(){
        Random randomGenerator = new Random();
        int randomIndex = randomGenerator.nextInt(GameTileCount);
        return GameTileIds[randomIndex];
    }

    public void generateGameTileGrid(){
        gameTiles.clear();
        for (int x = 0; x < Constants.GAME_SQUARE_SIZE; x++) {
            for (int y = 0; y < Constants.GAME_SQUARE_SIZE; y++) {
                Point position = new Point(x * Constants.TILE_SIZE, y * Constants.TILE_SIZE);
                addTile(new Tile(position, randomGameTileId(), TileType.GameTile));
            }
        }
    }

    public void addTile(Tile tile) {
        switch (tile.getTileType()){
            case GameTile:
                gameTiles.put(tile.getPosition(), tile);
                break;
            case EffectTile:
                effectTiles.put(tile.getPosition(), tile);
                break;
        }
    }


    public Collection<Tile> getGameTiles() {
        return gameTiles.values();
    }


    /**
     * Get tile at given position.
     * @param position position of the finger on the touch screen.
     * @return Tile or null if there is no game tile.
     */
    public Tile getTileAt(Point position){
        if (this.gameTiles.containsKey(position)){
            return this.gameTiles.get(position);
        }
        return null;
    }

    public Collection<Tile> getEffectTiles() {
        return effectTiles.values();
    }


    public int getBgId() {
        return bgId;
    }

    public Date getTimeLimit() {
        return timeLimit;
    }
}
