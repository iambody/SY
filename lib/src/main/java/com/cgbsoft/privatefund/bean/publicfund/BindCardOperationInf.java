package com.cgbsoft.privatefund.bean.publicfund;

/**
 * desc  ${DESC}
 * author wangyongkui
 */
public class BindCardOperationInf {
    String bindCardTipInfo;//":"运营信息",

    String bindCardPhoneIntroductionInfo;//":"预留手机号说明",

    String bindCardCardholderExplanation;//":"持卡人说明",

    String passwordSetupInfo;//":"请设置6位数字交易密码",

    String passwordSetupPswMessage;//":"该密码用于公募基金购买、卖出使用",

    String passwordAffirmBtnTitle;//":"提交按钮信息",

    public String getBindCardTipInfo() {
        return bindCardTipInfo;
    }

    public void setBindCardTipInfo(String bindCardTipInfo) {
        this.bindCardTipInfo = bindCardTipInfo;
    }

    public String getBindCardPhoneIntroductionInfo() {
        return bindCardPhoneIntroductionInfo;
    }

    public void setBindCardPhoneIntroductionInfo(String bindCardPhoneIntroductionInfo) {
        this.bindCardPhoneIntroductionInfo = bindCardPhoneIntroductionInfo;
    }

    public String getBindCardCardholderExplanation() {
        return bindCardCardholderExplanation;
    }

    public void setBindCardCardholderExplanation(String bindCardCardholderExplanation) {
        this.bindCardCardholderExplanation = bindCardCardholderExplanation;
    }

    public String getPasswordSetupInfo() {
        return passwordSetupInfo;
    }

    public void setPasswordSetupInfo(String passwordSetupInfo) {
        this.passwordSetupInfo = passwordSetupInfo;
    }

    public String getPasswordSetupPswMessage() {
        return passwordSetupPswMessage;
    }

    public void setPasswordSetupPswMessage(String passwordSetupPswMessage) {
        this.passwordSetupPswMessage = passwordSetupPswMessage;
    }

    public String getPasswordAffirmBtnTitle() {
        return passwordAffirmBtnTitle;
    }

    public void setPasswordAffirmBtnTitle(String passwordAffirmBtnTitle) {
        this.passwordAffirmBtnTitle = passwordAffirmBtnTitle;
    }
}
