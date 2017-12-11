package app.privatefund.com.vido.mvp.contract.video;

import com.cgbsoft.lib.base.model.VideoInfoEntity;
import com.cgbsoft.lib.base.mvp.presenter.BasePresenter;
import com.cgbsoft.lib.base.mvp.view.BaseView;
import com.cgbsoft.lib.mvp.model.video.VideoInfoModel;
import com.cgbsoft.lib.widget.dialog.LoadingDialog;


/**
 * Created by xiaoyu.zhang on 2016/12/7 18:08
 * Email:zhangxyfs@126.com
 *  
 */
public interface VideoDetailContract {
    interface Presenter extends BasePresenter {
        /**
         * 获取视频信息
         *
         * @param videoId
         */
        void getVideoDetailInfo(LoadingDialog loadingDialog, String videoId);

        /**
         * 保存播放进度
         *
         * @param playTime
         */
        void updataNowPlayTime(int playTime);

        /**
         * 保存下载视频清晰度
         *
         * @param type
         */
        void updataDownloadType(int type);

        /**
         * 更新最后观看时间
         */
        void updataFinalWatchTime();

        /**
         * 视频点赞
         */
        void toVideoLike();

        /**
         * 获取缓存视频数量
         *
         * @return
         */
        long getCacheVideoNum();

        /**
         * 获取本地视频数据
         *
         * @param videoId
         * @return
         */
        VideoInfoModel getVideoInfo(String videoId);

        /**
         * 添加到下载队列里
         *
         * @param videoId
         */
        void toDownload(String videoId);

        /**
         * 绑定下载监听
         *
         * @param videoId
         */
        void bindDownloadCallback(String videoId);
        /**
         * 添加评论
         */
        void addCommont(String commontStr,String vdieoId);
        /**
         * 获取更多评论
         */
        void getMoreCommont(String voideoId,String CommontId );

        /**
         * 判断视频校验结果
         */
        void addressValidateResult(boolean refreshPage);
    }

    interface View extends BaseView {
        void getLocalVideoInfoSucc(VideoInfoModel model);

        void getNetVideoInfoSucc(VideoInfoModel model, VideoInfoEntity.Result result);
        void getNetVideoInfoErr(String str);

        void toVideoLikeSucc(int likeRes, int likeNum);

        void onDownloadFinish(VideoInfoModel model);

        void onDownloadVideoAdd();

        void addCommontSucc(String commontsucc);

        void getMoreCommontSucc(String moreCommontStr);

        void setAddressValidateResult(String result, boolean refreshPage);
    }
}
