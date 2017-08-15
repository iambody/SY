package com.cgbsoft.privatefund.model.impl;

import android.text.TextUtils;

import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.privatefund.model.DatumManageModel;
import com.cgbsoft.privatefund.model.DatumManageModelListener;
import com.cgbsoft.privatefund.model.PersonalInformationModel;
import com.cgbsoft.privatefund.model.PersonalInformationModelListener;

import org.json.JSONException;
import org.json.JSONObject;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by feigecal on 2017/7/12 0012.
 */

public class DatumManageModelImpl implements DatumManageModel {

    @Override
    public void verifyIndentity(CompositeSubscription subscription, DatumManageModelListener listener) {
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
                    listener.verifyIndentitySuccess(identity,hasIdCard,title,credentialCode);
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
