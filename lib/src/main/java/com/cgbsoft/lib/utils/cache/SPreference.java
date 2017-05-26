package com.cgbsoft.lib.utils.cache;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.cgbsoft.lib.base.model.UserInfoDataEntity;
import com.cgbsoft.lib.base.mvp.model.BaseResult;
import com.cgbsoft.lib.utils.constant.Constant;
import com.cgbsoft.lib.utils.tools.LogUtils;
import com.cgbsoft.lib.utils.tools.Utils;
import com.google.gson.Gson;

import static com.cgbsoft.lib.utils.cache.CPConstant.IS_PLAY_ADVISER_ANIM;
import static com.cgbsoft.lib.utils.cache.CPConstant.IS_PLAY_INVERSTOR_ANIM;
import static com.cgbsoft.lib.utils.cache.CPConstant.IS_VISABLE_MESSAGE_LOGIN;
import static com.cgbsoft.lib.utils.cache.CPConstant.IS_VISABLE_PROTOCOL_LOGIN;

/**
 * Created by xiaoyu.zhang on 2016/11/11 09:05
 * 不要用于跨进程使用，虽然也可以用，如果需要数据跨进程请使用OtherDataProvider
 */
public class SPreference implements Constant {
    private static SharedPreferences getBase(@NonNull Context context) {
        return context.getApplicationContext().getSharedPreferences(USER_INFO_BEAN_SP, Context.MODE_PRIVATE);
    }
    public static void putString(@NonNull Context context, @NonNull String key, @NonNull String value) {
        SharedPreferences.Editor edit = getBase(context).edit();
        edit.putString(key, value);
        edit.apply();
    }

    public static String getString(@NonNull Context context, @NonNull String key) {
        return getBase(context).getString(key, null);
    }

    public static void putInt(@NonNull Context context, @NonNull String key, @NonNull int value) {
        SharedPreferences.Editor edit = getBase(context).edit();
        edit.putInt(key, value);
        edit.apply();
    }

    public static int getInt(@NonNull Context context, @NonNull String key) {
        return getBase(context).getInt(key, -1);
    }


    public static void putBoolean(@NonNull Context context, @NonNull String key, @NonNull boolean value) {
        SharedPreferences.Editor edit = getBase(context).edit();
        edit.putBoolean(key, value);
        edit.apply();
    }

    public static boolean getBoolean(@NonNull Context context, @NonNull String key) {
        return getBase(context).getBoolean(key, false);
    }

    public static <T> void put(@NonNull Context context, @NonNull String key, @NonNull T value) {
        if (value instanceof String) {
            putString(context, key, (String) value);
        } else if (value instanceof Integer) {
            putInt(context, key, (Integer) value);
        } else if (value instanceof Boolean) {
            putBoolean(context, key, (Boolean) value);
        } else if (value instanceof BaseResult) {
            try {
                putString(context, key, new Gson().toJson(((BaseResult) value).result));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 用于判断应用后台到前台
     *
     * @param context context
     * @return boolean
     */
    public static boolean isCurrentRunningForeground(@NonNull Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext()).getBoolean(IS_CURRENTRUNNINGFOREGROUND, false);
    }

    /**
     * 是否播放了投资人的动画
     *
     * @param context
     * @return
     */
    public static boolean isPlayInverstorAnim(Context context) {
        return getBoolean(context, IS_PLAY_INVERSTOR_ANIM);
    }

    public static void savePlayInverstorAnim(Context context, boolean b) {
        putBoolean(context, IS_PLAY_INVERSTOR_ANIM, b);
    }

    public static boolean isVisableProtocol(Context context) {
        return getBoolean(context, IS_VISABLE_PROTOCOL_LOGIN);
    }

    public static void saveVisableProtocol(Context context) {
        putBoolean(context, IS_VISABLE_PROTOCOL_LOGIN, true);
    }

    public static boolean isVisableMessage(Context context) {
        return getBoolean(context, IS_VISABLE_MESSAGE_LOGIN);
    }

    public static void saveVisableMessage(Context context) {
        putBoolean(context, IS_VISABLE_MESSAGE_LOGIN, true);
    }

    /**
     * 是否播放了理财师的动画
     *
     * @param context
     * @return
     */
    public static boolean isPlayAdviserAnim(Context context) {
        return getBoolean(context, IS_PLAY_ADVISER_ANIM);
    }

    public static void savePlayAdviserAnim(Context context, boolean b) {
        putBoolean(context, IS_PLAY_ADVISER_ANIM, b);
    }

    /**
     * app是否升级了，通过appVersion判断
     *
     * @param context context
     * @param isSave  是否自动保存当前版本号
     * @return boolean
     */
    public static boolean isAppUpdate(@NonNull Context context, boolean isSave) {
        boolean b;
        int oldVersion = getInt(context, LAST_APP_VERSION);
        int nowVersion = Utils.getVersionCode(context.getApplicationContext());
        if (oldVersion < 0) {
            b = true;
        } else {
            b = oldVersion < nowVersion;
        }
        if (isSave)
            putInt(context, LAST_APP_VERSION, nowVersion);
        return b;
    }

    /**
     * 保存app是否升级信息，可以用于显示开机大图完成后
     *
     * @param context 上下文
     */
    public static void saveIsAppUpdate(@NonNull Context context) {
        putInt(context, LAST_APP_VERSION, Utils.getVersionCode(context.getApplicationContext()));
    }

    /**
     * 获取登录用户信息。
     *
     * @param context 上下文
     * @return 用户信息类
     */
    public static UserInfoDataEntity.UserInfo getUserInfoData(@NonNull Context context) {
        String userInfoDataJson = getBase(context).getString(USER_INFO_BEAN_PROPERTY, "");
        if (TextUtils.isEmpty(userInfoDataJson)) {
            return null;
        }
        return new Gson().fromJson(userInfoDataJson, UserInfoDataEntity.UserInfo.class);
    }

    public static UserInfoDataEntity.ToBBean getToBBean(@NonNull Context context) {
        UserInfoDataEntity.UserInfo userInfo = getUserInfoData(context);
        if (userInfo != null) {
            return userInfo.toB;
        }
        return null;
    }

    public static UserInfoDataEntity.ToCBean getToCBean(@NonNull Context context) {
        UserInfoDataEntity.UserInfo userInfo = getUserInfoData(context);
        if (userInfo != null) {
            return userInfo.toC;
        }
        return null;
    }

    public static String isColorCloud(@NonNull Context context) {
        String category = "0";
        UserInfoDataEntity.UserInfo userInfo = getUserInfoData(context);
        if (userInfo != null && userInfo.toB != null) {
            category = userInfo.toB.category;
        }
        return TextUtils.equals(category, "3") ? "1" : "0";
    }

    public static String getOrganizationName(@NonNull Context context) {
        String name = "";
        UserInfoDataEntity.UserInfo userInfo = getUserInfoData(context);
        if (userInfo != null && userInfo.toB != null) {
            name = userInfo.toB.organizationName;
        }
        return name;
    }

    /**
     * 保存用户信息
     *
     * @param context
     * @param json
     */
    public static void saveUserInfoData(@NonNull Context context, String json) {
        getBase(context).edit().putString(USER_INFO_BEAN_PROPERTY, json).apply();
        LogUtils.Log("ta","操作");
    }

    public static void saveUserInfoData(@NonNull Context context, UserInfoDataEntity.UserInfo userInfo) {
        if (userInfo == null) {
            getBase(context).edit().putString(USER_INFO_BEAN_PROPERTY, "").apply();
            return;
        }
        getBase(context).edit().putString(USER_INFO_BEAN_PROPERTY, new Gson().toJson(userInfo)).apply();
        LogUtils.Log("ta","操作");
    }


    public static void saveOpenJsonLog(Context context, boolean b) {
        putBoolean(context, "couldOpenJsonLog", b);
    }

    public static boolean getOpenJsonLog(Context context) {
        return getBoolean(context, "couldOpenJsonLog");
    }

    public static void saveHasPushMsg(Context context, boolean b) {
        putBoolean(context, HAS_PUSH_MESSAGE, b);
    }

    public static boolean getHasPushMsg(Context context) {
        return getBoolean(context, HAS_PUSH_MESSAGE);
    }

    public static boolean isThisRunOpenDownload(Context context) {
        boolean b = getBoolean(context, ISTHISRUN_OPENDOWNLOAD);
        saveThisRunOpenDownload(context, true);
        return b;
    }

    /**
     * 重置每次打开应用时显示下载框
     *
     * @param context
     * @param b
     */
    public static void saveThisRunOpenDownload(Context context, boolean b) {
        putBoolean(context, ISTHISRUN_OPENDOWNLOAD, b);
    }

    public static void saveFloatViewX(Context context, int x) {
        putInt(context, FLOAT_POSITION_X, x);
    }

    public static void saveFloatViewY(Context context, int y) {
        putInt(context, FLOAT_POSITION_Y, y);
    }

    public static int getFloatViewX(Context context) {
        return getInt(context, FLOAT_POSITION_X);
    }

    public static int getFloatViewY(Context context) {
        return getInt(context, FLOAT_POSITION_Y);
    }
}
