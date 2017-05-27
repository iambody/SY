package com.cgbsoft.lib.base.mvp.presenter.impl;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.Toast;

import com.cgbsoft.lib.base.mvp.view.BaseView;
import com.cgbsoft.lib.utils.constant.RxConstant;

import org.json.JSONException;
import org.json.JSONObject;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by user on 2016/11/4.
 */

public abstract class BasePresenterImpl<V extends BaseView> implements RxConstant {
    private V view;
    private Context mContext;
    private Toast toast = null;
    private long oneTime;
    private String oldMsg;

    private CompositeSubscription mCompositeSubscription;

    public BasePresenterImpl(@NonNull Context context, @NonNull V view) {
        this.view = view;
        this.mContext = context;
        mCompositeSubscription = new CompositeSubscription();
    }

    protected V getView() {
        return view;
    }

    protected Context getContext() {
        return mContext;
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
        mContext = null;
    }

    protected void showToast(int resId) {
        if (getContext() == null) {
            return;
        }
        String s = getContext().getResources().getString(resId);
        showToast(s);
    }

    protected void showToast(String s) {
        if (s == null || TextUtils.isEmpty(s) || getContext() == null)
            return;
        if (s.contains("failed to connect to") || s.contains("502") || s.contains("404") || s.contains("failed to connect to"))
            s = "网络不给力，请重新尝试";
        if (toast == null) {
            toast = Toast.makeText(getContext(), s, Toast.LENGTH_SHORT);
            toast.show();
            oneTime = System.currentTimeMillis();
        } else {
            long twoTime = System.currentTimeMillis();
            if (s.equals(oldMsg)) {
                if (twoTime - oneTime > Toast.LENGTH_SHORT) {
                    toast.show();
                }
            } else {
                oldMsg = s;
                toast.setText(s);
                toast.show();
            }
            oneTime = twoTime;
        }
    }

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
