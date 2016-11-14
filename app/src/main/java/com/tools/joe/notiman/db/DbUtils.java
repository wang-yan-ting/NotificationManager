package com.tools.joe.notiman.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tools.joe.notiman.model.NotiInfo;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by joe_wang on 2016/8/8.
 */
public class DbUtils {
    NotiDbHelper mNotiDbHelper;

    @Inject
    public DbUtils(NotiDbHelper dbHelper) {
        mNotiDbHelper = dbHelper;
    }

    public void add(String packageName, String title,
             String content, long time) {

        SQLiteDatabase db = mNotiDbHelper.getWritableDatabase();
        if(db == null) {
            return;
        }

        try {
            ContentValues value = new ContentValues();
            value.put(DbContract.NotiInfo.PACKAGENAME, packageName);
            value.put(DbContract.NotiInfo.TITLE, title);
            value.put(DbContract.NotiInfo.CONTENT, content);
            value.put(DbContract.NotiInfo.TIME, time);
            db.insert(DbContract.NotiInfo.TABLE_NAME, null, value);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if(db != null && db.isOpen()) {
                db.close();
            }
        }
    }

    public void delete(String packageName, long time) {
        SQLiteDatabase db = mNotiDbHelper.getWritableDatabase();
        if(db == null) {
            return;
        }

        try {
            db.delete(DbContract.NotiInfo.TABLE_NAME,
                    DbContract.NotiInfo.PACKAGENAME + " = ? and " + DbContract.NotiInfo.TIME + " = ?",
                    new String[]{packageName, String.valueOf(time)});
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if(db != null && db.isOpen()) {
                db.close();
            }
        }

    }

    public void deleteAll() {

        SQLiteDatabase db = mNotiDbHelper.getWritableDatabase();
        if(db == null) {
            return;
        }

        try {
            db.delete(DbContract.NotiInfo.TABLE_NAME, null, null);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if(db != null && db.isOpen()) {
                db.close();
            }
        }
    }

    public List<NotiInfo> getAll() {
        SQLiteDatabase db = mNotiDbHelper.getReadableDatabase();
        if(db == null) {
            return null;
        }

        String packageName;
        String title;
        String content;
        long   time;

        int idxPackageName;
        int idxTitle;
        int idxContent;
        int idxTime;

        List<NotiInfo> list = new ArrayList<>();
        if(list == null) {
            return null;
        }

        Cursor c;
        final String[] columns = new String[] {DbContract.NotiInfo.PACKAGENAME,
                DbContract.NotiInfo.TITLE, DbContract.NotiInfo.CONTENT, DbContract.NotiInfo.TIME};

        try {
            c = db.query(DbContract.NotiInfo.TABLE_NAME, columns,
                    null, null, null, null, DbContract.NotiInfo.TIME + " desc");
            if(c == null) {
                return null;
            }

            idxPackageName = c.getColumnIndex(DbContract.NotiInfo.PACKAGENAME);
            idxTitle       = c.getColumnIndex(DbContract.NotiInfo.TITLE);
            idxContent     = c.getColumnIndex(DbContract.NotiInfo.CONTENT);
            idxTime        = c.getColumnIndex(DbContract.NotiInfo.TIME);

            c.moveToFirst();

            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                packageName = c.getString(idxPackageName);
                title = c.getString(idxTitle);
                content = c.getString(idxContent);
                time = c.getLong(idxTime);

                NotiInfo info = new NotiInfo(packageName, title, content, time);
                list.add(info);
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if(db != null && db.isOpen()) {
                db.close();
            }
        }

        return list;
    }
}
