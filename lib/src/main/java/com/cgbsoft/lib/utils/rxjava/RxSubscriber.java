package com.cgbsoft.lib.utils.rxjava;

import android.util.Log;

import java.net.SocketTimeoutException;

import rx.Subscriber;

/**
 * Created by xiaoyu.zhang on 2016/8/8.
 */

public abstract class RxSubscriber<T> extends Subscriber<T> {
    private static final String TAG = "RxSubscriber";

    @Override
    public void onNext(T t) {
        try {
            onEvent(t);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, t.getClass().toString() + "\n" + e.getMessage());
            toReRxRegister();
        }
    }

    @Override
    public void onCompleted() {
    }

    @Override
    public void onError(Throwable error) {
        if (error instanceof SocketTimeoutException)
            error = new Throwable("网络不给力，请重新尝试");
        onRxError(error);
    }

    protected abstract void onEvent(T t);

    protected abstract void onRxError(Throwable error);

    //重新注册
    protected void toReRxRegister() {

    }
}
