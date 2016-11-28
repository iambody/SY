package com.cgbsoft.privatefund.mvp.presenter.discovery;

import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.privatefund.mvp.contract.discovery.DiscoveryContract;

/**
 * Created by xiaoyu.zhang on 2016/11/17 09:33
 * Email:zhangxyfs@126.com
 *  
 */
public class DiscoveryPresenter extends BasePresenterImpl<DiscoveryContract.View> implements DiscoveryContract.Presenter {

    public DiscoveryPresenter(DiscoveryContract.View view) {
        super(view);
    }
}
