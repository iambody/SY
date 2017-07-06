package com.cgbsoft.privatefund.mvp.presenter.center;

import android.content.Context;
import android.support.annotation.NonNull;

import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.privatefund.mvp.contract.center.ChangePsdContract;

/**
 * 设置页面控制层
 * Created by sunfei on 2017/6/28 0028.
 */

public class ChangePsdPresenterImpl extends BasePresenterImpl<ChangePsdContract.ChangePsdView> implements ChangePsdContract.ChangePsdPresenter {
    public ChangePsdPresenterImpl(@NonNull Context context, @NonNull ChangePsdContract.ChangePsdView view) {
        super(context, view);
    }
}
