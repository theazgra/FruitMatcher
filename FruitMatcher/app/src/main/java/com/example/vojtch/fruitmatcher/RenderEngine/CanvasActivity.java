package com.example.vojtch.fruitmatcher.RenderEngine;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.vojtch.fruitmatcher.R;

import java.util.Random;

public class CanvasActivity extends Activity implements SurfaceHolder.Callback {
    private SurfaceView view;
    private BitmapCombinator bitmapCombinator;

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
        this.bitmapCombinator = new BitmapCombinator(width, height, getResources());



    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {

        //Log.d("onTouchEvent", "touching...");
        this.view.invalidate();
        this.view.postInvalidate();

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
        Random random = new Random();
        Log.d("draw", "drawing...");

        Bitmap bitmatToDraw = this.bitmapCombinator.getCombinedBitmap();
        canvas.drawBitmap(bitmatToDraw, 0, 0, null);

        /*
        canvas.drawRGB(random.nextInt(255), random.nextInt(255), random.nextInt(255));
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);

        paint.setColor(Color.parseColor("#CD5C5C"));
        canvas.drawCircle(canvas.getWidth() / 2, canvas.getHeight() / 2, 200.0f, paint);
        */
    }
}
