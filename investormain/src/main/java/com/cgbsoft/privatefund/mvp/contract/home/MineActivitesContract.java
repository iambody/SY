package com.cgbsoft.privatefund.mvp.contract.home;

import com.cgbsoft.lib.base.mvp.presenter.BasePresenter;
import com.cgbsoft.lib.base.mvp.view.BaseView;
import com.cgbsoft.privatefund.adapter.MineActivitesListAdapter;

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
