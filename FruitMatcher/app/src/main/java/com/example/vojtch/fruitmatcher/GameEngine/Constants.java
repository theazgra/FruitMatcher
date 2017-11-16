package com.example.vojtch.fruitmatcher.GameEngine;

public class Constants {
    public static final int TILE_SIZE = 100;
    public static final int GAME_SQUARE_SIZE = 10;


    public static final int WIDTH = 1080;
    public static final int HEIGHT = 1920;
    public static final int GAME_GRID_WIDTH = 1000;
    public static final int GAME_GRID_HEIGHT = 1000;

    public static final int X_OFFSET = 40;
    public static final int GRID_Y_OFFSET = HEIGHT - GAME_GRID_HEIGHT;

    public static final int QUEST_PANEL_WIDTH = GAME_GRID_WIDTH;
    public static final int COCTAIL_PANEL_WIDTH = GAME_GRID_WIDTH;
    public static final int QUEST_PANEL_HEIGH = 250;
    public static final int COCTAIL_PANEL_HEIGHT = HEIGHT - GAME_GRID_HEIGHT - QUEST_PANEL_HEIGH;

    public static final int QUEST_Y_OFFSET = GRID_Y_OFFSET - QUEST_PANEL_HEIGH;
}
