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

//    /**
//     * 保存V2的混淆的Token
//     */
//    public static void saveUserV2Token(Context sContext, String v2Token) {
//        SharedPreferences.Editor ed = getBasePreference(sContext).edit();
//        ed.putString(USERV2TOKEN, v2Token);
//        ed.commit();
//    }

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
     *
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
     *
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
     * 保存机构经理ID
     *
     * @param context
     * @return
     */
    public static void saveOrgManagerUid(Context context, String uid) {
        SharedPreferences.Editor ed = getBasePreference(context).edit();
        ed.putString(ORG_MANAGER_UID, uid);
        ed.commit();
    }

    /**
     * 保存机构经理手机号
     *
     * @param context
     * @return
     */
    public static void saveOrgManagerMobile(Context context, String mobileNumber) {
        SharedPreferences.Editor ed = getBasePreference(context).edit();
        ed.putString(ORG_MANAGER_MOBILE, mobileNumber);
        ed.commit();
    }

    /**
     * 保存团队长ID
     *
     * @param context
     * @return
     */
    public static void saveTeamManagerUid(Context context, String teamUid) {
        SharedPreferences.Editor ed = getBasePreference(context).edit();
        ed.putString(TEAM_MANAGER_UID, teamUid);
        ed.commit();
    }

    /**
     * 保存是否有团队长
     *
     * @param context
     * @return
     */
    public static void saveHasTeamManager(Context context, boolean hasTeamManager) {
        SharedPreferences.Editor ed = getBasePreference(context).edit();
        ed.putBoolean(HAS_TEAM_MANAGER, hasTeamManager);
        ed.commit();
    }

    /**
     * 保存是否有机构经理
     *
     * @param context
     * @return
     */
    public static void saveHasOrgManager(Context context, boolean hasOrgManager) {
        SharedPreferences.Editor ed = getBasePreference(context).edit();
        ed.putBoolean(HAS_ORG_MANAGER, hasOrgManager);
        ed.commit();
    }

    /**
     * 保存聊天名称 －－产品分享需要用到
     *
     * @param context
     * @param chatName
     */
    public static void saveChatName(Context context, String chatName) {
        SharedPreferences.Editor ed = getBasePreference(context).edit();
        ed.putString(CHAT_NAME, chatName);
        ed.commit();
    }

    /**
     * 保存聊天名称 －－产品分享需要用到
     *
     * @param context
     * @param conversationName
     */
    public static void saveConversation(Context context, String conversationName) {
        SharedPreferences.Editor ed = getBasePreference(context).edit();
        ed.putString(CONVERSATION_TYPE, conversationName);
        ed.commit();
    }

    /**
     * 保存用户是否首次登录，用用户id区别
     *
     * @param context
     */
    public static void saveUserFirstLogin(Context context, boolean isLogin) {
        SharedPreferences.Editor ed = getBasePreference(context).edit();
        ed.putBoolean(USER_FIRST_LOGIN.concat(AppManager.getUserId(context)), isLogin);
        ed.commit();
    }

    /**
     * 保存是否有用户所属群
     *
     * @param context
     * @return
     */
    public static void saveHasUserGroup(Context context, boolean hasGroup) {
        SharedPreferences.Editor ed = getBasePreference(context).edit();
        ed.putBoolean(HAS_USER_GROUP, hasGroup);
        ed.commit();
    }

    //保存数据
    public static void saveSousouHistory(Context pcContext, String str) {
        SharedPreferences.Editor ed = getBasePreference(pcContext).edit();
        ed.putString(SOUSOUHISTORY, str);
        ed.commit();
    }

    //最后退出时间
    public static void saveLastExitTime(Context pcContext, long time) {
        SharedPreferences.Editor ed = getBasePreference(pcContext).edit();
        ed.putLong(LAST_EXIT_BACK_TIME, time);
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


    /**
     * 保存获取的公钥
     */
    public static void savePublicKey(Context context, String publicKey) {
        SharedPreferences.Editor ed = getBasePreference(context).edit();
        ed.putString(PUBLIC_KEY, publicKey);
        ed.commit();
    }

}
