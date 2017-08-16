package com.cgbsoft.lib;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.bumptech.glide.Glide;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.cgbsoft.lib.utils.db.dao.DaoMaster;
import com.cgbsoft.lib.utils.db.dao.DaoSession;
import com.cgbsoft.lib.utils.net.OKHTTP;
import com.cgbsoft.lib.utils.tools.BStrUtils;
import com.cgbsoft.lib.utils.tools.BackgroundManager;
import com.cgbsoft.lib.utils.tools.LogUtils;
import com.cgbsoft.lib.utils.tools.Utils;
import com.lzy.okgo.OkGo;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.smtt.sdk.QbSdk;

import org.greenrobot.greendao.database.Database;

import java.io.InputStream;
import java.util.logging.Level;

/**
 * desc  基类Application
 * Created by yangzonghui on 2017/5/4 11:37
 * Email:yangzonghui@simuyun.com
 *  
 */
public class BaseApplication extends MultiDexApplication {
    protected static Context context;
    protected DaoSession daoSession;
    private BackgroundManager backgroundManager;

//    static {
//        //设置umeng分享 微信
//        PlatformConfig.setWeixin(Constant.WEIXIN_APPID, Constant.WEIXIN_APPSECRET);
//    }

    public BackgroundManager getBackgroundManager() {
        return backgroundManager;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();
        //设置glide使用okhttp
        Glide.get(this).register(GlideUrl.class, InputStream.class,
                new OkHttpUrlLoader.Factory(OKHTTP.getInstance().getOkClient()));

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, Utils.getDatabaseName(this));
        Database database = helper.getWritableDb();
        daoSession = new DaoMaster(database).newSession();
        //初始化umeng分享
//        UMShareAPI.get(this);
//        Config.IsToastTip = false;//关闭umeng toast
//        Config.dialogSwitch = false;//不使用默认的dialog

        initLearCanary();
        initX5Webview();
        //初始化 okGo 用于下载
        OkGo.init(this);
        try {
            OkGo.getInstance().debug("OKGO", Level.ALL, true)
                    .setConnectTimeout(OKHTTP.HTTP_CONNECTION_TIMEOUT)  //全局的连接超时时间
                    .setReadTimeOut(OKHTTP.HTTP_CONNECTION_TIMEOUT)     //全局的读取超时时间
                    .setWriteTimeOut(OKHTTP.HTTP_CONNECTION_TIMEOUT)    //全局的写入超时时间
                    .setRetryCount(3);
        } catch (Exception e) {
            e.printStackTrace();
        }

        backgroundManager = new BackgroundManager(this);
    }

    /**
     * 初始化内存检测LeakCanary
     */
    private void initLearCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);
    }

    /**
     * 初始化x5的浏览器内核
     *
     * @return
     */
    private void initX5Webview() {
        //x5内核初始化接口
        try {
            QbSdk.initX5Environment(getApplicationContext(), new QbSdk.PreInitCallback() {
                @Override
                public void onCoreInitFinished() {

                }

                @Override
                public void onViewInitFinished(boolean b) {

                }
            });
        } catch (Exception e) {
            LogUtils.Log("BaseApplication", e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 供埋点使用
     *
     * @return
     */
    public static String BindAdviserState() {
        return !BStrUtils.isEmpty(AppManager.getUserInfo(getContext()).getToC().getBandingAdviserId()) ? "1" : "0";
    }

    public static Context getContext() {
        return context;
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}
