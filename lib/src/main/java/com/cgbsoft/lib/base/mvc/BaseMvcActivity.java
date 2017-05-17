package com.cgbsoft.lib.base.mvc;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cgbsoft.lib.R;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initConifg();
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
}
