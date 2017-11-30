package com.cgbsoft.lib.utils.tools;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.BaseApplication;
import com.cgbsoft.lib.base.model.CommonEntity;
import com.cgbsoft.lib.base.model.bean.TrackingDataBean;
import com.cgbsoft.lib.utils.cache.OtherDataProvider;
import com.cgbsoft.lib.utils.db.DaoUtils;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.privatefund.bean.location.LocationBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;

import rx.Subscription;

/**
 * desc  ${DESC}
 * author yangzonghui  yangzonghui@simuyun.com
 * 日期 2017/11/29-下午12:33
 */

public class TrackingDataUtils {
    public static Subscription subscription;
    private static DaoUtils daoUtils;

    /**
     *
     */
    public static void save(Context context, String event, String param) {
        if(true)return;
        Context applicationContext = context.getApplicationContext();
        if (daoUtils == null) {
            daoUtils = new DaoUtils(applicationContext, DaoUtils.W_TRACKINGDATA);
        }
        //先查询已经存入的个数，如果已经存入20个直接拼上当前这个埋点，发送给服务器，清除数据
        int size = daoUtils.getTrackingDataListSize();
        if (size < 20) {
            daoUtils.saveTrackingData(new TrackingDataBean(event, System.currentTimeMillis(), param));
        } else {
            List<TrackingDataBean> trackingDataBeens = daoUtils.getTrackingData();
            trackingDataBeens.add(new TrackingDataBean(event, System.currentTimeMillis(), param));
            post(applicationContext, trackingDataBeens);
            daoUtils.deleteTrackData();
        }
    }

    private static void post(Context context, List<TrackingDataBean> trackingDataBeens) {
        final JSONArray jsonArray = new JSONArray();
        final JSONObject js = new JSONObject();
        LocationBean locationBean = AppManager.getLocation(context);

        try {
            if (locationBean != null) {
                js.put("lat", locationBean.getLocationlatitude());
                js.put("lng", locationBean.getLocationlontitude());
            }
//            js.put("clicktime", TimeUtils.format(System.currentTimeMillis()));
            js.put("uid", AppManager.getUserId(context.getApplicationContext()));
            js.put("ip", OtherDataProvider.getIP(context.getApplicationContext()));
            js.put("m", android.os.Build.MANUFACTURER + "--" + android.os.Build.MODEL);//设备品牌
            js.put("mos", "A");
            js.put("mv", android.os.Build.VERSION.RELEASE);//手机系统版本
            if (AppManager.isInvestor(context)) {
                js.put("v", "sxy");//应用系统(smy)
            } else {
                js.put("v", "smy");
            }
            js.put("vtp", Utils.getVersionCode(context.getApplicationContext()) + "");
            js.put("area", OtherDataProvider.getCity(context.getApplicationContext()));
//            js.put("mid", getUniqueCode());//机器码
            js.put("mid", DeviceUtils.getPhoneId(context));


        } catch (JSONException e) {
            e.printStackTrace();
        }


        for (int i = 0; i < trackingDataBeens.size(); i++) {
            JSONObject track = new JSONObject();
            try {
                track.put("e",trackingDataBeens.get(i).getE());
                track.put("t",trackingDataBeens.get(i).getT());
                track.put("d",trackingDataBeens.get(i).getD());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArray.put(track);
        }

        try {
            js.put("data",jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        subscription = ApiClient.pushTrackingData(js).subscribe(new RxSubscriber<CommonEntity.Result>() {
            @Override
            protected void onEvent(CommonEntity.Result result) {
                subscription.unsubscribe();
            }

            @Override
            protected void onRxError(Throwable error) {
                subscription.unsubscribe();
            }
        });
    }


    public static HashMap<String, String> getParams(String grp, String act, String[] args) {
        HashMap<String, String> map = new HashMap<>();
        map.put("grp", grp);
        map.put("act", act);
        for (int i = 1; i <= args.length; i++) {
            map.put("arg" + i, args[i - 1]);
        }
        return map;
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

}
