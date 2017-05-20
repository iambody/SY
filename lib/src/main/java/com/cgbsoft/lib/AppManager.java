package com.cgbsoft.lib;

import android.content.Context;
import android.content.SharedPreferences;

import com.cgbsoft.lib.contant.AppinfConstant;

/**
 * desc  进行不同application的管理 注意 防止误操作无修改 此处只能取状态不能村状态
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 17/4/5-17:32
 */
public class AppManager implements AppinfConstant {

    private static SharedPreferences getBasePreference(Context context) {
        return context.getSharedPreferences(USER_SHARE_PREFERENCE_SET, Context.MODE_PRIVATE);
    }
    /**
     * 获取是否是理财师的标识
     * @param context
     * @return
     */
    public static boolean isAdViser(Context context) {
        return getBasePreference(context).getBoolean(IsAdviser_Tage, false);
    }

    /**
     * 获取是否是理财师的标识
     *
     * @param context
     * @return
     */
    public static boolean isInvestor(Context context) {
        return !getBasePreference(context).getBoolean(IsAdviser_Tage, false);
    }

    /**
     * 获取用户id
     */
    public static String getUserId(Context context) {
        return getBasePreference(context).getString(USERIDSP, "");
    }

    /**
     * 获取用户token
     */
    public static String getUserToken(Context context) {
        return getBasePreference(context).getString(USERTOKENSP, "");
    }

    /**
     * 获取用户登录状态
     */
    public static boolean getIsLogin(Context context) {
        return getBasePreference(context).getBoolean(ISLOGIN, false);
    }

    //获取历史数据
    public static String getSousouHistory(Context context) {
        return getBasePreference(context).getString(SOUSOUHISTORY, "");
    }
}
