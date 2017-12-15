package com.cgbsoft.lib.base.model;

import com.cgbsoft.lib.base.mvp.model.BaseResult;

import java.util.List;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/6/28-10:49
 */
public class HomeEntity extends BaseResult<HomeEntity.Result> {
    public static class Result {
        public List<Banner> banner;
        public List<Operate> module;
        public Level myInfo;
    }

    public static class Banner {
        public String jumpId;//": "200100",
        public String url;//": "https://",
        public String imageUrl;//": "https://",
        public String title;//": "简单生活物语"
        public String jumpType;//
    }

    public static class Operate {
        public String jumpId;//": "300200",
        public String imageUrl;//": "https://",
        public String title;//": "会员专区",
        public String jumpType;//": "h5",
        public String url;//": "https://"
        public String isVisitorVisible;//1可点进去 2跳转登录
       public boolean isAppInAudit;//ios
    }

    public static class Level {
        public String  memberLevel;
        public String level;//":"1",
        public String levelName;//":"LV1盈卡",
        public String url;//":"http://www.baidu.com"

    }
}
