package algonquin.cst2335.cst2335_finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * This class is a helper for managing a SQLite database that stores image URLs of bears.
 * @author Daniel Stewart
 * @version 1.0
 */
public class BearImageDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "bear_images.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "bear_images";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_IMAGE_URL = "image_url";

    /**
     * Constructs a new instance of the BearImageDatabaseHelper class.
     *
     * @param context The context in which this helper will be used.
     */
    public BearImageDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Called when the database is created for the first time. This method is responsible for creating
     * the necessary table for storing bear image URLs.
     *
     * @param db The SQLiteDatabase instance on which the table will be created.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_IMAGE_URL + " TEXT)";
        db.execSQL(createTableQuery);
    }

    /**
     * Called when the database needs to be upgraded. This method is responsible for dropping the
     * existing table and creating a new one with the updated schema.
     *
     * @param db         The SQLiteDatabase instance being upgraded.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop the existing table
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Create a new table with the updated schema
        onCreate(db);
    }


    /**
     * Inserts a new image URL into the database.
     *
     * @param imageUrl The URL of the image to be inserted.
     */
    public void insertImageUrl(String imageUrl) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_IMAGE_URL, imageUrl);
        db.insert(TABLE_NAME, null, values);
    }

    /**
     * Retrieves a list of all stored image URLs from the database.
     *
     * @return An ArrayList containing all stored image URLs.
     */
    public ArrayList<String> getAllImageUrls() {
        ArrayList<String> urlList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            try {
                int imageUrlColumnIndex = cursor.getColumnIndex(COLUMN_IMAGE_URL);
                if (imageUrlColumnIndex >= 0) {
                    while (cursor.moveToNext()) {
                        String imageUrl = cursor.getString(imageUrlColumnIndex);
                        urlList.add(imageUrl);
                    }
                } else {
                    Log.e("BearImageDatabaseHelper", "Column not found: " + COLUMN_IMAGE_URL);
                }
            } finally {
                cursor.close();
            }
        }
        return urlList;
    }

    /**
     * Deletes an image URL from the database.
     *
     * @param imageUrl The URL of the image to be deleted.
     */
    public void deleteImageUrl(String imageUrl) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_IMAGE_URL + "=?", new String[]{imageUrl});
    }
}
