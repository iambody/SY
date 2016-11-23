package com.cgbsoft.privatefund.mvp.presenter.login;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.cgbsoft.lib.base.model.UserInfoDataEntity;
import com.cgbsoft.lib.base.mvp.presenter.BasePresenter;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.utils.tools.MD5Utils;
import com.cgbsoft.lib.widget.LoadingDialog;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.mvp.ui.home.MainPageActivity;
import com.cgbsoft.privatefund.mvp.view.login.SetPasswordView;
import com.google.gson.Gson;

/**
 * Created by xiaoyu.zhang on 2016/11/23 15:50
 * Email:zhangxyfs@126.com
 *  
 */
public class SetPasswordPresenter extends BasePresenter<SetPasswordView> {
    private Context context;

    public SetPasswordPresenter(Context context, SetPasswordView view) {
        super(view);
        this.context = context;
    }

    public void resetPwd(LoadingDialog loadingDialog, String un, String pwd, String code) {
        loadingDialog.setLoading(context.getString(R.string.reseting_str));
        loadingDialog.show();
        addSubscription(ApiClient.resetPwd(un, MD5Utils.getShortMD5(pwd), code).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                loadingDialog.setResult(true, "重置成功", 1000, () -> toNormalLogin(loadingDialog, un, pwd, false));
            }

            @Override
            protected void onRxError(Throwable error) {
                loadingDialog.setResult(true, "重置失败", 1000);
            }
        }));
    }

    /**
     * 登录接口
     *
     * @param un   用户名
     * @param pwd  密码
     * @param isWx 是否微信登录
     */
    private void toNormalLogin(@NonNull LoadingDialog loadingDialog, String un, String pwd, boolean isWx) {
        loadingDialog.setLoading(context.getString(R.string.la_login_loading_str));
        loadingDialog.show();
        pwd = isWx ? pwd : MD5Utils.getShortMD5(pwd);
        addSubscription(ApiClient.toLogin(un, pwd).subscribe(new RxSubscriber<UserInfoDataEntity.Result>() {
            @Override
            protected void onEvent(UserInfoDataEntity.Result loginBean) {
                SPreference.saveUserId(context.getApplicationContext(), loginBean.userId);
                SPreference.saveToken(context.getApplicationContext(), loginBean.token);

                SPreference.saveLoginFlag(context, true);
                if (loginBean.userInfo != null)
                    SPreference.saveUserInfoData(context, new Gson().toJson(loginBean.userInfo));
                loadingDialog.setResult(true, context.getString(R.string.la_login_succ_str), 1000, () -> {
                    context.startActivity(new Intent(context, MainPageActivity.class));
                    getView().toFinish();
                });
            }

            @Override
            protected void onRxError(Throwable error) {
                loadingDialog.setResult(false, context.getString(R.string.la_getinfo_error_str), 1000);
            }
        }));
    }


    @Override
    public void detachView() {
        super.detachView();
        context = null;
    }
}
