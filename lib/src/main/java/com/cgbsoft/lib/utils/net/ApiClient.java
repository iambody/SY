package com.cgbsoft.lib.utils.net;

import android.text.TextUtils;

import com.cgbsoft.lib.Appli;
import com.cgbsoft.lib.base.model.bean.AppResources;
import com.cgbsoft.lib.base.model.bean.DataStatistics;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.utils.rxjava.RxSchedulersHelper;
import com.cgbsoft.lib.utils.tools.Utils;

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
     * @return
     */
    public static Observable<AppResources> getAppResources() {
        Map<String, String> params = new HashMap<>();
        params.put("os", "1");
        params.put("version", Utils.getVersionName(Appli.getContext()));
        params.put("client", SPreference.isIdtentifyAdviser(Appli.getContext()) ? "1" : "2");
        return OKHTTP.getInstance().getRequestManager().getAppResource(checkNull(params)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.handleResult());
    }

    /**
     * 数据统计
     * @param json
     * @return
     */
    public static Observable<DataStatistics> pushDataStatistics(String json){
        Map<String, String> map = new HashMap<>();
        map.put("contents", json);
        return OKHTTP.getInstance().getRequestManager(NetConfig.SERVER_DS).pushDataStatistics(checkNull(map)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.handleResult());
    }

    /**
     * 获取ip
     * @return
     */
    public static Observable<String> getIP(){
        Map<String, String> map = new HashMap<>();
        map.put("ie", "utf-8");
        return OKHTTP.getInstance().getRequestManager(NetConfig.SERVER_IP).getIP(map).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.handleResult());
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
