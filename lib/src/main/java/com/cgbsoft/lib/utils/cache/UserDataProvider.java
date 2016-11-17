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
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by win8 on 2016/4/18.
 */
public class UserDataProvider extends ContentProvider {
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
        empProjectionMap.put(UserData.user.USER, UserData.user.USER);
        empProjectionMap.put(UserData.user.TOKEN, UserData.user.TOKEN);
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
        long rowId = db.insert(DBOpenHelper.TABLE_NAME_USER, UserData.user.USER, values);
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
        String token = query(context, UserData.user.TOKEN);
        return token;
    }

    /**
     * 获取是否登陆
     *
     * @param context
     * @return
     */
    public static Boolean queryLoginFlag(Context context) {
        String flag = query(context, UserData.user.LOGINFLAG);
        if (flag.equals("1")) {
            return true;
        }
        return false;
    }

    /**
     * 获取身份
     *
     * @param context
     * @return
     */
    public static String queryIdentify(Context context) {
        return query(context, UserData.user.IDENTIFY);
    }

    /**
     * 获取用户信息
     *
     * @param context
     * @return
     */
    public static String queryUserInfoData(Context context) {
        String json = query(context, UserData.user.USER);
        if (!TextUtils.isEmpty(json)) {
            return json;
        }
        return null;
    }

    /**
     * 初始化用户信息
     *
     * @param context
     * @param userData
     * @param token
     * @param identify
     */
    public static void insertUserInfo(Context context, String isLogin, String userData, String token, String identify) {
        UserDataProvider.del(context);
        ContentValues values = new ContentValues();
        values.put(UserData.user._ID, 1);
        values.put(UserData.user.LOGINFLAG, isLogin);
        values.put(UserData.user.USER, userData);
        values.put(UserData.user.TOKEN, token);
        values.put(UserData.user.IDENTIFY, identify);
        // 插入
        context.getContentResolver().insert(UserData.user.CONTENT_URI, values);
    }

    public static Boolean updateToken(Context context, String token) {
        ContentValues values = new ContentValues();
        values.put(UserData.user.TOKEN, token);
        String[] selectValue = {"1"};
        int status = context.getContentResolver().update(UserData.user.CONTENT_URI, values, "_id" + "=?", selectValue);
        if (status != 1) {
            return false;
        }
        return true;
    }

    public static Boolean updateUserInfoData(Context context, String userData) {
        ContentValues values = new ContentValues();
        values.put(UserData.user.USER, userData);
        String[] selectValue = {"1"};
        int status = context.getContentResolver().update(UserData.user.CONTENT_URI, values, "_id" + "=?", selectValue);
        if (status != 1) {
            return false;
        }
        return true;
    }

    public static Boolean updateLoginFlag(Context context, boolean flag) {
        ContentValues values = new ContentValues();
        values.put(UserData.user.LOGINFLAG, flag ? "1" : "0");
        String[] selectValue = {"1"};
        int status = context.getContentResolver().update(UserData.user.CONTENT_URI, values, "_id" + "=?", selectValue);
        if (status != 1) {
            return false;
        }
        return true;
    }

    /**
     * 1为投资人2为理财师
     *
     * @param context
     * @param identify
     * @return
     */
    public static Boolean updateUserIDENT(Context context, String identify) {
        ContentValues values = new ContentValues();
        values.put(UserData.user.IDENTIFY, identify);
        String[] selectValue = {"1"};
        int status = context.getContentResolver().update(UserData.user.CONTENT_URI, values, "_id" + "=?", selectValue);
        if (status != 1) {
            return false;
        }
        return true;
    }


    // 删除方法
    public static void del(Context context) {

        /****  删除ID为1的记录的方法：

         // 删除ID为1的记录
         Uri uri = ContentUris.withAppendedId(Employee.CONTENT_URI, 1);
         // 获得ContentResolver，并删除
         getContentResolver().delete(uri, null, null);

         ****/
        Uri uri = ContentUris.withAppendedId(UserData.user.CONTENT_URI, 1);
        int i = context.getContentResolver().delete(uri, null, null);
    }


}
