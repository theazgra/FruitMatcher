package com.example.vojtch.fruitmatcher.OpenGL;

import android.graphics.PointF;

/**
 * Created by Vojtěch on 27.10.2017.
 */

public class TextObject {

    private String text;
    private PointF position;
    private float[] color;

    public TextObject(String text, PointF position, float[] color)
    {
        this.text = text;
        this.position = position;
        this.color = new float[] {1f, 1f, 1f, 1.0f};
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public float[] getColor() {
        return color;
    }

    public void setColor(float[] color) {
        this.color = color;
    }

    public PointF getPosition() {
        return position;
    }

    public void setPosition(PointF position) {
        this.position = position;
    }
}
