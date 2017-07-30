package com.cgbsoft.privatefund;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import com.cgbsoft.lib.AppInfStore;
import com.cgbsoft.lib.InvestorAppli;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.utils.constant.Constant;
import com.cgbsoft.lib.utils.constant.RxConstant;
import com.cgbsoft.lib.utils.rxjava.RxBus;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.utils.tools.DeviceUtils;
import com.cgbsoft.privatefund.utils.SimuyunUncaughtExceptionHandler;

import java.util.List;

import app.privatefund.com.im.bean.NewsMessage;
import app.privatefund.com.im.bean.PdfMessage;
import app.privatefund.com.im.bean.ProductMessage;
import app.privatefund.com.im.listener.MyConnectionStatusListener;
import app.privatefund.com.im.listener.MyConversationBehaviorListener;
import app.privatefund.com.im.listener.MyConversationListBehaviorListener;
import app.privatefund.com.im.listener.MyReceiveMessageListener;
import app.privatefund.com.im.listener.MySendMessageListener;
import app.privatefund.com.im.listener.NewMessageItemProvider;
import app.privatefund.com.im.listener.PdfMessageItemProvider;
import app.privatefund.com.im.listener.ProductInputModule;
import app.privatefund.com.im.listener.ProductMessageItemProvider;
import io.rong.imkit.RongExtensionManager;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;
import qcloud.liveold.mvp.presenters.InitBusinessHelper;
import qcloud.liveold.mvp.utils.SxbLogImpl;
import rx.Observable;

/**
 * desc
 * Created by yangzonghui on 2017/5/16 20:20
 * Email:yangzonghui@simuyun.com
 *  
 */
public class InitApplication extends InvestorAppli {
    private Observable<Integer> logoutObservable;
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
        // "io.rong.push".equals(DeviceUtils.getCurProcessName(getApplicationContext()))
        if (getApplicationInfo().packageName.equals(DeviceUtils.getCurProcessName(getApplicationContext()))) {
            Log.i("InitApplication", "----initRongConnect");

            /**
             * IMKit SDK调用第一步 初始化
             */
            if (getApplicationInfo().packageName.equals(DeviceUtils.getCurProcessName(getApplicationContext()))) {
                RongIM.init(this);
            }
            RongIM.registerMessageType(ProductMessage.class);
            RongIM.registerMessageType(PdfMessage.class);
            RongIM.registerMessageType(NewsMessage.class);
            RongIM.registerMessageTemplate(new ProductMessageItemProvider());
            RongIM.registerMessageTemplate(new PdfMessageItemProvider());
            RongIM.registerMessageTemplate(new NewMessageItemProvider());
            RongIM.setOnReceiveMessageListener(new MyReceiveMessageListener());
            RongIM.setConnectionStatusListener(new MyConnectionStatusListener());
            RongIM.setConversationBehaviorListener(new MyConversationBehaviorListener()); //会话界面监听
            RongIM.setConversationListBehaviorListener(new MyConversationListBehaviorListener());//会话列表操作监听
            RongExtensionManager.getInstance().registerExtensionModule(new ProductInputModule(this));
            RongIM.getInstance().setSendMessageListener(new MySendMessageListener());
            RongIM.getInstance().setMessageAttachedUserInfo(true);
            registerLogoutObservable();
        }
    }

    private void registerLogoutObservable() {
        logoutObservable = RxBus.get().register(RxConstant.LOGIN_STATUS_DISABLE_OBSERVABLE, Integer.class);
        logoutObservable.subscribe(new RxSubscriber<Integer>() {
            @Override
            protected void onEvent(Integer integer) {
                AppInfStore.saveIsLogin(getApplicationContext(), false);
                AppInfStore.saveUserAccount(getApplicationContext(), null);
                AppInfStore.saveRongTokenExpired(getApplicationContext(), 0);
                ((InvestorAppli)InvestorAppli.getContext()).setRequestCustom(false);
                SPreference.putBoolean(InvestorAppli.getContext(), Constant.weixin_login, false);
                if(RongIM.getInstance().getRongIMClient()!=null){
                    RongIM.getInstance().getRongIMClient().clearConversations(Conversation.ConversationType.PRIVATE);
                    RongIM.getInstance().getRongIMClient().clearConversations(Conversation.ConversationType.GROUP);
                }
                if (RongIM.getInstance() != null) {
                    RongIM.getInstance().disconnect();
                }

                RxBus.get().post(RxConstant.CLOSE_MAIN_OBSERVABLE, true);
            }

            @Override
            protected void onRxError(Throwable error) {

            }
        });
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        unRegisterLogoutObservable();
    }

    private void unRegisterLogoutObservable() {
        if (logoutObservable != null) {
            RxBus.get().unregister(RxConstant.LOGIN_STATUS_DISABLE_OBSERVABLE,logoutObservable);
        }
    }

    private void initLive() {
//        if (shouldInit()) {
        SxbLogImpl.init(getApplicationContext());

        //初始化APP
        InitBusinessHelper.initApp(getApplicationContext());
//        }
    }


    /**
     * 判断是否需要初始化
     *
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
