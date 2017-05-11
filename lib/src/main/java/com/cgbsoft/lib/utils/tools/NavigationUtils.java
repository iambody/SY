package com.cgbsoft.lib.utils.tools;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.chenenyu.router.IRouter;
import com.chenenyu.router.Router;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author chenlong on 16/9/7.
 */
public class NavigationUtils {

    public static void startActivity(Context context, Class target) {
        Intent intent = new Intent(context, target);
        ((Activity) context).startActivity(intent);
    }


    public static void startActivity(Context context, Class target, String id) {
        Intent intent = new Intent(context, target);
        intent.putExtra("PARAMS_ID", id);
        ((Activity) context).startActivity(intent);
    }

    public static void startActivityForResult(Context context, Class target, int requestCode) {
        Intent intent = new Intent(context, target);
        ((Activity) context).startActivityForResult(intent, requestCode);
    }

    public static void startSystemCalendarActivity(Context context) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.android.calendar", "com.android.calendar.LaunchActivity"));
        context.startActivity(intent);
    }

    /**
     * 调用系统报打电话页面
     *
     * @param context
     */
    public static void startDialgTelephone(Context context, String number) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + number));
        context.startActivity(intent);
    }

    public static void startDialogSendMessage(Context context, String telephoneNum) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("smsto:" + telephoneNum));
        context.startActivity(intent);
    }

    public static void sendBroadcastToAlrm(Context context, File file) {
        Uri uri = Uri.fromFile(file);
        sendBroadcastToAlrm(context, uri);
    }

    public static void sendBroadcastToAlrm(Context context, Uri uri) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(uri);
        context.sendBroadcast(intent);
    }

    public static void startActivityByRouter(Context context, String routerType) {
        Router.build(routerType).go(context);
    }

    public static void startActivityByRouter(Context context, String routerType, Bundle bundle) {
        Router.build(routerType).with(bundle).go(context);
    }

    public static void startActivityByRouter(Context context, String routerType, String key, Object object) {
        Router.build(routerType).with(key, object).go(context);
    }

    public static void startActivityByRouter(Context context, String routerType, HashMap<String, String> hashMap) {
        IRouter iRouter = Router.build(routerType);
        Set<Map.Entry<String, String>> set = hashMap.entrySet();
        for (Map.Entry<String, String> entry : set) {
            iRouter.with(entry.getKey(), entry.getValue());
        }
        iRouter.go(context);
    }

    public static void startActivityByRouterForResult(Context context, String routerType, int requestCode) {
        Router.build(routerType).requestCode(requestCode).go(context);
    }

    public static void startActivityByRouter(Context context, String routerType, Bundle bundle, int flag) {
        Router.build(routerType).with(bundle).addFlags(flag).go(context);
    }

    public static void startActivityByRouter(Context context, String routerType, String key, Object object, int flag) {
        Router.build(routerType).with(key, object).addFlags(flag).go(context);
    }

    //进入视频播放页面
    public static void toPlayVideoActivityByRouter(Context context, String videoId) {
        Router.build("video_playtactivity").with("videoId", videoId).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).go(context);
    }
}
