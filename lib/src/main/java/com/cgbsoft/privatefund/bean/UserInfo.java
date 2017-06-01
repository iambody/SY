package com.cgbsoft.privatefund.bean;

import java.io.Serializable;

public class UserInfo implements Serializable {

    private String completedOrderCount;

    private String organizationName;

    private String weChatNickName;

    private String organizationId;

    // 旧理财师状态 0:已认证 1:未认证 2:认证中 3:驳回
    private String adviserStatus;

    private String myCustomersCount;

    private String completedOrderAmountCount;

    private String isExtension;

    private int teamNum;

    private String inviteCode;

    private String hideInviteCode;

    //理财师认证状态 1.未认证 2.认证中 3.已通过  4.认证失败  新加的
    private String adviserState;

    //客户认证状态  1.未认证  2.认证中  3.已通过 4.失败
    private String customerState;

    //我的资产
    private String myAssetsNum;

    private String preparedforNum;

    private String liveName;

    //是否填过问卷
    private String isEvaluated;

    private String riskEvaluationPhone;

    private String riskEvaluationIdnum;

    private String riskEvaluationName;

    private String ydQuantity;

    private UserInfoB toB;

    private UserInfoC toC;

    private String birthday;

    private String sex;

    private String headImageUrl;

    private String unionid;

    private String rcToken;

    private String memoToMember;

    private String education;

    private String authenticationType;

    private String id;

    private String phoneNum;

    private int isSingIn;

    private String email;

    private String nickName;

    private String userName;

    private String realName;

    private int myPoint;

    private String fatherId;

    private String lastLoginTime;

    private String memoToFather;

    private String wechatUnionid;

    private String adviserRealName;

    private String adviserPhone;

    public void setIsEvaluated(String isEvaluated) {
        this.isEvaluated = isEvaluated;
    }

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

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFatherId() {
        return fatherId;
    }

    public void setFatherId(String fatherId) {
        this.fatherId = fatherId;
    }

    public String getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getMemoToFather() {
        return memoToFather;
    }

    public void setMemoToFather(String memoToFather) {
        this.memoToFather = memoToFather;
    }

    public UserInfoB getToB() {
        return toB;
    }

    public void setToB(UserInfoB toB) {
        this.toB = toB;
    }

    public UserInfoC getToC() {
        return toC;
    }

    public void setToC(UserInfoC toC) {
        this.toC = toC;
    }

    public String getRiskEvaluationPhone() {
        return toC.getRiskEvaluationPhone();
    }

    public void setRiskEvaluationPhone(String riskEvaluationPhone) {
        this.riskEvaluationPhone = riskEvaluationPhone;
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

    public String getRiskEvaluationIdnum() {
        return toC.getRiskEvaluationIdnum();
    }

    public void setRiskEvaluationIdnum(String riskEvaluationIdnum) {
        this.riskEvaluationIdnum = riskEvaluationIdnum;
    }

    public String getRiskEvaluationName() {
        return toC.getRiskEvaluationName();
    }

    public void setRiskEvaluationName(String riskEvaluationName) {
        this.riskEvaluationName = riskEvaluationName;
    }

    public String getIsEvaluated() {
        return toC.getIsEvaluated() + "";
    }

    public String getLiveName() {
        return liveName;
    }

    public void setLiveName(String liveName) {
        this.liveName = liveName;
    }

    public String getPreparedforNum() {
        return toB.getPreparedforNum();
    }

    public void setPreparedforNum(String preparedforNum) {
        this.preparedforNum = preparedforNum;
    }

    public String getMyAssetsNum() {
        return toC.getMyAssetsNum();
    }

    public String getAdviserState() {
        return toB.getAdviserState();
    }

    public void setAdviserState(String adviserState) {
        this.adviserState = adviserState;
    }

    public String getCustomerState() {
        return toC.getCustomerState();
    }

    public void setCustomerState(String customerState) {
        this.customerState = customerState;
    }

    public void setMyAssetsNum(String myAssetsNum) {

        this.myAssetsNum = myAssetsNum;
    }

    public String getAdviser_state() {
        return toB.getAdviserState();
    }

    public void setAdviser_state(String adviserState) {
        this.adviserState = adviserState;
    }

    public String getCustomer_cert_state() {
        return customerState;
    }

    public void setCustomer_cert_state(String customerState) {
        this.customerState = customerState;
    }

    public String getHideInviteCode() {
        return hideInviteCode;
    }

    public void setHideInviteCode(String hideInviteCode) {
        this.hideInviteCode = hideInviteCode;
    }

    public String getInviteCode() {
        return toB.getInviteCode();
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public String getRcToken() {
        return rcToken;
    }

    public void setRcToken(String rcToken) {
        this.rcToken = rcToken;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public int getTeamNum() {
        return Integer.parseInt(toB.getTeamNum());
    }

    public void setTeamNum(int teamNum) {
        this.teamNum = teamNum;
    }

    public String getMyCustomersCount() {
        return toB.getMyCustomersCount();
    }

    public void setMyCustomersCount(String myCustomersCount) {
        this.myCustomersCount = myCustomersCount;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getIsExtension() {
        return toB.getIsExtension();
    }

    public void setIsExtension(String isExtension) {
        this.isExtension = isExtension;
    }

    public void setHeadImageUrl(String headImageUrl) {
        this.headImageUrl = headImageUrl;
    }

    public String getHeadImgUrl() {
        return this.headImageUrl;
    }

    public void setCompletedOrderCount(String completedOrderCount) {
        this.completedOrderCount = completedOrderCount;
    }

    public String getCompletedOrderCount() {
        return toB.getCompletedOrderCount();
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getOrganizationName() {
        return toB.getOrganizationName();
    }

    public void setWeChatNickName(String weChatNickName) {
        this.weChatNickName = weChatNickName;
    }

    public String getWeChatNickName() {
        return this.weChatNickName;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getOrganizationId() {
        return toB.getOrganizationId();
    }

    public void setAdviserStatus(String adviserStatus) {
        this.adviserStatus = adviserStatus;
    }

    public String getAdviserStatus() {
        return toB.getAdviserStatus() + "";
    }

    public void setCustomersCount(String customersCount) {
        this.myCustomersCount = customersCount;
    }

    public String getCustomersCount() {
        return this.myCustomersCount;
    }

    public void setIsSingIn(int isSingIn) {
        this.isSingIn = isSingIn;
    }

    public int getIsSingIn() {
        return isSingIn;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getNickName() {
        return this.nickName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }

    public String getAdviserLevel() {
        return toB.getAdviserLevel();
    }

    public void setCompletedOrderAmountCount(String completedOrderAmountCount) {
        this.completedOrderAmountCount = completedOrderAmountCount;
    }

    public String getCompletedOrderAmountCount() {
        return toB.getCompletedOrderAmountCount();
    }

    public void setMyPoint(int myPoint) {
        this.myPoint = myPoint;
    }

    public int getMyPoint() {
        return this.myPoint;
    }

    public String getPhoneNumber() {
        return phoneNum;

    }

    public String getWechatUnionid() {
        return unionid;
    }

    public void setWechatUnionid(String wechatUnionid) {
        this.unionid = wechatUnionid;
    }
}
