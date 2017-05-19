package com.cgbsoft.privatefund.mvp.presenter.home;

import android.content.Context;
import android.support.annotation.NonNull;

import com.cgbsoft.lib.base.model.CommonEntity;
import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.privatefund.mvp.contract.home.ModifyUserInfoContract;
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
                if ("suc".equals(result.results)) {
                    getView().modifyUserSuccess(isFiveTimes);
                } else {
                    getView().modifyUserFailure();
                }
            }

            @Override
            protected void onRxError(Throwable error) {}
        });
    }

    @Override
    public void validateUserPassword(HashMap<String, String> hashMap) {
        ApiClient.validateUserPassword(hashMap).subscribe(new RxSubscriber<CommonEntity.Result>() {
            @Override
            protected void onEvent(CommonEntity.Result result) {
                if ("1".equals(result.results)) {
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

    }
}
