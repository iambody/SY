package com.cgbsoft.privatefund.bean;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * Created by lee on 2016/9/10.
 */
public class UserInfoC implements Serializable {
    private String customerName;
    private String customerType;
    private String stockAssetsTime;
    private String riskEvaluationIdnum;
    private String bandingAdviserId;
    private String riskEvaluationPhone;
    private String bandingTime;
    private String customerState;
    private String customerIdPhoto;
    private String investmentType;
    private String customerPhone;
    private String stockAssetsId;
    private String assetsCertificationImage;
    private String riskEvaluationName;
    private String customerIdNumber;
    private String myAssetsNum;
    private String customerIdType;
    private int stockAssetsStatus;
    private int assetsCertificationStatus;
    private int isEvaluated;
    private String customerRiskEvaluation;
    private String stockAssetsImage;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getStockAssetsTime() {
        return stockAssetsTime;
    }

    public void setStockAssetsTime(String stockAssetsTime) {
        this.stockAssetsTime = stockAssetsTime;
    }

    public String getRiskEvaluationIdnum() {
        return riskEvaluationIdnum;
    }

    public void setRiskEvaluationIdnum(String riskEvaluationIdnum) {
        this.riskEvaluationIdnum = riskEvaluationIdnum;
    }

    public String getBandingAdviserId() {
        return bandingAdviserId;
    }

    public void setBandingAdviserId(String bandingAdviserId) {
        this.bandingAdviserId = bandingAdviserId;
    }

    public String getRiskEvaluationPhone() {
        return riskEvaluationPhone;
    }

    public void setRiskEvaluationPhone(String riskEvaluationPhone) {
        this.riskEvaluationPhone = riskEvaluationPhone;
    }

    public String getBandingTime() {
        return bandingTime;
    }

    public void setBandingTime(String bandingTime) {
        this.bandingTime = bandingTime;
    }

    public String getCustomerState() {
        return customerState;
    }

    public void setCustomerState(String customerState) {
        this.customerState = customerState;
    }

    public String getCustomerIdPhoto() {
        return customerIdPhoto;
    }

    public void setCustomerIdPhoto(String customerIdPhoto) {
        this.customerIdPhoto = customerIdPhoto;
    }

    public String getInvestmentType() {
        return investmentType;
    }

    public void setInvestmentType(String investmentType) {
        this.investmentType = investmentType;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getStockAssetsId() {
        return stockAssetsId;
    }

    public void setStockAssetsId(String stockAssetsId) {
        this.stockAssetsId = stockAssetsId;
    }

    public String getAssetsCertificationImage() {
        return assetsCertificationImage;
    }

    public void setAssetsCertificationImage(String assetsCertificationImage) {
        this.assetsCertificationImage = assetsCertificationImage;
    }

    public String getBindTeacher(){
        if (!TextUtils.isEmpty(bandingAdviserId)){
            return "1";
        }
        return "0";
    }

    public String getRiskEvaluationName() {
        return riskEvaluationName;
    }

    public void setRiskEvaluationName(String riskEvaluationName) {
        this.riskEvaluationName = riskEvaluationName;
    }

    public String getCustomerIdNumber() {
        return customerIdNumber;
    }

    public void setCustomerIdNumber(String customerIdNumber) {
        this.customerIdNumber = customerIdNumber;
    }

    public String getMyAssetsNum() {
        return myAssetsNum;
    }

    public void setMyAssetsNum(String myAssetsNum) {
        this.myAssetsNum = myAssetsNum;
    }

    public String getCustomerIdType() {
        return customerIdType;
    }

    public void setCustomerIdType(String customerIdType) {
        this.customerIdType = customerIdType;
    }

    public int getStockAssetsStatus() {
        return stockAssetsStatus;
    }

    public void setStockAssetsStatus(int stockAssetsStatus) {
        this.stockAssetsStatus = stockAssetsStatus;
    }

    public int getAssetsCertificationStatus() {
        return assetsCertificationStatus;
    }

    public void setAssetsCertificationStatus(int assetsCertificationStatus) {
        this.assetsCertificationStatus = assetsCertificationStatus;
    }

    public int getIsEvaluated() {
        return isEvaluated;
    }

    public void setIsEvaluated(int isEvaluated) {
        this.isEvaluated = isEvaluated;
    }

    public String getCustomerRiskEvaluation() {
        return customerRiskEvaluation;
    }

    public void setCustomerRiskEvaluation(String customerRiskEvaluation) {
        this.customerRiskEvaluation = customerRiskEvaluation;
    }

    public String getStockAssetsImage() {
        return stockAssetsImage;
    }

    public void setStockAssetsImage(String stockAssetsImage) {
        this.stockAssetsImage = stockAssetsImage;
    }
}
