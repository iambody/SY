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
    /**
     * 获取是否是理财师的标识
     * @param PcContext
     * @return
     */
    public static boolean isAdViser(Context PcContext) {
        return AppInfStore.Get_IsAdviser(PcContext);
    }

    /**
     * 获取是否是理财师的标识
     *
     * @param PcContext
     * @return
     */
    public static boolean isInvestor(Context PcContext) {
        return !AppInfStore.Get_IsAdviser(PcContext);
    }

    /**
     * 获取用户id
     */
    public static String getUserId(Context context) {
        SharedPreferences sp = context.getSharedPreferences(USERIDSP, Context.MODE_PRIVATE);
        return sp.getString("userid", "");
    }

    /**
     * 获取用户token
     */
    public static String getUserToken(Context context) {
        SharedPreferences sp = context.getSharedPreferences(USERTOKENSP, Context.MODE_PRIVATE);
        return sp.getString("usertoken", "");
    }

    /**
     * 获取用户登录状态
     */
    public static boolean getIsLogin(Context context) {
        SharedPreferences sp = context.getSharedPreferences(ISLOGIN, Context.MODE_PRIVATE);
        return sp.getBoolean("islogin", false);
    }
}
