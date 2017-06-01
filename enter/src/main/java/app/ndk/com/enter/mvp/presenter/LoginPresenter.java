package app.ndk.com.enter.mvp.presenter;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.cgbsoft.lib.AppInfStore;
import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.base.model.UserInfoDataEntity;
import com.cgbsoft.lib.base.model.WXUnionIDCheckEntity;
import com.cgbsoft.lib.base.mvp.model.BaseResult;
import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.encrypt.RSAUtils;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.utils.tools.BStrUtils;
import com.cgbsoft.lib.utils.tools.LogUtils;
import com.cgbsoft.lib.utils.tools.MD5Utils;
import com.cgbsoft.lib.utils.tools.PromptManager;
import com.cgbsoft.lib.widget.CustomDialog;
import com.cgbsoft.lib.widget.dialog.LoadingDialog;
import com.cgbsoft.privatefund.bean.StrResult;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;

import app.ndk.com.enter.R;
import app.ndk.com.enter.mvp.contract.LoginContract;
import app.privatefund.com.im.utils.RongConnect;
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
    public void toNormalLogin(@NonNull LoadingDialog loadingDialog, String un, String pwd, String publicKey, boolean isWx) {
        loadingDialog.setLoading(getContext().getString(R.string.la_login_loading_str));
        loadingDialog.show();
        pwd = isWx ? pwd : MD5Utils.getShortMD5(pwd);
//        /**
//         * 新版登录需要加密
//         */
        JSONObject object = new JSONObject();
        String RsaStr = "";
        try {
            object.put("userName", un);
            object.put("password", pwd);
            object.put("client", AppManager.isInvestor(getContext()) ? "C" : "B");
            RsaStr = RSAUtils.GetRsA(getContext(), object.toString(), publicKey);

        } catch (Exception e) {
            e.printStackTrace();
            PromptManager.ShowCustomToast(getContext(), getContext().getResources().getString(R.string.rsa_encryput_error));
            loadingDialog.dismiss();
            return;
        }
        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("sign", RsaStr);
        //todo 测试时候调用该接口，
//        addSubscription(ApiClient.toTestLogin(un, pwd).subscribe(new RxSubscriber<String>() {
        //测试V2登录接口
        addSubscription(ApiClient.toV2Login(paramMap).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                UserInfoDataEntity.Result loginBean = new Gson().fromJson(getV2String(s), UserInfoDataEntity.Result.class);
                AppInfStore.saveUserToken(getContext().getApplicationContext(), BStrUtils.decodeSimpleEncrypt(loginBean.token));
                AppInfStore.saveUserId(getContext(), loginBean.userId);
                AppInfStore.saveIsLogin(getContext().getApplicationContext(), true);
                AppInfStore.saveUserAccount(getContext().getApplicationContext(), un);
                Log.i("LoginPresenter", "-------userid=" + loginBean.userId + "------rongYunToken=" + loginBean.token);
                if (loginBean.userInfo != null) {
                    SPreference.saveUserInfoData(getContext(), new Gson().toJson(loginBean.userInfo));
                }
                RongConnect.initRongTokenConnect(loginBean.userId);
                loadingDialog.setResult(true, getContext().getString(R.string.la_login_succ_str), 1000, () -> getView().loginSuccess());
            }

            @Override
            protected void onRxError(Throwable error) {
                LogUtils.Log("LoginPresenter", "----error=" + error.toString());
                loadingDialog.dismiss();
//              loadingDialog.setResult(false, getContext().getString(R.string.la_getinfo_error_str), 1000, () -> getView().loginFail());
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
    public void toWxLogin(@NonNull LoadingDialog loadingDialog, CustomDialog.Builder builder, String unionid, String sex, String nickName, String headimgurl, String openId, String publicKey) {
        addSubscription(ApiClient.wxTestUnioIDCheck(unionid).flatMap(s -> {
            WXUnionIDCheckEntity.Result result = new Gson().fromJson(getV2String(s), WXUnionIDCheckEntity.Result.class);
            if (TextUtils.equals(result.isExist, "0")) {
                UserInfoDataEntity.Result r = new UserInfoDataEntity.Result();
                r.token = "-1";
//                return Observable.just(new Gson().toJson(r));
                return Observable.just(getJsonStr());
            } else {
                return ApiClient.toTestWxLogin(getContext(), sex, nickName, unionid, headimgurl, openId, publicKey);
            }
        }).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                UserInfoDataEntity.Result result = new Gson().fromJson(getV2String(s), UserInfoDataEntity.Result.class);
                if (null == result.token || TextUtils.equals(result.token, "-1")) {
                    loadingDialog.dismiss();
                    builder.setMessage(getContext().getString(R.string.la_cd_content_str, nickName));
                    builder.create().show();
                } else {
                    AppInfStore.saveUserToken(getContext().getApplicationContext(), BStrUtils.decodeSimpleEncrypt(result.token));
                    AppInfStore.saveIsLogin(getContext().getApplicationContext(), true);
                    AppInfStore.saveUserId(getContext().getApplicationContext(), result.userId);
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
    public void toDialogWxLogin(@NonNull LoadingDialog loadingDialog, String unionid, String sex, String nickName, String headimgurl, String openId, String publicKey) {
        loadingDialog.setLoading(getContext().getString(R.string.la_login_loading_str));
        loadingDialog.show();
        addSubscription(ApiClient.toTestWxLogin(getContext(), sex, nickName, unionid, headimgurl, openId, publicKey).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                UserInfoDataEntity.Result result = new Gson().fromJson(getV2String(s), UserInfoDataEntity.Result.class);
                AppInfStore.saveUserToken(getContext().getApplicationContext(), BStrUtils.decodeSimpleEncrypt(result.token));
                AppInfStore.saveIsLogin(getContext().getApplicationContext(), true);
                AppInfStore.saveUserId(getContext().getApplicationContext(), result.userId);
                if (result.userInfo != null) {
                    SPreference.saveUserInfoData(getContext().getApplicationContext(), new Gson().toJson(result.userInfo));
                    SPreference.saveUserInfoData(getContext().getApplicationContext(), new Gson().toJson(result.userInfo));
                }
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

    /**
     * 获取登录前的公钥
     */
    @Override
    public void toGetPublicKey() {
        addSubscription(ApiClient.getLoginPublic().subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                StrResult result = new Gson().fromJson(s, StrResult.class);
                getView().publicKeySuccess(result.result);
            }

            @Override
            protected void onRxError(Throwable error) {
                LogUtils.Log("user", error.toString());
            }
        }));
    }

    public void setAnimation(View VV) {
        AnimatorSet animationSet = new AnimatorSet();
        ObjectAnimator Translate = ObjectAnimator.ofFloat(VV, "translationY", 1000f, 0f);
        ObjectAnimator Alpha = ObjectAnimator.ofFloat(VV, "alpha", 0f, 0.4f, 06f, 0.7f, 0.9f, 1f);
        Alpha.setDuration(300);
        animationSet.playSequentially(Translate);
        animationSet.setDuration(300);
        animationSet.start();
    }

    public String getJsonStr() {
        BaseResult<UserInfoDataEntity.Result> result = new BaseResult<>();
        result.result = new UserInfoDataEntity.Result();
        result.result.token = "-1";
        return new Gson().toJson(result);
    }
}
