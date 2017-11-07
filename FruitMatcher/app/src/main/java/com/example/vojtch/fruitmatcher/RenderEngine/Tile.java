package com.example.vojtch.fruitmatcher.RenderEngine;


import android.graphics.Point;
import android.graphics.Rect;

public class Tile {

    private Point position;
    private Rect tileRect;
    private int drawableId;
    private TileType tileType;
    private boolean selected = false;
    private boolean visible = true;


    public Tile(Point position, int drawableId, TileType tileType){
        this.position = position;
        this.drawableId = drawableId;
        this.tileType = tileType;

        this.tileRect = new Rect(
                this.position.x,
                this.position.y,
                this.position.x + Constants.TILE_SIZE,
                this.position.y + Constants.TILE_SIZE);
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

    public void move(Point delta){
        this.position.x += delta.x;
        this.position.y += delta.y;

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
}
