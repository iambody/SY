package com.cgbsoft.privatefund.mvp.presenter.center;

import android.content.Context;
import android.support.annotation.NonNull;

import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.privatefund.model.ChangeLoginPsdModelListener;
import com.cgbsoft.privatefund.model.impl.ChangeLoginPsdModelImpl;
import com.cgbsoft.privatefund.mvp.contract.center.ChangePsdContract;

/**
 * 设置页面控制层
 * Created by sunfei on 2017/6/28 0028.
 */

public class ChangePsdPresenterImpl extends BasePresenterImpl<ChangePsdContract.ChangePsdView> implements ChangePsdContract.ChangePsdPresenter ,ChangeLoginPsdModelListener{
    private final ChangePsdContract.ChangePsdView changePsdView;
    private final ChangeLoginPsdModelImpl changeLoginPsdModel;

    public ChangePsdPresenterImpl(@NonNull Context context, @NonNull ChangePsdContract.ChangePsdView view) {
        super(context, view);
        this.changePsdView=view;
        changeLoginPsdModel = new ChangeLoginPsdModelImpl();
    }

    @Override
    public void submitChangeRequest(String userName,String oldPsd,String newPsd) {
        changePsdView.showLoadDialog();
        changeLoginPsdModel.submitChangeRequest(getCompositeSubscription(),this,userName,oldPsd,newPsd);
    }

    @Override
    public void changePsdSuccess(String result) {
        changePsdView.hideLoadDialog();
        changePsdView.changePsdSuccess();
    }

    @Override
    public void changePsdError(Throwable error) {
        changePsdView.hideLoadDialog();
        changePsdView.changePsdError(error);
    }
}
