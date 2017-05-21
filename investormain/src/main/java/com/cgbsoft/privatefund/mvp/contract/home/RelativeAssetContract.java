package com.cgbsoft.privatefund.mvp.contract.home;

import com.cgbsoft.lib.base.mvp.presenter.BasePresenter;
import com.cgbsoft.lib.base.mvp.view.BaseView;

/**
 * @author chenlong
 *  
 */
public interface RelativeAssetContract {

    interface Presenter extends BasePresenter {

        void uploadAssetRelatedFile(String imageUrl);
    }

    interface View extends BaseView {
        void requestSuccess();

        void requestFailure();

        void requestError(String mssage);

    }
}
