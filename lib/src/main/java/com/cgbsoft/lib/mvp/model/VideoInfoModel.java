package com.cgbsoft.lib.mvp.model;

/**
 * Created by xiaoyu.zhang on 2016/12/8 11:55
 * Email:zhangxyfs@126.com
 *  
 */
public class VideoInfoModel {
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

    public String localVideoPath;

    public int currentTime;  //增加当前播放时间
    public int status;       //下载完成:2 / 下载中：0  / 未下载：1
    public long finalPlayTime;  //最后一次播放时间
    public long size;          //下载文件大小
    public double percent;       //下载百分百
    public int downloadtype;     //下载清晰度  0 表示高清 1 标清
    public long downloadTime;   //下载时间
    public int encrypt;          //1:没有加密 2：加密
    public int hasRecord = 1;    //是否在播放列表中显示   1显示 0不显示

}
