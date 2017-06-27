package app.privatefund.com.vido.bean;

import java.util.List;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/6/23-16:50
 */
public class VideoAllModel {

    public List<Banner> banner;

    public List<VideoListModel> video;

    public List<VideoCategory> category;


    /**
     * Banner
     */
    public static class Banner {
        public String imageURLString;//": "http://pic1.win4000.com/wallpaper/f/54a8f54c6f210.jpg",
        public String title;//": "1234567",
        public String extension_url;//"
    }

    /**
     * 视频的分类
     */
    public static class VideoCategory {
        public String text;//": "商学院",
        public String value;//": "1"
        public String prelog;//": ""http://upload.simuyun.com/videos/1b22f252-b2fd-4172-a372-46d210823e58.png"",
        public String norlog;//":
    }

    /**
     * 视频列表列表
     */
    public static class VideoListModel {
        public String videoId;//": "3d44fc6eb052482b91e41488f667dc96",
        public String videoName;//": "213124123",
        public String videoSummary;//": "",
        public String shortName;//": "",
        public String coverImageUrl;//": "http://upload.simuyun.com/videos/1b22f252-b2fd-4172-a372-46d210823e58.png",
        public String SDVideoUrl;//": "http://1251892263.vod2.myqcloud.com/51e77c4evodtransgzp1251892263/1c197c069031868222904029367/f0.f30.mp4",
        public String HDVideoUrl;//": "http://1251892263.vod2.myqcloud.com/51e77c4evodtransgzp1251892263/1c197c069031868222904029367/f0.f30.mp4",
        public String tencentAppId;//": "1251892263",
        public String tencentVideoId;//": "1234566231234123",
        public String createDate;//": "2017-06-23",
        public String isLiked;//": "0",
        public String likes;//": 0,
        public String keyword;//": "",
        public String filterKey;//": "产品培训",
        public String isPush;//": "",
        public String category;//": "1",
        public String updateUser;//": "1882",
        public String createUser;//": "1882",
        public String updateTime;//": "2017-06-23 14:10:21",
        public String createTime;//": "2017-06-23 14:10:21"
    }

}
