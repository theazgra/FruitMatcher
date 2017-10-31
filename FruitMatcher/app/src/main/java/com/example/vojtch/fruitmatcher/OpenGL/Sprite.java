package com.example.vojtch.fruitmatcher.OpenGL;

import android.graphics.PointF;
import android.graphics.RectF;

/**
 * Created by Vojtěch on 26.10.2017.
 */


public class Sprite {
    private float angle;
    private float scale;
    private RectF base;
    private PointF translation;

    public Sprite(float scaleRation){
        //initial location of 0,0
        this.base = new RectF(-50f * scaleRation, 50f * scaleRation, 50f * scaleRation, -50f * scaleRation);
        this.translation = new PointF(50f * scaleRation, 50f * scaleRation);
        this.scale = 1f;
        this.angle = 0f;
    }

    public void Translate(float deltaX, float deltaY){
        this.translation.x += deltaX;
        this.translation.y += deltaY;
    }

    public void Scale(float scaleDelta){
        this.scale += scaleDelta;
    }

    public void Rotate(float angleDelta)
    {
        this.angle += angleDelta;
    }

    public float[] GetTransformedVertices(){
        //scaling
        float x1 = this.base.left   *   this.scale;
        float x2 = this.base.right  *   this.scale;
        float y1 = this.base.bottom *   this.scale;
        float y2 = this.base.top    *   this.scale;

        //rotating
        PointF one      = new PointF(x1, y2);
        PointF two      = new PointF(x1, y1);
        PointF three    = new PointF(x2, y1);
        PointF four     = new PointF(x2, y2);

        float sin = (float)Math.sin(angle);
        float cos = (float)Math.cos(angle);

        one.x   = x1 * cos - y2 * sin;
        one.y   = x1 * sin + y2 * cos;
        two.x   = x1 * cos - y1 * sin;
        two.y   = x1 * sin + y1 * cos;
        three.x = x2 * cos - y1 * sin;
        three.y = x2 * sin + y1 * cos;
        four.x  = x2 * cos - y2 * sin;
        four.y  = x2 * sin + y2 * cos;

        //translating
        one.x   += this.translation.x;
        one.y   += this.translation.y;
        two.x   += this.translation.x;
        two.y   += this.translation.y;
        three.x += this.translation.x;
        three.y += this.translation.y;
        four.x  += this.translation.x;
        four.y  += this.translation.y;

        return new float[]{
                one.x,      one.y,      0.0f,
                two.x,      two.y,      0.0f,
                three.x,    three.y,    0.0f
        };
    }
}
