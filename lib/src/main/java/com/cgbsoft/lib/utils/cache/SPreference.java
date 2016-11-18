package com.cgbsoft.lib.utils.cache;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Base64;

import com.cgbsoft.lib.base.model.bean.UserInfo;
import com.cgbsoft.lib.base.mvp.model.BaseResult;
import com.cgbsoft.lib.utils.constant.Constant;
import com.cgbsoft.lib.utils.tools.Base64Util;
import com.cgbsoft.lib.utils.tools.Utils;
import com.google.gson.Gson;

import static com.cgbsoft.lib.utils.cache.CPConstant.IS_PLAY_ADVISER_ANIM;
import static com.cgbsoft.lib.utils.cache.CPConstant.IS_PLAY_INVERSTOR_ANIM;

/**
 * Created by xiaoyu.zhang on 2016/11/11 09:05
 * 不要用于跨进程使用，虽然也可以用，如果需要数据跨进程请使用OtherDataProvider
 */
public class SPreference implements Constant {
    private static SharedPreferences getBase(@NonNull Context context) {
        return context.getApplicationContext().getSharedPreferences(USER_SETTING, Context.MODE_PRIVATE);
    }

    private static SharedPreferences getBase(@NonNull Context context, @NonNull String key) {
        return context.getApplicationContext().getSharedPreferences(key, Context.MODE_PRIVATE);
    }

    private static void putString(@NonNull Context context, @NonNull String key, @NonNull String value) {
        SharedPreferences.Editor edit = getBase(context).edit();
        edit.putString(key, value);
        edit.apply();
    }

    private static String getString(@NonNull Context context, @NonNull String key) {
        return getBase(context).getString(key, null);
    }

    private static void putInt(@NonNull Context context, @NonNull String key, @NonNull int value) {
        SharedPreferences.Editor edit = getBase(context).edit();
        edit.putInt(key, value);
        edit.apply();
    }

    /**
     * 默认值为-1
     *
     * @param context
     * @param key
     * @return
     */
    private static int getInt(@NonNull Context context, @NonNull String key) {
        return getBase(context).getInt(key, -1);
    }

    private static void putBoolean(@NonNull Context context, @NonNull String key, @NonNull boolean value) {
        SharedPreferences.Editor edit = getBase(context).edit();
        edit.putBoolean(key, value);
        edit.apply();
    }

    private static boolean getBoolean(@NonNull Context context, @NonNull String key) {
        return getBase(context).getBoolean(key, false);
    }

    private static <T> void put(@NonNull Context context, @NonNull String key, @NonNull T value) {
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
     * 对原来保存在sp里的数据进行迁移
     */
    public static void toDataMigration(@NonNull Context context) {
        Context c = context.getApplicationContext();
        SharedPreferences old1Sp = getBase(c, "simuyun");
        // 1理财师  2投资人
        String identify = old1Sp.getString("identify", "");
        //是否登陆
        String hasUserInfo = old1Sp.getString("hasUserInfo", "");
        //token
        String token = old1Sp.getString("token", "");

        SharedPreferences old2Sp = getBase(c, "saveBean.xml");
        String userBase64 = old2Sp.getString("userInfo", "");
        com.cgbsoft.privatefund.bean.UserInfo userInfo = Base64Util.getEntityByOIS(userBase64);
        String json = new Gson().toJson(userInfo);
        UserInfo ui = new Gson().fromJson(json, UserInfo.class);
        String ui64 = Base64Util.toBase64(userInfo, Base64.DEFAULT);

        if (!TextUtils.isEmpty(hasUserInfo)) {
            UserDataProvider.insertUserInfo(c, hasUserInfo, ui64, token);

            if (!TextUtils.isEmpty(ui64))
                UserDataProvider.updateUserInfoData(c, ui64);
            if (!TextUtils.isEmpty(hasUserInfo))
                UserDataProvider.updateLoginFlag(c, TextUtils.equals("1", hasUserInfo));
            if (!TextUtils.isEmpty(token))
                UserDataProvider.updateToken(c, token);
            if (TextUtils.equals("IdentityLicaishi", identify)) {
                OtherDataProvider.saveIdentify(c, 1);
            } else if (TextUtils.equals("IdentityTouziren", identify)) {
                OtherDataProvider.saveIdentify(c, 2);
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
        return PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext())
                .getBoolean(IS_CURRENTRUNNINGFOREGROUND, false);
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
     * 获取登陆用户信息。
     *
     * @param context 上下文
     * @return 用户信息类
     */
    public static UserInfo getUserInfoData(@NonNull Context context) {
        String userInfoDataJson = UserDataProvider.queryUserInfoData(context);
        if (TextUtils.isEmpty(userInfoDataJson)) {
            return null;
        }
        return new Gson().fromJson(userInfoDataJson, UserInfo.class);
    }

    /**
     * 清理用户信息
     *
     * @param context 上下文
     */
    public static void clearUserInfoData(@NonNull Context context) {
        UserDataProvider.del(context);
    }

    /**
     * 获取用户id
     *
     * @param context 上下文
     * @return 用户id
     */
    public static String getUserId(@NonNull Context context) {
        UserInfo userInfoData = getUserInfoData(context);
        if (userInfoData != null) {
            return userInfoData.getId();
        }
        return "";
    }

    /**
     * 保存登陆用户token信息。
     *
     * @param context 上下文
     * @param token   token
     * @return 是否保存成功
     */
    public static boolean saveToken(@NonNull Context context, @NonNull String token) {
        return UserDataProvider.updateToken(context, token);
    }

    /**
     * 获取登陆用户token信息。
     *
     * @param context 上下文
     * @return 用户token
     */
    public static String getToken(@NonNull Context context) {
        return UserDataProvider.queryToken(context);
    }

    /**
     * 保存登陆状态
     *
     * @param context 上下文
     * @param flag    是否成功
     * @return 是否保存成功
     */
    public static boolean saveLoginFlag(@NonNull Context context, boolean flag) {
        return UserDataProvider.updateLoginFlag(context, flag);
    }

    /**
     * 是否登陆
     *
     * @param context 上下文
     * @return 登陆状态
     */
    public static boolean isLogin(@NonNull Context context) {
        return UserDataProvider.queryUserInfoData(context) != null;
    }

    /**
     * 获取身份 1理财师  2投资人
     *
     * @param context 上下文
     * @return true 理财师
     */
    public static boolean isIdtentifyAdviser(@NonNull Context context) {
        return OtherDataProvider.getIdentify(context) == IDS_ADVISER;
    }

    /**
     * 保存身份信息
     *
     * @param context 上下文
     * @param value   1理财师，2投资人
     * @return 是否保存成功
     */
    public static void saveIdtentify(@NonNull Context context, int value) {
        OtherDataProvider.saveIdentify(context, value);
    }
}
