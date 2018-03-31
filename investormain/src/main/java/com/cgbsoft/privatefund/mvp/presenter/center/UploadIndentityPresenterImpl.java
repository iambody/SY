package com.cgbsoft.privatefund.mvp.presenter.center;

import android.content.Context;
import android.support.annotation.NonNull;

import com.cgbsoft.lib.base.model.bean.CredentialStateMedel;
import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.privatefund.model.CredentialModelListener;
import com.cgbsoft.privatefund.model.UploadIndentityModelListener;
import com.cgbsoft.privatefund.model.impl.UploadIndentityModelImpl;
import com.cgbsoft.privatefund.mvp.contract.center.CredentialDetialContract;
import com.cgbsoft.privatefund.mvp.contract.center.UploadIndentityContract;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by fei on 2017/8/12.
 */

public class UploadIndentityPresenterImpl extends BasePresenterImpl<UploadIndentityContract.UploadIndentityView> implements UploadIndentityContract.UploadIndentityPresenter ,CredentialDetialContract.CredentialDetialPresenter,UploadIndentityModelListener,CredentialModelListener{
    private final UploadIndentityContract.UploadIndentityView uploadIndentityView;
    private final UploadIndentityModelImpl uploadModel;

    public UploadIndentityPresenterImpl(@NonNull Context context, @NonNull UploadIndentityContract.UploadIndentityView view) {
        super(context, view);
        this.uploadIndentityView=view;
        uploadModel=new UploadIndentityModelImpl();
    }

    @Override
    public void uploadIndentity(List<String> remoteParams,String customerCode,String credentialCode) {
        uploadIndentityView.showLoadDialog();
        uploadModel.uploadIndentity(getCompositeSubscription(),this,remoteParams,customerCode,credentialCode);
    }

    @Override
    public void uploadOtherCrenditial(List<String> remoteParams, String customerCode, String credentialCode, String remotePerson) {
        uploadIndentityView.showLoadDialog();
        uploadModel.uploadOtherCrenditial(getCompositeSubscription(),this,remoteParams,customerCode,credentialCode,remotePerson);
    }

    @Override
    public void getLivingCount() {
        uploadIndentityView.showLoadDialog();
        uploadModel.getLivingCount(getCompositeSubscription(),this);
    }

    @Override
    public void getCredentialInfo(String credentialId) {
        uploadIndentityView.showLoadDialog();
        uploadModel.credentialDetail(getCompositeSubscription(),this,credentialId);
    }

    /**
     * 获取证件基本信息
     */
    @Override
    public void verifyIndentityV3() {
        addSubscription(ApiClient.verifyIndentityInClientV3().subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONObject result = jsonObject.getJSONObject("result");
                    Gson gson = new Gson();
                    CredentialStateMedel credentialStateMedel = gson.fromJson(result.toString(), CredentialStateMedel.class);
                    getView().verifyIndentitySuccessV3(credentialStateMedel);
                } catch (JSONException e) {
                    e.printStackTrace();
                    getView().verifyIndentityError(e);
                }
            }

            @Override
            protected void onRxError(Throwable error) {
                getView().verifyIndentityError(error);
            }
        }));
    }

    @Override
    public void uploadIndentitySuccess(String s) {
        uploadIndentityView.hideLoadDialog();
        uploadIndentityView.uploadIndentitySuccess(s);
    }

    @Override
    public void uploadIndentityError(Throwable error) {
        uploadIndentityView.hideLoadDialog();
        uploadIndentityView.uploadIndentityError(error);
    }

    @Override
    public void uploadOtherCrendtialSuccess(String result) {
        uploadIndentityView.hideLoadDialog();
        uploadIndentityView.uploadOtherCrenditral(result);
    }

    @Override
    public void getCrentialSuccess(String s) {
        uploadIndentityView.hideLoadDialog();
        uploadIndentityView.credentialDetialSuccess(s);
    }

    @Override
    public void getCrentialError(Throwable error) {
        uploadIndentityView.hideLoadDialog();
        uploadIndentityView.credentialDetialError(error);

    }

    @Override
    public void getLivingCountSuccess(String s) {
        uploadIndentityView.hideLoadDialog();
        uploadIndentityView.getLivingCountSuccess(s);
    }

    @Override
    public void getLivingCountError(Throwable error) {
        uploadIndentityView.hideLoadDialog();
        uploadIndentityView.getLivingCountError(error);
    }
}
