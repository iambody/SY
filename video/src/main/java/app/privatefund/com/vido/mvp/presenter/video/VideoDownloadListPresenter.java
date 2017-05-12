package app.privatefund.com.vido.mvp.presenter.video;

import android.content.Context;
import android.os.StatFs;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.cgbsoft.lib.R;
import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.mvp.contract.video.VideoDownloadListContract;
import com.cgbsoft.lib.mvp.model.video.VideoInfoModel;
import com.cgbsoft.lib.mvp.ui.video.model.VideoDownloadListModel;
import com.cgbsoft.lib.utils.cache.CacheManager;
import com.cgbsoft.lib.utils.constant.VideoStatus;
import com.cgbsoft.lib.utils.db.DaoUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okserver.download.DownloadInfo;
import com.lzy.okserver.download.DownloadManager;
import com.lzy.okserver.download.DownloadService;
import com.lzy.okserver.listener.DownloadListener;
import com.lzy.okserver.task.ExecutorWithListener;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaoyu.zhang on 2016/12/12 17:30
 * Email:zhangxyfs@126.com
 *  
 */
public class VideoDownloadListPresenter extends BasePresenterImpl<VideoDownloadListContract.View> implements VideoDownloadListContract.Presenter, ExecutorWithListener.OnAllTaskEndListener {
    private DaoUtils daoUtils;
    private DownloadManager downloadManager;

    public VideoDownloadListPresenter(@NonNull Context context, @NonNull VideoDownloadListContract.View view) {
        super(context, view);
        daoUtils = new DaoUtils(context, DaoUtils.W_VIDEO);
        downloadManager = DownloadService.getDownloadManager();
        downloadManager.getThreadPool().setCorePoolSize(1);
        downloadManager.setTargetFolder(CacheManager.getCachePath(context, CacheManager.VIDEO));
        downloadManager.getThreadPool().getExecutor().addOnAllTaskEndListener(this);
    }

    @Override
    public void getLocalDataList(boolean isRef) {
        List<VideoInfoModel> list = daoUtils.getAllVideoInfo();
        if (list != null) {
            List<VideoDownloadListModel> dataList = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                VideoInfoModel model = list.get(i);
                DownloadInfo info = getDownloadManager().getDownloadInfo(model.videoId);
                if (info != null && info.getState() == DownloadManager.FINISH && model.status != VideoStatus.FINISH) {
                    model.status = VideoStatus.FINISH;
                    saveOrUpdateVideoInfo(model);
                }
                dataList.add(createModel(model));
            }
            getView().getLocalListSucc(dataList, isRef);
        } else
            getView().getLocalListFail(isRef);

    }

    @Override
    public String getDownloadedFileSize(VideoInfoModel model) {
        DecimalFormat df = new DecimalFormat("#0.0");
        return df.format(model.size / 1024 / 1024) +
                "M/" +
                df.format(model.size / 1024 / 1024 * model.percent) +
                "M";
    }


    @Override
    public void delete(String videoId) {
        daoUtils.deleteVideoInfo(videoId);
        if (getDownloadManager() != null)
            getDownloadManager().removeTask(videoId);
    }

    @Override
    public VideoDownloadListModel getLocalVideoInfo(String videoId) {
        VideoInfoModel model = daoUtils.getVideoInfoModel(videoId);
        if (model == null)
            return null;
        return createModel(model);
    }


    @Override
    public String getSDCardSize() {
        String returnStr = null;
        String sDcString = android.os.Environment.getExternalStorageState();
        if (sDcString.equals(android.os.Environment.MEDIA_MOUNTED)) {
            File pathFile = android.os.Environment.getExternalStorageDirectory();
            StatFs statfs = new StatFs(pathFile.getPath());
            long blockSize = statfs.getBlockSize();                         //获取block的size
            float totalBlocks = statfs.getBlockCount();                     //获取block的总数
            float totalGbSize = blockSize * totalBlocks;
            DecimalFormat df = new DecimalFormat("#0.00");
            String totalGbSizeStr = df.format(totalGbSize);                //总共大小
            String[] totalStr = fileSize(totalGbSize);
            long availableBlocks = statfs.getAvailableBlocks();             //获取可用块大小
            String[] avaiStr = fileSize(availableBlocks * blockSize);

            returnStr = getContext().getString(R.string.avd_all_space_str) + totalStr[0] + totalStr[1] + "/"
                    + getContext().getString(R.string.avd_free_space_str) + avaiStr[0] + avaiStr[1];
        }
        return returnStr;
    }


    /**
     * 判断是否当前正在全部下载
     *
     * @param dataList
     * @return
     */
    @Override
    public boolean isStartAllDownloading(List<VideoDownloadListModel> dataList) {
        if (dataList.size() == 0) {
            return false;
        }
        List<String> adapterList = new ArrayList<>();
        List<String> taskList = new ArrayList<>();
        for (VideoDownloadListModel model : dataList) {
            if (model.status != VideoStatus.FINISH)
                adapterList.add(model.videoId);
        }
        for (DownloadInfo info : getAllDownloadTask()) {
            if (info.getState() == DownloadManager.DOWNLOADING)
                taskList.add(info.getTaskKey());
        }
        return taskList.containsAll(adapterList);
    }

    @Override
    public void startAllDownload(List<VideoDownloadListModel> dataList) {
        for (int i = 0; i < dataList.size(); i++) {
            if (dataList.get(i).status != VideoStatus.FINISH)
                startDownload(dataList.get(i));
        }
    }

    @Override
    public void startDownload(VideoDownloadListModel model) {
        if (getDownloadManager() == null)
            return;
        DownloadInfo info = getDownloadManager().getDownloadInfo(model.videoId);
        GetRequest getRequest = OkGo.get(model.videoUrl);
        if (info == null) {
            getDownloadManager().addTask(model.videoId, getRequest, new VideoDownloadCallback(model.videoId));
        } else {
            switch (info.getState()) {
                case DownloadManager.PAUSE:
                case DownloadManager.NONE:
                case DownloadManager.ERROR:
                    getDownloadManager().addTask(model.videoId, getRequest, new VideoDownloadCallback(model.videoId));
                case DownloadManager.DOWNLOADING:
                    info.setListener(new VideoDownloadCallback(model.videoId));
                    break;
            }
        }
    }

    @Override
    public void stopDownload(String videoId) {
        if (getDownloadManager() != null)
            getDownloadManager().stopTask(videoId);
    }

    @Override
    public void stopAllDownload() {
        if (getDownloadManager() == null)
            return;
        List<DownloadInfo> list = getAllDownloadTask();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getState() == DownloadManager.DOWNLOADING || list.get(i).getState() == DownloadManager.WAITING)
                getDownloadManager().stopTask(list.get(i).getTaskKey());
        }
    }

    @Override
    public void removeTask(String videoId) {
        if (getDownloadManager() == null || daoUtils == null)
            return;
        getDownloadManager().stopTask(videoId);
        getDownloadManager().removeTask(videoId);

        //将下载状态改为未下载
        VideoInfoModel model = daoUtils.getVideoInfoModel(videoId);
        if (model != null && model.status == VideoStatus.DOWNLOADING) {
            model.status = VideoStatus.NONE;
            daoUtils.saveOrUpdateVideoInfo(model);
        }
    }

    @Override
    public void bindDownloadCallback() {
        List<DownloadInfo> list = getAllDownloadTask();
        for (DownloadInfo info : list) {
            if (info.getState() == DownloadManager.DOWNLOADING || info.getState() == DownloadManager.WAITING) {
                info.setListener(new VideoDownloadCallback(info.getTaskKey()));
            }
        }
    }

    @Override
    public DownloadManager getDownloadManager() {
        return downloadManager;
    }

    @Override
    public List<DownloadInfo> getAllDownloadTask() {
        if (getDownloadManager() != null)
            return getDownloadManager().getAllTask();
        return new ArrayList<>();
    }

    @Override
    public String isHasDownloading() {
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

    public boolean isTaskWaiting(String videoId) {
        if (getDownloadManager() != null) {
            DownloadInfo info = getDownloadManager().getDownloadInfo(videoId);
            if (info != null && info.getState() == DownloadManager.WAITING) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onAllTaskEnd() {
        for (DownloadInfo downloadInfo : getAllDownloadTask()) {
            if (downloadInfo.getState() != DownloadManager.FINISH) {
                return;
            }
        }
        if (getView() != null)
            getView().allDownloadSucc();
    }


    private String[] fileSize(float size) {
        String str = "";
        if (size >= 1024) {
            str = "K";
            size /= 1024;
            if (size >= 1024) {
                str = "M";
                size /= 1024;
                if (size >= 1024) {
                    str = "G";
                    size /= 1024;
                }
            }
        }
        DecimalFormat formatter = new DecimalFormat("#0.00");
        formatter.setGroupingSize(3);
        String[] result = new String[2];
        result[0] = formatter.format(size);
        result[1] = str;
        return result;
    }

    private VideoDownloadListModel createModel(VideoInfoModel videoInfoModel) {
        VideoDownloadListModel model = new VideoDownloadListModel();
        model.type = VideoDownloadListModel.LIST;
        model.videoCoverUrl = videoInfoModel.videoCoverUrl;
        model.videoId = videoInfoModel.videoId;
        model.videoTitle = videoInfoModel.videoName;
        model.progressStr = getDownloadedFileSize(videoInfoModel);
        model.status = videoInfoModel.status;
        model.downloadTime = videoInfoModel.downloadTime;
        model.downloadtype = videoInfoModel.downloadtype;
        model.localPath = videoInfoModel.localVideoPath;
        model.max = 100;
        model.progress = (int) (videoInfoModel.percent * 100);

        if (videoInfoModel.downloadtype == VideoStatus.SD) {//标清
            model.videoUrl = videoInfoModel.sdUrl;
        } else if (videoInfoModel.downloadtype == VideoStatus.HD) {//高清
            model.videoUrl = videoInfoModel.hdUrl;
        }
        if (model.status != VideoStatus.FINISH) {
            DownloadInfo info = getDownloadManager().getDownloadInfo(model.videoId);
            if (info != null) {
                if (info.getState() == DownloadManager.DOWNLOADING) {
                    model.status = VideoStatus.DOWNLOADING;
                } else if (info.getState() == DownloadManager.WAITING) {
                    model.status = VideoStatus.WAIT;
                }
            }
        }
        return model;
    }

    @Override
    public void updateStatus(String videoId, int status) {
        if (daoUtils == null)
            return;
        VideoInfoModel model = daoUtils.getVideoInfoModel(videoId);
        if (model == null)
            return;
        model.status = status;
        daoUtils.saveOrUpdateVideoInfo(model);

    }

    private void saveOrUpdateVideoInfo(VideoInfoModel videoInfoModel) {
        if (daoUtils != null)
            daoUtils.saveOrUpdateVideoInfo(videoInfoModel);
    }

    private class VideoDownloadCallback extends DownloadListener {
        private String videoId;
        private VideoInfoModel videoInfoModel;

        public VideoDownloadCallback(String videoId) {
            this.videoId = videoId;
            videoInfoModel = daoUtils.getVideoInfoModel(videoId);
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
            long currentSize = downloadInfo.getDownloadLength();
            long totalSize = downloadInfo.getTotalLength();
            float progress = downloadInfo.getProgress();
            long networkSpeed = downloadInfo.getNetworkSpeed();

            videoInfoModel.percent = progress;
            videoInfoModel.size = totalSize;
            if (downloadInfo.getState() == DownloadManager.DOWNLOADING) {
                videoInfoModel.status = VideoStatus.DOWNLOADING;
            } else if (downloadInfo.getState() == DownloadManager.PAUSE || downloadInfo.getState() == DownloadManager.NONE) {
                videoInfoModel.status = VideoStatus.NONE;
            }
            saveOrUpdateVideoInfo(videoInfoModel);

            if (getView() != null)
                getView().onDownloadProgress(videoId, currentSize, totalSize, progress, networkSpeed, downloadInfo.getState());
        }

        @Override
        public void onFinish(DownloadInfo downloadInfo) {
            videoInfoModel.status = VideoStatus.FINISH;
            videoInfoModel.localVideoPath = downloadInfo.getTargetPath();
            saveOrUpdateVideoInfo(videoInfoModel);
            if (getView() != null)
                getView().onDownloadFinish(videoId);
        }

        @Override
        public void onError(DownloadInfo downloadInfo, String errorMsg, Exception e) {
            videoInfoModel.status = VideoStatus.NONE;
            saveOrUpdateVideoInfo(videoInfoModel);
            if (getView() != null)
                getView().onDownloadError(videoId);
        }
    }

    @Override
    public void detachView() {
        super.detachView();
        if (daoUtils != null) {
            daoUtils.destory();
            daoUtils = null;
        }
        if (downloadManager != null) {
            downloadManager.getThreadPool().getExecutor().removeOnAllTaskEndListener(this);
            downloadManager = null;
        }
    }

}
