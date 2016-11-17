package com.cgbsoft.lib;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.bumptech.glide.Glide;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.cgbsoft.lib.base.model.bean.DaoMaster;
import com.cgbsoft.lib.base.model.bean.DaoSession;
import com.cgbsoft.lib.utils.net.OKHTTP;
import com.cgbsoft.lib.utils.tools.Utils;

import org.greenrobot.greendao.database.Database;

import java.io.InputStream;

/**
 *  Created by xiaoyu.zhang on 2016/11/7 13:28
 *  
 */
public class Appli extends MultiDexApplication {
    private static Context context;
    private DaoSession daoSession;

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
    }

    public static Context getContext() {
        return context;
    }

    public DaoSession getDaoSession(){
        return daoSession;
    }
}
