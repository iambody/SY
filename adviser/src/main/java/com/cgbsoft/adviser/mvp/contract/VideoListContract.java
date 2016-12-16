package com.cgbsoft.adviser.mvp.contract;

import com.cgbsoft.adviser.mvp.ui.college.adapter.VideoListAdapter;
import com.cgbsoft.lib.base.mvp.presenter.BasePresenter;
import com.cgbsoft.lib.base.mvp.view.BaseView;

/**
 * 
 *  Created by xiaoyu.zhang on 2016/12/7 15:03
 *  Email:zhangxyfs@126.com
 *  
 */
public interface VideoListContract {
    interface Presenter extends BasePresenter {

        void getVideoListData(VideoListAdapter adapter, String type, boolean isRef);

    }

    interface View extends BaseView {
        void getVideoListDataSucc(boolean isRef);

        void getVideoListDataFail(boolean isRef);
    }
}
