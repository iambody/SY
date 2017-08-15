package com.cgbsoft.privatefund.mvp.presenter.center;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.privatefund.model.DatumManageModelListener;
import com.cgbsoft.privatefund.model.impl.DatumManageModelImpl;
import com.cgbsoft.privatefund.mvp.contract.center.DatumManageContract;

/**
 * Created by fei on 2017/8/15.
 */

public class DatumManagePresenterImpl extends BasePresenterImpl<DatumManageContract.DatumManageView> implements DatumManageContract.DatumManagePresenter ,DatumManageModelListener{
    private final DatumManageContract.DatumManageView datumManageView;
    private final DatumManageModelImpl datumManageModel;

    public DatumManagePresenterImpl(@NonNull Context context, @NonNull DatumManageContract.DatumManageView view) {
        super(context, view);
        this.datumManageView=view;
        datumManageModel=new DatumManageModelImpl();
    }

    @Override
    public void verifyIndentity() {
        datumManageView.showLoadDialog();
        datumManageModel.verifyIndentity(getCompositeSubscription(),this);
    }

    @Override
    public void verifyIndentitySuccess(String result, String hasIdCard, String title, String credentialCode) {
        datumManageView.hideLoadDialog();
        if (TextUtils.isEmpty(result)) {//无身份
            datumManageView.verifyIndentitySuccess(false,false,null,null,null);
        } else {//有身份
            if ("1001".equals(result) && "0".equals(hasIdCard)) {
                datumManageView.verifyIndentitySuccess(true, false,result,title,credentialCode);
            } else {
                datumManageView.verifyIndentitySuccess(true,true,result,null,null);
            }
        }
    }

    @Override
    public void verifyIndentityError(Throwable error) {
        datumManageView.hideLoadDialog();
        datumManageView.verifyIndentityError(error);
    }
}