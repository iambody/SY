package com.cgbsoft.privatefund.mvp.contract.center;

import com.cgbsoft.lib.base.mvp.presenter.BasePresenter;
import com.cgbsoft.lib.base.mvp.view.BaseView;

/**
 * @author chenlong
 */

public interface PublicFundInfoContract {

    interface PublicFundInfoView extends BaseView{
        /**
         * 显示loading弹窗
         */
        void showLoadDialog();
        /**
         * 隐藏loading弹窗
         */
        void hideLoadDialog();

        void  requestInfoSuccess(String[] info);

        void requestInfoFailure(String mssage);
    }

    interface PublicFundInfoPresenter extends BasePresenter{

        void requestPublicFundInfo();

    }
}
