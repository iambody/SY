package app.ndk.com.enter.mvp.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.cgbsoft.lib.base.model.UserInfoDataEntity;
import com.cgbsoft.lib.base.model.WXUnionIDCheckEntity;
import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.utils.tools.LogUtils;
import com.cgbsoft.lib.utils.tools.MD5Utils;
import com.cgbsoft.lib.widget.CustomDialog;
import com.cgbsoft.lib.widget.LoadingDialog;
import com.google.gson.Gson;

import app.ndk.com.enter.R;
import app.ndk.com.enter.mvp.contract.LoginContract;
import rx.Observable;

/**
 * Created by xiaoyu.zhang on 2016/11/17 11:45
 * Email:zhangxyfs@126.com
 *  
 */
public class LoginPresenter extends BasePresenterImpl<LoginContract.View> implements LoginContract.Presenter {
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

        //todo 测试时候调用该接口，
        addSubscription(ApiClient.toTestLogin(un, pwd).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {

                UserInfoDataEntity.Result loginBean = new Gson().fromJson(s, UserInfoDataEntity.Result.class);
                SPreference.saveUserId(getContext().getApplicationContext(), loginBean.userId);
                SPreference.saveToken(getContext().getApplicationContext(), loginBean.token);
                LogUtils.Log("loginresult",s);
                SPreference.saveLoginFlag(getContext(), true);
                if (loginBean.userInfo != null) {
                    SPreference.saveUserInfoData(getContext(), new Gson().toJson(loginBean.userInfo));
                    SPreference.saveLoginName(getContext(), un);
                }
                loadingDialog.dismiss();
                loadingDialog.setResult(true, getContext().getString(R.string.la_login_succ_str), 1000, () -> getView().loginSuccess());
            }

            @Override
            protected void onRxError(Throwable error) {
                LogUtils.Log("loginresult",error.toString());
                loadingDialog.dismiss();
//                loadingDialog.setResult(false, getContext().getString(R.string.la_getinfo_error_str), 1000, () -> getView().loginFail());
            }
        }));

        /*addSubscription(ApiClient.toLogin(un, pwd).subscribe(new RxSubscriber<UserInfoDataEntity.Result>() {
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
        }));*/
    }

    /**
     * 微信登录
     *
     * @param loadingDialog
     * @param unionid
     * @param sex
     * @param nickName
     * @param headimgurl
     */
    @Override
    public void toWxLogin(@NonNull LoadingDialog loadingDialog, CustomDialog.Builder builder, String unionid, String sex, String nickName, String headimgurl) {
        addSubscription(ApiClient.wxTestUnioIDCheck(unionid).flatMap(s -> {
            WXUnionIDCheckEntity.Result result = new Gson().fromJson(s, WXUnionIDCheckEntity.Result.class);
            if (TextUtils.equals(result.isExist, "0")) {
                UserInfoDataEntity.Result r = new UserInfoDataEntity.Result();
                r.token = "-1";
                return Observable.just(new Gson().toJson(r));
            } else {
                return ApiClient.toTestWxLogin(sex, nickName, unionid, headimgurl);
            }
        }).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                UserInfoDataEntity.Result result = new Gson().fromJson(s, UserInfoDataEntity.Result.class);
                if (TextUtils.equals(result.token, "-1")) {
                    loadingDialog.dismiss();
                    builder.setMessage(getContext().getString(R.string.la_cd_content_str, nickName));
                    builder.create().show();
                } else {
                    SPreference.saveToken(getContext().getApplicationContext(), result.token);
                    SPreference.saveUserId(getContext().getApplicationContext(), result.userId);
                    SPreference.saveLoginFlag(getContext(), true);
                    if (result.userInfo != null)
                        SPreference.saveUserInfoData(getContext().getApplicationContext(), new Gson().toJson(result.userInfo));
                    if (TextUtils.equals(result.isBind, "2")) {//1:已绑定，2：未绑定，3：绑定中
                        loadingDialog.dismiss();
                        loadingDialog.setResult(true, getContext().getString(R.string.al_need_bind_phone_str), 1000, () -> getView().toBindActivity());
                    } else
                        loadingDialog.setResult(true, getContext().getString(R.string.la_login_succ_str), 1000, () -> getView().loginSuccess());
                }
            }

            @Override
            protected void onRxError(Throwable error) {
                loadingDialog.setResult(false, getContext().getString(R.string.la_getinfo_error_str), 1000, () -> getView().loginFail());
            }
        }));
    }

    /**
     * 微信登录
     *
     * @param loadingDialog
     * @param unionid
     * @param sex
     * @param nickName
     * @param headimgurl
     */
    @Override
    public void toDialogWxLogin(@NonNull LoadingDialog loadingDialog, String unionid, String sex, String nickName, String headimgurl) {
        loadingDialog.setLoading(getContext().getString(R.string.la_login_loading_str));
        loadingDialog.show();
        addSubscription(ApiClient.toTestWxLogin(sex, nickName, unionid, headimgurl).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                UserInfoDataEntity.Result result = new Gson().fromJson(s, UserInfoDataEntity.Result.class);
                SPreference.saveToken(getContext().getApplicationContext(), result.token);
                SPreference.saveUserId(getContext().getApplicationContext(), result.userId);
                SPreference.saveLoginFlag(getContext(), true);
                if (result.userInfo != null)
                    SPreference.saveUserInfoData(getContext().getApplicationContext(), new Gson().toJson(result.userInfo));
                if (TextUtils.equals(result.isBind, "2")) {//1:已绑定，2：未绑定，3：绑定中
                    loadingDialog.dismiss();
                    loadingDialog.setResult(true, getContext().getString(R.string.al_need_bind_phone_str), 1000, () -> getView().toBindActivity());
                } else
                    loadingDialog.setResult(true, getContext().getString(R.string.la_login_succ_str), 1000, () -> getView().loginSuccess());
            }

            @Override
            protected void onRxError(Throwable error) {
                loadingDialog.setResult(false, getContext().getString(R.string.la_getinfo_error_str), 1000, () -> getView().loginFail());
            }
        }));
    }
}
