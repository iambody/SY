package com.cgbsoft.lib.base.webview;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.cgbsoft.lib.utils.constant.RxConstant;
import com.cgbsoft.lib.utils.rxjava.RxBus;
import com.cgbsoft.lib.utils.tools.NavigationUtils;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

/**
 * desc  所有C的交互全部在这里进行
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/5/4-17:41
 */
public class CWebClient extends WebViewClient {
    /**
     * C端对象注入时候的Js对象  仅仅给C端使用
     */
    private JavaScriptObjectToc javaScriptObjectToc;
    /**
     * 上下文
     */
    private Activity curretnAtivity;
    /**
     * 这个接口是直播和认购时候的特殊url截取逻辑 单独回调给ui进行处理  暴露给webview使用  @按照陈龙之前业务逻辑
     */
    private WebviewOnClick webviewOnClick;
    private String loadUrl;

    /**
     * 是否是商学院  @按照陈龙之前业务逻辑
     */
    private boolean isShangxueyuanc = false;

    /**
     * BaseWebview
     */
    private BaseWebview baseWebview;

    public CWebClient() {
        super();
    }

    public CWebClient(BaseWebview baseWebview, JavaScriptObjectToc javaScriptObjectToc, Context cactivity, WebviewOnClick click, boolean isShangxueyuanc) {
        this.javaScriptObjectToc = javaScriptObjectToc;
        this.baseWebview = baseWebview;
        this.curretnAtivity = (Activity) cactivity;
        this.webviewOnClick = click;
        this.isShangxueyuanc = isShangxueyuanc;
    }

    public void setWebviewOnClick(WebviewOnClick webviewOnClick) {
        this.webviewOnClick = webviewOnClick;
    }
    CWebviewManger cWebClient;
    @Override
    public boolean shouldOverrideUrlLoading(WebView webView, String url) {
//        return super.shouldOverrideUrlLoading(webView, s);
        // 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
        System.out.println("-------webview= " + url);
        if (url.startsWith("app:")) {
            /**
             * 认购按钮的特殊处理
             *
             */
            if (null != webviewOnClick && (
                    url.startsWith(WebViewConstant.AppCallBack.BUY_NEW) ||
                            url.startsWith(WebViewConstant.AppCallBack.CAN_BUY) ||
                            url.startsWith(WebViewConstant.AppCallBack.LIVE_VIDEO) ||
                            url.startsWith(WebViewConstant.AppCallBack.JUMP_PRODUCT_DETAIL) ||
                            url.startsWith(WebViewConstant.AppCallBack.INVITE_CUSTOM) ||
                            url.startsWith(WebViewConstant.AppCallBack.TOC_PRODUCT_TOUTIAO) ||
                            url.startsWith(WebViewConstant.AppCallBack.TOC_GO_PRODUCTLS) )) {
                CWebviewManger cWebClient = new CWebviewManger(curretnAtivity);
                cWebClient.setWeb((BaseWebview) webView);
                webviewOnClick.onClick(url);
            } else {
                /**
                 * 统一指令操作
                 */
                if(null==cWebClient)
                  cWebClient = new CWebviewManger(curretnAtivity);
                cWebClient.setWeb((BaseWebview) webView);
                cWebClient.setAction(url);
            }
            // view.loadUrl(loadUrl);

            loadUrl = loadUrl;
        } else if (url.startsWith("tel:")) {
            String[] vals = url.split(":");
            NavigationUtils.startDialgTelephone(curretnAtivity, vals[1]);
        } else {
            if (url.startsWith("http") || url.startsWith("https")) {
                webView.loadUrl(url);
                loadUrl = url;
            }
        }

        if (isShangxueyuanc) {
            baseWebview.shouldOverrideUrlLoading(webView, url);
        }
        return true;
    }

    @Override
    public void onReceivedError(WebView webView, int i, String s, String s1) {
        if (!TextUtils.isEmpty(s1) && !s1.endsWith("404.html")) {
            javaScriptObjectToc.setUrl(s1);
        }
        System.out.println("-----url= " + s1);
        webView.loadUrl("file:///android_asset/404.html");
    }

    @Override
    public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
        super.onReceivedSslError(webView, sslErrorHandler, sslError);
        //https忽略证书问题
        sslErrorHandler.proceed();
    }

    @Override
    public void onPageFinished(WebView webView, String s) {
        RxBus.get().post(RxConstant.REFRUSH_UNREAD_INFOMATION);
        super.onPageFinished(webView, s);
    }

    //此处可以有一个加载进度的接口  暂留 如有需要可以给暴露出来@wyk
    private onCWebViewListener mListener;

    public void setOnWebViewListener(onCWebViewListener listener) {
        this.mListener = listener;
    }

    //进度回调接口
    public interface onCWebViewListener {
        void onProgressChange(WebView view, int newProgress);

        void onPageFinish(WebView view);
    }

    //认购按钮的记得处理特殊处理!!!!!!!!!!!!!!!!!!!
    public interface WebviewOnClick {
        public void onClick(String result);
    }
}
