package com.cgbsoft.privatefund.mvp.contract.center;

import com.cgbsoft.lib.base.model.CardListEntity;
import com.cgbsoft.lib.base.mvp.presenter.BasePresenter;
import com.cgbsoft.lib.base.mvp.view.BaseView;
import com.cgbsoft.privatefund.model.CredentialModel;
import com.cgbsoft.privatefund.model.CredentialStateMedel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fei on 2017/8/15.
 */

public interface DatumManageContract {
    interface DatumManageView extends BaseView {
        /**
         * 显示loading弹窗
         */
        void showLoadDialog();

        /**
         * 隐藏loading弹窗
         */
        void hideLoadDialog();

        void verifyIndentitySuccess(boolean hasIndentity, boolean hasUpload, String indentityCode, String title, String credentialCode, String status, String statusCode);

        void verifyIndentityError(Throwable error);

        void verifyIndentitySuccessV3(CredentialStateMedel credentialStateMedel);

        void getLivingCountSuccess(String s);

        void getLivingCountError(Throwable error);

        void getCredentialDetialSuccess(CredentialModel credentialModel);

        void getCredentialDetialError(Throwable error);

        void uploadOtherCrendtialSuccess(String s);

        void uploadOtherCrendtialError(Throwable error);
    }

    interface DatumManagePresenter extends BasePresenter {
        void verifyIndentity();

        void verifyIndentityV3();

        void getLivingCount();

        void getCredentialDetial(String credentialCode);

        void uploadOtherCrendtial(List<String> remoteParams, String credentialCode, String customCode, String remotePerson);
    }
}
