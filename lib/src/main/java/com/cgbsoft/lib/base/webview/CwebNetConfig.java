package com.cgbsoft.lib.base.webview;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/5/4-19:13
 */
public class CwebNetConfig extends BaseWebNetConfig {

    // toc 我的首页
    public static String minePgge;
    // 产品
    public static String productPage;
    // 发现
    public static String discoverPage;
    // 俱乐部
    public static String clubPgge;

    public static String pageInit;

    public static String productDetail;

    //webView消息详情列表
    public static String msgDetal;

    static {
        initApi();
    }

    public static void initApi() {
        // toc 我的首页
        minePgge = baseParentUrl + "/index.html";
        productPage = baseParentUrl + "/apptie/productlist.html";
        discoverPage = baseParentUrl + "/discover/index.html";
        clubPgge = baseParentUrl + "/bank/default.html";
        pageInit = baseParentUrl + "/setData.html";
        productDetail = baseParentUrl + "/apptie/detail.html?schemeId=";
        msgDetal = baseParentUrl + "/apptie/notice_toB.html?id=";
    }
}
