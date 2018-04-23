package com.cgbsoft.privatefund.public_fund.model;

import com.cgbsoft.lib.base.mvp.presenter.BasePresenter;
import com.cgbsoft.lib.base.mvp.view.BaseView;

import java.util.HashMap;

/**
 * desc  ${DESC}
 * author wangyongkui
 */
public class TransactionPwdContract {
    public interface transactionPwdView extends BaseView {
        /**
         * 显示loading弹窗
         */
        void showLoadDialog();

        /**
         * 隐藏loading弹窗
         */
        void hideLoadDialog();

        /**
         * 请求数据
         */
        void getTransactionPwdSuccess(String str);

        /**
         * 请求失败
         */

        void getTransactionPwdError(String error);


    }

    public interface transactionPwdPresenter extends BasePresenter {
        void transactionPwdAction(HashMap<String, Object> map );
    }
}
