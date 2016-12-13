package com.cgbsoft.lib.mvp.ui;

import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;

import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.mvp.contract.VideoDownloadListContract;
import com.cgbsoft.lib.mvp.presenter.VideoDownloadListPresenter;

/**
 * 视频下载列表
 * Created by xiaoyu.zhang on 2016/12/12 17:30
 * Email:zhangxyfs@126.com
 *  
 */
public class VideoDownloadListActivity extends BaseActivity<VideoDownloadListPresenter> implements VideoDownloadListContract.View {


    @Override
    protected void after() {
        super.after();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    @Override
    protected int layoutID() {
        return 0;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }

    @Override
    protected VideoDownloadListPresenter createPresenter() {
        return new VideoDownloadListPresenter(this, this);
    }
}
