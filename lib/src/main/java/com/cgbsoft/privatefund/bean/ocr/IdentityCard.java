package com.cgbsoft.privatefund.bean.ocr;

/**
 * desc 身份证有头像的数据javabean
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/10/18-11:08
 */
public class IdentityCard {

    private String idCardName;//": "李永强",
    private String idCardNum;//": "37148119901112251X"
    private String validDate;//": "2018-07-11"

    public String getIdCardName() {
        return idCardName;
    }

    public void setIdCardName(String idCardName) {
        this.idCardName = idCardName;
    }

    public String getIdCardNum() {
        return idCardNum;
    }

    public void setIdCardNum(String idCardNum) {
        this.idCardNum = idCardNum;
    }

    public String getValidDate() {
        return validDate;
    }

    public void setValidDate(String validDate) {
        this.validDate = validDate;
    }

    @Override
    public String toString() {
        return "IdentityCard{" +
                "idCardName='" + idCardName + '\'' +
                ", idCardNum='" + idCardNum + '\'' +
                ", validDate='" + validDate + '\'' +
                '}';
    }
}
