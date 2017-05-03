package com.cgbsoft.privatefund.mvp.ui.discovery;

import android.os.Bundle;
import android.view.View;

import com.cgbsoft.lib.base.mvp.ui.BaseFragment;
import com.cgbsoft.privatefund.mvp.contract.discovery.DiscoveryContract;
import com.cgbsoft.privatefund.mvp.presenter.discovery.DiscoveryPresenter;

/**
 * 发现
 * Created by xiaoyu.zhang on 2016/11/15 14:10
 * Email:zhangxyfs@126.com
 *  
 */
public class DiscoveryFragment extends BaseFragment<DiscoveryPresenter> implements DiscoveryContract.View {
    @Override
    protected int layoutID() {
        return 0;
    }

    @Override
    protected void init(View view, Bundle savedInstanceState) {

    }

    @Override
    protected DiscoveryPresenter createPresenter() {
        return new DiscoveryPresenter(getContext(), this);
    }
}
