package com.cgbsoft.privatefund.model;

import com.cgbsoft.privatefund.mvp.presenter.center.CardCollectPresenterImpl;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by fei on 2017/8/10.
 */

public interface CardModel {
    void getCardList(CompositeSubscription subscription,CardListModelListener listener,String indentityCode);

    void getCardListAdd(CompositeSubscription subscription, CardCollectPresenterImpl listener, String indentityCode);
}
