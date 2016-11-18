package com.cgbsoft.privatefund.mvp.presenter.login;

import android.content.Context;

import com.cgbsoft.lib.base.model.bean.UserInfo;
import com.cgbsoft.lib.base.mvp.presenter.BasePresenter;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.utils.tools.MD5Utils;
import com.cgbsoft.lib.widget.LoadingDialog;
import com.cgbsoft.lib.widget.MToast;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.mvp.view.login.LoginView;
import com.google.gson.Gson;

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
    public void toLogin(final LoadingDialog dialog, String un, String pwd, boolean isWx) {
        dialog.show();
        pwd = isWx ? pwd : MD5Utils.getShortMD5(pwd);
        addSubscription(ApiClient.toLogin(un, pwd).flatMap(loginBean -> {
            SPreference.saveUserId(context.getApplicationContext(), loginBean.userId);
            SPreference.saveToken(context.getApplicationContext(), loginBean.token);
            return ApiClient.getUserInfo(loginBean.userId);
        }), new RxSubscriber<UserInfo>() {
            @Override
            protected void onEvent(UserInfo userInfo) {
                SPreference.saveLoginFlag(context, true);
                SPreference.saveUserInfoData(context, new Gson().toJson(userInfo));
                //todo 开启容云

                dialog.dismiss();
            }

            @Override
            protected void onRxError(Throwable error) {
                MToast.makeText(context, context.getString(R.string.la_getinfo_error_str), MToast.LENGTH_SHORT);
                dialog.dismiss();
            }
        });
    }

    @Override
    public void detachView() {
        super.detachView();
        context = null;
    }
}
