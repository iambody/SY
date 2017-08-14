package com.cgbsoft.privatefund.mvp.presenter.center;

import android.content.Context;
import android.support.annotation.NonNull;

import com.cgbsoft.lib.base.model.IndentityEntity;
import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.privatefund.model.impl.IndentityModelImpl;
import com.cgbsoft.privatefund.model.IndentityModelListener;
import com.cgbsoft.privatefund.mvp.contract.center.SelectIndentityContract;

import java.util.List;

/**
 * Created by fei on 2017/8/11.
 */

public class SelectIndentityPresenterImpl extends BasePresenterImpl<SelectIndentityContract.SelectIndentityView> implements SelectIndentityContract.SelectIndentityPresenter ,IndentityModelListener{
    private final SelectIndentityContract.SelectIndentityView indentityView;
    private final IndentityModelImpl indentityModel;

    public SelectIndentityPresenterImpl(@NonNull Context context, @NonNull SelectIndentityContract.SelectIndentityView view) {
        super(context, view);
        this.indentityView=view;
        indentityModel = new IndentityModelImpl();
    }

    @Override
    public void getIndentityList() {
        indentityView.showLoadDialog();
        indentityModel.getIndentitys(getCompositeSubscription(),this);
    }

    @Override
    public void getIndentityListSuccess(List<IndentityEntity.IndentityBean> indentityBeen) {
        indentityView.hideLoadDialog();
        indentityView.getIndentityListSuccess(indentityBeen);

    }

    @Override
    public void getIndentityListError(Throwable error) {
        indentityView.hideLoadDialog();
        indentityView.getIndentityListError(error);
    }
}
