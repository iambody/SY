package com.cgbsoft.lib.utils.cache;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by win8 on 2016/4/18.
 */
public class DBOpenHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "PrivateFundDB.db"; //数据库名称
    private static final int DATABASE_VERSION = 2;//数据库版本
    public static final String TABLE_NAME_USER = "pf_user";
    public static final String TABLE_NAME_OTHER = "pf_other";

    public DBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME_USER + " ("
                + UserData.user._ID + " TEXT,"
                + UserData.user.USER + " TEXT,"
                + UserData.user.TOKEN + " TEXT,"
                + UserData.user.LOGINFLAG + " TEXT,"
                + UserData.user.IDENTIFY + " TEXT"
                + ");");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME_OTHER + " ("
                + OtherData.Other._ID + " INTEGER PRIMARY KEY, "
                + OtherData.Other.TITLE + " TEXT NOT NULL, "
                + OtherData.Other.CONTENT + " TEXT"
                + ");"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_OTHER);
        onCreate(db);
    }
}
