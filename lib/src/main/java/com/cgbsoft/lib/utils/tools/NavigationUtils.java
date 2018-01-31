package com.cgbsoft.lib.utils.tools;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.cgbsoft.lib.InvestorAppli;
import com.cgbsoft.lib.base.mvp.model.NavigationBean;
import com.cgbsoft.lib.base.webview.BaseWebViewActivity;
import com.cgbsoft.lib.base.webview.CwebNetConfig;
import com.cgbsoft.lib.base.webview.WebViewConstant;
import com.cgbsoft.lib.contant.Contant;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.utils.constant.RxConstant;
import com.cgbsoft.lib.utils.rxjava.RxBus;
import com.cgbsoft.privatefund.bean.product.PublicFundInf;
import com.cgbsoft.privatefund.bean.product.PublishFundRecommendBean;
import com.chenenyu.router.IRouter;
import com.chenenyu.router.RouteCallback;
import com.chenenyu.router.Router;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    public static void startActivity(Context context, Class target, String propertyName, String propertyValue) {
        Intent intent = new Intent(context, target);
        intent.putExtra(propertyName, propertyValue);
        ((Activity) context).startActivity(intent);
    }

    public static void startActivity(Context context, Class target, HashMap<String, String> hashMap) {
        Intent intent = new Intent(context, target);
        Set<Map.Entry<String, String>> set = hashMap.entrySet();
        for (Map.Entry<String, String> entry : set) {
            intent.putExtra(entry.getKey(), entry.getValue());
        }
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

//    public static void startSystemImageForResult(Activity activity, int reqeustCode) {
//        ImageSelector selectSec = ImageSelector.create();
//        selectSec.single();  // 选择一张图片
//        selectSec.start(activity, reqeustCode);
//    }
//
//    public static void startSystemImageForResult(Activity activity, int reqeustCode, ArrayList<String> origins) {
//        ImageSelector selectSec = ImageSelector.create();
//        selectSec.multi();
//        selectSec.count(12);
//        selectSec.origin(origins);
//        selectSec.start(activity, reqeustCode);
//    }
//
//    public static ImageSelector startSystemImageMultiForResult(Activity activity, int reqeustCode) {
//        ImageSelector selectSec = ImageSelector.create();
//        selectSec.openCameraOnly(false);
//        selectSec.multi();
//        selectSec.count(10);
//        selectSec.start(activity, reqeustCode);
//        return selectSec;
//    }

    public static void toMainActivity(Context context) {
        Router.build(RouteConfig.GOTOCMAINHONE).go(context);
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
     * 跳转到统一的webactivity页面
     */
    public static void gotoWebActivity(Activity activity, String url, String title, boolean isShowRightShare) {
        Intent intent = new Intent(activity, BaseWebViewActivity.class);
        intent.putExtra(WebViewConstant.push_message_url, url);
        intent.putExtra(WebViewConstant.push_message_title, title);
        intent.putExtra(WebViewConstant.PAGE_SHOW_TITLE, isShowRightShare);
        activity.startActivity(intent);
    }

    /**
     * 跳转到公募基金
     */
    public static void goPublicFundCommon(Activity activity, int type) {
//        NavigationUtils.gotoWebActivity(activity, CwebNetConfig.publicFundRegistUrl, getResources().getString(R.string.public_fund_regist), false);
    }

    /**
     * 跳转到统一的webactivity页面
     */
    public static void gotoRightShareWebActivity(Activity activity, String url, String title) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(WebViewConstant.RIGHT_SHARE, true);
        hashMap.put(WebViewConstant.push_message_title, title);
        hashMap.put(WebViewConstant.push_message_url, url);

        NavigationUtils.startActivityByRouter(activity, RouteConfig.GOTO_RIGHT_SHARE_ACTIVITY, hashMap);
    }

    /**
     * 跳转到统一的webactivity页面
     */
    public static void gotoWebActivityWithPay(Activity activity, String url, String title) {
        Intent intent = new Intent(activity, BaseWebViewActivity.class);
        intent.putExtra(WebViewConstant.push_message_url, url);
        intent.putExtra(WebViewConstant.push_message_title, title);
        intent.putExtra(WebViewConstant.RIGHT_RECHARGE_HAS, false);
        activity.startActivity(intent);
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
        //进入自己的mvp类型的activity
        Router.build(RouteConfig.GOTOVIDEOPLAY).with("videoId", videoId).go(context);
    }

    /**
     * 跳转到video -->informaton资讯页
     *
     * @param
     * @param
     * @param
     */
    public static void startVideoInformationActivityu(Context context, String url, String title) {
        Router.build(RouteConfig.GOTO_VIDEO_INFORMATIOON)
                .with(WebViewConstant.push_message_url, url)
                .with(WebViewConstant.push_message_title, title)
                .with(WebViewConstant.PAGE_SHOW_TITLE, false)
                .with(WebViewConstant.RIGHT_SHARE, true)
                .go(context);
    }

    public static void startActivityByRouter(Context context, String routerType, Bundle bundle) {
        Router.build(routerType).with(bundle).go(context);
    }

    public static void startActivityByRouter(Context context, String routerType, String key, Object object) {
        Router.build(routerType).with(key, object).go(context);
    }

    public static void startActivityByRouter(Context context, String routerType, HashMap<String, Object> hashMap) {
        IRouter iRouter = Router.build(routerType);
        Set<Map.Entry<String, Object>> set = hashMap.entrySet();
        for (Map.Entry<String, Object> entry : set) {
            iRouter.with(entry.getKey(), entry.getValue());
        }
        iRouter.go(context);
    }

    public static void startActivityForResultByRouter(Context context, String routerType, HashMap<String, Object> hashMap, int requestCode) {
        IRouter iRouter = Router.build(routerType).requestCode(requestCode);
        Set<Map.Entry<String, Object>> set = hashMap.entrySet();
        for (Map.Entry<String, Object> entry : set) {
            iRouter.with(entry.getKey(), entry.getValue());
        }
        iRouter.go(context);
    }


    public static void startActivityByRouter(Context context, String routerType, HashMap<String, Object> hashMap, int enterAnim, int outerAnim) {
        IRouter iRouter = Router.build(routerType);
        Set<Map.Entry<String, Object>> set = hashMap.entrySet();
        for (Map.Entry<String, Object> entry : set) {
            iRouter.with(entry.getKey(), entry.getValue());
        }
        iRouter.anim(enterAnim, outerAnim).go(context);
    }

    public static void startActivityByRouterForResult(Context context, String routerType, int requestCode) {
        Router.build(routerType).requestCode(requestCode).go(context);
    }

    public static void startActivityByRouterForResult(Context context, String routerType, int requestCode, RouteCallback routeCallback) {
        Router.build(routerType).requestCode(requestCode).go(context, routeCallback);
    }

    public static void startActivityByRouter(Context context, String routerType, Bundle bundle, int flag) {
        Router.build(routerType).with(bundle).addFlags(flag).go(context);
    }

    public static void startActivityByRouter(Context context, String routerType, int flag) {
        Router.build(routerType).addFlags(flag).go(context);
    }

    public static void startActivityByRouter(Context context, String routerType, String key, Object object, int flag) {
        Router.build(routerType).with(key, object).addFlags(flag).go(context);
    }

    public static void startActivityByRouter(Context context, String routerType, String key, Object object, int enterAnim, int outerAnim) {
        Router.build(routerType).with(key, object).anim(enterAnim, outerAnim).go(context);
    }

    public static void startActivityByRouter(Context context, String routerType, HashMap<String, Object> hashMap, int flag) {
        IRouter iRouter = Router.build(routerType);
        Set<Map.Entry<String, Object>> set = hashMap.entrySet();
        for (Map.Entry<String, Object> entry : set) {
            iRouter.with(entry.getKey(), entry.getValue());
        }
        iRouter.addFlags(flag);
        iRouter.go(context);
    }

    public static void jumpNativePage(Context context, int code) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("code", code);
        switch (code) {
            case WebViewConstant.Navigation.MAIN_PAGE:
                NavigationUtils.startActivityByRouter(context, RouteConfig.GOTOCMAINHONE);
                break;
            case WebViewConstant.Navigation.MEMBER_PAGE:
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put(WebViewConstant.push_message_url, CwebNetConfig.memeberArea);
                hashMap.put(WebViewConstant.push_message_title, Contant.MEMBER_CENTER_TITLE);
                hashMap.put(WebViewConstant.RIGHT_SHARE, false);
//                NavigationUtils.gotoWebActivity((Activity) context,CwebNetConfig.memeberArea,  Contant.MEMBER_CENTER_TITLE, false);
                NavigationUtils.startActivityByRouter(InvestorAppli.getContext(), RouteConfig.GOTO_BASE_WEBVIEW, hashMap);
                break;
            case WebViewConstant.Navigation.RECHARGE_PAGE:
                NavigationUtils.startActivityByRouter(InvestorAppli.getContext(), RouteConfig.MALL_PAY);
                break;
            case WebViewConstant.Navigation.TASK_PAGE:
                NavigationUtils.startActivityByRouter(context, RouteConfig.INVTERSTOR_MAIN_TASK);
                TrackingDataManger.homeTask(context);
                break;
            case WebViewConstant.Navigation.YD_ENJOY_PAGE:
                jumpNativeMain(context, map);
                break;
            case WebViewConstant.Navigation.SHARE_FRIEND_PAGE:
                NavigationUtils.startActivityByRouter(context, RouteConfig.MALL_PAY);
                break;
            case WebViewConstant.Navigation.PRIVATE_BANK_PAGE:
                jumpNativeMain(context, map);
                break;
            case WebViewConstant.Navigation.PRODUCT_PAGE:
                jumpNativeMain(context, map);
                break;
            case WebViewConstant.Navigation.INFORMATION_PAGE:
                jumpNativeMain(context, map);
                break;
            case WebViewConstant.Navigation.VIDEO_PAGE:
                jumpNativeMain(context, map);
                break;
            case WebViewConstant.Navigation.LIFE_ENJOY_PAGE:
                jumpNativeMain(context, map);
                break;
            case WebViewConstant.Navigation.LIFT_HOME_PAGE:
                jumpNativeMain(context, map);
                break;
            case WebViewConstant.Navigation.LIFT_MALL_PAGE:
                jumpNativeMain(context, map);
                break;
            case WebViewConstant.Navigation.HEALTH_PAGE:
                jumpNativeMain(context, map);
                break;
            case WebViewConstant.Navigation.HEALTH_INTRODUCTION_PAGE:
                jumpNativeMain(context, map);
                break;
            case WebViewConstant.Navigation.HEALTH_CHECK_PAGE:
                jumpNativeMain(context, map);
                break;
            case WebViewConstant.Navigation.HEALTH_MEDICAL_PAGE:
                jumpNativeMain(context, map);
                break;
            case WebViewConstant.Navigation.HEALTH_PROJECT_SIMPLE:
                jumpNativeMain(context, map);
                break;
            case WebViewConstant.Navigation.HEALTH_COURES_PAGE:
                jumpNativeMain(context, map);
                break;
            case WebViewConstant.Navigation.MINE_PAGE:
                jumpNativeMain(context, map);
                break;
            case WebViewConstant.Navigation.MINE_SIGN_PAGE:
                jumpNativeMain(context, map);
                break;
            case WebViewConstant.Navigation.MINE_ACTION_PAGE:
                jumpNativeMain(context, map);
                break;
            case WebViewConstant.Navigation.MINE_CARD_PAGE:
                jumpNativeMain(context, map);
                break;
            case WebViewConstant.Navigation.MINE_GREETING_CARD_PAGE:
                jumpNativeMain(context, map);
                break;
        }
    }

    private static void jumpNativeMain(Context context, HashMap<String, Object> map) {
        if (context.toString().contains("MainPageActivity")) {
            RxBus.get().post(RxConstant.JUMP_INDEX, (int) map.get("code"));
        } else {
            NavigationUtils.startActivityByRouter(context, RouteConfig.GOTOCMAINHONE, map);
        }
    }

    public static ArrayList<NavigationBean> getNavigationBeans(Context context) {
        String navigation = SPreference.getString(context, "Navigation");
        if (navigation != null) {
            return new Gson().fromJson(navigation, new TypeToken<List<NavigationBean>>() {
            }.getType());
        }
        return null;

    }

    /**
     * 申购的跳转
     *
     * @param publishFundRecommendBeans
     * @param publicFundInf
     * @return
     */
    public static String getObjToBuy(PublishFundRecommendBean publishFundRecommendBeans, PublicFundInf publicFundInf) {
        String fundCode = publishFundRecommendBeans.getFundCode();//基金代码
        String fundName = publishFundRecommendBeans.getFundName();//基金名称
        String taNo = publishFundRecommendBeans.getTano();//TA代码
        String businesscode = publishFundRecommendBeans.getBusinesscode();//业务类型，认购或申购
        String buyflag = publishFundRecommendBeans.getBuyflag();//制购买标志，1为强制购买
        String depositacct = RegexUtil.getPublicFundBanks(publishFundRecommendBeans.getDepositacct()); //客户银行卡号
        String fundtype = publishFundRecommendBeans.getFundtype(); //基金类型，0-股票型基金 1-债券型基金 2-货币型基 金 3-混合型基金 4-专户基金 5-指数型基金 6-QDII 基金
        String mobiletelno = publicFundInf.getMobileno();//客户手机号
        String shareType = publishFundRecommendBeans.getSharetype(); //份额类别 'A'前收费 'B' 后收费
        String certificatetype = publishFundRecommendBeans.getCertificatetype();//证件类型
        String certificateno = publishFundRecommendBeans.getCertificateno(); //身份证号
        String depositacctname = publishFundRecommendBeans.getDepositacctname(); //客户姓名（
        //此处不用gson 如果某些字段是空的话 key值会直接隐藏不显示 会有问题
        JSONObject JsonObj = new JSONObject();
        try {
            JsonObj.put("fundcode", fundCode);//基金代码
            JsonObj.put("sharetype", shareType);
            JsonObj.put("tano", taNo);
            JsonObj.put("certificatetype", certificatetype);
            JsonObj.put("certificateno", certificateno);
            JsonObj.put("depositacctname", depositacctname);
            JsonObj.put("businesscode", businesscode);
            JsonObj.put("buyflag", buyflag);
            JsonObj.put("depositacct", depositacct);
            JsonObj.put("fundname", fundName);
            JsonObj.put("fundtype", fundtype);
            JsonObj.put("mobiletelno", mobiletelno);

            return JsonObj.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }


    }
}
