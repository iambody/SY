package com.cgbsoft.privatefund.model;

import com.cgbsoft.lib.base.model.CardListEntity;

import java.util.ArrayList;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by zhaojiaqi on 2017/11/3.
 */
/*
    "customerName": "",
    "id": "11081",
    "credentialNumber": "410****4031",
    "failCount": "0",
    "credentialCode": "100101",
    "imageUrl": [
      {
        "name": "frontImage",
        "url": "https://upload.simuyun.com/-/d6-app.simuyun.com/credential/100101/bed6f86d-0e86-48bf-90ac-17a1a551b5a5.jpg"
      },
      {
        "name": "backImage",
        "url": "https://upload.simuyun.com/-/d6-app.simuyun.com/credential/100101/98b3e816-3289-4e90-9014-61f611db9f30.jpg"
      }
    ],
    "validCode": "2",
    "userId": "19000000066",
    "stateName": "已通过",
    "credentialTypeName": "身份证",
    "stateCode": "50",
    "credentialNumberTrue": "410425199108114031",
    "periodValidity": "2024-07-09",
    "comment": ""
 */
public class CredentialModel {
    private String id;
    private ArrayList<CardListEntity.ImageBean> imageUrl;

    private String stateName;
    private String credentialTypeName;
    private String stateCode;
    private String credentialNumber;
    private String periodValidity;
    private String credentialCode;
    private String comment;
    private String userId;
    private String customerName;
    private String credentialNumberTrue;
    private String failCount;
    private String validCode;

    public CredentialModel(String id, ArrayList<CardListEntity.ImageBean> imageUrl, String stateName, String name, String stateCode, String number, String periodValidity, String code, String comment, String appUserId, String customerName,String numberTrue) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.stateName = stateName;
        this.credentialTypeName = name;
        this.stateCode = stateCode;
        this.credentialNumber = number;
        this.periodValidity = periodValidity;
        this.credentialCode = code;
        this.comment = comment;
        this.userId = appUserId;
        this.customerName = customerName;
        this.credentialNumberTrue = numberTrue;
    }


    public String getFailCount() {
        return failCount;
    }

    public void setFailCount(String failCount) {
        this.failCount = failCount;
    }

    public String getValidCode() {
        return validCode;
    }

    public void setValidCode(String validCode) {
        this.validCode = validCode;
    }

    public String getNumberTrue() {
        return credentialNumberTrue;
    }

    public void setNumberTrue(String numberTrue) {
        this.credentialNumberTrue = numberTrue;
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
        return credentialTypeName;
    }

    public void setName(String name) {
        this.credentialTypeName = name;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getNumber() {
        return credentialNumber;
    }

    public void setNumber(String number) {
        this.credentialNumber = number;
    }

    public String getPeriodValidity() {
        return periodValidity;
    }

    public void setPeriodValidity(String periodValidity) {
        this.periodValidity = periodValidity;
    }

    public String getCode() {
        return credentialCode;
    }

    public void setCode(String code) {
        this.credentialCode = code;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getAppUserId() {
        return userId;
    }

    public void setAppUserId(String appUserId) {
        this.userId = appUserId;
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
