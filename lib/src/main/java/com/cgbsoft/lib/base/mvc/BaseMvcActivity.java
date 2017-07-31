package com.cgbsoft.lib.base.mvc;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.cgbsoft.lib.AppInfStore;
import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.InvestorAppli;
import com.cgbsoft.lib.R;
import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.utils.constant.Constant;
import com.cgbsoft.lib.utils.constant.RxConstant;
import com.cgbsoft.lib.utils.rxjava.RxBus;
import com.cgbsoft.lib.utils.tools.NavigationUtils;
import com.cgbsoft.lib.widget.dialog.DefaultDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/5/11-20:23
 */
public class BaseMvcActivity extends AppCompatActivity implements  BaseContant {

    /**
     * 标题栏，左侧的返回按钮
     */
    protected ImageView titleLeft;
    /**
     * 标题栏，中间的标题显示，右侧的分享等按钮
     */
    protected TextView titleMid, titleRight;

    protected TextView jumpText;
    //网络订阅器
    protected CompositeSubscription mCompositeSubscription;
    //上下文
    protected Activity baseContext;
    private LogoutReceiver receiver;
    private LocalBroadcastManager manager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.BLACK);
        }
        initConifg();
        registerLogoutBroadcast();
    }

    /**
     * 监听用户被踢出的广播
     */
    private void registerLogoutBroadcast() {
        manager = LocalBroadcastManager.getInstance(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.RECEIVER_EXIT_ACTION);
        receiver = new LogoutReceiver();
        manager.registerReceiver(receiver, filter);
    }

    /**
     * 取消监听用户被踢出的广播
     */
    private void unRegisterLogoutBroadcast(){
        if (null != manager && null != receiver) {
            manager.unregisterReceiver(receiver);
        }
    }

    class LogoutReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null)
                return;
            String action = intent.getAction();
            if (TextUtils.equals(action, Constant.RECEIVER_EXIT_ACTION)) {
                int mCode = intent.getIntExtra(Constant.RECEIVER_ERRORCODE, -1);
                String msg = "";
                if (mCode == 510) {
                    msg = getString(R.string.token_error_510_str);
                } else if (mCode == 511) {
                    msg = getString(R.string.token_error_511_str);
                }

                boolean dialogShow = AppManager.getDialogShow(BaseMvcActivity.this);
                if (!dialogShow) {
                    DefaultDialog dialog = new DefaultDialog(BaseMvcActivity.this, msg, null, "确认"){

                        @Override
                        public void left() {

                        }

                        @Override
                        public void right() {
                            relogin();
                            dismiss();
                            AppInfStore.saveDialogTag(BaseMvcActivity.this,false);
                        }
                    };
                    //                dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                    dialog.setCancelable(false);
                    dialog.show();
                    AppInfStore.saveDialogTag(BaseMvcActivity.this,true);
                }
            }
            if (TextUtils.equals(action, Constant.VISITER_ERRORCODE)) {
                NavigationUtils.startActivityByRouter(BaseMvcActivity.this, RouteConfig.GOTO_LOGIN);
            }
        }
    }
    private void relogin() {
//        floatView.removeFromWindow();
        NavigationUtils.startActivityByRouter(this, RouteConfig.GOTO_LOGIN);
        RxBus.get().post(RxConstant.LOGIN_STATUS_DISABLE_OBSERVABLE,0);
//        Intent intent = new Intent();
//        intent.setClass(this, LoginActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);


//        stopService();
    }

    private void initConifg() {
        baseContext = BaseMvcActivity.this;
        mCompositeSubscription = new CompositeSubscription();
    }

    private void onUnsubscribe() {
        if (mCompositeSubscription != null && mCompositeSubscription.hasSubscriptions()) {
            mCompositeSubscription.unsubscribe();
        }
        mCompositeSubscription = null;
        baseContext = null;
    }
    //订阅
    protected void addSubscription(Subscription subscription) {
        mCompositeSubscription.add(subscription);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        onUnsubscribe();
        unRegisterLogoutBroadcast();
    }




    protected void showTileLeft() {
        if (null != titleLeft) {
            titleLeft.setVisibility(View.VISIBLE);
        }
    }

    protected void showTileRight(String text) {
        if (null != titleRight) {
            titleRight.setVisibility(View.VISIBLE);
            titleRight.setText(text);
        }
    }

    protected void showTileMid(String text) {
        if (null != titleMid) {
            titleMid.setText(text);
        }
    }
    public void resetStatusBar() {
        if (android.os.Build.VERSION.SDK_INT > 18) {
            Class<?> c = null;
            Object obj = null;
            Field field = null;
            int x = 0, statusBarHeight = 0;
            try {
                c = Class.forName("com.android.internal.R$dimen");
                obj = c.newInstance();
                field = c.getField("status_bar_height");
                x = Integer.parseInt(field.get(obj).toString());
                statusBarHeight = getResources().getDimensionPixelSize(x);

                View view = findViewById(R.id.main_main_container);

                if (null != view) {
                    view.setPadding(0, statusBarHeight, 0, 0);
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }
    /**
     * 初始化标题兰按钮
     */
    protected void initRegisterTitleBar() {
        titleLeft = (ImageView) findViewById(R.id.title_left);
        titleMid = (TextView) findViewById(R.id.title_mid);
        titleRight = (TextView) findViewById(R.id.title_right);

        if (null != titleLeft) {
            titleLeft.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

    /**
     * 获取result数据
     * @param resultStr
     * @return
     */
    protected String getV2String(String resultStr) {

        try {
            JSONObject obj = new JSONObject(resultStr);
            return obj.getString("result");
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }
}
