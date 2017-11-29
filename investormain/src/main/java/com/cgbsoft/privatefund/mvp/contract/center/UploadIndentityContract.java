package com.cgbsoft.privatefund.mvp.contract.center;

import com.cgbsoft.lib.base.mvp.presenter.BasePresenter;
import com.cgbsoft.lib.base.mvp.view.BaseView;

import java.util.List;

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
        void uploadIndentitySuccess(String s);
        void uploadIndentityError(Throwable error);

        void credentialDetialSuccess(String s);
        void credentialDetialError(Throwable error);

        void uploadOtherCrenditral(String s);

        void getLivingCountSuccess(String s);
        void getLivingCountError(Throwable error);
    }
    interface UploadIndentityPresenter extends BasePresenter {
        /**
         * @param remoteParams 远程地址集合
         * @param customerCode 父级身份code
         * @param credentialCode 子级身份code
         */
        void uploadIndentity(List<String> remoteParams,String customerCode,String credentialCode);

        void uploadOtherCrenditial(List<String> remoteParams,String customerCode,String credentialCode,String remotePerson);

        void getLivingCount();
    }
}
