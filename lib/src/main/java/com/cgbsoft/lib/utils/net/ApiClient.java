package com.cgbsoft.lib.utils.net;

import android.text.TextUtils;

import com.cgbsoft.lib.Appli;
import com.cgbsoft.lib.base.model.bean.AppResources;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.utils.rxjava.RxSchedulersHelper;
import com.cgbsoft.lib.utils.tools.Util;

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
        params.put("version", Util.getVersionName(Appli.getContext()));
        params.put("client", SPreference.getIdtentify(Appli.getContext()) ? "1" : "2");
        return OKHTTP.getInstance().getRequestManager().getAppResource(checkNull(params)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.handleResult());
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
                map.put("appVersion", Util.getVersionName(Appli.getContext()));
            }
            if (!map.containsKey("appPlatform")) {
                map.put("appPlatform", "android");
            }

        } else {
            map = new HashMap<>();
            if (!map.containsKey("appVersion")) {
                map.put("appVersion", Util.getVersionName(Appli.getContext()));
            }
            if (!map.containsKey("appPlatform")) {
                map.put("appPlatform", "android");
            }
        }
        return map;
    }
}
