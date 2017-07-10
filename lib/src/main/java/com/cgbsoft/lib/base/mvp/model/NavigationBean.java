package com.cgbsoft.lib.base.mvp.model;

import java.util.List;

/**
 * desc  Navigation
 * Created by yangzonghui on 2017/6/28 21:45
 * Email:yangzonghui@simuyun.com
 *  
 */
public class NavigationBean {
    private String jumpType;
    private String title;
    private String level;
    private String code;
    private List<SecondNavigation> secondNavigation;

    public String getJumpType() {
        return jumpType;
    }

    public void setJumpType(String jumpType) {
        this.jumpType = jumpType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<SecondNavigation> getSecondNavigation() {
        return secondNavigation;
    }

    public void setSecondNavigation(List<SecondNavigation> secondNavigation) {
        this.secondNavigation = secondNavigation;
    }
}
