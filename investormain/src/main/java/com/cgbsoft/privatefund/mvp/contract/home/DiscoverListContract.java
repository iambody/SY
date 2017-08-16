package com.cgbsoft.privatefund.mvp.contract.home;

import com.cgbsoft.lib.base.model.DiscoveryListModel;
import com.cgbsoft.lib.base.mvp.presenter.BasePresenter;
import com.cgbsoft.lib.base.mvp.view.BaseView;

import java.util.List;

/**
 * @author chenlong
 */
public interface DiscoverListContract {

    interface Presenter extends BasePresenter{

        void getDiscoveryListData(String offset, String category);
    }

    interface View extends BaseView{

        void requestListDataSuccess(List<DiscoveryListModel> discoveryListModel);

        void requestListDataFailure(String errMsg);
    }
}
