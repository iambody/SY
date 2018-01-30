package com.cgbsoft.privatefund.mvp.ui.home;

import android.os.Bundle;
import android.view.View;

import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.base.mvp.ui.BaseFragment;
import com.cgbsoft.lib.base.webview.BaseWebview;
import com.cgbsoft.lib.base.webview.CwebNetConfig;
import com.cgbsoft.privatefund.R;

import butterknife.BindView;

/**
 * @author chenlong
 *         公募基金
 */
public class PublicFundFragment extends BaseFragment {

    @BindView(R.id.webview)
    BaseWebview baseWebview;

    @Override
    protected int layoutID() {
        return R.layout.fragment_public_fund;
    }

    @Override
    protected void init(View view, Bundle savedInstanceState) {
        baseWebview.loadUrl(CwebNetConfig.publicFundHomeUrl);
    }

    @Override
    protected BasePresenterImpl createPresenter() {
        return null;
    }


}
