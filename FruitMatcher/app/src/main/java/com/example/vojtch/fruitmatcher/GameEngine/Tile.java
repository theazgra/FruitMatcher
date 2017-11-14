package com.example.vojtch.fruitmatcher.GameEngine;

import android.graphics.Point;
import android.graphics.Rect;

import com.example.vojtch.fruitmatcher.R;

public class Tile {

    private Point position;
    private Point destinationPosition;
    private boolean animate;

    private Rect tileRect;
    private int drawableId;
    private TileType tileType;
    private boolean selected = false;
    private boolean visible = true;

    private FruitType fruitType;

    private int requiredCount ;

    public Tile(Point position, int drawableId, float scale, int requiredCount){
        this.position = position;
        this.drawableId = drawableId;
        this.requiredCount = requiredCount;

        setFruitType();


        this.tileRect = new Rect(
                this.position.x,
                this.position.y,
                this.position.x + Constants.TILE_SIZE,
                this.position.y + Constants.TILE_SIZE);

        this.tileRect = scaleInPercent(scale);
    }

    public Tile(Point position, int drawableId, TileType tileType){
        this.position = position;
        this.drawableId = drawableId;
        this.tileType = tileType;

        setFruitType();

        this.tileRect = new Rect(
                this.position.x,
                this.position.y,
                this.position.x + Constants.TILE_SIZE,
                this.position.y + Constants.TILE_SIZE);
    }

    private void setFruitType(){
        switch (this.drawableId){
            case R.drawable.apple:
                this.fruitType = FruitType.Apple;
                break;
            case R.drawable.banana:
                this.fruitType = FruitType.Banana;
                break;
            case R.drawable.blueberry:
                this.fruitType = FruitType.Bluberry;
                break;
            case R.drawable.lemon:
                this.fruitType = FruitType.Lemon;
                break;
            case R.drawable.orange:
                this.fruitType = FruitType.Orange;
                break;
            case R.drawable.strawberry:
                this.fruitType = FruitType.Strawberry;
                break;
        }
    }

    public void setPosition(Point location){
        this.position = location;
        this.tileRect.left = this.position.x;
        this.tileRect.right = this.position.x + Constants.TILE_SIZE;
        this.tileRect.top = this.position.y;
        this.tileRect.bottom = this.position.y + Constants.TILE_SIZE;
    }

    public void setSelected(boolean value){
        this.selected = value;
    }

    /**
    * Scale in percent around center.
    * */
    public Rect scaleInPercent(float scale){

        int currentWidth =      this.tileRect.right - this.tileRect.left;
        int currentHeight =     this.tileRect.bottom - this.tileRect.top;

        int newWidth =          (int)(currentWidth * scale);
        int newHeight =         (int)(currentHeight * scale);

        int deltaWidth =        (newWidth - currentWidth)   / 2;
        int deltaHeight =       (newHeight - currentHeight) / 2;

        Rect scaledRect = new Rect(this.tileRect);
        scaledRect.left      -= deltaWidth;
        scaledRect.right     += deltaWidth;
        scaledRect.top       -= deltaHeight;
        scaledRect.bottom    += deltaHeight;

        return scaledRect;
    }

    public Rect getTileRect(){
        if (this.selected){
          return scaleInPercent(1.3f);
        }
        else {
            return this.tileRect;
        }

    }

    public Point getPosition(){
        return  this.position;
    }

    public int getDrawableId(){
        return this.drawableId;
    }

    public TileType getTileType() {
        return tileType;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public Point getDestinationPosition() {
        return destinationPosition;
    }

    public void setDestinationPosition(Point destinationPosition) {
        this.destinationPosition = destinationPosition;
    }

    public boolean isAnimate() {
        return animate;
    }

    public void setAnimate(boolean animate) {
        this.animate = animate;
    }

    public int getRequiredCount() {
        return requiredCount;
    }

    public void setRequiredCount(int requiredCount) {
        this.requiredCount = requiredCount;
    }

    public FruitType getFruitType() {
        return fruitType;
    }
}
