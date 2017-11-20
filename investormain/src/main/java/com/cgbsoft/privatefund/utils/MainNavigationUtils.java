package com.cgbsoft.privatefund.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.cgbsoft.lib.base.webview.CwebNetConfig;
import com.cgbsoft.lib.base.webview.WebViewConstant;
import com.cgbsoft.privatefund.mvp.ui.home.RiskEvaluationActivity;

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
     */
    public static void startCommonWebActivity(Context context ) {
        Intent intent = new Intent(context, RiskEvaluationActivity.class);
        ((Activity) context).startActivity(intent);

    }
}
