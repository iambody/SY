package com.cgbsoft.privatefund.bean.living;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/11/2-20:23
 */
public class LivingResultData {

    private String recognitionMsg;//":"姓名和身份证不匹配"
    private String recognitionCode;// 0成功 1客服审核 2ocr错误3标识失败

    public String getRecognitionMsg() {
        return recognitionMsg;
    }

    public void setRecognitionMsg(String recognitionMsg) {
        this.recognitionMsg = recognitionMsg;
    }

    public String getRecognitionCode() {
        return recognitionCode;
    }

    public void setRecognitionCode(String recognitionCode) {
        this.recognitionCode = recognitionCode;
    }

    public LivingResultData() {
    }

    public LivingResultData(String recognitionMsg, String recognitionCode) {
        this.recognitionMsg = recognitionMsg;
        this.recognitionCode = recognitionCode;
    }


    @Override
    public String toString() {
        return "LivingResultData{" +
                "recognitionMsg='" + recognitionMsg + '\'' +
                ", recognitionCode='" + recognitionCode + '\'' +
                '}';
    }
}
