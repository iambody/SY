package com.cgbsoft.lib.utils.tools;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.File;

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
}
