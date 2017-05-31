package com.cgbsoft.privatefund.mvp.presenter.home;

import android.content.Context;
import android.support.annotation.NonNull;

import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.base.model.CommonEntity;
import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.privatefund.mvp.contract.home.RelativeAssetContract;

/**
 * @author chenlong
 */

public class RelatedAssetPresenter extends BasePresenterImpl<RelativeAssetContract.View> implements RelativeAssetContract.Presenter {

    public RelatedAssetPresenter(@NonNull Context context, @NonNull RelativeAssetContract.View view) {
        super(context, view);
    }

    @Override
    public void uploadAssetRelatedFile(String imageUrl) {
        ApiClient.relatedAsset(AppManager.getUserId(getContext()), imageUrl).subscribe(new RxSubscriber<CommonEntity.Result>() {
            @Override
            protected void onEvent(CommonEntity.Result result) {
                if ("suc".equals(result.result)) {
                    getView().requestSuccess();
                } else {
                   getView().requestFailure();
                }
            }

            @Override
            protected void onRxError(Throwable error) {
                getView().requestFailure();
            }
        });
//        ApiClient.toTestRelatedAsset(AppManager.getUserId(getContext()), imageUrl).subscribe(new RxSubscriber<String>() {
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
//                } catch (JSONException e) {
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
