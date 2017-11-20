package com.example.vojtch.fruitmatcher.GameEngine;

import android.app.Activity;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.vojtch.fruitmatcher.Database.DBHandler;
import com.example.vojtch.fruitmatcher.Database.DatabaseEntity.LevelInfo;
import com.example.vojtch.fruitmatcher.Database.DatabaseEntity.PlayerInfo;
import com.example.vojtch.fruitmatcher.Database.DatabaseEntity.PlayerScore;
import com.example.vojtch.fruitmatcher.Database.DatabaseEntity.Time;
import com.example.vojtch.fruitmatcher.FruitMatcherApp;
import com.example.vojtch.fruitmatcher.R;
import com.example.vojtch.fruitmatcher.SoundFactory;

public class CanvasActivity extends Activity implements SurfaceHolder.Callback {
    private SurfaceView view;
    private Renderer renderer;
    private GameManager gameManager;
    private LevelInfo levelInfo;
    private long gameTime;
    private long startTime;

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

        this.levelInfo = (LevelInfo)getIntent().getParcelableExtra("levelInfo");

        if (this.levelInfo == null){
            throw new IllegalArgumentException("Level was not set");
        }



        this.gameManager = new GameManager(this.levelInfo, this, ((FruitMatcherApp)this.getApplication()).isSoundOn());
        this.gameManager.setActivityUnderHood(this);

        this.startTime = System.currentTimeMillis();
    }


    @Override
    public boolean onTouchEvent(MotionEvent e) {
        this.gameManager.onTouch(e);
        tryDrawing(this.view.getHolder());

        if (this.gameManager.isLevelWon()){
            this.gameTime = System.currentTimeMillis() - this.startTime;

            SoundFactory.playSound(this, R.raw.congratulation);
            updatePlayerMaxLvl();

            Toast.makeText(this,
                    String.valueOf(this.levelInfo.getLevelId()) + ". Úroveň byla dokončena.",
                    Toast.LENGTH_SHORT).show();

            setResult(RESULT_OK);
            finish();
        }
        return true;
    }

    private void updatePlayerMaxLvl(){
        PlayerInfo playerInfo = ((FruitMatcherApp)this.getApplication()).getPlayerInfo();
        DBHandler db = new DBHandler(this);

        if (this.levelInfo.getLevelId() > playerInfo.getMaxLevel()){
            playerInfo.setMaxLevel(this.levelInfo.getLevelId());
            db.updatePlayerInfo(playerInfo);
        }


        if (db.levelScoreInputExist(playerInfo.getId(), this.levelInfo.getLevelId())) {
            PlayerScore score = db.getPlayerScore(this.levelInfo.getLevelId(), playerInfo.getId());
            score.setTime(new Time(this.gameTime));
            db.updatePlayerScore(score);
        }
        else {
            PlayerScore score = new PlayerScore(playerInfo.getId(), this.levelInfo.getLevelId(), new Time(this.gameTime));
            db.addPlayerScore(score);
        }
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
        canvas.drawBitmap(
                this.renderer.getBitmapToRender(this.gameManager), 0, 0, null
        );
    }
}
