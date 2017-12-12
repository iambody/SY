package com.cgbsoft.privatefund.utils.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.cgbsoft.lib.BaseApplication;
import com.cgbsoft.lib.utils.net.OKHTTP;
import com.cgbsoft.lib.utils.tools.DeviceUtils;
import com.cgbsoft.lib.utils.tools.LogUtils;
import com.cgbsoft.privatefund.InitApplication;
import com.cgbsoft.privatefund.utils.SimuyunUncaughtExceptionHandler;
import com.crashlytics.android.Crashlytics;
import com.google.android.exoplayer.C;
import com.lzy.okgo.OkGo;
import com.tencent.msdk.dns.MSDKDnsResolver;
import com.tencent.smtt.sdk.QbSdk;
import com.umeng.analytics.MobclickAgent;

import java.util.logging.Level;

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
import io.fabric.sdk.android.Fabric;
import io.rong.imkit.RongExtensionManager;
import io.rong.imkit.RongIM;
import qcloud.liveold.mvp.presenters.InitBusinessHelper;
import qcloud.liveold.mvp.utils.SxbLogImpl;

/**
 * desc  application里面冷加载时候的初始化的新线程
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/12/12-15:14
 */
public class InitializeService extends IntentService {
    public static final String ACTION_INIT_WHEN_APP_CREATE = "com.cgbsoft.privatefund.service.action.app.create";
    public static final String EXTRA_PARAM = "com.cgbsoft.privatefund.service.extra.PARAM";
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     * @param name Used to name the worker thread, important only for debugging.
     */
    public InitializeService() {
        super("InitializeService");
    }
    public static void startService(Context context) {
        Intent intent = new Intent(context, InitializeService.class);
        intent.setAction(ACTION_INIT_WHEN_APP_CREATE);
        context.startService(intent);
        Log.i("okgogoogog","开始startService");
    }
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.i("okgogoogog","开始onHandleIntent");
        if (null != intent) {
            String action = intent.getAction();
            if (ACTION_INIT_WHEN_APP_CREATE.equals(action)) {

                performInit(action);
            }
        }
    }
    private void performInit(String action) {
        Log.i("okgogoogog","开始performInit");
        initOkGo();//初始化okgo
        initImageLoader();//初始化图片加载
        initLive();//初始化直播
        initIm();//初始化融云
        initGreenDao();//初始化数据库
        initX5();//初始化x5内核
        initMob();//初始化友盟统计
        initFabric();//初始化fabric
        initLearCanary();//初始化内检测

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void initFabric() {
        Fabric.with(InitializeService.this.getApplicationContext(), new Crashlytics());
    }

    private void initGreenDao() {
    }

    private void initX5() {
        //x5内核初始化接口
        ((InitApplication)InitializeService.this. getApplication()).initX5Webview();
//        try {
//            QbSdk.initX5Environment(InitializeService.this.getApplicationContext(), new QbSdk.PreInitCallback() {
//                @Override
//                public void onCoreInitFinished() {
//
//                }
//
//                @Override
//                public void onViewInitFinished(boolean b) {
//
//                }
//            });
//        } catch (Exception e) {
//            LogUtils.Log("BaseApplication", e.getMessage());
//            e.printStackTrace();
//        }
    }

    private void initIm() {
        /**
         * OnCreate 会被多个进程重入，这段保护代码，确保只有您需要使用 RongIM 的进程和 Push 进程执行了 init。
         * io.rong.push 为融云 push 进程名称，不可修改。
         */
        // "io.rong.push".equals(DeviceUtils.getCurProcessName(getApplicationContext()))
        if ( InitializeService.this.getApplication() .getApplicationInfo().packageName.equals(DeviceUtils.getCurProcessName( InitializeService.this.getApplicationContext()))) {
            Log.i("InitApplication", "----initRongConnect");
            /**
             * IMKit SDK调用第一步 初始化
             */
            if ( InitializeService.this.getApplication() .getApplicationInfo().packageName.equals(DeviceUtils.getCurProcessName( InitializeService.this.getApplicationContext()))) {
                RongIM.init(((InitApplication) InitializeService.this.getApplication()));
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
            ((InitApplication) getApplication()).registerLogoutObservable();
        }
    }

    private void initImageLoader() {
    }

    private void initLive() {
//        SxbLogImpl.init(InitializeService.this.getApplicationContext());
        //初始化APP
        InitBusinessHelper.initApp(InitializeService.this.getApplicationContext());
    }

    private void initOkGo() {
        Log.i("okgogoogog","开始service调用okgo");
        ((InitApplication)InitializeService.this. getApplication()).initOkGo();
//        OkGo.init((BaseApplication) getApplication());
//        try {
//            OkGo.getInstance().debug("OKGO", Level.ALL, true)
//                    .setConnectTimeout(OKHTTP.HTTP_CONNECTION_TIMEOUT)  //全局的连接超时时间
//                    .setReadTimeOut(OKHTTP.HTTP_CONNECTION_TIMEOUT)     //全局的读取超时时间
//                    .setWriteTimeOut(OKHTTP.HTTP_CONNECTION_TIMEOUT)    //全局的写入超时时间
//                    .setRetryCount(3);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }

    private void initMob() {
        MobclickAgent.openActivityDurationTrack(false);//禁止默认的页面统计方式，这样将不会再自动统计Activity
        MobclickAgent.setDebugMode(true);
        Thread.setDefaultUncaughtExceptionHandler(new SimuyunUncaughtExceptionHandler( InitializeService.this.getApplication()));
        MSDKDnsResolver.getInstance().init(InitializeService.this.getApplicationContext());

    }

    private void initLearCanary() {

        ((InitApplication)InitializeService.this. getApplication()).initLearCanary();
    }
}
