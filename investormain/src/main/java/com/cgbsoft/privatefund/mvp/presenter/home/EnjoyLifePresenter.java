package com.cgbsoft.privatefund.mvp.presenter.home;

import android.content.Context;
import android.support.annotation.NonNull;

import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.privatefund.mvp.contract.home.EnjoyLifeContract;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/6/28-11:30
 */
public class EnjoyLifePresenter extends BasePresenterImpl<EnjoyLifeContract.View>implements EnjoyLifeContract.Presenter {
    public EnjoyLifePresenter(@NonNull Context context, @NonNull EnjoyLifeContract.View view) {
        super(context, view);
    }
}
