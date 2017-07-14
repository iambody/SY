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
    private int code;

    public TabBean(String tabName, Fragment fragment, int code) {
        this.tabName = tabName;
        this.fragment = fragment;
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
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
