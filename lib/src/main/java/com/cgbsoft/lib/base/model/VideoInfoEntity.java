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
        public String videoId;
        public Rows rows;

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
        public List<ProductBean>product;
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

    public static class ProductBean {
        public String productId;//":"9b768306e36348319b9f4d3547b3c8a1",
        public String productName;//":"恒宇天泽长江二十七号私募投资基金",
        public String incomeMax;//":"7.0%+浮动",
        public String incomeMin;//":"7.0%+浮动",
        public String netUnit;//":"7.0%+浮动",
        public String netAll;//":"7.0%+浮动",
        public String productType;//":"1",
        public String remainingAmount;//":3900,
        public String remainingAmountStr;//":"3900万",
        public String raiseEndTime;//":"2017-05-26 17:00",
        public String term;//":"最长60个月",
        public String buyStart;//":100,
        public String increaseAmt;//":10,
        public String schemeId;//":"65b9e07d0337483286819c8878c684c9",
    }
}
