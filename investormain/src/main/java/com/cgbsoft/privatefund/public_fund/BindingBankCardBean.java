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

    private String custNo = ""; // 客户号
    private String certificateType; //证件类型
    private String certificateNo=""; // 证件号
    private String depositName =""; // 客户名字
    private String depositAcctName =""; // 客户名字
    private String depositprov = ""; // 客户所在省
    private String depositcity = ""; // 客户所在城市
    private String banknameid = "";//

    private String channelid = ""; // 开户时的交易密码， 绑定银行是为空就可以
    private String bankname = "";//
    private String channelname = "";
    private String paratype = "";

    private String operOrg = "9999"; // 交易操作网点，写死9999就可以
    private String tPasswd = ""; // 开户时的交易密码， 绑定银行是为空就可以

    public String getCustNo() {
        return custNo;
    }

    public void setCustNo(String custNo) {
        this.custNo = custNo;
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

    public String getDepositName() {
        return depositName;
    }

    public void setDepositName(String depositName) {
        this.depositName = depositName;
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

    public String getDepositAcctName() {
        return depositAcctName;
    }

    public void setDepositAcctName(String depositAcctName) {
        this.depositAcctName = depositAcctName;
    }

    public String getBanknameid() {
        return banknameid;
    }

    public void setBanknameid(String banknameid) {
        this.banknameid = banknameid;
    }

    public String getChannelid() {
        return channelid;
    }

    public void setChannelid(String channelid) {
        this.channelid = channelid;
    }

    public String getBankname() {
        return bankname;
    }

    public void setBankname(String bankname) {
        this.bankname = bankname;
    }

    public String getChannelname() {
        return channelname;
    }

    public void setChannelname(String channelname) {
        this.channelname = channelname;
    }

    public String getParatype() {
        return paratype;
    }

    public void setParatype(String paratype) {
        this.paratype = paratype;
    }

    public String getOperOrg() {
        return operOrg;
    }

    public void setOperOrg(String operOrg) {
        this.operOrg = operOrg;
    }

    public String gettPasswd() {
        return tPasswd;
    }

    public void settPasswd(String tPasswd) {
        this.tPasswd = tPasswd;
    }
}
