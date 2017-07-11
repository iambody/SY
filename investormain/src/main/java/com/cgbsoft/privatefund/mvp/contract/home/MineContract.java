package com.cgbsoft.privatefund.mvp.contract.home;

import com.cgbsoft.lib.base.mvp.presenter.BasePresenter;
import com.cgbsoft.lib.base.mvp.view.BaseView;
import com.cgbsoft.privatefund.model.DiscoveryListModel;
import com.cgbsoft.privatefund.model.MineModel;

import java.util.List;

/**
 * @author chenlong
 */
public interface MineContract {

    interface Presenter extends BasePresenter{

        void getMineData();
    }

    interface View extends BaseView{

        void requestDataSuccess(MineModel mineModel);

        void requestDataFailure(String errMsg);
    }
}