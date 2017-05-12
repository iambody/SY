package com.cgbsoft.lib.utils.tools;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import java.util.HashMap;
import java.util.List;

/**
 * desc  activity等ui跳转的工具类
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 17/4/7-18:39
 */
public class UiSkipUtils {
    /**
     *
     * @Description: 隐式启动,跳转
     * @param packageContext
     * @param action
     *            含操作的Intent
     */
    public static void startActivityIntentSafe(Context packageContext,
                                               Intent action) {
        // Verify it resolves
        PackageManager packageManager = packageContext.getPackageManager();
        List activities = packageManager.queryIntentActivities(action,
                PackageManager.MATCH_DEFAULT_ONLY);
        boolean isIntentSafe = activities.size() > 0;

        if (isIntentSafe) {
            packageContext.startActivity(action);
        }

    }

    /**
     * @Description: 跳转
     * @param packageContext
     *            from,一般传XXXActivity.this
     * @param cls
     *            to,一般传XXXActivity.class
     */
    public static void toNextActivity(Context packageContext, Class<?> cls) {
        Intent i = new Intent(packageContext, cls);
        packageContext.startActivity(i);
    }
    public static void toNextActivityWithparth(Context packageContext, Class<?> cls, HashMap<String,String>map) {
        Intent i = new Intent(packageContext, cls);
        packageContext.startActivity(i);
    }
}
