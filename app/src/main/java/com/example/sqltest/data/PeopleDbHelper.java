package com.example.sqltest.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PeopleDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = PeopleDbHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "people.db";

    private static final int DATABASE_VERSION = 1;


    public PeopleDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String SQL_CREATE_PETS_TABLE =  "CREATE TABLE " + PeopleContract.PeopleEntry.TABLE_NAME + " ("
                + PeopleContract.PeopleEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PeopleContract.PeopleEntry.COLUMN_NAME + " TEXT NOT NULL, "
                + PeopleContract.PeopleEntry.COLUMN_SURNAME+ " TEXT); ";

        db.execSQL(SQL_CREATE_PETS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // The database is still at version 1, so there's nothing to do be done here.
    }
}
