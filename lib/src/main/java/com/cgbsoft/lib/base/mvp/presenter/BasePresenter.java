package com.cgbsoft.lib.base.mvp.presenter;

import com.cgbsoft.lib.utils.rxjava.RxSchedulersHelper;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;

import rx.Observable;
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

    public V getView(){
        return view;
    }

    public void detachView() {
        this.view = null;
        onUnsubscribe();
    }


    //订阅
    public <T> void addSubscription(Observable<T> observable, RxSubscriber<T> subscriber) {
        mCompositeSubscription.add(observable.compose(RxSchedulersHelper.io_main()).subscribe(subscriber));
    }

    //RXjava取消注册，以避免内存泄露
    private void onUnsubscribe() {
        if (mCompositeSubscription != null && mCompositeSubscription.hasSubscriptions()) {
            mCompositeSubscription.unsubscribe();
        }
        mCompositeSubscription = null;
    }


}
