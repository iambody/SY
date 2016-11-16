package com.cgbsoft.privatefund.mvp.ui.home;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;

import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.mvp.presenter.HomePresenter;
import com.cgbsoft.privatefund.mvp.view.HomeView;
import com.cgbsoft.privatefund.utils.HomeTabManager;
import com.cgbsoft.privatefund.widget.navigation.BottomNavigationBar;

import butterknife.BindView;

public class HomeActivity extends BaseActivity implements HomeView, BottomNavigationBar.BottomClickListener {
    private FragmentManager mFragmentManager;
    private Fragment mContentFragment;
    private HomePresenter homePresenter;

    @BindView(R.id.bottomNavigationBar)
    BottomNavigationBar bottomNavigationBar;

    private boolean isExit;

    @Override
    protected int layoutID() {
        return R.layout.activity_home;
    }

    @Override
    protected void init() {
        homePresenter = new HomePresenter(this);
        mFragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        mContentFragment = HomeTabManager.getInstance().getFragmentByIndex(R.id.nav_left_first);

//        transaction.add(R.id.fl_main_content, mContentFragment);
//        transaction.commitAllowingStateLoss();
    }

    @Override
    protected void data() {
        SPreference.toDataMigration(this);
        bottomNavigationBar.setOnClickListener(this);
        bottomNavigationBar.setActivity(this);
    }

    @Override
    public <T> void getDataList(RxSubscriber<T> subscriber) {

    }


    private void switchFragment(Fragment to) {
        if (mContentFragment != to) {
            FragmentTransaction transaction = mFragmentManager.beginTransaction().setCustomAnimations(
                    R.anim.home_fade_in, R.anim.home_fade_out);
            if (!to.isAdded()) {    // 先判断是否被add过
                transaction.hide(mContentFragment).add(R.id.fl_main_content, to).commitAllowingStateLoss(); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                transaction.hide(mContentFragment).show(to).commitAllowingStateLoss(); // 隐藏当前的fragment，显示下一个
            }
            todo();
            mContentFragment = to;
        }
    }

    private void todo() {
//        if (mContentFragment instanceof StarFragment) {
//            RxBus.get().post(RELEASE_STAR_OBSERVABLE, true);
//        }
    }


    @Override
    public void onBottomNavClickListener(String tag) {
        if (TextUtils.equals("call", tag)) {

        } else if (TextUtils.equals("meet", tag)) {

        } else if (TextUtils.equals("live", tag)) {

        } else if (TextUtils.equals("message", tag)) {

        } else if (TextUtils.equals("cs", tag)) {

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        homePresenter.detachView();
        HomeTabManager.getInstance().destory();

    }

    @Override
    public void onBackPressed() {
//        boolean isInstanceof = mContentFragment instanceof MainLiveFragment;
//        if (!isInstanceof) {
//            bottomNavigationBar.fl_bottom_navigation_live.performClick();
//        } else {
//            exitBy2Click();
//        }
        exitBy2Click();
    }
}
