package com.cgbsoft.privatefund.model;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by sunfei on 2017/7/13 0013.
 */

public interface SalonsModel {
    void getSalonsAndCitys(CompositeSubscription subscription, SalonsModelListener listener, String cityCode, int offset, int limit);
}
