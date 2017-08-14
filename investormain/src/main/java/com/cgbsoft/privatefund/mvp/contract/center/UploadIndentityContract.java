package com.cgbsoft.privatefund.mvp.contract.center;

import com.cgbsoft.lib.base.mvp.presenter.BasePresenter;
import com.cgbsoft.lib.base.mvp.view.BaseView;

/**
 * Created by fei on 2017/8/12.
 */

public interface UploadIndentityContract {
    interface UploadIndentityView extends BaseView {
        /**
         * 显示loading弹窗
         */
        void showLoadDialog();
        /**
         * 隐藏loading弹窗
         */
        void hideLoadDialog();
        void uploadIndentitySuccess();
        void uploadIndentityError(Throwable error);
    }
    interface UploadIndentityPresenter extends BasePresenter {
        void uploadIndentity();
    }
}
