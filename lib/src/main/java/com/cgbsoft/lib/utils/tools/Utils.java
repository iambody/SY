package com.cgbsoft.lib.utils.tools;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.ViewConfiguration;
import android.view.WindowManager;

import com.cgbsoft.lib.BaseApplication;
import com.cgbsoft.lib.R;
import com.cgbsoft.lib.base.webview.BaseWebNetConfig;
import com.cgbsoft.lib.base.webview.CwebNetConfig;
import com.cgbsoft.lib.base.webview.WebViewConstant;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.utils.net.NetConfig;
import com.cgbsoft.lib.widget.dialog.DefaultDialog;
import com.tencent.msdk.dns.MSDKDnsResolver;
import com.tencent.qcload.playersdk.util.VideoInfo;

import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by user on 2016/11/4.
 */

public class Utils {
    /**
     * 转换dip为px
     *
     * @param context context
     * @param dip     dip
     * @return int
     */
    public static int convertDipOrPx(@NonNull Context context, float dip) {
        float scale = context.getApplicationContext().getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f * (dip >= 0 ? 1 : -1));
    }


    /**
     * 转换px为dip
     *
     * @param context context
     * @param px      px
     * @return float
     */
    public static float convertPxOrDip(@NonNull Context context, int px) {
        float scale = context.getApplicationContext().getResources().getDisplayMetrics().density;
        return px / scale + 0.5f * (px >= 0 ? 1 : -1);
    }

    /**
     * 获取屏幕的宽度
     *
     * @param context context
     * @return int
     */
    public static int getScreenWidth(@NonNull final Context context) {
        return context.getApplicationContext().getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取屏幕高度
     *
     * @param ctx context
     * @return int
     */
    public static int getScreenHeight(@NonNull Context ctx) {
        return ctx.getApplicationContext().getResources().getDisplayMetrics().heightPixels;
    }


    public static int getRealScreenWidth(Activity activity) {
        int widthPixels;
        WindowManager w = activity.getWindowManager();
        Display d = w.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        d.getMetrics(metrics);
        widthPixels = metrics.widthPixels;
        if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17)
            try {
                widthPixels = (Integer) Display.class.getMethod("getRawWidth").invoke(d);
            } catch (Exception ignored) {
            }
        else if (Build.VERSION.SDK_INT >= 17)
            try {
                android.graphics.Point realSize = new android.graphics.Point();
                Display.class.getMethod("getRealSize", android.graphics.Point.class).invoke(d, realSize);
                widthPixels = realSize.x;
            } catch (Exception ignored) {
            }
        return widthPixels;
    }

    /**
     * 获取真正的屏幕高度
     *
     * @param activity
     * @return
     */
    public static int getRealScreenHeight(Activity activity) {
        int heightPixels;
        WindowManager w = activity.getWindowManager();
        Display d = w.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        d.getMetrics(metrics);
        // since SDK_INT = 1;
        heightPixels = metrics.heightPixels;
        // includes window decorations (statusbar bar/navigation bar)
        if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17)
            try {
                heightPixels = (Integer) Display.class
                        .getMethod("getRawHeight").invoke(d);
            } catch (Exception ignored) {
            }
            // includes window decorations (statusbar bar/navigation bar)
        else if (Build.VERSION.SDK_INT >= 17)
            try {
                android.graphics.Point realSize = new android.graphics.Point();
                Display.class.getMethod("getRealSize",
                        android.graphics.Point.class).invoke(d, realSize);
                heightPixels = realSize.y;
            } catch (Exception ignored) {
            }
        return heightPixels;
    }

//    public static boolean isAppRunningOnTop(Context context, String name) {
//        ActivityManager activityManager = (ActivityManager)context.getSystemService("activity");
//        List runningTaskInfo = activityManager.getRunningTasks(1);
//        if(runningTaskInfo != null && runningTaskInfo.size() != 0) {
//            String topAppPackageName = ((ActivityManager.RunningTaskInfo)runningTaskInfo.get(0)).topActivity.getPackageName();
//            return !TextUtils.isEmpty(name) && name.equals(topAppPackageName);
//        } else {
//            return false;
//        }
//    }

    /**
     * 获取状态栏高度
     *
     * @param ctx activity
     * @return int
     */
    public static int getTop(Activity ctx) {
        Rect rect = new Rect();
        ctx.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);

        if (rect.top == 0) {
            try {
                Class c = Class.forName("com.android.internal.R$dimen");
                Object obj = c.newInstance();
                Field field = c.getField("status_bar_height");
                int x = Integer.parseInt(field.get(obj).toString());
                return ctx.getResources().getDimensionPixelSize(x);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        return rect.top;
    }

    /**
     * 获取NavigationBar的高度
     *
     * @param context
     * @return
     */
    public static int getNavigationBarHeight(Context context) {
        if (!checkDeviceHasNavigationBar(context.getApplicationContext())) {
            return 0;
        }
        Resources resources = context.getApplicationContext().getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }

    /**
     * 通过判断设备是否有返回键、菜单键(不是虚拟键,是手机屏幕外的按键)来确定是否有navigation bar
     *
     * @param context
     * @return
     */
    public static boolean checkDeviceHasNavigationBar(Context context) {
        boolean hasMenuKey = ViewConfiguration.get(context).hasPermanentMenuKey();
        boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
        return !hasMenuKey && !hasBackKey;
    }

    /**
     * 检查是否连接网络
     *
     * @param context
     * @return
     */
    public static boolean checkNetWork(Context context) {
        ConnectivityManager cwjManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        cwjManager.getActiveNetworkInfo();
        NetworkInfo networkInfo = cwjManager.getActiveNetworkInfo();
        return networkInfo != null;
    }

    /**
     * @param context if null, use the default format
     *                (Mozilla/5.0 (Linux; U; Android %s) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 %sSafari/534.30).
     * @return
     */
    public static String getUserAgent(Context context) {
        String webUserAgent = null;
        if (context != null) {
            try {
                Class sysResCls = Class.forName("com.android.internal.R$string");
                Field webUserAgentField = sysResCls.getDeclaredField("web_user_agent");
                Integer resId = (Integer) webUserAgentField.get(null);
                webUserAgent = context.getString(resId);
            } catch (Throwable ignored) {
            }
        }
        if (TextUtils.isEmpty(webUserAgent)) {
            webUserAgent = "Mozilla/5.0 (Linux; U; Android %s) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 %sSafari/533.1";
        }

        Locale locale = Locale.getDefault();
        StringBuffer buffer = new StringBuffer();
        // Add version
        final String version = Build.VERSION.RELEASE;
        if (version.length() > 0) {
            buffer.append(version);
        } else {
            // default to "1.0"
            buffer.append("1.0");
        }
        buffer.append("; ");
        final String language = locale.getLanguage();
        if (language != null) {
            buffer.append(language.toLowerCase());
            final String country = locale.getCountry();
            if (country != null) {
                buffer.append("-");
                buffer.append(country.toLowerCase());
            }
        } else {
            // default to "en"
            buffer.append("en");
        }
        // add the model for the release build
        if ("REL".equals(Build.VERSION.CODENAME)) {
            final String model = Build.MODEL;
            if (model.length() > 0) {
                buffer.append("; ");
                buffer.append(model);
            }
        }
        final String id = Build.ID;
        if (id.length() > 0) {
            buffer.append(" Build/");
            buffer.append(id);
        }
        return String.format(webUserAgent, buffer, "Mobile ");
    }

    /**
     * 需要權限 READ_PHONE_STATE READ_PHONE_STATE
     *
     * @param context
     * @return
     */
    public static String getIMEI(Context context) {
        TelephonyManager mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        @SuppressLint("HardwareIds") String imei = mTelephonyManager.getDeviceId();
        if (TextUtils.isEmpty(imei)) {
            imei = String.valueOf(System.currentTimeMillis());
        }
        return imei;
    }

    public static String URLDecoder(String converData) {
        String json = null;
        try {
            json = URLDecoder.decode(converData, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static String URLEncoder(String converData) {
        String json = null;
        try {
            json = URLEncoder.encode(converData, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return json;
    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public static String getVersionName(Context context) {
        String versonName;
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            versonName = info.metaData.getString("versionName");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            versonName = "1";
        }
        return versonName;
    }

    /**
     * 获取app版本号
     *
     * @return 当前应用的版本号
     */
    public static int getVersionCode(Context context) {
        int versonName;
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            versonName = info.metaData.getInt("versionCode");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            versonName = 1;
        }
        return versonName;
    }
//    /**
//     * [获取应用程序版本名称信息]
//     *
//     * @param context
//     * @return 当前应用的版本名称
//     */
//    public static String getVersionName(Context context)
//    {
//        try
//        {
//            PackageManager packageManager = context.getPackageManager();
//            PackageInfo packageInfo = packageManager.getPackageInfo(
//                    context.getPackageName(), 0);
//            return packageInfo.versionName;
//
//        } catch (PackageManager.NameNotFoundException e)
//        {
//            e.printStackTrace();
//        }
//        return null;
//    }

    /**
     * 得到屏幕分辨率数组
     *
     * @param context
     * @return
     */
    public static int[] getScreenDispaly(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = windowManager.getDefaultDisplay().getWidth();
        int height = windowManager.getDefaultDisplay().getHeight();
        int result[] = {width, height};
        return result;
    }

    public static void OpenSharePage(Context context, String routeConfig, String data, boolean rightSave, boolean initPage, boolean rightShare) {
        try {
            String baseParams = URLDecoder.decode(data, "utf-8");
            String[] split = baseParams.split(":");
            String url = split[2];
            String title = split[3];
            if (!url.contains("http")) {
                url = BaseWebNetConfig.baseParentUrl + url;
            } else {
                title = split[4];
                url = split[2] + ":" + split[3];
            }
            HashMap hashMap = new HashMap();
            hashMap.put(WebViewConstant.push_message_url, url);
            hashMap.put(WebViewConstant.push_message_title, title);
            if (initPage) {
                String pushMessage = split[4];
                hashMap.put(WebViewConstant.push_message_value, pushMessage);
            }
            hashMap.put(WebViewConstant.RIGHT_SAVE, rightSave);
            hashMap.put(WebViewConstant.RIGHT_SHARE, rightShare);
            hashMap.put(WebViewConstant.PAGE_INIT, initPage);
            if (split.length >= 5) {
                hashMap.put(WebViewConstant.PAGE_SHOW_TITLE, Boolean.valueOf(split[split.length - 1]));
            }
            NavigationUtils.startActivityByRouter(context, routeConfig, hashMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void OpenSharePage(Context context, String routeConfig, String title, String url, boolean rightSave, boolean initPage, boolean rightShare) {
        try {
            HashMap hashMap = new HashMap();
            hashMap.put(WebViewConstant.push_message_url, url);
            hashMap.put(WebViewConstant.push_message_title, title);
            hashMap.put(WebViewConstant.RIGHT_SAVE, rightSave);
            hashMap.put(WebViewConstant.RIGHT_SHARE, rightShare);
            hashMap.put(WebViewConstant.PAGE_INIT, initPage);
            NavigationUtils.startActivityByRouter(context, routeConfig, hashMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取数据库名字
     *
     * @param context
     * @return
     */
    public static String getDatabaseName(Context context) {
        String dbName;
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            dbName = info.metaData.getString("dbName");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            dbName = "sxyFundDB";
        }
        if (TextUtils.isEmpty(dbName)) {
            dbName = "sxyFundDB";
        }

        return dbName;
    }

    /**
     * 获取屏幕宽高
     *
     * @param c
     * @return
     */
    public static int[] getWidthHeight(Context c) {
        WindowManager wm = ((Activity) c).getWindowManager();

        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();

        int[] s = new int[2];
        s[0] = width;
        s[1] = height;
        return s;
    }

    /**
     * 数据库版本号
     *
     * @param context
     * @return
     */
    public static int getDBVersionCode(Context context) {
        int versonCode;
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            versonCode = info.metaData.getInt("dbVersion");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            versonCode = 1;
        }
        return versonCode;
    }

    /**
     * 是否为数字
     *
     * @param str
     * @return
     */
    public static boolean isNumber(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }

    public static boolean isDebug() {
        if (BaseApplication.getContext() != null) {
            boolean isCouldOpen = SPreference.getOpenJsonLog(BaseApplication.getContext());
            if (isCouldOpen) {
                return true;
            } else if (NetConfig.isLocal) {
                return true;
            }
        }
        return false;
    }

    public static void log(String tag, String msg) {
        if (TextUtils.isEmpty(msg)) {
            msg = "";
        }
        if (BaseApplication.getContext() != null) {
            boolean isCouldOpen = SPreference.getOpenJsonLog(BaseApplication.getContext());
            if (isCouldOpen) {
                Log.e(tag, msg);
            } else if (NetConfig.isLocal) {
                Log.e(tag, msg);
            }
        }
    }

    public static void log(String tag, String msg, String which) {
        if (TextUtils.isEmpty(msg)) {
            msg = "";
        }
        if (BaseApplication.getContext() != null) {
            boolean isCouldOpen = SPreference.getOpenJsonLog(BaseApplication.getContext());
            if (isCouldOpen || NetConfig.isLocal) {
                if (TextUtils.equals(which, "e")) {
                    Log.e(tag, msg);
                } else if (TextUtils.equals(which, "d")) {
                    Log.d(tag, msg);
                }
            }
        }
    }

    /**
     * 过滤掉特色字符
     *
     * @return
     */
    public static String replaceSpeialStr(String resouceStr) {
        String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(resouceStr);
        return m.replaceAll("").trim();
    }

    /**
     * 是否微信安装了
     *
     * @param context
     * @return
     */
    public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isServiceRunning(Context context, String serviceName) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(Integer.MAX_VALUE);
        if (serviceList == null || serviceList.size() == 0) return false;
        for (int i = 0; i < serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().equals(serviceName)) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }

    public static void telHotline(final Context context) {
        new DefaultDialog(context, "拨打电话：" + context.getString(R.string.hotline), "取消", "确定") {
            public void left() {
                this.cancel();
            }

            public void right() {
                Intent phoneIntent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + context.getString(R.string.hotline)));
                context.startActivity(phoneIntent);
                this.cancel();
            }
        }.show();
    }
    /**
     * 大陆手机号码11位数，匹配格式：前三位固定格式+后8位任意数
     * 此方法中前三位格式有：
     * 13+任意数
     * 15+除4的任意数
     * 18+除1和4的任意数
     * 17+除9的任意数
     * 147
     */
    /**
     * 判断手机号
     */
    public static boolean isMobileNO(String mobiles) {// ^1[358]\\d{9}$
        Pattern p = Pattern.compile("^1[0-9]{10}$");

        Matcher m = p.matcher(mobiles);
        System.out.println(m.matches() + "---");
        return m.matches();
    }

    /**
     * 香港手机号码8位数，5|6|8|9开头+7位任意数
     */
    public static boolean isHKPhoneLegal(String str) throws PatternSyntaxException {
        String regExp = "^(5|6|8|9)\\d{7}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 调用系统界面，给指定的号码发送短信
     *
     * @param context
     * @param number
     */
    public static void sendSmsWithNumber(Context context, String number) {
        Intent sendIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + number));
        context.startActivity(sendIntent);
    }

    /**
     * 判断当前应用是否是debug状态
     */
    public static boolean isApkInDebug(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 追加http前缀
     *
     * @param url
     * @return
     */
    public static String appendWebViewUrl(String url) {
        if (!TextUtils.isEmpty(url)) {
            if (!url.startsWith("http")) {
                url = url.startsWith("/") ? CwebNetConfig.baseParentUrl.concat(url.substring(1)) : CwebNetConfig.baseParentUrl.concat(url);
            }
        }
        return url;
    }

    /**
     * 校验18位身份证号
     *
     * @param identityCode 返回true则表示校验通过
     */
    public static boolean checkIdentityCode(String identityCode) {
        // 校验身份证位数为18位
        if (!identityCode.matches("\\d{17}(\\d|x|X)$")) {
            return false;
        }
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        if (Integer.parseInt(identityCode.substring(6, 10)) < 1900
                || Integer.parseInt(identityCode.substring(6, 10)) > year) {// 7-10位是出生年份，范围应该在1900-当前年份之间
            return false;
        }
        if (Integer.parseInt(identityCode.substring(10, 12)) < 1
                || Integer.parseInt(identityCode.substring(10, 12)) > 12) {// 11-12位代表出生月份，范围应该在01-12之间
            return false;
        }
        if (Integer.parseInt(identityCode.substring(12, 14)) < 1
                || Integer.parseInt(identityCode.substring(12, 14)) > 31) {// 13-14位是出生日期，范围应该在01-31之间
            return false;
        }
        // 校验第18位
        // S = Sum(Ai * Wi), i = 0, ... , 16 ，先对前17位数字的权求和
        // Ai:表示第i位置上的身份证号码数字值
        // Wi:表示第i位置上的加权因子
        // Wi: 7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4 2
        String[] tempA = identityCode.split("|");
        int[] a = new int[18];
        for (int i = 0; i < tempA.length - 2; i++) {
            a[i] = Integer.parseInt(tempA[i + 1]);
        }
        int[] w = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2}; // 加权因子
        int sum = 0;
        for (int i = 0; i < 17; i++) {
            sum = sum + a[i] * w[i];
        }
        // Y = mod(S, 11)
        // 通过模得到对应的校验码
        // Y: 0 1 2 3 4 5 6 7 8 9 10
        // 校验码: 1 0 X 9 8 7 6 5 4 3 2
        String[] v = {"1", "0", "x", "9", "8", "7", "6", "5", "4", "3", "2"}; // 校验码
        int y = sum % 11;
        if (!v[y].equalsIgnoreCase(identityCode.substring(17))) {// 第18位校验码错误
            return false;
        }
        return true;
    }

    public static boolean isAppRunningOnTop(Context context, String name) {
        ActivityManager activityManager = (ActivityManager)context.getSystemService("activity");
        List runningTaskInfo = activityManager.getRunningTasks(1);
        if(runningTaskInfo != null && runningTaskInfo.size() != 0) {
            String topAppPackageName = ((ActivityManager.RunningTaskInfo)runningTaskInfo.get(0)).topActivity.getPackageName();
            return !TextUtils.isEmpty(name) && name.equals(topAppPackageName);
        } else {
            return false;
        }
    }
//
//    /**
//     * 通过域名查找Ip
//     * @param domainUrl
//     * @return
//     */
//    public static String getIpByDomain(String domainUrl) {
//       return MSDKDnsResolver.getInstance().getAddrByName(domainUrl);
//    }

//    public static String replaceDomainByIp(String url){
//        System.out.println("------url= " + url);
//        if (!TextUtils.isEmpty(url) && StringUtils.startsWithIgnoreCase(url, "http")) {
//            URI uri = null;
//           try {
//                uri = new URI(url);
//                String host = uri.getHost();
//                System.out.println("------returnVal= " + host);
//                String ip = getIpByDomain(host);
//                System.out.println("------ip= " + ip);
//               if (!TextUtils.isEmpty(ip)) {
//                   return uri.toString().replace(host, ip);
//               }
//            } catch (URISyntaxException e) {
//                e.printStackTrace();
//            }
//        }
//        return url;
//    }
}
