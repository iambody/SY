package com.cgbsoft.lib.base.model;

import com.cgbsoft.lib.base.mvp.model.BaseResult;

import java.util.List;

/**
 * 学院视频数据
 * Created by xiaoyu.zhang on 2016/12/1 14:58
 * Email:zhangxyfs@126.com
 *  
 */
public class CollegeVideoEntity extends BaseResult {
    public static class Result {
        public List<Row> rows;
    }

    public static class Row{
        public String sdvideoUrl;
        public String hdvideoUrl;
        public String likes;
        public String videoSummary;
        public String videoId;
        public String isLiked;
        public String tencentAppId;
        public String createDate;
        public String tencentVideoId;
        public String coverImageUrl;
        public String shortName;
        public String videoName;
        public String currentTime;  //增加当前播放时间
        public String id;            //增加id
        public String status;       //下载完成:2 / 下载中：1  / 未下载：0
        public String finalPlayTime;  //最后一次播放时间
        public String size;          //下载文件大小
        public String percent;       //下载百分百
        public String downloadtype;     //下载清晰度  0 表示高清 1 标清
        public String downloadTime;   //下载时间
        public String encrypt;          //1:没有加密 2：加密
        public String hasRecord;    //是否在播放列表中显示   1显示 0不显示
    }
}
