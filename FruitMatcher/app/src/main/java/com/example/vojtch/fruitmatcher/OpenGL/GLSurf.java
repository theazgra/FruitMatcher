package com.example.vojtch.fruitmatcher.OpenGL;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

/**
 * Created by Vojtěch on 25.10.2017.
 */

public class GLSurf extends GLSurfaceView {

    private final GLRenderer renderer;

    public GLSurf(Context context) {
        super(context);
        setEGLContextClientVersion(2);

        renderer = new GLRenderer(context);
        setRenderer(renderer);

        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

    }

    @Override
    public  boolean onTouchEvent(MotionEvent event){
        renderer.processTouchEvent(event);
        return true;
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        renderer.onPause();
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        renderer.onResume();
    }
}
