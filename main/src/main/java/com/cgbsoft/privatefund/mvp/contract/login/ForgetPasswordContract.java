package com.cgbsoft.privatefund.mvp.contract.login;

import android.support.annotation.NonNull;

import com.cgbsoft.lib.base.mvp.presenter.BasePresenter;
import com.cgbsoft.lib.base.mvp.view.BaseView;
import com.cgbsoft.lib.widget.LoadingDialog;

/**
 * Created by xiaoyu.zhang on 2016/11/28 11:40
 * Email:zhangxyfs@126.com
 *  
 */
public interface ForgetPasswordContract {

    interface Presenter extends BasePresenter {
        /**
         * 发送验证码
         *
         * @param loadingDialog 等待框
         * @param un            用户名
         */
        void sendCode(@NonNull LoadingDialog loadingDialog, String un);

        /**
         * 对验证码进行验证
         *
         * @param loadingDialog 等待框
         * @param un            用户名
         * @param code          验证码
         */
        void checkCode(@NonNull LoadingDialog loadingDialog, String un, String code);
    }

    interface View extends BaseView {
        void sendSucc();

        void checkSucc();
    }
}
