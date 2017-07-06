package com.cgbsoft.privatefund.model;

import com.cgbsoft.lib.base.model.bean.BannerBean;
import com.cgbsoft.lib.utils.tools.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chenlong
 */
public class DiscoverModel {

    public List<Banner> banner;

    public List<DiscoveryListModel> informations;

    public List<DiscoverCategory> category;

    /**
     * Banner
     */
    public static class Banner {
        public String imageURLString;
        public String title;
        public String extensionUrl;
    }

    /**
     * 发现的分类
     */
    public static class DiscoverCategory {
        public String text;
        public String value;
        public String prelog;
        public String norlog;
    }

    /**
     * 发现列表
     */
    public static class DiscoveryListModel {
        private String id;

        private String image;

        private String category;

        private String title;

        private String views;

        public String getViews() {
            return views;
        }

        public void setViews(String views) {
            this.views = views;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

    public static BannerBean formatBanner(Banner banner) {
        BannerBean bannerBean = new BannerBean();
        bannerBean.setTitle(banner.title);
        bannerBean.setImageUrl(banner.imageURLString);
        bannerBean.setJumpUrl(banner.extensionUrl);
        return bannerBean;
    }

    public static List<BannerBean> formatBanner(List<Banner> bannerList) {
        List<BannerBean> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(bannerList)) {
            for (Banner banner : bannerList) {
                BannerBean bannerBean = new BannerBean();
                bannerBean.setTitle(banner.title);
                bannerBean.setImageUrl(banner.imageURLString);
                bannerBean.setJumpUrl(banner.extensionUrl);
                list.add(bannerBean);
            }
        }
        return list;
    }

}
