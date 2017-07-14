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

    public static String  pageInit;

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

    // 我的订单
    public static String mineGoodsOrder;
    // 我的资产订单
    public static String mineAssertOrder;
    // 我的健康订单
    public static String mineHealthOrder;
    // 资讯详情
    public static String discoveryDetail;
    // 会员专区
    public static String memeberArea;

    // 签到页面
    public static String signInPage;
    // 我的卡券
    public static String mineCardCoupons;
    // 我的贺卡
    public static String mineBestCard;
    // 选择理财师
    public static String selectAdviser;
    // 投资日历
    public static String investeCarlendar;

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
        pageInit = baseSxyParentUrl + "/setData.html";
        productDetail = baseSxyParentUrl + "/biz/product/index.html?id=";
        msgDetal = baseParentUrl + "/apptie/notice_toB.html?id=";
        riskEvaluationQuestion = baseParentUrl + "/settings/index.html";
        product = baseParentUrl + "/apptie/detail.html?schemeId=";
        mineTouGu = baseParentUrl + "/myAdviser/index.html";
        noBindUserInfo = baseParentUrl + "/settings/assets_basis.html";
        invistorCertify = baseParentUrl + "/settings/assets_report.html";
    }
    public static void initSxyUrl(){
        HOME_URL= baseSxyParentUrl + "/biz/profile/";
        elegantGoodsDetail=baseSxyParentUrl+"/biz/life/detail.html?goodsId=";
        mineGoodsOrder = baseSxyParentUrl + "/biz/mine/mine_order.html";
        mineAssertOrder = baseSxyParentUrl + "/biz/mine/balance_order.html";
        mineHealthOrder = baseSxyParentUrl + "/biz/mine/mine_health.html";
        signInPage = baseSxyParentUrl + "/biz/mine/mine-sign.html";
        mineCardCoupons = baseSxyParentUrl + "/biz/card/no_card.html";
        mineBestCard = baseSxyParentUrl + "/biz/celebrate/no_celebrate.html";
        selectAdviser = baseSxyParentUrl + "/biz/adviser/assets_basis.html";
        discoveryDetail = baseSxyParentUrl + "/biz/information/details.html";
        memeberArea = baseSxyParentUrl + "/biz/members/index.html";
        investeCarlendar = baseSxyParentUrl + "/biz/calendar/index.html";
        choiceAdviser= baseSxyParentUrl + "/biz/adviser/assets_basis.html";
    }
}
