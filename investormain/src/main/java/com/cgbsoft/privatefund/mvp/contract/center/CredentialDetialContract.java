package com.cgbsoft.privatefund.mvp.contract.center;

import com.cgbsoft.lib.base.mvp.presenter.BasePresenter;
import com.cgbsoft.lib.base.mvp.view.BaseView;

/**
 * Created by zhaojiaqi on 2017/11/3.
 */

public interface CredentialDetialContract {
    interface CredentialDetialView extends BaseView{
        /**
         * 显示loading弹窗
         */
        void showLoadDialog();
        /**
         * 隐藏loading弹窗
         */
        void hideLoadDialog();

        void getCredentialInfoSuccess(String s);
        void getCredentialInfoError(Throwable error);

    }

    interface CredentialDetialPresenter extends BasePresenter{
        /**
         * 获取证件详情
         * @param CaredentialCode 证件类型
         */
        void getCredentialInfo(String CaredentialCode);
    }
}
