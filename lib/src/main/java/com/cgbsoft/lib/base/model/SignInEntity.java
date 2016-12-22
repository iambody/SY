package com.cgbsoft.lib.base.model;

import com.cgbsoft.lib.base.mvp.model.BaseResult;

/**
 * Created by xiaoyu.zhang on 2016/12/16 17:56
 * Email:zhangxyfs@126.com
 *  
 */
public class SignInEntity extends BaseResult<SignInEntity.Result> {
    public static class Result {
        public String point;
        public Information information;
        public String todayPoint;
        public String totalPoint;
        public String signInDate;
        public String isSignin;
        public String Multiple;
        public String infoTitle;
    }

    public static class Information {
        String infoId;
        String title;
        String likes;
        String publish_time;
        String category;
        String isLike;
        String summary;
    }
}
