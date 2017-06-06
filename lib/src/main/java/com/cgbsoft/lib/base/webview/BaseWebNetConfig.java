package com.cgbsoft.lib.base.webview;

import com.cgbsoft.lib.utils.net.NetConfig;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/5/4-19:12
 */
public class BaseWebNetConfig extends NetConfig {
    //基础host
    public static String baseParentUrl = SERVER_ADD + "/app5.0/";

    //基础pdf路径
    public static String basePdfUrl = baseParentUrl + "pdfjs/web/viewer.html?";
    //c侧product模块中的路径
    public static String pdfUrlToC = basePdfUrl + "file=";
    //测跳的产品详细跳转到资讯页面
    public static String detailToZiXun=baseParentUrl+"apptie/new_detail_toc.html?id=";
}
