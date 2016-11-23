package com.cgbsoft.lib.utils.cache;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;

/**
 * 缓存目录控制
 * Created by xiaoyu.zhang on 2016/11/10 14:57
 *  
 */
public class CacheManager {
    public static final int DEFAULT = 0x0000;
    public static final int APK = 0x00100;
    public static final int VOICE = 0x00200;
    public static final int VIDEO = 0x00300;
    public static final int PIC = 0x00400;
    public static final int DB = 0x00500;
    public static final int CONFIG = 0x00600;
    public static final int CACHE = 0x00700;
    public static final int USED = 0x00800;
    public static final int GIFT = 0x00900;
    public static final int GLIDE = 0x01000;
    public static final int CRASH = 0x01100;
    public static final int SMILEY = 0x01200;
    public static final int FILE = 0x01300;

    private static final String NOMEDIA = ".nomedia";

    /**
     * 获取缓存路径
     */
    public static String getCachePath(Context context) {
        String savePath = getSaveFilePath() + File.separator + context.getPackageName() + File.separator + "cache";
        File fDir = new File(savePath);
        if (!fDir.exists()) {
            fDir.mkdirs();
        }
        return savePath;
    }

    public static String getRelativePath(Context context) {
        String savePath = getSaveFilePath() + File.separator + context.getPackageName() + File.separator + "cache";
        String relatviePath = File.separator + context.getPackageName() + File.separator + "cache";
        File fDir = new File(savePath);
        if (!fDir.exists()) {
            fDir.mkdirs();
        }

        return relatviePath;
    }

    public static String getCachePath() {
        String savePath = getSaveFilePath() + File.separator + "com.cgbsoft.privatefund" + File.separator + "cache";
        File fDir = new File(savePath);
        if (!fDir.exists()) {
            fDir.mkdirs();
        }
        return savePath;
    }

    public static String getRelativePath() {
        String savePath = getSaveFilePath() + File.separator + "com.cgbsoft.privatefund" + File.separator + "cache";
        String relatviePath = File.separator + "com.cgbsoft.privatefund" + File.separator + "cache";
        File fDir = new File(savePath);
        if (!fDir.exists()) {
            fDir.mkdirs();
        }
        return relatviePath;
    }

    public static String getCachePath(Context context, int which) {
        String savePath = "";
        if (context == null) {
            savePath = getCachePath();
        } else {
            savePath = getCachePath(context);
        }
        savePath += File.separator;
        if (which == APK) {
            savePath += "apk";
        } else if (which == VOICE) {
            savePath += "voice";
        } else if (which == VIDEO) {
            savePath += "video";
        } else if (which == PIC) {
            savePath += "picture";
        } else if (which == DB) {
            savePath += "database";
        } else if (which == CONFIG) {
            savePath += "config";
        } else if (which == CACHE) {
            savePath += "httpCache";
        } else if (which == USED) {
            savePath += "used";
        } else if (which == GIFT) {
            savePath += "gift";
        } else if (which == GLIDE) {
            savePath += "glide";
        } else if (which == CRASH) {
            savePath += "crash";
        } else if (which == SMILEY) {
            savePath += "smiley";
        }else if(which == FILE){
            savePath += "file";
        }
        savePath += File.separator;
        String nomediaPath = savePath + NOMEDIA;

        File fDir = new File(savePath);
        if (!fDir.exists()) {
            fDir.mkdirs();
        }
        File npDir = new File(nomediaPath);
        if (!npDir.exists()) {
            npDir.mkdir();
        }
        return savePath;
    }

    public static String getRelativePath(Context context, int which) {
        String savePath = "";
        if (context == null) {
            savePath = getRelativePath();
        } else {
            savePath = getRelativePath(context);
        }
        savePath += File.separator;
        if (which == APK) {
            savePath += "apk";
        } else if (which == VOICE) {
            savePath += "voice";
        } else if (which == VIDEO) {
            savePath += "video";
        } else if (which == PIC) {
            savePath += "picture";
        } else if (which == DB) {
            savePath += "database";
        } else if (which == CONFIG) {
            savePath += "config";
        } else if (which == CACHE) {
            savePath += "httpCache";
        } else if (which == USED) {
            savePath += "used";
        } else if (which == GIFT) {
            savePath += "gift";
        } else if (which == GLIDE) {
            savePath += "glide";
        } else if (which == CRASH) {
            savePath += "crash";
        }
        savePath += File.separator;

        File fDir = new File(savePath);
        if (!fDir.exists()) {
            fDir.mkdirs();
        }
        return savePath;
    }

    private static String getAppCacheRoot(Context context) {
        String status = Environment.getExternalStorageState();
        if (TextUtils.equals(status, Environment.MEDIA_MOUNTED)) {
            return context.getExternalFilesDir("").getAbsolutePath();
        } else {
            return getSaveFilePath();
        }
    }

    /**
     * 生成下载文件保存路径
     *
     * @return
     */
    public static String getSaveFilePath() {
        File file = null;
        String rootPath = "";
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            file = Environment.getExternalStorageDirectory();//获取跟目录
            rootPath = file.getPath();
        } else {
            Dev_MountInfo dev = Dev_MountInfo.getInstance();
            Dev_MountInfo.DevInfo info = dev.getInternalInfo();
            if (info != null) {
                rootPath = info.getPath();
            } else {
                return "";
            }
        }
        return rootPath;
    }
}
