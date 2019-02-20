package com.example.user1.applock.project.DbHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHandler extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "ClearDataDemo";
// Table Name

    private static final String TABLE_DEMO = "demo";
    // Demo Table Columns names
    private static final String ID = "id";

    private static final String EMAIL = "email";

    private static final String PASS = "pass";

    public DataBaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
// TODO Auto-generated method stub
        String CREATE_DEMO_TABLE = "CREATE TABLE " + TABLE_DEMO + "("
                + ID + " INTEGER PRIMARY KEY,"
                + EMAIL + " TEXT,"
                + PASS + " TEXT" + ")";
        db.execSQL(CREATE_DEMO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DEMO);
        onCreate(db);
    }

    // Saving to DB
    public void saveDemo_data(String name, String pass) {

        System.out.println(name + " " + pass);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EMAIL, name);
        values.put(PASS, pass);

        db.insert(TABLE_DEMO, null, values);

        db.close();
    }

    // Getting row count from table
    public int getTable_Count() {

        String selectQuery = null;
        selectQuery = "SELECT  * FROM " + TABLE_DEMO;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor.getCount();
    }
}
