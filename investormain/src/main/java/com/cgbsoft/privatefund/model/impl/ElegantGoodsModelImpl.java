package com.cgbsoft.privatefund.model.impl;

import com.cgbsoft.lib.base.model.ElegantGoodsEntity;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.privatefund.model.ElegantGoodsModel;
import com.cgbsoft.privatefund.model.ElegantGoodsModelListener;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by sunfei on 2017/6/30 0030.
 */

public class ElegantGoodsModelImpl implements ElegantGoodsModel {
    @Override
    public void getElegantGoodsFirst(CompositeSubscription subscription, ElegantGoodsModelListener listener, int offset) {
        subscription.add(ApiClient.getElegantGoodsFirstObservable(offset).subscribe(new RxSubscriber<ElegantGoodsEntity.Result>() {
            @Override
            protected void onEvent(ElegantGoodsEntity.Result result) {
                if (null != result) {
                    listener.onModelFirstSuccess(result);
                } else {
                    listener.onModelFirstError(null);
                }
            }

            @Override
            protected void onRxError(Throwable error) {
                error.printStackTrace();
                listener.onModelFirstError(error);
            }
        }));
    }

    @Override
    public void getElegantGoodsMore(CompositeSubscription subscription, ElegantGoodsModelListener listener, int offset,String category) {
        subscription.add(ApiClient.getElegantGoodsMoreObservable(offset,category).subscribe(new RxSubscriber<ElegantGoodsEntity.ResultMore>() {
            @Override
            protected void onEvent(ElegantGoodsEntity.ResultMore resultMore) {
                if (null != resultMore) {
                    listener.onModelMoreSuccess(resultMore);
                } else {
                    listener.onModelMoreError(null);
                }
            }

            @Override
            protected void onRxError(Throwable error) {
                listener.onModelMoreError(error);
            }
        }));
    }
//    @Override
//    public void getElegantLivingBanners(CompositeSubscription subscription, ElegantLivingModelListener listener,int offset) {
//        subscription.add(ApiClient.getElegantLivingObservable(offset).subscribe(new RxSubscriber<ElegantLivingEntity.Result>() {
//            @Override
//            protected void onEvent(ElegantLivingEntity.Result result) {
//                if (null != result) {
//                    listener.onModelSuccess(result);
//                } else {
//                    listener.onModelError();
//                }
//            }
//
//            @Override
//            protected void onRxError(Throwable error) {
//                listener.onModelError();
//            }
//        }));
//    }
}
