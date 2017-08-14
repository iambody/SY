package com.cgbsoft.privatefund.model;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by fei on 2017/8/10.
 */

public interface CardModel {
    void getCardList(CompositeSubscription subscription,CardListModelListener listener,String indentityCode);
}
