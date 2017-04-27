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
    }
}
