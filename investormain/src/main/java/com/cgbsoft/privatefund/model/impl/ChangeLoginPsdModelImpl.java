package com.cgbsoft.privatefund.model.impl;

import android.text.TextUtils;

import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.utils.tools.LogUtils;
import com.cgbsoft.privatefund.model.ChangeLoginPsdModel;
import com.cgbsoft.privatefund.model.ChangeLoginPsdModelListener;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by sunfei on 2017/7/12 0012.
 */

public class ChangeLoginPsdModelImpl implements ChangeLoginPsdModel{
    @Override
    public void submitChangeRequest(CompositeSubscription subscription, ChangeLoginPsdModelListener listener,String userName,String oldPsd,String newPsd) {
        subscription.add(ApiClient.changeLoginPsdRequest(userName,oldPsd,newPsd).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                if (TextUtils.isEmpty(s)) {
                    listener.changePsdError(null);
                } else {
                    LogUtils.Log("aaa","s==="+s);
                    listener.changePsdSuccess(s);
                }
            }

            @Override
            protected void onRxError(Throwable error) {
                listener.changePsdError(error);
            }
        }));
    }
}
