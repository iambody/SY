//package com.cgbsoft.lib.utils.tools;
//
//import android.content.Context;
//import android.os.Build;
//import android.webkit.CookieManager;
//import android.webkit.CookieSyncManager;
//
//import com.cgbsoft.lib.AppManager;
//
///**
// * @author chenlong
// */
//public class CookieManage {
//
//    public static boolean syncCookie(Context context, String url) {
////        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
////            CookieSyncManager.createInstance(context);
////        }
////        CookieManager cookieManager = CookieManager.getInstance();
//////        document.cookie = 'uid=%@';document.cookie = 'version=5.7.3';document.cookie = 'token=%@';document.cookie = 'role=1';document.cookie = 'client=C';
////        cookieManager.setCookie(url, "uid=".concat(AppManager.getUserId(context)));
////        cookieManager.setCookie(url, "version=5.7.3");
////        cookieManager.setCookie(url, "token=".concat(AppManager.getUserToken(context)));
////        cookieManager.setCookie(url, "role=1");
////        cookieManager.setCookie(url, "client=C");
////        String newCookie = cookieManager.getCookie(url);
////        System.out.println("--------newCookit=".concat(newCookie));
////        return TextUtils.isEmpty(newCookie);
//        CookieSyncManager.createInstance(context);
//        CookieManager cookieManager = CookieManager.getInstance();
//        cookieManager.setAcceptCookie(true);
//        cookieManager.setCookie(url, "uid=".concat(AppManager.getUserId(context)));
//        cookieManager.setCookie(url, "version=5.7.3");
//        cookieManager.setCookie(url, "token=".concat(AppManager.getUserToken(context)));
//        cookieManager.setCookie(url, "role=1");
//        cookieManager.setCookie(url, "client=C");
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
//            CookieManager.getInstance().flush();
//        } else {
//            CookieSyncManager.getInstance().sync();
//        }
//        String newCookie = cookieManager.getCookie(url);
//        System.out.println("--------newCookit=".concat(newCookie));
//        return true;
//    }
//}
