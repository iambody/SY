package com.cgbsoft.privatefund.utils;

import android.content.Context;

/**
 * @author chenlong
 */
public class SimuyunUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {


    private final Thread.UncaughtExceptionHandler defaultHandler;

    public SimuyunUncaughtExceptionHandler(Context context) {
        this.defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        defaultHandler.uncaughtException(thread, ex);
    }

}
