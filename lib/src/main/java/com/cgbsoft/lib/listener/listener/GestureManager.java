package com.cgbsoft.lib.listener.listener;

import android.content.Context;

import com.cgbsoft.lib.AppInfStore;
import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.base.model.UserInfoDataEntity;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.utils.constant.RxConstant;
import com.cgbsoft.lib.utils.rxjava.RxBus;
import com.cgbsoft.lib.utils.tools.NavigationUtils;
import com.cgbsoft.lib.widget.dialog.DefaultDialog;

/**
 * @author chenlong
 *
 * 手势密码管理类
 */
public class GestureManager {
    public static final String HAD_GESTRUE_PASSWORD = "1";
    public static void showAssertGestureManager(Context context) {
        UserInfoDataEntity.UserInfo userInfo = AppManager.getUserInfo(context);
        if (HAD_GESTRUE_PASSWORD.equals(userInfo.getToC().getGestureSwitch())) {
            if (isLargeTime(context)) {
                NavigationUtils.startActivityByRouter(context, RouteConfig.VALIDATE_GESTURE_PASSWORD, "PARAM_FROM_SHWO_ASSERT", true);
            } else {
                RxBus.get().post(RxConstant.SWITCH_ASSERT_SHOW, true);
            }
        } else {
            showSetGestureDialog(context);
        }
    }

    private static void showSetGestureDialog(Context context) {
        new DefaultDialog(context, "为保障您的私密信息安全，建议您设置手势密码。若您当前暂不想设置，随后可在我的－设置－开启手势密码", "暂不设置", "确定") {
            @Override
            public void left() {
                this.dismiss();
                RxBus.get().post(RxConstant.SWITCH_ASSERT_SHOW, true);
            }

            @Override
            public void right() {
                this.dismiss();
                NavigationUtils.startActivityByRouter(context, RouteConfig.SET_GESTURE_PASSWORD, "PARAM_FROM_SHWO_ASSERT", true);
            }
        }.show();
    }

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
        boolean isLargeTime = isLargeTime(context);
        if ((HAD_GESTRUE_PASSWORD.equals(userInfo.getToC().getGestureSwitch())) && (resetLogin || isLargeTime)) {
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
        long preTime = AppManager.getLastSetOrValidateTime(context);
        System.out.println("-------time=" + (currentTime - preTime));
        return (currentTime - preTime)/1000 > 120;
    }
}
