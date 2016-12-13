package com.cgbsoft.lib.mvp.contract;

import com.cgbsoft.lib.base.mvp.presenter.BasePresenter;
import com.cgbsoft.lib.base.mvp.view.BaseView;
import com.cgbsoft.lib.mvp.ui.model.VideoDownloadListModel;

import java.util.List;

/**
 * Created by xiaoyu.zhang on 2016/12/12 17:31
 * Email:zhangxyfs@126.com
 *  
 */
public interface VideoDownloadListContract {

    interface Presenter extends BasePresenter {
        void getLocalDataList(boolean isRef);

        void delete(String videoId);
    }

    interface View extends BaseView {
        void getLocalListSucc(List<VideoDownloadListModel> dataList, boolean isRef);

        void getLocalListFail(boolean isRef);
    }
}
