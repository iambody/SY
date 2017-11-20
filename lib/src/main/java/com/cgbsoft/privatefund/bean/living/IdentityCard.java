package com.cgbsoft.privatefund.bean.living;

/**
 * desc 身份证正反面公用的bean的数据javabean
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/10/18-11:08
 */
public class IdentityCard {

    private String localPath;
    private String remotPath;
    private String idCardName;//": "李永强",
    private String idCardNum;//": "37148119901112251X"
    private String validDate;//": "2018-07-11"
    private String birth;
    private String sex;
    private String address;
    //1是识别成功 0为识别失败
    private String analysisType;
    private String analysisMsg;

    public String getAnalysisMsg() {
        return analysisMsg;
    }

    public void setAnalysisMsg(String analysisMsg) {
        this.analysisMsg = analysisMsg;
    }

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

    public String getAnalysisType() {
        return analysisType;
    }

    public void setAnalysisType(String analysisType) {
        this.analysisType = analysisType;
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

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public String getRemotPath() {
        return remotPath;
    }

    public void setRemotPath(String remotPath) {
        this.remotPath = remotPath;
    }

    @Override
    public String toString() {
        return "IdentityCard{" +
                "localPath='" + localPath + '\'' +
                ", remotPath='" + remotPath + '\'' +
                ", idCardName='" + idCardName + '\'' +
                ", idCardNum='" + idCardNum + '\'' +
                ", validDate='" + validDate + '\'' +
                ", birth='" + birth + '\'' +
                ", sex='" + sex + '\'' +
                ", address='" + address + '\'' +
                ", analysisType='" + analysisType + '\'' +
                ", analysisMsg='" + analysisMsg + '\'' +
                '}';
    }
}
