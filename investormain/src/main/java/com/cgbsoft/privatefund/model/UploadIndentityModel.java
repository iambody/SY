package com.cgbsoft.privatefund.model;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by fei on 2017/8/12.
 */

public interface UploadIndentityModel {
    void uploadIndentity(CompositeSubscription subscription, UploadIndentityModelListener listener);
}
