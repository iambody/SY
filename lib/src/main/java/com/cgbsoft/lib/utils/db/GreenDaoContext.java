package com.cgbsoft.lib.utils.db;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;

import com.cgbsoft.lib.Appli;
import com.cgbsoft.lib.utils.cache.CacheManager;

import java.io.File;

/**
 * 为greenDAO准备的Context
 * Created by xiaoyu.zhang on 2016/11/30 13:56
 * Email:zhangxyfs@126.com
 *  
 */
public class GreenDaoContext extends ContextWrapper {

    public GreenDaoContext(Context base) {
        super(base);
    }

    /**
     * 获得数据库路径，如果不存在，则创建对象
     *
     * @param dbName
     */
    @Override
    public File getDatabasePath(String dbName) {
        StringBuilder sb = new StringBuilder();
        String path = CacheManager.getCachePath(Appli.getContext(), CacheManager.DB);
        sb.append(path);
        sb.append(dbName);
        return new File(sb.toString());
    }

    /**
     * 重载这个方法，是用来打开SD卡上的数据库的，android 2.3及以下会调用这个方法。
     *
     * @param name
     * @param mode
     * @param factory
     */
    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory) {
        return SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), factory);
    }

    /**
     * Android 4.0会调用此方法获取数据库。
     *
     * @param name
     * @param mode
     * @param factory
     * @param errorHandler
     * @see android.content.ContextWrapper#openOrCreateDatabase(java.lang.String, int,
     * android.database.sqlite.SQLiteDatabase.CursorFactory,
     * android.database.DatabaseErrorHandler)
     */
    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory, DatabaseErrorHandler errorHandler) {
        return SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), factory);
    }


}
