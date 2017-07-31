package com.cgbsoft.lib.base.mvp.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
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

import java.lang.reflect.Field;
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
                //这里使用到反射，拿到Tab对象后获取Class
                Class c = tab.getClass();
                try {
                    //Filed “字段、属性”的意思,c.getDeclaredField 获取私有属性。
                    //"mView"是Tab的私有属性名称(可查看TabLayout源码),类型是 TabView,TabLayout私有内部类。
                    Field field = c.getDeclaredField("mView");
                    //值为 true 则指示反射的对象在使用时应该取消 Java 语言访问检查。值为 false 则指示反射的对象应该实施 Java 语言访问检查。
                    //如果不这样会报如下错误
                    // java.lang.IllegalAccessException:
                    //Class com.test.accessible.Main
                    //can not access
                    //a member of class com.test.accessible.AccessibleTest
                    //with modifiers "private"
                    field.setAccessible(true);
                    final View viewTab = (View) field.get(tab);
                    if (viewTab == null) return;
                    viewTab.setTag(tabBean.getTabName());
                    viewTab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String tabName = (String) viewTab.getTag();
                            LogUtils.Log("aaa","tabName==="+tabName);
                            //这里就可以根据业务需求处理点击事件了。
                            clickTabButton(tabName);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
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

    /**
     * 点击尚品按钮
     * @param tabName
     */
    protected void clickTabButton(String tabName) {

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
