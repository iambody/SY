package com.cgbsoft.privatefund.model.impl;

import com.cgbsoft.lib.base.model.ElegantLivingEntity;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.privatefund.model.ElegantLivingModel;
import com.cgbsoft.privatefund.model.ElegantLivingModelListener;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by sunfei on 2017/6/30 0030.
 */

public class ElegantLivingModelImpl implements ElegantLivingModel {
    @Override
    public void getElegantLivingBanners(CompositeSubscription subscription, ElegantLivingModelListener listener,int offset) {
        subscription.add(ApiClient.getElegantLivingObservable(offset).subscribe(new RxSubscriber<ElegantLivingEntity.Result>() {
            @Override
            protected void onEvent(ElegantLivingEntity.Result result) {
                if (null != result) {
                    listener.onModelSuccess(result);
                } else {
                    listener.onModelError();
                }
            }

            @Override
            protected void onRxError(Throwable error) {
                listener.onModelError();
            }
        }));
    }
}
