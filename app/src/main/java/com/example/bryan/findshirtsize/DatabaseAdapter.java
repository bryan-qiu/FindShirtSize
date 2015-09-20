package com.example.bryan.findshirtsize;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Credits to https://github.com/tracylei/
 */
public class DatabaseAdapter {
    // Logcat tag
    private static final String LOG = "Database";
    DatabaseHelper helper;
    Context ct;
    public DatabaseAdapter(Context context){
        Log.i(LOG, "Adapter ctor");
        helper = new DatabaseHelper(context);
        helper.getWritableDatabase();
        ct = context;
    }

    public long createShirt (int input_size, String brand, String brand_shirt_size){
        Log.i(LOG, "Inserting shirt data");
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.KEY_INPUT_SIZE, input_size);
        cv.put(DatabaseHelper.KEY_BRAND, brand);
        cv.put(DatabaseHelper.KEY_SHIRT_SIZE, brand_shirt_size);
        //Returns id corresponding to the newly inserted row
        return db.insert(DatabaseHelper.TABLE_NAME, null, cv);
    }

    public List<String> searchShirts (int input_size) {

        List<String> ret = new ArrayList<String>();

        Log.i(LOG, "Searching shirt data");

        SQLiteDatabase db = helper.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_NAME + " WHERE input_size = "
                + input_size;

        Log.i(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                String td = "";

                td = c.getString(c.getColumnIndex(DatabaseHelper.KEY_BRAND)) + " " +
                        c.getString(c.getColumnIndex(DatabaseHelper.KEY_SHIRT_SIZE));

                // adding to ret list
                ret.add(td);
            } while (c.moveToNext());
        }

        return ret;

    }

    static class DatabaseHelper extends SQLiteOpenHelper {

        // Logcat tag
        private static final String LOG = "Database";

        private static final int DATABASE_VERSION = 8;

        private static final String DATABASE_NAME = "shirtDatabase";

        // Table names
        private static final String TABLE_NAME = "shirts";

        // Key
        private static final String UID = "_id";

        // SHIRTS table
        private static final String KEY_INPUT_SIZE = "input_size";
        private static final String KEY_BRAND = "brand";
        private static final String KEY_SHIRT_SIZE = "brand_shirt_size";


        private Context context;
        private static final String CREATE_TABLE_SHIRTS =  "CREATE TABLE " + TABLE_NAME + " ( "+ UID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, "+ KEY_INPUT_SIZE + " TEXT, "+ KEY_BRAND +
                " TEXT, " + KEY_SHIRT_SIZE + " TEXT);";


        private static final String DROP_SHIRTS = "DROP TABLE IF EXISTS "+ TABLE_NAME;

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            Log.i(LOG, "Database helper ctor");
            this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try{
                db.execSQL(CREATE_TABLE_SHIRTS);
                Log.i(LOG, "Shirt table created");
            } catch (SQLException e){

            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try{
                db.execSQL(DROP_SHIRTS);
                Log.i(LOG, "Shirt table deleted");
            } catch (SQLException e){

            }
        }
    }
}