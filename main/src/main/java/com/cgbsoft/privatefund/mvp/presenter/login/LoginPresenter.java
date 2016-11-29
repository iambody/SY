package com.cgbsoft.privatefund.mvp.presenter.login;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.cgbsoft.lib.base.model.UserInfoDataEntity;
import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.utils.tools.MD5Utils;
import com.cgbsoft.lib.widget.LoadingDialog;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.mvp.contract.login.LoginContract;
import com.google.gson.Gson;

/**
 * Created by xiaoyu.zhang on 2016/11/17 11:45
 * Email:zhangxyfs@126.com
 *  
 */
public class LoginPresenter extends BasePresenterImpl<LoginContract.View> implements LoginContract.Presenter {
    private String isExist;

    public LoginPresenter(Context context, LoginContract.View view) {
        super(context, view);
    }

    /**
     * 登录接口
     *
     * @param un   用户名
     * @param pwd  密码
     * @param isWx 是否微信登录
     */
    @Override
    public void toNormalLogin(@NonNull LoadingDialog loadingDialog, String un, String pwd, boolean isWx) {
        loadingDialog.setLoading(getContext().getString(R.string.la_login_loading_str));
        loadingDialog.show();
        pwd = isWx ? pwd : MD5Utils.getShortMD5(pwd);
        addSubscription(ApiClient.toLogin(un, pwd).subscribe(new RxSubscriber<UserInfoDataEntity.Result>() {
            @Override
            protected void onEvent(UserInfoDataEntity.Result loginBean) {
                SPreference.saveUserId(getContext().getApplicationContext(), loginBean.userId);
                SPreference.saveToken(getContext().getApplicationContext(), loginBean.token);

                SPreference.saveLoginFlag(getContext(), true);
                if (loginBean.userInfo != null)
                    SPreference.saveUserInfoData(getContext(), new Gson().toJson(loginBean.userInfo));
                loadingDialog.setResult(true, getContext().getString(R.string.la_login_succ_str), 1000, () -> getView().loginSuccess());
            }

            @Override
            protected void onRxError(Throwable error) {
                loadingDialog.setResult(false, getContext().getString(R.string.la_getinfo_error_str), 1000, () -> getView().loginFail());
            }
        }));
    }

    /**
     * 微信登陆
     *
     * @param loadingDialog
     * @param unionid
     * @param sex
     * @param nickName
     * @param headimgurl
     */
    @Override
    public void toWxLogin(@NonNull LoadingDialog loadingDialog, String unionid, String sex, String nickName, String headimgurl) {
        addSubscription(ApiClient.wxUnioIDCheck(unionid).flatMap(result -> {
            isExist = result.isExist;
            return ApiClient.toWxLogin(sex, nickName, unionid, headimgurl);
        }).subscribe(new RxSubscriber<UserInfoDataEntity.Result>() {
            @Override
            protected void onEvent(UserInfoDataEntity.Result result) {
                SPreference.saveToken(getContext().getApplicationContext(), result.token);
                SPreference.saveUserId(getContext().getApplicationContext(), result.userId);
                SPreference.saveLoginFlag(getContext(), true);
                if (result.userInfo != null)
                    SPreference.saveUserInfoData(getContext().getApplicationContext(), new Gson().toJson(result.userInfo));

                if (TextUtils.equals(isExist, "0")) {
                    loadingDialog.dismiss();
                    loadingDialog.setResult(true, getContext().getString(R.string.al_need_bind_phone_str), 1000, () -> getView().toBindActivity());
                } else {
                    loadingDialog.setResult(true, getContext().getString(R.string.la_login_succ_str), 1000, () -> getView().loginSuccess());
                }
            }

            @Override
            protected void onRxError(Throwable error) {
                loadingDialog.setResult(false, getContext().getString(R.string.la_getinfo_error_str), 1000, () -> getView().loginFail());
            }
        }));
    }
}
