package com.cgbsoft.privatefund.public_fund;

/**
 * Created by wangpeng on 18-1-29.
 */

public class ReqeustListParmBean {
    private String trantype = "520102";// 接口号
    private String custno;// 客户号
    private String isall = "";// 客户号

    public String getTrantype() {
        return trantype;
    }

    public void setTrantype(String trantype) {
        this.trantype = trantype;
    }

    public String getCustno() {
        return custno;
    }

    public void setCustno(String custno) {
        this.custno = custno;
    }

    public String getIsall() {
        return isall;
    }

    public void setIsall(String isall) {
        this.isall = isall;
    }
}
