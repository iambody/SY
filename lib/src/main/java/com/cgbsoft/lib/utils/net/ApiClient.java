package com.cgbsoft.lib.utils.net;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.cgbsoft.lib.Appli;
import com.cgbsoft.lib.base.model.AppResourcesEntity;
import com.cgbsoft.lib.base.model.RongTokenEntity;
import com.cgbsoft.lib.base.model.UserInfoDataEntity;
import com.cgbsoft.lib.base.model.WXUnionIDCheckEntity;
import com.cgbsoft.lib.base.model.bean.UserInfo;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.utils.rxjava.RxSchedulersHelper;
import com.cgbsoft.lib.utils.tools.Utils;

import org.json.JSONException;
import org.json.JSONObject;

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
    public static Observable<AppResourcesEntity.Result> getAppResources() {
        Map<String, String> params = new HashMap<>();
        params.put("os", "1");
        params.put("version", Utils.getVersionName(Appli.getContext()));
        params.put("client", SPreference.getIdtentify(Appli.getContext()) + "");

        return OKHTTP.getInstance().getRequestManager().getAppResource(createProgram(params)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.handleResult());
    }

    /**
     * 数据统计
     *
     * @param json
     * @return
     */
    public static Observable<String> pushDataStatistics(String json) {
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
        return OKHTTP.getInstance().getRequestManager(NetConfig.SERVER_IP, false).getIP(map).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.filterResultToString());
    }

    /**
     * 登录
     *
     * @param username 用户名
     * @param pwdMD5   md5密码
     * @return
     */
    public static Observable<UserInfoDataEntity.Result> toLogin(String username, String pwdMD5) {
        Map<String, String> map = new HashMap<>();
        map.put("userName", username);
        map.put("password", pwdMD5);
        return OKHTTP.getInstance().getRequestManager().toLogin(createProgram(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.handleResult());
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
        return OKHTTP.getInstance().getRequestManager(true).getUserInfo(createProgram(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.handleResult());
    }

    /**
     * 获取容云token
     *
     * @param rongExpired
     * @param rongUID
     * @return
     */
    public static Observable<RongTokenEntity.Result> getRongToken(String rongExpired, String rongUID) {
        Map<String, String> map = new HashMap<>();
        if (rongExpired != null)
            map.put("tokenExpired", rongExpired);
        map.put("uid", rongUID);
        return OKHTTP.getInstance().getRequestManager().getRongToken(createProgram(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.handleResult());
    }


    /**
     * 微信 unioid 验证
     *
     * @param unionid
     * @return
     */
    public static Observable<WXUnionIDCheckEntity.Result> wxUnioIDCheck(String unionid) {
        Map<String, String> map = new HashMap<>();
        map.put("unionid", unionid);
        return OKHTTP.getInstance().getRequestManager().wxUnioIDCheck(createProgram(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.handleResult());
    }

    /**
     * 微信登录
     *
     * @param sex
     * @param nickName
     * @param unionid
     * @param headimgurl
     * @return
     */
    public static Observable<UserInfoDataEntity.Result> toWxLogin(String sex, String nickName, String unionid, String headimgurl) {
        Map<String, String> map = new HashMap<>();
        map.put("sex", sex);
        map.put("nickName", nickName);
        map.put("unionid", unionid);
        map.put("headImageUrl", headimgurl);
        return OKHTTP.getInstance().getRequestManager().toWxLogin(createProgram(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.handleResult());
    }

    /**
     * 获取协议
     *
     * @return
     */
    public static Observable<String> getProtocol() {
        return OKHTTP.getInstance().getRequestManager(NetConfig.SERVER_WWW, false).getProtocol().compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.filterResultToString());
    }

    /**
     * 注册
     * @param userName
     * @param pwdMd5
     * @param code
     * @return
     */
    public static Observable<UserInfoDataEntity.Result> toRegister(String userName, String pwdMd5, String code){
        Map<String, String> map = new HashMap<>();
        map.put("userName", userName);
        map.put("password", pwdMd5);
        map.put("captcha", code);
        return OKHTTP.getInstance().getRequestManager().toRegister(createProgram(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.handleResult());
    }

    /**
     * 发送验证码
     * @param phone
     * @return
     */
    public static Observable<String> sendCode(String phone){
        Map<String, String> map = new HashMap<>();
        map.put("phone", phone);
        map.put("checkPhoneDuplicate", "1");
        return OKHTTP.getInstance().getRequestManager().sendCode(createProgram(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.handleResult());
    }


    //.compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.handleResult());

    /**
     * 重新生成Get 方式的value值
     *
     * @param map
     * @return
     */
    private static Map<String, String> createProgram(@NonNull Map<String, String> map) {
        String paramValue = getParamJSON(map);
        Map<String, String> params = new HashMap<>();
        if (!TextUtils.isEmpty(paramValue))
            params.put("param", paramValue);
        return params;
    }

    /**
     * 生成json
     *
     * @param map
     * @return
     */
    private static String getParamJSON(@NonNull Map<String, String> map) {
        String paramValue = "";
        JSONObject jsonObject = null;
        Set<String> set = map.keySet();
        try {
            jsonObject = new JSONObject();
            for (String key : set) {
                String value = map.get(key);
                jsonObject.put(key, value);
            }
            paramValue = jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return paramValue;
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
