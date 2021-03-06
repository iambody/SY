package com.cgbsoft.lib.base.webview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.cgbsoft.lib.AppInfStore;
import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.BaseApplication;
import com.cgbsoft.lib.InvestorAppli;
import com.cgbsoft.lib.R;
import com.cgbsoft.lib.base.model.UserInfoDataEntity;
import com.cgbsoft.lib.base.model.bean.CredentialStateMedel;
import com.cgbsoft.lib.base.webview.bean.JsCall;
import com.cgbsoft.lib.contant.Contant;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.share.bean.ShareCommonBean;
import com.cgbsoft.lib.share.dialog.CommonNewShareDialog;
import com.cgbsoft.lib.share.dialog.CommonScreenDialog;
import com.cgbsoft.lib.share.dialog.CommonSharePosterDialog;
import com.cgbsoft.lib.share.utils.ShareManger;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.utils.constant.RxConstant;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.net.NetConfig;
import com.cgbsoft.lib.utils.poster.ElevenPoster;
import com.cgbsoft.lib.utils.poster.ScreenShot;
import com.cgbsoft.lib.utils.previewphoto.PhotoPreviewIntent;
import com.cgbsoft.lib.utils.rxjava.RxBus;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.utils.tools.BStrUtils;
import com.cgbsoft.lib.utils.tools.DeviceUtils;
import com.cgbsoft.lib.utils.tools.NavigationUtils;
import com.cgbsoft.lib.utils.tools.PromptManager;
import com.cgbsoft.lib.utils.tools.ThreadUtils;
import com.cgbsoft.lib.utils.tools.TrackingDataUtils;
import com.cgbsoft.lib.utils.tools.UiSkipUtils;
import com.cgbsoft.lib.utils.tools.Utils;
import com.cgbsoft.lib.widget.dialog.LoadingDialog;
import com.cgbsoft.privatefund.bean.UserInf;
import com.cgbsoft.privatefund.bean.commui.JsShareBean;
import com.cgbsoft.privatefund.bean.commui.OpenWebBean;
import com.cgbsoft.privatefund.bean.commui.WebRightTopViewConfigBean;
import com.cgbsoft.privatefund.bean.product.PublicFundInf;
import com.chenenyu.router.Router;
import com.google.gson.Gson;
import com.tencent.smtt.sdk.WebView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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
    private boolean isHindBack;

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

    /**
     * H5调用埋点
     *
     * @param param
     */
    @JavascriptInterface
    public void postEventTrackingData(String param) {
        try {
            JSONObject ja = new JSONObject(param);
            JSONObject data = ja.getJSONObject("data");
            String callback = ja.getString("callback");
            String d = data.getString("d");
            String e = data.optString("e");
            ThreadUtils.runOnMainThread(() -> {
                this.webView.loadUrl("javascript:" + callback + "()");
            });
            TrackingDataUtils.save(context, e, d);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    BaseWebViewActivity viewActivity;

    @JavascriptInterface
    public void openWebview(String param) {
        try {
            JSONObject ja = new JSONObject(param);
            JSONObject data = ja.getJSONObject("data");
            String callback = ja.getString("callback");
            OpenWebBean webBean = new Gson().fromJson(data.toString(), OpenWebBean.class);


            Intent intent = new Intent(context, BaseWebViewActivity.class);
            intent.putExtra(WebViewConstant.push_message_url, NetConfig.SERVER_ADD + webBean.getURL());
            intent.putExtra(WebViewConstant.push_message_title, webBean.getTitle());
            intent.putExtra(WebViewConstant.push_message_title_isdiv, webBean.isHasHTMLTag());
            intent.putExtra(WebViewConstant.push_message_title_is_hidetoolbar, true);

            InvestorAppli investorAppli = ((InvestorAppli) InvestorAppli.getContext());
            investorAppli.setWebBean(webBean);
            if (data.has("switchTab") && -1 != webBean.getSwitchTab()) {
//                RxBus.get().post(RxConstant.JUMP_H5_INDEX, getNavigation(webBean.getSwitchTab()));
                Router.build(RouteConfig.GOTOCMAINHONE).with("goWebTab", "1").with("switchTab", getNavigation(webBean.getSwitchTab())).go(context);
                return;
            }

            context.startActivity(intent);
            ThreadUtils.runOnMainThread(() -> {
                this.webView.loadUrl("javascript:" + callback + "()");
            });

        } catch (Exception e) {
        }
    }

    private int getNavigation(int tabPostion) {

        switch (tabPostion) {
            case 0:
                return WebViewConstant.Navigation.MAIN_PAGE;
            case 1:
                return WebViewConstant.Navigation.PRIVATE_BANK_PAGE;
            case 2:
                return WebViewConstant.Navigation.LIFE_ENJOY_PAGE;
            case 3:
                return WebViewConstant.Navigation.HEALTH_PAGE;
            case 4:
                return WebViewConstant.Navigation.MINE_PAGE;
            default:
                return WebViewConstant.Navigation.MINE_PAGE;


        }
    }

    @JavascriptInterface
    public void openCredentialsFolder(String param) {
        try {
            JSONObject ja = new JSONObject(param);
            JSONObject data = ja.getJSONObject("data");
            String callback = ja.getString("callback");
            ThreadUtils.runOnMainThread(() -> {
                this.webView.loadUrl("javascript:" + callback + "()");
            });
            CredentialStateMedel credentialStateMedel = new Gson().fromJson(data.toString(), CredentialStateMedel.class);
            if (!TextUtils.isEmpty(credentialStateMedel.getCustomerIdentity())) {
                if ("1001".equals(credentialStateMedel.getCustomerIdentity())) {  //身份证
                    if ("5".equals(credentialStateMedel.getIdCardState()) || "45".equals(credentialStateMedel.getIdCardState()) || ("50".equals(credentialStateMedel.getIdCardState()) && "0".equals(credentialStateMedel.getCustomerLivingbodyState()))) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("credentialStateMedel", credentialStateMedel);
                        NavigationUtils.startActivityByRouter(context, RouteConfig.CrenditralGuideActivity, bundle);
//                        Intent intent = new Intent(context, CrenditralGuideActivity.class);
//                        intent.putExtra("credentialStateMedel", credentialStateMedel);
//                        startActivity(intent);
                    } else if ("10".equals(credentialStateMedel.getIdCardState()) || "30".equals(credentialStateMedel.getIdCardState())) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("credentialStateMedel", credentialStateMedel);
                        NavigationUtils.startActivityByRouter(context, RouteConfig.UploadIndentityCradActivity, bundle);

//                        Intent intent = new Intent(getActivity(), UploadIndentityCradActivity.class);
//                        intent.putExtra("credentialStateMedel", credentialStateMedel);
//                        startActivity(intent);
                    } else {  //已通过 核身成功

//                        Intent intent = new Intent(getActivity(), CardCollectActivity.class);
//                        intent.putExtra("indentityCode", credentialStateMedel.getCustomerIdentity());
//                        startActivity(intent);
                    }
                } else {//  非大陆去证件列表
                    Bundle bundle = new Bundle();
                    bundle.putString("indentityCode", credentialStateMedel.getCustomerIdentity());
                    NavigationUtils.startActivityByRouter(context, RouteConfig.CardCollectActivity, bundle);
//                    Intent intent = new Intent(getActivity(), CardCollectActivity.class);
//                    intent.putExtra("indentityCode", credentialStateMedel.getCustomerIdentity());
//                    startActivity(intent);
                }
            } else {//无身份
                NavigationUtils.startActivityByRouter(context, RouteConfig.SelectIndentityActivity);
//                Intent intent = new Intent(getActivity(), SelectIndentityActivity.class);
//                startActivity(intent);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @JavascriptInterface
    public void finishInvestorConfig(String datas) {
        UserInfoDataEntity.UserInfo userInfo = AppManager.getUserInfo(context);
        userInfo.getToC().setCustomerSpecialFlag("1");
        AppInfStore.saveUserInfo(context, userInfo);
        RxBus.get().post(RxConstant.REFRESH_INVESTOR_INFO, 1);
    }


    //生成海报的监听
    @JavascriptInterface
    public void shareCustomizedImage(String datas) {
        JsCall jscall = new Gson().fromJson(datas, JsCall.class);

        String picPath = ElevenPoster.base64ToPath(jscall.getData(), System.currentTimeMillis() + "");

        CommonSharePosterDialog commonSharePosterDialog = new CommonSharePosterDialog(context, CommonSharePosterDialog.Tag_Style_WxPyq, picPath, new CommonSharePosterDialog.CommentShareListener() {
            @Override
            public void completShare(int shareType) {
                if (null != jscall && !BStrUtils.isEmpty(jscall.getCallback()))
                    ThreadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            if (webView != null) {
                                webView.loadUrl(String.format("javascript:%s(1)", jscall.getCallback()));
                            }
                        }
                    });
            }

            @Override
            public void cancleShare() {
                if (null != jscall && !BStrUtils.isEmpty(jscall.getCallback()))
                    ThreadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            if (webView != null) {
                                webView.loadUrl(String.format("javascript:%s(0)", jscall.getCallback()));
                            }
                        }
                    });
            }
        });
        commonSharePosterDialog.show();

    }

    //截屏通知的监听
    @JavascriptInterface
    public void shareScreenshot(String datas) {

        JsCall jscall = new Gson().fromJson(datas, JsCall.class);
        Bitmap paths = ScreenShot.GetandSaveCurrentImage((Activity) context);
        CommonScreenDialog commonScreenDialog = new CommonScreenDialog(context, paths, new CommonScreenDialog.CommentScreenListener() {
            @Override
            public void completShare() {
                if (null != jscall && !BStrUtils.isEmpty(jscall.getCallback()))
                    ThreadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            if (webView != null) {
                                webView.loadUrl(String.format("javascript:%s(1)", jscall.getCallback()));
                            }
                        }
                    });
            }

            @Override
            public void cancleShare() {
                if (null != jscall && !BStrUtils.isEmpty(jscall.getCallback()))
                    ThreadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            if (webView != null) {
                                webView.loadUrl(String.format("javascript:%s(0)", jscall.getCallback()));
                            }
                        }
                    });
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
    }

    @JavascriptInterface
    public void shareMyTrip(String datas) {
        JsCall jscall = new Gson().fromJson(datas, JsCall.class);

        shareTravelEquity(BStrUtils.nullToEmpty(jscall.getData()));

    }

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

    /**
     * 申购页面
     *
     * @param jsonObj
     */
    @JavascriptInterface
    public void subscribeFund(String jsonObj) {
        if (!BStrUtils.isEmpty(jsonObj)) {
            try {
                JSONObject object = new JSONObject(jsonObj);
                if (object.has("data")) {
                    String data = object.getString("data");
                    if (!BStrUtils.isEmpty(data)) {
                        JSONObject obj = new JSONObject(data);
                        String fundCode = obj.getString("fundCode");
                        String fundName = "";
                        String fundType = "";
                        if (obj.has("fundName")) {
                            fundName = obj.getString("fundName");
                        }
                        if (obj.has("fundType")) {
                            fundType = obj.getString("fundType");
                        }
                        String riskLevel = obj.getString("riskLevel");

                        UiSkipUtils.toBuyPublicFundFromNative((Activity) context, fundCode, fundName, fundType, riskLevel);

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

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
                        ThreadUtils.runOnMainThread(() -> {
                            if (webView != null) {
                                webView.loadUrl("javaScript:" + callName + "('" + vas + "')");
                            } else if (googleWebView != null) {
                                googleWebView.loadUrl("javaScript:" + callName + "('" + vas + "')");
                            }
                        });

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

    /**
     * 打开绑卡
     */
    @JavascriptInterface
    public void bindBankCard(String jsonstr) {
        try {
            JSONObject object = new JSONObject(jsonstr);
            String data = object.getString("data");
            String callback = object.getString("callback");
            ThreadUtils.runOnMainThread(() -> {
                webView.loadUrl(String.format("javascript:%s()", callback));
            });
            RxBus.get().post(RxConstant.REFRESH_PUBLIC_FUND_INFO, 10);
            HashMap<String, Object> map = new HashMap<>();
            map.put("tag_parameter", data);
            NavigationUtils.startActivityByRouter(context, RouteConfig.GOTO_PUBLIC_FUND_BIND_BANK_CARD, map);
            RxBus.get().post(RxConstant.REFRESH_PUBLIC_FUND_INFO, 10);
            ((Activity) context).finish();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //    同步用户信息
    @JavascriptInterface
    public void syncAccountInfo(String jsonstr) {

        RxBus.get().post(RxConstant.REFRESH_PUBLIC_FUND_INFO, 10);
        if (!BStrUtils.isEmpty(jsonstr)) {
            try {
                JSONObject object = new JSONObject(jsonstr);


                if (object.has("data")) {
                    String result = object.getString("data");
                    if (!BStrUtils.isEmpty(result)) {
                        JSONObject obj = new JSONObject(result);
                        PublicFundInf publicFundInf = AppManager.getPublicFundInf(context.getApplicationContext());


                        if (obj.has("custNo") && !BStrUtils.isEmpty(obj.getString("custNo"))) {
                            publicFundInf.setCustNo(obj.getString("custNo"));
                            if (obj.has("type")) {
                                publicFundInf.setType(obj.getString("type"));
                            }
                            AppInfStore.savePublicFundInf(context, publicFundInf);

                        }
                        if (obj.has("custRisk") && !BStrUtils.isEmpty(obj.getString("custRisk"))) {
                            publicFundInf.setCustRisk(obj.getString("custRisk"));
                            if (obj.has("type")) {
                                publicFundInf.setType(obj.getString("type"));
                            }
                            AppInfStore.savePublicFundInf(context, publicFundInf);
//                            webView.loadUrl(String.format("javascript:%s()", object.getString("callback")));
                        }

                        if (obj.has("custRisk") && !BStrUtils.isEmpty(obj.getString("custRisk"))) {
                            publicFundInf.setCustRisk(obj.getString("custRisk"));
                            if (obj.has("type")) {
                                publicFundInf.setType(obj.getString("type"));
                            }
//                            AppInfStore.savePublicFundInf(context, publicFundInf);
                            RxBus.get().post(RxConstant.REFRESH_PUBLIC_FUND_INFO, 10);
//                            webView.loadUrl(String.format("javascript:%s()", object.getString("callback")));
                        }
                    }
                }


                if (object.has("callback")) {//调用js
                    ThreadUtils.runOnMainThread(() -> {
                        if (webView != null) {
                            try {
                                webView.loadUrl(String.format("javascript:%s()", object.getString("callback")));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    //完成开户流程
    @JavascriptInterface
    public void finishOpeningFundAccount(String jsostr) {

        RxBus.get().post(RxConstant.REFRESH_PUBLIC_FUND_INFO, 10);
        RxBus.get().post(RxConstant.REFRESH_PUBLIC_FUND_INFO, 9);
        Router.build(RouteConfig.GOTOCMAINHONE).with("tobuypublicfund", "1").go(context);
//        RxBus.get().post(RxConstant.MAIN_PUBLIC_TO_BUY, 11);
        try {
            JSONObject object = new JSONObject(jsostr);
            String callBack = object.getString("callback");
            ThreadUtils.runOnMainThread(() -> {
                if (webView != null) {
                    webView.loadUrl(String.format("javascript:%s()", callBack));
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //开户
    @JavascriptInterface
    public void openFundAccount(String jsostr) {
        Log.i("sss", jsostr);

        //跳转到开户页面*************************
        UiSkipUtils.toPublicFundRegist((Activity) context);

    }

    /**
     * 赎回结果页 点击确定 直接回到首页
     *
     * @param jsostr
     */
    @JavascriptInterface
    public void destroyWebview(String jsostr) {
//        Router.build(RouteConfig.GOTOCMAINHONE).go(context);
        RxBus.get().post(RxConstant.REFRESH_PUBLIC_FUND_INFO, 10);
        ((Activity) context).finish();
    }

    @JavascriptInterface
    public void redeemFund(String jsostr) {
        try {
            JSONObject object = new JSONObject(jsostr);
            if (object.has("data")) {
                String data = object.getString("data");
                //跳转到赎回*************************
                UiSkipUtils.gotoRedeemFund((Activity) context, data);
            }
        } catch (Exception e) {
        }
    }

    /**
     * 分享旅游权益
     *
     * @param sharContext
     */
    public void shareTravelEquity(String sharContext) {
        ShareCommonBean shareCommonBean = new ShareCommonBean();
        shareCommonBean.setShareContent(sharContext);
        CommonNewShareDialog commonNewShareDialog = new CommonNewShareDialog(context, CommonNewShareDialog.Tag_Style_NoteWxCopy, shareCommonBean, new CommonNewShareDialog.CommentShareListener() {
            @Override
            public void completShare(int shareType) {

            }

            @Override
            public void cancleShare() {

            }
        });
        commonNewShareDialog.show();
    }

    /**
     * 公共分享框架
     */
    public void commonShareOrder(String jsonstr) {
        JSONObject object = null;
        try {
            object = new JSONObject(jsonstr);
            String data = object.getString("data");
            String callback = object.getString("callback");
            if (BStrUtils.isEmpty(data)) return;
            JsShareBean jsShareBean = new Gson().fromJson(data, JsShareBean.class);
            if (null == jsShareBean.getResource()) return;
            shareResultListenr shareResultListenr = new shareResultListenr(jsShareBean.getTyp());
            switch (jsShareBean.getTyp()) {//1:html（default）, 2:image, 3:miniprogram, 4:text, 5:video, 6:audio
                case "1":
                    ShareManger.getInstance(context, new ShareCommonBean(jsShareBean.getResource().getTitle(), jsShareBean.getResource().getConten(), jsShareBean.getResource().getPageURL(), jsShareBean.getResource().getImage()), shareResultListenr).goShareWx(ShareManger.WXSHARE);
                    break;
                case "2":
                    Bitmap bitmap = null; //需要确保分享的bitmap部位null
                    ShareManger.getInstance(context, new ShareCommonBean(bitmap), shareResultListenr);
                    break;
                case "3":
                    ShareManger.getInstance(context, new ShareCommonBean(jsShareBean.getResource().getTitle(), jsShareBean.getResource().getConten(), jsShareBean.getResource().getPageURL(), jsShareBean.getResource().getImage()), shareResultListenr).goShareWx(ShareManger.WXMINIPROGRAM);
                    break;
                case "4"://文本
                    ShareManger.getInstance(context, new ShareCommonBean(jsShareBean.getResource().getTitle(), jsShareBean.getResource().getConten(), jsShareBean.getResource().getPageURL(), jsShareBean.getResource().getImage()), shareResultListenr).goShareWx(ShareManger.CIRCLETXT);
                    break;
                case "5"://视频
                    ShareManger.getInstance(context, new ShareCommonBean(jsShareBean.getResource().getTitle(), jsShareBean.getResource().getConten(), jsShareBean.getResource().getPageURL(), jsShareBean.getResource().getImage()), shareResultListenr).goShareWx(ShareManger.CIRCLEVIDEO);

                    break;
                case "6"://音频
                    ShareManger.getInstance(context, new ShareCommonBean(jsShareBean.getResource().getTitle(), jsShareBean.getResource().getConten(), jsShareBean.getResource().getPageURL(), jsShareBean.getResource().getImage()), shareResultListenr).goShareWx(ShareManger.CIRCLMUSICE);

                    break;
                default:
                    ShareManger.getInstance(context, new ShareCommonBean(jsShareBean.getResource().getTitle(), jsShareBean.getResource().getConten(), jsShareBean.getResource().getPageURL(), jsShareBean.getResource().getImage()), shareResultListenr).goShareWx(ShareManger.WXSHARE);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 公用分享时候的回调  不需要可以直接初始化时候职位null
     */
    class shareResultListenr implements com.cgbsoft.lib.share.utils.ShareManger.ShareResultListenr {
        private String type;//1:html（default）, 2:image, 3:miniprogram, 4:text, 5:video, 6:audio

        public shareResultListenr(String type) {
            this.type = type;
        }

        @Override
        public void completShare() {
            //某种类型需要分享成功时候的回调

        }

        @Override
        public void errorShare() {

        }

        @Override
        public void cancelShare() {

        }
    }

    @JavascriptInterface
    public void getUserInfo(String jsostr) {
        if (BStrUtils.isEmpty(jsostr)) return;
        JsCall jscall = new Gson().fromJson(jsostr, JsCall.class);
        Context rContext = context instanceof Activity ? context.getApplicationContext() : context;
        UserInfoDataEntity.UserInfo userInfo = AppManager.getUserInfo(rContext);
        UserInf userInf = new Gson().fromJson(new Gson().toJson(userInfo), UserInf.class);
//        Map<String, String> map = new HashMap<>(1, 1);
//        JSONObject object=new Gson().fromJson(new Gson().toJson(userInf),JSONObject.class);new Gson().toJson(userInf)
//          webView.loadUrl("javascript:Command.aaaa()");
        ThreadUtils.runOnMainThread(() -> {
            if (webView != null) {
                if (null != jscall && !BStrUtils.isEmpty(jscall.getCallback()))
                    webView.loadUrl(String.format("javascript:%s(\'%s\')", jscall.getCallback(), new Gson().toJson(userInf)));
            }
        });

    }

    /**
     * 右上角的  按钮显示
     *
     * @param data
     */
    @JavascriptInterface
    public void setWebviewConfig(String data) {

        try {
            JSONObject jsonObject = new JSONObject(data);
            WebRightTopViewConfigBean webRightTopViewConfigBean = new Gson().fromJson(jsonObject.getString("data"), WebRightTopViewConfigBean.class);

            webRightTopViewConfigBean.setOpenWebUrl(((InvestorAppli) (((Activity) context).getApplication())).getOpenWebUrl());
            ((BaseWebViewActivity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ((BaseWebViewActivity) context).setWebRightTopViewConfig(webRightTopViewConfigBean);
                }
            });


            String callback = jsonObject.getString("callback");
            ThreadUtils.runOnMainThread(() -> {
                if (webView != null) {
                    webView.loadUrl(String.format("javascript:%s()", callback));
                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * 长时间的toast
     *
     * @param data
     */
    @JavascriptInterface
    public void showToast(String data) {// {"data":{"text":"显示失败！","duration":5},"callback":"Command.func10f8c6cc988d4ababcff2e4f77a50bb3"}
        try {
            JSONObject jsonObject = new JSONObject(data);
            String datas = jsonObject.getString("data");
            String text = (new JSONObject(datas)).getString("text");
            int duration = (new JSONObject(datas)).getInt("duration");
            PromptManager.ShowCustomLongToast(context, text, duration);

            String callback = jsonObject.getString("callback");
            ThreadUtils.runOnMainThread(() -> {
                if (webView != null) {
                    webView.loadUrl(String.format("javascript:%s()", callback));
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * 轮播的 图片
     *
     * @param data
     */
    @JavascriptInterface
    public void previewImagesByURLs(String data) {// {"data":{"images":["https://www.apple.com/ac/flags/1/images/cn/32.png","https://www.apple.com/ac/flags/1/images/cn/32.png"]},"callback":"Command.func34f013e1dca9490f90b3dee38d4c2003"}
        try {
            JSONObject jsonObject = new JSONObject(data);
            String callback = jsonObject.getString("callback");
            String images = jsonObject.getString("data");
            String igs = new JSONObject(images).getString("images");
            PhotoPreviewIntent intent = new PhotoPreviewIntent(context);
            List<String> ivs = new Gson().fromJson(igs, new ArrayList<String>().getClass());
            intent.setPhotoPaths(ivs);//预览图片对象列表
            intent.setDefluatDrawble(R.drawable.logo);//加载错误时的图片
            intent.launch();
            ThreadUtils.runOnMainThread(() -> {
                if (webView != null) {
                    webView.loadUrl(String.format("javascript:%s()", callback));
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 弹框  场景  公募风险测评 按返回键 mWebview.loadUrl("javascript:WebView.back(0)") 后 会调用这个指令
     *
     * @param data
     */
    @JavascriptInterface
    public void showModal(String data) {

    }

    /**
     * 单纯的关闭  页面
     *
     * @param data
     */
    @JavascriptInterface
    public void closeWebview(String data) {
        RxBus.get().post(RxConstant.REFRESH_PUBLIC_FUND_INFO, 10);
        RxBus.get().post(RxConstant.REFRESH_PUBLIC_FUND_INFO, 9);
        try {
            ((Activity) context).finish();
        } catch (Exception e) {
        }
//        Router.build(RouteConfig.GOTOCMAINHONE).go(context);
    }


//    /**
//     * 单纯的 隐藏返回键盘 或者物理键盘无反应
//     */
//    @JavascriptInterface
//    public void hideReturnButton(String data) {
//        try {
//            ((Activity)context).runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    ((BaseWebViewActivity) context).hideBackIv();
//                }
//            });
//        } catch (Exception e) {
//        }
//    }

}
