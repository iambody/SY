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
import com.cgbsoft.lib.R;

/**
 * @author chenlong
 *
 * 手势密码管理类
 */
public class GestureManager {

    public static final String HAD_GESTRUE_PASSWORD = "1";
    public static final String PARAM_FROM_GROUP_ASSERT = "PARAM_FROM_SHWO_GROUP_ASSERT";
    public static final String ASSERT_GROUP = "1";
    public static final String INVISTE_CARLENDAR = "2";
    public static final String DATUM_MANAGER = "3";

    public static void showAssertGestureManager(Context context) {
        UserInfoDataEntity.UserInfo userInfo = AppManager.getUserInfo(context);
        if (HAD_GESTRUE_PASSWORD.equals(userInfo.getToC().getGestureSwitch())) {
            if (isLargeTime(context)) {
                NavigationUtils.startActivityByRouter(context, RouteConfig.VALIDATE_GESTURE_PASSWORD, "PARAM_FROM_SHWO_ASSERT", true);
            } else {
                RxBus.get().post(RxConstant.SWITCH_ASSERT_SHOW, true);
            }
        } else {
            showSetGestureDialog(context, RxConstant.SWITCH_ASSERT_SHOW, "");
        }
    }

    private static void showSetGestureDialog(Context context, String rxConstant, String values) {
        new DefaultDialog(context, context.getString(R.string.gesture_new_no_dialog_desc), context.getString(R.string.gesture_new_no_set), context.getString(R.string.button_ok)) {
            @Override
            public void left() {
                this.dismiss();
                if (rxConstant.equals(RxConstant.SWITCH_ASSERT_SHOW)) {
                    RxBus.get().post(rxConstant, true);
                } else {
                    RxBus.get().post(RxConstant.SWITCH_GROUP_SHOW, values);
                }
            }

            @Override
            public void right() {
                this.dismiss();
                if (rxConstant.equals(RxConstant.SWITCH_ASSERT_SHOW)) {
                    NavigationUtils.startActivityByRouter(context, RouteConfig.SET_GESTURE_PASSWORD, "PARAM_FROM_SHWO_ASSERT", true);
                } else {
                    NavigationUtils.startActivityByRouter(context, RouteConfig.SET_GESTURE_PASSWORD, PARAM_FROM_GROUP_ASSERT, values);
                }
            }
        }.show();
    }

    public static void showGroupGestureManage(Context context, String values) {
        UserInfoDataEntity.UserInfo userInfo = AppManager.getUserInfo(context);
        if (HAD_GESTRUE_PASSWORD.equals(userInfo.getToC().getGestureSwitch())) {
            if (isLargeTime(context)) {
                NavigationUtils.startActivityByRouter(context, RouteConfig.VALIDATE_GESTURE_PASSWORD, PARAM_FROM_GROUP_ASSERT, values);
            } else {
                RxBus.get().post(RxConstant.SWITCH_GROUP_SHOW, values);
            }
        } else {
            showSetGestureDialog(context, RxConstant.SWITCH_GROUP_SHOW, values);
        }
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
            if (!HAD_GESTRUE_PASSWORD.equals(userInfo.getToC().getGestureSwitch()) && (AppManager.getUserFirstLogin(context) || resetLogin)) {
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
        return (currentTime - preTime)/1000 > 120;
    }
}
