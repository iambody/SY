package com.cgbsoft.privatefund.bean.share;

import com.cgbsoft.privatefund.bean.BaseBean;

/**
 * desc  不同模块需要调用分享模块时候需要传递的分享bean  可能包含所有需要分享的字段
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 17/4/1-10:55
 */

public class CommonShareBean extends BaseBean {



    //产品分享bean
    /**
     * 分享title
     */
    private String title;
    /**
     * 分享content
     */
    private String content;
    /**
     * 分享左边的log
     */
    private int image;

    /**
     * 分享左边的log的网络地址
     */
    private String imageUrl;
    /**
     * 分享的点击url
     */
    private String url;

    private String duanxinText;
    private String youjianText;
    private String link;
    private String youjianTitleText;
    private String productID;
    private String productType;
    private String typeCode;
    private String schemeId;
    // 资讯分享是不同的字段
    private String zixun;
    private NewsBean newsBean;

    public CommonShareBean() {
    }
//    public void setData(String title, String content, int image,
//                        String url, String duanxinText, String youjianTitleText,
//                        String youjianText, String link, String zixun) {
//
//    }

    /**
     * 视频分享中仅仅分享微信好友的bean
     */
    public CommonShareBean(String title, String content, int image, String url) {
        this.title = title;
        this.content = content;
        this.image = image;
        this.url = url;
    }

    /**
     * 消息资讯里面的分享bean  比较基金详情多了一个zixun,NewsBean
     * @param title
     * @param content
     * @param image
     * @param url
     * @param duanxinText
     * @param youjianText
     * @param link
     * @param zixun
     */

    public CommonShareBean(String title, String content, int image, String url, String duanxinText, String youjianText, String link,String youjianTitleText, String zixun,NewsBean newsBean) {
        this.title = title;
        this.content = content;
        this.image = image;
        this.url = url;
        this.duanxinText = duanxinText;
        this.youjianText = youjianText;
        this.link = link;
        this.youjianTitleText=youjianTitleText;
        this.zixun = zixun;
        this.newsBean = newsBean;
    }

    /**
     * 基金详情的分享数据
     *
     * @param title
     * @param content
     * @param image
     * @param url
     * @param duanxinText
     * @param youjianText
     * @param link
     * @param youjianTitleText
     * @param productID
     * @param productType
     * @param typeCode
     * @param schemeId
     */
    public CommonShareBean(String title, String content, int image, String url, String duanxinText, String youjianText, String link, String youjianTitleText, String productID, String productType, String typeCode, String schemeId) {
        this.title = title;
        this.content = content;
        this.image = image;
        this.url = url;
        this.duanxinText = duanxinText;
        this.youjianText = youjianText;
        this.link = link;
        this.youjianTitleText = youjianTitleText;
        this.productID = productID;
        this.productType = productType;
        this.typeCode = typeCode;
        this.schemeId = schemeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDuanxinText() {
        return duanxinText;
    }

    public void setDuanxinText(String duanxinText) {
        this.duanxinText = duanxinText;
    }

    public String getYoujianText() {
        return youjianText;
    }

    public void setYoujianText(String youjianText) {
        this.youjianText = youjianText;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getYoujianTitleText() {
        return youjianTitleText;
    }

    public void setYoujianTitleText(String youjianTitleText) {
        this.youjianTitleText = youjianTitleText;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getSchemeId() {
        return schemeId;
    }

    public void setSchemeId(String schemeId) {
        this.schemeId = schemeId;
    }
    public String getZixun() {
        return zixun;
    }

    public void setZixun(String zixun) {
        this.zixun = zixun;
    }

    public NewsBean getNewsBean() {
        return newsBean;
    }

    public void setNewsBean(NewsBean newsBean) {
        this.newsBean = newsBean;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
