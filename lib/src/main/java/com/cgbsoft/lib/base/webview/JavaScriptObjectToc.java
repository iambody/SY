package com.cgbsoft.lib.base.webview;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.cgbsoft.lib.utils.tools.PromptManager;
import com.cgbsoft.lib.utils.tools.ThreadUtils;

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

    /**
     * 暴露 给H5 用户信息的 对象接口
     * @return
     */
    @JavascriptInterface
    public String getData() {
//        System.out.println("-------token=" + MApplication.getToken() + "------version=" + AppInfo.versionCode(context));
//        String token = TextUtils.isEmpty(MApplication.getToken()) ? SPSave.getInstance(MApplication.getInstance().getApplicationContext()).getString(Contant.token) : MApplication.getToken();
//        String userId = TextUtils.isEmpty(MApplication.getUserid()) ? SPSave.getInstance(MApplication.getInstance().getApplicationContext()).getString(Contant.userid) : MApplication.getUserid();
//        String identify = SPSave.getInstance(context).getString(Contant.identify);
//        StringBuffer sb = new StringBuffer();
//        sb.append(token).append(":").append(userId).append(":").append(AppInfo.versionCode(context)).append(":").append(identify.equals(Contant.IdentityLicaishi) ? "2" : "1");
//        return sb.toString();
        return "";
    }

    /**
     * 重新加载url的 对象接口
     * @return
     */
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
