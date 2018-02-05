package com.cgbsoft.privatefund.public_fund;

/**
 * Created by wangpeng on 18-2-2.
 */

public class BindingBankCardBean {
    /*   "custno":"193",
                "certificatetype":"0",
                "certificateno":"110102200001018457",
                "depositacctname":"杨静",
                "authenticateflag":"1",
                "depositname":"杨静",
                "depositcity":" ",
                "depositprov":" ",
                "operorg":"9999",
                "tpasswd":"123456"*/

    private String custno = ""; // 客户号
    private String certificatetype; //证件类型
    private String certificateno=""; // 证件号
    private String depositacctname; // 客户名字
    private String depositname; // 客户名字
    private String depositprov = ""; // 客户所在省
    private String depositcity = ""; // 客户所在城市
    private String operorg = "9999"; // 交易操作网点，写死9999就可以
    private String tpasswd = ""; // 开户时的交易密码， 绑定银行是为空就可以
    private String channelid = ""; // 开户时的交易密码， 绑定银行是为空就可以

    public String getCustno() {
        return custno;
    }

    public void setCustno(String custno) {
        this.custno = custno;
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

    public String getDepositname() {
        return depositname;
    }

    public void setDepositname(String depositname) {
        this.depositname = depositname;
    }

    public String getDepositprov() {
        return depositprov;
    }

    public void setDepositprov(String depositprov) {
        this.depositprov = depositprov;
    }

    public String getDepositcity() {
        return depositcity;
    }

    public void setDepositcity(String depositcity) {
        this.depositcity = depositcity;
    }

    public String getOperorg() {
        return operorg;
    }

    public void setOperorg(String operorg) {
        this.operorg = operorg;
    }

    public String getTpasswd() {
        return tpasswd;
    }

    public void setTpasswd(String tpasswd) {
        this.tpasswd = tpasswd;
    }

    public String getChannelid() {
        return channelid;
    }

    public void setChannelid(String channelid) {
        this.channelid = channelid;
    }
}
