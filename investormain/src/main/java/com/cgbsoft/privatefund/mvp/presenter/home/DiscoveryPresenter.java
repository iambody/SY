package com.cgbsoft.privatefund.mvp.presenter.home;

import android.content.Context;
import android.support.annotation.NonNull;

import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.privatefund.mvp.contract.home.DiscoverContract;

/**
 * @author chenlong
 *
 */
public class DiscoveryPresenter extends BasePresenterImpl<DiscoverContract.View> implements DiscoverContract.Presenter {

    public DiscoveryPresenter(@NonNull Context context, @NonNull DiscoverContract.View view) {
        super(context, view);
    }

    @Override
    public void getDiscoveryList() {

    }
}
