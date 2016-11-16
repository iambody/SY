package com.cgbsoft.privatefund.mvp.ui.home;

import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.WindowManager;

import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.mvp.presenter.HomePresenter;
import com.cgbsoft.privatefund.mvp.view.HomeView;
import com.cgbsoft.privatefund.utils.MainTabManager;
import com.cgbsoft.privatefund.widget.navigation.BottomNavigationBar;

import butterknife.BindView;

public class MainPageActivity extends BaseActivity implements HomeView, BottomNavigationBar.BottomClickListener {
    private FragmentManager mFragmentManager;
    private Fragment mContentFragment;
    private HomePresenter homePresenter;

    @BindView(R.id.bottomNavigationBar)
    BottomNavigationBar bottomNavigationBar;

    @Override
    protected int layoutID() {
        return R.layout.activity_main_page;
    }

    @Override
    protected void after() {
        super.after();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    @Override
    protected void init() {
        homePresenter = new HomePresenter(this);
        mFragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        mContentFragment = MainTabManager.getInstance().getFragmentByIndex(R.id.nav_left_first);

//        transaction.add(R.id.fl_main_content, mContentFragment);
//        transaction.commitAllowingStateLoss();
    }

    @Override
    protected void data() {
        SPreference.toDataMigration(this);
        bottomNavigationBar.setOnClickListener(this);
        bottomNavigationBar.setActivity(this);
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
    public void onCloudMenuClick(int position) {
        switch (position){
            case 0://呼叫投资顾问

                break;
            case 1://对话

                break;
            case 2://直播

                break;
            case 3://短信

                break;
            case 4://客服

                break;
        }
    }

    @Override
    public void onTabSelected(int position) {
        switch (position){
            case 0://左1

                break;
            case 1://左2

                break;
            case 2://左3

                break;
            case 3://左4

                break;
            case 4://中间

                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        homePresenter.detachView();
        MainTabManager.getInstance().destory();

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
