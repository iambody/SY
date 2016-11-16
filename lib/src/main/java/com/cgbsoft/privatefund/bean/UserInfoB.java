package com.cgbsoft.privatefund.bean;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * Created by lee on 2016/9/10.
 */
public class UserInfoB implements Serializable {

    private String completedOrderCount;
    private String rejectReason;
    private int serviceCheckStatus;
    private String organizationName;
    private String preparedforNum;
    private String submitTimeprivate;
    private String teamNum;
    private String organizationId;
    private String adviserPhoto;
    private String adviserState;
    private int adviserStatus;
    private String adviserOrganization;
    private String myCustomersCount;
    private String authUpdateTime;
    private String adviserName;
    private String inviteCode;
    private String adviserLevel;
    private String completedOrderAmountCount;
    private String isExtension;
    private String isEmployee;
    private String adviserNumber;
    private String category;
    // 理财师
    private String beatRank;


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String isColorCloud(){
        if (TextUtils.isEmpty(category)){
            return "0";//非采云
        }else if (category.equals("3")){
            return "1";//采云
        }
        return "0";
    }

    public String getCompletedOrderCount() {
        return completedOrderCount;
    }

    public void setCompletedOrderCount(String completedOrderCount) {
        this.completedOrderCount = completedOrderCount;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }

    public int getServiceCheckStatus() {
        return serviceCheckStatus;
    }

    public void setServiceCheckStatus(int serviceCheckStatus) {
        this.serviceCheckStatus = serviceCheckStatus;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getPreparedforNum() {
        return preparedforNum;
    }

    public void setPreparedforNum(String preparedforNum) {
        this.preparedforNum = preparedforNum;
    }

    public String getSubmitTimeprivate() {
        return submitTimeprivate;
    }

    public void setSubmitTimeprivate(String submitTimeprivate) {
        this.submitTimeprivate = submitTimeprivate;
    }

    public String getTeamNum() {
        return teamNum;
    }


    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getAdviserPhoto() {
        return adviserPhoto;
    }

    public void setAdviserPhoto(String adviserPhoto) {
        this.adviserPhoto = adviserPhoto;
    }

    public String getAdviserState() {
        return adviserState;
    }

    public void setAdviserState(String adviserState) {
        this.adviserState = adviserState;
    }

    public int getAdviserStatus() {
        return adviserStatus;
    }

    public void setAdviserStatus(int adviserStatus) {
        this.adviserStatus = adviserStatus;
    }

    public String getAdviserOrganization() {
        return adviserOrganization;
    }

    public void setAdviserOrganization(String adviserOrganization) {
        this.adviserOrganization = adviserOrganization;
    }

    public String getMyCustomersCount() {
        return myCustomersCount;
    }

    public void setMyCustomersCount(String myCustomersCount) {
        this.myCustomersCount = myCustomersCount;
    }

    public String getAuthUpdateTime() {
        return authUpdateTime;
    }

    public void setAuthUpdateTime(String authUpdateTime) {
        this.authUpdateTime = authUpdateTime;
    }

    public String getAdviserName() {
        return adviserName;
    }

    public void setAdviserName(String adviserName) {
        this.adviserName = adviserName;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public String getAdviserLevel() {
        return adviserLevel;
    }

    public void setAdviserLevel(String adviserLevel) {
        this.adviserLevel = adviserLevel;
    }

    public String getCompletedOrderAmountCount() {
        return completedOrderAmountCount;
    }

    public void setCompletedOrderAmountCount(String completedOrderAmountCount) {
        this.completedOrderAmountCount = completedOrderAmountCount;
    }

    public String getIsExtension() {
        return isExtension;
    }

    public void setIsExtension(String isExtension) {
        this.isExtension = isExtension;
    }

    public String getIsEmployee() {
        return isEmployee;
    }

    public void setIsEmployee(String isEmployee) {
        this.isEmployee = isEmployee;
    }

    public String getAdviserNumber() {
        return adviserNumber;
    }

    public void setAdviserNumber(String adviserNumber) {
        this.adviserNumber = adviserNumber;
    }

    public String getBeatRank() {
        return beatRank;
    }

    public void setBeatRank(String beatRank) {
        this.beatRank = beatRank;
    }
}
