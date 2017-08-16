package com.cgbsoft.privatefund.mvp.contract.home;

import com.cgbsoft.lib.base.model.DiscoverModel;
import com.cgbsoft.lib.base.mvp.presenter.BasePresenter;
import com.cgbsoft.lib.base.mvp.view.BaseView;

/**
 * @author chenlong
 */
public interface DiscoverContract {

    interface Presenter extends BasePresenter{

        void getDiscoveryFirstData();
    }

    interface View extends BaseView{

        void requestFirstDataSuccess(DiscoverModel discoverModel);

        void requestFirstDataFailure(String errMsg);
    }
}
