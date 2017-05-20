package com.cgbsoft.lib;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.cgbsoft.lib.base.model.UserInfoDataEntity;
import com.cgbsoft.lib.contant.AppinfConstant;
import com.cgbsoft.lib.utils.cache.SPreference;

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
        ed.putString("userid", userid);
        ed.commit();
    }

    /**
     * 保存用户id
     */
    public static void saveUserToken(Context scContext, String usertoken) {
        SharedPreferences sp = scContext.getSharedPreferences(USERTOKENSP, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString("usertoken", usertoken);
        ed.commit();
    }

    /**
     * 用户是否登录
     */
    public static void saveIsLogin(Context scContext, boolean islogin) {
        SharedPreferences sp = scContext.getSharedPreferences(ISLOGIN, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean("islogin", islogin);
        ed.commit();

    }

    //保存数据
    public static void saveSousouHistory(Context pcContext, String str) {
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

    /**
     * 更新关联资产状态
     * @param context
     * @param stockAssetStaus
     */
    public static void updateUserStockAssetsStatus(Context context, String stockAssetStaus) {
        UserInfoDataEntity.UserInfo userInfo = SPreference.getUserInfoData(context);
        userInfo.getToC().setStockAssetsStatus(stockAssetStaus);
        SPreference.saveUserInfoData(context, userInfo);
    }

    /**
     * 更新关联资产图片信息
     * @param context
     * @param imageUrl
     */
    public static void updateUserStockAssetsImageUrl(Context context, String imageUrl) {
        UserInfoDataEntity.UserInfo userInfo = SPreference.getUserInfoData(context);
        userInfo.getToC().setStockAssetsImage(imageUrl);
        SPreference.saveUserInfoData(context, userInfo);
    }

    /**
     * 更新资产证明状态
     * @param context
     * @param assetCertificationStatus
     */
    public static void updateUserAssetCertificationStatus(Context context, String assetCertificationStatus) {
        UserInfoDataEntity.UserInfo userInfo = SPreference.getUserInfoData(context);
        userInfo.getToC().setAssetsCertificationStatus(assetCertificationStatus);
        SPreference.saveUserInfoData(context, userInfo);
    }

    /**
     * 更新资产证明状态
     * @param context
     * @param investmentType
     */
    public static void updateUserInvestentType(Context context, String investmentType) {
        UserInfoDataEntity.UserInfo userInfo = SPreference.getUserInfoData(context);
        userInfo.getToC().setInvestmentType(investmentType);
        SPreference.saveUserInfoData(context, userInfo);
    }

    /**
     * 更新手势密码值
     * @param context
     * @param getsturePassword
     */
    public static void updateUserGesturePassword(Context context, String getsturePassword) {
        UserInfoDataEntity.UserInfo userInfo = SPreference.getUserInfoData(context);
        userInfo.getToC().setGesturePassword(getsturePassword);
        userInfo.getToC().setGestureSwitch(TextUtils.isEmpty(getsturePassword) ? "2" : "1");
        SPreference.saveUserInfoData(context, userInfo);
    }
}
