package com.cgbsoft.lib;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.cgbsoft.lib.base.model.HomeEntity;
import com.cgbsoft.lib.base.model.UserInfoDataEntity;
import com.cgbsoft.lib.contant.AppinfConstant;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.utils.tools.BStrUtils;
import com.cgbsoft.privatefund.bean.location.LocationBean;

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
     *
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
     * 保存用户账号
     */
    public static String getUserAccount(Context scContext) {
        return getBasePreference(scContext).getString(USERACCOUNT, "");
    }

//    /**
//     * 获取用户 的翻转V2TOKEN
//     */
//    public static String getUserV2Token(Context context) {
//        return getBasePreference(context).getString(USERV2TOKEN, "");
//    }

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
     * 获取聊天页面名称－－产品分享需要用到
     *
     * @param context
     * @return
     */
    public static String getChatName(Context context) {
        return getBasePreference(context).getString(CHAT_NAME, "");
    }

    /**
     * 获取聊天类型－－产品分享需要用到
     *
     * @param context
     * @return
     */
    public static String getConversationType(Context context) {
        return getBasePreference(context).getString(CONVERSATION_TYPE, "");
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

    /**
     * 获取融云Token
     *
     * @param context
     * @return
     */
    public static String getRongToken(Context context) {
        return getBasePreference(context).getString(RONGYUN_TOKEN, "");
    }

    /**
     * 判断用户是否首次登录
     *
     * @param context
     */
    public static boolean getUserFirstLogin(Context context) {
        return getBasePreference(context).getBoolean(USER_FIRST_LOGIN.concat(getUserId(context)), true);
    }

    /**
     * 获取融云token有效期
     *
     * @param context
     * @return
     */
    public static int getRongTokenExpired(Context context) {
        return getBasePreference(context).getInt(RONGYUN_TOKEN_EXPIRED, 0);
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

    /**
     * 获取机构经理ID
     *
     * @param context
     * @return
     */
    public static String getOrgManagerUid(Context context) {
        return getBasePreference(context).getString(ORG_MANAGER_UID, "");
    }

    /**
     * 获取机构经理手机号
     *
     * @param context
     * @return
     */
    public static String getOrgManagerMobile(Context context) {
        return getBasePreference(context).getString(ORG_MANAGER_MOBILE, "");
    }

    /**
     * 获取团队长ID
     *
     * @param context
     * @return
     */
    public static String getTeamManagerUid(Context context) {
        return getBasePreference(context).getString(TEAM_MANAGER_UID, "");
    }

    /**
     * 是否有团队长
     *
     * @param context
     * @return
     */
    public static boolean hasTeamManager(Context context) {
        return getBasePreference(context).getBoolean(HAS_TEAM_MANAGER, false);
    }

    /**
     * 是否有机构经理
     *
     * @param context
     * @return
     */
    public static boolean hasOrgManager(Context context) {
        return getBasePreference(context).getBoolean(HAS_ORG_MANAGER, false);
    }

    /**
     * 是否有用户所属群
     *
     * @param context
     * @return
     */
    public static boolean hasUserGroup(Context context) {
        return getBasePreference(context).getBoolean(HAS_USER_GROUP, false);
    }

    /**
     * 得到最后一次验证或者设置手势密码时间
     *
     * @param context
     * @return
     */
    public static long getLastSetOrValidateTime(Context context) {
        return getBasePreference(context).getLong(LAST_SET_VALIDATE_TIME, 0);
    }

    /**
     * 获取保存的登录公钥
     */
    public static String getPublicKey(Context context) {
        return getBasePreference(context).getString(PUBLIC_KEY, "");
    }

    /**
     * 获取定位信息
     */
    public static LocationBean getLocation(Context context) {
        return AppInfStore.getLocationInf(context.getApplicationContext());
    }

    /**
     * 获取是否是游客模式
     */
    public static boolean isVisitor(Context context) {
        return getBasePreference(context.getApplicationContext()).getBoolean(VISITOR_KEY, false);
    }

    /**
     * 获取手势密码是否打开
     *
     * @param context
     * @return
     */
    public static boolean getGestureFlag(Context context) {
        UserInfoDataEntity.UserInfo userInfo = SPreference.getUserInfoData(context);
        String gestureSwitch = userInfo.getToC().getGestureSwitch();
        return !TextUtils.isEmpty(gestureSwitch) && gestureSwitch.equals("1") ? true : false;
    }

    /**
     * 获取是否绑定过理财师
     */
    public static boolean isBindAdviser(Context context) {
        return !BStrUtils.isEmpty(AppManager.getUserInfo(context).getToC().getBandingAdviserId());
    }

    /**
     * 获取首页数据 ==》如果缓存不存在就需要进行
     */
    public static HomeEntity.Result getHomeCache(Context pContext) {
        return AppInfStore.getHomeData(pContext);
    }

}
