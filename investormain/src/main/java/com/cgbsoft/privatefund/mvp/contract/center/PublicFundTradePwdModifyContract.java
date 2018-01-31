package com.cgbsoft.privatefund.mvp.contract.center;

import com.cgbsoft.lib.base.mvp.presenter.BasePresenter;
import com.cgbsoft.lib.base.mvp.view.BaseView;

/**
 * @author chenlong
 */

public interface PublicFundTradePwdModifyContract {

    interface PublicFundTradePwdModifyView extends BaseView{
        /**
         * 显示loading弹窗
         */
        void showLoadDialog();
        /**
         * 隐藏loading弹窗
         */
        void hideLoadDialog();

        void  modifyPwdSuccess(String info);

        void modifyPwdFailure(String mssage);
    }

    interface PublicFundTradePwdModifyoPresenter extends BasePresenter{

        void modifyPublicFundTradePwd();

        void getPhoneValidateCode(String phone);
    }
}
