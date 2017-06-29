package com.cgbsoft.privatefund.mvp.presenter.home;


import android.content.Context;
import android.support.annotation.NonNull;

import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.privatefund.mvp.contract.home.HappyLifeContract.HappyLifePresenter;
import com.cgbsoft.privatefund.mvp.contract.home.HappyLifeContract.HappyLifeView;

/**
 * Created by sunfei on 2017/6/28 0028.
 */

public class HappyLifePresenterImpl extends BasePresenterImpl<HappyLifeView> implements HappyLifePresenter {
    public HappyLifePresenterImpl(@NonNull Context context, @NonNull HappyLifeView view) {
        super(context, view);
    }
}
