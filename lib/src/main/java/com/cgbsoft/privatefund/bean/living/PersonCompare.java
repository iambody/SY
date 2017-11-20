package com.cgbsoft.privatefund.bean.living;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/11/8-17:08
 */
public class PersonCompare {
    //0成功 1失败
    private int resultTage;
    public String currentPageTag;

    private PersonCompare() {
    }

    public int getResultTage() {
        return resultTage;
    }

    public void setResultTage(int resultTage) {
        this.resultTage = resultTage;
    }

    public String getCurrentPageTag() {
        return currentPageTag;
    }

    public void setCurrentPageTag(String currentPageTag) {
        this.currentPageTag = currentPageTag;
    }

    public PersonCompare(int resultTage, String currentPageTag) {
        this.resultTage = resultTage;
        this.currentPageTag = currentPageTag;
    }
}
