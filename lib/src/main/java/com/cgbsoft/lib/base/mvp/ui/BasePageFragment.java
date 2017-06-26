package com.cgbsoft.lib.base.mvp.ui;


import android.os.Bundle;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.util.ArrayMap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.cgbsoft.lib.R;
import com.cgbsoft.lib.R2;
import com.cgbsoft.lib.base.mvp.model.TabBean;
import com.cgbsoft.lib.base.mvp.presenter.BasePagePresenter;
import com.cgbsoft.lib.base.mvp.presenter.BasePresenter;
import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;

/**
 *  desc 可以翻页的Fragment
 *  Created by yangzonghui on 2017/5/25 14:42
 *  Email:yangzonghui@simuyun.com
 */
public abstract class BasePageFragment extends BaseFragment<BasePagePresenter> {


    @BindView(R2.id.title_layout)
    FrameLayout title_layout;

    @BindView(R2.id.tab_layout)
    TabLayout tabLayout;

    @BindView(R2.id.view_pager)
    ViewPager viewPager;

    protected abstract int titleLayoutId();

    protected abstract ArrayList<TabBean> list();

    @Override
    protected int layoutID() {
        return R.layout.fragment_base_page;
    }

    @Override
    protected void init(View view, Bundle savedInstanceState) {

        for (TabBean tabBean : list()) {
            TabLayout.Tab tab = tabLayout.newTab();
            tab.setText(tabBean.getTabName());
            tabLayout.addTab(tab);
        }

        tabLayout.setupWithViewPager(viewPager);

        LayoutInflater.from(getContext()).inflate(titleLayoutId(),title_layout,false);
        viewPager.setAdapter(new FragmentPagerAdapter(getFragmentManager()) {
            @Override
            public int getCount() {
                return list().size();
            }

            @Override
            public Fragment getItem(int position) {
                return list().get(position).getFragment();
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }
        });

    }
    @Override
    protected BasePagePresenter createPresenter() {
        return null;
    }

    private TabLayout.OnTabSelectedListener listener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            //选择的tab
            Log.e("TT","onTabSelected:" + tab.getText().toString());
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
            //离开的那个tab
            Log.e("TT","onTabUnselected" + tab.getText().toString());
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
            //再次选择tab
            Log.e("TT","onTabReselected" + tab.getText().toString());
        }
    };
}
