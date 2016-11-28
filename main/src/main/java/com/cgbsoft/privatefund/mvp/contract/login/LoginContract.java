package com.cgbsoft.privatefund.mvp.contract.login;

import android.support.annotation.NonNull;

import com.cgbsoft.lib.base.mvp.presenter.BasePresenter;
import com.cgbsoft.lib.base.mvp.view.BaseView;
import com.cgbsoft.lib.widget.CustomDialog;
import com.cgbsoft.lib.widget.LoadingDialog;

/**
 * 
 *  Created by xiaoyu.zhang on 2016/11/28 11:58
 *  Email:zhangxyfs@126.com
 *  
 */
public interface LoginContract {

    interface Presenter extends BasePresenter{

        /**
         * 登录接口
         *
         * @param un   用户名
         * @param pwd  密码
         * @param isWx 是否微信登录
         */
        void toNormalLogin(@NonNull LoadingDialog loadingDialog, String un, String pwd, boolean isWx);

        /**
         * 微信登陆
         */
        void toWxLogin(@NonNull LoadingDialog loadingDialog, @NonNull CustomDialog.Builder builder, String unionid, String sex, String nickName, String headimgurl);

        /**
         * 微信登录
         */
        void toDialogWxLogin(@NonNull LoadingDialog loadingDialog, String unionid, String sex, String nickName, String headimgurl);

    }

    interface View extends BaseView{
        void loginSuccess();

        void loginFail();
    }
}
