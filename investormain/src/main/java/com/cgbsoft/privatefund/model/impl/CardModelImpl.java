package com.cgbsoft.privatefund.model.impl;

import com.cgbsoft.lib.base.model.CardListEntity;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.utils.tools.LogUtils;
import com.cgbsoft.privatefund.model.CardListModelListener;
import com.cgbsoft.privatefund.model.CardModel;

import java.util.List;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by fei on 2017/8/10.
 */

public class CardModelImpl implements CardModel {
    @Override
    public void getCardList(CompositeSubscription subscription, CardListModelListener listener,String indentityCode) {
        subscription.add(ApiClient.getIndentityList(indentityCode).subscribe(new RxSubscriber<CardListEntity.Result>() {
            @Override
            protected void onEvent(CardListEntity.Result result) {
                if (null == result) {
                    listener.getDataError(null);
                } else {
                    List<CardListEntity.CardBean> credentials = result.getCredentials();
                    listener.getDataSuccess(credentials);
                }
            }

            @Override
            protected void onRxError(Throwable error) {
                listener.getDataError(error);
            }
        }));
    }
}
