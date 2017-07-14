package com.cgbsoft.privatefund.mvp.contract.center;

import com.cgbsoft.lib.base.mvp.presenter.BasePresenter;
import com.cgbsoft.lib.base.mvp.view.BaseView;

/**
 * Created by feigecal on 2017/6/28 0028.
 */

public interface ChangePsdContract {
    interface ChangePsdView extends BaseView{
        /**
         * 显示loading弹窗
         */
        void showLoadDialog();
        /**
         * 隐藏loading弹窗
         */
        void hideLoadDialog();
        void changePsdSuccess();
        void changePsdError(Throwable error);
    }
    interface ChangePsdPresenter extends BasePresenter{
        void submitChangeRequest(String userName,String oldPsd,String newPsd);
    }
}
