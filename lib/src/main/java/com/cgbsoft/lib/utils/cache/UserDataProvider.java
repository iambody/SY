package com.cgbsoft.lib.utils.cache;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.cgbsoft.lib.utils.tools.Base64Util;

import java.util.HashMap;

/**
 * Created by win8 on 2016/4/18.
 */
public class UserDataProvider extends ContentProvider implements CPConstant {
    private DBOpenHelper dbHelper;
    // Uri工具类
    private static final UriMatcher sUriMatcher;
    // 查询、更新条件
    private static final int EMPLOYEE = 1;
    private static final int EMPLOYEE_ID = 2;
    // 查询列集合
    private static HashMap<String, String> empProjectionMap;
//    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        // Uri匹配工具类
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(UserData.AUTHORITY, "user", EMPLOYEE);
        sUriMatcher.addURI(UserData.AUTHORITY, "user/#", EMPLOYEE_ID);
        // 实例化查询列集合
        empProjectionMap = new HashMap<>();
        // 添加查询列
        empProjectionMap.put(UserData.user.TITLE, UserData.user.TITLE);
        empProjectionMap.put(UserData.user.VALUE, UserData.user.VALUE);
    }


    @Override
    public boolean onCreate() {
        this.dbHelper = new DBOpenHelper(this.getContext());
        dbHelper.onCreate(dbHelper.getWritableDatabase());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        switch (sUriMatcher.match(uri)) {
            // 查询所有
            case EMPLOYEE:
                qb.setTables(DBOpenHelper.TABLE_NAME_USER);
                qb.setProjectionMap(empProjectionMap);
                break;
            // 根据ID查询
            case EMPLOYEE_ID:
                qb.setTables(DBOpenHelper.TABLE_NAME_USER);
                qb.setProjectionMap(empProjectionMap);
                qb.appendWhere(UserData.user._ID + "=" + uri.getPathSegments().get(1));
                break;
            default:
                throw new IllegalArgumentException("Uri错误！ " + uri);
        }
        // 获得数据库实例
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        // 返回游标集合
        try {
            Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
            if (c != null) {
                c.setNotificationUri(getContext().getContentResolver(), uri);
                return c;
            }
            return null;
        } catch (Exception ex) {
            Log.d("Contacts", ex.toString());
        }
        return null;

    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // 获得数据库实例
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // 插入数据，返回行ID
        long rowId = db.insert(DBOpenHelper.TABLE_NAME_USER, UserData.user.TITLE, values);
        // 如果插入成功返回uri
        if (rowId > 0) {
            Uri empUri = ContentUris.withAppendedId(UserData.user.CONTENT_URI, rowId);
            getContext().getContentResolver().notifyChange(empUri, null);
            return empUri;
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // 获得数据库实例
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // 获得数据库实例
        int count;
        switch (sUriMatcher.match(uri)) {
            // 根据指定条件删除
            case EMPLOYEE:
                count = db.delete(DBOpenHelper.TABLE_NAME_USER, selection, selectionArgs);
                break;
            // 根据指定条件和ID删除
            case EMPLOYEE_ID:
                String noteId = uri.getPathSegments().get(1);
                count = db.delete(DBOpenHelper.TABLE_NAME_USER, UserData.user._ID + "=" + noteId
                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("错误的 URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // 获得数据库实例
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count;
        switch (sUriMatcher.match(uri)) {
            // 根据指定条件更新
            case EMPLOYEE:
                count = db.update(DBOpenHelper.TABLE_NAME_USER, values, selection, selectionArgs);
                break;
            // 根据指定条件和ID更新
            case EMPLOYEE_ID:
                String noteId = uri.getPathSegments().get(1);
                count = db.update(DBOpenHelper.TABLE_NAME_USER, values, UserData.user._ID + "=" + noteId
                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("错误的 URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    private static String query(Context context, String key) {
        // 查询列数组
        String[] PROJECTION = new String[]{
                key
        };
        String value = "";

        Cursor cursor = context.getContentResolver().query(UserData.user.CONTENT_URI, PROJECTION, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                // 遍历游标
                for (int i = 0; i < cursor.getCount(); i++) {
                    cursor.moveToPosition(i);
                    value = cursor.getString(0);
                }
            }
            cursor.close();
            cursor = null;
        }
        return value;
    }

    /**
     * 获取token
     *
     * @param context
     * @return
     */
    public static String queryToken(Context context) {
        return queryByTitle(context, USER_TOKEN_KEY);
    }

    /**
     * 获取是否登录
     *
     * @param context
     * @return
     */
    public static Boolean queryLoginFlag(Context context) {
        String flag = queryByTitle(context, USER_LOGINFLAG_KEY);
        if (flag.equals("1")) {
            return true;
        }
        return false;
    }

    public static void quitLogin(Context context){
        delete(context, USER_LOGINFLAG_KEY);
        delete(context, USER_TOKEN_KEY);
        delete(context, USER_ID_KEY);
        delete(context, USER_INFO_KEY);
    }

    public static void saveUserId(Context context, String uid) {
        delete(context, USER_ID_KEY);
        insertUpDate(context, USER_ID_KEY, uid);
    }

    public static String getUserId(Context context) {
        return queryByTitle(context, USER_ID_KEY);
    }

    public static void saveLoginName(Context context, String loginName){
        delete(context, USER_LOGIN_NAME);
        insertUpDate(context, USER_LOGIN_NAME, loginName);
    }

    public static String getLoginName(Context context){
        return queryByTitle(context, USER_LOGIN_NAME);
    }

    /**
     * 获取用户信息
     *
     * @param context
     * @return
     */
    public static String queryUserInfoData(Context context) {
        String json = queryByTitle(context, USER_INFO_KEY);
        if (!TextUtils.isEmpty(json)) {
            return json;
        }
        return null;
    }

    public static void saveToken(Context context, String token) {
        delete(context, USER_TOKEN_KEY);
        insertUpDate(context, USER_TOKEN_KEY, token);
    }

    public static void saveUserInfo(Context context, String json) {
        delete(context, USER_INFO_KEY);
        insertUpDate(context, USER_INFO_KEY, json);
    }

    public static void saveLoginFlag(Context context, boolean flag) {
        delete(context, USER_LOGINFLAG_KEY);
        insertUpDate(context, USER_LOGINFLAG_KEY, flag ? "1" : "0");
    }


    public static void clear(Context context) {
        context.getContentResolver().delete(UserData.user.CONTENT_URI, null, null);
    }


    public static String queryByTitle(@NonNull Context context, @NonNull String title) {
        String base64 = "";

        Cursor cursor = context.getContentResolver().query(UserData.user.CONTENT_URI, null, UserData.user.TITLE + "=?", new String[]{title}, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                // 遍历游标
                for (int i = 0; i < cursor.getCount(); i++) {
                    cursor.moveToPosition(i);
                    base64 = cursor.getString(0);
                }
            }
            cursor.close();
        }
        if (!TextUtils.isEmpty(base64)) {
            return Base64Util.fromBase64(base64, Base64.DEFAULT);
        }
        return base64;
    }

    public static void insertUpDate(@NonNull Context context, @NonNull String title, @NonNull String content) {
        String base64 = Base64Util.toBase64(content, Base64.DEFAULT);

        String value = queryByTitle(context, title);
        ContentValues values = new ContentValues();
        if (TextUtils.isEmpty(value)) {
            values.put(UserData.user.TITLE, title);
            values.put(UserData.user.VALUE, base64);
            // 插入
            context.getContentResolver().insert(UserData.user.CONTENT_URI, values);
        } else {
            //更新
            values.put(UserData.user.VALUE, base64);
            context.getContentResolver().update(UserData.user.CONTENT_URI, values, UserData.user.TITLE + "=?", new String[]{title});
        }
    }

    public static void delete(Context context, String title) {
        String value = queryByTitle(context, title);
        if (!TextUtils.isEmpty(value)) {
            context.getContentResolver().delete(UserData.user.CONTENT_URI, UserData.user.TITLE + "=?", new String[]{title});
        }
    }


}
