package com.cgbsoft.lib.widget;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.cgbsoft.lib.base.webview.CWebClient;
import com.cgbsoft.lib.base.webview.JavaScriptObjectToc;

/**
 * @author chenlong
 */
public class MyBaseWebview extends WebView {

    private OnScrollChangedCallback onScrollChangedCallback;

    private GestureDetector gestureDetector;
    private Context cb;
    private JavaScriptObjectToc javaScriptObject;
    private CWebClient cWebClient;

    public MyBaseWebview(Context context) {
        super(context);
        cb = context;
        initView(context);
        gestureDetector = new GestureDetector(this.getContext(),
                onGestureListener);
    }

    public void setOnScrollChangedCallback(OnScrollChangedCallback onScrollChangedCallback) {
        this.onScrollChangedCallback = onScrollChangedCallback;
    }

    public MyBaseWebview(Context context, AttributeSet attrs) {
        super(context, attrs);
        cb = context;
        initView(context);
        gestureDetector = new GestureDetector(this.getContext(),
                onGestureListener);
    }

    private void initView(Context context) {
        //开启js脚本支持
        getSettings().setJavaScriptEnabled(true);
        getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        //适配手机大小
        getSettings().setUseWideViewPort(true);
        getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        getSettings().setLoadWithOverviewMode(true);
        getSettings().setBuiltInZoomControls(true);
        getSettings().setSavePassword(true);
        getSettings().setSaveFormData(true);
        getSettings().setGeolocationEnabled(true);
        getSettings().setDomStorageEnabled(true);
        getSettings().setCacheMode(android.webkit.WebSettings.LOAD_NO_CACHE);
        getSettings().setAppCacheEnabled(false);
        getSettings().setDefaultFontSize(16);
        if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.KITKAT) {
            this.setWebContentsDebuggingEnabled(true);
        }
        javaScriptObject = new JavaScriptObjectToc(context, this);
        this.addJavascriptInterface(javaScriptObject, "simuyun");
//        setWebChromeClient(new BaseWebview.WVChromeClient());
//        cWebClient = new CWebClient(BaseWebview.this, javaScriptObject, wcontext, click, isShangxueyuan);
//        //目前先写C侧的Client 后续B重构会添加判断 添加B||C的Client
//        setWebViewClient(isInitData ? new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(com.tencent.smtt.sdk.WebView webView, String s) {
//                return true;
//            }
//        } : cWebClient);
    }


    // 重载滑动事件
    @Override
    public boolean onTouchEvent(MotionEvent evt) {
        gestureDetector.onTouchEvent(evt);
        return super.onTouchEvent(evt);
    }

    private GestureDetector.OnGestureListener onGestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            if (getContentHeight() * getScale() > getHeight()) {
                float y = e2.getY() - e1.getY();
                if (y > 100) {
                    // 右滑 事件
                    onScrollChangedCallback.onScrollDown();
                } else if (y < -100) {
                    // 左滑事件
                    onScrollChangedCallback.onScrollUp();
                }
            }
            return true;
        }
    };

    public interface OnScrollChangedCallback {
        void onScrollUp();
        void onScrollDown();
    }
}