package algonquin.cst2335.cst2335_finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * A utility class for managing a SQLite database to store trivia scores.
 * This class extends SQLiteOpenHelper and provides methods to create, upgrade,
 * insert, retrieve, and delete trivia scores from the database.
 */
public class TriviaDatabase extends SQLiteOpenHelper {

    // Constants for the database
    private static final String DATABASE_NAME = "trivia.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "trivia_score";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_PLAYER_NAME = "player_name";
    private static final String COLUMN_SCORE = "score";
    private static final String COLUMN_DATE = "game_date";

    /**
     * Constructor for TriviaDatabase.
     *
     * @param context The context in which the database should be created.
     */
    public TriviaDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Called when the database is created for the first time. This method should create
     * the necessary tables.
     *
     * @param db The SQLiteDatabase instance.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTblQry = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PLAYER_NAME + " VARCHAR(20) NOT NULL, " +
                COLUMN_SCORE + " INT(3) NOT NULL, " +
                COLUMN_DATE + " DATE NOT NULL)";
        db.execSQL(createTblQry);
    }

    /**
     * Called when the database needs to be upgraded. This method should handle any necessary
     * data migration and re-creation of tables.
     *
     * @param db         The SQLiteDatabase instance.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    /**
     * Insert a TriviaScore object into the database.
     *
     * @param ts The TriviaScore object to be inserted.
     */
    public void insertTriviaScore(TriviaScore ts) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PLAYER_NAME, ts.getPlayerName());
        values.put(COLUMN_SCORE, ts.getScore());
        values.put(COLUMN_DATE, ts.getGameDate().toString());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    /**
     * Retrieve all stored trivia scores from the database.
     *
     * @return A list of TriviaScore objects representing all stored scores.
     */
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

    /**
     * Delete a specific TriviaScore object from the database.
     *
     * @param ts The TriviaScore object to be deleted.
     */
    public void deleteTriviaScore(TriviaScore ts) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + "=?", new String[]{String.valueOf(ts.getId())});
        db.close();
    }
}
