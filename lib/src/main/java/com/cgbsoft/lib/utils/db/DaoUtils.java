package com.cgbsoft.lib.utils.db;

import android.content.Context;
import android.support.annotation.NonNull;

import com.cgbsoft.lib.Appli;
import com.cgbsoft.lib.base.model.bean.OtherInfo;
import com.cgbsoft.lib.base.model.bean.VideoInfo;
import com.cgbsoft.lib.mvp.model.VideoInfoModel;
import com.cgbsoft.lib.utils.db.dao.OtherInfoDao;
import com.cgbsoft.lib.utils.db.dao.UserInfoDao;
import com.cgbsoft.lib.utils.db.dao.VideoInfoDao;

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

    public static final int W_OTHER = 1;
    public static final int W_USER = 2;
    public static final int W_VIDEO = 3;

    public DaoUtils(Context context, int which) {
        switch (which) {
            case W_OTHER:
                otherInfoDao = ((Appli) context.getApplicationContext()).getDaoSession().getOtherInfoDao();
                break;
            case W_USER:
                userInfoDao = ((Appli) context.getApplicationContext()).getDaoSession().getUserInfoDao();
                break;
            case W_VIDEO:
                videoInfoDao = ((Appli) context.getApplicationContext()).getDaoSession().getVideoInfoDao();
                break;
        }
    }

    public OtherInfo getOtherInfo(String title) {
        return otherInfoDao.queryBuilder().where(OtherInfoDao.Properties.Title.eq(title)).build().unique();
    }

    public void deleteOther(String title) {
        OtherInfo otherInfo = getOtherInfo(title);
        if (otherInfo != null) {
            otherInfoDao.delete(otherInfo);
        }
    }

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
     * 查找所有的缓存视频
     *
     * @return
     */
    public List<VideoInfoModel> getAllVideoInfo() {
        //根据最后播放时间倒叙排列
        List<VideoInfo> list = videoInfoDao.queryBuilder().orderDesc(VideoInfoDao.Properties.FinalPlayTime).build().list();
        List<VideoInfoModel> results = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            results.add(getVideoInfoModel(list.get(i)));
        }
        return results;
    }

    /**
     * 已经下载完成的视频数量
     * @return
     */
    public long getCacheVideoNum(){
        return videoInfoDao.queryBuilder().where(VideoInfoDao.Properties.Status.eq(2)).buildCount().count();
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
        return model;
    }


    public void destory() {
        otherInfoDao = null;
        userInfoDao = null;
        videoInfoDao = null;
    }

}
