package com.cgbsoft.lib.utils.cache;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.cgbsoft.lib.base.model.UserInfoDataEntity;
import com.cgbsoft.lib.base.mvp.model.BaseResult;
import com.cgbsoft.lib.utils.constant.Constant;
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
    /*public static void toDataMigration(@NonNull Context context) {
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

        if (!TextUtils.isEmpty(json))
            UserDataProvider.saveUserInfo(c, json);
        if (!TextUtils.isEmpty(hasUserInfo))
            UserDataProvider.saveLoginFlag(c, TextUtils.equals("1", hasUserInfo));
        if (!TextUtils.isEmpty(token))
            UserDataProvider.saveToken(c, token);
        if (TextUtils.equals("IdentityLicaishi", identify)) {
            OtherDataProvider.saveIdentify(c, 1);
        } else if (TextUtils.equals("IdentityTouziren", identify)) {
            OtherDataProvider.saveIdentify(c, 2);
        }
    }*/

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
     * 获取登陆用户信息。
     *
     * @param context 上下文
     * @return 用户信息类
     */
    public static UserInfoDataEntity.UserInfo getUserInfoData(@NonNull Context context) {
        String userInfoDataJson = UserDataProvider.queryUserInfoData(context);
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
        UserDataProvider.saveUserInfo(context, json);
    }

    /**
     * 清理用户信息
     *
     * @param context 上下文
     */
    public static void quitLogin(@NonNull Context context) {
        UserDataProvider.quitLogin(context);
    }

    /**
     * 获取用户id
     *
     * @param context 上下文
     * @return 用户id
     */
    public static String getUserId(@NonNull Context context) {
        String userId = UserDataProvider.getUserId(context);
        if (!TextUtils.isEmpty(userId)) {
            return userId;
        }
        UserInfoDataEntity.UserInfo userInfoData = getUserInfoData(context);
        if (userInfoData != null) {
            return userInfoData.id;
        }
        return "";
    }

    /**
     * 保存登陆用户token信息。
     *
     * @param context 上下文
     * @param token   token
     */
    public static void saveToken(@NonNull Context context, @NonNull String token) {
        UserDataProvider.saveToken(context, token);
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

    public static void saveUserId(@NonNull Context context, @NonNull String uid) {
        UserDataProvider.saveUserId(context, uid);
    }

    /**
     * 保存登陆状态
     *
     * @param context 上下文
     * @param flag    是否成功
     */
    public static void saveLoginFlag(@NonNull Context context, boolean flag) {
        UserDataProvider.saveLoginFlag(context, flag);
    }

    /**
     * 是否登陆
     *
     * @param context 上下文
     * @return 登陆状态
     */
    public static boolean isLogin(@NonNull Context context) {
        return UserDataProvider.queryLoginFlag(context);
    }

    /**
     * 获取身份 1理财师  2投资人
     *
     * @param context 上下文
     * @return true 理财师
     */
    public static int getIdtentify(@NonNull Context context) {
        return OtherDataProvider.getIdentify(context);
    }

    /**
     * 保存身份信息
     *
     * @param context 上下文
     * @param value   1理财师，2投资人
     */
    public static void saveIdtentify(@NonNull Context context, int value) {
        OtherDataProvider.saveIdentify(context, value);
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
}
