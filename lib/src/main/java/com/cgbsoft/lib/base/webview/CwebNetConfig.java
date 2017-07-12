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
    public static String clubPage;

    public static String pageInit;

    public static String productDetail;

    public static String product;

    //webView消息详情列表
    public static String msgDetal;

    // 风险评测结果页面
    public static String riskEvaluationQuestion;

    // 我的投顾
    public static String mineTouGu;

    // 未绑定信息天蝎页面
    public static String noBindUserInfo;

    // 投资人认证
    public static String invistorCertify;
    // 生活模块尚品列表商品详情
    public static String elegantGoodsDetail;


    // 投资人认证
    public static String HOME_URL;
    //私享云 选择投顾页面
    public static String choiceAdviser;

    static {
        initApi();
        initSxyUrl();
    }

    public static void initApi() {
        // toc 我的首页
        minePgge = baseParentUrl + "/index.html";
        productPage = baseParentUrl + "/apptie/productlist.html";
        discoverPage = baseParentUrl + "/discover/index.html";
        clubPage = baseParentUrl + "/bank/default.html";
        pageInit = baseParentUrl + "/setData.html";
        productDetail = baseParentUrl + "/apptie/detail.html?schemeId=";
        msgDetal = baseParentUrl + "/apptie/notice_toB.html?id=";
        riskEvaluationQuestion = baseParentUrl + "/settings/index.html";
        product = baseParentUrl + "/apptie/detail.html?schemeId=";
        mineTouGu = baseParentUrl + "/myAdviser/index.html";
        noBindUserInfo = baseParentUrl + "/settings/assets_basis.html";
        invistorCertify = baseParentUrl + "/settings/assets_report.html";
    }

    /**
     * 私享云使用新得weburl
     */
    public static void initSxyUrl() {
        HOME_URL = baseSxyParentUrl + "biz/profile/";
        elegantGoodsDetail = baseSxyParentUrl + "biz/life/detail.html?goodsId=";
        //选择理财师
        choiceAdviser = baseSxyParentUrl + "/biz/adviser/assets_basis.html";
//        https://d10-app.simuyun.com/app6.0/biz/adviser/assets_basis.html

    }

}
