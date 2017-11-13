package com.example.vojtch.fruitmatcher.RenderEngine;

import android.content.Context;
import android.graphics.Point;
import android.view.MotionEvent;
import android.widget.Toast;

import com.example.vojtch.fruitmatcher.Database.DatabaseEntity.LevelInfo;
import com.example.vojtch.fruitmatcher.R;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.Stack;


public class GameManager {
    private LevelInfo levelInfo;
    private boolean levelWon = false;

    private HashMap<Point, Tile> gameTiles;
    private HashMap<Point, Tile> effectTiles;
    private Stack<Tile> selectedTiles;


    private int bgId;
    private HashMap<String, Integer> quest;
    private Date timeLimit;
    private int levelTileCount;

    private Context context;

    private boolean handleTouch = true;
    private boolean wasCombo = false;
    private int selectingId = -1;
    private int possibleComboSize = 0;

    private static int[] GameTileIds = {
            R.drawable.apple,
            R.drawable.banana,
            R.drawable.blueberry,
            R.drawable.lemon,
            R.drawable.orange,
            R.drawable.strawberry
    };

    private int effectTile = R.drawable.selected;


    public GameManager(LevelInfo levelInfo, Context context){
        this.gameTiles = new HashMap<Point, Tile>();
        this.effectTiles = new HashMap<Point, Tile>();
        this.selectedTiles = new Stack<Tile>();
        this.levelInfo = levelInfo;
        this.context = context;


        loadLevel();
    }

    private void deselectAllTiles(){
        for (Tile tile : this.gameTiles.values()){
            tile.setSelected(false);
        }
        this.effectTiles.clear();
        this.selectedTiles.clear();
    }

    public void onTouch(MotionEvent e){
        int x = (int) e.getRawX();
        int y = (int) e.getRawY();

        switch (e.getAction()){
            case MotionEvent.ACTION_DOWN:{
                this.wasCombo = false;
                if (handleTouch){ setSelectedTiles(x, y, true); }
                break;
            }
            case MotionEvent.ACTION_MOVE:{
                if (handleTouch){ setSelectedTiles(x, y, false); }
                break;
            }
            case MotionEvent.ACTION_UP:{
                if (handleTouch){ setSelectedTiles(x, y, false); }

                checkMatch();

                deselectAllTiles();

                if (!combinationExist()) {
                    Toast.makeText(this.context, "No combination", Toast.LENGTH_SHORT).show();
                    //generateGameTileGrid();
                }

                refillGameGrid();
                applyGravity();
                break;
            }

        }
    }

    private void applyGravity(){
        for (int x = 0; x < Constants.GAME_SQUARE_SIZE; x++) {
            for (int y = Constants.GAME_SQUARE_SIZE - 1; y >= 0 ; y--) {
                Point location = new Point(x * Constants.TILE_SIZE, y * Constants.TILE_SIZE);
                if (getTileAt(location) == null){
                    applyGravityToColumn(new Point(x, y));
                    break;
                }
            }
        }
    }

    private void applyGravityToColumn(Point freeSpace){
        Stack<Point> freeSpaces = new Stack<Point>();
        Stack<Tile>  tilesToShift = new Stack<Tile>();

        for (int y = freeSpace.y; y >= 0 ; y--) {
            Point location = new Point(freeSpace.x * Constants.TILE_SIZE, y * Constants.TILE_SIZE);
            Tile tile = getTileAt(location);

            if (tile == null){
                freeSpaces.push(location);
            }
            else {
                freeSpaces.push(tile.getPosition());
                tilesToShift.push(tile);
            }
        }

        while (!tilesToShift.isEmpty()){
            Tile tile = getTileAt(tilesToShift.firstElement().getPosition());
            tilesToShift.removeElementAt(0);
            if (tile == null){
                continue;
            }

            //test only
            relocateTile(tile, freeSpaces.firstElement());

            tile.setAnimate(true);
            tile.setDestinationPosition(freeSpaces.firstElement());
            freeSpaces.removeElementAt(0);
        }
    }

    private void relocateTile(Tile tile, Point newPosition){

        this.gameTiles.remove(tile.getPosition());
        this.gameTiles.put(newPosition, tile);
        tile.setPosition(newPosition);
    }

    private void checkMatch(){
        if (this.selectedTiles.size() >= 3){
            this.wasCombo = true;
            int combo = this.selectedTiles.size();

            FruitType fruitType = null;

            while (!this.selectedTiles.isEmpty()){
                Tile tmp = this.selectedTiles.pop();
                fruitType = tmp.getFruitType();
                Point p = tmp.getPosition();
                this.gameTiles.remove(p);
            }

            this.levelInfo.setFruitCount(fruitType, (this.levelInfo.getFruitCount(fruitType) - combo));
            
            //Toast.makeText(context, "COMBO: " + String.valueOf(combo), Toast.LENGTH_SHORT).show();
            checkLevelWon();
        }
    }

    private void checkLevelWon(){
        if (    this.levelInfo.getAppleCount() <= 0 &&
                this.levelInfo.getBananaCount() <= 0 &&
                this.levelInfo.getBlueberryCount() <= 0 &&
                this.levelInfo.getLemonCount() <= 0 &&
                this.levelInfo.getOrangeCount() <= 0 &&
                this.levelInfo.getStrawberryCount() <= 0){
            //----------------------------------------
            this.handleTouch = false;
            this.levelWon = true;
        }
    }
    
    private boolean combinationExist(){

        if (this.gameTiles.size() <= 0){
            return false;
        }

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

        if (this.selectedTiles.size() <= 0){
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

    private void loadLevel(){
        this.bgId = R.drawable.bg;
        this.levelTileCount = this.levelInfo.getTileCount();

        generateGameTileGrid();
    }

    private int randomGameTileId(){
        Random randomGenerator = new Random();
        int randomIndex = randomGenerator.nextInt(GameTileIds.length);
        return GameTileIds[randomIndex];
    }

    private void refillGameGrid(){
        if (this.levelTileCount < 1 || !this.wasCombo){
            return;
        }

        //int numOfTiles = 0;
        for (int x = 0; x < Constants.GAME_SQUARE_SIZE; x++) {
            Point location = new Point(x * Constants.TILE_SIZE, 0);
            if (getTileAt(location) == null) {
                Tile newTile = new Tile(location, randomGameTileId(), TileType.GameTile);
                newTile.setDestinationPosition(location);

                addTile(newTile);

                //newTile.setPosition(new Point(location.x, location.y - Constants.TILE_SIZE));
                --this.levelTileCount;
            }
            if (this.levelTileCount < 1){
                return;
            }
        }
    }

    private void generateGameTileGrid(){
        gameTiles.clear();
        int numOfTiles = 0;

        if (this.levelTileCount <= 0){
            return;
        }

        for (int y = Constants.GAME_SQUARE_SIZE - 1; y >= 0 ; y--) {
            for (int x = 0; x < Constants.GAME_SQUARE_SIZE; x++) {

                Point position = new Point(x * Constants.TILE_SIZE, y * Constants.TILE_SIZE);
                addTile(new Tile(position, randomGameTileId(), TileType.GameTile));
                numOfTiles++;

            }
            if (numOfTiles > this.levelTileCount){
                break;
            }
        }

        this.levelTileCount -= numOfTiles;


        if (!combinationExist()){
            Toast.makeText(this.context, "no combination", Toast.LENGTH_SHORT).show();
            //generateGameTileGrid();
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

    private Tile getTileAt(Point position){
        if (this.gameTiles.containsKey(position)){
            return this.gameTiles.get(position);
        }
        return null;
    }

    public Collection<Tile> getEffectTiles() {
        return effectTiles.values();
    }

    public Collection<Tile> getGameTiles() {
        return gameTiles.values();
    }

    public int getBgId() {
        return bgId;
    }

    public LevelInfo getLevelInfo(){
        return this.levelInfo;
    }

    public boolean isLevelWon() {
        return levelWon;
    }
}
