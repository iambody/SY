package com.cgbsoft.privatefund.mvp.presenter.login;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.cgbsoft.lib.base.model.LoginEntity;
import com.cgbsoft.lib.base.mvp.presenter.BasePresenter;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.utils.tools.MD5Utils;
import com.cgbsoft.lib.widget.CustomDialog;
import com.cgbsoft.lib.widget.LoadingDialog;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.mvp.view.login.LoginView;
import com.google.gson.Gson;

import rx.Observable;

/**
 * Created by xiaoyu.zhang on 2016/11/17 11:45
 * Email:zhangxyfs@126.com
 *  
 */
public class LoginPresenter extends BasePresenter<LoginView> {
    private Context context;

    public LoginPresenter(Context context, LoginView view) {
        super(view);
        this.context = context;
    }

    /**
     * 登录接口
     *
     * @param un   用户名
     * @param pwd  密码
     * @param isWx 是否微信登录
     */
    public void toNormalLogin(@NonNull LoadingDialog loadingDialog, String un, String pwd, boolean isWx) {
        loadingDialog.setLoading(context.getString(R.string.la_login_loading_str));
        loadingDialog.show();
        pwd = isWx ? pwd : MD5Utils.getShortMD5(pwd);
        addSubscription(ApiClient.toLogin(un, pwd).subscribe(new RxSubscriber<LoginEntity.Result>() {
            @Override
            protected void onEvent(LoginEntity.Result loginBean) {
                SPreference.saveUserId(context.getApplicationContext(), loginBean.userId);
                SPreference.saveToken(context.getApplicationContext(), loginBean.token);

                SPreference.saveLoginFlag(context, true);
                if (loginBean.userInfo != null)
                    SPreference.saveUserInfoData(context, new Gson().toJson(loginBean.userInfo));
                loadingDialog.setResult(true, context.getString(R.string.la_login_succ_str), 1000, () -> getView().loginSuccess());
            }

            @Override
            protected void onRxError(Throwable error) {
                loadingDialog.setResult(false, context.getString(R.string.la_getinfo_error_str), 1000, () -> getView().loginFail());
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
    public void toWxLogin(@NonNull LoadingDialog loadingDialog, @NonNull CustomDialog.Builder builder, String unionid, String sex, String nickName, String headimgurl) {
        addSubscription(ApiClient.wxUnioIDCheck(unionid).flatMap(result -> {
            if (TextUtils.equals(result.isExist, "0")) {
                LoginEntity.Result r = new LoginEntity.Result();
                r.token = "-1";
                return Observable.just(r);
            } else {
                return ApiClient.toWxLogin(sex, nickName, unionid, headimgurl);
            }
        }).subscribe(new RxSubscriber<LoginEntity.Result>() {
            @Override
            protected void onEvent(LoginEntity.Result result) {
                if (TextUtils.equals(result.token, "-1")) {
                    loadingDialog.dismiss();
                    builder.setMessage(context.getString(R.string.la_cd_content_str, nickName));
                    builder.create().show();
                } else {
                    SPreference.saveToken(context.getApplicationContext(), result.token);
                    SPreference.saveUserId(context.getApplicationContext(), result.userId);
                    SPreference.saveLoginFlag(context, true);
                    if (result.userInfo != null)
                        SPreference.saveUserInfoData(context.getApplicationContext(), new Gson().toJson(result.userInfo));
                    loadingDialog.setResult(true, context.getString(R.string.la_login_succ_str), 1000, () -> getView().loginSuccess());
                }
            }

            @Override
            protected void onRxError(Throwable error) {
                loadingDialog.setResult(false, context.getString(R.string.la_getinfo_error_str), 1000, () -> getView().loginFail());
            }
        }));
    }

    /**
     * 微信登陆
     * @param loadingDialog
     * @param unionid
     * @param sex
     * @param nickName
     * @param headimgurl
     */
    public void toDialogWxLogin(@NonNull LoadingDialog loadingDialog, String unionid, String sex, String nickName, String headimgurl) {
        loadingDialog.setLoading(context.getString(R.string.la_login_loading_str));
        loadingDialog.show();
        addSubscription(ApiClient.toWxLogin(sex, nickName, unionid, headimgurl).subscribe(new RxSubscriber<LoginEntity.Result>() {
            @Override
            protected void onEvent(LoginEntity.Result result) {
                SPreference.saveToken(context.getApplicationContext(), result.token);
                SPreference.saveUserId(context.getApplicationContext(), result.userId);
                SPreference.saveLoginFlag(context, true);
                if (result.userInfo != null)
                    SPreference.saveUserInfoData(context.getApplicationContext(), new Gson().toJson(result.userInfo));
                loadingDialog.setResult(true, context.getString(R.string.la_login_succ_str), 1000, () -> getView().loginSuccess());
            }

            @Override
            protected void onRxError(Throwable error) {
                loadingDialog.setResult(false, context.getString(R.string.la_getinfo_error_str), 1000, () -> getView().loginFail());
            }
        }));
    }

    @Override
    public void detachView() {
        super.detachView();
        context = null;
    }
}
