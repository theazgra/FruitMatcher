package com.example.vojtch.fruitmatcher.RenderEngine;

public class Constants {
    public static final int TILE_SIZE = 100;
    public static final int GAME_SQUARE_SIZE = 10;


    public static final int WIDTH = 1080;
    public static final int HEIGHT = 1920;
    public static final int GAME_GRID_WIDTH = 1000;
    public static final int GAME_GRID_HEIGHT = 1000;

    public static final int GRID_X_OFFSET = 40;
    public static final int GRID_Y_OFFSET = HEIGHT - GAME_GRID_HEIGHT;

    public static final int QUESTION_PANEL_WIDTH = WIDTH;
    public static final int COCTAIL_PANEL_WIDTH = WIDTH;
    public static final int QUESTION_PANEL_HEIGH = 200;
    public static final int COCTAIL_PANEL_HEIGHT = HEIGHT - GAME_GRID_HEIGHT - QUESTION_PANEL_HEIGH;

    public static final int QUESTION_PANEL_Y = GRID_Y_OFFSET - QUESTION_PANEL_HEIGH;
}
