package com.cgbsoft.privatefund.model.impl;

import com.cgbsoft.lib.base.model.IndentityEntity;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.privatefund.model.IndentityModel;
import com.cgbsoft.privatefund.model.IndentityModelListener;

import java.util.List;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by fei on 2017/8/11.
 */

public class IndentityModelImpl implements IndentityModel {
    @Override
    public void getIndentitys(CompositeSubscription subscription, IndentityModelListener listener) {
        subscription.add(ApiClient.getIndentityObservable().subscribe(new RxSubscriber<List<IndentityEntity.IndentityBean>>() {
            @Override
            protected void onEvent(List<IndentityEntity.IndentityBean> indentityBeen) {
                listener.getIndentityListSuccess(indentityBeen);
//                List<IndentityEntity.IndentityBean> indentityBeans = indentityBeen;
//                if (indentityBeans.size() == 2) {
//                    IndentityEntity.IndentityBean indentityBean = indentityBeans.get(0);
//                    String beanCode = indentityBean.getCode();
//                    List<IndentityEntity.IndentityItem> personItems;
//                    List<IndentityEntity.IndentityItem> institutionItems;
//                    if (beanCode.equals("10")) {//自然人
//                        personItems = indentityBean.getResult();
//                        IndentityEntity.IndentityBean institutionBean = indentityBeans.get(1);
//                        institutionItems = institutionBean.getResult();
//                    } else if(beanCode.equals("20")){
//                        institutionItems = indentityBean.getResult();
//                        IndentityEntity.IndentityBean institutionBean = indentityBeans.get(1);
//                        personItems = institutionBean.getResult();
//                    }
//                    listener.getIndentityListSuccess(personItems,institutionItems);
//                } else {
//                    listener.getIndentityListError(null);
//                }
            }

            @Override
            protected void onRxError(Throwable error) {
                listener.getIndentityListError(error);
            }
        }));
    }
}