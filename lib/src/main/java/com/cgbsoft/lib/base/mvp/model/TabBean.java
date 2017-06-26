package com.cgbsoft.lib.base.mvp.model;

import android.support.v4.app.Fragment;

/**
 * desc  tab标签名字以及对应的fragment
 * Created by yangzonghui on 2017/6/22 14:24
 * Email:yangzonghui@simuyun.com
 *  
 */
public class TabBean {

    private String tabName;
    private Fragment fragment;

    public TabBean(String tabName, Fragment fragment) {
        this.tabName = tabName;
        this.fragment = fragment;
    }

    public String getTabName() {
        return tabName;
    }

    public void setTabName(String tabName) {
        this.tabName = tabName;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }
}
