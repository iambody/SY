package com.cgbsoft.lib.mvp.contract;

import com.cgbsoft.lib.base.mvp.presenter.BasePresenter;
import com.cgbsoft.lib.base.mvp.view.BaseView;
import com.cgbsoft.lib.mvp.model.VideoInfoModel;

/**
 * Created by xiaoyu.zhang on 2016/12/7 18:08
 * Email:zhangxyfs@126.com
 *  
 */
public interface VideoDetailContract {
    interface Presenter extends BasePresenter {
        void getVideoDetailInfo(String videoId);
        void updataNowPlayTime(int playTime);
        void toVideoLike();
        long getCacheVideoNum();
    }

    interface View extends BaseView {
        void getLocalVideoInfoSucc(VideoInfoModel model);

        void getNetVideoInfoSucc(VideoInfoModel model);

        void toVideoLikeSucc(int likeRes, int likeNum);
    }
}
