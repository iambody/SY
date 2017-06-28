package com.cgbsoft.lib.base.mvp.ui;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.cgbsoft.lib.R;
import com.cgbsoft.lib.R2;
import com.cgbsoft.lib.base.mvp.model.TabBean;
import com.cgbsoft.lib.base.mvp.presenter.BasePagePresenter;

import java.lang.reflect.Field;
import java.util.ArrayList;

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

    @BindView(R2.id.viewpager)
    ViewPager viewPager;

    protected abstract int titleLayoutId();

    protected abstract ArrayList<TabBean> list();

    @Override
    protected int layoutID() {
        return R.layout.fragment_base_page;
    }

    @Override
    protected void init(View view, Bundle savedInstanceState) {
        tabLayout.post(() -> setIndicator(tabLayout,70,70));
        for (TabBean tabBean : list()) {
            TabLayout.Tab tab = tabLayout.newTab();
            tab.setText(tabBean.getTabName());
            tabLayout.addTab(tab);
        }
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

        tabLayout.setupWithViewPager(viewPager);

//        for (int i=0;i<tabLayout.getTabCount();i++) {
//            tabLayout.getTabAt(i).setText(list().get(i).getTabName());
//        }

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

    public void setIndicator(TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());

        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }


    }
}
