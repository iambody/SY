package com.cgbsoft.privatefund.mvp.presenter.home;

import android.content.Context;
import android.support.annotation.NonNull;

import com.cgbsoft.lib.base.model.CommonEntity;
import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.privatefund.mvp.contract.home.AssetProveContract;

import org.json.JSONArray;

/**
 * @author chenlong
 *
 */
public class AssetProvePresenter extends BasePresenterImpl<AssetProveContract.View> implements AssetProveContract.Presenter {

    public AssetProvePresenter(@NonNull Context context, @NonNull AssetProveContract.View view) {
        super(context, view);
    }

    @Override
    public void uploadAssetProveData(JSONArray assertImage, String investmentType) {
        ApiClient.assertProve(SPreference.getUserId(getContext()), assertImage, investmentType).subscribe(new RxSubscriber<CommonEntity.Result>() {
            @Override
            protected void onEvent(CommonEntity.Result result) {
                if ("suc".equals(result.results)) {
                    getView().requestSuccess();
                } else {
                    getView().requestFailure();
                }
            }

            @Override
            protected void onRxError(Throwable error) {}
        });
    }
}
