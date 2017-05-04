package com.cgbsoft.privatefund.mvp.contract.login;

import android.support.annotation.NonNull;

import com.cgbsoft.lib.base.mvp.presenter.BasePresenter;
import com.cgbsoft.lib.base.mvp.view.BaseView;
import com.cgbsoft.lib.widget.LoadingDialog;

/**
 * 
 *  Created by xiaoyu.zhang on 2016/11/28 12:03
 *  Email:zhangxyfs@126.com
 *  
 */
public interface RegisterContract {

    interface Presenter extends BasePresenter{
        /**
         * 注册
         * @param loadingDialog
         * @param un
         * @param pwd
         * @param code
         */
        void toRegister(@NonNull LoadingDialog loadingDialog, String un, String pwd, String code);

        /**
         * 发送验证码
         * @param loadingDialog
         * @param un
         */
        void sendCode(@NonNull LoadingDialog loadingDialog, String un);


    }

    interface View extends BaseView{
        void regSucc();
        void regFail();

        void sendSucc();
    }
}