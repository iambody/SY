package com.cgbsoft.privatefund.bean.product;

/**
 * desc  ${DESC}
 * author wangyongkui
 */
public class PublicFundInf {
    private String custNo;
    private String mobileNo;
    private String isHaveCustBankAcct;//0：未绑卡1：已绑卡
    private String custRisk;//01：安逸型 02：保守型 03：稳健型 04：积极型 05：激进型 空表示无

    private String whiteUserListFlg; // 是否白名单 1 是 0 否

    private String certificateNo;//= 220301197912013513;//证件号

    private String certificateType;//= 0;//（证件类型 个人证件类型 0-身份证 1-护 照 2-军官证 3-士兵证 4-回乡证 5-户 口本 6-外国护照 7-其它 8-文职证 9- 警官证; 机构证件类型 0-技术监督局代 码 1-营业执照 2-行政机关 3-社会团体 4-军队 5-武警 6-下属机构(具有主管单 位批文号) 7-基金会 8-其它，目前可写死0 ）


    private String custFullName;// = ""; 客户名字

    private String depositAcct;// = "";客户银行卡号

    private String depositAcctName;//客户姓名

    private String type;//@永强添加 待定使用？？？？？？？？

    private String userId;
    private String depositName;
    public String getCustNo() {
        return custNo;
    }

    public void setCustNo(String custNo) {
        this.custNo = custNo;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getIsHaveCustBankAcct() {
        return isHaveCustBankAcct;
    }

    public void setIsHaveCustBankAcct(String isHaveCustBankAcct) {
        this.isHaveCustBankAcct = isHaveCustBankAcct;
    }

    public String getCustRisk() {
        return custRisk;
    }

    public void setCustRisk(String custRisk) {
        this.custRisk = custRisk;
    }

    public String getWhiteUserListFlg() {
        return whiteUserListFlg;
    }

    public void setWhiteUserListFlg(String whiteUserListFlg) {
        this.whiteUserListFlg = whiteUserListFlg;
    }

    public String getCertificateNo() {
        return certificateNo;
    }

    public void setCertificateNo(String certificateNo) {
        this.certificateNo = certificateNo;
    }

    public String getCertificateType() {
        return certificateType;
    }

    public void setCertificateType(String certificateType) {
        this.certificateType = certificateType;
    }

    public String getCustFullName() {
        return custFullName;
    }

    public void setCustFullName(String custFullName) {
        this.custFullName = custFullName;
    }

    public String getDepositAcct() {
        return depositAcct;
    }

    public void setDepositAcct(String depositAcct) {
        this.depositAcct = depositAcct;
    }

    public String getDepositAcctName() {
        return depositAcctName;
    }

    public void setDepositAcctName(String depositAcctName) {
        this.depositAcctName = depositAcctName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDepositName() {
        return depositName;
    }

    public void setDepositName(String depositName) {
        this.depositName = depositName;
    }
}
