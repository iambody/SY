package com.cgbsoft.privatefund.mvp.presenter.center;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.privatefund.model.CredentialModel;
import com.cgbsoft.lib.base.model.bean.CredentialStateMedel;
import com.cgbsoft.privatefund.model.DatumManageModelListener;
import com.cgbsoft.privatefund.model.impl.DatumManageModelImpl;
import com.cgbsoft.privatefund.mvp.contract.center.DatumManageContract;

import java.util.List;

/**
 * Created by fei on 2017/8/15.
 */

public class DatumManagePresenterImpl extends BasePresenterImpl<DatumManageContract.DatumManageView> implements DatumManageContract.DatumManagePresenter, DatumManageModelListener {
    private final DatumManageContract.DatumManageView datumManageView;
    private final DatumManageModelImpl datumManageModel;

    public DatumManagePresenterImpl(@NonNull Context context, @NonNull DatumManageContract.DatumManageView view) {
        super(context, view);
        this.datumManageView = view;
        datumManageModel = new DatumManageModelImpl();
    }

    @Override
    public void verifyIndentity() {
        datumManageView.showLoadDialog();
        datumManageModel.verifyIndentityV3(getCompositeSubscription(), this);
    }

    @Override
    public void getLivingCount() {
        datumManageView.showLoadDialog();
        datumManageModel.getLivingCount(getCompositeSubscription(), this);
    }

    @Override
    public void getCredentialDetial(String credentialCode) {
        datumManageView.showLoadDialog();
        datumManageModel.getCredentialDetial(getCompositeSubscription(), this, credentialCode);
    }

    @Override
    public void uploadOtherCrendtial(List<String> remoteParams, String customCode, String credentialCode, String remotePerson) {
        datumManageView.showLoadDialog();
        datumManageModel.uploadOtherCrenditial(getCompositeSubscription(), this, remoteParams, customCode, credentialCode, remotePerson);
    }


    @Override
    public void verifyIndentityV3() {
        datumManageView.showLoadDialog();
        datumManageModel.verifyIndentityV3(getCompositeSubscription(), this);
    }

    @Override
    public void verifyIndentitySuccess(String result, String hasIdCard, String title, String credentialCode, String status, String statusCode, String customerName, String credentialNumber, String credentialTitle, String existStatus) {
        datumManageView.hideLoadDialog();
        if (TextUtils.isEmpty(result)) {//无身份
            datumManageView.verifyIndentitySuccess(false, false, null, null, null, status, statusCode);
        } else {//有身份
            if ("1001".equals(result) && "0".equals(hasIdCard)) {
                datumManageView.verifyIndentitySuccess(true, false, result, title, credentialCode, status, statusCode);
            } else {
                datumManageView.verifyIndentitySuccess(true, true, result, null, null, status, statusCode);
            }
        }
    }

    @Override
    public void verifyIndentityError(Throwable error) {
        datumManageView.hideLoadDialog();
        datumManageView.verifyIndentityError(error);
    }

    @Override
    public void verifyIndentitySuccessV3(CredentialStateMedel credentialStateMedel) {
        datumManageView.hideLoadDialog();
        datumManageView.verifyIndentitySuccessV3(credentialStateMedel);
    }

    @Override
    public void getLivingCountSuccess(String s) {
        datumManageView.hideLoadDialog();
        datumManageView.getLivingCountSuccess(s);
    }

    @Override
    public void getLivingCountError(Throwable error) {
        datumManageView.hideLoadDialog();
        datumManageView.getLivingCountError(error);
    }

    @Override
    public void getCredentialDetialSuccess(CredentialModel credentialModel) {
        datumManageView.showLoadDialog();
        datumManageView.getCredentialDetialSuccess(credentialModel);
    }

    @Override
    public void getCredentialDetialError(Throwable error) {
        datumManageView.hideLoadDialog();
        datumManageView.getCredentialDetialError(error);
    }

    @Override
    public void uploadOtherCrendtialSuccess(String s) {
        datumManageView.hideLoadDialog();
        datumManageView.uploadOtherCrendtialSuccess(s);
    }

    @Override
    public void uploadOtherCrendtialError(Throwable error) {
        datumManageView.hideLoadDialog();
        datumManageView.uploadOtherCrendtialError(error);
    }
}
