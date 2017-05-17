package com.cgbsoft.lib.utils.db;

import android.content.Context;
import android.support.annotation.NonNull;

import com.cgbsoft.lib.BaseApplication;
import com.cgbsoft.lib.base.model.bean.OtherInfo;
import com.cgbsoft.lib.base.model.bean.VideoInfo;
import com.cgbsoft.lib.mvp.model.video.VideoInfoModel;
import com.cgbsoft.lib.utils.constant.VideoStatus;
import com.cgbsoft.lib.utils.db.dao.DayTaskBeanDao;
import com.cgbsoft.lib.utils.db.dao.HistorySearchBeanDao;
import com.cgbsoft.lib.utils.db.dao.OtherInfoDao;
import com.cgbsoft.lib.utils.db.dao.UserInfoDao;
import com.cgbsoft.lib.utils.db.dao.VideoInfoDao;
import com.cgbsoft.privatefund.bean.commui.DayTaskBean;

import org.greenrobot.greendao.query.Query;
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
    private DayTaskBeanDao dayTaskDao;

    public static final int W_OTHER = 1;
    public static final int W_USER = 2;
    public static final int W_VIDEO = 3;
    //搜索历史的数据库
    public static final int W_SousouHistory = 101;

    public static final int W_TASK = 4;

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
            case W_TASK:
                dayTaskDao = ((BaseApplication) context.getApplicationContext()).getDaoSession().getDayTaskBeanDao();
                break;
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

    /**
     * 插入一条
     *
     * @param historySearchBean
     */
    public void insertHistorySearch(HistorySearchBean historySearchBean) {
        if (null == historySearchBeanDao) return;
        if (historySearchBeanDao.queryBuilder().where(HistorySearchBeanDao.Properties.Name.eq(historySearchBean.getName())).buildCount().count() > 0){
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
        if (null == historySearchBeanDao) return new ArrayList<>();
        return historySearchBeanDao.queryBuilder().where(HistorySearchBeanDao.Properties.Type.eq(Type), HistorySearchBeanDao.Properties.UserId.eq(userId)).build().list();

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
        List<VideoInfo> list = videoInfoDao.queryBuilder().where(VideoInfoDao.Properties.IsDelete.eq(VideoStatus.UNDELETE)).orderAsc(VideoInfoDao.Properties.Status).build().list();
        List<VideoInfoModel> results = new ArrayList<>();
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
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
        List<VideoInfo> list = videoInfoDao.queryBuilder().where(VideoInfoDao.Properties.HasRecord.eq(VideoStatus.RECORD), VideoInfoDao.Properties.IsDelete.eq(VideoStatus.UNDELETE)).orderDesc(VideoInfoDao.Properties.FinalPlayTime).build().list();
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
            videoInfo.setHasRecord(0);
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

    /**
     * 获取每日任务完成情况
     */
    public ArrayList<DayTaskBean> getDayTaskList(){
        return (ArrayList<DayTaskBean>) dayTaskDao.loadAll();
    }

    /**
     * 修改任务完成情况
     */
    public void updataDayTask(String taskName,int state){
        DayTaskBean dayTaskBean = dayTaskDao.queryBuilder().where(DayTaskBeanDao.Properties.TaskName.eq(taskName)).build().unique();
        dayTaskBean.setState(state);
        dayTaskDao.update(dayTaskBean);
    }

    /**
     * 获取任务状态
     */
    public int getDayTaskState(String taskName){
        DayTaskBean dayTaskBean = dayTaskDao.queryBuilder().where(DayTaskBeanDao.Properties.TaskName.eq(taskName)).build().unique();
        return dayTaskBean.getState();
    }

    /**
     * 清空任务
     */
    public void clearDayTask(){
        dayTaskDao.deleteAll();
    }

    /**
     * 获取任务最后更新时间
     * @return
     */
    public String getTaskLastDate(){
        DayTaskBean dayTaskBean = dayTaskDao.queryBuilder().where(DayTaskBeanDao.Properties.TaskName.eq("每日签到")).build().unique();
        return dayTaskBean.getResetDay();

    }

    /**
     * 初始化任务
     * @param resetDay
     */
    public void initDayTask(String resetDay){
        clearDayTask();
        ArrayList<DayTaskBean> dayTaskBeans = new ArrayList<>();
        DayTaskBean signIn = new DayTaskBean();
        signIn.setId(1L);
        signIn.setTaskName("每日签到");
        signIn.setContent("每日可以签到一次，获得1-10个云豆");
        signIn.setCreateDate(resetDay);
        signIn.setResetDay(resetDay);
        signIn.setState(0);
        signIn.setTaskType(0);
        dayTaskBeans.add(signIn);

        DayTaskBean zixunTask = new DayTaskBean();
        zixunTask.setId(3L);
        zixunTask.setTaskName("查看资讯");
        zixunTask.setContent("阅读最新资讯/公告可获得2个云豆");
        zixunTask.setCreateDate(resetDay);
        zixunTask.setResetDay(resetDay);
        zixunTask.setTaskType(3);
        zixunTask.setState(0);
        dayTaskBeans.add(zixunTask);

        DayTaskBean shareZixunTask = new DayTaskBean();
        shareZixunTask.setId(4L);
        shareZixunTask.setTaskName("分享资讯");
        shareZixunTask.setContent("成功分享资讯到微信可获得5个云豆");
        shareZixunTask.setCreateDate(resetDay);
        shareZixunTask.setResetDay(resetDay);
        shareZixunTask.setTaskType(4);
        shareZixunTask.setState(0);
        dayTaskBeans.add(shareZixunTask);

        DayTaskBean productTask = new DayTaskBean();
        productTask.setId(6L);
        productTask.setTaskName("查看产品");
        productTask.setContent("查看在线产品可获得2个云豆");
        productTask.setCreateDate(resetDay);
        productTask.setResetDay(resetDay);
        productTask.setState(0);
        productTask.setTaskType(1);
        dayTaskBeans.add(productTask);

        DayTaskBean shareTask = new DayTaskBean();
        shareTask.setId(2L);
        shareTask.setTaskName("分享产品");
        shareTask.setContent("成功分享产品到微信好友可获得5个云豆");
        shareTask.setCreateDate(resetDay);
        shareTask.setResetDay(resetDay);
        shareTask.setTaskType(2);
        shareTask.setState(0);
        dayTaskBeans.add(shareTask);

        DayTaskBean videoTask = new DayTaskBean();
        videoTask.setId(5L);
        videoTask.setTaskName("学习视频");
        videoTask.setContent("观看学院视频超过5分钟可获得10个云豆");
        videoTask.setCreateDate(resetDay);
        videoTask.setResetDay(resetDay);
        videoTask.setTaskType(5);
        videoTask.setState(0);
        dayTaskBeans.add(videoTask);
        dayTaskDao.saveInTx(dayTaskBeans);
    }

    public void destory() {
        otherInfoDao = null;
        userInfoDao = null;
        videoInfoDao = null;
        dayTaskDao = null;
    }

}
