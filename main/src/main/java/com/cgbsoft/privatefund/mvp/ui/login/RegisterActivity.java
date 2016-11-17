package com.cgbsoft.privatefund.mvp.ui.login;

import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.privatefund.mvp.presenter.login.RegisterPresenter;
import com.cgbsoft.privatefund.mvp.view.login.RegisterView;

/**
 * 注册
 * Created by xiaoyu.zhang on 2016/11/17 18:24
 * Email:zhangxyfs@126.com
 *  
 */
public class RegisterActivity extends BaseActivity<RegisterPresenter> implements RegisterView {

    @Override
    protected int layoutID() {
        return 0;
    }

    @Override
    protected void init() {

    }

    @Override
    protected RegisterPresenter createPresenter() {
        return null;
    }
}
