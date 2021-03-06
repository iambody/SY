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
    // Webview
    public static String msgMoreDetail;

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
    // 沙龙详情
    public static String salonDetail;
    // 推荐好友
    public static String recommendFriends;
    // 关于本应用
    public static String aboutapp;
    // 常见问题
    public static String common_problem;
    // 我的二维码
    public static String myqr;
    // 云豆规则
    public static String yundouRule;

    // 我的订单
    public static String mineGoodsOrder;
    // 我的资产订单
    public static String mineAssertOrder;
    // 我的健康咨询
    public static String mineHealthKnow;
    // 我的健康订单
    public static String mineHealthOrder;
    // 我的订单详情
    public static String mineHealthOrderDetail;
    // 我的旅游订单列表
    public static String mineTravelOrderList;
    // 我的旅游订单详情
    public static String mineTravelOrderDetail;
    // 我的旅游权益介绍
    public static String mineTravelIntroduce;

    // 资讯详情
    public static String discoveryDetail;
    // 会员专区
    public static String memeberArea;
    // 财富值
    public static String healthValue;
    // 会员规则
    public static String memberRule;
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
    // 资产报告
    public static String assetReport;

    // 我的云豆
    public static String mineYunDou;

    // 活动详情
    public static String activitesDeatil;

    // 投资人认证
    public static String HOME_URL;
    //私享云 投顾页面  未绑定过
    public static String choiceAdviser;
    //私享云 投顾页面  已经绑定过
    public static String BindchiceAdiser;
    //会员
    public static String membercenter;
    public static String qrcoderesult;
    //会员规则
    public static String adviserrules;

    // 头条号
    public static String touTiaoHao;

    //贵宾卡兑换
    public static String vipCardExchange;
    //国内公募基注册的
    public static String publicFundRegistUrl;
    //公募基金详情
    public static String publicFundDetailUrl;
    //公募基金首页
    public static String publicFundHomeUrl;
    // 私享宝详情
    public static String publicShareBaoDetail;
    // 我的公募基金列表
    public static String minePublicFund;
    //公募基金的风险测评
    public static String publicFundRiskUrl;
    //赎回结果页
    public static String publicFundRedeemResult;
    public static String publicFundBuyOrSell;
    public static String publicFundBuyResult;

    //交易记录
    public static String private_share_bao_record;
    public static String public_fund_record;
    //私享宝详情页
    public static String sxbFundDetailUrl;
    //公募基金的买入卖出协议wallet=1钱包 wallet=0普通基金 type=1买入 type=2卖出

    public static String fundBuyDeclareUrl;//
    public static String fundSellDeclareUrl;
    public static String sxbBuyDeclareUrl;
    public static String sxbSellDeclareUrl;

    //银行卡转账说明
    public static String bankTransferState;

    static {
        initApi();
        initSxyUrl();
    }

    public static void updateRequestUrl() {
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
        productDetail = baseSxyParentUrl + "/biz/product/index.html?schemeId=";
        riskEvaluationQuestion = baseParentUrl + "/settings/index.html";
        product = baseSxyParentUrl + "/apptie/detail.html?schemeId=";
        mineTouGu = baseParentUrl + "/myAdviser/index.html";
        noBindUserInfo = baseParentUrl + "/settings/assets_basis.html";
        invistorCertify = baseParentUrl + "/settings/assets_report.html";

    }

    public static void initSxyUrl() {
        HOME_URL = baseSxyParentUrl + "/biz/profile/";
        elegantGoodsDetail = baseSxyParentUrl + "/biz/life/detail.html?goodsId=";
        mineGoodsOrder = baseSxyParentUrl + "/biz/mine/mine_order.html";
        mineAssertOrder = baseSxyParentUrl + "/biz/mine/balance_order.html";
        mineHealthKnow = baseSxyParentUrl + "/biz/mine/mine_health.html";
        mineHealthOrder = baseSxyParentUrl + "/biz/mine/health_order.html";
        mineHealthOrderDetail = baseSxyParentUrl + "/biz/mine/health_order_detail.html?orderCode=";
        mineTravelOrderList = baseSxyParentUrl + "/biz/mine/travel_order.html";
        mineTravelOrderDetail = baseSxyParentUrl + "/biz/mine/travel_order_detail.html?orderCode=";
        mineTravelIntroduce = baseSxyParentUrl + "/biz/members/detail_tour.html";

//        msgDetal = baseParentUrl + "/apptie/notice_toB.html?id=";// 博伦修改地址
        msgDetal = baseSxyParentUrl + "/biz/product/notice.html?id=";
        msgMoreDetail = baseSxyParentUrl + "/biz/product/notice_detail.html?id=";
        signInPage = baseSxyParentUrl + "/biz/mine/mine-sign.html";
        mineCardCoupons = baseSxyParentUrl + "/biz/card/mine_card.html";
        mineBestCard = baseSxyParentUrl + "/biz/celebrate/no_celebrate.html";
        selectAdviser = baseSxyParentUrl + "/biz/adviser/assets_basis.html";
//      discoveryDetail = baseSxyParentUrl + "/biz/product/new_detail_toc.html"; // 博伦页面 （热搜进去没有内容，其他资讯页面是没有问题）
        discoveryDetail = baseSxyParentUrl + "/biz/information/details.html";
        memeberArea = baseSxyParentUrl + "/biz/members/index.html";
        healthValue = baseSxyParentUrl + "/biz/members/wealth.html";
        investeCarlendar = baseSxyParentUrl + "/biz/calendar/index.html";
        //没绑定过的
        choiceAdviser = baseSxyParentUrl + "/biz/adviser/assets_basis.html?bindChannel=5";
        //绑定过的
        BindchiceAdiser = baseSxyParentUrl + "/biz/adviser/details_bind.html?bindChannel=5";
        activitesDeatil = baseSxyParentUrl + "/biz/indexSecond/active_detail.html";
        assetReport = baseSxyParentUrl + "/biz/report/index.html";
        salonDetail = baseSxyParentUrl + "/biz/indexSecond/active_detail.html?id=";
        recommendFriends = baseSxyParentUrl + "/biz/mine/mine-recommend.html";
        aboutapp = baseSxyParentUrl + "/biz/mine/mine-about.html";
        common_problem = baseSxyParentUrl + "/biz/mine/mine-help.html";
        myqr = baseSxyParentUrl + "/biz/mine/mine-myCode.html";
        mineYunDou = baseSxyParentUrl + "/biz/mine/mine_ydaccount.html";
        yundouRule = baseSxyParentUrl + "/biz/mine/yd-rules.html";
        memberRule = baseSxyParentUrl + "/biz/members/rules.html";

        membercenter = baseSxyParentUrl + "/biz/members/index.html";
        qrcoderesult = baseSxyParentUrl + "/biz/adviser/assets_report.html?";
//        touTiaoHao = baseSxyParentUrl + "/biz/product/new_detail_toc.html";

        adviserrules = baseSxyParentUrl + "/biz/members/rules.html";
        touTiaoHao = baseSxyParentUrl + "/biz/product/new_detail_toc.html?id=";
        vipCardExchange = baseSxyParentUrl + "/biz/yundous/rechargeCard.html";


        //公募基金相关******************
        publicFundRegistUrl = baseSxyParentUrl + "/biz/publicfund/account.html";
        publicFundDetailUrl = baseSxyParentUrl + "/biz/publicfund/detail.html";
        publicFundHomeUrl = baseSxyParentUrl + "/biz/publicfund/fundIndex.html";
        publicShareBaoDetail = baseSxyParentUrl + "/biz/publicfund/profile.html";
        minePublicFund = baseSxyParentUrl + "/biz/publicfund/myfund.html";
        publicFundBuyResult = baseSxyParentUrl + "/biz/publicfund/result4subscription.html";
        //开户
        publicFundRiskUrl = baseSxyParentUrl + "/biz/publicfund/assessment.html";
        //首页跳转到私享宝详情页
        sxbFundDetailUrl = baseSxyParentUrl + "/biz/publicfund/profile.html";

        //赎回成功后 跳转的赎回结果页
        publicFundRedeemResult = baseSxyParentUrl + "/biz/publicfund/redemption_end.html";
        // 盈泰钱包的赎回和购买结果页
        publicFundBuyOrSell = baseSxyParentUrl + "/biz/publicfund/deal_prompt.html";

        //盈泰钱包交易记录地址
        private_share_bao_record = baseSxyParentUrl + "/biz/publicfund/record.html?type=1";
        //公募基金交易记录地址
        public_fund_record = baseSxyParentUrl + "/biz/publicfund/record.html?type=2";
        //公募基金的买入卖出协议wallet=1钱包 wallet=0普通基金 type=1买入 type=2卖出
        // 正常的买说明
        fundBuyDeclareUrl = baseSxyParentUrl + "/biz/publicfund/exchange_instruction.html?wallet=0&type=1";
        //正常的卖
        fundSellDeclareUrl = baseSxyParentUrl + "/biz/publicfund/exchange_instruction.html?wallet=0&type=2";
        //钱包的买
        sxbBuyDeclareUrl = baseSxyParentUrl + "/biz/publicfund/exchange_instruction.html?wallet=1&type=1";
        //钱包的卖
        sxbSellDeclareUrl = baseSxyParentUrl + "/biz/publicfund/exchange_instruction.html?wallet=1&type=2";
        //银行转帐授权协议
        bankTransferState = baseSxyParentUrl + "/biz/publicfund/licensing_agreements.html";
    }
}
