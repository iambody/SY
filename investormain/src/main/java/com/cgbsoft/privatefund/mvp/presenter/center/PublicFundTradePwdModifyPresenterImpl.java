package com.cgbsoft.privatefund.mvp.presenter.center;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.utils.net.ApiBusParam;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.privatefund.mvp.contract.center.PublicFundTradePwdModifyContract;

/**
 * @author chenlong
 */
public class PublicFundTradePwdModifyPresenterImpl extends BasePresenterImpl<PublicFundTradePwdModifyContract.PublicFundTradePwdModifyView> implements PublicFundTradePwdModifyContract.PublicFundTradePwdModifyoPresenter {

    public PublicFundTradePwdModifyPresenterImpl(@NonNull Context context, @NonNull PublicFundTradePwdModifyContract.PublicFundTradePwdModifyView view) {
        super(context, view);
    }

    @Override
    public void modifyPublicFundTradePwd() {

    }

    @Override
    public void getPhoneValidateCode(String phone) {
        addSubscription(ApiClient.bespeakHealthValidatePhone(ApiBusParam.getBespeakHealthValidateParams(phone)).subscribe(new RxSubscriber<String>(){
            @Override
            protected void onEvent(String s) {
                Log.d("HealthBespeakPresenter", s);
            }

            @Override
            protected void onRxError(Throwable error) {
                Log.d("HealthBespeakPresenter", error.getMessage());
            }
        }));
    }
}
