package com.cgbsoft.privatefund.mvp.contract.center;

import com.cgbsoft.lib.base.mvp.presenter.BasePresenter;
import com.cgbsoft.lib.base.mvp.view.BaseView;
import com.cgbsoft.lib.base.model.bean.CredentialStateMedel;

/**
 * Created by feigecal on 2017/7/8 0008.
 */

public interface PersonalInformationContract {
    interface PersonalInformationView extends BaseView{
        /**
         * 显示loading弹窗
         */
        void showLoadDialog();
        /**
         * 隐藏loading弹窗
         */
        void hideLoadDialog();
        void updateSuccess();
        void updateError(Throwable error);
        void uploadImgSuccess(String imgRemotePath);
        void uploadImgError(Throwable error);
        void verifyIndentitySuccess(boolean hasIndentity,boolean hasUpload,String indentityCode,String title,String credentialCode,String status,String statusCode);

        void verifyIndentityError(Throwable error);

        void verifyIndentityV3Success(CredentialStateMedel credentialStateMedel);
    }
    interface PersonalInformationPresenter extends BasePresenter{
        void updateUserInfoToServer(String userName, String gender, String birthday);
        void uploadRemotePath(String adviserId);
        void verifyIndentity();
        void verifyIndentityV3();
    }
}
