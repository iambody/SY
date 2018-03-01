package com.cgbsoft.privatefund.public_fund;

/**
 * Created by wangpeng on 18-1-29.
 */

public class PayOfBuyPublicBean{
    private String trantype;
    private String applicationamt; // 输入的金额
    private String tpasswd; // 交易密码
    // 以下由H5提供
    private String custno; //客户号
    private String fundcode;  //基金代码（H5调取app指令的时候会传入）
    private String fundname; // 基金名字
    private String sharetype;//份额类别 'A'先收费 'B' 后收费（H5调取app指令的时候会传入）
    private String tano; //TA代码（H5调取app指令的时候会传入）
    private String certificatetype;//证件类型（H5调取app指令的时候会传入）
    private String certificateno; //身份证号（H5调取app指令的时候会传入）
    private String depositacctname; //客户姓名（H5调取app指令的时候会传入）
    private String businesscode; //业务类型，认购或申购（H5调取app指令的时候会传入）
    private String callbackurl= ""; //银行回调地址，留空即可
    private String depositacct; // 客户银行卡号
    private String fundtype; //基金类型，0-股票型基金 1-债券型基金 2-货币型基 金 3-混合型基金 4-专户基金 5-指数型基金 6-QDII 基金（H5调取app指令的时候会传入）
    private String mobiletelno; // 客户手机号

    // 以下向服务器请求银行卡列表数据，然后从中获取
    private String transactionaccountid;//份额托管网点编号（从银行卡列表信息中获取）
    private String channelid; //'支付网点id'
    private String branchcode; // '份额托管网点编号'
    private String moneyaccount;//资金账户（从银行卡列表信息中获取）
    private String paycenterid;//支付网点所属中心（从银行卡列表信息中获取）
    private String riskwarnflag = "1"; //已经阅读风险提示标志，传死1即可

    public String getTrantype() {
        return trantype;
    }

    public void setTrantype(String trantype) {
        this.trantype = trantype;
    }

    public String getApplicationamt() {
        return applicationamt;
    }

    public void setApplicationamt(String applicationamt) {
        this.applicationamt = applicationamt;
    }

    public String getTpasswd() {
        return tpasswd;
    }

    public void setTpasswd(String tpasswd) {
        this.tpasswd = tpasswd;
    }

    public String getCustno() {
        return custno;
    }

    public void setCustno(String custno) {
        this.custno = custno;
    }

    public String getFundcode() {
        return fundcode;
    }

    public void setFundcode(String fundcode) {
        this.fundcode = fundcode;
    }

    public String getFundname() {
        return fundname;
    }

    public void setFundname(String fundname) {
        this.fundname = fundname;
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

    public String getFundtype() {
        return fundtype;
    }

    public void setFundtype(String fundtype) {
        this.fundtype = fundtype;
    }

    public String getMobiletelno() {
        return mobiletelno;
    }

    public void setMobiletelno(String mobiletelno) {
        this.mobiletelno = mobiletelno;
    }

    public String getTransactionaccountid() {
        return transactionaccountid;
    }

    public void setTransactionaccountid(String transactionaccountid) {
        this.transactionaccountid = transactionaccountid;
    }

    public String getChannelid() {
        return channelid;
    }

    public void setChannelid(String channelid) {
        this.channelid = channelid;
    }

    public String getBranchcode() {
        return branchcode;
    }

    public void setBranchcode(String branchcode) {
        this.branchcode = branchcode;
    }

    public String getMoneyaccount() {
        return moneyaccount;
    }

    public void setMoneyaccount(String moneyaccount) {
        this.moneyaccount = moneyaccount;
    }

    public String getPaycenterid() {
        return paycenterid;
    }

    public void setPaycenterid(String paycenterid) {
        this.paycenterid = paycenterid;
    }

    public String getRiskwarnflag() {
        return riskwarnflag;
    }

    public void setRiskwarnflag(String riskwarnflag) {
        this.riskwarnflag = riskwarnflag;
    }

    @Override
    public String toString() {
        return "PayOfBuyPublicBean{" +
                "trantype='" + trantype + '\'' +
                ", applicationamt='" + applicationamt + '\'' +
                ", tpasswd='" + tpasswd + '\'' +
                ", custno='" + custno + '\'' +
                ", fundcode='" + fundcode + '\'' +
                ", fundname='" + fundname + '\'' +
                ", sharetype='" + sharetype + '\'' +
                ", tano='" + tano + '\'' +
                ", certificatetype='" + certificatetype + '\'' +
                ", certificateno='" + certificateno + '\'' +
                ", depositacctname='" + depositacctname + '\'' +
                ", businesscode='" + businesscode + '\'' +
                ", callbackurl='" + callbackurl + '\'' +
                ", depositacct='" + depositacct + '\'' +
                ", fundtype='" + fundtype + '\'' +
                ", mobiletelno='" + mobiletelno + '\'' +
                ", transactionaccountid='" + transactionaccountid + '\'' +
                ", channelid='" + channelid + '\'' +
                ", branchcode='" + branchcode + '\'' +
                ", moneyaccount='" + moneyaccount + '\'' +
                ", paycenterid='" + paycenterid + '\'' +
                ", riskwarnflag='" + riskwarnflag + '\'' +
                '}';
    }
}
