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
import com.cgbsoft.lib.utils.db.DaoUtils;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.google.gson.Gson;

/**
 * Created by xiaoyu.zhang on 2016/12/7 18:09
 * Email:zhangxyfs@126.com
 *  
 */
public class VideoDetailPresenter extends BasePresenterImpl<VideoDetailContract.View> implements VideoDetailContract.Presenter {
    private DaoUtils daoUtils;
    private VideoInfoModel viModel;
    private boolean isInitData;

    public VideoDetailPresenter(@NonNull Context context, @NonNull VideoDetailContract.View view) {
        super(context, view);
        daoUtils = new DaoUtils(context, DaoUtils.W_VIDEO);
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
                viModel.hasRecord = 1;
                if (isInitData) {
                    viModel.status = 1;
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

    /**
     * 获取本地数据
     *
     * @param videoId
     * @return
     */
    private void getLocalVideoInfo(String videoId) {
        viModel = daoUtils.getVideoInfoModel(videoId);
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
    }
}
