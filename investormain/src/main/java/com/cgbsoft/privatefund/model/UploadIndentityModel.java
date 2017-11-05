package com.cgbsoft.privatefund.model;

import java.util.List;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by fei on 2017/8/12.
 */

public interface UploadIndentityModel {
    /**
     *
     * @param subscription
     * @param listener
     * @param remoteParams
     * @param customerCode 父级code
     * @param credentialCode 子级code
     */
    void uploadIndentity(CompositeSubscription subscription, UploadIndentityModelListener listener,List<String> remoteParams,String customerCode,String credentialCode);

    void credentialDetail(CompositeSubscription subscription, CredentialModelListener listener,String credentialCode);
}
