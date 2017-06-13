package com.cgbsoft.lib.utils.tools;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * desc  activity等ui跳转的工具类
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 17/4/7-18:39
 */
public class UiSkipUtils {
    /**
     * @param packageContext
     * @param action         含操作的Intent
     * @Description: 隐式启动, 跳转
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
     * @param packageContext from,一般传XXXActivity.this
     * @param cls            to,一般传XXXActivity.class
     * @Description: 跳转
     */
    public static void toNextActivity(Context packageContext, Class<?> cls) {
        Intent i = new Intent(packageContext, cls);
        packageContext.startActivity(i);
    }


    /**
     * @param packageContext
     * @param cls
     * @param keyvalues      需要传进去的String参数{{key1,values},{key2,value2}...}
     * @Description: 跳转, 带参数的方法;需要其它的数据类型,再继续重载吧
     */
    public static void toNextActivityWithParamsMap(Context packageContext, Class<?> cls,
                                                   HashMap<String, String> keyvalues) {
        Intent i = new Intent(packageContext, cls);
        Set<Map.Entry<String, String>> set = keyvalues.entrySet();
        Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry<String, String>) iterator.next();
            i.putExtra(entry.getKey(), entry.getValue());
        }
        packageContext.startActivity(i);
    }

    /**
     * 直接处理intent
     *
     * @param packageContext
     * @param intent
     */

    public static void toNextActivityWithIntent(Context packageContext, Intent intent) {
        packageContext.startActivity(intent);
    }
}
