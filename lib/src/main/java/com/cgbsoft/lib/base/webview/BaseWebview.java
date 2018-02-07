package com.cgbsoft.lib.base.webview;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.cgbsoft.lib.R;
import com.cgbsoft.lib.utils.tools.Utils;
import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/5/4-17:32
 */
public class BaseWebview extends WebView {
    /**
     * C端对象注入时候的Js对象  仅仅给C端使用
     */
    private JavaScriptObjectToc javaScriptObject;
    /**
     * 上下文
     */
    private Context wcontext;

    /**
     * 这个接口是直播和认购时候的特殊url截取逻辑 单独回调给ui进行处理   @按照陈龙之前业务 逻辑
     */

    public CWebClient.WebviewOnClick click;

    private CWebClient cWebClient;

    /**
     * WebChromeClient回调方法
     */
    private android.webkit.WebChromeClient.CustomViewCallback myCallback;
    /**
     * 是否是商学院   @按照陈龙之前业务逻辑
     */
    private boolean isShangxueyuan = false;

    private boolean isInitData;

    /**
     * 加载url
     *
     * @param context
     */
    private String loadUrl;

    public BaseWebview(Context context) {
        super(context);
        initView(context);
    }

    public BaseWebview(Context context, boolean isInitData) {
        super(context);
        this.isInitData = isInitData;
        initView(context);
    }

    public BaseWebview(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.wcontext = context;
        parseAttr(context, attributeSet);
        initView(context);
    }

    private void parseAttr(Context context, AttributeSet attributeSet) {
        TypedArray ta = context.obtainStyledAttributes(attributeSet, R.styleable.initWebView);
        isInitData = ta.getBoolean(R.styleable.initWebView_init, false);
//       System.out.println("-------initData=" + isInitData);
        ta.recycle();
    }

    private void initView(Context context) {
        //开启js脚本支持
        getSettings().setJavaScriptEnabled(true);
        getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        //适配手机大小
        getSettings().setUseWideViewPort(true);
        getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        getSettings().setLoadWithOverviewMode(true);
//        getSettings().setSupportZoom(true);
        getSettings().setBuiltInZoomControls(true);
        getSettings().setAllowFileAccess(true); // 允许访问文件
//        getSettings().setDisplayZoomControls(false);
        getSettings().setSavePassword(true);
        getSettings().setSaveFormData(true);
        getSettings().setGeolocationEnabled(true);
        getSettings().setDomStorageEnabled(true);
        getSettings().setCacheMode(android.webkit.WebSettings.LOAD_NO_CACHE);
        getSettings().setAppCacheEnabled(false);
        getSettings().setDefaultFontSize(16);
        //硬件加速暂时不用  目前Android不支持view级别开启硬件加速
//        this.setLayerType(View.LAYER_TYPE_HARDWARE,null);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            this.setWebContentsDebuggingEnabled(true);
        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
//        } else {
        getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
//        }
        javaScriptObject = new JavaScriptObjectToc(context, this);
        this.addJavascriptInterface(javaScriptObject, "simuyun");
        setWebChromeClient(new WVChromeClient());
        cWebClient = new CWebClient(BaseWebview.this, javaScriptObject, wcontext, click, isShangxueyuan);
        //目前先写C侧的Client 后续B重构会添加判断 添加B||C的Client
        setWebViewClient(isInitData ? new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String s) {
                return true;
            }
        } : cWebClient);
//        this.setWebContentsDebuggingEnabled(true);
    }

    //进度显示
    public class WVChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
//                progressbar.setVisibility(GONE);
            } else {
//                if (progressbar.getVisibility() == GONE)
//                    progressbar.setVisibility(VISIBLE);
//                progressbar.setProgress(newProgress);
            }

//            if (mListener != null) {
//                mListener.onProgressChange(view, newProgress);
//            }
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public void onShowCustomView(View view, IX5WebChromeClient.CustomViewCallback callback) {
            super.onShowCustomView(view, 0, callback);
            if (myCallback != null) {
                myCallback.onCustomViewHidden();
                myCallback = null;
                return;
            }
        }

        @Override
        public void onHideCustomView() {
            if (myCallback != null) {
                myCallback.onCustomViewHidden();
                myCallback = null;
            }
        }
    }

    /**
     * 加载url，加一些别的参数
     *
     * @param url
     */
    public void loadUrls(String url) {
        int[] widthHeight = Utils.getWidthHeight(wcontext);
        if (url.contains("?")) {
            url += "&w=" + widthHeight[0] + "&h=" + widthHeight[1];
        } else {
            url += "?w=" + widthHeight[0] + "&h=" + widthHeight[1];
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMDDhhmmss");
        String time = sdf.format(new Date(System.currentTimeMillis()));
        url = url + "&t=" + time;
        url = url.replace("%2F", "/");
        //loadingNoNetPic();
        Log.e("webview_url", url);
        this.loadUrl(url);
        loadUrl = url;
    }

    public void shouldOverrideUrlLoading(WebView view, String url) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
            }
        }, 500);
    }

    //这个是之前webview需要有的live和认购的特殊回调  @陈龙
    public void setClick(CWebClient.WebviewOnClick click) {
        this.click = click;
        cWebClient.setWebviewOnClick(click);
    }

    public boolean isShangxueyuan() {
        return isShangxueyuan;
    }

    public void setShangxueyuan(boolean isShangxueyuan) {
        this.isShangxueyuan = isShangxueyuan;
    }

}
