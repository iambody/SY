package app.privatefund.com.vido.mvp.contract.video;

import com.cgbsoft.lib.base.mvp.presenter.BasePresenter;
import com.cgbsoft.lib.base.mvp.view.BaseView;
import com.cgbsoft.lib.mvp.model.video.VideoInfoModel;
import com.cgbsoft.lib.mvp.ui.video.model.VideoDownloadListModel;
import com.lzy.okserver.download.DownloadInfo;
import com.lzy.okserver.download.DownloadManager;

import java.util.List;

/**
 * Created by xiaoyu.zhang on 2016/12/12 17:31
 * Email:zhangxyfs@126.com
 *  
 */
public interface VideoDownloadListContract {

    interface Presenter extends BasePresenter {
        /**
         * 获取下载列表
         * @param isRef
         */
        void getLocalDataList(boolean isRef);

        /**
         * 删除下载任务
         * @param videoId
         */
        void delete(String videoId);

        /**
         * 下载进度转换
          * @param model
         * @return
         */
        String getDownloadedFileSize(VideoInfoModel model);

        /**
         * 获取当前的存储空间大小
         * @return
         */
        String getSDCardSize();

        /**
         * 判断当前是否处于所有任务开始下载
         * @param dataList
         * @return
         */
        boolean isStartAllDownloading(List<VideoDownloadListModel> dataList);

        /**
         * 开始所有的任务下载，同时进行的任务数量为1
         * @param dataList
         */
        void startAllDownload(List<VideoDownloadListModel> dataList);

        /**
         * 开始下载任务
         * @param model
         */
        void startDownload(VideoDownloadListModel model);

        /**
         * 如果当前有正在下载的任务，那么和监听器绑定
         */
        void bindDownloadCallback();

        /**
         * 获取下载管理器
         * @return
         */
        DownloadManager getDownloadManager();

        /**
         * 获取下载队列，不论是否有正在下载的任务
         * @return
         */
        List<DownloadInfo> getAllDownloadTask();

        /**
         * 是否有正在下载的数据
         * @return
         */
        String isHasDownloading();

        /**
         * 停止下载
         * @param videoId
         */
        void stopDownload(String videoId);

        /**
         * 从下载队列中移除
         * @param videoId
         */
        void removeTask(String videoId);

        /**
         * 获取数据库的数据
         * @param videoId
         * @return
         */
        VideoDownloadListModel getLocalVideoInfo(String videoId);

        /**
         * 保存状态
         * @param videoId
         * @param status
         */
        void updateStatus(String videoId, int status);

        /**
         * 停止所有下载
         */
        void stopAllDownload();

    }

    interface View extends BaseView {
        void getLocalListSucc(List<VideoDownloadListModel> dataList, boolean isRef);

        void getLocalListFail(boolean isRef);

        void onDownloadProgress(String videoId, long currentSize, long totalSize, float progress, long networkSpeed, int downloadState);

        void onDownloadFinish(String videoId);

        void onDownloadError(String videoId);

        void allDownloadSucc();
    }
}
