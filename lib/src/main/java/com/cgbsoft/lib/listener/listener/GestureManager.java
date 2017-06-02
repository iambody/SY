package com.cgbsoft.lib.listener.listener;

import android.content.Context;

import com.cgbsoft.lib.AppInfStore;
import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.BaseApplication;
import com.cgbsoft.lib.base.model.UserInfoDataEntity;
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
     * @return
     */
    public static boolean intercepterGestureActivity(Context context, UserInfoDataEntity.UserInfo userInfo, boolean resetLogin) {
        if (userInfo == null) {
            return false;
        }

        if ((HAD_GESTRUE_PASSWORD.equals(userInfo.getToC().getGestureSwitch())) && (resetLogin || isLargeTime(context))) {
            NavigationUtils.startActivityByRouter(context, RouteConfig.VALIDATE_GESTURE_PASSWORD, "PARAM_FROM_LOGIN", true);
            return true;
        } else {
            if (!HAD_GESTRUE_PASSWORD.equals(userInfo.getToC().getGestureSwitch()) && AppManager.getUserFirstLogin(context)) {
                NavigationUtils.startActivityByRouter(context, RouteConfig.SET_GESTURE_PASSWORD, "PARAM_FROM_REGEIST_OR_LOGIN", true);
                AppInfStore.saveUserFirstLogin(context, false);
                return true;
            }
        }
        return false;
    }

    private static boolean isLargeTime(Context context) {
        long currentTime = System.currentTimeMillis();
        long preTime = AppManager.getLastExitTime(context);
        return (currentTime - preTime)/1000 > 120;
    }
}
