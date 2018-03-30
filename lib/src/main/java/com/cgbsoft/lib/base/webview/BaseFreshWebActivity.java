package com.cgbsoft.lib.base.webview;

import android.os.Bundle;

import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.base.mvp.ui.BaseActivity;

/**
 * desc  ${DESC}
 * author wangyongkui
 */
public class BaseFreshWebActivity<T extends BasePresenterImpl> extends BaseActivity {
    @Override
    protected int layoutID() {
        return 0;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }

    @Override
    protected BasePresenterImpl createPresenter() {
        return null;
    }
}
