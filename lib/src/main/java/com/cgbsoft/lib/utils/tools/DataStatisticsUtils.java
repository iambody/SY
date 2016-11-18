package com.cgbsoft.lib.utils.tools;

import android.content.Context;
import android.os.Build;

import com.cgbsoft.lib.base.model.bean.DataStatistics;
import com.cgbsoft.lib.utils.cache.OtherDataProvider;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

import rx.Subscription;

/**
 * 数据统计
 * Created by xiaoyu.zhang on 2016/11/17 15:06
 * Email:zhangxyfs@126.com
 *  
 */
public class DataStatisticsUtils {
    public static Subscription subscription;

    public static void push(Context context, final HashMap<String, String> param) {
        final JSONArray jsonArray = new JSONArray();
        final JSONObject js = new JSONObject();
        try {
            js.put("uid", SPreference.getUserId(context.getApplicationContext()));
            js.put("ip", OtherDataProvider.getIP(context.getApplicationContext()));
            js.put("m", android.os.Build.MANUFACTURER + "--" + android.os.Build.MODEL);//设备品牌
            js.put("mos", "A");
            js.put("mv", android.os.Build.VERSION.RELEASE);//手机系统版本
            js.put("v", "smy");//应用系统(smy)
            js.put("vtp", Utils.getVersionCode(context.getApplicationContext()) + "");
            js.put("area", OtherDataProvider.getCity(context.getApplicationContext()));
            js.put("mid", getUniqueCode());//机器码
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Iterator iter = param.keySet().iterator();
        while (iter.hasNext()) {
            String key = (String) iter.next();
            String value = param.get(key);
            if (isToC(context)) {
                value = replaceActBtoC(value);
            }
            try {
                js.put(key, value);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        jsonArray.put(js);

        subscription = ApiClient.pushDataStatistics(jsonArray.toString()).subscribe(new RxSubscriber<DataStatistics>() {
            @Override
            protected void onEvent(DataStatistics dataStatistics) {
                subscription.unsubscribe();
            }

            @Override
            protected void onRxError(Throwable error) {
                subscription.unsubscribe();
            }
        });

    }


    private static String getUniqueCode() {
        return Build.BOARD.length() % 10 + "" +
                Build.BRAND.length() % 10 + "" +
                Build.CPU_ABI.length() % 10 + "" +
                Build.DEVICE.length() % 10 + "" +
                Build.DISPLAY.length() % 10 + "" +
                Build.HOST.length() % 10 + "" +
                Build.ID.length() % 10 + "" +
                Build.MANUFACTURER.length() % 10 + "" +
                Build.MODEL.length() % 10 + "" +
                Build.PRODUCT.length() % 10 + "" +
                Build.TAGS.length() % 10 + "" +
                Build.TYPE.length() % 10 + "" +
                Build.USER.length() % 10; //13 digits;
    }

    private static boolean isToC(Context context) {
        return !SPreference.isIdtentifyAdviser(context.getApplicationContext());
    }


    private static String replaceActBtoC(String param) {
        String value = param;
        switch (param) {
            case "10005":
                value = "20002";
                break;
            case "10007":
                value = "20003";
                break;
            case "10006":
                value = "20004";
                break;
            case "10008":
                value = "20005";
                break;
            case "10017":
                value = "20007";
                break;
            case "10016":
                value = "20009";
                break;
        }
        return value;
    }

}
