package com.tools.joe.notiman.db;

import android.provider.BaseColumns;

/**
 * Created by joe_wang on 2016/8/8.
 */
public interface DbContract {
    interface NotiInfo {
        String TABLE_NAME = "Noti";

        String ID = BaseColumns._ID;
        String PACKAGENAME = "packageName";
        String TITLE="title";
        String CONTENT="content";
        String TIME = "time";

        String CREATOR = " CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( "
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PACKAGENAME + " STRING NOT NULL, "
                + TITLE + " TEXT, "
                + CONTENT + " TEXT, "
                + TIME + " DATETIME ); ";

    }
}
