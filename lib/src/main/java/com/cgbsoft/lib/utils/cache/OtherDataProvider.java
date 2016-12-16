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

import com.cgbsoft.lib.utils.tools.Utils;

import java.util.HashMap;

/**
 * Created by xiaoyu.zhang on 2016/11/7 13:56
 */
public class OtherDataProvider extends ContentProvider implements CPConstant {
    private DBOpenHelper dbHelper;
    // Uri工具类
    private static final UriMatcher sUriMatcher;

    // 查询列集合
    private static HashMap<String, String> empProjectionMap;//设置别名：用户定义列名->数据库列名
//    private static ObjectMapper objectMapper = new ObjectMapper();


    static {
        // Uri匹配工具类
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(OtherData.AUTHORITY, "other", EMPLOYEE);
        sUriMatcher.addURI(OtherData.AUTHORITY, "other/#", EMPLOYEE_ID);
        // 实例化查询列集合
        empProjectionMap = new HashMap<>();
        // 添加查询列
        empProjectionMap.put(OtherData.Other.TITLE, OtherData.Other.TITLE);
        empProjectionMap.put(OtherData.Other.CONTENT, OtherData.Other.CONTENT);
    }

    @Override
    public boolean onCreate() {
        this.dbHelper = new DBOpenHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        switch (sUriMatcher.match(uri)) {
            // 查询所有
            case EMPLOYEE:
                qb.setTables(DBOpenHelper.TABLE_NAME_OTHER);
                qb.setProjectionMap(empProjectionMap);
                break;
            // 根据ID查询
            case EMPLOYEE_ID:
                qb.setTables(DBOpenHelper.TABLE_NAME_OTHER);
                qb.setProjectionMap(empProjectionMap);
                qb.appendWhere(OtherData.Other._ID + "=" + uri.getPathSegments().get(1));
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
            ex.printStackTrace();
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
        long rowId = db.insert(DBOpenHelper.TABLE_NAME_OTHER, OtherData.Other.TITLE, values);
        // 如果插入成功返回uri
        if (rowId > 0) {
            Uri empUri = ContentUris.withAppendedId(OtherData.Other.CONTENT_URI, rowId);
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
                count = db.delete(DBOpenHelper.TABLE_NAME_OTHER, selection, selectionArgs);
                break;
            // 根据指定条件和ID删除
            case EMPLOYEE_ID:
                String noteId = uri.getPathSegments().get(1);
                count = db.delete(DBOpenHelper.TABLE_NAME_OTHER, OtherData.Other._ID + "=" + noteId
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
                count = db.update(DBOpenHelper.TABLE_NAME_OTHER, values, selection, selectionArgs);
                break;
            // 根据指定条件和ID更新
            case EMPLOYEE_ID:
                String noteId = uri.getPathSegments().get(1);
                count = db.update(DBOpenHelper.TABLE_NAME_OTHER, values, OtherData.Other._ID + "=" + noteId
                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("错误的 URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    /**
     * 获取身份 1为理财师 2为投资人
     *
     * @return
     */
    public static int getIdentify(@NonNull Context context) {
        String what = queryByTitle(context.getApplicationContext(), IDENTIFY_FLAG_KEY);
        if (TextUtils.equals(what, "1")) {
            return 1;
        } else if (TextUtils.equals(what, "2")) {
            return 2;
        }
        return -1;
    }

    /**
     * 保存身份信息
     *
     * @param context
     * @param ids
     */
    public static void saveIdentify(@NonNull Context context, int ids) {
        delete(context.getApplicationContext(), IDENTIFY_FLAG_KEY);
        insertUpDate(context.getApplicationContext(), IDENTIFY_FLAG_KEY, ids + "");
    }


    public static int getVersonCode(@NonNull Context context) {
        int code = 1;
        context = context.getApplicationContext();
        String what = queryByTitle(context, APP_VERSION_CODE_KEY);
        if (TextUtils.isEmpty(what)) {
            code = Utils.getVersionCode(context);
            insertUpDate(context, APP_VERSION_CODE_KEY, String.valueOf(code));
        } else {
            code = Utils.isNumber(what) ? Integer.parseInt(what) : code;
        }
        return code;
    }

    public static String getVersonName(@NonNull Context context) {
        String name;
        context = context.getApplicationContext();
        String what = queryByTitle(context, APP_VERSION_NAME_KEY);
        if (TextUtils.isEmpty(what)) {
            name = Utils.getVersionName(context);
            insertUpDate(context, APP_VERSION_CODE_KEY, name);
        } else {
            name = what;
        }
        return name;
    }


    public static String findTopActivity(Context context) {
        return queryByTitle(context.getApplicationContext(), ACTIVITY_KEY);
    }

    public static void addTopActivity(Context context, String which) {
        delete(context.getApplicationContext(), ACTIVITY_KEY);
        insertUpDate(context.getApplicationContext(), ACTIVITY_KEY, which);
    }


    public static void delTopActivity(Context context) {
        delete(context.getApplicationContext(), ACTIVITY_KEY);
    }

    /**
     * 是否第一次打开应用
     *
     * @param context
     * @return
     */
    public static boolean isFirstOpenApp(Context context) {
        String txt = queryByTitle(context, ISFIRSTOEPNAPP_KEY);
        if (TextUtils.isEmpty(txt)) {
            return false;
        }
        return true;
    }

    public static void saveFirstOpenApp(Context context) {
        delete(context, ISFIRSTOEPNAPP_KEY);
        insertUpDate(context, ISFIRSTOEPNAPP_KEY, "true");
    }

    /**
     * 获取ip
     *
     * @param context
     * @return
     */
    public static String getIP(Context context) {
        return queryByTitle(context, NET_PHONE_IP_KEY);
    }

    public static void saveIP(Context context, String ip) {
        delete(context, NET_PHONE_IP_KEY);
        insertUpDate(context, NET_PHONE_IP_KEY, ip);
    }

    /**
     * 获取city
     *
     * @param context
     * @return
     */
    public static String getCity(Context context) {
        return queryByTitle(context, LCAL_PHONE_CITY_KEY);
    }

    public static void saveCity(Context context, String ip) {
        delete(context, LCAL_PHONE_CITY_KEY);
        insertUpDate(context, LCAL_PHONE_CITY_KEY, ip);
    }


    /**
     * 是否第一次启动
     *
     * @param context
     * @return
     */
    public static boolean isFirstLaunched(Context context) {
        String txt = queryByTitle(context, IS_ALREADY_LAUNCH);
        if (TextUtils.isEmpty(txt)) {
            return false;
        }
        return true;
    }


    /**
     * 保存apk路径
     *
     * @param context
     * @param path
     */
    public static void saveDownloadApk(Context context, String path) {
        delete(context, DOWNLOADAPK_KEY);
        insertUpDate(context, DOWNLOADAPK_KEY, path);
    }

    public static String getDownloadApk(Context context) {
        return queryByTitle(context, DOWNLOADAPK_KEY);
    }

    public static void saveDownloadApkId(Context context, long id) {
        delete(context, DOWNLOADAPKID_KEY);
        insertUpDate(context, DOWNLOADAPKID_KEY, String.valueOf(id));
    }

    public static long getDownloadApkId(Context context) {
        String id = queryByTitle(context, DOWNLOADAPKID_KEY);
        return Utils.isNumber(id) ? Long.parseLong(id) : -1;
    }

    public static void saveWelcomeImgUrl(Context context, String path) {
        delete(context, WELCOME_IMG_KEY);
        insertUpDate(context, WELCOME_IMG_KEY, path);
    }

    public static String getWelcomeImgUrl(Context context) {
        return queryByTitle(context, WELCOME_IMG_KEY);
    }

    //---------------融云---------------------

    /**
     * 保存融云token
     *
     * @param context
     * @param token
     */
    public static void saveRongToken(Context context, String token) {
        delete(context, RONGCLOUD_TOKEN_KEY);
        insertUpDate(context, RONGCLOUD_TOKEN_KEY, token);
    }

    public static String getRongToken(Context context) {
        return queryByTitle(context, RONGCLOUD_TOKEN_KEY);
    }

    public static void saveRongExpired(Context context, String te) {
        delete(context, RONDCLOUD_TOKENEXPIRED_KEY);
        insertUpDate(context, RONDCLOUD_TOKENEXPIRED_KEY, te);
    }

    public static String getRongTokenExpired(Context context) {
        return queryByTitle(context, RONDCLOUD_TOKENEXPIRED_KEY);
    }

    public static void saveRongUid(Context context, String uid) {
        delete(context, RONGCLOUD_UID_KEY);
        insertUpDate(context, RONGCLOUD_UID_KEY, uid);
    }

    public static String getRongUid(Context context) {
        return queryByTitle(context, RONGCLOUD_UID_KEY);
    }

    //---------------------------------

    public static void clear(Context context) {
        context.getContentResolver().delete(OtherData.Other.CONTENT_URI, null, null);
    }

    public static String queryByTitle(Context context, String title) {
        String value = "";

        Cursor cursor = context.getContentResolver().query(OtherData.Other.CONTENT_URI, null, OtherData.Other.TITLE + "=?", new String[]{title}, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                // 遍历游标
                for (int i = 0; i < cursor.getCount(); i++) {
                    cursor.moveToPosition(i);
                    value = cursor.getString(0);
                }
            }
            cursor.close();
        }
        return value;
    }

    public static void insertUpDate(Context context, String title, String content) {
        String value = queryByTitle(context, title);
        ContentValues values = new ContentValues();
        if (TextUtils.isEmpty(value)) {
            values.put(OtherData.Other.TITLE, title);
            values.put(OtherData.Other.CONTENT, content);
            // 插入
            context.getContentResolver().insert(OtherData.Other.CONTENT_URI, values);
        } else {
            //更新
            values.put(OtherData.Other.CONTENT, content);
            context.getContentResolver().update(OtherData.Other.CONTENT_URI, values, OtherData.Other.TITLE + "=?", new String[]{title});
        }
    }

    public static void delete(Context context, String title) {
        String value = queryByTitle(context, title);
        if (!TextUtils.isEmpty(value)) {
            context.getContentResolver().delete(OtherData.Other.CONTENT_URI, OtherData.Other.TITLE + "=?", new String[]{title});
        }
    }
}
