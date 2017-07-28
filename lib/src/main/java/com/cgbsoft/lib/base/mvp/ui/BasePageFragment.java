package com.cgbsoft.lib.base.mvp.ui;

import android.graphics.Color;
import android.os.Build;
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
import com.cgbsoft.lib.utils.StatusBarUtil;
import com.cgbsoft.lib.utils.tools.LogUtils;

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
    @BindView(R2.id.status_replace)
    View statusReplace;

    protected abstract int titleLayoutId();

    protected abstract ArrayList<TabBean> list();

    //绑定标题中的ID
    protected abstract void bindTitle(View titleView);

    protected abstract int indexSel();

    private int index;

    @Override
    protected int layoutID() {
        return R.layout.fragment_base_page;
    }

    @Override
    protected void init(View view, Bundle savedInstanceState) {
        int statusBarHeight = StatusBarUtil.getStatusBarHeight(baseActivity);
        ViewGroup.LayoutParams layoutParams = statusReplace.getLayoutParams();
        layoutParams.height = statusBarHeight;
        statusReplace.setLayoutParams(layoutParams);
        LayoutInflater.from(getContext()).inflate(titleLayoutId(), title_layout, true);
        bindTitle(title_layout);
        if (list() != null) {
            for (TabBean tabBean : list()) {
                XTabLayout.Tab tab = tabLayout.newTab();
                tab.setText(tabBean.getTabName());
                tabLayout.addTab(tab);
            }
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

            tabLayout.setupWithViewPager(viewPager);
            for (int i = 0; i < tabLayout.getTabCount(); i++) {
                tabLayout.getTabAt(i).setText(list().get(i).getTabName());
            }
            for (int i = 0; i < list().size(); i++) {
                if (list().get(i).getCode() == index)
                    viewPager.setCurrentItem(i);
            }
        }
    }

    protected void setIndex(int code) {
        if (code != 0 && code > 1000) {
            if (null == viewPager) {
                index = code;
            } else {
                index = code;
                for (int i = 0; i < list().size(); i++) {
                    if (list().get(i).getCode() == code)
                        viewPager.setCurrentItem(i);
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected BasePagePresenter createPresenter() {
        return null;
    }

}
