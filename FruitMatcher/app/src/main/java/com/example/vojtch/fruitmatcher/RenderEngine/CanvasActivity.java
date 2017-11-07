package com.example.vojtch.fruitmatcher.RenderEngine;

import android.app.Activity;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;

import com.example.vojtch.fruitmatcher.R;

public class CanvasActivity extends Activity implements SurfaceHolder.Callback {
    private SurfaceView view;
    private Renderer renderer;
    private GameManager gameManager;
    private static int level = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_canvas);

        this.view = new SurfaceView(this);

        setContentView(view);
        view.getHolder().addCallback(this);

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        this.renderer = new Renderer(width, height, getResources());



        if (level == -1) {
            level = 1;
            //throw new IllegalArgumentException("Level was not set!");
        }
        this.gameManager = new GameManager(level, this);
    }

    public static void setLevel(int lvl){
        level = lvl;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        this.gameManager.onTouch(e);
        tryDrawing(this.view.getHolder());
        return true;
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        tryDrawing(surfaceHolder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        tryDrawing(surfaceHolder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    public static void loadLevel(int level){

    }

    private void tryDrawing(SurfaceHolder holder) {
        Canvas canvas = holder.lockCanvas();
        if (canvas == null) {
            Log.d("Error","canvas is null");
        }
        else {
            draw(canvas);
            holder.unlockCanvasAndPost(canvas);
        }
    }

    private void draw (final Canvas canvas) {
        canvas.drawBitmap(
                this.renderer.getBitmapToRender(this.gameManager), 0, 0, null
        );
    }
}
