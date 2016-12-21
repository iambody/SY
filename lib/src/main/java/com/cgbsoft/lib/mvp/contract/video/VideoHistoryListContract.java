package com.cgbsoft.lib.mvp.contract.video;

import com.cgbsoft.lib.base.mvp.presenter.BasePresenter;
import com.cgbsoft.lib.base.mvp.view.BaseView;
import com.cgbsoft.lib.mvp.ui.video.model.VideoHistoryModel;

import java.util.List;

/**
 * Created by xiaoyu.zhang on 2016/12/12 17:40
 * Email:zhangxyfs@126.com
 *  
 */
public interface VideoHistoryListContract {

    interface Presenter extends BasePresenter {
        void getLocalVideoInfoList(boolean isRef);

        void delete(String videoId);

    }

    interface View extends BaseView {
        void getLocalListSucc(List<VideoHistoryModel> dataList, boolean isRef);

        void getLocalListFail(boolean isRef);
    }
}
