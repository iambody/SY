package com.cgbsoft.privatefund.mvp.contract.login;

import android.support.annotation.NonNull;

import com.cgbsoft.lib.base.mvp.presenter.BasePresenter;
import com.cgbsoft.lib.base.mvp.view.BaseView;
import com.cgbsoft.lib.widget.LoadingDialog;

/**
 * Created by xiaoyu.zhang on 2016/11/29 14:21
 * Email:zhangxyfs@126.com
 *  
 */
public interface BindPhoneContract {

    interface Presenter extends BasePresenter {
        /**
         * 发送验证码
         *
         * @param loadingDialog 等待框
         * @param un            用户名
         */
        void sendCode(@NonNull LoadingDialog loadingDialog, String un);

        /**
         * 微信账号合并--验证手机号
         */
        void wxMergePhone(@NonNull LoadingDialog loadingDialog, String un, String code);

        /**
         * 合并手机账户－－确认合并
         */
        void wxMergeConfirm(@NonNull LoadingDialog loadingDialog);
    }

    interface View extends BaseView {
        void sendSucc();

        void margeSucc();

    }
}
