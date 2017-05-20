package com.cgbsoft.privatefund.mvp.contract.home;

import com.cgbsoft.lib.base.mvp.presenter.BasePresenter;
import com.cgbsoft.lib.base.mvp.view.BaseView;

import java.util.HashMap;

/**
 * @author chenlong
 *          
 */
public interface ModifyUserInfoContract {

    interface Presenter extends BasePresenter {

        void modifyUserInfo(HashMap<String, String> hashMap, boolean isFiveTimes);

        void validateUserPassword(HashMap<String, String> hashMap);
    }

    interface View extends BaseView {

        void modifyUserSuccess(boolean isFiveTimes);

        void modifyUserFailure();

        void validatePasswordSuccess();

        void validatePasswordFailure();

        void validatePasswordError(String errorMsg);
    }
}
