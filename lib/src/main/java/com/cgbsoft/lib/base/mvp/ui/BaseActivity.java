package com.cgbsoft.lib.base.mvp.ui;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.badoo.mobile.util.WeakHandler;
import com.cgbsoft.lib.Appli;
import com.cgbsoft.lib.R;
import com.cgbsoft.lib.base.model.bean.DaoSession;
import com.cgbsoft.lib.utils.cache.OtherDataProvider;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.utils.constant.Constant;
import com.cgbsoft.lib.widget.MToast;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Subscription;

/**
 * TODO 基础activity 默认开启透明导航条
 * Created by user on 2016/11/4.
 */

public abstract class BaseActivity extends RxAppCompatActivity implements Constant {
    private Appli mAppli;
    protected boolean isNeedAdapterPhone = true;
    protected boolean isNeedGoneNavigationBar;
    protected WeakHandler baseHandler;
    private Subscription subscription;
    private long mExitPressedTime = 0;
    private DaoSession daoSession;
    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        before();
        if (layoutID() > 0)
            setContentView(layoutID());
        after();
        init();
        data();
    }

    protected void before() {
        mAppli = (Appli) getApplication();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        OtherDataProvider.addTopActivity(getApplicationContext(), getClass().getName());
    }

    protected abstract int layoutID();

    protected abstract void init();

    protected void after() {
        unbinder = ButterKnife.bind(this);
        baseHandler = new WeakHandler();

        if (isNeedAdapterPhone && !isNeedAdapterPhone()) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                //透明状态栏
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                //透明导航栏
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            }
        }
        if (isNeedGoneNavigationBar) {
            baseHandler.post(mHideRunnable);
            final View decorView = getWindow().getDecorView();
            decorView.setOnSystemUiVisibilityChangeListener(visibility -> {
                baseHandler.post(mHideRunnable); // hide the navigation bar
            });
        }
    }

    protected void data() {

    }

    protected boolean isNeedAdapterPhone() {
        if (Build.VERSION.SDK_INT > 21) {
            return false;
        } else if (android.os.Build.MODEL.toLowerCase().contains("vivo")) {
            return false;
        } else {
            return true;
        }
    }


    protected Appli getAppli() {
        return mAppli;
    }

    protected DaoSession getDaoSession() {
        if (daoSession == null) {
            daoSession = getAppli().getDaoSession();
        }
        return daoSession;
    }

    protected Runnable mHideRunnable = () -> {
        int flags;
        int curApiVersion = Build.VERSION.SDK_INT;
        // This work only for android 4.4+
        if (curApiVersion >= Build.VERSION_CODES.KITKAT) {
            // This work only for android 4.4+
            // hide navigation bar permanently in android activity
            // touch the screen, the navigation bar will not show
            flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE;
//                    | View.SYSTEM_UI_FLAG_FULLSCREEN;

        } else {
            // touch the screen, the navigation bar will show
            flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        }

        // must be executed in main thread :)
        getWindow().getDecorView().setSystemUiVisibility(flags);
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        baseHandler.removeCallbacksAndMessages(null);
        baseHandler = null;
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        boolean isCurrentRunningForeground = SPreference.isCurrentRunningForeground(this);

//        Observable.just(1).filter(v-> !isCurrentRunningForeground)

    }


    /**
     * 双击退出。
     */
    protected void exitBy2Click() {
        long mNowTime = System.currentTimeMillis();//获取第一次按键时间
        if ((mNowTime - mExitPressedTime) > 2000) {//比较两次按键时间差
            MToast.makeText(this, getString(R.string.nav_back_again_finish), Toast.LENGTH_SHORT);
            mExitPressedTime = mNowTime;
        } else {
            finish();
        }
    }
}
