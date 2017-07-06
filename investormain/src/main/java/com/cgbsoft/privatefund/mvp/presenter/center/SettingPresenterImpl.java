package com.cgbsoft.privatefund.mvp.presenter.center;

import android.content.Context;
import android.support.annotation.NonNull;

import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.privatefund.mvp.contract.center.SettingContract;
import com.cgbsoft.privatefund.mvp.contract.center.SettingContract.SettingView;

/**
 * 设置页面控制层
 * Created by sunfei on 2017/6/28 0028.
 */

public class SettingPresenterImpl extends BasePresenterImpl<SettingView> implements SettingContract.SettingPresenter {
    public SettingPresenterImpl(@NonNull Context context, @NonNull SettingView view) {
        super(context, view);
    }
}
