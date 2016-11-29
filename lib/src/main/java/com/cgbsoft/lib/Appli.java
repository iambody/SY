package com.cgbsoft.lib;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.bumptech.glide.Glide;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.cgbsoft.lib.base.model.bean.DaoMaster;
import com.cgbsoft.lib.base.model.bean.DaoSession;
import com.cgbsoft.lib.utils.constant.Constant;
import com.cgbsoft.lib.utils.net.OKHTTP;
import com.cgbsoft.lib.utils.tools.Utils;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import org.greenrobot.greendao.database.Database;

import java.io.InputStream;

/**
 *  Created by xiaoyu.zhang on 2016/11/7 13:28
 *  
 */
public class Appli extends MultiDexApplication {
    private static Context context;
    private DaoSession daoSession;

    static {
        //设置umeng分享 微信
        PlatformConfig.setWeixin(Constant.WEIXIN_APPID, Constant.WEIXIN_APPSECRET);
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
        UMShareAPI.get(this);
        Config.IsToastTip = false;//关闭umeng toast
        Config.dialogSwitch = false;//不使用默认的dialog

    }

    public static Context getContext() {
        return context;
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}
