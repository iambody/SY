package com.cgbsoft.lib.base.webview;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/5/4-19:13
 */
public class CwebNetConfig extends BaseWebNetConfig {

    // toc 我的首页
    public static String minePgge;

    static {
        initApi();
    }

    public static void initApi() {
        // toc 我的首页
        minePgge = baseWebsite + "/index.html";
    }
}
