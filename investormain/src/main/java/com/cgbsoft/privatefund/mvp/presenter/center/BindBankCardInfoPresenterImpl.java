package com.cgbsoft.privatefund.mvp.presenter.center;

import android.content.Context;
import android.support.annotation.NonNull;

import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.privatefund.mvp.contract.center.BindBankCardInfoContract;
import com.cgbsoft.privatefund.mvp.contract.center.PublicFundInfoContract;

/**
 * @author chenlong
 */

public class BindBankCardInfoPresenterImpl extends BasePresenterImpl<BindBankCardInfoContract.BindBankCardInfoView> implements BindBankCardInfoContract.BindBankCardInfoPresenter {


    public BindBankCardInfoPresenterImpl(@NonNull Context context, @NonNull BindBankCardInfoContract.BindBankCardInfoView view) {
        super(context, view);
    }

    @Override
    public void requestBindBankCardInfo() {

    }
}
