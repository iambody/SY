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
 * 俱乐部
 * Created by xiaoyu.zhang on 2016/11/15 14:10
 * Email:zhangxyfs@126.com
 *  
 */
public class ClubFragment extends BaseFragment {

    @BindView(R.id.webView)
    BaseWebview baseWebview;


    @Override
    protected int layoutID() {
        return R.layout.fragment_webview_common;
    }

    @Override
    protected void init(View view, Bundle savedInstanceState) {
        baseWebview.loadUrls(CwebNetConfig.clubPgge);
    }

    @Override
    protected BasePresenterImpl createPresenter() {
        return null;
    }
}
