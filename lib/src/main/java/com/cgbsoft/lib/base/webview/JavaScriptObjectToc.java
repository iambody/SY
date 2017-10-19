package com.cgbsoft.lib.base.webview;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.BaseApplication;
import com.cgbsoft.lib.InvestorAppli;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.utils.tools.DeviceUtils;
import com.cgbsoft.lib.utils.tools.ThreadUtils;
import com.cgbsoft.lib.utils.tools.Utils;

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

    @JavascriptInterface
    public void sendRemoteRequest(String requestMethod, String addressUrl, String params, String javascriptCallMethod) {
        if ("get".equals(requestMethod.toLowerCase())) {
            requestGetMethodCallBack(addressUrl, params, javascriptCallMethod);
//            requestGetMethod(addre、ssUrl, params, javascriptCallMethod);
        } else if ("post".equals(requestMethod.toLowerCase())) {
            requestPostMethod(addressUrl, params, javascriptCallMethod);
        }
    }

    @JavascriptInterface
    public String getRequestValue(String key) {
        InvestorAppli investorAppli = ((InvestorAppli)InvestorAppli.getContext());
        if (investorAppli.getServerDatahashMap() != null) {
            String hasVas = investorAppli.getServerDatahashMap().get(key);
            return hasVas;
        }
        return "";
    };

    private void requestGetMethodCallBack(String url, String params, String javascirptCallMethod) {
        String[] vas = params.split("&");
        HashMap<String, String> hashMap = new HashMap<>();
        for(String targ: vas) {
            String[] va = targ.split("=");
            hashMap.put(va[0], va[1]);
        }
        System.out.println("---javascirptCallMethod=" + javascirptCallMethod);
        ApiClient.getCommonGetRequest(url,hashMap).subscribe(new RxSubscriber<String>(){
            @Override
            protected void onEvent(String sa) {
                Log.d("HealthBespeakPresenterw", "----"+ sa);
                InvestorAppli investorAppli = ((InvestorAppli)InvestorAppli.getContext());
                investorAppli.getServerDatahashMap().put(javascirptCallMethod, sa);
                ThreadUtils.runOnMainThread(() -> {
                    webView.loadUrl("javascript:" + javascirptCallMethod + "()");
                });
            }

            @Override
            protected void onRxError(Throwable error) {
                System.out.println("---error message=" + error.getMessage());
            }
        });
    }

    private void requestGetMethod(String url, String params, String javascirptCallMethod) {
        String[] vas = params.split("&");
        HashMap<String, String> hashMap = new HashMap<>();
        for(String targ: vas) {
            String[] va = targ.split("=");
            hashMap.put(va[0], va[1]);
        }
        System.out.println("---url=" + url + "---params=" + params);
        System.out.println("---javascirptCallMethod=" + javascirptCallMethod);
        ApiClient.getCommonGetRequest(url,hashMap).subscribe(new RxSubscriber<String>(){
            @Override
            protected void onEvent(String sa) {
                Log.d("HealthBespeakPresenterw", "----"+ sa);
                String s = "{\n" +
                "\"message\": \"\",\n" +
                "\"result\": {\n" +
                "\"content\": \"<p>十几分拉斯就拉风撒酒疯拉斯甲方领导手机开发房间领导手机发了多少家里的事讲课费简单来说放假了</p>\\n\\n<p>真正好大米，自己会说话！ " +
                        "黑龙江五常&mdash;&mdash;中国优质稻米之乡，稻米产地中的皇冠，适于耕种水稻的自然条件得天独厚。始于1835年种植的五常大米一度是晚清皇室独享的贡米。" +
                        "<img alt=\\\"\\\" src=\\\"https://upload.simuyun.com/banners/b1c00b26-5ee2-4ca3-b9ed-62ce27bbc198.jpeg\\\" />" +
                        "地处北纬45&deg;、东经127&deg;，黑龙江第一积温带，全年接受超过2700小时的日照。世界仅存三大黑土地之一，黑土层平均厚度 达2米，土壤中所含有机物高达10%，" +
                        "植被覆盖率高达40%。拉林河、阿什河两大水系河网密度，水温高于地下水，水中矿物质含量丰富，皆利于水稻生长。 温馨提示：大米物流为申通快递，一般2-5天左右到货，" +
                        "按距离计算到达。个别情况除外（例：遇恶劣天气或交通问题会影响到达时间） 如需要修改相关信息，请在下单后当日告知客服。 若为18:00后下单需修改，" +
                        "请在次日10点前联系客服修改。</p>\\n\\n<p><strong>温馨提示：</strong><strong> </strong>商城自收到订单的次工作日发货（最晚不超过2个工作日）" +
                        "</p>\\n\\n<p>例：周一提交订单，周二商城会处理发货。 周五提交订单，下周一统一处理发货。</p>\\n\\n<p>预计到货时间：2~3天（除个别特殊情况）</p>\\n\\n<p>如需要修改相关信息，" +
                        "请在下单后当日告知客服。 若为18:00后下单需修改，请在次日10点前联系客服修改。</p>\\n\"," +
                "\"id\": \"2e4cdcbae0f14351a14b644c9ba83732\",\n" +
                "\"title\": \"十几分拉斯就拉风撒酒疯拉斯甲方领导手机开发房间领导手机发了多少家里的事讲课费简单来说放假了闪亮的积分蓝思科技法律手段\",\n" +
                "\"releaseDate\": \"2017年09月20日 11:46:14\"\n" +
                "},\n" +
                "\"code\": \"\"\n" +
                "}";
                ThreadUtils.runOnMainThread(() -> {
                    String ss = s.replaceAll(" +", "");
                    ss = ss.replaceAll("\\\\n", "\\\n");
//                    ss = ss.replaceAll("\\\\\"", "\\\"");
                    ss = ss.replaceAll(":","@@@");
                    Log.e("ssssssssss",ss);
                    webView.loadUrl("javascript:" + javascirptCallMethod + "('" + ss + "')");
                });
            }

            @Override
            protected void onRxError(Throwable error) {
                System.out.println("---error message=" + error.getMessage());
            }
        });
    }

    private void requestPostMethod(String url, String params, String javascirptCallMethod) {
        JSONObject jsonObject;
        HashMap<String, String> hashMap = new HashMap<>();
        try {
            jsonObject = new JSONObject(params);
            Iterator<String> iterator = jsonObject.keys();
            while (iterator.hasNext()) {
                String key = iterator.next();
                hashMap.put(iterator.next(), jsonObject.getString(key));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ApiClient.getCommonPostRequest(url,hashMap).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String sa) {
                Log.d("requestPostMethod", "----"+ sa);
                ThreadUtils.runOnMainThread(() -> {
                    String ss = sa.replaceAll(" +", "");
                    ss = ss.replaceAll("\\\\n", "\\\n");
                    ss = ss.replaceAll(":","@@@");
                    Log.e("ssssssssss",ss);
                    webView.loadUrl("javascript:" + javascirptCallMethod + "('" + ss + "')");
                });
            }

            @Override
            protected void onRxError(Throwable error) {
                System.out.println("---requestPostMethod error message=" + error.getMessage());
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
                        webView.loadUrl("javaScript:" + callName + "('" + vas + "')");
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
