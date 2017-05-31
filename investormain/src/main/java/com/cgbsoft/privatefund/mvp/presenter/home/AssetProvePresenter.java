package com.cgbsoft.privatefund.mvp.presenter.home;

import android.content.Context;
import android.support.annotation.NonNull;

import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.base.model.CommonEntity;
import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.privatefund.mvp.contract.home.AssetProveContract;

import org.json.JSONArray;
import org.json.JSONObject;

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
        ApiClient.assertProve(AppManager.getUserId(getContext()), assertImage, investmentType).subscribe(new RxSubscriber<CommonEntity.Result>() {
            @Override
            protected void onEvent(CommonEntity.Result result) {
                if ("suc".equals(result.result)) {
                    getView().requestSuccess();
                } else {
                    getView().requestFailure();
                }
            }

            @Override
            protected void onRxError(Throwable error) {}
        });
//        ApiClient.toTestassertProve(AppManager.getUserId(getContext()), assertImage, investmentType).subscribe(new RxSubscriber<String>() {
//            @Override
//            protected void onEvent(String result) {
//                JSONObject jsonObject = null;
//                try {
//                    jsonObject = new JSONObject(result);
//                    String results = jsonObject.get("result").toString();
//                    if ("suc".equals(results)) {
//                        getView().requestSuccess();
//                    } else {
//                        getView().requestFailure();
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            protected void onRxError(Throwable error) {
//                getView().requestError(error.getMessage());
//            }
//        });
    }
}
