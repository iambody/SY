package com.cgbsoft.privatefund.model;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by sunfei on 2017/7/13 0013.
 */

public interface OldSalonsModel {
    void getOldSalons(CompositeSubscription subscription, OldSalonsModelListener listener,int offset, int limit);
}
