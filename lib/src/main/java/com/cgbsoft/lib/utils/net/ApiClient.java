package com.cgbsoft.lib.utils.net;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.cgbsoft.lib.Appli;
import com.cgbsoft.lib.base.model.bean.AppResources;
import com.cgbsoft.lib.base.model.bean.DataStatistics;
import com.cgbsoft.lib.base.model.bean.LoginBean;
import com.cgbsoft.lib.base.model.bean.UserInfo;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.utils.rxjava.RxSchedulersHelper;
import com.cgbsoft.lib.utils.tools.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import rx.Observable;

/**
 *  * Created by xiaoyu.zhang on 2016/11/10 17:54
 *  
 */
public class ApiClient {

    /**
     * 获取服务器资源
     *
     * @return
     */
    public static Observable<AppResources> getAppResources() {
        Map<String, String> params = new HashMap<>();
        params.put("os", "1");
        params.put("version", Utils.getVersionName(Appli.getContext()));
        params.put("client", SPreference.getIdtentify(Appli.getContext()) + "");

        return OKHTTP.getInstance().getRequestManager().getAppResource(checkGETParam(params)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.handleResult());
    }

    /**
     * 数据统计
     *
     * @param json
     * @return
     */
    public static Observable<DataStatistics> pushDataStatistics(String json) {
        Map<String, String> map = new HashMap<>();
        map.put("contents", json);
        return OKHTTP.getInstance().getRequestManager(NetConfig.SERVER_DS).pushDataStatistics(checkNull(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.handleResult());
    }

    /**
     * 获取ip
     *
     * @return
     */
    public static Observable<String> getIP() {
        Map<String, String> map = new HashMap<>();
        map.put("ie", "utf-8");
        return OKHTTP.getInstance().getRequestManager(NetConfig.SERVER_IP).getIP(map).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.handleResult());
    }

    /**
     * 登录
     *
     * @param username 用户名
     * @param pwdMD5   md5密码
     * @return
     */
    public static Observable<LoginBean> toLogin(String username, String pwdMD5) {
        Map<String, String> map = new HashMap<>();
        map.put("userName", username);
        map.put("password", pwdMD5);
        return OKHTTP.getInstance().getRequestManager().toLogin(map).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.handleResult());
    }

    /**
     * 获取用户信息
     *
     * @param userid 用户id
     * @return
     */
    public static Observable<UserInfo> getUserInfo(String userid) {
        Map<String, String> map = new HashMap<>();
        map.put("adviserId", userid);
        return OKHTTP.getInstance().getRequestManager(true).getUserInfo(map).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.handleResult());
    }

    /**
     * 重新生成Get 方式的value值
     *
     * @param map
     * @return
     */
    private static Map<String, String> checkGETParam(@NonNull Map<String, String> map) {
        String paramValue = "";
        JSONObject jsonObject = null;
        Set<String> set = map.keySet();
        try {
            jsonObject = new JSONObject();
            for (String key : set) {
                String value = map.get(key);
                jsonObject.put(key, value);
            }
            paramValue = URLEncoder.encode(jsonObject.toString(), "utf-8");
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Map<String, String> params = new HashMap<>();
        if (!TextUtils.isEmpty(paramValue))
            params.put("param", paramValue);
        return params;
    }


    private static Map<String, String> checkNull(Map<String, String> map) {
        if (map != null) {
            Set<String> set = map.keySet();
            LinkedList<String> list = new LinkedList<>();
            for (String key : set) {
                String value = map.get(key);
                if (TextUtils.isEmpty(value)) {
                    list.add(key);
                }
            }
            for (String key : list) {
                map.remove(key);
            }
            if (!map.containsKey("appVersion")) {
                map.put("appVersion", Utils.getVersionName(Appli.getContext()));
            }
            if (!map.containsKey("appPlatform")) {
                map.put("appPlatform", "android");
            }

        } else {
            map = new HashMap<>();
            if (!map.containsKey("appVersion")) {
                map.put("appVersion", Utils.getVersionName(Appli.getContext()));
            }
            if (!map.containsKey("appPlatform")) {
                map.put("appPlatform", "android");
            }
        }
        return map;
    }
}
