package com.cgbsoft.privatefund.mvp.ui.login;

import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.privatefund.mvp.presenter.login.LoginBasePresenter;
import com.cgbsoft.privatefund.mvp.view.login.LoginBaseView;

/**
 * Created by xiaoyu.zhang on 2016/11/17 11:42
 * Email:zhangxyfs@126.com
 *  
 */
public class LoginActivity extends BaseActivity<LoginBasePresenter> implements LoginBaseView {

    @Override
    protected int layoutID() {
        return 0;
    }

    @Override
    protected void init() {

    }

    @Override
    protected LoginBasePresenter createPresenter() {
        return new LoginBasePresenter(this);
    }
}
