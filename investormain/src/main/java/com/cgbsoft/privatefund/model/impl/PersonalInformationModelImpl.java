package com.cgbsoft.privatefund.model.impl;

import android.text.TextUtils;

import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.privatefund.model.PersonalInformationModel;
import com.cgbsoft.privatefund.model.PersonalInformationModelListener;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by feigecal on 2017/7/12 0012.
 */

public class PersonalInformationModelImpl implements PersonalInformationModel {
    @Override
    public void updateUserInfoToServer(CompositeSubscription subscription, PersonalInformationModelListener listener, String userName, String
            gender, String birthday) {
        subscription.add(ApiClient.updateUserInfoNewC(userName,gender,birthday).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                if (TextUtils.isEmpty(s)) {
                    listener.updateError(null);
                } else {
                    listener.updateSuccess();
                }
            }

            @Override
            protected void onRxError(Throwable error) {
                listener.updateError(null);
            }
        }));
    }
    /**
     * 上传头像的远程路径给服务端
     */
    @Override
    public void uploadRemotePath(CompositeSubscription subscription, PersonalInformationModelListener listener,String adviserId, String
            imageRemotePath) {
        subscription.add(ApiClient.uploadIconRemotePath(adviserId,imageRemotePath).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                if (TextUtils.isEmpty(s)) {
                    listener.uploadImgError(null);
                } else {
                    listener.uploadImgSuccess();
                }
            }

            @Override
            protected void onRxError(Throwable error) {
                listener.uploadImgError(null);
            }
        }));
    }
}
