package com.cgbsoft.lib.base.model;

import android.text.TextUtils;

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

    public static class UserInfo {
        public String userNewId;
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
        public String myPoint;
        public String fatherId;
        public String lastLogStringime;
        public String memoToFather;
        public String isAdvisers;

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getHeadImageUrl() {
            return headImageUrl;
        }

        public void setHeadImageUrl(String headImageUrl) {
            this.headImageUrl = headImageUrl;
        }

        public ToCBean getToC() {
            return toC;
        }

        public void setToC(ToCBean toC) {
            this.toC = toC;
        }

        public String getUnionid() {
            return unionid;
        }

        public void setUnionid(String unionid) {
            this.unionid = unionid;
        }

        public String getRcToken() {
            return rcToken;
        }

        public void setRcToken(String rcToken) {
            this.rcToken = rcToken;
        }

        public ToBBean getToB() {
            return toB;
        }

        public void setToB(ToBBean toB) {
            this.toB = toB;
        }

        public String getAdviserRealName() {
            return adviserRealName;
        }

        public void setAdviserRealName(String adviserRealName) {
            this.adviserRealName = adviserRealName;
        }

        public String getAdviserPhone() {
            return adviserPhone;
        }

        public void setAdviserPhone(String adviserPhone) {
            this.adviserPhone = adviserPhone;
        }

        public String getMemoToMember() {
            return memoToMember;
        }

        public void setMemoToMember(String memoToMember) {
            this.memoToMember = memoToMember;
        }

        public String getEducation() {
            return education;
        }

        public void setEducation(String education) {
            this.education = education;
        }

        public String getAuthenticationType() {
            return authenticationType;
        }

        public void setAuthenticationType(String authenticationType) {
            this.authenticationType = authenticationType;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPhoneNum() {
            return phoneNum;
        }

        public void setPhoneNum(String phoneNum) {
            this.phoneNum = phoneNum;
        }

        public String getResidentCity() {
            return residentCity;
        }

        public void setResidentCity(String residentCity) {
            this.residentCity = residentCity;
        }

        public String getIsSingIn() {
            return isSingIn;
        }

        public void setIsSingIn(String isSingIn) {
            this.isSingIn = isSingIn;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getAgeStage() {
            return ageStage;
        }

        public void setAgeStage(String ageStage) {
            this.ageStage = ageStage;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getRealName() {
            return realName;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }

        public String getMyPoint() {
            return myPoint;
        }

        public void setMyPoint(String myPoString) {
            this.myPoint = myPoint;
        }

        public String getFatherId() {
            return fatherId;
        }

        public void setFatherId(String fatherId) {
            this.fatherId = fatherId;
        }

        public String getLastLogStringime() {
            return lastLogStringime;
        }

        public void setLastLogStringime(String lastLogStringime) {
            this.lastLogStringime = lastLogStringime;
        }

        public String getMemoToFather() {
            return memoToFather;
        }

        public void setMemoToFather(String memoToFather) {
            this.memoToFather = memoToFather;
        }

        public String getUserNewId() {
            return userNewId;
        }

        public void setUserNewId(String userNewId) {
            this.userNewId = userNewId;
        }
    }

    public static class ToBBean {
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

    public static class ToCBean {
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
        public String gesturePassword;
        private String gestureSwitch;

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

        public String getGesturePassword() {
            return gesturePassword;
        }

        public void setGesturePassword(String gesturePassword) {
            this.gesturePassword = gesturePassword;
        }

        public String getGestureSwitch() {
            return gestureSwitch;
        }

        public void setGestureSwitch(String gestureSwitch) {
            this.gestureSwitch = gestureSwitch;
        }

        public String getBindTeacher(){
            if (!TextUtils.isEmpty(bandingAdviserId)){
                return "1";
            }
            return "0";
        }
    }
}
