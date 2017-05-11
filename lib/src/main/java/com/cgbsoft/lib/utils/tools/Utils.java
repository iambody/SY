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
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.utils.net.NetConfig;
import com.cgbsoft.lib.widget.DefaultDialog;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            dbName = "privateFundDB";
        }
        if (TextUtils.isEmpty(dbName)) {
            dbName = "privateFundDB";
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

    public static void telHotline(final Context context){
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
}
