package com.cgbsoft.lib.base.webview;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.BaseApplication;
import com.cgbsoft.lib.InvestorAppli;
import com.cgbsoft.lib.base.webview.bean.JsCall;
import com.cgbsoft.lib.contant.Contant;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.share.dialog.CommonScreenDialog;
import com.cgbsoft.lib.share.dialog.CommonSharePosterDialog;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.poster.ElevenPoster;
import com.cgbsoft.lib.utils.poster.ScreenShot;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.utils.tools.BStrUtils;
import com.cgbsoft.lib.utils.tools.DeviceUtils;
import com.cgbsoft.lib.utils.tools.NavigationUtils;
import com.cgbsoft.lib.utils.tools.ThreadUtils;
import com.cgbsoft.lib.utils.tools.Utils;
import com.cgbsoft.lib.widget.dialog.LoadingDialog;
import com.google.gson.Gson;
import com.tencent.smtt.sdk.WebView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/5/4-17:59
 */
public class JavaScriptObjectToc {
    private Context context;
    private WebView webView;
    private android.webkit.WebView googleWebView;
    private String url;
    private LoadingDialog mLoadingDialog;

    public JavaScriptObjectToc(Context context, WebView webView) {
        this.context = context;
        this.webView = webView;
        mLoadingDialog = LoadingDialog.getLoadingDialog(context, "", false, false);
    }

    public JavaScriptObjectToc(Context context, android.webkit.WebView googleWebView) {
        this.context = context;
        this.googleWebView = googleWebView;
        mLoadingDialog = LoadingDialog.getLoadingDialog(context, "", false, false);
    }

    public void setUrl(String url) {
        Log.i(this.getClass().getName(), "url==" + url);
        this.url = url;
    }

    /**
     * 暴露 给H5 用户信息的 对象接口
     *
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

    private void hideLoadDialog() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
    }

    private void showLoadDialog() {
        if (null == mLoadingDialog) {
            mLoadingDialog = LoadingDialog.getLoadingDialog(context, "", false, false);
        }
        if (!mLoadingDialog.isShowing()) {
            mLoadingDialog.show();
        }
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
            } else if (googleWebView != null) {
                googleWebView.loadUrl(url);
            }
        });
    }

    //生成海报的监听
    @JavascriptInterface
    public void shareCustomizedImage(String datas) {
        JsCall jscall = new Gson().fromJson(datas, JsCall.class);

        String picPath = ElevenPoster.base64ToPath(jscall.getData(), System.currentTimeMillis() + "");

        CommonSharePosterDialog commonSharePosterDialog = new CommonSharePosterDialog(context, CommonSharePosterDialog.Tag_Style_WxPyq, picPath, new CommonSharePosterDialog.CommentShareListener() {
            @Override
            public void completShare(int shareType) {
                if (null!=jscall&&!BStrUtils.isEmpty(jscall.getCallback()))
                    webView.loadUrl(String.format("javascript:%s(1)", jscall.getCallback()));
            }

            @Override
            public void cancleShare() {
                if (null!=jscall&&!BStrUtils.isEmpty(jscall.getCallback()))
                    webView.loadUrl(String.format("javascript:%s(0)", jscall.getCallback()));
            }
        });
        commonSharePosterDialog.show();

    }

    //截屏通知的监听
    @JavascriptInterface
    public void shareScreenshot(String datas) {

        JsCall jscall = new Gson().fromJson(datas, JsCall.class);
        String paths = ScreenShot.GetandSaveCurrentImage((Activity) context);
        CommonScreenDialog commonScreenDialog = new CommonScreenDialog(context, paths, new CommonScreenDialog.CommentScreenListener() {
            @Override
            public void completShare() {
                if (null!=jscall&&!BStrUtils.isEmpty(jscall.getCallback()))
                    webView.loadUrl(String.format("javascript:%s(1)", jscall.getCallback()));
            }

            @Override
            public void cancleShare() {
                if (null!=jscall&&!BStrUtils.isEmpty(jscall.getCallback()))
                    webView.loadUrl(String.format("javascript:%s(0)", jscall.getCallback()));
            }
        });
        commonScreenDialog.show();
    }

    @JavascriptInterface
    public void sendRemoteRequest(String requestMethod, String addressUrl, String params, String javascriptCallMethod) {
        if ("get".equals(requestMethod.toLowerCase())) {
            requestGetMethodCallBack(addressUrl, params, javascriptCallMethod);
        } else if ("post".equals(requestMethod.toLowerCase())) {
            requestPostMethod(addressUrl, params, javascriptCallMethod);
        }
    }

    @JavascriptInterface
    public String getRequestValue(String key) {
        InvestorAppli investorAppli = ((InvestorAppli) InvestorAppli.getContext());
        if (investorAppli.getServerDatahashMap() != null) {
            String hasVas = investorAppli.getServerDatahashMap().get(key);
            return hasVas;
        }
        return "";
    };

    private void requestGetMethodCallBack(String url, String params, String javascirptCallMethod) {
        System.out.println("---javascirptCallMethod=" + javascirptCallMethod);
        ThreadUtils.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                showLoadDialog();
            }
        });
        ApiClient.getCommonGetRequest(url, formatJsonObjectToHashMap(params)).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String sa) {
                Log.d("HealthBespeakPresenterw", "----" + sa);
                InvestorAppli investorAppli = ((InvestorAppli) InvestorAppli.getContext());
                investorAppli.getServerDatahashMap().put(javascirptCallMethod, sa);
                ThreadUtils.runOnMainThread(() -> {
                    if (webView != null) {
                        webView.loadUrl("javascript:" + javascirptCallMethod + "('200')");
                    } else if (googleWebView != null) {
                        googleWebView.loadUrl("javascript:" + javascirptCallMethod + "('200')");
                    }
                    hideLoadDialog();
                });
            }

            @Override
            protected void onRxError(Throwable error) {
                System.out.println("---error message=" + error.getMessage());
                ThreadUtils.runOnMainThread(() -> {
                    if (webView != null) {
                        webView.loadUrl("javascript:" + javascirptCallMethod + "('501')");
                    } else if (googleWebView != null) {
                        googleWebView.loadUrl("javascript:" + javascirptCallMethod + "('501')");
                    }
                    hideLoadDialog();
                });
            }
        });
    }

    private HashMap formatJsonObjectToHashMap(String param) {
        HashMap hashMap = new HashMap<>();
        if (TextUtils.isEmpty(param)) {
            return hashMap;
        }
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(param);
            Iterator<String> iterator = jsonObject.keys();
            while (iterator.hasNext()) {
                String key = iterator.next();
                hashMap.put(key, jsonObject.get(key));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return hashMap;
    }

    @JavascriptInterface
    public void activateNativeLivePlayer(String jsonObject) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("liveJson", jsonObject);
        NavigationUtils.startActivityByRouter(InvestorAppli.getContext(), RouteConfig.GOTOLIVE, hashMap);
        try {
            JSONObject jsonObject1 = new JSONObject(jsonObject);
            SPreference.putString(InvestorAppli.getContext(), Contant.CUR_LIVE_ROOM_NUM, jsonObject1.getString("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void requestPostMethod(String url, String params, String javascirptCallMethod) {
        ThreadUtils.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                showLoadDialog();
            }
        });
        ApiClient.getCommonPostRequest(url, formatJsonObjectToHashMap(params)).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String sa) {
                Log.d("requestPostMethod", "----" + sa);
                ThreadUtils.runOnMainThread(() -> {
                    InvestorAppli investorAppli = ((InvestorAppli) InvestorAppli.getContext());
                    investorAppli.getServerDatahashMap().put(javascirptCallMethod, sa);
                    if (webView != null) {
                        webView.loadUrl("javascript:" + javascirptCallMethod + "('200')");
                    } else if (googleWebView != null) {
                        googleWebView.loadUrl("javascript:" + javascirptCallMethod + "('200')");
                    }
                    hideLoadDialog();
                });
            }

            @Override
            protected void onRxError(Throwable error) {
                System.out.println("---requestPostMethod error message=" + error.getMessage());
                ThreadUtils.runOnMainThread(() -> {
                    if (webView != null) {
                        webView.loadUrl("javascript:" + javascirptCallMethod + "('501')");
                    } else if (googleWebView != null) {
                        googleWebView.loadUrl("javascript:" + javascirptCallMethod + "('501')");
                    }
                    hideLoadDialog();
                });
            }
        });
    }

    /**
     * H5存取数据方法
     */
    @JavascriptInterface
    public void postMessage(String params) {
        if (!TextUtils.isEmpty(params)) {
            try {
                JSONObject jsonObject = new JSONObject(params);
                String eventName = jsonObject.getString("event");
                switch (eventName) {
                    case "modifyWebTitle":
                        String name = jsonObject.getString("title");
                        modifyWebViewTitleName(name);
                        break;
                    case "setTempData":
                        String keyName = jsonObject.optString("identification");
                        String values = jsonObject.optString("data");
                        putValue(keyName, values);
                        break;
                    case "getTempData":
                        String key = jsonObject.optString("identification");
                        String callName = jsonObject.optString("callback");
                        String vas = getStringValue(key);
                        if (webView != null) {
                            webView.loadUrl("javaScript:" + callName + "('" + vas + "')");
                        } else if (googleWebView != null) {
                            googleWebView.loadUrl("javaScript:" + callName + "('" + vas + "')");
                        }
                        break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * H5 存储 Key Value
     */
    private void putValue(String key, String value) {
        Map<String, String> map = new HashMap<String, String>();
        String javascriptInterfaceSP = SPreference.getString(context, "JavascriptInterfaceSP");
        if (javascriptInterfaceSP != null) {
            map = transStringToMap(javascriptInterfaceSP);
        }
        map.put(key, value);
        SPreference.putString(context, "JavascriptInterfaceSP", transMapToString(map));
    }

    /**
     * H5 取值
     */
    private String getStringValue(String key) {
        String javascriptInterfaceSP = SPreference.getString(context, "JavascriptInterfaceSP");
        if (javascriptInterfaceSP != null) {
            Map map = transStringToMap(javascriptInterfaceSP);
            return (String) map.get(key);
        }
        return "";
    }

    private void modifyWebViewTitleName(String name) {
        if (context instanceof BaseWebViewActivity) {
            BaseWebViewActivity baseWebViewActivity = (BaseWebViewActivity) context;
            if (!TextUtils.isEmpty(name)) {
                ThreadUtils.runOnMainThreadDelay(new Runnable() {
                    @Override
                    public void run() {
                        baseWebViewActivity.modifyTitleName(name);
                    }
                }, 500);
            }
        }
    }

    /**
     * Map to String
     *
     * @param map
     * @return
     */
    public static String transMapToString(Map map) {
        java.util.Map.Entry entry;
        StringBuffer sb = new StringBuffer();
        for (Iterator iterator = map.entrySet().iterator(); iterator.hasNext(); ) {
            entry = (java.util.Map.Entry) iterator.next();
            sb.append(entry.getKey().toString()).append("'").append(null == entry.getValue() ? "" :
                    entry.getValue().toString()).append(iterator.hasNext() ? "^" : "");
        }
        return sb.toString();
    }

    /**
     * String To Map
     *
     * @param mapString
     * @return
     */
    public static Map transStringToMap(String mapString) {
        Map map = new HashMap();
        java.util.StringTokenizer items;
        for (StringTokenizer entrys = new StringTokenizer(mapString, "^"); entrys.hasMoreTokens();
             map.put(items.nextToken(), items.hasMoreTokens() ? ((Object) (items.nextToken())) : null))
            items = new StringTokenizer(entrys.nextToken(), "'");
        return map;
    }


}
