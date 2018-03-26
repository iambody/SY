package com.cgbsoft.privatefund.mvp.presenter.center;

import android.content.Context;
import android.support.annotation.NonNull;

import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.privatefund.mvp.contract.center.PublicFundSettingContract;

/**
 * desc  ${DESC}
 * author yangzonghui  yangzonghui@simuyun.com
 * 日期 2018/3/26-上午10:25
 */

public class PublicFundSettingPresenterImpl extends BasePresenterImpl<PublicFundSettingContract.PublicFundSettingView> implements PublicFundSettingContract.PublicFundSettingPresenter {
    public PublicFundSettingPresenterImpl(@NonNull Context context, @NonNull PublicFundSettingContract.PublicFundSettingView view) {
        super(context, view);
    }
}