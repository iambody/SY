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

    protected VideoInfoModel(Parcel in) {
        id = in.readInt();
        videoId = in.readString();
        videoCoverUrl = in.readString();
        sdUrl = in.readString();
        hdUrl = in.readString();
        isLike = in.readByte() != 0;
        videoName = in.readString();
        shortName = in.readString();
        content = in.readString();
        likeNum = in.readInt();
        lecturerRemark = in.readString();
        localVideoPath = in.readString();
        currentTime = in.readInt();
        status = in.readInt();
        finalPlayTime = in.readLong();
        size = in.readLong();
        percent = in.readDouble();
        downloadtype = in.readInt();
        downloadTime = in.readLong();
        encrypt = in.readInt();
        hasRecord = in.readInt();
        isDelete = in.readInt();
    }

    public static final Creator<VideoInfoModel> CREATOR = new Creator<VideoInfoModel>() {
        @Override
        public VideoInfoModel createFromParcel(Parcel in) {
            return new VideoInfoModel(in);
        }

        @Override
        public VideoInfoModel[] newArray(int size) {
            return new VideoInfoModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(videoId);
        dest.writeString(videoCoverUrl);
        dest.writeString(sdUrl);
        dest.writeString(hdUrl);
        dest.writeByte((byte) (isLike ? 1 : 0));
        dest.writeString(videoName);
        dest.writeString(shortName);
        dest.writeString(content);
        dest.writeInt(likeNum);
        dest.writeString(lecturerRemark);
        dest.writeString(localVideoPath);
        dest.writeInt(currentTime);
        dest.writeInt(status);
        dest.writeLong(finalPlayTime);
        dest.writeLong(size);
        dest.writeDouble(percent);
        dest.writeInt(downloadtype);
        dest.writeLong(downloadTime);
        dest.writeInt(encrypt);
        dest.writeInt(hasRecord);
        dest.writeInt(isDelete);
    }
}
