package com.cgbsoft.lib.base.model.bean;

import com.cgbsoft.lib.utils.constant.VideoStatus;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by xiaoyu.zhang on 2016/12/9 09:09
 * Email:zhangxyfs@126.com
 *  
 */
@Entity
public class VideoInfo {
    @Id
    private Long id;            //增加id
    private String videoId;

    private String videoCoverUrl;
    private String sdUrl;
    private String hdUrl;
    private boolean isLike;
    private String videoName;
    private String shortName;
    private String content;
    private int likeNum;

    private String localVideoPath;

    private int currentTime;  //增加当前播放时间
    private int status = VideoStatus.NONE;
    private long finalPlayTime;  //最后一次播放时间
    private long size;          //下载文件大小
    private double percent;       //下载百分百
    private int downloadtype;     //下载清晰度  0 表示高清 1 标清
    private long downloadTime;   //下载时间
    private int encrypt;          //1:没有加密 2：加密
    private int hasRecord = 1;    //是否在播放列表中显示   1显示 0不显示
    private int isDelete = 0;//是否删除了
    @Generated(hash = 357693825)
    public VideoInfo(Long id, String videoId, String videoCoverUrl, String sdUrl,
            String hdUrl, boolean isLike, String videoName, String shortName,
            String content, int likeNum, String localVideoPath, int currentTime,
            int status, long finalPlayTime, long size, double percent,
            int downloadtype, long downloadTime, int encrypt, int hasRecord,
            int isDelete) {
        this.id = id;
        this.videoId = videoId;
        this.videoCoverUrl = videoCoverUrl;
        this.sdUrl = sdUrl;
        this.hdUrl = hdUrl;
        this.isLike = isLike;
        this.videoName = videoName;
        this.shortName = shortName;
        this.content = content;
        this.likeNum = likeNum;
        this.localVideoPath = localVideoPath;
        this.currentTime = currentTime;
        this.status = status;
        this.finalPlayTime = finalPlayTime;
        this.size = size;
        this.percent = percent;
        this.downloadtype = downloadtype;
        this.downloadTime = downloadTime;
        this.encrypt = encrypt;
        this.hasRecord = hasRecord;
        this.isDelete = isDelete;
    }
    @Generated(hash = 296402066)
    public VideoInfo() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getVideoId() {
        return this.videoId;
    }
    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }
    public String getVideoCoverUrl() {
        return this.videoCoverUrl;
    }
    public void setVideoCoverUrl(String videoCoverUrl) {
        this.videoCoverUrl = videoCoverUrl;
    }
    public String getSdUrl() {
        return this.sdUrl;
    }
    public void setSdUrl(String sdUrl) {
        this.sdUrl = sdUrl;
    }
    public String getHdUrl() {
        return this.hdUrl;
    }
    public void setHdUrl(String hdUrl) {
        this.hdUrl = hdUrl;
    }
    public boolean getIsLike() {
        return this.isLike;
    }
    public void setIsLike(boolean isLike) {
        this.isLike = isLike;
    }
    public String getVideoName() {
        return this.videoName;
    }
    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }
    public String getShortName() {
        return this.shortName;
    }
    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
    public String getContent() {
        return this.content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public int getLikeNum() {
        return this.likeNum;
    }
    public void setLikeNum(int likeNum) {
        this.likeNum = likeNum;
    }
    public String getLocalVideoPath() {
        return this.localVideoPath;
    }
    public void setLocalVideoPath(String localVideoPath) {
        this.localVideoPath = localVideoPath;
    }
    public int getCurrentTime() {
        return this.currentTime;
    }
    public void setCurrentTime(int currentTime) {
        this.currentTime = currentTime;
    }
    public int getStatus() {
        return this.status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public long getFinalPlayTime() {
        return this.finalPlayTime;
    }
    public void setFinalPlayTime(long finalPlayTime) {
        this.finalPlayTime = finalPlayTime;
    }
    public long getSize() {
        return this.size;
    }
    public void setSize(long size) {
        this.size = size;
    }
    public double getPercent() {
        return this.percent;
    }
    public void setPercent(double percent) {
        this.percent = percent;
    }
    public int getDownloadtype() {
        return this.downloadtype;
    }
    public void setDownloadtype(int downloadtype) {
        this.downloadtype = downloadtype;
    }
    public long getDownloadTime() {
        return this.downloadTime;
    }
    public void setDownloadTime(long downloadTime) {
        this.downloadTime = downloadTime;
    }
    public int getEncrypt() {
        return this.encrypt;
    }
    public void setEncrypt(int encrypt) {
        this.encrypt = encrypt;
    }
    public int getHasRecord() {
        return this.hasRecord;
    }
    public void setHasRecord(int hasRecord) {
        this.hasRecord = hasRecord;
    }
    public int getIsDelete() {
        return this.isDelete;
    }
    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }
}
