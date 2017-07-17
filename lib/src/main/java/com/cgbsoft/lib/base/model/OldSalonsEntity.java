package com.cgbsoft.lib.base.model;

import com.cgbsoft.lib.base.mvp.model.BaseResult;

import java.util.List;

/**
 * Created by sunfei on 2017/7/13 0013.
 */

public class OldSalonsEntity extends BaseResult<OldSalonsEntity.SalonBean> {
    public static class SalonBean{
        private List<SalonItemBean> rows;

        public List<SalonItemBean> getRows() {
            return rows;
        }

        public void setRows(List<SalonItemBean> rows) {
            this.rows = rows;
        }
    }
    public static class SalonItemBean{
        private String isButton="0";//1代表是按钮
        private String address;
        private String city;
        private String startTime;
        private String id;
        private String title;
        private String hot;
        private String speaker;
        private String imageUrl;

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getSpeaker() {
            return speaker;
        }

        public void setSpeaker(String speaker) {
            this.speaker = speaker;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getHot() {
            return hot;
        }

        public void setHot(String hot) {
            this.hot = hot;
        }

        public String getIsButton() {
            return isButton;
        }

        public void setIsButton(String isButton) {
            this.isButton = isButton;
        }
    }
}
