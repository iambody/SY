package com.cgbsoft.lib.base.model;

import com.cgbsoft.lib.base.mvp.model.BaseResult;

import java.util.List;

/**
 * Created by sunfei on 2017/7/13 0013.
 */

public class SalonsEntity extends BaseResult<SalonsEntity.Result> {
    public static class Result{
        private List<CityBean> citys;
        private SalonBean salons;

        public List<CityBean> getCitys() {
            return citys;
        }

        public void setCitys(List<CityBean> citys) {
            this.citys = citys;
        }

        public SalonBean getSalons() {
            return salons;
        }

        public void setSalons(SalonBean salons) {
            this.salons = salons;
        }
    }
    public static class CityBean{
        private String code;
        private String text;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
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
        private String mainImage;
        private String id;
        private String title;
        private String hot;

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

        public String getMainImage() {
            return mainImage;
        }

        public void setMainImage(String mainImage) {
            this.mainImage = mainImage;
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
