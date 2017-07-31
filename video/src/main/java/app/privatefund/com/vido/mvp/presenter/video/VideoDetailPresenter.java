package app.privatefund.com.vido.mvp.presenter.video;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.R;
import com.cgbsoft.lib.base.model.VideoInfoEntity;
import com.cgbsoft.lib.base.model.VideoLikeEntity;
import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.mvp.model.video.VideoInfoModel;
import com.cgbsoft.lib.utils.cache.CacheManager;
import com.cgbsoft.lib.utils.constant.VideoStatus;
import com.cgbsoft.lib.utils.db.DaoUtils;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.utils.tools.LogUtils;
import com.cgbsoft.lib.utils.tools.NetUtils;
import com.cgbsoft.lib.utils.tools.PromptManager;
import com.cgbsoft.lib.widget.dialog.LoadingDialog;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okserver.download.DownloadInfo;
import com.lzy.okserver.download.DownloadManager;
import com.lzy.okserver.download.DownloadService;
import com.lzy.okserver.listener.DownloadListener;

import java.util.List;

import app.privatefund.com.vido.mvp.contract.video.VideoDetailContract;

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

    public VideoDetailPresenter(@NonNull Context context, @NonNull VideoDetailContract.View view) {
        super(context, view);
        daoUtils = new DaoUtils(context, DaoUtils.W_VIDEO);
        downloadManager = DownloadService.getDownloadManager();
        downloadManager.getThreadPool().setCorePoolSize(1);
        downloadManager.setTargetFolder(CacheManager.getCachePath(context, CacheManager.VIDEO));
    }


    public void getLocalVideoDetailInfo(LoadingDialog loadingDialog, String videoId) {
        getVideoDetailInfo(loadingDialog, videoId);
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
        GetRequest getRequest = OkGo.get(videoUrl);
        if (info == null) {
            getDownloadManager().addTask(videoId, getRequest, new VideoDownloadCallback(videoId));
        } else {
            switch (info.getState()) {
                case DownloadManager.PAUSE:
                case DownloadManager.NONE:
                case DownloadManager.ERROR:
                    getDownloadManager().addTask(videoId, getRequest, new VideoDownloadCallback(videoId));
                    break;
            }
        }
    }

    @Override
    public void getVideoDetailInfo(LoadingDialog loadingDialog, String videoId) {
        getLocalVideoInfo(videoId);

        if (viModel != null) {
            getView().getLocalVideoInfoSucc(viModel);
        } else {
            viModel = new VideoInfoModel();
            isInitData = true;
        }
        loadingDialog.show();
        addSubscription(ApiClient.getTestVideoInfo(videoId).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                loadingDialog.dismiss();
                VideoInfoEntity.Result result = new Gson().fromJson(getV2String(s), VideoInfoEntity.Result.class);

                viModel.videoId = result.videoId;
                viModel.videoCoverUrl = result.rows.coverImageUrl;
                viModel.sdUrl = result.rows.SDVideoUrl;
                viModel.hdUrl = result.rows.HDVideoUrl;
                viModel.isLike = !TextUtils.equals(result.rows.isLiked, "0");
                viModel.videoName = result.rows.videoName;
                viModel.shortName = result.rows.shortName;
                viModel.content = result.rows.videoSummary;
                viModel.categoryName = result.rows.categoryName;
                viModel.likeNum = Integer.parseInt(result.rows.likes);
                viModel.finalPlayTime = System.currentTimeMillis();
                viModel.hasRecord = VideoStatus.RECORD;
                viModel.encrypt = 1;
                viModel.isDelete = VideoStatus.UNDELETE;
                viModel.lecturerRemark=result.rows.lecturerRemark;
                if (isInitData) {
                    viModel.status = VideoStatus.NONE;
                }

                updataLocalVideoInfo();

                getView().getNetVideoInfoSucc(viModel, result);

            }

            @Override
            protected void onRxError(Throwable error) {
                loadingDialog.dismiss();
               getView().getNetVideoInfoErr(error.getMessage());
                LogUtils.Log("s", error.toString());
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
//        //todo 测试使用
//        viModel.finalPlayTime = 1499669773L;
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

    @Override
    public void bindDownloadCallback(String videoId) {
        List<DownloadInfo> list = downloadManager.getAllTask();
        DownloadInfo downloadInfo = null;
        for (DownloadInfo info : list) {
            if (info.getState() == DownloadManager.DOWNLOADING) {
                if (TextUtils.equals(info.getTaskKey(), videoId)) {
                    downloadInfo = info;
                    break;
                }
            }
        }
        if (downloadInfo != null)
            downloadInfo.setListener(new VideoDownloadCallback(videoId));
    }

    //添加评论
    @Override
    public void addCommont(String commontStr, String vdieoId) {
        if(!NetUtils.isNetworkAvailable(getContext())){

        }
        PromptManager.ShowCustomToast(getContext(),"sss");
        addSubscription(ApiClient.videoCommentAdd(commontStr, AppManager.getUserId(getContext()), vdieoId).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                getView().addCommontSucc(getV2String(s));

            }

            @Override
            protected void onRxError(Throwable error) {
                LogUtils.Log("addcommont", error.toString());

            }
        }));
    }

    //获取更多评论接口
    @Override
    public void getMoreCommont(String voideoId, String CommontId) {

        addSubscription(ApiClient.videoCommentLs(voideoId, CommontId).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                getView().getMoreCommontSucc(s);
            }

            @Override
            protected void onRxError(Throwable error) {
                getView().getMoreCommontSucc(null);
            }
        }));
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

    private String isHasDownloading() {
        if (getDownloadManager() == null)
            return null;
        List<DownloadInfo> list = getDownloadManager().getAllTask();
        for (DownloadInfo info : list) {
            if (info.getState() == DownloadManager.DOWNLOADING) {
                return info.getTaskKey();
            }
        }
        return null;
    }

    private class VideoDownloadCallback extends DownloadListener {
        private VideoDownloadCallback(String videoId) {
            if (viModel == null)
                viModel = getVideoInfo(videoId);
        }

        @Override
        public void onAdd(DownloadInfo downloadInfo) {
            String videoId = isHasDownloading();
            if (TextUtils.equals(videoId, downloadInfo.getTaskKey())) {
                viModel.status = VideoStatus.DOWNLOADING;
            } else {
                viModel.status = VideoStatus.WAIT;
            }
            viModel.downloadTime = System.currentTimeMillis();
            updataLocalVideoInfo();

            if (getView() != null)
                getView().onDownloadVideoAdd();
        }

        @Override
        public void onProgress(DownloadInfo downloadInfo) {
            long totalSize = downloadInfo.getTotalLength();
            float progress = downloadInfo.getProgress();

            viModel.percent = progress;
            viModel.size = totalSize;
            if (downloadInfo.getState() == DownloadManager.DOWNLOADING) {
                viModel.status = VideoStatus.DOWNLOADING;
            } else if (downloadInfo.getState() == DownloadManager.PAUSE || downloadInfo.getState() == DownloadManager.NONE) {
                viModel.status = VideoStatus.NONE;
            }
            updataLocalVideoInfo();
        }

        @Override
        public void onFinish(DownloadInfo downloadInfo) {
            viModel.status = VideoStatus.FINISH;
            viModel.localVideoPath = downloadInfo.getTargetPath();


            updataLocalVideoInfo();

            getView().onDownloadFinish(viModel);
        }

        @Override
        public void onError(DownloadInfo downloadInfo, String errorMsg, Exception e) {
            viModel.status = VideoStatus.NONE;
            updataLocalVideoInfo();
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
