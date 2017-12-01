package com.jrpohlman.chatmeup.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jorda on 12/1/2017.
 */

public class UserDbHelper extends SQLiteOpenHelper{
    public static final String LOG_TAG = UserDbHelper.class.getSimpleName();
    private static final String DATABASE_NAME = "chatroom.db";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_USERNAME = "swd_groupnine";
    private static final String DATABASE_PASSWORD = "2017swd";
    private static final int PORT = 3306;
    private static final String HOST_NAME = "107.180.46.234";


    public UserDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        // Create a String that contains the SQL statement to create the pets table
        String SQL_CREATE_SWD_TABLE =  "CREATE TABLE " + UserContract.UserEntry.TABLE1_NAME + " ("
                + UserContract.UserEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + UserContract.UserEntry.COLUMN_NAME + " TEXT NOT NULL, "
                + UserContract.UserEntry.COLUMN_MESSAGE + "TEXT);";

        db.execSQL(SQL_CREATE_SWD_TABLE);

        String SQL_CREATE_GROUP_TABLE =  "CREATE TABLE " + UserContract.UserEntry.TABLE2_NAME + " ("
                + UserContract.UserEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + UserContract.UserEntry.COLUMN_NAME + " TEXT NOT NULL, "
                + UserContract.UserEntry.COLUMN_MESSAGE + "TEXT);";

        db.execSQL(SQL_CREATE_GROUP_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
