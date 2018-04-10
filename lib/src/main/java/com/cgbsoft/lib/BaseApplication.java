package com.cgbsoft.lib;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.multidex.MultiDexApplication;

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

import java.util.HashMap;
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
    private HashMap<String, Object> publicBuyMaps ;

    public HashMap<String, Object> getPublicBuyMaps() {
        return publicBuyMaps;
    }

    public void setPublicBuyMaps(HashMap<String, Object> publicBuyMaps) {
        this.publicBuyMaps = publicBuyMaps;
    }
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
//        Glide.get(this).register(GlideUrl.class, InputStream.class,
//                new OkHttpUrlLoader.Factory(OKHTTP.getInstance().getOkClient()));

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, Utils.getDatabaseName(this));
        Database database = helper.getWritableDb();
        daoSession = new DaoMaster(database).newSession();
        //初始化umeng分享
//        UMShareAPI.get(this);
//        Config.IsToastTip = false;//关闭umeng toast
//        Config.dialogSwitch = false;//不使用默认的dialog

//        initLearCanary();
        initX5Webview();
//        //初始化 okGo 用于下载
//        OkGo.init(this);
//        try {
//            OkGo.getInstance().debug("OKGO", Level.ALL, true)
//                    .setConnectTimeout(OKHTTP.HTTP_CONNECTION_TIMEOUT)  //全局的连接超时时间
//                    .setReadTimeOut(OKHTTP.HTTP_CONNECTION_TIMEOUT)     //全局的读取超时时间
//                    .setWriteTimeOut(OKHTTP.HTTP_CONNECTION_TIMEOUT)    //全局的写入超时时间
//                    .setRetryCount(3);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        initOkGo();
        backgroundManager = new BackgroundManager(this);
    }

    public void initOkGo() {
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
    }

    /**
     * 初始化内存检测LeakCanary
     */
    public void initLearCanary() {
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
    public void initX5Webview() {
        //x5内核初始化接口
        try {
            QbSdk.initX5Environment(context, new QbSdk.PreInitCallback() {
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
     * 获取App安装包信
     */
    public PackageInfo getPackageInfo() {
        PackageInfo info = null;
        try {
            info = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace(System.err);
        }
        if (info == null)
            info = new PackageInfo();
        return info;
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
