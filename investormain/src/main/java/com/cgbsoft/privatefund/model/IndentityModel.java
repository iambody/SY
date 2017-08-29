package com.cgbsoft.privatefund.model;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by fei on 2017/8/11.
 */

public interface IndentityModel {
    void getIndentitys(CompositeSubscription subscription, IndentityModelListener listener);
}
