package com.cgbsoft.lib.mvp.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.mvp.contract.VideoDownloadListContract;
import com.cgbsoft.lib.mvp.model.VideoInfoModel;
import com.cgbsoft.lib.utils.db.DaoUtils;

import java.util.List;

/**
 * 
 *  Created by xiaoyu.zhang on 2016/12/12 17:30
 *  Email:zhangxyfs@126.com
 *  
 */
public class VideoDownloadListPresenter extends BasePresenterImpl<VideoDownloadListContract.View> implements VideoDownloadListContract.Presenter {
    private DaoUtils daoUtils;

    public VideoDownloadListPresenter(@NonNull Context context, @NonNull VideoDownloadListContract.View view) {
        super(context, view);
        daoUtils = new DaoUtils(context, DaoUtils.W_VIDEO);
    }

    @Override
    public void getLocalDataList(boolean isRef) {
        List<VideoInfoModel> list = daoUtils.getAllVideoInfoHistory();
    }

    @Override
    public void delete(String videoId){
        daoUtils.delteVideoInfo(videoId);
    }



    @Override
    public void detachView() {
        super.detachView();
        if (daoUtils != null) {
            daoUtils.destory();
            daoUtils = null;
        }
    }

}
