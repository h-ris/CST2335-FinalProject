package algonquin.cst2335.cst2335_finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class TriviaDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "trivia.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "trivia_score";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_PLAYER_NAME = "player_name";
    private static final String COLUMN_SCORE = "score";
    private static final String COLUMN_DATE = "game_date";

    public TriviaDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTblQry = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PLAYER_NAME + " VARCHAR(20) NOT NULL, " +
                COLUMN_SCORE + " INT(3) NOT NULL, " +
                COLUMN_DATE + " DATE NOT NULL)";
        db.execSQL(createTblQry);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertTriviaScore(TriviaScore ts) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PLAYER_NAME, ts.getPlayerName());
        values.put(COLUMN_SCORE, ts.getScore());
        values.put(COLUMN_DATE, ts.getGameDate().toString());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public List<TriviaScore> getAllTriviaScores() {
        List<TriviaScore> scoreList = new ArrayList<>();
        String selectAllQry = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectAllQry, null);
        if (cursor != null) {
            try {
                while (cursor.moveToNext()) {
                    int id = cursor.getInt(0);
                    String playerName = cursor.getString(1);
                    int score = cursor.getInt(2);
                    String gameDate = cursor.getString(3);
                    scoreList.add(new TriviaScore(id, playerName, score, gameDate));
                }
            } finally {
                cursor.close();
            }
        }
        db.close();
        return scoreList;
    }

    public void deleteTriviaScore(TriviaScore ts) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + "=?", new String[]{String.valueOf(ts.getId())});
        db.close();
    }
}
