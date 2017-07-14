package com.cgbsoft.privatefund.model.impl;

import com.cgbsoft.lib.base.model.SalonsEntity;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.privatefund.model.SalonsModel;
import com.cgbsoft.privatefund.model.SalonsModelListener;

import java.util.List;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by sunfei on 2017/7/13 0013.
 */

public class SalonsModelImpl implements SalonsModel {
    @Override
    public void getSalonsAndCitys(CompositeSubscription subscription, SalonsModelListener listener, String cityCode, int offset, int limit) {
        subscription.add(ApiClient.getSalonsAndCity(cityCode,offset,limit).subscribe(new RxSubscriber<SalonsEntity.Result>() {
            @Override
            protected void onEvent(SalonsEntity.Result result) {
                if (null == result) {
                    listener.getDataError(null);
                } else {
                    List<SalonsEntity.CityBean> citys = result.getCitys();
                    SalonsEntity.SalonBean salonBean = result.getSalons();
                    List<SalonsEntity.SalonItemBean> salons = salonBean.getRows();
                    listener.getDataSuccess(salons,citys);
                }
            }

            @Override
            protected void onRxError(Throwable error) {
                listener.getDataError(error);
            }
        }));
    }
}
