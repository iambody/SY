package com.cgbsoft.privatefund.model.impl;

import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.privatefund.model.UploadIndentityModel;
import com.cgbsoft.privatefund.model.UploadIndentityModelListener;

import java.util.List;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by fei on 2017/8/12.
 */

public class UploadIndentityModelImpl implements UploadIndentityModel {
    @Override
    public void uploadIndentity(CompositeSubscription subscription, UploadIndentityModelListener listener,List<String> remoteParams,String customerCode,String credentialCode) {
        subscription.add(ApiClient.uploadIndentityRemotePath(remoteParams,customerCode,credentialCode).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {

            }

            @Override
            protected void onRxError(Throwable error) {

            }
        }));
    }
}
