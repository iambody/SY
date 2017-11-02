package com.cgbsoft.privatefund.bean.ocr;

/**
 * desc 身份证正反面公用的bean的数据javabean
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/10/18-11:08
 */
public class IdentityCard {

    private String idCardName;//": "李永强",
    private String idCardNum;//": "37148119901112251X"
    private String validDate;//": "2018-07-11"
    private String birth;
    private String sex;
    private String address;
    //0标识正面 1标识反面 -1为识别到身份证信息
    private int Type;

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

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


}
