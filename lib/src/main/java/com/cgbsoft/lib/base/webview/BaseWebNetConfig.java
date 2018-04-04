package com.cgbsoft.lib.base.webview;

import com.cgbsoft.lib.utils.net.NetConfig;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/5/4-19:12
 */
public class BaseWebNetConfig extends NetConfig {
    //基础host
    public static String baseParentUrl = SERVER_ADD + "/";

    public static String baseSxyParentUrl = SERVER_ADD + "/app6.0";
    //基础pdf路径
    public static String basePdfUrl = baseParentUrl + "pdfjs/web/viewer.html?";
    //
    public static String pdfUrlToC = basePdfUrl + "file=";
    //测跳的产品详细跳转到资讯页面
    public static String detailToZiXun = baseParentUrl + "apptie/new_detail_toc.html?id=";

    //绑定理财师
    public static String bindAdviser = baseSxyParentUrl + "/biz/adviser/assets_basis.html";

    //风险测评
    public static String evaluation = baseSxyParentUrl + "/biz/assesment/index.html";

    //特定投资者认定
    public static String specialInvestorPerson = "/biz/assesment/quest_person1.html";
    public static String specialInvestorCompany = "/biz/assesment/quest_company1.html";

    //投资者完善资料
    public static String investorInfoPerson = "app6.0/biz/assesment/quest_person1.html";
    public static String investorInfoCompany = "app6.0/biz/assesment/quest_company1.html";

    public static void updateRequestUrl() {
        baseParentUrl = SERVER_ADD + "/";
        baseSxyParentUrl = SERVER_ADD + "/app6.0";
        basePdfUrl = baseParentUrl + "pdfjs/web/viewer.html?";
        pdfUrlToC = basePdfUrl + "file=";
        detailToZiXun = baseParentUrl + "apptie/new_detail_toc.html?id=";
        bindAdviser = baseSxyParentUrl + "/biz/adviser/assets_basis.html";
        evaluation = baseSxyParentUrl + "/biz/assesment/index.html";
    }
}
