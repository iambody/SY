package com.cgbsoft.privatefund.mvp.contract.home;

import com.cgbsoft.lib.base.mvp.presenter.BasePresenter;
import com.cgbsoft.lib.base.mvp.view.BaseView;
import com.cgbsoft.privatefund.adapter.MineActivitesListAdapter;
import com.cgbsoft.privatefund.model.DiscoverModel;
import com.cgbsoft.privatefund.model.MineActivitesModel;

import java.util.List;

import app.privatefund.investor.health.adapter.CheckHealthAdapter;

/**
 * @author chenlong
 */
public interface MineActivitesContract {

    interface Presenter extends BasePresenter{

        void getActivitesList(MineActivitesListAdapter adapter, boolean isRef);
    }

    interface View extends BaseView{

        void requestDataSuccess(boolean isRef);

        void requestDataFailure(String errMsg);
    }
}
