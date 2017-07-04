package com.cgbsoft.lib.base.model;

import com.cgbsoft.lib.base.mvp.model.BaseResult;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/6/28-10:49
 */
public class HomeEntity extends BaseResult<HomeEntity.Result> {
    public static class Result {
    }

    public static class Banner {
        public String jumpId;//": "200100",
        public String url;//": "https://",
        public String imageUrl;//": "https://",
        public String title;//": "简单生活物语"
    }

    public static class Operate {
        public String jumpId;//": "300200",
        public String imageUrl;//": "https://",
        public String title;//": "会员专区",
        public String jumpType;//": "h5",
        public String url;//": "https://"
    }
}
