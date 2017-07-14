package com.cgbsoft.lib.base.webview;

import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.BaseApplication;
import com.cgbsoft.lib.utils.tools.DeviceUtils;
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

    /**
     * 暴露 给H5 用户信息的 对象接口
     * @return
     */
    @JavascriptInterface
    public String getData() {
        String token = AppManager.getUserToken(context);
        String userId = AppManager.getUserId(context);
        String visiter = AppManager.isVisitor(context) ? "1" : "2"; // 1是游客模式 2是正常模式
        System.out.println("---------userId=" + userId);
        System.out.println("---------token=" + token);
        StringBuffer sb = new StringBuffer();
        sb.append(token).append(":").append(userId).append(":").append(Utils.getVersionCode(BaseApplication.getContext())).append(":").append("1").append(":").
                append("C").append(":").append(DeviceUtils.getPhoneId(context)).append(":").append(visiter);

        Log.i("JavaScriptObjectToc", sb.toString());
        return sb.toString();
    }

    /**
     * 重新加载url的 对象接口
     *
     * @return
     */
    @JavascriptInterface
    public void reloadNetWork() {
        Log.i("JavaScriptObject", "reloadNetWork------url=" + url);
        ThreadUtils.runOnMainThread(() -> {
            if (webView != null) {
                webView.loadUrl(url);
            }
        });
    }

////  PromptManager.ShowCustomToast(context,"拉动和打火机");
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
