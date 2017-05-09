package com.cgbsoft.privatefund.mvp.ui.product;

import android.os.Bundle;
import android.view.View;

import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.base.mvp.ui.BaseFragment;
import com.cgbsoft.lib.base.webview.BaseWebview;
import com.cgbsoft.lib.base.webview.CwebNetConfig;
import com.cgbsoft.privatefund.R;

import butterknife.BindView;

/**
 *  产品
 *  Created by xiaoyu.zhang on 2016/11/15 14:09
 *  Email:zhangxyfs@126.com
 *  
 */
public class ProductFragment extends BaseFragment {

    @BindView(R.id.webView)
    BaseWebview baseWebview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int layoutID() {
        return R.layout.fragment_webview_common;
    }

    @Override
    protected void init(View view, Bundle savedInstanceState) {
        baseWebview.loadUrls(CwebNetConfig.productPage);
    }

    @Override
    protected BasePresenterImpl createPresenter() {
        return null;
    }
}
