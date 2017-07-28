package com.cgbsoft.privatefund.mvp.presenter.home;

import android.content.Context;
import android.support.annotation.NonNull;

import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.utils.net.ApiBusParam;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.privatefund.mvp.contract.home.InvisiteAccountContract;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author chenlong
 *
 */
public class InvisiteAccountPresenter extends BasePresenterImpl<InvisiteAccountContract.View> implements InvisiteAccountContract.Presenter {

    public InvisiteAccountPresenter(@NonNull Context context, @NonNull InvisiteAccountContract.View view) {
        super(context, view);
    }

    @Override
    public void commitInvisiteAccount(String userId, String customerName, String customerIdType, String customerIdNumber) {
        addSubscription(ApiClient.commitInvisitAccount(ApiBusParam.commitInvisiteAccount(userId, customerName, customerIdType, customerIdNumber)).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                try {
                    System.out.println("------InvisiteAccountPresenter=" + s);
                    JSONObject jsonObject = new JSONObject(s);
                    String result = jsonObject.getString("result");
                    getView().commitSuccess();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void onRxError(Throwable error) {
                getView().commitFailure();
            }
        }));
    }
}

