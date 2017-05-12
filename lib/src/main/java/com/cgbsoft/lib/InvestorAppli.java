package com.cgbsoft.lib;

import com.cgbsoft.lib.base.model.bean.UserInfo;
import com.chenenyu.router.Router;

/**
 * desc  投资人Application
 * Created by yangzonghui on 2017/5/4 12:02
 * Email:yangzonghui@simuyun.com
 *  
 */
public class InvestorAppli extends BaseApplication {

    private UserInfo userInfo;

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Router.initialize(this);
    }
}
