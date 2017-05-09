package com.cgbsoft.privatefund.mvp.ui.discovery;

import android.os.Bundle;
import android.view.View;

import com.cgbsoft.lib.R2;
import com.cgbsoft.lib.base.mvp.ui.BaseFragment;
import com.cgbsoft.lib.base.webview.BaseWebview;
import com.cgbsoft.lib.base.webview.CwebNetConfig;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.mvp.contract.discovery.DiscoveryContract;
import com.cgbsoft.privatefund.mvp.presenter.discovery.DiscoveryPresenter;

import butterknife.BindView;

/**
 * 发现
 * Created by xiaoyu.zhang on 2016/11/15 14:10
 * Email:zhangxyfs@126.com
 *  
 */
public class DiscoveryFragment extends BaseFragment<DiscoveryPresenter> implements DiscoveryContract.View {

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
        baseWebview.loadUrls(CwebNetConfig.discoverPage);
    }

    @Override
    protected DiscoveryPresenter createPresenter() {
        return new DiscoveryPresenter(getContext(), this);
    }
}
