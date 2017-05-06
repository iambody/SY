package com.cgbsoft.lib.base.webview;

import android.app.Activity;
import android.content.Context;

import com.tencent.smtt.sdk.WebView;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/5/4-17:58
 */
public class CWebviewUtil {
    private Activity activity;
    private WebView webview;

    private static CWebviewUtil instance = null;

    private CWebviewUtil(Activity activity) {
        super();
        this.activity = activity;
    }


    public static synchronized CWebviewUtil getInstance(Activity bacActivity) {
        if (instance == null) {
            instance = new CWebviewUtil(bacActivity);
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
