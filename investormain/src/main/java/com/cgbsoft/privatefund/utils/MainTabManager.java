package com.cgbsoft.privatefund.utils;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.cgbsoft.lib.utils.constant.RxConstant;
import com.cgbsoft.lib.utils.rxjava.RxBus;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.mvp.ui.home.EverHealthFragment;
import com.cgbsoft.privatefund.mvp.ui.home.HappyLifeFragment;
import com.cgbsoft.privatefund.mvp.ui.home.MainHomeFragment;
import com.cgbsoft.privatefund.mvp.ui.home.MineFragment;
import com.cgbsoft.privatefund.mvp.ui.home.PrivateBanksFragment;

/**
 * @author chenlong
 */
public class MainTabManager {
    private static MainTabManager mInstance;

    private MainHomeFragment firstPageFragment;//首页
    private PrivateBanksFragment priveteBankFragment; // 尊享私行
    private HappyLifeFragment happyLifeFragment; // 快乐生活
    private EverHealthFragment eveyHealthFragment; // 永享健康
    private MineFragment mineFragment; // 我的

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

    public Fragment getFragmentByIndex(int index,int code) {
        Fragment fragment = null;
        switch (index) {
            case R.id.nav_left_first:
                if (firstPageFragment == null) {
                    firstPageFragment = new MainHomeFragment();
                }
                fragment = firstPageFragment;
                break;
            case R.id.nav_left_second:
                if (priveteBankFragment == null) {
                    priveteBankFragment = new PrivateBanksFragment();
                }
                if (code!=0) {
                    priveteBankFragment.setCode(code);
                }
                fragment = priveteBankFragment;
                break;
            case R.id.nav_center:
                if (happyLifeFragment == null) {
                    happyLifeFragment = new HappyLifeFragment();
                }
                if (code!=0) {
                    happyLifeFragment.setCode(code);
                }
                fragment = happyLifeFragment;
                break;
            case R.id.nav_right_first:
                if (eveyHealthFragment == null) {
                    eveyHealthFragment = new EverHealthFragment();
                }
                if (code!=0){
                    eveyHealthFragment.setCode(code);
                }
                fragment = eveyHealthFragment;
                break;
            case R.id.nav_right_second:
                if (mineFragment == null) {
                    mineFragment = new MineFragment();
                }
                fragment = mineFragment;
                break;
        }
        if (fragment != null) {
            if (mBundle != null) {
                fragment.setArguments(mBundle);
            }
        }
        return fragment;
    }

    public void destory() {
        firstPageFragment = null;
        priveteBankFragment = null;
        happyLifeFragment = null;
        eveyHealthFragment = null;
        mineFragment = null;
    }



}
