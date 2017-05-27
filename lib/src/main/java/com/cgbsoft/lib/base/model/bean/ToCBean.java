package com.cgbsoft.lib.base.model.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * TODO
 * TODO Created by xiaoyu.zhang on 2016/11/11 13:29
 * TODO Email:zhangxyfs@126.com
 *  
 */
@Entity
public class ToCBean {
    /**
     * customerName : 文杰
     * customerType : 2
     * stockAssetsTime :
     * riskEvaluationIdnum :
     * bandingAdviserId : c659194159cc444091bc31df317671d3
     * riskEvaluationPhone :
     * bandingTime : 2016-10-21 10:04:35
     * customerState : 1
     * customerIdPhoto :
     * investmentType : 1
     * customerPhone : 18210735626
     * stockAssetsId : 622738392916193972
     * assetsCertificationImage :
     * riskEvaluationName :
     * customerIdNumber : 622738392916193972
     * customerIdType : 军官证
     * stockAssetsStatus : 0
     * assetsCertificationStatus : 0
     * isEvaluated : 1
     * customerRiskEvaluation : C,A,B,A,B,C,A,B,C,A,C,A,B,B
     * stockAssetsImage : http://www.simuyun.com/peyunupload//ftp/peyunapp/upload//2016-07-21/cf09acb0931a404eaac2ad90e84e318e.jpg
     */

    @Id
    private String id;

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
    private String customerIdType;
    private int stockAssetsStatus;
    private int assetsCertificationStatus;
    private int isEvaluated;
    private String customerRiskEvaluation;
    private String stockAssetsImage;
    private String gesturePassword;
    private String gestureSwitch;
    @Generated(hash = 1018212650)
    public ToCBean(String id, String customerName, String customerType, String stockAssetsTime, String riskEvaluationIdnum,
            String bandingAdviserId, String riskEvaluationPhone, String bandingTime, String customerState, String customerIdPhoto,
            String investmentType, String customerPhone, String stockAssetsId, String assetsCertificationImage,
            String riskEvaluationName, String customerIdNumber, String customerIdType, int stockAssetsStatus,
            int assetsCertificationStatus, int isEvaluated, String customerRiskEvaluation, String stockAssetsImage,
            String gesturePassword, String gestureSwitch) {
        this.id = id;
        this.customerName = customerName;
        this.customerType = customerType;
        this.stockAssetsTime = stockAssetsTime;
        this.riskEvaluationIdnum = riskEvaluationIdnum;
        this.bandingAdviserId = bandingAdviserId;
        this.riskEvaluationPhone = riskEvaluationPhone;
        this.bandingTime = bandingTime;
        this.customerState = customerState;
        this.customerIdPhoto = customerIdPhoto;
        this.investmentType = investmentType;
        this.customerPhone = customerPhone;
        this.stockAssetsId = stockAssetsId;
        this.assetsCertificationImage = assetsCertificationImage;
        this.riskEvaluationName = riskEvaluationName;
        this.customerIdNumber = customerIdNumber;
        this.customerIdType = customerIdType;
        this.stockAssetsStatus = stockAssetsStatus;
        this.assetsCertificationStatus = assetsCertificationStatus;
        this.isEvaluated = isEvaluated;
        this.customerRiskEvaluation = customerRiskEvaluation;
        this.stockAssetsImage = stockAssetsImage;
        this.gesturePassword = gesturePassword;
        this.gestureSwitch = gestureSwitch;
    }
    @Generated(hash = 1961999522)
    public ToCBean() {
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getCustomerName() {
        return this.customerName;
    }
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    public String getCustomerType() {
        return this.customerType;
    }
    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }
    public String getStockAssetsTime() {
        return this.stockAssetsTime;
    }
    public void setStockAssetsTime(String stockAssetsTime) {
        this.stockAssetsTime = stockAssetsTime;
    }
    public String getRiskEvaluationIdnum() {
        return this.riskEvaluationIdnum;
    }
    public void setRiskEvaluationIdnum(String riskEvaluationIdnum) {
        this.riskEvaluationIdnum = riskEvaluationIdnum;
    }
    public String getBandingAdviserId() {
        return this.bandingAdviserId;
    }
    public void setBandingAdviserId(String bandingAdviserId) {
        this.bandingAdviserId = bandingAdviserId;
    }
    public String getRiskEvaluationPhone() {
        return this.riskEvaluationPhone;
    }
    public void setRiskEvaluationPhone(String riskEvaluationPhone) {
        this.riskEvaluationPhone = riskEvaluationPhone;
    }
    public String getBandingTime() {
        return this.bandingTime;
    }
    public void setBandingTime(String bandingTime) {
        this.bandingTime = bandingTime;
    }
    public String getCustomerState() {
        return this.customerState;
    }
    public void setCustomerState(String customerState) {
        this.customerState = customerState;
    }
    public String getCustomerIdPhoto() {
        return this.customerIdPhoto;
    }
    public void setCustomerIdPhoto(String customerIdPhoto) {
        this.customerIdPhoto = customerIdPhoto;
    }
    public String getInvestmentType() {
        return this.investmentType;
    }
    public void setInvestmentType(String investmentType) {
        this.investmentType = investmentType;
    }
    public String getCustomerPhone() {
        return this.customerPhone;
    }
    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }
    public String getStockAssetsId() {
        return this.stockAssetsId;
    }
    public void setStockAssetsId(String stockAssetsId) {
        this.stockAssetsId = stockAssetsId;
    }
    public String getAssetsCertificationImage() {
        return this.assetsCertificationImage;
    }
    public void setAssetsCertificationImage(String assetsCertificationImage) {
        this.assetsCertificationImage = assetsCertificationImage;
    }
    public String getRiskEvaluationName() {
        return this.riskEvaluationName;
    }
    public void setRiskEvaluationName(String riskEvaluationName) {
        this.riskEvaluationName = riskEvaluationName;
    }
    public String getCustomerIdNumber() {
        return this.customerIdNumber;
    }
    public void setCustomerIdNumber(String customerIdNumber) {
        this.customerIdNumber = customerIdNumber;
    }
    public String getCustomerIdType() {
        return this.customerIdType;
    }
    public void setCustomerIdType(String customerIdType) {
        this.customerIdType = customerIdType;
    }
    public int getStockAssetsStatus() {
        return this.stockAssetsStatus;
    }
    public void setStockAssetsStatus(int stockAssetsStatus) {
        this.stockAssetsStatus = stockAssetsStatus;
    }
    public int getAssetsCertificationStatus() {
        return this.assetsCertificationStatus;
    }
    public void setAssetsCertificationStatus(int assetsCertificationStatus) {
        this.assetsCertificationStatus = assetsCertificationStatus;
    }
    public int getIsEvaluated() {
        return this.isEvaluated;
    }
    public void setIsEvaluated(int isEvaluated) {
        this.isEvaluated = isEvaluated;
    }
    public String getCustomerRiskEvaluation() {
        return this.customerRiskEvaluation;
    }
    public void setCustomerRiskEvaluation(String customerRiskEvaluation) {
        this.customerRiskEvaluation = customerRiskEvaluation;
    }
    public String getStockAssetsImage() {
        return this.stockAssetsImage;
    }
    public void setStockAssetsImage(String stockAssetsImage) {
        this.stockAssetsImage = stockAssetsImage;
    }
    public String getGesturePassword() {
        return this.gesturePassword;
    }
    public void setGesturePassword(String gesturePassword) {
        this.gesturePassword = gesturePassword;
    }
    public String getGestureSwitch() {
        return this.gestureSwitch;
    }
    public void setGestureSwitch(String gestureSwitch) {
        this.gestureSwitch = gestureSwitch;
    }

}
