package com.cgbsoft.lib.base.model.bean;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import com.cgbsoft.lib.utils.db.dao.DaoSession;
import com.cgbsoft.lib.utils.db.dao.ToBBeanDao;
import com.cgbsoft.lib.utils.db.dao.ToCBeanDao;
import com.cgbsoft.lib.utils.db.dao.UserInfoDao;

/**
 *  用户信息
 *  Created by xiaoyu.zhang on 2016/11/11 13:22
 *  Email:zhangxyfs@126.com
 *  
 */
@Entity
public class UserInfo {

    /**
     * birthday : 2016-10-18
     * sex : 女
     * headImageUrl : http://www.simuyun.com/peyunupload/userHeadImage/c9a7b3925b2f43fe8b818b76af3b489a_1472496774382.jpg
     * toC : {"customerName":"文杰","customerType":"2","stockAssetsTime":"","riskEvaluationIdnum":"","bandingAdviserId":"c659194159cc444091bc31df317671d3","riskEvaluationPhone":"","bandingTime":"2016-10-21 10:04:35","customerState":"1","customerIdPhoto":"","investmentType":"1","customerPhone":"18210735626","stockAssetsId":"622738392916193972","assetsCertificationImage":"","riskEvaluationName":"","customerIdNumber":"622738392916193972","customerIdType":"军官证","stockAssetsStatus":0,"assetsCertificationStatus":0,"isEvaluated":1,"customerRiskEvaluation":"C,A,B,A,B,C,A,B,C,A,C,A,B,B","stockAssetsImage":"http://www.simuyun.com/peyunupload//ftp/peyunapp/upload//2016-07-21/cf09acb0931a404eaac2ad90e84e318e.jpg"}
     * unionid :
     * rcToken : AKr5olKnM9i0iVz5gFcLZdEMZH+vxGatN7TgU20dA516RjxfpQogInYmz6h6BfrBOWMX0dcjWEgdcRswQwiOvmI2yxlQe1VckHtc2lXnqXKzCgNDPnyGuqCHlhDhXNt/CPiShEeBBl8=
     * toB : {"organizationName":"润强投资","preparedforNum":0,"submitTime":"2016-08-27 11:47:27","teamNum":101,"organizationId":"1c8434df72ba4dffb6058d9e5054d4d7","beatRank":0,"adviserPhoto":"","adviserStatus":0,"adviserOrganization":"","adviserName":"","adviserLevel":6,"isEmployee":"1","adviserNumber":"","completedOrderCount":0,"rejectReason":"到审核期限未处理，系统自动驳回","serviceCheckStatus":0,"adviserPerformance":"","adviserState":"4","category":"5","adviserRank":"","myCustomersCount":1,"authUpdateTime":"2016-08-27 11:47:27","inviteCode":"与光明同行，与优秀相伴！我是winjay，我在私募云，邀请您加入我的团队，请复制此文本，打开??私募云App??，快速认证理财师，让我带你飞！????￥null￥????","completedOrderAmountCount":"","isExtension":"1"}
     * adviserRealName : 林功尧
     * adviserPhone : 13501232940
     * memoToMember :
     * education :
     * authenticationType : 0
     * id : c9a7b3925b2f43fe8b818b76af3b489a
     * phoneNum : 18210735626
     * residentCity :
     * isSingIn : 0
     * email : luwenjie@simuyun.com
     * nickName : winjay
     * ageStage :
     * userName : 18210735626
     * realName : winjay
     * myPoint : 861
     * fatherId : c659194159cc444091bc31df317671d3
     * lastLoginTime : 2016-11-11 11:02:08
     * memoToFather :
     */

    private String birthday;
    private String sex;
    private String headImageUrl;
    @ToOne
    private ToCBean toC;
    private String unionid;
    private String rcToken;
    @ToOne
    private ToBBean toB;
    private String adviserRealName;
    private String adviserPhone;
    private String memoToMember;
    private String education;
    private String authenticationType;
    @Id
    private String id;
    private String phoneNum;
    private String residentCity;
    private int isSingIn;
    private String email;
    private String nickName;
    private String ageStage;
    private String userName;
    private String realName;
    private int myPoint;
    private String fatherId;
    private String lastLoginTime;
    private String memoToFather;
    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /**
     * Used for active entity operations.
     */
    @Generated(hash = 437952339)
    private transient UserInfoDao myDao;

    @Generated(hash = 1995299261)
    public UserInfo(String birthday, String sex, String headImageUrl, String unionid, String rcToken, String adviserRealName, String adviserPhone, String memoToMember, String education, String authenticationType, String id, String phoneNum, String residentCity, int isSingIn, String email, String nickName, String ageStage, String userName, String realName, int myPoint, String fatherId, String lastLoginTime, String memoToFather) {
        this.birthday = birthday;
        this.sex = sex;
        this.headImageUrl = headImageUrl;
        this.unionid = unionid;
        this.rcToken = rcToken;
        this.adviserRealName = adviserRealName;
        this.adviserPhone = adviserPhone;
        this.memoToMember = memoToMember;
        this.education = education;
        this.authenticationType = authenticationType;
        this.id = id;
        this.phoneNum = phoneNum;
        this.residentCity = residentCity;
        this.isSingIn = isSingIn;
        this.email = email;
        this.nickName = nickName;
        this.ageStage = ageStage;
        this.userName = userName;
        this.realName = realName;
        this.myPoint = myPoint;
        this.fatherId = fatherId;
        this.lastLoginTime = lastLoginTime;
        this.memoToFather = memoToFather;
    }

    @Generated(hash = 1279772520)
    public UserInfo() {
    }

    public String getBirthday() {
        return this.birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getSex() {
        return this.sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getHeadImageUrl() {
        return this.headImageUrl;
    }

    public void setHeadImageUrl(String headImageUrl) {
        this.headImageUrl = headImageUrl;
    }

    public String getUnionid() {
        return this.unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public String getRcToken() {
        return this.rcToken;
    }

    public void setRcToken(String rcToken) {
        this.rcToken = rcToken;
    }

    public String getAdviserRealName() {
        return this.adviserRealName;
    }

    public void setAdviserRealName(String adviserRealName) {
        this.adviserRealName = adviserRealName;
    }

    public String getAdviserPhone() {
        return this.adviserPhone;
    }

    public void setAdviserPhone(String adviserPhone) {
        this.adviserPhone = adviserPhone;
    }

    public String getMemoToMember() {
        return this.memoToMember;
    }

    public void setMemoToMember(String memoToMember) {
        this.memoToMember = memoToMember;
    }

    public String getEducation() {
        return this.education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getAuthenticationType() {
        return this.authenticationType;
    }

    public void setAuthenticationType(String authenticationType) {
        this.authenticationType = authenticationType;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhoneNum() {
        return this.phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getResidentCity() {
        return this.residentCity;
    }

    public void setResidentCity(String residentCity) {
        this.residentCity = residentCity;
    }

    public int getIsSingIn() {
        return this.isSingIn;
    }

    public void setIsSingIn(int isSingIn) {
        this.isSingIn = isSingIn;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickName() {
        return this.nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAgeStage() {
        return this.ageStage;
    }

    public void setAgeStage(String ageStage) {
        this.ageStage = ageStage;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRealName() {
        return this.realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public int getMyPoint() {
        return this.myPoint;
    }

    public void setMyPoint(int myPoint) {
        this.myPoint = myPoint;
    }

    public String getFatherId() {
        return this.fatherId;
    }

    public void setFatherId(String fatherId) {
        this.fatherId = fatherId;
    }

    public String getLastLoginTime() {
        return this.lastLoginTime;
    }

    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getMemoToFather() {
        return this.memoToFather;
    }

    public void setMemoToFather(String memoToFather) {
        this.memoToFather = memoToFather;
    }

    @Generated(hash = 1107984292)
    private transient boolean toC__refreshed;

    /**
     * To-one relationship, resolved on first access.
     */
    @Generated(hash = 1388221563)
    public ToCBean getToC() {
        if (toC != null || !toC__refreshed) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ToCBeanDao targetDao = daoSession.getToCBeanDao();
            targetDao.refresh(toC);
            toC__refreshed = true;
        }
        return toC;
    }

    /**
     * To-one relationship, returned entity is not refreshed and may carry only the PK property.
     */
    @Generated(hash = 2057282853)
    public ToCBean peakToC() {
        return toC;
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 1772577376)
    public void setToC(ToCBean toC) {
        synchronized (this) {
            this.toC = toC;
            toC__refreshed = true;
        }
    }

    @Generated(hash = 1064926403)
    private transient boolean toB__refreshed;

    /**
     * To-one relationship, resolved on first access.
     */
    @Generated(hash = 799395230)
    public ToBBean getToB() {
        if (toB != null || !toB__refreshed) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ToBBeanDao targetDao = daoSession.getToBBeanDao();
            targetDao.refresh(toB);
            toB__refreshed = true;
        }
        return toB;
    }

    /**
     * To-one relationship, returned entity is not refreshed and may carry only the PK property.
     */
    @Generated(hash = 1308781770)
    public ToBBean peakToB() {
        return toB;
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 980058782)
    public void setToB(ToBBean toB) {
        synchronized (this) {
            this.toB = toB;
            toB__refreshed = true;
        }
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 821180768)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getUserInfoDao() : null;
    }
}
