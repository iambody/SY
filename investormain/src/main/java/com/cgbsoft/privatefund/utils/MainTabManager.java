package com.cgbsoft.privatefund.utils;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.cgbsoft.adviser.mvp.ui.message.MessageFragment;
import com.cgbsoft.adviser.mvp.ui.college.CollegeFragment;
import com.cgbsoft.adviser.mvp.ui.setting.SettingFragment;
import com.cgbsoft.lib.BaseApplication;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.utils.constant.Constant;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.mvp.ui.discovery.DiscoveryFragment;
import com.cgbsoft.privatefund.mvp.ui.home.CloudFragment;
import com.cgbsoft.privatefund.mvp.ui.home.ClubFragment;
import com.cgbsoft.privatefund.mvp.ui.home.MineFragment;
import com.cgbsoft.privatefund.mvp.ui.product.ProductFragment;


/**
 * Created by win8 -1 on 2015/8/14.
 */
public class MainTabManager {
    private static MainTabManager mInstance;
    //投资人
    private MineFragment mineFragment;//个人页
    private CloudFragment cloudFragment;//云键
    private ClubFragment clubFragment;//俱乐部

    //理财师
    private MessageFragment messageFragment;//消息
    private SettingFragment settingFragment;//设置
    private CollegeFragment collegeFragment;//学院

    private DiscoveryFragment discoveryFragment;//发现
    private ProductFragment productFragment;//产品


    private Bundle mBundle;

    public static MainTabManager getInstance() {
        if (mInstance == null) {
            synchronized (MainTabManager.class) {
                if (mInstance == null) {
                    mInstance = new MainTabManager();
                }
            }
        }
        return mInstance;
    }

    //设置参数
    public MainTabManager setBundle(Bundle bundle) {
        mBundle = bundle;
        return mInstance;
    }

    public Fragment getFragmentByIndex(int index) {
        Fragment fragment = null;
        switch (index) {
            case R.id.nav_left_first:
                if (isInvestor()) {
                    if (mineFragment == null) {
                        mineFragment = new MineFragment();
                    }
                    fragment = mineFragment;
                } else {
                    if (messageFragment == null) {
                        messageFragment = new MessageFragment();
                    }
                    fragment = messageFragment;
                }
                break;
            case R.id.nav_left_second:
                if (productFragment == null) {
                    productFragment = new ProductFragment();
                }
                fragment = productFragment;
                break;
            case R.id.nav_center:
                if (isInvestor()) {
                    if (cloudFragment == null) {
                        cloudFragment = new CloudFragment();
                    }
                    fragment = cloudFragment;
                } else {
                    if (settingFragment == null) {
                        settingFragment = new SettingFragment();
                    }
                    fragment = settingFragment;
                }
                break;
            case R.id.nav_right_first:
                if (discoveryFragment == null) {
                    discoveryFragment = new DiscoveryFragment();
                }
                fragment = discoveryFragment;
                break;
            case R.id.nav_right_second:
                if (isInvestor()) {
                    if (clubFragment == null) {
                        clubFragment = new ClubFragment();
                    }
                    fragment = clubFragment;
                } else {
                    if (collegeFragment == null) {
                        collegeFragment = new CollegeFragment();
                    }
                    fragment = collegeFragment;
                }
                break;
        }
        if (fragment != null) {
            if (mBundle != null) {
                fragment.setArguments(mBundle);
            }
        }
        return fragment;
    }

    /**
     * 是否为投资者
     */
    private boolean isInvestor() {
        return SPreference.getIdtentify(BaseApplication.getContext()) == Constant.IDS_INVERSTOR;
    }

    public void destory() {
        mineFragment = null;
        cloudFragment = null;
        clubFragment = null;

        messageFragment = null;
        settingFragment = null;
        collegeFragment = null;

        discoveryFragment = null;
        productFragment = null;
    }

}