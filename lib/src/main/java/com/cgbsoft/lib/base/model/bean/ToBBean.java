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
public class ToBBean {
    /**
     * organizationName : 润强投资
     * preparedforNum : 0
     * submitTime : 2016-08-27 11:47:27
     * teamNum : 101
     * organizationId : 1c8434df72ba4dffb6058d9e5054d4d7
     * beatRank : 0
     * adviserPhoto :
     * adviserStatus : 0
     * adviserOrganization :
     * adviserName :
     * adviserLevel : 6
     * isEmployee : 1
     * adviserNumber :
     * completedOrderCount : 0
     * rejectReason : 到审核期限未处理，系统自动驳回
     * serviceCheckStatus : 0
     * adviserPerformance :
     * adviserState : 4
     * category : 5
     * adviserRank :
     * myCustomersCount : 1
     * authUpdateTime : 2016-08-27 11:47:27
     * inviteCode : 与光明同行，与优秀相伴！我是winjay，我在私募云，邀请您加入我的团队，请复制此文本，打开??私募云App??，快速认证理财师，让我带你飞！????￥null￥????
     * completedOrderAmountCount :
     * isExtension : 1
     */
    @Id
    public String id;

    private String organizationName;
    private int preparedforNum;
    private String submitTime;
    private int teamNum;
    private String organizationId;
    private int beatRank;
    private String adviserPhoto;
    private int adviserStatus;
    private String adviserOrganization;
    private String adviserName;
    private int adviserLevel;
    private String isEmployee;
    private String adviserNumber;
    private int completedOrderCount;
    private String rejectReason;
    private int serviceCheckStatus;
    private String adviserPerformance;
    private String adviserState;
    private String category;
    private String adviserRank;
    private int myCustomersCount;
    private String authUpdateTime;
    private String inviteCode;
    private String completedOrderAmountCount;
    private String isExtension;

    @Generated(hash = 67589438)
    public ToBBean(String id, String organizationName, int preparedforNum, String submitTime, int teamNum,
                   String organizationId, int beatRank, String adviserPhoto, int adviserStatus,
                   String adviserOrganization, String adviserName, int adviserLevel, String isEmployee,
                   String adviserNumber, int completedOrderCount, String rejectReason, int serviceCheckStatus,
                   String adviserPerformance, String adviserState, String category, String adviserRank,
                   int myCustomersCount, String authUpdateTime, String inviteCode, String completedOrderAmountCount,
                   String isExtension) {
        this.id = id;
        this.organizationName = organizationName;
        this.preparedforNum = preparedforNum;
        this.submitTime = submitTime;
        this.teamNum = teamNum;
        this.organizationId = organizationId;
        this.beatRank = beatRank;
        this.adviserPhoto = adviserPhoto;
        this.adviserStatus = adviserStatus;
        this.adviserOrganization = adviserOrganization;
        this.adviserName = adviserName;
        this.adviserLevel = adviserLevel;
        this.isEmployee = isEmployee;
        this.adviserNumber = adviserNumber;
        this.completedOrderCount = completedOrderCount;
        this.rejectReason = rejectReason;
        this.serviceCheckStatus = serviceCheckStatus;
        this.adviserPerformance = adviserPerformance;
        this.adviserState = adviserState;
        this.category = category;
        this.adviserRank = adviserRank;
        this.myCustomersCount = myCustomersCount;
        this.authUpdateTime = authUpdateTime;
        this.inviteCode = inviteCode;
        this.completedOrderAmountCount = completedOrderAmountCount;
        this.isExtension = isExtension;
    }

    @Generated(hash = 701101520)
    public ToBBean() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrganizationName() {
        return this.organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public int getPreparedforNum() {
        return this.preparedforNum;
    }

    public void setPreparedforNum(int preparedforNum) {
        this.preparedforNum = preparedforNum;
    }

    public String getSubmitTime() {
        return this.submitTime;
    }

    public void setSubmitTime(String submitTime) {
        this.submitTime = submitTime;
    }

    public int getTeamNum() {
        return this.teamNum;
    }

    public void setTeamNum(int teamNum) {
        this.teamNum = teamNum;
    }

    public String getOrganizationId() {
        return this.organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public int getBeatRank() {
        return this.beatRank;
    }

    public void setBeatRank(int beatRank) {
        this.beatRank = beatRank;
    }

    public String getAdviserPhoto() {
        return this.adviserPhoto;
    }

    public void setAdviserPhoto(String adviserPhoto) {
        this.adviserPhoto = adviserPhoto;
    }

    public int getAdviserStatus() {
        return this.adviserStatus;
    }

    public void setAdviserStatus(int adviserStatus) {
        this.adviserStatus = adviserStatus;
    }

    public String getAdviserOrganization() {
        return this.adviserOrganization;
    }

    public void setAdviserOrganization(String adviserOrganization) {
        this.adviserOrganization = adviserOrganization;
    }

    public String getAdviserName() {
        return this.adviserName;
    }

    public void setAdviserName(String adviserName) {
        this.adviserName = adviserName;
    }

    public int getAdviserLevel() {
        return this.adviserLevel;
    }

    public void setAdviserLevel(int adviserLevel) {
        this.adviserLevel = adviserLevel;
    }

    public String getIsEmployee() {
        return this.isEmployee;
    }

    public void setIsEmployee(String isEmployee) {
        this.isEmployee = isEmployee;
    }

    public String getAdviserNumber() {
        return this.adviserNumber;
    }

    public void setAdviserNumber(String adviserNumber) {
        this.adviserNumber = adviserNumber;
    }

    public int getCompletedOrderCount() {
        return this.completedOrderCount;
    }

    public void setCompletedOrderCount(int completedOrderCount) {
        this.completedOrderCount = completedOrderCount;
    }

    public String getRejectReason() {
        return this.rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }

    public int getServiceCheckStatus() {
        return this.serviceCheckStatus;
    }

    public void setServiceCheckStatus(int serviceCheckStatus) {
        this.serviceCheckStatus = serviceCheckStatus;
    }

    public String getAdviserPerformance() {
        return this.adviserPerformance;
    }

    public void setAdviserPerformance(String adviserPerformance) {
        this.adviserPerformance = adviserPerformance;
    }

    public String getAdviserState() {
        return this.adviserState;
    }

    public void setAdviserState(String adviserState) {
        this.adviserState = adviserState;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAdviserRank() {
        return this.adviserRank;
    }

    public void setAdviserRank(String adviserRank) {
        this.adviserRank = adviserRank;
    }

    public int getMyCustomersCount() {
        return this.myCustomersCount;
    }

    public void setMyCustomersCount(int myCustomersCount) {
        this.myCustomersCount = myCustomersCount;
    }

    public String getAuthUpdateTime() {
        return this.authUpdateTime;
    }

    public void setAuthUpdateTime(String authUpdateTime) {
        this.authUpdateTime = authUpdateTime;
    }

    public String getInviteCode() {
        return this.inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public String getCompletedOrderAmountCount() {
        return this.completedOrderAmountCount;
    }

    public void setCompletedOrderAmountCount(String completedOrderAmountCount) {
        this.completedOrderAmountCount = completedOrderAmountCount;
    }

    public String getIsExtension() {
        return this.isExtension;
    }

    public void setIsExtension(String isExtension) {
        this.isExtension = isExtension;
    }
}
