package com.cgbsoft.lib;

import com.chenenyu.router.Router;

/**
 * desc  投资人Application
 * Created by yangzonghui on 2017/5/4 12:02
 * Email:yangzonghui@simuyun.com
 *  
 */
public class InvestorAppli extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        Router.initialize(this);
    }
}
