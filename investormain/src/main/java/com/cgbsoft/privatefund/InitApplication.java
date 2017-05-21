package com.cgbsoft.privatefund;

import android.app.ActivityManager;
import android.content.Context;

import com.cgbsoft.lib.InvestorAppli;
import com.cgbsoft.lib.utils.tools.DeviceUtils;
import com.cgbsoft.privatefund.utils.SimuyunUncaughtExceptionHandler;

import java.util.List;

import app.live.com.mvp.presenter.InitBusinessHelper;
import app.live.com.utils.SxbLogImpl;
import app.privatefund.com.im.bean.NewsMessage;
import app.privatefund.com.im.bean.PdfMessage;
import app.privatefund.com.im.bean.ProductMessage;
import app.privatefund.com.im.listener.MyConversationBehaviorListener;
import app.privatefund.com.im.listener.MyConversationListBehaviorListener;
import app.privatefund.com.im.listener.MyReceiveMessageListener;
import app.privatefund.com.im.listener.NewMessageItemProvider;
import app.privatefund.com.im.listener.PdfMessageItemProvider;
import app.privatefund.com.im.listener.ProductMessageItemProvider;
import io.rong.imkit.RongIM;

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

        Thread.setDefaultUncaughtExceptionHandler(new SimuyunUncaughtExceptionHandler(this));

        /**
         * OnCreate 会被多个进程重入，这段保护代码，确保只有您需要使用 RongIM 的进程和 Push 进程执行了 init。
         * io.rong.push 为融云 push 进程名称，不可修改。
         */
        if (getApplicationInfo().packageName.equals(DeviceUtils.getCurProcessName(getApplicationContext())) ||
                "io.rong.push".equals(DeviceUtils.getCurProcessName(getApplicationContext()))) {

            /**
             * IMKit SDK调用第一步 初始化
             */
            RongIM.init(this);
            RongIM.registerMessageType(ProductMessage.class);
            RongIM.registerMessageType(PdfMessage.class);
            RongIM.registerMessageType(NewsMessage.class);
            RongIM.getInstance().registerMessageTemplate(new ProductMessageItemProvider());
            RongIM.getInstance().registerMessageTemplate(new PdfMessageItemProvider());
            RongIM.getInstance().registerMessageTemplate(new NewMessageItemProvider());
            RongIM.setOnReceiveMessageListener(new MyReceiveMessageListener());
            RongIM.setConversationBehaviorListener(new MyConversationBehaviorListener()); //会话界面监听
            RongIM.setConversationListBehaviorListener(new MyConversationListBehaviorListener());//会话列表操作监听
        }
    }


    private void initLive() {
        if (shouldInit()) {
            SxbLogImpl.init(getApplicationContext());

            //初始化APP
//            InitBusinessHelper.initApp(context);
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
