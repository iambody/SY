package com.cgbsoft.privatefund.bean;

/**
 * @author chenlong
 */
public class BindBankCardInfoBean {

    private String bankname;

    private String channelid;

    private String depositacct;

    private String icon;

    private String background;

    public String getDepositacct() {
        return depositacct;
    }

    public void setDepositacct(String depositacct) {
        this.depositacct = depositacct;
    }

    public String getBankname() {
        return bankname;
    }

    public void setBankname(String bankname) {
        this.bankname = bankname;
    }

    public String getChannelid() {
        return channelid;
    }

    public void setChannelid(String channelid) {
        this.channelid = channelid;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }
}
