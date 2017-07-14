package com.cgbsoft.privatefund.mvp.presenter.center;

import android.content.Context;
import android.support.annotation.NonNull;

import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.privatefund.mvp.contract.center.ChangeNameContract;

/**
 * Created by sunfei on 2017/7/12 0012.
 */

public class ChangeNamePresenterImpl extends BasePresenterImpl<ChangeNameContract.ChangeNameView> implements ChangeNameContract.ChangeNamePersenter{
    public ChangeNamePresenterImpl(@NonNull Context context, @NonNull ChangeNameContract.ChangeNameView view) {
        super(context, view);
    }
}
