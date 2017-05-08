package com.cgbsoft.lib.base.webview;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.cgbsoft.lib.BaseApplication;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.utils.tools.PromptManager;
import com.cgbsoft.lib.utils.tools.ThreadUtils;
import com.cgbsoft.lib.utils.tools.Utils;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/5/4-17:59
 */
public class JavaScriptObjectToc {

    private Context context;
    private BaseWebview webView;
    private String url;

    public JavaScriptObjectToc(Context context, BaseWebview webView) {
        this.context = context;
        this.webView = webView;
    }

    public void setUrl(String url) {
        Log.i(this.getClass().getName(), "url==" + url);
        this.url = url;
    }

    @JavascriptInterface
    public String getData() {
        String token = SPreference.getToken(context);
        String userId = SPreference.getUserId(context);
        StringBuffer sb = new StringBuffer();
        sb.append(token).append(":").append(userId).append(":").append(Utils.getVersionName(BaseApplication.getContext())).append(":").append("1");
        return sb.toString();
    }

    @JavascriptInterface
    public void reloadNetWork() {
        Log.i("JavaScriptObject", "reloadNetWork------url=" + url);
        ThreadUtils.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                if (webView != null) {
                    webView.loadUrl(url);
//                    PromptManager.ShowCustomToast(context,"拉动和打火机");
                }
            }
        });
    }

//    @JavascriptInterface
//    public boolean hasAddCarlender(String title, String startTime) {
//        Log.i("JavaScriptObject", "hasAddCarlender----=" + CalendarManamger.isExist(context, startTime, title));
//        return CalendarManamger.isExist(context, startTime, title);
//    }
//
//    @JavascriptInterface
//    public void openPrePlayVideoDialog(String title, String address, String headImgUrl, String startTime, String slog) {
//        Log.i("JavaScriptObject", "prePlayVideoNotifycation");
//        EventBus.getDefault().post(new PreVideoPlay(slog, headImgUrl, title, startTime, address));
//    }

}