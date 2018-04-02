package com.cgbsoft.privatefund.bean;

/**
 * @author chenlong
 */
public class BindBankCardInfoBean {

    private String channelid;

    private String background;

    private String bankEnableStatus;

    private String bankLimit;

    private String bankName;

    private String bankShortName;

    //银行卡号
    private String depositAcct;
    //持卡人姓名
    private String depositAcctName;

    private String icon;

    private String channeld;

    public String getChannelid() {
        return channelid;
    }

    public void setChannelid(String channelid) {
        this.channelid = channelid;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getBankEnableStatus() {
        return bankEnableStatus;
    }

    public void setBankEnableStatus(String bankEnableStatus) {
        this.bankEnableStatus = bankEnableStatus;
    }

    public String getBankLimit() {
        return bankLimit;
    }

    public void setBankLimit(String bankLimit) {
        this.bankLimit = bankLimit;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankShortName() {
        return bankShortName;
    }

    public void setBankShortName(String bankShortName) {
        this.bankShortName = bankShortName;
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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getChanneld() {
        return channeld;
    }

    public void setChanneld(String channeld) {
        this.channeld = channeld;
    }
}
