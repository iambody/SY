package com.cgbsoft.privatefund.mvp.ui.home;

import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.WindowManager;

import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.base.webview.BaseWebview;
import com.cgbsoft.lib.base.webview.CwebNetConfig;
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
import com.chenenyu.router.annotation.Route;

import app.privatefund.com.im.listener.MyConnectionStatusListener;
import butterknife.BindView;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import rx.Observable;

@Route("investornmain_mainpageactivity")
public class MainPageActivity extends BaseActivity<MainPagePresenter> implements BottomNavigationBar.BottomClickListener, MainPageContract.View {
    private FragmentManager mFragmentManager;
    private Fragment mContentFragment;

    @BindView(R.id.bottomNavigationBar)
    BottomNavigationBar bottomNavigationBar;

    @BindView(R.id.webView)
    BaseWebview baseWebview;

    private Observable<Boolean> closeMainObservable;
    private Observable<Boolean> gestruePwdObservable;
    private boolean isOnlyClose;
    private int currentResId;

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
        baseWebview.loadUrls(CwebNetConfig.pageInit);

        bottomNavigationBar.setOnClickListener(this);
        bottomNavigationBar.setActivity(this);

        if (!SPreference.isThisRunOpenDownload(this))
            new DownloadDialog(this, true);
    }

    private void switchFragment(Fragment to) {
        if (mContentFragment != to) {
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
//            setCustomAnimations(R.anim.home_fade_in, R.anim.home_fade_out);
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
    protected void onStart() {
        super.onStart();
        if (RongIM.getInstance() != null && RongIM.getInstance().getRongIMClient() != null) {
            /**
             * 设置连接状态变化的监听器.
             */
            if (RongIM.getInstance().getRongIMClient().getCurrentConnectionStatus() == RongIMClient.ConnectionStatusListener.ConnectionStatus.DISCONNECTED)
                RongIM.getInstance().getRongIMClient().setConnectionStatusListener(new MyConnectionStatusListener());
        }
    }

    int switchID = -1;
    int currentPostion = -1;

    @Override
    public void onTabSelected(int position) {
        int switchID = -1;
        switch (position) {
            case 0://左1
                switchID = R.id.nav_left_first;
                currentPostion = 0;
                break;
            case 1://左2
                switchID = R.id.nav_left_second;
                currentPostion = 1;
                break;
            case 2://左3
                switchID = R.id.nav_right_first;
                currentPostion = 2;
                break;
            case 3://左4
                switchID = R.id.nav_right_second;
                currentPostion = 3;
                break;
            case 4://中间
                // getPresenter().toSignIn();
                switchID = R.id.nav_center;
                currentPostion = 4;
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

        gestruePwdObservable = RxBus.get().register(RxConstant.ON_ACTIVITY_RESUME_OBSERVABLE, Boolean.class);
        gestruePwdObservable.subscribe(new RxSubscriber<Boolean>() {
            @Override
            protected void onEvent(Boolean aBoolean) {
                if (SPreference.getToCBean(MainPageActivity.this) != null && "1".equals(SPreference.getToCBean(MainPageActivity.this).getGestureSwitch())) {
                    gesturePasswordJumpPage();
                }
            }

            @Override
            protected void onRxError(Throwable error) {

            }
        });
    }

    private void gesturePasswordJumpPage() {
        if (SPreference.getToCBean(this) != null && "1".equals(SPreference.getToCBean(this).getGestureSwitch()) && mContentFragment == MainTabManager.getInstance().getFragmentByIndex(0)) {
            /*Intent intent = new Intent(context, GestureVerifyActivity.class);
            intent.putExtra(GestureVerifyActivity.FROM_EXCCEED_TIIME, true);
            context.startActivity(intent);*/
        }
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

        Process.killProcess(Process.myPid());
        System.exit(1);
    }

    @Override
    public void onBackPressed() {
        if (1 == currentPostion && MainTabManager.getInstance().getProductFragment().isShow()) {
            MainTabManager.getInstance().getProductFragment().backClick();
        } else
            exitBy2Click();
    }
}
