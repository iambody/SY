package com.cgbsoft.privatefund;

import android.app.ActivityManager;
import android.content.Context;

import com.cgbsoft.lib.InvestorAppli;

import java.util.List;

import app.live.com.mvp.presenter.InitBusinessHelper;
import app.live.com.utils.SxbLogImpl;

/**
 * desc
 * Created by yangzonghui on 2017/5/16 20:20
 * Email:yangzonghui@simuyun.com
 *  
 */
public class InitApplication extends InvestorAppli {
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化直播
        initLive();
    }


    private void initLive() {
        if (shouldInit()) {
            SxbLogImpl.init(getApplicationContext());

            //初始化APP
            InitBusinessHelper.initApp(context);
        }
    }


    /**
     * 判断是否需要初始化
     * @return
     */
    private boolean shouldInit() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = android.os.Process.myPid();

        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }

}
