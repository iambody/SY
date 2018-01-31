package com.cgbsoft.privatefund.mvp.presenter.center;

import android.content.Context;
import android.support.annotation.NonNull;

import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.privatefund.mvp.contract.center.PublicFundInfoContract;

/**
 * @author chenlong
 */
public class PublicFundInfoPresenterImpl extends BasePresenterImpl<PublicFundInfoContract.PublicFundInfoView> implements PublicFundInfoContract.PublicFundInfoPresenter {


    public PublicFundInfoPresenterImpl(@NonNull Context context, @NonNull PublicFundInfoContract.PublicFundInfoView view) {
        super(context, view);
    }

    @Override
    public void requestPublicFundInfo() {
        getView().showLoadDialog();
    }
}
