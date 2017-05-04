package com.cgbsoft.lib;

import com.cgbsoft.lib.BaseApplication;
import com.chenenyu.router.Router;

/**
 * desc  用户端的application
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 17/4/5-17:30
 */
public class CustomerAppli extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        Router.initialize(this,true);
    }
}
