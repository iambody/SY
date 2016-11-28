package com.cgbsoft.privatefund.mvp.presenter.login;

import android.content.Context;
import android.support.annotation.NonNull;

import com.cgbsoft.lib.base.model.UserInfoDataEntity;
import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.utils.tools.MD5Utils;
import com.cgbsoft.lib.widget.LoadingDialog;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.mvp.contract.login.RegisterContract;
import com.google.gson.Gson;

/**
 * Created by xiaoyu.zhang on 2016/11/17 18:25
 * Email:zhangxyfs@126.com
 *  
 */
public class RegisterPresenter extends BasePresenterImpl<RegisterContract.View> implements RegisterContract.Presenter {

    public RegisterPresenter(Context context, RegisterContract.View view) {
        super(context, view);
    }

    @Override
    public void toRegister(@NonNull LoadingDialog loadingDialog, String un, String pwd, String code) {
        loadingDialog.setLoading(getContext().getString(R.string.ra_register_loading_str));
        loadingDialog.show();
        addSubscription(ApiClient.toRegister(un, MD5Utils.getShortMD5(pwd), code).subscribe(new RxSubscriber<UserInfoDataEntity.Result>() {
            @Override
            protected void onEvent(UserInfoDataEntity.Result result) {
                SPreference.saveUserId(getContext().getApplicationContext(), result.userId);
                SPreference.saveToken(getContext().getApplicationContext(), result.token);

                SPreference.saveLoginFlag(getContext(), true);
                if (result.userInfo != null)
                    SPreference.saveUserInfoData(getContext(), new Gson().toJson(result.userInfo));
                loadingDialog.setResult(true, getContext().getString(R.string.ra_register_success_str), 1000, () -> getView().regSucc());
            }

            @Override
            protected void onRxError(Throwable error) {
                loadingDialog.setResult(false, getContext().getString(R.string.ra_register_fail_str), 1000, () -> getView().regFail());
            }
        }));
    }

    @Override
    public void sendCode(@NonNull LoadingDialog loadingDialog, String un) {
        loadingDialog.setLoading(getContext().getString(R.string.sending_str));
        loadingDialog.show();
        addSubscription(ApiClient.sendCode(un, 1).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                loadingDialog.setResult(true, getContext().getString(R.string.sending_succ_str), 1000, () -> getView().sendSucc());
            }

            @Override
            protected void onRxError(Throwable error) {
                loadingDialog.setResult(false, getContext().getString(R.string.sending_fail_str), 1000);
            }
        }));
    }

    @Override
    public void detachView() {
        super.detachView();
    }
}
