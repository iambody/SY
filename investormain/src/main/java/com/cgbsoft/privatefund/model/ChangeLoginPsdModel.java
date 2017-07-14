package com.cgbsoft.privatefund.model;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by sunfei on 2017/7/12 0012.
 */

public interface ChangeLoginPsdModel {
    void submitChangeRequest(CompositeSubscription subscription, ChangeLoginPsdModelListener listener,String userName,String oldPsd,String newPsd);
}
