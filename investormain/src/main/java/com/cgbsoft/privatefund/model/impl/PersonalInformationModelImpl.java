package com.cgbsoft.privatefund.model.impl;

import android.text.TextUtils;

import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.base.model.bean.CredentialStateMedel;
import com.cgbsoft.privatefund.model.PersonalInformationModel;
import com.cgbsoft.privatefund.model.PersonalInformationModelListener;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

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
                listener.uploadImgError(error);
            }
        }));
    }

    @Override
    public void verifyIndentity(CompositeSubscription subscription, PersonalInformationModelListener listener) {
        subscription.add(ApiClient.verifyIndentityInClient().subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                try {
                    JSONObject obj = new JSONObject(s);
                    JSONObject result = (JSONObject) obj.get("result");
                    String identity = result.getString("identity");
                    String hasIdCard = result.getString("hasIdCard");//用户选择的是身份证类型，1代表已经上传了身份证，0是还未传身份证
                    String title = result.getString("title");//
                    String credentialCode = result.getString("credentialCode");//
                    String stateName = result.getString("stateName");
                    String stateCodeOut = result.getString("stateCode");

                    JSONObject exist = (JSONObject) result.get("exist");
                    String customerName = exist.getString("customerName");
                    String credentialNumber = exist.getString("credentialNumber");
                    String credentialTitle = exist.getString("credentialTitle");
                    String stateCodeIn = exist.getString("stateCode");
                    listener.verifyIndentitySuccess(identity,hasIdCard,title,credentialCode,stateName,stateCodeOut,customerName,credentialNumber,credentialTitle,stateCodeIn);
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.verifyIndentityError(e);
                }
            }

            @Override
            protected void onRxError(Throwable error) {
                listener.verifyIndentityError(error);
            }
        }));
    }

    @Override
    public void verifyIndentityV3(CompositeSubscription subscription, PersonalInformationModelListener listener) {
        subscription.add(ApiClient.verifyIndentityInClientV3().subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONObject result = jsonObject.getJSONObject("result");
                    Gson g = new Gson();
                    CredentialStateMedel credentialStateMedel = g.fromJson(result.toString(), CredentialStateMedel.class);
                    listener.verifyIndentitySuccessV3(credentialStateMedel);
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.verifyIndentityError(e);
                }
            }

            @Override
            protected void onRxError(Throwable error) {
                listener.verifyIndentityError(error);
            }
        }));
    }


}
