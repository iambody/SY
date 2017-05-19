package com.cgbsoft.privatefund.mvp.contract.home;

import com.cgbsoft.lib.base.mvp.presenter.BasePresenter;
import com.cgbsoft.lib.base.mvp.view.BaseView;

import org.json.JSONArray;

/**
 * 
 * @author chenlong
 *  
 */
public interface FeedBackUserContract {

    interface Presenter extends BasePresenter{
        void feedBack(String content, JSONArray imageUrl);
    }

    interface View extends BaseView{
        void requestSuccess();

        void requestFailure(String errorMsg);
    }
}
