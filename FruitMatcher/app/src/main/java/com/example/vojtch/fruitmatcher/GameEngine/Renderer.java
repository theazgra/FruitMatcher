package com.example.vojtch.fruitmatcher.GameEngine;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;

import com.example.vojtch.fruitmatcher.Database.DatabaseEntity.LevelInfo;
import com.example.vojtch.fruitmatcher.R;

import java.util.ArrayList;

public class Renderer {

    private int lastDrawnBg = -1;

    private Bitmap masterBitmap;
    private Bitmap gameGridBitmap;
    private Bitmap questBitmap;
    private Bitmap coctailBitmap;

    private Canvas masterCanvas;
    private Canvas gameGridCanvas;
    private Canvas questCanvas;
    private Canvas coctailCanvas;

    private Resources resources;

    private int bitmapWidth;
    private int bitmapHeight;

    private Paint textPaint;
    private Paint rectPaint;

    private ArrayList<Tile> questTiles =  null;

    public Renderer(int width, int height, Resources resources){
        updateBitmapResolution(width, height);

        this.resources = resources;

        this.textPaint = new Paint();
        this.textPaint.setColor(Color.BLACK);
        this.textPaint.setTextAlign(Paint.Align.LEFT);
        this.textPaint.setFakeBoldText(true);
        this.textPaint.setTextSize(40.0f);

        this.rectPaint = new Paint();
        this.rectPaint.setColor(Color.BLUE);
        this.rectPaint.setStrokeWidth(15.0f);
        this.rectPaint.setStyle(Paint.Style.STROKE);
    }

    public void updateBitmapResolution(int width, int height){
        this.bitmapWidth = width;
        this.bitmapHeight = height;

        this.masterBitmap = Bitmap.createBitmap(this.bitmapWidth, this.bitmapHeight, Bitmap.Config.ARGB_8888);
        this.gameGridBitmap = Bitmap.createBitmap(Constants.GAME_GRID_WIDTH, Constants.GAME_GRID_HEIGHT, Bitmap.Config.ARGB_8888);
        this.questBitmap = Bitmap.createBitmap(Constants.QUEST_PANEL_WIDTH, Constants.QUEST_PANEL_HEIGH, Bitmap.Config.ARGB_8888);
        this.coctailBitmap = Bitmap.createBitmap(Constants.COCTAIL_PANEL_WIDTH, Constants.COCTAIL_PANEL_HEIGHT, Bitmap.Config.ARGB_8888);

        this.masterCanvas = new Canvas(this.masterBitmap);
        this.gameGridCanvas = new Canvas(this.gameGridBitmap);
        this.questCanvas = new Canvas(this.questBitmap);
        this.coctailCanvas = new Canvas(this.coctailBitmap);
    }

    private Drawable getDrawable(int id){
        return this.resources.getDrawable(id, null);
    }

    public Bitmap getBitmapToRender(GameManager gameManager){

        if (this.lastDrawnBg != gameManager.getBgId()){
            Drawable bg = getDrawable(gameManager.getBgId());
            bg.setBounds(0,0, this.masterBitmap.getWidth(), this.masterBitmap.getHeight());
            bg.draw(this.masterCanvas);

            this.lastDrawnBg = gameManager.getBgId();
        }

        this.gameGridCanvas.drawRGB(20, 20, 20);
        this.questCanvas.drawRGB(255, 136, 20);


        //Game grid
        for (Tile tile : gameManager.getGameTiles()){
            if (tile.isVisible()){
                Drawable gameTile = getDrawable(tile.getDrawableId());
                gameTile.setBounds(tile.getTileRect());
                gameTile.draw(this.gameGridCanvas);
            }
        }

        for (Tile tile : gameManager.getEffectTiles()){
            Drawable effectTile = getDrawable(tile.getDrawableId());
            effectTile.setBounds(tile.getTileRect());
            effectTile.draw(this.gameGridCanvas);
        }


        //this.questCanvas.drawRGB(0,0,255);
        drawQuestPanel(gameManager.getLevelInfo());

        this.coctailCanvas.drawRGB(150,0,255);


        this.masterCanvas.drawBitmap(
                this.gameGridBitmap,
                Constants.GRID_X_OFFSET,
                Constants.GRID_Y_OFFSET,
                null);

        this.masterCanvas.drawBitmap(
                this.questBitmap,
                Constants.Quest_X_OFFSET,
                Constants.QUEST_Y_OFFSET,
                null
        );

        this.masterCanvas.drawBitmap(
                this.coctailBitmap,
                0,
                0,
                null
        );



        return  this.masterBitmap;
    }

    private void drawQuestPanel(LevelInfo levelInfo){
        //first call
        if (this.questTiles == null){
            this.questTiles = new ArrayList<Tile>();

            int y = (Constants.QUEST_PANEL_HEIGH / 2) - (Constants.TILE_SIZE / 2) ;
            int x = 50;

            if (levelInfo.getAppleCount() > 0){
                this.questTiles.add(new Tile(new Point(x, y), R.drawable.apple, 1.2f, levelInfo.getAppleCount()));
                x += Constants.TILE_SIZE + 50;
            }

            if (levelInfo.getBananaCount() > 0){
                this.questTiles.add(new Tile(new Point(x, y), R.drawable.banana, 1.2f, levelInfo.getBananaCount()));
                x += Constants.TILE_SIZE + 50;
            }

            if (levelInfo.getBlueberryCount() > 0){
                this.questTiles.add(new Tile(new Point(x, y), R.drawable.blueberry, 1.2f, levelInfo.getBlueberryCount()));
                x += Constants.TILE_SIZE + 50;
            }

            if (levelInfo.getLemonCount() > 0){
                this.questTiles.add(new Tile(new Point(x, y), R.drawable.lemon, 1.2f, levelInfo.getLemonCount()));
                x += Constants.TILE_SIZE + 50;
            }

            if (levelInfo.getOrangeCount() > 0){
                this.questTiles.add(new Tile(new Point(x, y), R.drawable.orange, 1.2f, levelInfo.getOrangeCount()));
                x += Constants.TILE_SIZE + 50;
            }

            if (levelInfo.getStrawberryCount() > 0){
                this.questTiles.add(new Tile(new Point(x, y), R.drawable.strawberry, 1.2f, levelInfo.getStrawberryCount()));
            }
        }
        else
        {
            for (Tile tile : this.questTiles){
                FruitType fruit = tile.getFruitType();
                tile.setRequiredCount(levelInfo.getFruitCount(fruit));
            }
        }

        this.questCanvas.drawRect(0, 0, this.questCanvas.getWidth(), this.questCanvas.getHeight(), rectPaint);

        for (Tile tile : this.questTiles){
            Drawable drawable = getDrawable(tile.getDrawableId());
            drawable.setBounds(tile.getTileRect());
            drawable.draw(this.questCanvas);

            this.questCanvas.drawText(String.valueOf(tile.getRequiredCount()), tile.getTileRect().right,
                    (Constants.QUEST_PANEL_HEIGH/2) + 10.0f, this.textPaint);
        }
    }
}
