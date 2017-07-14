package com.cgbsoft.privatefund.model;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by feigecal on 2017/7/12 0012.
 */

public interface PersonalInformationModel {
    void updateUserInfoToServer(CompositeSubscription subscription, PersonalInformationModelListener listener,String userName,String gender,String birthday);


    void uploadRemotePath(CompositeSubscription compositeSubscription, PersonalInformationModelListener listener,String adviserId, String imageId);
}
