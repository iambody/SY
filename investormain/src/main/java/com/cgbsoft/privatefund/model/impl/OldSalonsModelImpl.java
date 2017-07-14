package com.cgbsoft.privatefund.model.impl;

import com.cgbsoft.lib.base.model.OldSalonsEntity;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.privatefund.model.OldSalonsModel;
import com.cgbsoft.privatefund.model.OldSalonsModelListener;

import java.util.List;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by sunfei on 2017/7/13 0013.
 */

public class OldSalonsModelImpl implements OldSalonsModel {
    @Override
    public void getOldSalons(CompositeSubscription subscription, OldSalonsModelListener listener, int offset, int limit) {
        subscription.add(ApiClient.getOldSalons(offset,limit).subscribe(new RxSubscriber<OldSalonsEntity.SalonBean>() {
            @Override
            protected void onEvent(OldSalonsEntity.SalonBean result) {
                if (null == result) {
                    listener.getDataError(null);
                } else {
                    List<OldSalonsEntity.SalonItemBean> rows = result.getRows();
                    listener.getDataSuccess(rows);
                }
            }

            @Override
            protected void onRxError(Throwable error) {
                listener.getDataError(error);
            }
        }));
    }
}
