package algonquin.cst2335.cst2335_finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class BearImageDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "bear_images.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "bear_images";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_IMAGE_URL = "image_url";

    public BearImageDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_IMAGE_URL + " TEXT)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertImageUrl(String imageUrl) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_IMAGE_URL, imageUrl);
        db.insert(TABLE_NAME, null, values);

    }

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


    public void deleteImageUrl(String imageUrl) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_IMAGE_URL + "=?", new String[]{imageUrl});

    }
}
