package com.cgbsoft.privatefund.bean.publicfund;

/**
 * desc 根据银行卡号获取银行卡的信息
 * author wangyongkui
 */
public class BankBranchInf {

    String channelId;//":"金证中银行卡的渠道ID",
    String bankNameId;//去获取支行时候需要用到的

    String bankName;//":"银行卡名称",

    String paraCity;//":"归属地",

    String icon;//":"银行卡图标",

    String depositAcct;//":"银行卡号"

    String bankLimit;//提现说明

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getParaCity() {
        return paraCity;
    }

    public void setParaCity(String paraCity) {
        this.paraCity = paraCity;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDepositAcct() {
        return depositAcct;
    }

    public void setDepositAcct(String depositAcct) {
        this.depositAcct = depositAcct;
    }

    public String getBankLimit() {
        return bankLimit;
    }

    public void setBankLimit(String bankLimit) {
        this.bankLimit = bankLimit;
    }

    public String getBankNameId() {
        return bankNameId;
    }

    public void setBankNameId(String bankNameId) {
        this.bankNameId = bankNameId;
    }
}
