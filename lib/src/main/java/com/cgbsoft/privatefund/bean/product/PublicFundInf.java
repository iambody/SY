package com.cgbsoft.privatefund.bean.product;

/**
 * desc  ${DESC}
 * author wangyongkui
 */
public class PublicFundInf {
    private String custno;
    private String mobileno;
    private String isHaveCustBankAcct;//0：未绑卡1：已绑卡
    private String custRisk;//01：安逸型 02：保守型 03：稳健型 04：积极型 05：激进型 空表示无


    private String certificateno;//= 220301197912013513;//证件号

    private String certificatetype;//= 0;//（证件类型 个人证件类型 0-身份证 1-护 照 2-军官证 3-士兵证 4-回乡证 5-户 口本 6-外国护照 7-其它 8-文职证 9- 警官证; 机构证件类型 0-技术监督局代 码 1-营业执照 2-行政机关 3-社会团体 4-军队 5-武警 6-下属机构(具有主管单 位批文号) 7-基金会 8-其它，目前可写死0 ）


    private String custfullname;// = ""; 客户名字

    private String depositacct;// = "";客户银行卡号


    public String getCustno() {
        return custno;
    }

    public void setCustno(String custno) {
        this.custno = custno;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
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

    public String getCertificateno() {
        return certificateno;
    }

    public void setCertificateno(String certificateno) {
        this.certificateno = certificateno;
    }

    public String getCertificatetype() {
        return certificatetype;
    }

    public void setCertificatetype(String certificatetype) {
        this.certificatetype = certificatetype;
    }

    public String getCustfullname() {
        return custfullname;
    }

    public void setCustfullname(String custfullname) {
        this.custfullname = custfullname;
    }

    public String getDepositacct() {
        return depositacct;
    }

    public void setDepositacct(String depositacct) {
        this.depositacct = depositacct;
    }


}
