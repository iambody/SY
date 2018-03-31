package com.cgbsoft.privatefund.bean.product;

/**
 * desc  ${DESC}
 * author wangyongkui
 */
public class PublishFundRecommendBean {

    private String isHaveAccount;
    private String fundCode;
    private String fundName;
    private String fundDes;
    private String fundType;

    private String leftUpValue;
    private String leftDownDes;
    private String rightUpValue;
    private String rightDownDes;
    //1.30 15:00 新增
    private String shareType;////份额类别 'A'前收费 'B' 后收费
    private String taNo;//ta代码
    private String certificateType;////证件类型
    private String certificateNo;////身份证号 (证件号)
    private String depositacctName;////客户姓名
    private String businessCode;// //业务类型，认购或申购
    private String buyFlag;// / 制购买标志，1为强制购买
    private String callbackUrl;
    private String depositAcct;// //客户银行卡号
    private String custRisk;// 客户的风险等级！！！ 不为空就是风险风等级   1:安全型 2:保守型 3:稳健型 4:积极 型 5:进取型)
    private String riskLevel;//产品的风险等级！！

    public String getIsHaveAccount() {
        return isHaveAccount;
    }

    public void setIsHaveAccount(String isHaveAccount) {
        this.isHaveAccount = isHaveAccount;
    }

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public String getFundName() {
        return fundName;
    }

    public void setFundName(String fundName) {
        this.fundName = fundName;
    }

    public String getFundDes() {
        return fundDes;
    }

    public void setFundDes(String fundDes) {
        this.fundDes = fundDes;
    }

    public String getFundType() {
        return fundType;
    }

    public void setFundType(String fundType) {
        this.fundType = fundType;
    }

    public String getLeftUpValue() {
        return leftUpValue;
    }

    public void setLeftUpValue(String leftUpValue) {
        this.leftUpValue = leftUpValue;
    }

    public String getLeftDownDes() {
        return leftDownDes;
    }

    public void setLeftDownDes(String leftDownDes) {
        this.leftDownDes = leftDownDes;
    }

    public String getRightUpValue() {
        return rightUpValue;
    }

    public void setRightUpValue(String rightUpValue) {
        this.rightUpValue = rightUpValue;
    }

    public String getRightDownDes() {
        return rightDownDes;
    }

    public void setRightDownDes(String rightDownDes) {
        this.rightDownDes = rightDownDes;
    }

    public String getShareType() {
        return shareType;
    }

    public void setShareType(String shareType) {
        this.shareType = shareType;
    }

    public String getTaNo() {
        return taNo;
    }

    public void setTaNo(String taNo) {
        this.taNo = taNo;
    }

    public String getCertificateType() {
        return certificateType;
    }

    public void setCertificateType(String certificateType) {
        this.certificateType = certificateType;
    }

    public String getCertificateNo() {
        return certificateNo;
    }

    public void setCertificateNo(String certificateNo) {
        this.certificateNo = certificateNo;
    }

    public String getDepositacctName() {
        return depositacctName;
    }

    public void setDepositacctName(String depositacctName) {
        this.depositacctName = depositacctName;
    }

    public String getBusinessCode() {
        return businessCode;
    }

    public void setBusinessCode(String businessCode) {
        this.businessCode = businessCode;
    }

    public String getBuyFlag() {
        return buyFlag;
    }

    public void setBuyFlag(String buyFlag) {
        this.buyFlag = buyFlag;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public String getDepositAcct() {
        return depositAcct;
    }

    public void setDepositAcct(String depositAcct) {
        this.depositAcct = depositAcct;
    }

    public String getCustRisk() {
        return custRisk;
    }

    public void setCustRisk(String custRisk) {
        this.custRisk = custRisk;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }
}
