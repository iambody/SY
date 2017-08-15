package com.cgbsoft.privatefund.model;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by feigecal on 2017/7/12 0012.
 */

public interface DatumManageModel {
    void verifyIndentity(CompositeSubscription compositeSubscription, DatumManageModelListener listener);
}
