package com.cgbsoft.lib.widget.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.cgbsoft.lib.base.mvp.ui.BaseLazyFragment;

import java.util.List;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/6/26-17:56
 */
public class FragmentAdapter extends FragmentPagerAdapter {

    public List<BaseLazyFragment> lazyFragments;


    public FragmentAdapter(FragmentManager fm, List<BaseLazyFragment> lazyFragments) {
        super(fm);
        this.lazyFragments = lazyFragments;
    }
    @Override
    public Fragment getItem(int position) {
        return lazyFragments.get(position);
    }

    @Override
    public int getCount() {
        return lazyFragments.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //如果注释这行，那么不管怎么切换，page都不会被销毁
        super.destroyItem(container, position, object);
    }
}
