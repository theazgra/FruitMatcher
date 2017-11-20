package com.example.vojtch.fruitmatcher.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import com.example.vojtch.fruitmatcher.Database.DatabaseEntity.LevelInfo;
import com.example.vojtch.fruitmatcher.Database.DatabaseEntity.PlayerInfo;
import com.example.vojtch.fruitmatcher.Database.DatabaseEntity.PlayerScore;
import com.example.vojtch.fruitmatcher.Database.DatabaseEntity.Time;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;


public class DBHandler extends SQLiteOpenHelper {

    private static final int DB_VERSION = 5;
    private static final String DB_NAME = "FRUITY";
    private static final String LEVEL_INFO_TABLE_NAME = "level";
    private static final String PLAYER_INFO_TABLE_NAME = "player";
    private static final String SCORE_TABLE_NAME = "score";

    private static final String KEY_LVL_ID = "levelId";
    private static final String KEY_LVL_LIMIT = "timeLimit";
    private static final String KEY_LVL_TILE = "tileCount";
    private static final String KEY_LVL_APPLE = "appleCount";
    private static final String KEY_LVL_BANANA = "bananaCount";
    private static final String KEY_LVL_BLUEBERRY = "blueberryCount";
    private static final String KEY_LVL_LEMON = "lemonCount";
    private static final String KEY_LVL_ORANGE= "orangeCount";
    private static final String KEY_LVL_STRAWBERRY = "strawberryCount";

    private static final String KEY_PLAYER_ID = "id";
    private static final String KEY_PLAYER_NAME = "name";
    private static final String KEY_PLAYER_MAXLVL = "maxLevel";
    private static final String KEY_PLAYER_PLAYED = "lastPlayed";
    private static final String KEY_PLAYER_IMAGE = "image";

    private static final String KEY_SCORE_PLAYER = "playerId";
    private static final String KEY_SCORE_LEVEL = "levelId";
    private static final String KEY_SCORE_TIME = "time";

    private Context context;

    public DBHandler(Context context){
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    public void forceCreate(){
        this.getReadableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createLevelInfo = "CREATE TABLE " + LEVEL_INFO_TABLE_NAME + " ( " +
                KEY_LVL_ID + " INTEGER PRIMARY KEY, " +
                KEY_LVL_LIMIT + " TEXT, " +
                KEY_LVL_TILE + " INTEGER NOT NULL DEFAULT 0, " +
                KEY_LVL_APPLE + " INTEGER NOT NULL DEFAULT 0, " +
                KEY_LVL_BANANA + " INTEGER NOT NULL DEFAULT 0, " +
                KEY_LVL_BLUEBERRY + " INTEGER NOT NULL DEFAULT 0, " +
                KEY_LVL_LEMON + " INTEGER NOT NULL DEFAULT 0, " +
                KEY_LVL_ORANGE + " INTEGER NOT NULL DEFAULT 0, " +
                KEY_LVL_STRAWBERRY + " INTEGER NOT NULL DEFAULT 0 " +
                ");";
        String createPlayerInfo = "CREATE TABLE " + PLAYER_INFO_TABLE_NAME + " ( " +
                KEY_PLAYER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_PLAYER_NAME + " TEXT NOT NULL, " +
                KEY_PLAYER_MAXLVL + " INTEGER NOT NULL DEFAULT 0, " +
                KEY_PLAYER_PLAYED + " TEXT, " +
                KEY_PLAYER_IMAGE + " BLOB, " +
                "FOREIGN KEY(" + KEY_PLAYER_MAXLVL + ") REFERENCES " + LEVEL_INFO_TABLE_NAME + "(" + KEY_LVL_ID + ") " +
                ");";

        String createScore = "CREATE TABLE " + SCORE_TABLE_NAME + " ( " +
                KEY_SCORE_PLAYER + " INTEGER NOT NULL, " +
                KEY_SCORE_LEVEL + " INTEGER NOT NULL, " +
                KEY_SCORE_TIME + " INTEGER NOT NULL, " +
                "FOREIGN KEY(" + KEY_SCORE_PLAYER + ") REFERENCES " + PLAYER_INFO_TABLE_NAME + "(" + KEY_PLAYER_ID + ") " +
                "FOREIGN KEY(" + KEY_SCORE_LEVEL + ") REFERENCES " + LEVEL_INFO_TABLE_NAME + "(" + KEY_LVL_ID + ") " +
                ");";

        db.execSQL(createLevelInfo);
        db.execSQL(createPlayerInfo);
        db.execSQL(createScore);

        Toast.makeText(this.context, "Created DB." , Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {

        db.execSQL("DROP TABLE IF EXISTS " + PLAYER_INFO_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + LEVEL_INFO_TABLE_NAME);
       // Create tables again
        onCreate(db);
    }

    public void insertLevels(){
        //level, limit, tileLimit, apple, banana, blueberry, lemon, orange, strawberry
        addLevelInfo(new LevelInfo(1, "00:05:00", 200, 5, 5, 5, 5, 5, 5));
        addLevelInfo(new LevelInfo(2, "00:05:00", 200, 20, 5, 30, 5, 15, 5));
        addLevelInfo(new LevelInfo(3, "00:05:00", 55, 3, 0, 5, 5, 3, 5));
        addLevelInfo(new LevelInfo(4, "00:04:00", 150, 10, 10, 10, 10, 10, 10));
        addLevelInfo(new LevelInfo(5, "00:04:00", 100, 5, 5, 5, 5, 5, 5));
        addLevelInfo(new LevelInfo(6, "00:02:00", 300, 30, 30, 30, 30, 30, 30));
        addLevelInfo(new LevelInfo(7, "00:03:00", 150, 5, 25, 5, 35, 5, 5));
        addLevelInfo(new LevelInfo(8, "00:01:30", 200, 20, 0, 5, 0, 5, 20));
        addLevelInfo(new LevelInfo(9, "00:02:00", 140, 5, 18, 5, 20, 5, 5));
        addLevelInfo(new LevelInfo(10, "00:02:00", 240, 14, 20, 5, 30, 5, 10));

        //not configured
        addLevelInfo(new LevelInfo(11, "00:05:00", 200, 5, 5, 5, 5, 5, 5));
        addLevelInfo(new LevelInfo(12, "00:05:00", 300, 25, 15, 30, 21, 40, 5));
        addLevelInfo(new LevelInfo(13, "00:05:00", 180, 5, 25, 5, 5, 15, 5));
        addLevelInfo(new LevelInfo(14, "00:04:00", 150, 10, 10, 10, 10, 10, 10));
        addLevelInfo(new LevelInfo(15, "00:04:00", 100, 5, 5, 15, 16, 5, 5));
        addLevelInfo(new LevelInfo(16, "00:02:00", 999, 100, 40, 30, 20, 0, 10));
        addLevelInfo(new LevelInfo(17, "00:03:00", 150, 5, 25, 18, 19, 5, 5));
        addLevelInfo(new LevelInfo(18, "00:01:30", 80, 12, 0, 5, 0, 5, 8));
        addLevelInfo(new LevelInfo(19, "00:02:00", 140, 5, 15, 5, 20, 5, 5));
        addLevelInfo(new LevelInfo(20, "00:02:00", 9999, 100, 100, 100, 100, 100, 100));
        addLevelInfo(new LevelInfo(21, "00:02:00", 99999, 1000, 1000, 1000, 1000, 1000, 1000));
    }

    public void addLevelInfo(LevelInfo levelInfo){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_LVL_ID, levelInfo.getLevelId());
        values.put(KEY_LVL_LIMIT, levelInfo.getTimeLimit());
        values.put(KEY_LVL_TILE, levelInfo.getTileCount());
        values.put(KEY_LVL_APPLE, levelInfo.getAppleCount());
        values.put(KEY_LVL_BANANA, levelInfo.getBananaCount());
        values.put(KEY_LVL_BLUEBERRY, levelInfo.getBlueberryCount());
        values.put(KEY_LVL_LEMON, levelInfo.getLemonCount());
        values.put(KEY_LVL_ORANGE, levelInfo.getOrangeCount());
        values.put(KEY_LVL_STRAWBERRY, levelInfo.getStrawberryCount());

        db.insert(LEVEL_INFO_TABLE_NAME, null, values);
        db.close();
    }

    public PlayerScore getPlayerScore(int levelId, int playerId){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(SCORE_TABLE_NAME,
                new String[]{
                        KEY_SCORE_PLAYER, KEY_SCORE_LEVEL, KEY_SCORE_TIME
                },
                KEY_SCORE_LEVEL + "=? AND " + KEY_SCORE_PLAYER + "=?",
                new String[]{String.valueOf(levelId), String.valueOf(playerId)},
                null, null, null, null );

        if (cursor != null && cursor.getCount() > 0){
            cursor.moveToFirst();
            int i = -1;
            PlayerScore playerScore = new PlayerScore(
                    cursor.getInt(++i),
                    cursor.getInt(++i),
                    new Time(cursor.getLong(++i))
            );

            cursor.close();
            db.close();
            return playerScore;
        }
        cursor.close();
        db.close();
        return null;
    }


    public LevelInfo getLevelInfo(int id){
        SQLiteDatabase db = this.getReadableDatabase();


        Cursor cursor = db.query(LEVEL_INFO_TABLE_NAME,
                new String[]{
                        KEY_LVL_ID, KEY_LVL_LIMIT, KEY_LVL_TILE,
                        KEY_LVL_APPLE, KEY_LVL_BANANA, KEY_LVL_BLUEBERRY,
                        KEY_LVL_LEMON,KEY_LVL_ORANGE, KEY_LVL_STRAWBERRY
                        },
                KEY_LVL_ID + "=?",
                new String[]{String.valueOf(id)},
                null, null, null, null );


        if (cursor != null){
            cursor.moveToFirst();
            int i = -1;
            LevelInfo levelInfo = new LevelInfo(
                    cursor.getInt(++i),
                    cursor.getString(++i),
                    cursor.getInt(++i),
                    cursor.getInt(++i),
                    cursor.getInt(++i),
                    cursor.getInt(++i),
                    cursor.getInt(++i),
                    cursor.getInt(++i),
                    cursor.getInt(++i)
            );

            cursor.close();
            db.close();
            return levelInfo;
        }
        cursor.close();
        db.close();
        return null;
    }

    public List<PlayerScore> getAllPlayerScores(int playerId){
        List<PlayerScore> scores = new ArrayList<PlayerScore>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(SCORE_TABLE_NAME,
                new String[]{
                        KEY_SCORE_LEVEL, KEY_SCORE_PLAYER, KEY_SCORE_TIME
                },
                KEY_SCORE_PLAYER + "=?",
                new String[]{String.valueOf(playerId)},
                null, null, null, null );

        if (cursor.moveToFirst()){
            do{
                int i = -1;
                PlayerScore playerScore = new PlayerScore(
                        cursor.getInt(++i),
                        cursor.getInt(++i),
                        new Time(cursor.getLong(++i))
                );
                scores.add(playerScore);

            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return scores;
    }


    public List<LevelInfo> getLevels(){
        List<LevelInfo> levels = new ArrayList<LevelInfo>();
        String query = "SELECT * FROM " + LEVEL_INFO_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);


        if (cursor.moveToFirst()){
            do{
                int i = -1;
                LevelInfo levelInfo = new LevelInfo(
                        cursor.getInt(++i),
                        cursor.getString(++i),
                        cursor.getInt(++i),
                        cursor.getInt(++i),
                        cursor.getInt(++i),
                        cursor.getInt(++i),
                        cursor.getInt(++i),
                        cursor.getInt(++i),
                        cursor.getInt(++i)
                );
                levels.add(levelInfo);

            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return levels;
    }

    public int getLevelCount(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + LEVEL_INFO_TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);

        int count =  cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }

    public int updateLevelInfo(LevelInfo levelInfo){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_LVL_LIMIT, levelInfo.getTimeLimit());
        values.put(KEY_LVL_TILE, levelInfo.getTileCount());
        values.put(KEY_LVL_APPLE, levelInfo.getAppleCount());
        values.put(KEY_LVL_BANANA, levelInfo.getBananaCount());
        values.put(KEY_LVL_BLUEBERRY, levelInfo.getBlueberryCount());
        values.put(KEY_LVL_LEMON, levelInfo.getLemonCount());
        values.put(KEY_LVL_ORANGE, levelInfo.getOrangeCount());
        values.put(KEY_LVL_STRAWBERRY, levelInfo.getStrawberryCount());

        int rows = db.update(LEVEL_INFO_TABLE_NAME, values, KEY_LVL_ID + "=?", new String[] {String.valueOf(levelInfo.getLevelId())});
        db.close();
        return rows;
    }

    public int updatePlayerScore(PlayerScore score){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_SCORE_LEVEL, score.getLevelId());
        values.put(KEY_SCORE_PLAYER, score.getPlayerId());
        values.put(KEY_SCORE_TIME, score.getTime().toLong());
        int rows = db.update(
                SCORE_TABLE_NAME,
                values,
                KEY_SCORE_PLAYER + "=? AND " + KEY_SCORE_LEVEL + "=?",
                new String[]{String.valueOf(score.getPlayerId()), String.valueOf(score.getLevelId())});

        db.close();
        return rows;
    }

    public void deleteLevelInfo(LevelInfo levelInfo){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(LEVEL_INFO_TABLE_NAME, KEY_LVL_ID + " = ?",
                new String[] { String.valueOf(levelInfo.getLevelId()) });
        db.close();
    }

    public void delelePlayerScore(PlayerScore score){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(SCORE_TABLE_NAME,
                KEY_SCORE_PLAYER + "=? AND " + KEY_SCORE_LEVEL + "=?",
                new String[]{String.valueOf(score.getPlayerId()), String.valueOf(score.getLevelId())});
        db.close();
    }

    public void addPlayerScore(PlayerScore playerScore){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_SCORE_LEVEL, playerScore.getLevelId());
        values.put(KEY_SCORE_PLAYER, playerScore.getPlayerId());
        values.put(KEY_SCORE_TIME, playerScore.getTime().toLong());

        db.insert(SCORE_TABLE_NAME, null, values);
        db.close();
    }

    public void addPlayerInfo(PlayerInfo playerInfo){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_PLAYER_NAME, playerInfo.getName());
        values.put(KEY_PLAYER_MAXLVL, 0);
        values.put(KEY_PLAYER_PLAYED, "");
        values.put(KEY_PLAYER_IMAGE, getBytes(playerInfo.getPlayerImg()));

        db.insert(PLAYER_INFO_TABLE_NAME, null, values);
        db.close();
    }

    private static byte[] getBytes(Bitmap bitmap){
        if (bitmap == null){
            return null;
        }
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 75, stream);
        return  stream.toByteArray();
    }

    public String getBestPlayerForLevel(int levelId) {
        SQLiteDatabase db = this.getReadableDatabase();

        String q = "SELECT " + KEY_SCORE_PLAYER + " FROM " + SCORE_TABLE_NAME + " WHERE " + KEY_SCORE_LEVEL + " =? ORDER BY " + KEY_SCORE_TIME + " LIMIT 1;";
        Cursor cursor = db.rawQuery(q, new String[]{String.valueOf(levelId)});
        if (cursor != null && cursor.getCount() > 0){
            cursor.moveToFirst();
            int playerId = cursor.getInt(0);
            cursor.close();
            db.close();
            return getPlayerInfo(playerId).getName();
        }
        return "";
    }

    public PlayerInfo getPlayerInfo(int id){
        SQLiteDatabase db = this.getReadableDatabase();


        Cursor cursor = db.query(PLAYER_INFO_TABLE_NAME,
                new String[]{
                        KEY_PLAYER_ID, KEY_PLAYER_NAME,
                        KEY_PLAYER_MAXLVL, KEY_PLAYER_PLAYED,
                        KEY_PLAYER_IMAGE
                },
                KEY_PLAYER_ID +  "=?",
                new String[]{String.valueOf(id)},
                null, null, null, null );


        if (cursor != null){
            if (cursor.getCount() <= 0){
                cursor.close();
                db.close();
                return null;
            }
            cursor.moveToFirst();
            int cols = cursor.getColumnCount();
            int i = -1;
            PlayerInfo playerInfo = new PlayerInfo(
                    cursor.getInt(++i),
                    cursor.getString(++i),
                    cursor.getInt(++i),
                    cursor.getString(++i),
                    cursor.isNull(++i) ? null : BitmapFactory.decodeByteArray(cursor.getBlob(i), 0, cursor.getBlob(i).length)
            );
            cursor.close();
            db.close();
            return playerInfo;
        }
        cursor.close();
        db.close();
        return null;
    }

    public List<PlayerInfo> getPlayers(){
        List<PlayerInfo> players = new ArrayList<PlayerInfo>();
        String query = "SELECT * FROM " + PLAYER_INFO_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);


        if (cursor.moveToFirst()){
            do{
                int i = -1;
                PlayerInfo playerInfo = new PlayerInfo(
                        cursor.getInt(++i),
                        cursor.getString(++i),
                        cursor.getInt(++i),
                        cursor.getString(++i),
                        cursor.isNull(++i) ? null : BitmapFactory.decodeByteArray(cursor.getBlob(i), 0, cursor.getBlob(i).length)
                );
                players.add(playerInfo);

            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return players;
    }

    public int getPlayerCount(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + PLAYER_INFO_TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);

        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }

    public boolean levelScoreInputExist(int playerId, int levelId){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + PLAYER_INFO_TABLE_NAME;
        Cursor cursor = db.query(
                SCORE_TABLE_NAME,
                new String[]{KEY_SCORE_PLAYER, KEY_SCORE_LEVEL, KEY_SCORE_TIME},
                KEY_SCORE_PLAYER + "=? AND " + KEY_SCORE_LEVEL + "=?",
                new String[]{String.valueOf(playerId), String.valueOf(levelId)},
                null, null, null, null );


        int count = cursor.getCount();
        cursor.close();
        db.close();
         if (count > 0){
             return true;
         }
         else {
             return false;
         }
    }

    public int updatePlayerInfo(PlayerInfo playerInfo){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_PLAYER_NAME, playerInfo.getName());
        values.put(KEY_PLAYER_MAXLVL, playerInfo.getMaxLevel());
        values.put(KEY_PLAYER_PLAYED, playerInfo.getPlayed());
        values.put(KEY_PLAYER_IMAGE, getBytes(playerInfo.getPlayerImg()));

        int rows = db.update(PLAYER_INFO_TABLE_NAME, values, KEY_PLAYER_ID + "=?", new String[] {String.valueOf(playerInfo.getId())});

        db.close();

        return  rows;
    }

    public void deletePlayer(PlayerInfo playerInfo){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(PLAYER_INFO_TABLE_NAME, KEY_PLAYER_ID + " = ?",
                new String[] { String.valueOf(playerInfo.getId()) });
        db.close();
    }
}

