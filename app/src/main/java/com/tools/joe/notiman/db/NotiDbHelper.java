package com.tools.joe.notiman.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by joe_wang on 2016/8/8.
 */
public class NotiDbHelper extends SQLiteOpenHelper {

    public  NotiDbHelper(Context context) {
        super(context, "notifications.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DbContract.NotiInfo.CREATOR);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }
}
