package com.cgbsoft.lib.base.model;

import com.cgbsoft.lib.base.mvp.model.BaseResult;

/**
 * 用户信息
 * Created by xiaoyu.zhang on 2016/11/11 13:24
 * Email:zhangxyfs@126.com
 *  
 */
public class UserInfoDataEntity extends BaseResult<UserInfoDataEntity.Result> {

    public static class Result {
        public String token;
        public String userId;
        public String isBind;
        public UserInfo userInfo;
    }


    public static class UserInfo{
        public String birthday;
        public String sex;
        public String headImageUrl;
        public ToCBean toC;
        public String unionid;
        public String rcToken;
        public ToBBean toB;
        public String adviserRealName;
        public String adviserPhone;
        public String memoToMember;
        public String education;
        public String authenticationType;
        public String id;
        public String phoneNum;
        public String residentCity;
        public String isSingIn;
        public String email;
        public String nickName;
        public String ageStage;
        public String userName;
        public String realName;
        public String myPoString;
        public String fatherId;
        public String lastLogStringime;
        public String memoToFather;
    }

    public static class ToBBean{
        public String organizationName;
        public String preparedforNum;
        public String submitTime;
        public String teamNum;
        public String organizationId;
        public String beatRank;
        public String adviserPhoto;
        public String adviserStatus;
        public String adviserOrganization;
        public String adviserName;
        public String adviserLevel;
        public String isEmployee;
        public String adviserNumber;
        public String completedOrderCount;
        public String rejectReason;
        public String serviceCheckStatus;
        public String adviserPerformance;
        public String adviserState;
        public String category;
        public String adviserRank;
        public String myCustomersCount;
        public String authUpdateTime;
        public String inviteCode;
        public String completedOrderAmountCount;
        public String isExtension;
    }

    public static class ToCBean{
        public String customerName;
        public String customerType;
        public String stockAssetsTime;
        public String riskEvaluationIdnum;
        public String bandingAdviserId;
        public String riskEvaluationPhone;
        public String bandingTime;
        public String customerState;
        public String customerIdPhoto;
        public String investmentType;
        public String customerPhone;
        public String stockAssetsId;
        public String assetsCertificationImage;
        public String riskEvaluationName;
        public String customerIdNumber;
        public String customerIdType;
        public String stockAssetsStatus;
        public String assetsCertificationStatus;
        public String isEvaluated;
        public String customerRiskEvaluation;
        public String stockAssetsImage;
        public String checkFailureReason;

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

        public String getCustomerIdType() {
            return customerIdType;
        }

        public void setCustomerIdType(String customerIdType) {
            this.customerIdType = customerIdType;
        }

        public String getStockAssetsStatus() {
            return stockAssetsStatus;
        }

        public void setStockAssetsStatus(String stockAssetsStatus) {
            this.stockAssetsStatus = stockAssetsStatus;
        }

        public String getAssetsCertificationStatus() {
            return assetsCertificationStatus;
        }

        public void setAssetsCertificationStatus(String assetsCertificationStatus) {
            this.assetsCertificationStatus = assetsCertificationStatus;
        }

        public String getIsEvaluated() {
            return isEvaluated;
        }

        public void setIsEvaluated(String isEvaluated) {
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

        public String getCheckFailureReason() {
            return checkFailureReason;
        }

        public void setCheckFailureReason(String checkFailureReason) {
            this.checkFailureReason = checkFailureReason;
        }
    }
}
