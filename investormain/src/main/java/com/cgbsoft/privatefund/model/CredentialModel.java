package com.cgbsoft.privatefund.model;

import com.cgbsoft.lib.base.model.CardListEntity;

import java.util.ArrayList;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by zhaojiaqi on 2017/11/3.
 */

public class CredentialModel {
    private String id;
    private ArrayList<CardListEntity.ImageBean> imageUrl;

    private String stateName;
    private String name;
    private String stateCode;
    private String number;
    private String periodValidity;
    private String code;
    private String comment;
    private String appUserId;
    private String customerName;

    public CredentialModel(String id, ArrayList<CardListEntity.ImageBean> imageUrl, String stateName, String name, String stateCode, String number, String periodValidity, String code, String comment, String appUserId, String customerName) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.stateName = stateName;
        this.name = name;
        this.stateCode = stateCode;
        this.number = number;
        this.periodValidity = periodValidity;
        this.code = code;
        this.comment = comment;
        this.appUserId = appUserId;
        this.customerName = customerName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<CardListEntity.ImageBean> getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(ArrayList<CardListEntity.ImageBean> imageUrl) {
        this.imageUrl = imageUrl;
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

    public String getPeriodValidity() {
        return periodValidity;
    }

    public void setPeriodValidity(String periodValidity) {
        this.periodValidity = periodValidity;
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

    public String getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(String appUserId) {
        this.appUserId = appUserId;
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
