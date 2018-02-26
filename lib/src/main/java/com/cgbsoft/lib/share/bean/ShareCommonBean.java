package com.cgbsoft.lib.share.bean;
/**
 * desc  分享时候需要传递的bean
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 17/3/31-18:51
 */

/**
 * desc  分享时候需要传递的bean
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 17/3/31-18:51
 */
public class ShareCommonBean extends BaseShareBean {
    private String ShareTitle;
    private String ShareContent;
    //网络图片
    private String ShareNetLog;
    private String ShareUrl;

    private boolean isNetLog;


    public ShareCommonBean() {

    }
    //微信分享 默认是本地

    public ShareCommonBean(String shareTitle, String shareContent, String shareUrl, String shareLog) {
        ShareTitle = shareTitle;
        ShareContent = shareContent;
        ShareUrl = shareUrl;
        ShareNetLog = shareLog;
    }


    public boolean isNetLog() {
        return isNetLog;
    }

    public void setNetLog(boolean netLog) {
        isNetLog = netLog;
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

    public String getShareNetLog() {
        return ShareNetLog;
    }

    public void setShareNetLog(String shareNetLog) {
        ShareNetLog = shareNetLog;
    }

    public String getShareUrl() {
        return ShareUrl;
    }

    public void setShareUrl(String shareUrl) {
        ShareUrl = shareUrl;
    }

}
