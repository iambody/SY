package com.cgbsoft.privatefund.bean.ocr;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/10/19-15:15
 */
public class LivingSign {


    private String sign;//": "51F3BA49118C5EA066FDC46CABFB8DEA6A0CEEB1",
    private String licence;//": "iLEAoljNuRLoJbnYEGAnk57xVJ1Xl8YpWRRT/ljdRjzbhKG3M13DVHTlSwCp4hvwkMVVBTeVm5n6VUKA1xz8QKuXNamk6CVQwVAWhVV0CXt0ETjEknIavp5KPBh7ZDfPAESKR/y7T0JCcOgBxgj03Tx0IB8+J6/T6XC5X6czGPlCaG+WHBTRmmxZgXZPL8aJUf/Bk7dAa74J+Lez9e1znp3fzQ22f/6PgsZbkG21+H3a1B6YODmNy7uB4HVfI09w8ZuC88Rv+BjWkD9SIfHDJLeDGytcW142FDYqq9UTiKSCiliCob42IToTJBXpOnw4OHMKBqShiyQdiFC6a9TI9w==",
    private String appId;//": "TIDAdipj",
    private String nonce;//": "2e93c3b4cecb44f095dcf5b9214d39e6",
    private String orderNum;//": "24ac272fe92d4fa29802ca431378e1a5"
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getLicence() {
        return licence;
    }

    public void setLicence(String licence) {
        this.licence = licence;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }
}
