package com.example.vojtch.fruitmatcher.RenderEngine;

import android.content.Context;
import android.graphics.Point;
import android.media.ExifInterface;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import com.example.vojtch.fruitmatcher.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.Stack;
import java.util.StringTokenizer;


public class GameManager {
    private HashMap<Point, Tile> gameTiles;
    private HashMap<Point, Tile> effectTiles;
    private Stack<Tile> selectedTiles;

    private int level;

    private int bgId;
    private HashMap<String, Integer> quest;
    private Date timeLimit;

    private Context context;

    private boolean handleTouch = true;
    private int selectingId = -1;
    private int possibleComboSize = 0;

    private Direction lastMotionDirection = Direction.None;
    private Point startingPosition;

    private static int[] GameTileIds = {
            R.drawable.apple,
            R.drawable.banana,
            R.drawable.blueberry,
            R.drawable.lemon,
            R.drawable.orange,
            R.drawable.strawberry
    };

    private int effectTile = R.drawable.selected;


    public GameManager(int level, Context context){
        this.gameTiles = new HashMap<Point, Tile>();
        this.effectTiles = new HashMap<Point, Tile>();
        this.selectedTiles = new Stack<Tile>();
        this.level = level;
        this.context = context;

        loadLevel(this.level);
    }

    private void deselectAllTiles(){
        for (Tile tile : this.gameTiles.values()){
            tile.setSelected(false);
        }
        this.effectTiles.clear();
        this.selectedTiles.clear();
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
                checkMatch();
                deselectAllTiles();

                if (!combinationExist())
                    //throw new Exception("No combination. Implement handler for this.");


                break;
        }
    }

    private void checkMatch(){
        if (this.selectedTiles.size() >= 3){
            int combo = this.selectedTiles.size();

            while (!this.selectedTiles.isEmpty()){
                getTileAt(this.selectedTiles.pop().getPosition()).setVisible(false);
            }
            
            Toast.makeText(context, "COMBO: " + String.valueOf(combo), Toast.LENGTH_SHORT).show();
        }
    }
    
    private boolean combinationExist(){

        for (int x = 0; x < Constants.GAME_SQUARE_SIZE; x++) {
            for (int y = 0; y < Constants.GAME_SQUARE_SIZE; y++) {

                this.possibleComboSize = 0;
                Point location = new Point(x * Constants.TILE_SIZE, y * Constants.TILE_SIZE);
                Tile tile = getTileAt(location);
                if (tile == null)
                    continue;

                if (checkCombinationFromTile(tile, null)){
                    return true;
                }
            }
        }

        return false;
    }

    private boolean checkCombinationFromTile(Tile tile, Tile ignoreTile){

        this.possibleComboSize++;
        if (this.possibleComboSize >= 3)
            return true;

        Tile upTile = getTileAt(new Point(tile.getPosition().x, tile.getPosition().y - Constants.TILE_SIZE));
        if (upTile != null && upTile != ignoreTile && upTile.getDrawableId() == tile.getDrawableId()){
            if (checkCombinationFromTile(upTile, tile))
                return true;
        }


        Tile downTile = getTileAt(new Point(tile.getPosition().x, tile.getPosition().y + Constants.TILE_SIZE));
        if (downTile != null && downTile != ignoreTile && downTile.getDrawableId() == tile.getDrawableId()){
            if (checkCombinationFromTile(downTile, tile))
                return true;
        }

        Tile leftTile = getTileAt(new Point(tile.getPosition().x - Constants.TILE_SIZE, tile.getPosition().y));
        if (leftTile != null && leftTile != ignoreTile && leftTile.getDrawableId() == tile.getDrawableId()){
            if (checkCombinationFromTile(leftTile, tile))
                return true;
        }

        Tile rightTile = getTileAt(new Point(tile.getPosition().x + Constants.TILE_SIZE, tile.getPosition().y));
        if (rightTile != null && rightTile != ignoreTile && rightTile.getDrawableId() == tile.getDrawableId()){
            if (checkCombinationFromTile(rightTile, tile))
                return true;

        }

        Tile upLeftTile = getTileAt(new Point(tile.getPosition().x - Constants.TILE_SIZE, tile.getPosition().y - Constants.TILE_SIZE));
        if (upLeftTile != null && upLeftTile != ignoreTile && upLeftTile.getDrawableId() == tile.getDrawableId()){
            if (checkCombinationFromTile(upLeftTile, tile))
                return true;
        }

        Tile upRightTile = getTileAt(new Point(tile.getPosition().x + Constants.TILE_SIZE, tile.getPosition().y - Constants.TILE_SIZE));
        if (upRightTile != null && upRightTile != ignoreTile && upRightTile.getDrawableId() == tile.getDrawableId()){
            if (checkCombinationFromTile(upRightTile, tile))
                return true;
        }

        Tile downLeftTile = getTileAt(new Point(tile.getPosition().x - Constants.TILE_SIZE, tile.getPosition().y + Constants.TILE_SIZE));
        if (downLeftTile != null && downLeftTile != ignoreTile && downLeftTile.getDrawableId() == tile.getDrawableId()){
            if (checkCombinationFromTile(downLeftTile, tile))
                return true;
        }

        Tile downRightTile = getTileAt(new Point(tile.getPosition().x + Constants.TILE_SIZE, tile.getPosition().y + Constants.TILE_SIZE));
        if (downRightTile != null && downRightTile != ignoreTile && downRightTile.getDrawableId() == tile.getDrawableId()){
            if (checkCombinationFromTile(downRightTile, tile))
                return true;
        }

        return false;
    }

    private void setSelectedTiles(int x, int y, boolean firstTouch){

        if (x > Constants.GRID_X_OFFSET && x < Constants.GAME_GRID_WIDTH + Constants.GRID_X_OFFSET &&
            y > Constants.GRID_Y_OFFSET && y < Constants.GAME_GRID_HEIGHT + Constants.GRID_Y_OFFSET){

            x = (x - Constants.GRID_X_OFFSET) / Constants.TILE_SIZE;
            y = (y - Constants.GRID_Y_OFFSET) / Constants.TILE_SIZE;

            Point touchedTilePosition = new Point(x * Constants.TILE_SIZE, y * Constants.TILE_SIZE);
            Tile tile = getTileAt(touchedTilePosition);
            if (tile != null){

                if (firstTouch){
                    this.selectingId = tile.getDrawableId();
                    markTile(tile, true);
                }
                else {
                    if (canBeSelected(tile)) {
                        markTile(tile, true);
                    }

                    if (tile == getSecondLastSelectedTile()){
                        markTile(this.selectedTiles.lastElement(), false);
                    }
                }
            }
        }
    }

    private Tile getSecondLastSelectedTile(){
        if (this.selectedTiles.size() < 2)
            return null;
        else
            return this.selectedTiles.elementAt(this.selectedTiles.size() - 2);
    }

    private boolean canBeSelected(Tile tile){

        if (this.selectingId != tile.getDrawableId()) {
            return false;
        }


        Tile lastSelectedTile = this.selectedTiles.peek();
        int xDistance = Math.abs(lastSelectedTile.getPosition().x - tile.getPosition().x);
        int yDistance = Math.abs(lastSelectedTile.getPosition().y - tile.getPosition().y);

        if ((xDistance == Constants.TILE_SIZE || xDistance == 0) && (yDistance == Constants.TILE_SIZE || yDistance == 0)){
            return  true;
        }

        return  false;
    }

    private void markTile(Tile tile, boolean selected){
        tile.setSelected(selected);
        if (selected){
            addTile(new Tile(tile.getPosition(), this.effectTile, TileType.EffectTile));
            if (!this.selectedTiles.contains(tile)){
                this.selectedTiles.push(tile);
            }
        }
        else {
            this.selectedTiles.pop();
            this.effectTiles.remove(tile.getPosition());
        }
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
        int randomIndex = randomGenerator.nextInt(GameTileIds.length);
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


        if (!combinationExist()){
            generateGameTileGrid();
        }
    }

    public void addTile(Tile tile) {
        switch (tile.getTileType()){
            case GameTile:
                gameTiles.put(tile.getPosition(), tile);
                break;
            case EffectTile:
                if (!this.effectTiles.containsKey(tile.getPosition()))
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
