package com.cgbsoft.privatefund.mvp.presenter.home;

import android.content.Context;
import android.support.annotation.NonNull;

import com.cgbsoft.lib.base.model.SalonsEntity;
import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.privatefund.model.SalonsModelListener;
import com.cgbsoft.privatefund.model.impl.SalonsModelImpl;
import com.cgbsoft.privatefund.mvp.contract.home.SalonsContract;

import java.util.List;

/**
 * Created by sunfei on 2017/7/13 0013.
 */

public class SalonsPresenterImpl extends BasePresenterImpl<SalonsContract.SalonsView> implements SalonsContract.SalonsPresenter,SalonsModelListener {
    private final SalonsContract.SalonsView salonsView;
    private final SalonsModelImpl salonsModel;

    public SalonsPresenterImpl(@NonNull Context context, @NonNull SalonsContract.SalonsView view) {
        super(context, view);
        this.salonsView=view;
        salonsModel=new SalonsModelImpl();
    }

    @Override
    public void getSalonsAndCitys(String cityCode, int offset, int limit) {
        salonsView.showLoadDialog();
        salonsModel.getSalonsAndCitys(getCompositeSubscription(),this,cityCode,offset,limit);
    }

    @Override
    public void getDataSuccess(List<SalonsEntity.SalonItemBean> salons, List<SalonsEntity.CityBean> citys) {
        salonsView.hideLoadDialog();
        salonsView.getDataSuccess(salons,citys);
    }

    @Override
    public void getDataError(Throwable error) {
        salonsView.hideLoadDialog();
        salonsView.getDataError(error);
    }
}
