package com.cgbsoft.lib.utils.listener;

import android.text.TextUtils;

import com.cgbsoft.lib.Appli;
import com.cgbsoft.lib.mvp.model.VideoInfoModel;
import com.cgbsoft.lib.utils.constant.VideoStatus;
import com.cgbsoft.lib.utils.db.DaoUtils;
import com.lzy.okserver.download.DownloadInfo;
import com.lzy.okserver.download.DownloadManager;
import com.lzy.okserver.download.DownloadService;
import com.lzy.okserver.listener.DownloadListener;

import java.util.List;

/**
 * Created by xiaoyu.zhang on 2016/12/15 17:03
 * Email:zhangxyfs@126.com
 *  
 */
public class VideoDownloadCallback extends DownloadListener {
    private VideoInfoModel videoInfoModel;
    private DownloadManager downloadManager;
    private DaoUtils daoUtils;

    public VideoDownloadCallback(String videoId) {
        downloadManager = DownloadService.getDownloadManager();
        daoUtils = new DaoUtils(Appli.getContext(), DaoUtils.W_VIDEO);
        videoInfoModel = daoUtils.getVideoInfoModel(videoId);
    }

    public void destory() {
        daoUtils = null;
        downloadManager = null;
        videoInfoModel = null;
    }

    @Override
    public void onAdd(DownloadInfo downloadInfo) {
        String videoId = isHasDownloading();
        if (TextUtils.equals(videoId, downloadInfo.getTaskKey())) {
            videoInfoModel.status = VideoStatus.DOWNLOADING;
        } else {
            videoInfoModel.status = VideoStatus.WAIT;
        }
        videoInfoModel.downloadTime = System.currentTimeMillis();
        saveOrUpdateVideoInfo(videoInfoModel);
    }

    @Override
    public void onProgress(DownloadInfo downloadInfo) {
        long totalSize = downloadInfo.getTotalLength();
        float progress = downloadInfo.getProgress();

        if (videoInfoModel == null)
            return;

        videoInfoModel.percent = progress;
        videoInfoModel.size = totalSize;
        saveOrUpdateVideoInfo(videoInfoModel);
    }

    @Override
    public void onFinish(DownloadInfo downloadInfo) {
        if (videoInfoModel == null)
            return;

        videoInfoModel.status = VideoStatus.FINISH;
        videoInfoModel.localVideoPath = downloadInfo.getTargetPath();
        saveOrUpdateVideoInfo(videoInfoModel);
    }

    @Override
    public void onError(DownloadInfo downloadInfo, String errorMsg, Exception e) {
        if (videoInfoModel == null)
            return;

        videoInfoModel.status = VideoStatus.NONE;
        saveOrUpdateVideoInfo(videoInfoModel);
    }

    private String isHasDownloading() {
        if (downloadManager == null)
            return null;
        List<DownloadInfo> list = downloadManager.getAllTask();
        for (DownloadInfo info : list) {
            if (info.getState() == DownloadManager.DOWNLOADING) {
                return info.getTaskKey();
            }
        }
        return null;
    }

    private void saveOrUpdateVideoInfo(VideoInfoModel videoInfoModel) {
        if (daoUtils != null)
            daoUtils.saveOrUpdateVideoInfo(videoInfoModel);
    }
}
