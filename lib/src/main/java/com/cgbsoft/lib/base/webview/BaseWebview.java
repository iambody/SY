package com.cgbsoft.lib.base.webview;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;

import com.cgbsoft.lib.utils.tools.Utils;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;

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
     * 这个接口是直播和认购时候的特殊url截取逻辑 单独回调给ui进行处理   @按照陈龙之前业务逻辑
     */

    public CWebClient.WebviewOnClick click;
    /**
     *  是否是商学院   @按照陈龙之前业务逻辑
     */

    private boolean isShangxueyuan = false;

    /**
     * 加载url
     * @param context
     */
    private String loadUrl;

    public BaseWebview(Context context) {
        super(context);
        initView(context);
    }

    public BaseWebview(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.wcontext = context;
        initView(context);

    }

    private void initView(Context context) {
        //开启js脚本支持
        getSettings().setJavaScriptEnabled(true);

        //适配手机大小
        getSettings().setUseWideViewPort(true);
        getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        getSettings().setLoadWithOverviewMode(true);
        getSettings().setSupportZoom(true);
        getSettings().setBuiltInZoomControls(true);
        getSettings().setDisplayZoomControls(false);

        javaScriptObject = new JavaScriptObjectToc(context, this);
        this.addJavascriptInterface(javaScriptObject, "simuyun");
        setWebChromeClient(new WVChromeClient());
        //目前先写C侧的Client 后续B重构会添加判断 添加B||C的Client
        setWebViewClient(new CWebClient(BaseWebview.this,javaScriptObject, wcontext,click,isShangxueyuan));
    }


    //进度显示
    private class WVChromeClient extends WebChromeClient {
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

        loadUrl = url;
        Log.e("webview_url", url);
//        synCookies(context, "https://app-intime.simuyun.com");
        this.loadUrl(url);
        loadUrl = url;
    }
    public void shouldOverrideUrlLoading(WebView view, String url) {

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                //TODO 去通知商学院  其他逻辑处理已经好了 只用确定按照那种方式进行调用即可
 //                EventBus.getDefault().post(new ShangxueyuanBackBean());

            }
        }, 500);

    }
    //这个是之前webview需要有的live和认购的特殊回调  @陈龙
    public void setClick(CWebClient.WebviewOnClick click) {
        this.click = click;
    }

    public boolean isShangxueyuan() {
        return isShangxueyuan;
    }

    public void setShangxueyuan(boolean isShangxueyuan) {
        this.isShangxueyuan = isShangxueyuan;
    }

}
