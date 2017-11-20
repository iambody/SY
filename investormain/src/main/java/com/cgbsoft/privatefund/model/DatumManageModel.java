package com.cgbsoft.privatefund.model;

import java.util.List;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by feigecal on 2017/7/12 0012.
 */

public interface DatumManageModel {
    void verifyIndentity(CompositeSubscription compositeSubscription, DatumManageModelListener listener);
    void uploadOtherCrenditial(CompositeSubscription subscription, DatumManageModelListener listener, List<String> remoteParams, String customerCode, String credentialCode, String remotePersonParams);
}
