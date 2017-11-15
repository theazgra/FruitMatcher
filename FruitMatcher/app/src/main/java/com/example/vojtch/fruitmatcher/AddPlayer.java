package com.example.vojtch.fruitmatcher;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.vojtch.fruitmatcher.Database.DBHandler;
import com.example.vojtch.fruitmatcher.Database.DatabaseEntity.PlayerInfo;

public class AddPlayer extends Activity {

    private int CAMERA_INTENT_CODE = 9894;
    private Bitmap playerImg = null;
    private String playerName = null;

    private EditText playerNameEdit;
    private ImageView playerImgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_add_player);

        this.playerNameEdit = (EditText) findViewById(R.id.editName);
        this.playerImgView = (ImageView)findViewById(R.id.newPlayerImg);

    }

    public void onAddImgClick(View view){
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (camera.resolveActivity(getPackageManager()) != null){
            startActivityForResult(camera, CAMERA_INTENT_CODE);
        }
    }

    public void onSavePlayerClick(View view){
        String playerName = playerNameEdit.getText().toString();
        if (playerName.isEmpty()){
            Toast.makeText(this, "Jméno hráče je prázdné", Toast.LENGTH_SHORT).show();
            return;
        }
        PlayerInfo newPlayer = new PlayerInfo(playerName);
        newPlayer.setPlayerImg(this.playerImg);

        DBHandler db = new DBHandler(this);
        db.addPlayerInfo(newPlayer);

        setResult(RESULT_OK);
        finish();
    }

    public void onCancelClick(View v){
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_INTENT_CODE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");


            this.playerImg = imageBitmap;
            this.playerImgView.setImageBitmap(this.playerImg);
        }
    }
}


