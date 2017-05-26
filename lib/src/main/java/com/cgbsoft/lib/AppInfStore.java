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

    private static SharedPreferences getBasePreference(Context context) {
        return context.getSharedPreferences(USER_SHARE_PREFERENCE_SET, Context.MODE_PRIVATE);
    }

    /**
     * 保存是否是理财师端
     */
    public static void saveAdvise(Context spContext, boolean IsAdviser) {
        SharedPreferences.Editor ed = getBasePreference(spContext).edit();
        ed.putBoolean(IsAdviser_Tage, IsAdviser);
        ed.commit();
    }

    /**
     * 保存用户id
     */
    public static void saveUserId(Context scContext, String userid) {
        SharedPreferences.Editor ed = getBasePreference(scContext).edit();
        ed.putString(USERIDSP, userid);
        ed.commit();
    }

    /**
     * 保存用户id
     */
    public static void saveUserToken(Context scContext, String usertoken) {
        SharedPreferences.Editor ed = getBasePreference(scContext).edit();
        ed.putString(USERTOKENSP, usertoken);
        ed.commit();
    }

    /**
     * 保存V2的混淆的Token
     */
    public static void saveUserV2Token(Context sContext, String v2Token) {
        SharedPreferences.Editor ed = getBasePreference(sContext).edit();
        ed.putString(USERV2TOKEN, v2Token);
        ed.commit();
    }

    /**
     * 保存用户账号
     */
    public static void saveUserAccount(Context scContext, String userAccount) {
        SharedPreferences.Editor ed = getBasePreference(scContext).edit();
        ed.putString(USERACCOUNT, userAccount);
        ed.commit();
    }

    /**
     * 用户是否登录
     */
    public static void saveIsLogin(Context scContext, boolean islogin) {
        SharedPreferences.Editor ed = getBasePreference(scContext).edit();
        ed.putBoolean(ISLOGIN, islogin);
        ed.commit();
    }

    /**
     * 保存融云token
     * @param context
     * @param rongYunToken
     */
    public static void saveRongToken(Context context, String rongYunToken) {
        SharedPreferences.Editor ed = getBasePreference(context).edit();
        ed.putString(RONGYUN_TOKEN, rongYunToken);
        ed.commit();
    }

    /**
     * 融云token有效期
     * @param context
     * @param rongYunTokenExpired
     */
    public static void saveRongTokenExpired(Context context, int rongYunTokenExpired) {
        SharedPreferences.Editor ed = getBasePreference(context).edit();
        ed.putInt(RONGYUN_TOKEN_EXPIRED, rongYunTokenExpired);
        ed.commit();
    }

    /**
     * 保存用户信息
     *
     * @param context
     * @param userInfo
     */
    public static void saveUserInfo(Context context, UserInfoDataEntity.UserInfo userInfo) {
        SPreference.saveUserInfoData(context, userInfo);
    }

    /**
     * 得到用户信息
     *
     * @param context
     * @return
     */
    public static UserInfoDataEntity.UserInfo getUserInfo(Context context) {
        return SPreference.getUserInfoData(context);
    }

    //保存数据
    public static void saveSousouHistory(Context pcContext, String str) {
        SharedPreferences.Editor ed = getBasePreference(pcContext).edit();
        ed.putString(SOUSOUHISTORY, str);
        ed.commit();
    }

    /**
     * 更新关联资产状态
     *
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
     *
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
     *
     * @param context
     * @param assetCertificationStatus
     */
    public static void updateUserAssetCertificationStatus(Context context, String assetCertificationStatus) {
        UserInfoDataEntity.UserInfo userInfo = SPreference.getUserInfoData(context);
        userInfo.getToC().setAssetsCertificationStatus(assetCertificationStatus);
        SPreference.saveUserInfoData(context, userInfo);
    }

    /**
     * 更新资产证明图片信息
     *
     * @param context
     * @param assetCertificationImageUrl
     */
    public static void updateUserAssetCertificationImageUrl(Context context, String assetCertificationImageUrl) {
        UserInfoDataEntity.UserInfo userInfo = SPreference.getUserInfoData(context);
        userInfo.getToC().setAssetsCertificationImage(assetCertificationImageUrl);
        SPreference.saveUserInfoData(context, userInfo);
    }

    /**
     * 更新资产证明状态
     *
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
     *
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
