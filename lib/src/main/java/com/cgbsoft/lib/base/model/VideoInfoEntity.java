package com.cgbsoft.lib.base.model;

import com.cgbsoft.lib.base.mvp.model.BaseResult;

import java.util.List;

/**
 * 视频详情
 * Created by xiaoyu.zhang on 2016/12/8 11:45
 * Email:zhangxyfs@126.com
 *  
 */
public class VideoInfoEntity extends BaseResult<VideoInfoEntity.Result> {

    public static class Result {
        public   String videoId;
        public   Rows rows;

    }

    public static class Rows {
        public String shareUrl;
        public String shareUrlToB;
        public String shareUrlToC;
        public String shareImg;
        public String canShare;
        public String videoId;
        public String groupId;
        public String playCount;
        public String lecturerRemark;
        public String playLimit;

        public String videoName;
        public String videoSummary;
        public String shortName;
        public String coverImageUrl;
        public String tencentAppId;
        public String tencentVideoId;
        public String likes;
        public String createDate;
        public String isLiked;
        public String SDVideoUrl;
        public String HDVideoUrl;
        public List<CommentBean> comment;
    }

    // 评论的部分
    public static class CommentBean {
        public String sender;//":"6c3a3d65ab0f4bfda527a6318a292e13",
        public String commentTime;//":"2017-5-19 18:56:53",
        public String sendName;//":"陈龙",
        public String commentContent;//":"讲解很到位很棒！！！！",
        public String sendAvatar;//":"https://upload.simuyun.com/avata/283c09c7-13bf-40a4-a4d7-057a1990b295.png",
        public String commentId;//":105
    }

}
