package com.cgbsoft.lib.utils.tools;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.cgbsoft.lib.base.webview.BaseWebViewActivity;
import com.cgbsoft.lib.base.webview.CwebNetConfig;
import com.cgbsoft.lib.base.webview.WebViewConstant;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.mvp.ui.video.VideoDetailActivity;
import com.cgbsoft.lib.utils.constant.Constant;
import com.chenenyu.router.IRouter;
import com.chenenyu.router.Router;
import com.jhworks.library.ImageSelector;

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

    public static void startSystemImageForResult(Activity activity, int reqeustCode) {
        ImageSelector selectSec = ImageSelector.create();
        selectSec.single();  // 选择一张图片
        selectSec.start(activity, reqeustCode);
    }

    public static void toMainActivity(Context context) {
        Router.build("investornmain_mainpageactivity").go(context);
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

    /**
     * 跳转到产品详情的操作
     *
     * @param context
     * @param schemeId
     * @param productName
     * @param requestCode
     */
    public static void startProductDetailActivity(Context context, String schemeId, String productName, int requestCode) {
        String url = CwebNetConfig.productDetail.concat(schemeId);
        Intent intent = new Intent(context, BaseWebViewActivity.class);
        intent.putExtra(WebViewConstant.push_message_url, url);
        intent.putExtra(WebViewConstant.push_message_title, productName);
        intent.putExtra(WebViewConstant.PAGE_SHOW_TITLE, true);
        ((Activity) context).startActivityForResult(intent, requestCode);
    }

    /**
     * 跳转到视频详情页面
     *
     * @param
     * @param
     * @param
     */
    public static void startVidoDetailActivity(Context context, String videoId, String videoCoverUrl, int comeFrom) {
//        Intent intent = new Intent(context, VideoDetailActivity.class);
//        intent.putExtra("videoId", videoId);
//        intent.putExtra("videoCoverUrl", videoCoverUrl);
//        intent.putExtra("comeFrom", comeFrom);
//        ((Activity) context).startActivity(intent);
//进入自己的mvp类型的activity
        Router.build(RouteConfig.GOTOVIDEOPLAY).with("videoId", videoId).go(context);
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
}
