package com.cgbsoft.lib;

import android.content.Context;
import android.content.SharedPreferences;

import com.cgbsoft.lib.contant.AppinfConstant;

/**
 * desc  app的各种配置信息的存储 使用sp  eg 保存B/C的标识  ，application等
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 17/4/5-17:39
 */
public class AppInfStore implements AppinfConstant {


    /**
     * 保存是否是理财师端
     */
    public static void Save_IsAdviser(Context spContext, boolean IsAdviser) {
        SharedPreferences sp = spContext.getSharedPreferences(IsAdviser_Tage, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean("adviser", IsAdviser);
        ed.commit();
    }

    /**
     * 获取对否是理财师
     */
    public static boolean Get_IsAdviser(Context spContext) {
        SharedPreferences sp = spContext.getSharedPreferences(IsAdviser_Tage, Context.MODE_PRIVATE);
        return sp.getBoolean("adviser", false);
    }

    /**
     * 保存用户id
     */
    public static void saveUserId(Context scContext, String userid) {
        SharedPreferences sp = scContext.getSharedPreferences(USERIDSP, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString("userid",userid);
        ed.commit();

    }

    /**
     * 保存用户id
     */
    public static void saveUserToken(Context scContext, String usertoken) {
        SharedPreferences sp = scContext.getSharedPreferences(USERTOKENSP, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString("usertoken",usertoken);
        ed.commit();

    }


    /**
     * 用户是否登录
     */
    public static void saveIsLogin(Context scContext, boolean islogin) {
        SharedPreferences sp = scContext.getSharedPreferences(ISLOGIN, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean("islogin",islogin);
        ed.commit();

    }



    //保存数据
    public static void saveSousouHistory(Context pcContext,String str){
        SharedPreferences sp = pcContext.getSharedPreferences(SOUSOUHISTORY, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString("history", str);
        ed.commit();
    }
    //获取历史数据
    public static String getSousouHistory(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SOUSOUHISTORY, Context.MODE_PRIVATE);
        return sp.getString("history", "");
    }
}
