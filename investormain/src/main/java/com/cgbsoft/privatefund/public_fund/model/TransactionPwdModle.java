package com.cgbsoft.privatefund.public_fund.model;

import java.util.HashMap;

import rx.subscriptions.CompositeSubscription;

/**
 * desc  ${DESC}
 * author wangyongkui
 */
public interface  TransactionPwdModle {
    public void transactionPwdAction(CompositeSubscription subscription,HashMap<String, String> hashMap,TransactionPwdListener transactionPwdListener);

    public void getOperationInf(CompositeSubscription subscription,TransactionPwdListener transactionPwdListener);
}
