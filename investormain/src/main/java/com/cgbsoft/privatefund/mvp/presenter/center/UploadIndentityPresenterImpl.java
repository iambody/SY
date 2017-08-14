package com.cgbsoft.privatefund.mvp.presenter.center;

import android.content.Context;
import android.support.annotation.NonNull;

import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.privatefund.model.UploadIndentityModelListener;
import com.cgbsoft.privatefund.model.impl.UploadIndentityModelImpl;
import com.cgbsoft.privatefund.mvp.contract.center.UploadIndentityContract;

/**
 * Created by fei on 2017/8/12.
 */

public class UploadIndentityPresenterImpl extends BasePresenterImpl<UploadIndentityContract.UploadIndentityView> implements UploadIndentityContract.UploadIndentityPresenter ,UploadIndentityModelListener{
    private final UploadIndentityContract.UploadIndentityView uploadIndentityView;
    private final UploadIndentityModelImpl uploadModel;

    public UploadIndentityPresenterImpl(@NonNull Context context, @NonNull UploadIndentityContract.UploadIndentityView view) {
        super(context, view);
        this.uploadIndentityView=view;
        uploadModel=new UploadIndentityModelImpl();
    }

    @Override
    public void uploadIndentity() {

    }

    @Override
    public void uploadIndentitySuccess() {

    }

    @Override
    public void uploadIndentityError(Throwable error) {

    }
}
