package com.cgbsoft.privatefund.mvp.ui.login;

import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.privatefund.mvp.presenter.login.ForgetPasswordPresenter;
import com.cgbsoft.privatefund.mvp.view.login.ForgetPasswordView;

/**
 * 忘记密码
 * Created by xiaoyu.zhang on 2016/11/18 14:50
 * Email:zhangxyfs@126.com
 *  
 */
public class ForgetPasswordActivity extends BaseActivity<ForgetPasswordPresenter> implements ForgetPasswordView {
    @Override
    protected int layoutID() {
        return 0;
    }

    @Override
    protected void init() {

    }

    @Override
    protected ForgetPasswordPresenter createPresenter() {
        return null;
    }
}
