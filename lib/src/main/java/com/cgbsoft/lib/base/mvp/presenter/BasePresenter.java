package com.cgbsoft.lib.base.mvp.presenter;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by user on 2016/11/4.
 */

public abstract class BasePresenter<V> {
    private V view;
    private CompositeSubscription mCompositeSubscription;

    public BasePresenter(V view) {
        this.view = view;
        mCompositeSubscription = new CompositeSubscription();
    }

    protected V getView() {
        return view;
    }

    public void detachView() {
        this.view = null;
        onUnsubscribe();
    }


    //订阅
    protected void addSubscription(Subscription subscription) {
        mCompositeSubscription.add(subscription);
    }

    //RXjava取消注册，以避免内存泄露
    private void onUnsubscribe() {
        if (mCompositeSubscription != null && mCompositeSubscription.hasSubscriptions()) {
            mCompositeSubscription.unsubscribe();
        }
        mCompositeSubscription = null;
    }


}
