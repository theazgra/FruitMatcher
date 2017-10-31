package com.example.vojtch.fruitmatcher;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;


import java.util.ArrayList;

public class GameActivity extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);

        //set applicattion full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);



        setContentView(R.layout.activity_game);

        RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.relativeLayout);

        RelativeLayout.LayoutParams glParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
        );

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
