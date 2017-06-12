package com.cgbsoft.lib.utils.tools;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import com.cgbsoft.lib.AppInfStore;
import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.BaseApplication;
import com.cgbsoft.lib.base.model.bean.DataStatisticsBean;
import com.cgbsoft.lib.utils.cache.OtherDataProvider;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.utils.constant.Constant;
import com.cgbsoft.lib.utils.db.DaoUtils;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.privatefund.bean.location.LocationBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import rx.Subscription;

/**
 * 数据统计
 * Created by xiaoyu.zhang on 2016/11/17 15:06
 * Email:zhangxyfs@126.com
 *  
 */
public class DataStatisticsUtils {
    public static Subscription subscription;
    private static DaoUtils daoUtils;

    public static void push(Context context, final HashMap<String, String> param, boolean isRealTime) {
        final JSONArray jsonArray = new JSONArray();
        final JSONObject js = new JSONObject();
        LocationBean locationBean= AppManager.getLocation(context);

        try {
            if (locationBean!=null){
                js.put("lat",locationBean.getLocationlatitude());
                js.put("lon",locationBean.getLocationlontitude());
            }
            js.put("uid", AppManager.getUserId(context.getApplicationContext()));
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
        if (isRealTime) {

            jsonArray.put(js);

            subscription = ApiClient.pushDataStatistics(jsonArray.toString()).subscribe(new RxSubscriber<String>() {
                @Override
                protected void onEvent(String string) {
                    subscription.unsubscribe();
                }

                @Override
                protected void onRxError(Throwable error) {
                    subscription.unsubscribe();
                }
            });
        } else {
//            if (daoUtils == null) {
//                daoUtils = new DaoUtils(context, DaoUtils.W_DATASTISTICS);
//            }
//            //先查询已经存入的个数，如果已经存入4个直接拼上当前这个埋点，发送给服务器，清除数据
//            List<DataStatisticsBean> datastisticList = daoUtils.getDatastisticList();
//            if (datastisticList.size() == 4) {
//                jsonArray.put(js);
//                for (DataStatisticsBean dataStatisticsBean : datastisticList) {
//                    jsonArray.put(dataStatisticsBean.getJsonObject());
//                }
//
//                subscription = ApiClient.pushDataStatistics(jsonArray.toString()).subscribe(new RxSubscriber<String>() {
//                    @Override
//                    protected void onEvent(String string) {
//                        subscription.unsubscribe();
//                    }
//
//                    @Override
//                    protected void onRxError(Throwable error) {
//                        subscription.unsubscribe();
//                    }
//                });
//                daoUtils.deleteDataStatitic();
//            }else {
//                DataStatisticsBean dataStatisticsBean = new DataStatisticsBean(System.currentTimeMillis(), MessageFormat.format("{0}", System.currentTimeMillis()),js.toString());
//                daoUtils.saveDataStatistic(dataStatisticsBean);
//            }
        }
    }

    private static String getUniqueCode() {
        String m_szDevIDShort = Build.BOARD.length() % 10 + "" +
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

        TelephonyManager TelephonyMgr = (TelephonyManager) BaseApplication.getContext().getSystemService(Context.TELEPHONY_SERVICE);
        String m_szImei = TelephonyMgr.getDeviceId();

        String m_szAndroidID = Settings.Secure.getString(BaseApplication.getContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        WifiManager wm = (WifiManager) BaseApplication.getContext().getSystemService(Context.WIFI_SERVICE);
        String m_szWLANMAC = wm.getConnectionInfo().getMacAddress();

        BluetoothAdapter m_BluetoothAdapter = null; // Local Bluetooth adapter
        m_BluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        String m_szBTMAC = m_BluetoothAdapter.getAddress();

        String m_szLongID = m_szImei + m_szDevIDShort
                + m_szAndroidID + m_szWLANMAC + m_szBTMAC;
        // compute md5
        MessageDigest m = null;
        try {
            m = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        m.update(m_szLongID.getBytes(), 0, m_szLongID.length());
        // get md5 bytes
        byte p_md5Data[] = m.digest();
        // create a hex string
        String m_szUniqueID = new String();
        for (int i = 0; i < p_md5Data.length; i++) {
            int b = (0xFF & p_md5Data[i]);
            // if it is a single digit, make sure it have 0 in front (proper padding)
            if (b <= 0xF)
                m_szUniqueID += "0";
            // add number to string
            m_szUniqueID += Integer.toHexString(b);
        }   // hex string to uppercase
        return m_szUniqueID.toUpperCase();
    }

    private static boolean isToC(Context context) {
        return AppManager.isAdViser(context);
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
