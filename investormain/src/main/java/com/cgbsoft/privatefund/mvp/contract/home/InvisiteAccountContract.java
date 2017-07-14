package com.cgbsoft.privatefund.mvp.contract.home;

import com.cgbsoft.lib.base.mvp.presenter.BasePresenter;
import com.cgbsoft.lib.base.mvp.view.BaseView;
import com.cgbsoft.privatefund.model.DiscoverModel;

/**
 * @author chenlong
 */
public interface InvisiteAccountContract {

    interface Presenter extends BasePresenter{
        void commitInvisiteAccount(String userId, String customerName, String customerIdType, String customerIdNumber);
    }

    interface View extends BaseView{

        void commitSuccess();

        void commitFailure();
    }
}
