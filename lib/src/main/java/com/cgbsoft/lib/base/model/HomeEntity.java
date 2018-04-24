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
        public bankInf bank;
    }

    public static class Banner {
        public String jumpId;//": "200100",
        public String url;//": "https://",
        public String imageUrlV3;//": "https://",
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
        public String memberLevel;
        public String level;//":"1",
        public String levelName;//":"LV1盈卡",
        public String url;//":"http://www.baidu.com"

    }

    public static class bankInf {
        public String jumpId;//":"2001",
        public String title;//":"尊享私行",
        public String subtitle;//":"连接人与财富",
        public bankBean product;
        public Infomation information;
    }

    public static class bankBean {
        public String title;//":"2001",
        public bankData content;//":"尊享私行",

    }

    public static class bankData {
        public String remainingAmount;//":7030,
        public String series;//":"3",
        public String schemeId;//":"66534c125a974cc192716708e5f2cdcb",
        public String raiseEndTime;//":"2018-01-03 10:05",
        public String investmentArea;//":"房地产",
        public String hotName;//":"中弘百荣，债权，产品亮点突出，未来市场潜力巨大！",
        public String state;//":"50",
        public String expectedYield;//":"8.9",
        public String marketingImageUrl;//":"https://upload.simuyun.com/products/2221b9a2-3901-4fed-bc80-bc5222e0c413.jpg",
        public String cumulativeNet;//":"",
        public String productType;//":"1",
        public String productId;//":"afcee0def84744f0a6066890a8192b5c",
        public String isHotProduct;//":"1",
        public String term;//":"12个月",
        public String shortName;//":"恒山十七号",
        public String productName;//":"天和盈泰恒山十七号私募投资基金"
    }

    public static class Infomation {
        public List<InfoContent> content;
        public String title;
        public String jumpId;

        public class InfoContent {
            public String id;
            public String title;
            public String category;
            public String imageUrl;
            public String readCount;
            public String label;
        }
    }
}
