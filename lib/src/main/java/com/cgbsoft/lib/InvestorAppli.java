package com.cgbsoft.lib;

import com.cgbsoft.lib.base.model.bean.UserInfo;
import com.chenenyu.router.Router;

import java.util.HashMap;

/**
 * desc  投资人Application
 * Created by yangzonghui on 2017/5/4 12:02
 * Email:yangzonghui@simuyun.com
 *  
 */
public class InvestorAppli extends BaseApplication {
    private boolean isMainpage;

    private UserInfo userInfo;

    private boolean isRequestCustom;

    private boolean isTouGuOnline;

    private String tempFile;

    private   String openWebUrl;

    public   String getOpenWebUrl() {
        return openWebUrl;
    }

    public boolean isFristClickPublicTab;

    public boolean isFristClickPublicTab() {
        return isFristClickPublicTab;
    }

    public void setFristClickPublicTab(boolean fristClickPublicTab) {
        isFristClickPublicTab = fristClickPublicTab;
    }

    public   void setOpenWebUrl(String openWebUrl) {
         openWebUrl = openWebUrl;
    }

    private HashMap<String, String> serverDatahashMap = new HashMap<>();


    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public boolean isRequestCustom() {
        return isRequestCustom;
    }

    public void setRequestCustom(boolean requestCustom) {
        isRequestCustom = requestCustom;
    }

    public boolean isTouGuOnline() {
        return isTouGuOnline;
    }

    public void setTouGuOnline(boolean touGuOnline) {
        isTouGuOnline = touGuOnline;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Router.initialize(this);
    }

    public boolean isMainpage() {
        return isMainpage;
    }

    public void setMainpage(boolean mainpage) {
        isMainpage = mainpage;
    }

    public HashMap<String, String> getServerDatahashMap() {
        return serverDatahashMap;
    }

    public void setServerDatahashMap(HashMap<String, String> serverDatahashMap) {
        this.serverDatahashMap = serverDatahashMap;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        serverDatahashMap.clear();
    }
}
