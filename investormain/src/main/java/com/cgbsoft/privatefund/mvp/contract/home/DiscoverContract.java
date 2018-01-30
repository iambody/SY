package com.cgbsoft.privatefund.mvp.contract.home;

import com.cgbsoft.lib.base.model.DiscoverModel;
import com.cgbsoft.lib.base.model.bean.StockIndexBean;
import com.cgbsoft.lib.base.mvp.presenter.BasePresenter;
import com.cgbsoft.lib.base.mvp.view.BaseView;

import java.util.List;

/**
 * @author chenlong
 */
public interface DiscoverContract {

    interface Presenter extends BasePresenter {

        void getDiscoveryFirstData();

        void getStockIndex();
    }

    interface View extends BaseView {

        void requestFirstDataSuccess(DiscoverModel discoverModel);

        void requestFirstDataFailure(String errMsg);

        void requestStockIndexSuccess(List<StockIndexBean> dataList);

        void reqeustStockIndexFailure(String message);
    }
}
