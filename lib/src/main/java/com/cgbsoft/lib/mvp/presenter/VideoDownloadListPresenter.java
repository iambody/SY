package com.cgbsoft.lib.mvp.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.mvp.contract.VideoDownloadListContract;

/**
 * 
 *  Created by xiaoyu.zhang on 2016/12/12 17:30
 *  Email:zhangxyfs@126.com
 *  
 */
public class VideoDownloadListPresenter extends BasePresenterImpl<VideoDownloadListContract.View> implements VideoDownloadListContract.Presenter {

    public VideoDownloadListPresenter(@NonNull Context context, @NonNull VideoDownloadListContract.View view) {
        super(context, view);
    }


}
