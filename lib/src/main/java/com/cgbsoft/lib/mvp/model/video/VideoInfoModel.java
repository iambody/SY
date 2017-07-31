package com.cgbsoft.lib.mvp.model.video;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by xiaoyu.zhang on 2016/12/8 11:55
 * Email:zhangxyfs@126.com
 *  
 */
public class VideoInfoModel implements Parcelable {
    public int id;            //增加id
    public String videoId;

    public String videoCoverUrl;
    public String sdUrl;
    public String hdUrl;
    public boolean isLike;
    public String videoName;
    public String shortName;
    public String content;
    public int likeNum;
public String lecturerRemark;
    public String localVideoPath;
    public String categoryName;

    public int currentTime;  //增加当前播放时间
    public int status;
    public long finalPlayTime;  //最后一次播放时间
    public long size;          //下载文件大小
    public double percent;       //下载百分百
    public int downloadtype;     //下载清晰度  0 表示高清 1 标清
    public long downloadTime;   //下载时间
    public int encrypt;          //1:没有加密 2：加密
    public int hasRecord = 1;    //是否在播放列表中显示   1显示 0不显示
    public int isDelete = 0;//是否删除了

    public VideoInfoModel() {}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.videoId);
        dest.writeString(this.videoCoverUrl);
        dest.writeString(this.sdUrl);
        dest.writeString(this.hdUrl);
        dest.writeByte(this.isLike ? (byte) 1 : (byte) 0);
        dest.writeString(this.videoName);
        dest.writeString(this.shortName);
        dest.writeString(this.content);
        dest.writeInt(this.likeNum);
        dest.writeString(this.lecturerRemark);
        dest.writeString(this.localVideoPath);
        dest.writeString(this.categoryName);
        dest.writeInt(this.currentTime);
        dest.writeInt(this.status);
        dest.writeLong(this.finalPlayTime);
        dest.writeLong(this.size);
        dest.writeDouble(this.percent);
        dest.writeInt(this.downloadtype);
        dest.writeLong(this.downloadTime);
        dest.writeInt(this.encrypt);
        dest.writeInt(this.hasRecord);
        dest.writeInt(this.isDelete);
    }

    protected VideoInfoModel(Parcel in) {
        this.id = in.readInt();
        this.videoId = in.readString();
        this.videoCoverUrl = in.readString();
        this.sdUrl = in.readString();
        this.hdUrl = in.readString();
        this.isLike = in.readByte() != 0;
        this.videoName = in.readString();
        this.shortName = in.readString();
        this.content = in.readString();
        this.likeNum = in.readInt();
        this.lecturerRemark = in.readString();
        this.localVideoPath = in.readString();
        this.categoryName = in.readString();
        this.currentTime = in.readInt();
        this.status = in.readInt();
        this.finalPlayTime = in.readLong();
        this.size = in.readLong();
        this.percent = in.readDouble();
        this.downloadtype = in.readInt();
        this.downloadTime = in.readLong();
        this.encrypt = in.readInt();
        this.hasRecord = in.readInt();
        this.isDelete = in.readInt();
    }

    public static final Creator<VideoInfoModel> CREATOR = new Creator<VideoInfoModel>() {
        @Override
        public VideoInfoModel createFromParcel(Parcel source) {
            return new VideoInfoModel(source);
        }

        @Override
        public VideoInfoModel[] newArray(int size) {
            return new VideoInfoModel[size];
        }
    };
}
