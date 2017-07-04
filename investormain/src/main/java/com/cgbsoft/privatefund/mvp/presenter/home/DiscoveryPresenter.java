package com.cgbsoft.privatefund.mvp.presenter.home;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.privatefund.model.DiscoverModel;
import com.cgbsoft.privatefund.mvp.contract.home.DiscoverContract;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;

/**
 * @author chenlong
 *
 */
public class DiscoveryPresenter extends BasePresenterImpl<DiscoverContract.View> implements DiscoverContract.Presenter {
    public DiscoveryPresenter(@NonNull Context context, @NonNull DiscoverContract.View view) {
        super(context, view);
    }

    @Override
    public void getDiscoveryFirstData() {
        addSubscription(ApiClient.getDiscoverFirstData(new HashMap()).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                Log.d("DiscoveryPresenter", "----"+ s.toString());
                DiscoverModel result = new Gson().fromJson(s, new TypeToken<DiscoverModel>() {}.getType());
                getView().requestFirstDataSuccess(result);
            }

            @Override
            protected void onRxError(Throwable error) {
                getView().requestFirstDataFailure(error.getMessage());
            }
        }));
    }
}
