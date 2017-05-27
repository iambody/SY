package com.cgbsoft.lib.listener.listener;

import android.content.Context;
import android.content.Intent;

import com.cgbsoft.lib.AppInfStore;
import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.BaseApplication;
import com.cgbsoft.lib.base.model.UserInfoDataEntity;
import com.cgbsoft.lib.base.model.bean.UserInfo;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.utils.tools.BackgroundManager;
import com.cgbsoft.lib.utils.tools.NavigationUtils;

import java.util.Calendar;

/**
 * @author chenlong
 *
 * 手势密码管理类
 */
public class GestureManager {

    public static final String HAD_GESTRUE_PASSWORD = "1";
    /**
     * 手势页面拦截器
     * @param context
     * @param userInfo
     * @param isSameAccount
     * @return
     */
    public static boolean intercepterGestureActivity(Context context, UserInfoDataEntity.UserInfo userInfo, boolean isSameAccount) {
        if (userInfo == null) {
            return false;
        }
        BackgroundManager backgroundManager = ((BaseApplication)BaseApplication.getContext()).getBackgroundManager();
        Calendar expireDate = Calendar.getInstance();
        expireDate.add(BackgroundManager.EXPIRE_DAN_WEI, BackgroundManager.EXPIRE_IN_SECOND);
        backgroundManager.setExitCalendar(expireDate);
        Intent intent;
        if (("1".equals(userInfo.getToC().getGestureSwitch())) && ((isSameAccount && Calendar.getInstance().after(backgroundManager.getExitCalendar())) || !isSameAccount)) {
            NavigationUtils.startActivityByRouter(context, RouteConfig.VALIDATE_GESTURE_PASSWORD, "PARAM_FROM_LOGIN", true);
            return true;
        } else {
            if (!"1".equals(userInfo.getToC().getGestureSwitch()) && AppManager.getUserFirstLogin(context)) {
                NavigationUtils.startActivityByRouter(context, RouteConfig.SET_GESTURE_PASSWORD, "PARAM_FROM_REGEIST_OR_LOGIN", true);
                AppInfStore.saveUserFirstLogin(context, true);
                return true;
            }
        }
        return false;
    }
}
