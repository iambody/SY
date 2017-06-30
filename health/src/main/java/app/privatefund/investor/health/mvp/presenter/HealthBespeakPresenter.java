package app.privatefund.investor.health.mvp.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.utils.net.ApiBusParam;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;

import app.privatefund.investor.health.mvp.contract.HealthBespeakContract;

/**
 * @author chenlong
 *
 */
public class HealthBespeakPresenter extends BasePresenterImpl<HealthBespeakContract.View> implements HealthBespeakContract.Presenter {

    public HealthBespeakPresenter(@NonNull Context context, @NonNull HealthBespeakContract.View view) {
        super(context, view);
    }

    @Override
    public void commitHealthBespeak(String id, String name, String phone, String capta) {
        addSubscription(ApiClient.bespeakHealth(ApiBusParam.getBespeakHealthParams(id, name, phone, capta)).subscribe(new RxSubscriber<String>(){

            @Override
            protected void onEvent(String s) {

            }

            @Override
            protected void onRxError(Throwable error) {

            }
        }));
    }

    @Override
    public void getValidateCode(String phoneNumber) {
        addSubscription(ApiClient.bespeakHealthValidatePhone(ApiBusParam.getBespeakHealthValidateParams(phoneNumber)).subscribe(new RxSubscriber<String>(){

            @Override
            protected void onEvent(String s) {
            }

            @Override
            protected void onRxError(Throwable error) {

            }
        }));
    }
}
