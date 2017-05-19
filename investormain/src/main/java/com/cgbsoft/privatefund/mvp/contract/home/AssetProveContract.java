package com.cgbsoft.privatefund.mvp.contract.home;

import com.cgbsoft.lib.base.mvp.presenter.BasePresenter;
import com.cgbsoft.lib.base.mvp.view.BaseView;

import org.json.JSONArray;

/**
 * @author chenlong
 *  
 */
public interface AssetProveContract {

    interface Presenter extends BasePresenter {

        void uploadAssetProveData(JSONArray assertImage, String investmentType);
    }

    interface View extends BaseView {
        void requestSuccess();

        void requestFailure();

    }
}
