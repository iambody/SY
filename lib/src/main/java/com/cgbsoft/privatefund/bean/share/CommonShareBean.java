package com.cgbsoft.privatefund.bean.share;

import com.cgbsoft.privatefund.bean.BaseBean;

/**
 * desc  不同模块需要调用分享模块时候需要传递的分享bean  可能包含所有需要分享的字段
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 17/4/1-10:55
 */

public class CommonShareBean extends BaseBean {
    /**
     * 分享title
     */
    private String ShareTitle;
    /**
     * 分享content
     */
    private String ShareContent;
    /**
     * 分享左边的log
     */
    private String ShareLog;
    /**
     * 分享的点击url
     */
    private String ShareUrl;

    public CommonShareBean() {
    }

    public String getShareTitle() {
        return ShareTitle;
    }

    public void setShareTitle(String shareTitle) {
        ShareTitle = shareTitle;
    }

    public String getShareContent() {
        return ShareContent;
    }

    public void setShareContent(String shareContent) {
        ShareContent = shareContent;
    }

    public String getShareLog() {
        return ShareLog;
    }

    public void setShareLog(String shareLog) {
        ShareLog = shareLog;
    }

    public String getShareUrl() {
        return ShareUrl;
    }

    public void setShareUrl(String shareUrl) {
        ShareUrl = shareUrl;
    }
}
