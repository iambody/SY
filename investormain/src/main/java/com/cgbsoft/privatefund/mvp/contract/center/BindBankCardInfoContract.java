package com.cgbsoft.privatefund.mvp.contract.center;

import com.cgbsoft.lib.base.mvp.presenter.BasePresenter;
import com.cgbsoft.lib.base.mvp.view.BaseView;
import com.cgbsoft.privatefund.bean.BindBankCardInfoBean;

import java.util.List;

/**
 * @author chenlong
 */

public interface BindBankCardInfoContract {

    interface BindBankCardInfoView extends BaseView{
        /**
         * 显示loading弹窗
         */
        void showLoadDialog();
        /**
         * 隐藏loading弹窗
         */
        void hideLoadDialog();

        void requestInfoSuccess(List<BindBankCardInfoBean> listBean);

        void requestInfoFailure(String mssage);
    }

    interface BindBankCardInfoPresenter extends BasePresenter{

        void requestBindBankCardInfo();

    }
}
