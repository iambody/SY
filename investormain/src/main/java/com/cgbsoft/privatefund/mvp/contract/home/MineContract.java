package com.cgbsoft.privatefund.mvp.contract.home;

import com.cgbsoft.lib.base.mvp.presenter.BasePresenter;
import com.cgbsoft.lib.base.mvp.view.BaseView;
import com.cgbsoft.privatefund.model.MineModel;

/**
 * @author chenlong
 */
public interface MineContract {

    interface Presenter extends BasePresenter{

        void getMineData();
    }

    interface View extends BaseView{

        void requestDataSuccess(MineModel mineModel);

        void requestDataFailure(String errMsg);

        /**
         *
         * @param identity 身份码
         * @param hasIdCard 用户选择的是身份证类型，1代表已经上传了身份证，0是还未传身份证
         * @param title 用户身份名称
         * @param credentialCode 证件码
         * @param status 证件审核状态的汉语名字
         * @param statusCode 证件审核状态的code码
         */
        void verifyIndentitySuccess(String identity, String hasIdCard, String title, String credentialCode,String status,String statusCode,String customerName,String credentialNumber,String credentialTitle,String existStatus);

        void verifyIndentityError(Throwable e);
    }
}
