package com.cgbsoft.lib;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * desc  app的各种配置信息的存储 使用sp  eg 保存B/C的标识  ，application等
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 17/4/5-17:39
 */
public class AppInfStore {
    /**
     * 存储理财师标识的sp的名字
     */
    private static final String IsAdviser_Tage="isadviser_tag";

    /**
     * 存储用户信息
     */
    private static final String User_Tage="user_tag";

    /**
     * 保存是否是理财师端
     */
    public static void Save_IsAdviser(Context spContext,boolean IsAdviser ){
        SharedPreferences sp=spContext.getSharedPreferences(IsAdviser_Tage,Context.MODE_PRIVATE);
        SharedPreferences.Editor ed=sp.edit();
        ed.putBoolean("adviser",IsAdviser);
        ed.commit();
    }
    /**
     * 获取对否是理财师
     */
    public static boolean Get_IsAdviser(Context spContext){
        SharedPreferences sp=spContext.getSharedPreferences(IsAdviser_Tage,Context.MODE_PRIVATE);
       return  sp.getBoolean("adviser",false);
    }





}
