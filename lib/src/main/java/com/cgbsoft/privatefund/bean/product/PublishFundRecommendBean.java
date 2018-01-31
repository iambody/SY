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
    private String fundtype;

    private String leftUpValue;
    private String leftDownDes;
    private String rightUpValue;

    private String rightDownDes;
    //1.30 15:00 新增
    private String sharetype;////份额类别 'A'前收费 'B' 后收费
    private String tano;//ta代码
    private String certificatetype;////证件类型
    private String certificateno;////身份证号 (证件号)
    private String depositacctname;////客户姓名
    private String businesscode;// //业务类型，认购或申购
    private String buyflag;// / 制购买标志，1为强制购买
    private String callbackurl;
    private String depositacct;// //客户银行卡号
    private String custrisk;// 客户的风险等级！！！ 不为空就是风险风等级   1:安全型 2:保守型 3:稳健型 4:积极 型 5:进取型)
    private String risklevel;//产品的风险等级！！

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

    public String getFundtype() {
        return fundtype;
    }

    public void setFundtype(String fundtype) {
        this.fundtype = fundtype;
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

    public String getSharetype() {
        return sharetype;
    }

    public void setSharetype(String sharetype) {
        this.sharetype = sharetype;
    }

    public String getTano() {
        return tano;
    }

    public void setTano(String tano) {
        this.tano = tano;
    }

    public String getCertificatetype() {
        return certificatetype;
    }

    public void setCertificatetype(String certificatetype) {
        this.certificatetype = certificatetype;
    }

    public String getCertificateno() {
        return certificateno;
    }

    public void setCertificateno(String certificateno) {
        this.certificateno = certificateno;
    }

    public String getDepositacctname() {
        return depositacctname;
    }

    public void setDepositacctname(String depositacctname) {
        this.depositacctname = depositacctname;
    }

    public String getBusinesscode() {
        return businesscode;
    }

    public void setBusinesscode(String businesscode) {
        this.businesscode = businesscode;
    }

    public String getBuyflag() {
        return buyflag;
    }

    public void setBuyflag(String buyflag) {
        this.buyflag = buyflag;
    }

    public String getCallbackurl() {
        return callbackurl;
    }

    public void setCallbackurl(String callbackurl) {
        this.callbackurl = callbackurl;
    }

    public String getDepositacct() {
        return depositacct;
    }

    public void setDepositacct(String depositacct) {
        this.depositacct = depositacct;
    }

    public String getCustrisk() {
        return custrisk;
    }

    public void setCustrisk(String custrisk) {
        this.custrisk = custrisk;
    }

    public String getRisklevel() {
        return risklevel;
    }

    public void setRisklevel(String risklevel) {
        this.risklevel = risklevel;
    }
}
