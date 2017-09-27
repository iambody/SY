package com.cgbsoft.lib.utils.db;

import android.content.Context;
import android.support.annotation.NonNull;

import com.cgbsoft.lib.BaseApplication;
import com.cgbsoft.lib.base.model.bean.DataStatisticsBean;
import com.cgbsoft.lib.base.model.bean.OtherInfo;
import com.cgbsoft.lib.base.model.bean.VideoInfo;
import com.cgbsoft.lib.mvp.model.video.VideoInfoModel;
import com.cgbsoft.lib.utils.constant.VideoStatus;
import com.cgbsoft.lib.utils.db.dao.DataStatisticsBeanDao;
import com.cgbsoft.lib.utils.db.dao.HistorySearchBeanDao;
import com.cgbsoft.lib.utils.db.dao.OtherInfoDao;
import com.cgbsoft.lib.utils.db.dao.UserInfoDao;
import com.cgbsoft.lib.utils.db.dao.VideoInfoDao;
import com.cgbsoft.privatefund.bean.product.HistorySearchBean;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据库工具
 * Created by xiaoyu.zhang on 2016/11/24 14:15
 * Email:zhangxyfs@126.com
 *  
 */
public class DaoUtils {
    private OtherInfoDao otherInfoDao;
    private UserInfoDao userInfoDao;
    private VideoInfoDao videoInfoDao;
    private HistorySearchBeanDao historySearchBeanDao;
    private DataStatisticsBeanDao dataStatisticsBeanDao;

    public static final int W_OTHER = 1;
    public static final int W_USER = 2;
    public static final int W_VIDEO = 3;
    //搜索历史的数据库
    public static final int W_SousouHistory = 101;

    public static final int W_TASK = 4;
    public static final int W_DATASTISTICS = 5;

    public DaoUtils(Context context, int which) {
        switch (which) {
            case W_OTHER:
                otherInfoDao = ((BaseApplication) context.getApplicationContext()).getDaoSession().getOtherInfoDao();
                break;
            case W_USER:
                userInfoDao = ((BaseApplication) context.getApplicationContext()).getDaoSession().getUserInfoDao();
                break;
            case W_VIDEO:
                videoInfoDao = ((BaseApplication) context.getApplicationContext()).getDaoSession().getVideoInfoDao();
                break;
            case W_SousouHistory:
                historySearchBeanDao = ((BaseApplication) context.getApplicationContext()).getDaoSession().getHistorySearchBeanDao();
                break;
            case W_DATASTISTICS:
                dataStatisticsBeanDao = ((BaseApplication) context.getApplicationContext()).getDaoSession().getDataStatisticsBeanDao();
        }
    }

    /**
     * 获取搜索历史的全部数据
     *
     * @return
     */
    public List<HistorySearchBean> getHistoryLs() {
        if (null == historySearchBeanDao) return new ArrayList<>();

        return historySearchBeanDao.queryBuilder().list();
    }

    /**
     * 清除历史搜索记录
     */
    public void clearnHistorySearch() {
        if (null != historySearchBeanDao)
            historySearchBeanDao.deleteAll();

    }

    public void clearnHistoryByID(String Type, String userId) {
        List<HistorySearchBean> been = new ArrayList<>();
        been = getHistorysByType(Type, userId);
        if (null == been || been.size() == 0) return;
        for (int i = 0; i < been.size(); i++) {
            historySearchBeanDao.delete(been.get(i));
        }
    }

    /**
     * 插入一条
     *
     * @param historySearchBean
     */
    public void insertHistorySearch(HistorySearchBean historySearchBean) {
        if (null == historySearchBeanDao) return;
        if (historySearchBeanDao.queryBuilder().where(HistorySearchBeanDao.Properties.Name.eq(historySearchBean.getName())).buildCount().count() > 0) {
            historySearchBeanDao.update(historySearchBean);
            return;
        }
        historySearchBean.set_id(getHistoryLs().size());
        historySearchBeanDao.insert(historySearchBean);
    }

    /**
     * 根据type和用户id获取搜搜列表
     *
     * @param Type
     * @param userId
     */
    public List<HistorySearchBean> getHistorysByType(String Type, String userId) {
        List<HistorySearchBean> been = new ArrayList<>();
        if (null == historySearchBeanDao) return new ArrayList<>();
        been = historySearchBeanDao.queryBuilder().where(HistorySearchBeanDao.Properties.Type.eq(Type), HistorySearchBeanDao.Properties.UserId.eq(userId)).build().list();
        if (null == been) return new ArrayList<>();
        else return been;
    }

    /**
     * 获取在欢迎页保存的首页背景图片应用升级信息
     * @param title
     * @return
     */
    public OtherInfo getOtherInfo(String title) {
        return otherInfoDao.queryBuilder().where(OtherInfoDao.Properties.Title.eq(title)).build().unique();
    }

    public void deleteOther(String title) {
        OtherInfo otherInfo = getOtherInfo(title);
        if (otherInfo != null) {
            otherInfoDao.delete(otherInfo);
        }
    }

    /**
     * 在欢迎页获取首页背景图片，应用升级信息，然后保存
     * @param title
     * @param value
     */
    public void saveOrUpdataOther(String title, String value) {
        OtherInfo info = getOtherInfo(title);
        if (info != null) {
            info.setContent(value);
            otherInfoDao.update(info);
        } else
            otherInfoDao.insert(new OtherInfo(null, title, value));
    }


    /**
     * 获取
     *
     * @param videoId
     * @return
     */
    public VideoInfoModel getVideoInfoModel(String videoId) {
        VideoInfo videoInfo = getVideoInfo(videoId);
        if (videoInfo == null) {
            return null;
        }
        return getVideoInfoModel(videoInfo);
    }

    private VideoInfo getVideoInfo(String videoId) {
        return videoInfoDao.queryBuilder().where(VideoInfoDao.Properties.VideoId.eq(videoId)).build().unique();
    }

    /**
     * 获取下载记录
     */
    /**
     * 查找所有的缓存视频
     *
     * @return
     */
    public List<VideoInfoModel> getDownLoadVideoInfo() {
//        //根据最后播放时间倒叙排列
        List<VideoInfo> list = videoInfoDao.queryBuilder().where(VideoInfoDao.Properties.IsDelete.eq(VideoStatus.UNDELETE)).orderAsc(VideoInfoDao.Properties.Status).build().list();
        List<VideoInfoModel> results = new ArrayList<>();
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                if (0 != list.get(i).getDownloadTime() && list.get(i).getStatus() != VideoStatus.NONE)
                    results.add(getVideoInfoModel(list.get(i)));
            }
            return results;
        }
        return null;
//        //根据最后播放时间倒叙排列
//        List<VideoInfo> list = videoInfoDao.queryBuilder().where(VideoInfoDao.Properties.IsDelete.eq(VideoStatus.UNDELETE),VideoInfoDao.Properties.Status.notEq(VideoStatus.NONE)).orderAsc(VideoInfoDao.Properties.Status).build().list();
//        List<VideoInfoModel> results = new ArrayList<>();
//        if (list != null) {
//            for (int i = 0; i < list.size(); i++) {
//                results.add(getVideoInfoModel(list.get(i)));
//            }
//            return results;
//        }
//        return null;
    }

    /**
     * 查找所有的缓存视频
     *
     * @return
     */
    public List<VideoInfoModel> getAllVideoInfo() {
        //根据最后播放时间倒叙排列
        List<VideoInfo> list = videoInfoDao.queryBuilder().where(VideoInfoDao.Properties.IsDelete.eq(VideoStatus.UNDELETE)).orderAsc(VideoInfoDao.Properties.Status).build().list();
        List<VideoInfoModel> results = new ArrayList<>();
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                if (0 != list.get(i).getDownloadTime())
                    results.add(getVideoInfoModel(list.get(i)));
            }
            return results;
        }
        return null;
    }

    /**
     * 查找所有的播放历史视频
     *
     * @return
     */
    public List<VideoInfoModel> getAllVideoInfoHistory() {
        List<VideoInfo> list = videoInfoDao.queryBuilder().where(VideoInfoDao.Properties.HasRecord.eq(VideoStatus.RECORD)).orderDesc(VideoInfoDao.Properties.FinalPlayTime).build().list();
        List<VideoInfoModel> results = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            results.add(getVideoInfoModel(list.get(i)));
        }
        return results;
    }

    /**
     * 播放历史是否显示
     *
     * @param videoId
     */
    public void deleteVideoInfoHistory(String videoId) {
        VideoInfo videoInfo = videoInfoDao.queryBuilder().where(VideoInfoDao.Properties.VideoId.eq(videoId)).build().unique();
        if (videoInfo != null) {
            videoInfo.setHasRecord(VideoStatus.UNRECORD);
            videoInfoDao.update(videoInfo);
        }
    }

    /**
     * 删除视频
     *
     * @param videoId
     */
    public void deleteVideoInfo(String videoId) {
        VideoInfo videoInfo = videoInfoDao.queryBuilder().where(VideoInfoDao.Properties.VideoId.eq(videoId)).build().unique();

        String localPath = null;
        if (videoInfo != null) {
            localPath = videoInfo.getLocalVideoPath();
            videoInfo.setIsDelete(VideoStatus.DELETE);
            videoInfo.setStatus(VideoStatus.NONE);
            videoInfo.setFinalPlayTime(0);
            videoInfo.setSize(0);
            videoInfo.setLocalVideoPath("");
            videoInfo.setPercent(0);
            videoInfo.setDownloadtype(-1);
            videoInfo.setDownloadTime(0);
            videoInfoDao.update(videoInfo);
        }
        if (localPath != null) {
            File file = new File(localPath);
            if (file.isFile() && file.exists()) {
                file.delete();
            }
        }
    }

    /**
     * 视频下载完成
     */

    public void videoDoneLoad(String videoId,String path) {
        VideoInfo videoInfo = videoInfoDao.queryBuilder().where(VideoInfoDao.Properties.VideoId.eq(videoId)).build().unique();
        videoInfo.setStatus(VideoStatus.FINISH);
        videoInfo.setLocalVideoPath(path);
        videoInfoDao.update(videoInfo);


    }

    /**
     * 除了没下载以外的视频数量
     *
     * @return
     */
    public long getCacheVideoNum() {
        return videoInfoDao.queryBuilder().where(VideoInfoDao.Properties.Status.notEq(VideoStatus.NONE), VideoInfoDao.Properties.IsDelete.eq(VideoStatus.UNDELETE)).buildCount().count();
    }

    /**
     * 保存视频信息
     *
     * @param model
     */
    public void saveOrUpdateVideoInfo(@NonNull VideoInfoModel model) {
        VideoInfo videoInfo = getVideoInfo(model.videoId);

        if (videoInfo != null) {
            videoInfoDao.update(getVideoInfo(videoInfo, model));
        } else
            videoInfoDao.insert(getVideoInfo(model));
    }


    private VideoInfo getVideoInfo(VideoInfo videoInfo, VideoInfoModel model) {
        videoInfo.setCurrentTime(model.currentTime);
        videoInfo.setContent(model.content);
        videoInfo.setDownloadTime(model.downloadTime);
        videoInfo.setDownloadtype(model.downloadtype);
        videoInfo.setEncrypt(model.encrypt);
        videoInfo.setFinalPlayTime(model.finalPlayTime);
        videoInfo.setHasRecord(model.hasRecord);
        videoInfo.setHdUrl(model.hdUrl);
        videoInfo.setIsLike(model.isLike);
        videoInfo.setLikeNum(model.likeNum);
        videoInfo.setLocalVideoPath(model.localVideoPath);
        videoInfo.setPercent(model.percent);
        videoInfo.setDownloadTime(model.downloadTime);
        videoInfo.setSdUrl(model.sdUrl);
        videoInfo.setShortName(model.shortName);
        videoInfo.setSize(model.size);
        videoInfo.setStatus(model.status);
        videoInfo.setVideoCoverUrl(model.videoCoverUrl);
        videoInfo.setVideoName(model.videoName);
        videoInfo.setVideoId(model.videoId);
        videoInfo.setIsDelete(model.isDelete);
        return videoInfo;
    }

    private VideoInfo getVideoInfo(VideoInfoModel model) {
        VideoInfo videoInfo = new VideoInfo();
        videoInfo.setCurrentTime(model.currentTime);
        videoInfo.setContent(model.content);
        videoInfo.setDownloadTime(model.downloadTime);
        videoInfo.setDownloadtype(model.downloadtype);
        videoInfo.setEncrypt(model.encrypt);
        videoInfo.setFinalPlayTime(model.finalPlayTime);
        videoInfo.setHasRecord(model.hasRecord);
        videoInfo.setHdUrl(model.hdUrl);
        videoInfo.setIsLike(model.isLike);
        videoInfo.setLikeNum(model.likeNum);
        videoInfo.setLocalVideoPath(model.localVideoPath);
        videoInfo.setPercent(model.percent);
        videoInfo.setDownloadTime(model.downloadTime);
        videoInfo.setSdUrl(model.sdUrl);
        videoInfo.setShortName(model.shortName);
        videoInfo.setSize(model.size);
        videoInfo.setStatus(model.status);
        videoInfo.setVideoCoverUrl(model.videoCoverUrl);
        videoInfo.setVideoName(model.videoName);
        videoInfo.setVideoId(model.videoId);
        videoInfo.setIsDelete(model.isDelete);
        return videoInfo;
    }

    private VideoInfoModel getVideoInfoModel(VideoInfo videoInfo) {
        VideoInfoModel model = new VideoInfoModel();
        model.currentTime = videoInfo.getCurrentTime();
        model.content = videoInfo.getContent();
        model.videoName = videoInfo.getVideoName();
        model.likeNum = videoInfo.getLikeNum();
        model.downloadTime = videoInfo.getDownloadTime();
        model.downloadtype = videoInfo.getDownloadtype();
        model.encrypt = videoInfo.getEncrypt();
        model.finalPlayTime = videoInfo.getFinalPlayTime();
        model.hasRecord = videoInfo.getHasRecord();
        model.hdUrl = videoInfo.getHdUrl();
        model.isLike = videoInfo.getIsLike();
        model.localVideoPath = videoInfo.getLocalVideoPath();
        model.percent = videoInfo.getPercent();
        model.sdUrl = videoInfo.getSdUrl();
        model.shortName = videoInfo.getShortName();
        model.size = videoInfo.getSize();
        model.status = videoInfo.getStatus();
        model.videoCoverUrl = videoInfo.getVideoCoverUrl();
        model.videoId = videoInfo.getVideoId();
        model.isDelete = videoInfo.getIsDelete();
        return model;
    }

    public List<DataStatisticsBean> getDatastisticList() {
        return dataStatisticsBeanDao.queryBuilder().list();
    }

    public void deleteDataStatitic() {
        dataStatisticsBeanDao.deleteAll();
    }

    public void saveDataStatistic(DataStatisticsBean dataStatisticsBean) {
        dataStatisticsBeanDao.save(dataStatisticsBean);
    }

    public void destory() {
        otherInfoDao = null;
        userInfoDao = null;
        videoInfoDao = null;
        dataStatisticsBeanDao = null;
    }

}
