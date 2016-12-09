package com.cgbsoft.lib.base.model;

import com.cgbsoft.lib.base.mvp.model.BaseResult;

/**
 * 视频详情
 * Created by xiaoyu.zhang on 2016/12/8 11:45
 * Email:zhangxyfs@126.com
 *  
 */
public class VideoInfoEntity extends BaseResult<VideoInfoEntity.Result> {

    public static class Result{
        public String videoId;
        public String videoName;
        public String videoSummary;
        public String shortName;
        public String coverImageUrl;
        public String tencentAppId;
        public String tencentVideoId;
        public String likes;
        public String createDate;
        public String isLiked;
        public String sdvideoUrl;
        public String hdvideoUrl;
    }


}
