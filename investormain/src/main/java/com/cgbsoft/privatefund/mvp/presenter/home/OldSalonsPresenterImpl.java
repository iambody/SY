package com.cgbsoft.privatefund.mvp.presenter.home;

import android.content.Context;
import android.support.annotation.NonNull;

import com.cgbsoft.lib.base.model.OldSalonsEntity;
import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.privatefund.model.OldSalonsModelListener;
import com.cgbsoft.privatefund.model.impl.OldSalonsModelImpl;
import com.cgbsoft.privatefund.mvp.contract.home.OldSalonsContract;

import java.util.List;

/**
 * Created by sunfei on 2017/7/13 0013.
 */

public class OldSalonsPresenterImpl extends BasePresenterImpl<OldSalonsContract.OldSalonsView> implements OldSalonsContract.OldSalonsPresenter,OldSalonsModelListener {
    private final OldSalonsContract.OldSalonsView salonsView;
    private final OldSalonsModelImpl salonsModel;

    public OldSalonsPresenterImpl(@NonNull Context context, @NonNull OldSalonsContract.OldSalonsView view) {
        super(context, view);
        this.salonsView=view;
        salonsModel=new OldSalonsModelImpl();
    }

    @Override
    public void getOldSalons(int offset, int limit) {
        salonsView.showLoadDialog();
        salonsModel.getOldSalons(getCompositeSubscription(),this,offset,limit);
    }

    @Override
    public void getDataSuccess(List<OldSalonsEntity.SalonItemBean> oldSalons) {
        salonsView.hideLoadDialog();
        salonsView.getDataSuccess(oldSalons);
    }

    @Override
    public void getDataError(Throwable error) {
        salonsView.hideLoadDialog();
        salonsView.getDataError(error);
    }
}
