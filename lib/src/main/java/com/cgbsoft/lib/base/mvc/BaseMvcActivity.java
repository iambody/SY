package com.cgbsoft.lib.base.mvc;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/5/11-20:23
 */
public class BaseMvcActivity extends AppCompatActivity {
    protected CompositeSubscription mCompositeSubscription;
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
}
