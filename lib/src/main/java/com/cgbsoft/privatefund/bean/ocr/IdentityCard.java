package com.cgbsoft.privatefund.bean.ocr;

/**
 * desc 身份证有头像的数据javabean
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/10/18-11:08
 */
public class IdentityCard {
    //正面的数据
    public String name;
    public String sex;
    public String nation;
    public String birth;
    public String address;
    public String idNo;
    public String frontFullImageSrc;


    //国徽面的数据
    public String office;
    public String validDate;
    public String sign;
    public String orderNo;
    public String ocrId;
    public String backFullImageSrc;

    public String getBackFullImageSrc() {
        return backFullImageSrc;
    }

    public void setBackFullImageSrc(String backFullImageSrc) {
        this.backFullImageSrc = backFullImageSrc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public String getValidDate() {
        return validDate;
    }

    public void setValidDate(String validDate) {
        this.validDate = validDate;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOcrId() {
        return ocrId;
    }

    public void setOcrId(String ocrId) {
        this.ocrId = ocrId;
    }

    public String getFrontFullImageSrc() {
        return frontFullImageSrc;
    }

    public void setFrontFullImageSrc(String frontFullImageSrc) {
        this.frontFullImageSrc = frontFullImageSrc;
    }

    @Override
    public String toString() {
        return "IdentityCard{" +
                "name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", nation='" + nation + '\'' +
                ", birth='" + birth + '\'' +
                ", address='" + address + '\'' +
                ", idNo='" + idNo + '\'' +
                ", office='" + office + '\'' +
                ", validDate='" + validDate + '\'' +
                ", sign='" + sign + '\'' +
                ", orderNo='" + orderNo + '\'' +
                ", ocrId='" + ocrId + '\'' +
                ", frontFullImageSrc='" + frontFullImageSrc + '\'' +
                '}';
    }
}
