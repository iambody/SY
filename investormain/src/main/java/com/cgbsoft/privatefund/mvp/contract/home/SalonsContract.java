package com.cgbsoft.privatefund.mvp.contract.home;

import com.cgbsoft.lib.base.model.SalonsEntity;
import com.cgbsoft.lib.base.mvp.presenter.BasePresenter;
import com.cgbsoft.lib.base.mvp.view.BaseView;

import java.util.List;

/**
 * Created by sunfei on 2017/7/13 0013.
 */

public interface SalonsContract {
    interface SalonsView extends BaseView {
        /**
         * 显示loading弹窗
         */
        void showLoadDialog();
        /**
         * 隐藏loading弹窗
         */
        void hideLoadDialog();
        void getDataSuccess(List<SalonsEntity.SalonItemBean> salons, List<SalonsEntity.CityBean> citys);
        void getDataError(Throwable error);
    }
    interface SalonsPresenter extends BasePresenter {
        void getSalonsAndCitys(String cityCode,int offset,int limit);
    }
}
