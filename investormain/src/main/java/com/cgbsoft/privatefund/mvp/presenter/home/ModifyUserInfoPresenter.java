package com.cgbsoft.privatefund.mvp.presenter.home;

import android.content.Context;
import android.support.annotation.NonNull;

import com.cgbsoft.lib.base.model.CommonEntity;
import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.privatefund.mvp.contract.home.ModifyUserInfoContract;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * @author chenlong
 */

public class ModifyUserInfoPresenter extends BasePresenterImpl<ModifyUserInfoContract.View> implements ModifyUserInfoContract.Presenter {

    public ModifyUserInfoPresenter(@NonNull Context context, @NonNull ModifyUserInfoContract.View view) {
        super(context, view);
    }

    @Override
    public void modifyUserInfo(HashMap<String, String> hashMap, boolean isFiveTimes) {
        ApiClient.updateUserInfo(hashMap).subscribe(new RxSubscriber<CommonEntity.Result>() {
            @Override
            protected void onEvent(CommonEntity.Result result) {
                String results = result.results;
                if ("suc".equals(results)) {
                    getView().modifyUserSuccess(isFiveTimes);
                } else {
                    getView().modifyUserFailure();
                }
            }

            @Override
            protected void onRxError(Throwable error) {
                getView().modifyUserFailure();
            }
        });
//        ApiClient.toTestUpdateUserInfo(hashMap).subscribe(new RxSubscriber<String>() {
//            @Override
//            protected void onEvent(String result) {
//                JSONObject jsonObject = null;
//                try {
//                    jsonObject = new JSONObject(result);
//                    String results = jsonObject.get("result").toString();
//                    if ("suc".equals(results)) {
//                        getView().modifyUserSuccess(isFiveTimes);
//                    } else {
//                        getView().modifyUserFailure();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            protected void onRxError(Throwable error) {
//                getView().modifyUserFailure();
//            }
//        });
    }

    @Override
    public void validateUserPassword(HashMap<String, String> hashMap) {
        ApiClient.validateUserPassword(hashMap).subscribe(new RxSubscriber<CommonEntity.Result>() {
            @Override
            protected void onEvent(CommonEntity.Result result) {
                String results = result.results;
                if ("1".equals(results)) {
                    getView().validatePasswordSuccess();
                } else {
                    getView().validatePasswordFailure();
                }
            }

            @Override
            protected void onRxError(Throwable error) {
                getView().validatePasswordError(error.getMessage());
            }
        });
//        ApiClient.toTestValidateUserPassword(hashMap).subscribe(new RxSubscriber<String>() {
//            @Override
//            protected void onEvent(String result) {
//                JSONObject jsonObject = null;
//                try {
//                    jsonObject = new JSONObject(result);
//                    String results = jsonObject.get("result").toString();
//                    if ("1".equals(results)) {
//                        getView().validatePasswordSuccess();
//                    } else {
//                        getView().validatePasswordFailure();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            protected void onRxError(Throwable error) {
//                getView().validatePasswordError(error.getMessage());
//            }
//        });

    }
}
