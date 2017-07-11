package com.cgbsoft.lib.base.mvp.ui;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.androidkun.xtablayout.XTabLayout;
import com.cgbsoft.lib.R;
import com.cgbsoft.lib.R2;
import com.cgbsoft.lib.base.mvp.model.TabBean;
import com.cgbsoft.lib.base.mvp.presenter.BasePagePresenter;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * desc 可以翻页的Fragment
 * Created by yangzonghui on 2017/5/25 14:42
 * Email:yangzonghui@simuyun.com
 */
public abstract class BasePageFragment extends BaseFragment<BasePagePresenter> {

    @BindView(R2.id.title_layout)
    protected FrameLayout title_layout;

    @BindView(R2.id.tab_layout)
    XTabLayout tabLayout;

    @BindView(R2.id.viewpager)
    ViewPager viewPager;

    protected abstract int titleLayoutId();

    protected abstract ArrayList<TabBean> list();

    //绑定标题中的ID
    protected abstract void bindTitle(View titleView);

    @Override
    protected int layoutID() {
        return R.layout.fragment_base_page;
    }

    @Override
    protected void init(View view, Bundle savedInstanceState) {
        for (TabBean tabBean : list()) {
            XTabLayout.Tab tab = tabLayout.newTab();
            tab.setText(tabBean.getTabName());
            tabLayout.addTab(tab);
        }
        LayoutInflater.from(getContext()).inflate(titleLayoutId(), title_layout, true);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
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
                if (object instanceof View) {
                    container.removeView((View) object);
                } else if (object instanceof Fragment) {
                    getChildFragmentManager().beginTransaction().detach((Fragment) object);
                }
            }
        });
        bindTitle(title_layout);
        tabLayout.setupWithViewPager(viewPager);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            tabLayout.getTabAt(i).setText(list().get(i).getTabName());
        }

    }

    @Override
    protected BasePagePresenter createPresenter() {
        return null;
    }

    private TabLayout.OnTabSelectedListener listener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            //选择的tab
            Log.e("TT", "onTabSelected:" + tab.getText().toString());
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
            //离开的那个tab
            Log.e("TT", "onTabUnselected" + tab.getText().toString());
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
            //再次选择tab
            Log.e("TT", "onTabReselected" + tab.getText().toString());
        }
    };
}
