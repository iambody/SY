package com.cgbsoft.lib.base.model;

import com.cgbsoft.lib.base.mvp.model.BaseResult;

import java.util.List;

/**
 * Created by fei on 2017/8/14.
 */

public class CardListEntity extends BaseResult<CardListEntity.Result>{
    public static class Result{
        private List<CardListEntity.CardBean> credentials;

        public List<CardListEntity.CardBean> getCredentials() {
            return credentials;
        }

        public void setCredentials(List<CardListEntity.CardBean> credentials) {
            this.credentials = credentials;
        }
    }
    public static class CardBean{
        private List<ImageBean> images;
        private String stateName;
        private String name;
        private String stateCode;
        private String number;
        private String code;
        private String comment;

        public List<ImageBean> getImages() {
            return images;
        }

        public void setImages(List<ImageBean> images) {
            this.images = images;
        }

        public String getStateName() {
            return stateName;
        }

        public void setStateName(String stateName) {
            this.stateName = stateName;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getStateCode() {
            return stateCode;
        }

        public void setStateCode(String stateCode) {
            this.stateCode = stateCode;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }
    }
    public static class ImageBean{
        private String name;
        private String url;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
