package com.cgbsoft.lib.utils.tools;

import android.os.Handler;
import android.os.Looper;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/5/4-18:00
 */
public class ThreadUtils {

    private static final Handler mainThreadHandler = new Handler(Looper.getMainLooper());

    private ThreadUtils() {
    }

    public static void checkRunningOnNonUiThread() {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw new AssertionError("Run this on non UI thread");
        }
    }

    public static void runOnMainThread(Runnable runnable) {
        mainThreadHandler.post(runnable);
    }

    /**
     * 延迟执行操作
     */
    public static void runOnMainThreadDelay(Runnable runnable, long delay) {
        mainThreadHandler.postDelayed(runnable, delay);
    }

}
