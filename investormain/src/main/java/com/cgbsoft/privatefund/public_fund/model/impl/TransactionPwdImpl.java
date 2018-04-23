package com.cgbsoft.privatefund.public_fund.model.impl;

import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.privatefund.public_fund.model.TransactionPwdListener;
import com.cgbsoft.privatefund.public_fund.model.TransactionPwdModle;

import java.util.HashMap;

import rx.subscriptions.CompositeSubscription;

/**
 * desc  ${DESC}
 * author wangyongkui
 */
public class TransactionPwdImpl implements TransactionPwdModle {
    @Override
    public void transactionPwdAction(CompositeSubscription subscription, HashMap<String, Object> hashMap, TransactionPwdListener transactionPwdListener) {
        subscription.add(ApiClient.ActionPoint(hashMap).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                transactionPwdListener.getTransactionPwdSuccess(s);
            }

            @Override
            protected void onRxError(Throwable error) {
                transactionPwdListener.getTransactionPwdError(error.getMessage());
            }
        }));
    }
}
