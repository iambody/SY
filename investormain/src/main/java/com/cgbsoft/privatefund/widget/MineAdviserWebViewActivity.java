package com.cgbsoft.privatefund.widget;

import com.cgbsoft.lib.base.webview.BaseWebViewActivity;
import com.cgbsoft.lib.base.webview.WebViewConstant;
import com.cgbsoft.lib.utils.tools.Utils;

/**
 * @author chenlong
 */
public class MineAdviserWebViewActivity extends BaseWebViewActivity {
    @Override
    protected void executeOverideUrlCallBack(String actionUrl) {
        if (actionUrl.contains(WebViewConstant.AppCallBack.TOC_SHARE)) {
            Utils.OpenSharePage(this, actionUrl, false, false, true);
        }
    }
}
