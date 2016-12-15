package com.cgbsoft.lib.mvp.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.cgbsoft.lib.R;
import com.cgbsoft.lib.base.model.VideoInfoEntity;
import com.cgbsoft.lib.base.model.VideoLikeEntity;
import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.mvp.contract.VideoDetailContract;
import com.cgbsoft.lib.mvp.model.VideoInfoModel;
import com.cgbsoft.lib.utils.cache.CacheManager;
import com.cgbsoft.lib.utils.constant.VideoStatus;
import com.cgbsoft.lib.utils.db.DaoUtils;
import com.cgbsoft.lib.utils.listener.VideoDownloadCallback;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okserver.download.DownloadInfo;
import com.lzy.okserver.download.DownloadManager;
import com.lzy.okserver.download.DownloadService;

/**
 * Created by xiaoyu.zhang on 2016/12/7 18:09
 * Email:zhangxyfs@126.com
 *  
 */
public class VideoDetailPresenter extends BasePresenterImpl<VideoDetailContract.View> implements VideoDetailContract.Presenter {
    private DaoUtils daoUtils;
    private VideoInfoModel viModel;
    private boolean isInitData;
    private DownloadManager downloadManager;
    private VideoDownloadCallback videoDownloadCallback;

    public VideoDetailPresenter(@NonNull Context context, @NonNull VideoDetailContract.View view) {
        super(context, view);
        daoUtils = new DaoUtils(context, DaoUtils.W_VIDEO);
        downloadManager = DownloadService.getDownloadManager();
        downloadManager.getThreadPool().setCorePoolSize(1);
        downloadManager.setTargetFolder(CacheManager.getCachePath(context, CacheManager.VIDEO));
    }


    public void getLocalVideoDetailInfo(String videoId) {
        getVideoDetailInfo(videoId);
        getView().getLocalVideoInfoSucc(viModel);
    }


    @Override
    public void toDownload(String videoId) {
        viModel = getVideoInfo(videoId);
        String videoUrl;
        if (viModel.downloadtype == VideoStatus.HD) {//高清
            videoUrl = viModel.hdUrl;
        } else {
            videoUrl = viModel.sdUrl;
        }
        if (getDownloadManager() == null) {
            return;
        }
        DownloadInfo info = getDownloadManager().getDownloadInfo(videoId);
        videoDownloadCallback = new VideoDownloadCallback(videoId);
        GetRequest getRequest = OkGo.get(videoUrl);
        if (info == null) {
            getDownloadManager().addTask(videoId, getRequest, videoDownloadCallback);
        } else {
            switch (info.getState()) {
                case DownloadManager.PAUSE:
                case DownloadManager.NONE:
                case DownloadManager.ERROR:
                    getDownloadManager().addTask(videoId, getRequest, videoDownloadCallback);
                    break;
            }
        }
    }

    @Override
    public void getVideoDetailInfo(String videoId) {
        getLocalVideoInfo(videoId);

        if (viModel != null) {
            getView().getLocalVideoInfoSucc(viModel);
        } else {
            viModel = new VideoInfoModel();
            isInitData = true;
        }

        addSubscription(ApiClient.getTestVideoInfo(videoId).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                VideoInfoEntity.Result result = new Gson().fromJson(s, VideoInfoEntity.Result.class);

                viModel.videoId = result.videoId;
                viModel.videoCoverUrl = result.coverImageUrl;
                viModel.sdUrl = result.sdvideoUrl;
                viModel.hdUrl = result.hdvideoUrl;
                viModel.isLike = !TextUtils.equals(result.isLiked, "0");
                viModel.videoName = result.videoName;
                viModel.shortName = result.shortName;
                viModel.content = result.videoSummary;
                viModel.likeNum = Integer.parseInt(result.likes);
                viModel.finalPlayTime = System.currentTimeMillis();
                viModel.hasRecord = VideoStatus.RECORD;
                viModel.encrypt = 1;
                viModel.isDelete = VideoStatus.UNDELETE;
                if (isInitData) {
                    viModel.status = VideoStatus.NONE;
                }

                updataLocalVideoInfo();

                getView().getNetVideoInfoSucc(viModel);

            }

            @Override
            protected void onRxError(Throwable error) {

            }
        }));
    }

    @Override
    public void updataNowPlayTime(int playTime) {
        if (viModel == null) {
            return;
        }
        viModel.currentTime = playTime;
        updataLocalVideoInfo();
    }

    @Override
    public void updataDownloadType(int type) {
        if (viModel == null)
            return;
        viModel.downloadtype = type;
        updataLocalVideoInfo();
    }

    @Override
    public void updataFinalWatchTime() {
        if (viModel == null)
            return;
        viModel.finalPlayTime = System.currentTimeMillis();
        updataLocalVideoInfo();
    }

    @Override
    public void toVideoLike() {
        if (viModel.isLike) {
            return;
        }
        addSubscription(ApiClient.toTestVideoLike(viModel.videoId).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                VideoLikeEntity.Result result = new Gson().fromJson(s, VideoLikeEntity.Result.class);
                if (TextUtils.equals(result.results, "ok")) {
                    viModel.isLike = !viModel.isLike;
                    if (viModel.isLike) {
                        viModel.likeNum += 1;
                    } else {
                        viModel.likeNum -= 1;
                        if (viModel.likeNum < 0) {
                            viModel.likeNum = 0;
                        }
                    }
                    updataLocalVideoInfo();
                    getView().toVideoLikeSucc(viModel.isLike ? R.drawable.ic_like_down : R.drawable.ic_like_up, viModel.likeNum);
                }
            }

            @Override
            protected void onRxError(Throwable error) {

            }
        }));
    }

    @Override
    public long getCacheVideoNum() {
        return daoUtils.getCacheVideoNum();
    }

    @Override
    public VideoInfoModel getVideoInfo(String videoId) {
        return daoUtils.getVideoInfoModel(videoId);
    }


    /**
     * 获取本地数据
     *
     * @param videoId
     * @return
     */
    private void getLocalVideoInfo(String videoId) {
        viModel = daoUtils.getVideoInfoModel(videoId);
    }

    private DownloadManager getDownloadManager() {
        return downloadManager;
    }

    /**
     * 保存到本地
     */
    private void updataLocalVideoInfo() {
        if (viModel == null || TextUtils.isEmpty(viModel.videoId) || TextUtils.isEmpty(viModel.sdUrl)) {
            return;
        }
        try {
            daoUtils.saveOrUpdateVideoInfo(viModel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void detachView() {
        super.detachView();
        if (daoUtils != null) {
            daoUtils.destory();
            daoUtils = null;
        }
        if (videoDownloadCallback != null) {
            videoDownloadCallback.destory();
            videoDownloadCallback = null;
        }
    }
}
