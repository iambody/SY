package com.cgbsoft.privatefund.model.impl;

import android.text.TextUtils;

import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.utils.tools.LogUtils;
import com.cgbsoft.privatefund.model.CredentialModelListener;
import com.cgbsoft.privatefund.model.UploadIndentityModel;
import com.cgbsoft.privatefund.model.UploadIndentityModelListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by fei on 2017/8/12.
 */

public class UploadIndentityModelImpl implements UploadIndentityModel {
    @Override
    public void uploadIndentity(CompositeSubscription subscription, UploadIndentityModelListener listener, List<String> remoteParams, String customerCode, String credentialCode) {
        subscription.add(ApiClient.uploadIndentityRemotePath(remoteParams, customerCode, credentialCode).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String code = jsonObject.getString("code");
                    if (TextUtils.isEmpty(code) || "100004".equals(code)) {
                        listener.uploadIndentitySuccess(null);
                    } else {
                        JSONObject result = (JSONObject) jsonObject.get("result");
                        String errorMsg = result.getString("errorMsg");
                        listener.uploadIndentitySuccess(errorMsg);
                    }
                } catch (JSONException e) {
                    listener.uploadIndentitySuccess(e.getMessage());
                    e.printStackTrace();
                }

            }

            @Override
            protected void onRxError(Throwable error) {
                LogUtils.Log("aaa", "error===" + error.getMessage());
                listener.uploadIndentityError(error);
            }
        }));
    }

    @Override
    public void credentialDetail(CompositeSubscription subscription, CredentialModelListener listener, String credentialCode) {
        subscription.add(ApiClient.getCredentialDetial(credentialCode).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                LogUtils.Log("getCredentialDetial", "onEvent===" + s);
                listener.getCrentialSuccess(s);
            }

            @Override
            protected void onRxError(Throwable error) {
                LogUtils.Log("aaa", "error===" + error.getMessage());
                listener.getCrentialError(error);
            }
        }));
    }
}
