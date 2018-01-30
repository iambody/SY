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
}
