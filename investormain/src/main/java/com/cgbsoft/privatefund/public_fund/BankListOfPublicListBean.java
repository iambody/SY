package com.cgbsoft.privatefund.public_fund;

/**
 * Created by wangpeng on 18-1-29.
 */

public class BankListOfPublicListBean {
    private String authenticateflag; //鉴权标志(0 未鉴权,1 已鉴权)[110027]
    private String bankname; //银行账户地址全称
    private String branchcode; // 托管网点号
    private String cardtelno; // 银行卡预留手机号
    private String channelid; // 支付网点代码[110080]
    private String custno; // 客户号
    private String depositacct; // 系统外资金账号全称(投资人)
    private String isopenmobiletrade; // 是否支持手机交易网点
    private String moneyaccount; // 资金账户
    private String paycenterid; // 支付网点所属中心
    private int status; // 状态
    private String transactionaccountid; // 交易账号

    public String getAuthenticateflag() {
        return authenticateflag;
    }

    public void setAuthenticateflag(String authenticateflag) {
        this.authenticateflag = authenticateflag;
    }

    public String getBankname() {
        return bankname;
    }

    public void setBankname(String bankname) {
        this.bankname = bankname;
    }

    public String getBranchcode() {
        return branchcode;
    }

    public void setBranchcode(String branchcode) {
        this.branchcode = branchcode;
    }

    public String getCardtelno() {
        return cardtelno;
    }

    public void setCardtelno(String cardtelno) {
        this.cardtelno = cardtelno;
    }

    public String getChannelid() {
        return channelid;
    }

    public void setChannelid(String channelid) {
        this.channelid = channelid;
    }

    public String getCustno() {
        return custno;
    }

    public void setCustno(String custno) {
        this.custno = custno;
    }

    public String getDepositacct() {
        return depositacct;
    }

    public void setDepositacct(String depositacct) {
        this.depositacct = depositacct;
    }

    public String getIsopenmobiletrade() {
        return isopenmobiletrade;
    }

    public void setIsopenmobiletrade(String isopenmobiletrade) {
        this.isopenmobiletrade = isopenmobiletrade;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTransactionaccountid() {
        return transactionaccountid;
    }

    public void setTransactionaccountid(String transactionaccountid) {
        this.transactionaccountid = transactionaccountid;
    }

    @Override
    public String toString() {
        return "BankListOfPublicListBean{" +
                "authenticateflag='" + authenticateflag + '\'' +
                ", bankname='" + bankname + '\'' +
                ", branchcode='" + branchcode + '\'' +
                ", cardtelno='" + cardtelno + '\'' +
                ", channelid='" + channelid + '\'' +
                ", custno='" + custno + '\'' +
                ", depositacct='" + depositacct + '\'' +
                ", isopenmobiletrade='" + isopenmobiletrade + '\'' +
                ", moneyaccount='" + moneyaccount + '\'' +
                ", paycenterid='" + paycenterid + '\'' +
                ", status=" + status +
                ", transactionaccountid='" + transactionaccountid + '\'' +
                '}';
    }
}
