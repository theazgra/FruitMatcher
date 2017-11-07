package com.example.vojtch.fruitmatcher.RenderEngine;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

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

    public Renderer(int width, int height, Resources resources){
        updateBitmapResolution(width, height);

        this.resources = resources;
    }

    public void updateBitmapResolution(int width, int height){
        this.bitmapWidth = width;
        this.bitmapHeight = height;

        this.masterBitmap = Bitmap.createBitmap(this.bitmapWidth, this.bitmapHeight, Bitmap.Config.ARGB_8888);
        this.gameGridBitmap = Bitmap.createBitmap(Constants.GAME_GRID_WIDTH, Constants.GAME_GRID_HEIGHT, Bitmap.Config.ARGB_8888);
        this.questBitmap = Bitmap.createBitmap(Constants.QUESTION_PANEL_WIDTH, Constants.QUESTION_PANEL_HEIGH, Bitmap.Config.ARGB_8888);
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

        this.gameGridCanvas.drawRGB(100, 100, 0);


        //Game grid
        for (Tile tile : gameManager.getGameTiles()){
            Drawable gameTile = getDrawable(tile.getDrawableId());
            gameTile.setBounds(tile.getTileRect());
            gameTile.draw(this.gameGridCanvas);
        }

        for (Tile tile : gameManager.getEffectTiles()){
            Drawable effectTile = getDrawable(tile.getDrawableId());
            effectTile.setBounds(tile.getTileRect());
            effectTile.draw(this.gameGridCanvas);
        }


        this.questCanvas.drawRGB(0,0,255);
        this.coctailCanvas.drawRGB(150,0,255);

        Paint p = new Paint();
        p.setARGB(255, 255, 100, 10);
        p.setTextSize(60);

        this.questCanvas.drawText("Bananas: 25", 20,100, p);

        this.masterCanvas.drawBitmap(
                this.gameGridBitmap,
                Constants.GRID_X_OFFSET,
                Constants.GRID_Y_OFFSET,
                null);

        this.masterCanvas.drawBitmap(
                this.questBitmap,
                0,
                Constants.QUESTION_PANEL_Y,
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
}
