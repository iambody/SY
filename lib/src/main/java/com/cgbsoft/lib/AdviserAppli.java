package com.cgbsoft.lib;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.bumptech.glide.Glide;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.cgbsoft.lib.utils.constant.Constant;
import com.cgbsoft.lib.utils.db.dao.DaoMaster;
import com.cgbsoft.lib.utils.db.dao.DaoSession;
import com.cgbsoft.lib.utils.net.OKHTTP;
import com.cgbsoft.lib.utils.tools.Utils;
import com.chenenyu.router.Router;
import com.lzy.okgo.OkGo;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import org.greenrobot.greendao.database.Database;

import java.io.InputStream;
import java.util.logging.Level;

/**
 * desc  理财师端的application
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 17/4/5-17:30
 */
public class AdviserAppli extends Appli {
    @Override
    public void onCreate() {
        super.onCreate();
    }
}
