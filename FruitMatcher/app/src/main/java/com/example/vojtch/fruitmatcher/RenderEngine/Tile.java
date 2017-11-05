package com.example.vojtch.fruitmatcher.RenderEngine;


import android.graphics.Point;
import android.graphics.Rect;

public class Tile {

    private Point position;
    private Rect tileRect;
    private int drawableId;
    private TileType tileType;


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

    public void scaleInPercent(float scale){
        int currentWidth = this.tileRect.right - this.tileRect.left;
        int currentHeight = this.tileRect.bottom - this.tileRect.top;

        int newWidth = (int)(currentWidth * scale);
        int newHeight = (int)(currentHeight * scale);

        this.tileRect.right = this.tileRect.left + newWidth;
        this.tileRect.bottom = this.tileRect.top + newHeight;
    }

    public void move(Point delta){
        this.position.x += delta.x;
        this.position.y += delta.y;
    }

    public Rect getTileRect(){
        return this.tileRect;
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
}
