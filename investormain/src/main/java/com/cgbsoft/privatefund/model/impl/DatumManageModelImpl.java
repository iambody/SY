package com.cgbsoft.privatefund.model.impl;

import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.utils.tools.LogUtils;
import com.cgbsoft.privatefund.model.CardCollectModelListener;
import com.cgbsoft.privatefund.model.CredentialModel;
import com.cgbsoft.privatefund.model.CredentialStateMedel;
import com.cgbsoft.privatefund.model.DatumManageModel;
import com.cgbsoft.privatefund.model.DatumManageModelListener;
import com.cgbsoft.privatefund.model.UploadIndentityModelListener;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

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


    public void uploadOtherCrenditial(CompositeSubscription subscription, DatumManageModelListener listener, List<String> remoteParams, String customerCode, String credentialCode, String remotePersonParams) {
        subscription.add(ApiClient.uploadOtherRemotePath(remoteParams, customerCode, credentialCode,remotePersonParams).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONObject result = jsonObject.getJSONObject("result");
                    String recognitionCode = result.getString("recognitionCode");
                    String recognitionMsg = result.getString("recognitionMsg");
                    if ("1".equals(recognitionCode)) {
                        listener.uploadOtherCrendtialSuccess(recognitionMsg);
                    }
                } catch (JSONException e) {
                    listener.uploadOtherCrendtialError(e);
                    e.printStackTrace();
                }

            }

            @Override
            protected void onRxError(Throwable error) {
                LogUtils.Log("aaa", "error===" + error.getMessage());
                listener.uploadOtherCrendtialError(error);
            }
        }));
    }


    public void verifyIndentityV3(CompositeSubscription subscription, DatumManageModelListener listener) {
        subscription.add(ApiClient.verifyIndentityInClientV3().subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONObject result = jsonObject.getJSONObject("result");
                    Gson gson = new Gson();
                    CredentialStateMedel credentialStateMedel = gson.fromJson(result.toString(), CredentialStateMedel.class);
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

    public void getLivingCount(CompositeSubscription subscription, DatumManageModelListener listener){
        subscription.add(ApiClient.getLivingCount().subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                listener.getLivingCountSuccess(s);
            }

            @Override
            protected void onRxError(Throwable error) {
                listener.getLivingCountError(error);
            }
        }));
    }

    public void getCardCollectLivingCount(CompositeSubscription subscription, CardCollectModelListener listener){
        subscription.add(ApiClient.getLivingCount().subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                listener.getLivingCountSuccess(s);
            }

            @Override
            protected void onRxError(Throwable error) {
                listener.getLivingCountError(error);
            }
        }));
    }

    public void getCredentialDetial(CompositeSubscription subscription, DatumManageModelListener listener,String credentialCode){
        subscription.add(ApiClient.getCredentialDetial(credentialCode).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                LogUtils.Log("getCredentialDetial", "onEvent===" + s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONObject result = jsonObject.getJSONObject("result");
                    Gson g = new Gson();
                    CredentialModel credentialModel = g.fromJson(result.toString(), CredentialModel.class);
                    listener.getCredentialDetialSuccess(credentialModel);
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.getCredentialDetialError(e);
                }
            }

            @Override
            protected void onRxError(Throwable error) {
                LogUtils.Log("aaa", "error===" + error.getMessage());
                listener.getCredentialDetialError(error);
            }
        }));
    }

}
