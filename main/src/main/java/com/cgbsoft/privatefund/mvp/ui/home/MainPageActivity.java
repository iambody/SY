package com.cgbsoft.privatefund.mvp.ui.home;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.WindowManager;

import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.utils.constant.RxConstant;
import com.cgbsoft.lib.utils.rxjava.RxBus;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.utils.service.FloatVideoService;
import com.cgbsoft.lib.widget.DownloadDialog;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.mvp.contract.home.MainPageContract;
import com.cgbsoft.privatefund.mvp.presenter.home.MainPagePresenter;
import com.cgbsoft.privatefund.utils.MainTabManager;
import com.cgbsoft.privatefund.widget.navigation.BottomNavigationBar;

import butterknife.BindView;
import rx.Observable;

public class MainPageActivity extends BaseActivity<MainPagePresenter> implements BottomNavigationBar.BottomClickListener, MainPageContract.View {
    private FragmentManager mFragmentManager;
    private Fragment mContentFragment;

    @BindView(R.id.bottomNavigationBar)
    BottomNavigationBar bottomNavigationBar;

    private Observable<Boolean> closeMainObservable;
    private boolean isOnlyClose;

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
    protected void init(Bundle savedInstanceState) {
        mFragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        mContentFragment = MainTabManager.getInstance().getFragmentByIndex(R.id.nav_left_first);

        transaction.add(R.id.fl_main_content, mContentFragment);
        transaction.commitAllowingStateLoss();

        initRxObservable();
    }

    @Override
    protected MainPagePresenter createPresenter() {
        return new MainPagePresenter(this, this);
    }

    @Override
    protected void data() {
        bottomNavigationBar.setOnClickListener(this);
        bottomNavigationBar.setActivity(this);

        if (!SPreference.isThisRunOpenDownload(this))
            new DownloadDialog(this, true);
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
        switch (position) {
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
        int switchID = -1;
        switch (position) {
            case 0://左1
                switchID = R.id.nav_left_first;
                break;
            case 1://左2
                switchID = R.id.nav_left_second;
                break;
            case 2://左3
                switchID = R.id.nav_right_first;
                break;
            case 3://左4
                switchID = R.id.nav_right_second;
                break;
            case 4://中间
                getPresenter().toSignIn();
                switchID = R.id.nav_center;
                break;
        }
        switchFragment(MainTabManager.getInstance().getFragmentByIndex(switchID));
    }

    private void initRxObservable() {
        closeMainObservable = RxBus.get().register(RxConstant.CLOSE_MAIN_OBSERVABLE, Boolean.class);
        closeMainObservable.subscribe(new RxSubscriber<Boolean>() {
            @Override
            protected void onEvent(Boolean aBoolean) {
                isOnlyClose = aBoolean;
                finish();
            }

            @Override
            protected void onRxError(Throwable error) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        if (closeMainObservable != null) {
            RxBus.get().unregister(RxConstant.CLOSE_MAIN_OBSERVABLE, closeMainObservable);
        }

        super.onDestroy();
        MainTabManager.getInstance().destory();
        FloatVideoService.stopService();

        if (isOnlyClose) {
            return;
        }

        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

    @Override
    public void onBackPressed() {
        exitBy2Click();
    }
}
