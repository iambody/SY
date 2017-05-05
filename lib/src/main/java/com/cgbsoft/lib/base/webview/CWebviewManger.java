package com.cgbsoft.lib.base.webview;

import android.app.Activity;

import com.tencent.smtt.sdk.WebView;

/**
 * desc 负责处理action  (Url)的操作 ！
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/5/4-17:58
 */
public class CWebviewManger {
    private Activity activity;
    private WebView webview;

    private static CWebviewManger instance = null;

    private CWebviewManger(Activity activity) {
        super();
        this.activity = activity;
    }


    public static synchronized CWebviewManger getInstance(Activity bacActivity) {
        if (instance == null) {
            instance = new CWebviewManger(bacActivity);
        }
        return instance;
    }


    //开始带动用action
    public static void setAction(String action) {


    }

    public void setWeb(WebView webViews) {
        this.webview = webViews;
    }
}
