package com.cgbsoft.privatefund.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.cgbsoft.lib.base.webview.CwebNetConfig;
import com.cgbsoft.lib.base.webview.WebViewConstant;

import app.product.com.mvp.ui.ProductDetailActivity;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/5/23-09:26
 */
public class MainNavigationUtils {
    /**
     * 跳转到公用的web
     *
     * @param context
     * @param url
     * @param title
     * @param isShowTitle
     */
    public static void startCommonWebActivity(Context context, String url, String title, boolean isShowTitle) {
        Intent intent = new Intent(context, ProductDetailActivity.class);
        intent.putExtra(WebViewConstant.push_message_url, url);
        intent.putExtra(WebViewConstant.push_message_title, title);
        intent.putExtra(WebViewConstant.PAGE_SHOW_TITLE, isShowTitle);
        ((Activity) context).startActivity(intent);

    }
}
