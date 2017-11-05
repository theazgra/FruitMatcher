package com.example.vojtch.fruitmatcher.RenderEngine;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

public class BitmapCombinator {

    private Bitmap masterBitmap;
    private Canvas masterCanvas;
    private Resources resources;

    private int bitmapWidth;
    private int bitmapHeight;

    public BitmapCombinator(int width, int height, Resources resources){
        this.bitmapWidth = width;
        this.bitmapHeight = height;
        this.resources = resources;

        this.masterBitmap = Bitmap.createBitmap(this.bitmapWidth, this.bitmapHeight, Bitmap.Config.ARGB_8888);
        this.masterCanvas = new Canvas(masterBitmap);

    }

    public void updateBitmatResolution(int width, int height){
        this.bitmapWidth = width;
        this.bitmapHeight = height;

        this.masterBitmap = Bitmap.createBitmap(this.bitmapWidth, this.bitmapHeight, Bitmap.Config.ARGB_8888);
        this.masterCanvas = new Canvas(masterBitmap);
    }


    public Bitmap getCombinedBitmap(TileManager tileManager){

        //here get all needed bitmaps, e.g.

        /*
        Drawable d = this.resources.getDrawable(R.drawable.flag_cz, null);

        int drawableWidth = 100;
        int drawableHeight = d.getIntrinsicHeight();

        int colCount = this.bitmapWidth / drawableWidth;
        int rowCount = this.bitmapHeight / drawableWidth;

        for (int i = 0; i < colCount; i++) {
            for (int j = 0; j < rowCount; j++) {

                Drawable d1 = this.resources.getDrawable(R.drawable.flag_cz, null);
                int x = drawableWidth * i;
                int y = drawableWidth * j;
                d1.setBounds(x, y, x + drawableWidth, y + drawableWidth);
                d1.draw(this.masterCanvas);
            }
        }
        */

        //first pass just draw tiles
        for (Tile tile : tileManager.getGameTiles()){
            Drawable tmpDrawable = this.resources.getDrawable(tile.getDrawableId(), null);
            tmpDrawable.setBounds(tile.getTileRect());
            tmpDrawable.draw(this.masterCanvas);
        }

        //second pass styling tiles


        return  masterBitmap;
    }

    public static Bitmap getBitmapFromDrawable(Context context, int resId){
        return BitmapFactory.decodeResource(context.getResources(), resId);
    }
}
