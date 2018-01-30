package com.cgbsoft.privatefund.bean.product;

/**
 * desc  ${DESC}
 * author wangyongkui
 */
public class PublishFundRecommendBean {

    private String isHaveAccount;
    private String fundCode;
    private String fundName;
    private String fundDes;
    private String fundtype;

    private String leftUpValue;
    private String  leftDownDes;
    private String  rightUpValue;

    private String rightDownDes;


    public String getIsHaveAccount() {
        return isHaveAccount;
    }

    public void setIsHaveAccount(String isHaveAccount) {
        this.isHaveAccount = isHaveAccount;
    }

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public String getFundName() {
        return fundName;
    }

    public void setFundName(String fundName) {
        this.fundName = fundName;
    }

    public String getFundDes() {
        return fundDes;
    }

    public void setFundDes(String fundDes) {
        this.fundDes = fundDes;
    }

    public String getFundtype() {
        return fundtype;
    }

    public void setFundtype(String fundtype) {
        this.fundtype = fundtype;
    }

    public String getLeftUpValue() {
        return leftUpValue;
    }

    public void setLeftUpValue(String leftUpValue) {
        this.leftUpValue = leftUpValue;
    }

    public String getLeftDownDes() {
        return leftDownDes;
    }

    public void setLeftDownDes(String leftDownDes) {
        this.leftDownDes = leftDownDes;
    }

    public String getRightUpValue() {
        return rightUpValue;
    }

    public void setRightUpValue(String rightUpValue) {
        this.rightUpValue = rightUpValue;
    }

    public String getRightDownDes() {
        return rightDownDes;
    }

    public void setRightDownDes(String rightDownDes) {
        this.rightDownDes = rightDownDes;
    }
}
