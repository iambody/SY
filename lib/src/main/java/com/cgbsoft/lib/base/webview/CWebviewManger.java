package com.cgbsoft.lib.base.webview;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.ClipboardManager;
import android.text.TextUtils;
import android.widget.Toast;

import com.cgbsoft.lib.AppInfStore;
import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.BaseApplication;
import com.cgbsoft.lib.InvestorAppli;
import com.cgbsoft.lib.R;
import com.cgbsoft.lib.TaskInfo;
import com.cgbsoft.lib.base.model.AppResourcesEntity;
import com.cgbsoft.lib.base.model.CommonEntity;
import com.cgbsoft.lib.base.model.UserInfoDataEntity;
import com.cgbsoft.lib.base.model.bean.CalendarListBean;
import com.cgbsoft.lib.base.model.bean.ConversationBean;
import com.cgbsoft.lib.base.model.bean.OtherInfo;
import com.cgbsoft.lib.base.mvp.ui.QrMidActivity;
import com.cgbsoft.lib.contant.Contant;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.share.bean.ShareCommonBean;
import com.cgbsoft.lib.share.dialog.CommonShareDialog;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.utils.constant.Constant;
import com.cgbsoft.lib.utils.constant.RxConstant;
import com.cgbsoft.lib.utils.db.DBConstant;
import com.cgbsoft.lib.utils.db.DaoUtils;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxBus;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.utils.tools.BStrUtils;
import com.cgbsoft.lib.utils.tools.CacheDataManager;
import com.cgbsoft.lib.utils.tools.CalendarManamger;
import com.cgbsoft.lib.utils.tools.DataStatistApiParam;
import com.cgbsoft.lib.utils.tools.DataStatisticsUtils;
import com.cgbsoft.lib.utils.tools.JumpNativeUtil;
import com.cgbsoft.lib.utils.tools.LogOutAccount;
import com.cgbsoft.lib.utils.tools.LogUtils;
import com.cgbsoft.lib.utils.tools.MD5Utils;
import com.cgbsoft.lib.utils.tools.NavigationUtils;
import com.cgbsoft.lib.utils.tools.PromptManager;
import com.cgbsoft.lib.utils.tools.Utils;
import com.cgbsoft.lib.utils.ui.DialogUtils;
import com.cgbsoft.lib.widget.MToast;
import com.cgbsoft.lib.widget.dialog.DefaultDialog;
import com.cgbsoft.lib.widget.dialog.DownloadDialog;
import com.cgbsoft.privatefund.bean.share.NewsBean;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Set;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/5/4-17:58
 */
public class CWebviewManger {
    private Activity context;
    private BaseWebview webview;
    private boolean headRefreshing; // 头部是否有刷新
    private boolean loadMoreing; // 加载更大

    private String[] PERMISSIONS = new String[]{Manifest.permission.CAMERA};
    private int REQUEST_CODE = 2000; // 请求码

    public CWebviewManger(Activity activity) {
        super();
        this.context = activity;
    }

    public boolean isHeadRefreshing() {
        return headRefreshing;
    }

    public void setHeadRefreshing(boolean headRefreshing) {
        this.headRefreshing = headRefreshing;
    }

    public void setWeb(BaseWebview webView) {
        this.webview = webView;
    }

    /**
     * 接收网页指令，分发处理
     * 伪协议的分类名称列表
     * <p/>
     * openpage	打开页面
     * closepage	关闭页面
     * buynow	认购
     * filing	报备
     * filingdata	报备(带订单数据)
     * changepassword	修改密码
     * viewpdf	查看 PDF
     * copytoclipboard 拷贝
     * jumpProduct 跳转到产品
     *
     * @param action
     */
    public void setAction(String action) {

        LogUtils.Log("webview", action);
        if (action.contains("visiterGotoLogin")) {
            gotoVisiterLogin();
        } else if (action.contains("toastError")) {
            showToast(action);

        } else if (action.contains("jumtoActivitiesSalon")) {//沙龙活动
//            UiSkipUtils.toNextActivity(context,new Intent(context,));
            NavigationUtils.startActivityByRouter(context, RouteConfig.GOTO_SALONS_ACTIVITY);
        } else if (action.contains("filingdata")) { // TOB
//            toBaobeiWithdata(action);
        } else if (action.contains("filing")) { // TOB
//            toBaobei();
        } else if (action.contains("openMytask")) {//C端新加需求 需要首页有我的任务 跳转到我的任务
            goMyTask();
        } else if (action.contains("openpage")) {
            openpage(action, false, false, false);
        } else if (action.contains("openSingleTaskPage")) {
//            openSingleTaskPage(action, false, false, true);
        } else if (action.contains("closepage") || action.contains("closePage")) {
            closepage(action);
        } else if (action.contains("secretviewpdf")) { // 预留，可能后端直接写好
            gotoScretPdf(action);
        } else if (action.contains("changepassword")) {
            changepassword(action);
        } else if (action.contains("copytoclipboard")) {
            copytoclipboard(action);
        } else if (action.contains("clickPasteServeCode")) {
            getContentFromPasteBoard(action);
        } else if (action.contains("playVideo")) {
            playVideo(action);
        } else if (action.contains("jumpProductList")) {
            jumpProductList(action);
//        } else if (action.contains("jumpProduct")) {
//            jumpProduct(action);
        } else if (action.contains("custbyorder")) { // TOB
//            jumpOrderList(action);
//        } else if (action.contains("commitRedeem")) {
//            commitRedeem(action);
        } else if (action.contains("withdrawAlert")) { // TOB
//            drawAlert(action);
        } else if (action.contains("secrecyPage")) {
            //TODO
        } else if (action.contains("submitQuestionnaire")) { // TOB
//            toRiskRusult(action);
        } else if (action.contains("openMallPage")) {
            openMallPage(action);
        } else if (action.contains("openDialog")) {
            openDialog(action);
        } else if (action.contains("resetIdentfy")) {
            DialogUtils.createSwitchBcDialog(context).show();
        } else if (action.contains("addAddress")) {
            NavigationUtils.startActivityByRouter(context, "mall_address");
        } else if (action.contains("choiceAddress")) {
            NavigationUtils.startActivityByRouter(context, "mall_choice_address");
        } else if (action.contains("telephone")) {

            //判断是否有拨打电话权限
            if (needPermissions(Constant.PERMISSION_CALL_PHONE)) {
                PromptManager.ShowCustomToast(context, "请到设置允许拨打电话权限");

                return;
            }

            Utils.telHotline(context);
        } else if (action.contains("openMallMain")) {
        } else if (action.contains("submitQuestionnaire")) {
        } else if (action.contains("iftakeup")) { // todo 认购
//            checkRengou(action);
        } else if (action.contains("saveSuccess")) { // CHEN
            Intent intent = new Intent();
            intent.putExtra(BaseWebViewActivity.SAVE_PARAM, getValue(action));
            context.setResult(Activity.RESULT_OK, intent);
            context.finish();
        } else if (action.contains("openSavePage")) {
            openpage(action, true, false, false);
        } else if (action.contains("toastError")) {
            showNewToast(action);
        } else if (action.contains("openUrl")) {
            openUrl(action);
        } else if (action.contains("openParam")) {
            openpage(action, false, true, false);
        } else if (action.contains("setUpHeadImage")) {// 上传头像
            startImagePage(action);
        } else if (action.contains("scanning")) {
            toQrCode();
        } else if (action.contains("toastStatus") || action.equals("toastSuccess")) {
            showToast(action);
        } else if (action.contains("showAlert")) {
            showComfirmDialog(action);
        } else if (action.contains("openMessage")) {
            toMessageList();
        } else if (action.contains("cloudAnimate")) {
            //TODO 云动画
        } else if (action.contains("practiseAbility")) {
        } else if (action.contains("relationAssets")) {//关联资产动作
            NavigationUtils.startActivityByRouterForResult(context, "investornmain_relativeassetctivity", BaseWebViewActivity.RELATIVE_ASSERT);
        } else if (action.contains("pssetsProve")) {
            NavigationUtils.startActivityByRouterForResult(context, "investornmain_proveassetctivity", BaseWebViewActivity.ASSERT_PROVE);
        } else if (action.contains("selectedInvestment")) {
//            EventBus.getDefault().post(new Qrcode());
        } else if (action.contains("recommendFriend")) {
            recommentFriend();
        } else if (action.contains("h5-native")) {
//            VersonUpdate();
        } else if (action.contains("logOut")) {
            DefaultDialog dialog = new DefaultDialog(context, "确认要退出账号吗？", "取消", "确认") {
                @Override
                public void left() {
                    dismiss();
                }

                @Override
                public void right() {
                    dismiss();
                    logOutUser();
                }
            };
            dialog.show();
        } else if (action.contains("backPage")) {
            backPage(action);
        } else if (action.contains("toast") || action.contains("toastInfo")) {
            showToast(action);
        } else if (action.contains("mobClick")) {
            mobClick(action);
        } else if (action.contains("presentPage")) {
            startActivityOverridePendingTransition(action);
        } else if (action.contains("dismissPage")) {
            overActivityOverridePendingTransition();
        } else if (action.contains("inviteCust")) {
            shareDataFriend(action);
        } else if (action.contains("clearLocal")) {
            CacheDataManager.cleanApplicationData(context, new String[]{context.getCacheDir().getAbsolutePath(), context.getExternalCacheDir().getAbsolutePath()});
            new MToast(context).show("应用缓存清除成功", 0);
        } else if (action.contains("aboutWX")) {
//            guanlianWeixin();
        } else if (action.contains("openQuestionnaire")) {
            openpage(action, false, false, false);
        } else if (action.contains("tokenError")) {
            new MToast(context).show("token信息错误", 0);
        } else if (action.contains("tocShare")) {
            shareToC(action);
        } else if (action.contains("riskTest")) {
            String[] split = action.split(":");
            UserInfoDataEntity.UserInfo userInfo = AppManager.getUserInfo(context);
            userInfo.getToC().setCustomerType(split[2]);
            AppInfStore.saveUserInfo(context, userInfo);
            NavigationUtils.startActivityByRouter(context, "investornmain_riskresultactivity", "level", split[2]);

            context.finish();
        } else if (action.contains("tel:")) {
            NavigationUtils.startDialgTelephone(context, "4001888848");
        } else if (action.contains("openSharePage")) {
            opensharepage(action, false, false, true);
        } else if (action.contains("checkVersion")) {
            DaoUtils daoUtils = new DaoUtils(context, DaoUtils.W_OTHER);
            OtherInfo otherInfo = daoUtils.getOtherInfo(DBConstant.APP_UPDATE_INFO);
            String values = SPreference.getBoolean(context, Contant.VISITE_LOOK_NAVIGATION) ? "1" : "0";
            if (otherInfo != null) {
                String json = otherInfo.getContent();
                AppResourcesEntity.Result result = new Gson().fromJson(json, AppResourcesEntity.Result.class);
                String newVersion = result.version;
                String oldVersion = Utils.getVersionName(context);
                String language = "javascript:newVersion('" + oldVersion + "','" + (TextUtils.isEmpty(newVersion) ? "0" : newVersion) + "'," + values + ")";
                webview.loadUrl(language);
                LogUtils.Log("aaa", "language===" + language);
            } else {
                String oldVersion = String.valueOf(Utils.getVersionName(context));
                String language = "javascript:newVersion('" + oldVersion + "',0," + values + ")";
                webview.loadUrl(language);
                LogUtils.Log("aaa", "language=---==" + language);
            }
        } else if (action.contains("updated")) {
            versonUpdate();
        } else if (action.contains("feedback")) {//意见反馈跳转
            NavigationUtils.startActivityByRouter(context, "investornmain_feedbackctivity");
        } else if (action.contains("signEnt")) {
//            SignIn();
        } else if (action.contains("setGestruePassword")) { //设置手势密码
            NavigationUtils.startActivityByRouter(context, RouteConfig.SET_GESTURE_PASSWORD);
            String valuse = "1".equals(AppManager.getUserInfo(context).getToC().getGestureSwitch()) ? "2" : "1";
            DataStatistApiParam.onSwitchGesturePassword(valuse);
        } else if (action.contains("modifyGestruePassword")) { // 修改手势密码
            NavigationUtils.startActivityByRouter(context, RouteConfig.VALIDATE_GESTURE_PASSWORD, "PARAM_FROM_MODIFY", true);
            DataStatistApiParam.onModifyGesturePassword();
            context.finish();
        } else if (action.contains("closeGestruePassword")) { // 关闭手势密码
            NavigationUtils.startActivityByRouter(context, RouteConfig.VALIDATE_GESTURE_PASSWORD, "PARAM_CLOSE_PASSWORD", true);
        } else if (action.contains("openInformation")) {
            gotoDiscoverDetail(action);
        } else if (action.contains("shareAchievement")) {
            cutScreenShare(action);
        } else if (action.contains("openCustomerChat")) {
            openCustomerChat(action);
        } else if (action.contains("LivePrompt")) { // 直播提醒
            livePrompt(action);
//            //startVideoDetail(action);
        } else if (action.contains("toVideoLive")) { // 视频直播
//            startVideoLive(action);
        } else if (action.contains("onlineVisite")) { // 我的投顾上线通知
            isTouGuOnline(action);
        } else if (action.contains("callLiCaiShi")) { // 呼叫理财师
            callLiCaiShi(action);
        } else if (action.contains("imLicaishi")) { // 回话理财师
            chatToTouGu(action);
        } else if (action.contains("sendPhoneMsg")) {  // 发送短信
            sendMessageToTouGu(action);
        } else if (action.contains("messageDialog")) { // 和投顾对话
            chatToTouGu(action);
        } else if (action.contains("hasAddCarlender")) { // 是否已经添加日历
            hasAddCarlender(action);
//        } else if (action.contains("openPrePlayVideoDialog")) { // 显示直播预告弹窗
//            showPreVideoDialog(action);
        } else if (action.contains("clipScreenShare")) {
//            clipScreenShare();
        } else if (action.contains("toAppRefreshMessage")) {
            refreshEnd(action);
        } else if (action.contains("toAppGetMoreMessage")) {
            loadMore(action);
        } else if (action.contains("searchProduct")) {
            startSearchBaseActivity();
        } else if (action.contains("showContactImDialog")) {
            showContactImDialog(action);
        } else if (action.contains("sendInfoData")) {
            initZixunData(action);
        } else if (action.contains("appIsguided")) {
            //getWeb().loadUrl("javascript.appIsguided('" + values + "')");
        } else if (action.contains("shareAdviserCard")) {
//            PromptManager.ShowCustomToast(context,"我需要分享名片");
            shareAdviserCardShow(action);
        } else if (action.contains("QRCallBack")) {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("isCallBack", "Y");
            NavigationUtils.startActivityByRouter(context, RouteConfig.GOTO_TWO_CODE_ACTIVITY, hashMap);
//            Intent intent = new Intent(context, CaptureActivity.class);
//            intent.putExtra("isCallBack", "Y");
//            context.startActivity(intent);
        } else if (action.contains("sharePoster")) {
//            sharePoster();
        } else if (action.contains("ydPay")) {
            NavigationUtils.startActivityByRouter(context, RouteConfig.MALL_PAY);
        } else if (action.contains("rootPage")) {
            //通知刷新用户xinxi数据
            HashMap hashMap = new HashMap();
            hashMap.put("gotoMainPage", true);
            NavigationUtils.startActivityByRouter(context, RouteConfig.GOTOCMAINHONE, hashMap);
        } else if (action.contains("jumpNativeCode")) {
            jumpNativeCode(action);
        } else if (action.contains("callPhone")) {
            callPhone(action);
        } else if (action.contains("showShareItem")) {
            switchShareButton();
        } else if (action.contains("showPayItem")) {
            showPayItem(action);
        } else if (action.contains("viewpdf")) {
            gotoScretPdf(action);
        } else if (action.contains("goLoginBackHome")) {
            //直接跳转到
            HashMap<String, Object> map = new HashMap<>();
            map.put("insidegotologin", true);
            map.put("backgohome", true);
            NavigationUtils.startActivityByRouter(context, RouteConfig.GOTO_LOGIN, map);
        } else if (action.contains("bindAdviserSucc")) {
            //绑定成功
            RxBus.get().post(RxConstant.BindAdviser, 1);
        } else if (action.contains("gotoMineActivity")) { // 跳转到我的活动
            NavigationUtils.startActivityByRouter(context, RouteConfig.GOTO_MINE_ACTIVITY);
            context.finish();
        } else if (action.contains("modificationWebTitle")) {
            String name = "";
            try {
                String urlcodeAction = URLDecoder.decode(action, "utf-8");
                String[] split = urlcodeAction.split(":");
                name = split[2];
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            if (context instanceof BaseWebViewActivity) {
                BaseWebViewActivity baseWebViewActivity = (BaseWebViewActivity) context;
                if (!TextUtils.isEmpty(name)) {
                    baseWebViewActivity.modifyTitleName(name);
                }
            } else {
                if (!TextUtils.isEmpty(name)) {
                    RxBus.get().post(RxConstant.WEBVIEW_MODIFY_TITLE, name);
                }
            }
        } else if (action.contains("showTitleRightStr")) {
            showTitleRightStr(action);
        } else if (action.contains("shareFromScreenshot")) {
            sharePoster(action);
        } else if (action.contains("penLargeImage")) {
            //分享截屏的图片
            gotoLargeImage(action);
        }
    }

    private void gotoLargeImage(String url) {
        try {
            String[] urlcodeAction = url.split(":");
            String urlCoder = URLDecoder.decode(urlcodeAction[2], "utf-8");
            if (!TextUtils.isEmpty(urlCoder)) {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put(Constant.IMAGE_SAVE_PATH_LOCAL, urlCoder);
                NavigationUtils.startActivityByRouter(context, RouteConfig.SMOOT_IMAGE_ACTIVITY, hashMap);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 分享海报
     *
     * @param action
     */
    private void sharePoster(String action) {
//        String path = ScreenShot.GetandSaveCurrentImage(context);
//        CommonScreenDialog commonScreenDialog=new CommonScreenDialog(context, path, new CommonScreenDialog.CommentScreenListener() {
//            @Override
//            public void completShare() {
//
//            }
//
//            @Override
//            public void cancleShare() {
//
//            }
//        });
//        commonScreenDialog.show();

//        CommonSharePosterDialog commonSharePosterDialog = new CommonSharePosterDialog(context, CommonSharePosterDialog.Tag_Style_WxPyq, path, new CommonSharePosterDialog.CommentShareListener() {
//            @Override
//            public void completShare(int shareType) {
//
//            }
//
//            @Override
//            public void cancleShare() {
//
//            }
//        });
//        commonSharePosterDialog.show();
    }

    private void showPayItem(String action) {
        if (context instanceof BaseWebViewActivity) {
            ((BaseWebViewActivity) context).showPayItem();
        }
    }

    private void showTitleRightStr(String action) {
        String urlcodeAction = null;
        try {
            urlcodeAction = URLDecoder.decode(action, "utf-8");
            String[] split = urlcodeAction.split(":");
            String codeStr = split[2];
            if (context instanceof BaseWebViewActivity) {
                ((BaseWebViewActivity) context).showTitleRight(codeStr);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    private void switchShareButton() {
        if (context instanceof BaseWebViewActivity) {
            ((BaseWebViewActivity) context).showShareButton();
        }
    }

    private void jumpNativeCode(String action) {
        try {
            String urlcodeAction = URLDecoder.decode(action, "utf-8");
            String[] split = urlcodeAction.split(":");
            String codeStr = split[2];
            NavigationUtils.jumpNativePage(context, Integer.decode(codeStr));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 跳转到本地
     *
     * @param action
     */
    private void gotoNative(String action) {
        String urcodeAction = null;
        try {
            urcodeAction = URLDecoder.decode(action, "utf-8");
            String[] split = urcodeAction.split(":");
            JumpNativeUtil.SkipNative(context, split[2]);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * h5提示需要跳转到登录界面
     */
    private void gotoVisiterLogin() {
//        RouteConfig.GOTO_LOGIN
        HashMap<String, Object> map = new HashMap<>();
        map.put("insidegotologin", true);
        NavigationUtils.startActivityByRouter(context, RouteConfig.GOTO_LOGIN, map);
    }

    private void opensharepage(String data, boolean rightSave, boolean initPage, boolean rightShare) {
        Utils.OpenSharePage(context, RouteConfig.GOTO_RIGHT_SHARE_ACTIVITY, data, rightSave, initPage, rightShare);
    }

    /**
     * 跳转到secretpdf
     *
     * @param action
     */
    private void gotoScretPdf(String action) {
        try {
            String urcodeAction = URLDecoder.decode(action, "utf-8");
            String[] split = urcodeAction.split(":");
            String string = split[2];

            HashMap<String, Object> map = new HashMap<>();
            map.put("pdfurl", split[2] + ":" + split[3]);
            map.put("pdftitle", split[4]);
            NavigationUtils.startActivityByRouter(context, RouteConfig.GOTO_PDF_ACTIVITY, map);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void versonUpdate() {
        new DownloadDialog(context, true, true);
//        if (MApplication.getAppUpdate() != null && !TextUtils.isEmpty(MApplication.getAppUpdate().getVersion())
//                && !MApplication.getAppUpdate().getVersion().equals(new AppInfo().versionName(context))) {
//            new AppUpdateDialog(context).show();
//        }
    }

    /**
     * 理财师名片分享
     *
     * @param action
     */
    private void shareAdviserCardShow(String action) {
//        BShare bShare = GetShareData(action);
//        if (null == bShare) return;
//        CommonShareDialog commonShareDialog = new CommonShareDialog(context, bShare, CommonShareDialog.Tag_Style_Circle_WeiXin, new CommonShareDialog.CommentShareListener() {
//            @Override
//            public void onclick() {
//                //预留事件处理器  &考虑以后需求改变
//            }
//        });
//        commonShareDialog.show();
    }

//    private void openSingleTaskPage(String data, boolean rightSave, boolean initPage, boolean rightShare) {
//        String[] split = data.split(":");
//        String string = split[2];
//        String title = split[3];
//        String decodeTitle = "";
//        String decodeUrl = "";
//        if (!string.contains("http")) {
//            string = CwebNetConfig.baseParentUrl + string;
//        }
//        Intent i = new Intent(context, SinglePushMsgActivity.class);
//        // i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        try {
//            decodeTitle = URLDecoder.decode(title, "utf-8");
//            decodeUrl = URLDecoder.decode(string, "utf-8");
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            i.putExtra(Contant.push_message_url, URLDecoder.decode(string, "utf-8"));
//            i.putExtra(Contant.push_message_title, URLDecoder.decode(title, "utf-8"));
//            if (initPage) {
//                i.putExtra(Contant.push_message_value, URLDecoder.decode(split[4], "utf-8"));
//            }
//            i.putExtra(Contant.RIGHT_SAVE, rightSave);
//            i.putExtra(Contant.RIGHT_SHARE, rightShare);
//            i.putExtra(Contant.PAGE_INIT, initPage);
//            if (split.length >= 5) {
//                i.putExtra(Contant.PAGE_SHOW_TITLE, Boolean.valueOf(split[split.length - 1]));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        ((Activity) context).startActivityForResult(i, 300);
//        //ScreenManager.getScreenManager().pushActivity(context);
//        if ("产品详情".equals(decodeTitle) && Utils.isVisteRole(context)) {
//            new RundouTaskManager(context).executeRundouTask("查看产品");
//        } else if (!TextUtils.isEmpty(decodeUrl) && decodeUrl.contains("discover/details.html")) {
//            new RundouTaskManager(context).executeRundouTask("查看资讯");
//        }
//    }

//    private void sharePoster() {
//        MApplication.shareWXType = Contant.SHARE_POSTER;
//        MyBitmapUtils myBitmapUtils = new MyBitmapUtils();
//        String name = "";
//        if (!TextUtils.isEmpty(MApplication.getUser().getRealName())) {
//            name = "我是" + MApplication.getUser().getRealName();
//        } else if (!TextUtils.isEmpty(MApplication.getUser().getNickName())) {
//            name = "我是" + MApplication.getUser().getNickName();
//        }
//        Bitmap bitmap = myBitmapUtils.getActiveBitmap(context, "https://app.simuyun.com/app5.0/carnival/index.html?userId=" + MApplication.getUserid(), name);
//        WeiXinShare share = new WeiXinShare(context, "");
//        share.shareWeixinquanWithPic(bitmap);
//        System.out.println("分享");
//    }

    /**
     * 获取理财师名片分享bean数据
     *
     * @return
     */
    private void livePrompt(String action) {
        String[] split = action.split(":");
        try {
            String title = URLDecoder.decode(split[2], "utf-8");
            String address = URLDecoder.decode(split[3], "utf-8");
            String startTime = URLDecoder.decode(split[4], "utf-8");
            String content = URLDecoder.decode(split[5], "utf-8");
            CalendarListBean.CalendarBean calendarListBean = new CalendarListBean.CalendarBean();
            calendarListBean.setTitle(title);
            calendarListBean.setAddress(address);
            calendarListBean.setStartTime(startTime);
            calendarListBean.setEndTime(String.valueOf(Long.parseLong(startTime) + 1000 * 60 * 30));
            calendarListBean.setContent(content);
            calendarListBean.setAlert("10");
            String eventId = String.valueOf(CalendarManamger.insertSystemCalendar(context, calendarListBean));
            String laun = "javascript:Tools.saveSuccess('" + (TextUtils.isEmpty(eventId) ? 0 : 1) + "','" + title + "');";
            webview.loadUrl(laun);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showContactImDialog(String action) {
        String[] split = action.split(":");
        try {
            String targetId = URLDecoder.decode(split[2], "utf-8");
            String title = URLDecoder.decode(split[3], "utf-8");
            String content = URLDecoder.decode(split[4], "utf-8");
            String btnText = URLDecoder.decode(split[5], "utf-8");
            new DefaultDialog(context, content, "", btnText) {
                public void left() {
                    this.cancel();
                }

                public void right() {
//                    RongIM.getInstance().startPrivateChat(context, "dd0cc61140504258ab474b8f0a38bb56", "平台客服");
                    this.cancel();
                }
            }.show();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void initZixunData(String action) {
        String[] split = action.split(":");
        try {
            String islike = URLDecoder.decode(split[2], "utf-8");
            String likes = URLDecoder.decode(split[3], "utf-8");
            String summary = URLDecoder.decode(split[4], "utf-8");
//            EventBus.getDefault().post(new ZixunInfo(islike, likes, summary));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private void clipScreenShare() {
//        ClipScreenShareDialog shareDialog = new ClipScreenShareDialog(context);
//        shareDialog.setTargetView(webview);
//        shareDialog.show();
//    }

    //app:hasAddCarlender:test2:https%3A%2F%2Fupload.simuyun.com%2Flive%2Fee51aa0a-aad4-4761-9bb0-58f6daeb6097.png:1483606920000:undefined
    public void hasAddCarlender(String action) {
        String[] split = action.split(":");
//        try {
//            String title = URLDecoder.decode(split[2], "utf-8");
//            String headImgUrl = URLDecoder.decode(split[3], "utf-8");
//            String startTime = URLDecoder.decode(split[4], "utf-8");
//            String slog = URLDecoder.decode(split[5], "utf-8");
//            boolean isExist = CalendarManamger.isExist(context, title, slog);
//            System.out.println("--------isExist=" + isExist);
//            String laun = "javascript:Tools.isExistCarlender(" + isExist + ", '" + title + "')";
//            getWeb().loadUrl(laun);
//            if (!isExist && ((MApplication) MApplication.getInstance()).isPrePlayVideoDialogPrompt()) {
//                EventBus.getDefault().post(new PreVideoPlay(slog, headImgUrl, title, startTime, ""));
//                ((MApplication) MApplication.getInstance()).setPrePlayVideoDialogPrompt(false);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    private void startSearchBaseActivity() {
//        Intent intent = new Intent(context, SearchBaseActivity.class);
//        intent.putExtra(SearchBaseActivity.TYPE_PARAM, SearchBaseActivity.PRODUCT);
//        context.startActivity(intent);
    }

    private void refreshEnd(String action) {
        String[] split = action.split(":");
        try {
            String status = URLDecoder.decode(split[2], "utf-8");
            String message = URLDecoder.decode(split[3], "utf-8");
            headRefreshing = false;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void loadMore(String action) {
        String[] split = action.split(":");
        try {
            String status = URLDecoder.decode(split[2], "utf-8");
            loadMoreing = false;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void chatToTouGu(String action) {
        String[] split = action.split(":");
        try {
            String targetId = URLDecoder.decode(split[2], "utf-8");
            String name = URLDecoder.decode(split[3], "utf-8");
            ConversationBean conversationBean = new ConversationBean();
            conversationBean.setName(name);
            conversationBean.setContext(context);
            conversationBean.setTargetId(targetId);
            RxBus.get().post(RxConstant.START_CONVERSATION_OBSERVABLE, conversationBean);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendMessageToTouGu(String action) {
        String[] split = action.split(":");
        try {
            String telephone = URLDecoder.decode(split[2], "utf-8");
            if (TextUtils.isEmpty(telephone)) {
                MToast.makeText(context, context.getResources().getString(R.string.no_phone_number), Toast.LENGTH_LONG).show();
                return;
            }
            NavigationUtils.startDialogSendMessage(context, telephone);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jumpProductList(String action) {
        if (AppManager.isInvestor(context)) {
            NavigationUtils.startActivityByRouter(context, "investornmain_mainpageactivity",
                    "jumpId", 1, Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
    }

    private void callLiCaiShi(String action) {
        String[] split = action.split(":");
        try {
            final String telephone = URLDecoder.decode(split[2], "utf-8");
            String name = URLDecoder.decode(split[3], "utf-8");

            if (TextUtils.isEmpty(telephone)) {
                MToast.makeText(context, context.getResources().getString(R.string.no_phone_number), Toast.LENGTH_LONG).show();
                return;
            }

            //判断是否有拨打电话权限
            if (needPermissions(Constant.PERMISSION_CALL_PHONE)) {
                PromptManager.ShowCustomToast(context, "请到设置允许拨打电话权限");

                return;
            }

            DefaultDialog dialog = new DefaultDialog(context, "呼叫私行家".concat(name).concat("电话") + "\n" + telephone.concat(" ?"), "取消", "呼叫") {
                @Override
                public void left() {
                    dismiss();
                }

                @Override
                public void right() {
                    dismiss();
                    NavigationUtils.startDialgTelephone(context, telephone);
                }
            };
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callPhone(String action) {
        String[] split = action.split(":");
        try {
            final String telephone = URLDecoder.decode(split[2], "utf-8");
            String text = URLDecoder.decode(split[3], "utf-8");

            if (TextUtils.isEmpty(telephone)) {
                MToast.makeText(context, context.getResources().getString(R.string.no_phone_number), Toast.LENGTH_LONG).show();
                return;
            }
            //判断是否有拨打电话权限
            if (needPermissions(Constant.PERMISSION_CALL_PHONE)) {
                PromptManager.ShowCustomToast(context, "请到设置允许拨打电话权限");

                return;
            }

            DefaultDialog dialog = new DefaultDialog(context, text, "取消", "呼叫") {
                @Override
                public void left() {
                    dismiss();
                }

                @Override
                public void right() {
                    dismiss();
                    NavigationUtils.startDialgTelephone(context, telephone);
                }
            };
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void isTouGuOnline(String action) {
        String[] split = action.split(":");
        try {
            String value = URLDecoder.decode(split[2], "utf-8");
            ((InvestorAppli) InvestorAppli.getContext()).setTouGuOnline("1".equals(value) ? true : false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private void startVideoLive(String action) {
//        String[] split = action.split(":");
//        try {
//            String userId = URLDecoder.decode(split[2], "utf-8");
//            String roomNum = URLDecoder.decode(split[3], "utf-8");
//            String chatId = URLDecoder.decode(split[4], "utf-8");
//            int isShare = Integer.decode(URLDecoder.decode(split[5], "utf-8"));
//            String shareUrl = URLDecoder.decode(split[6], "utf-8");
//            String slogan = URLDecoder.decode(split[7], "utf-8");
//            Intent intent = new Intent(context, LiveActivity.class);
//            intent.putExtra(Constants.ID_STATUS, Constants.HOST);
//            MySelfInfo.getInstance().setIdStatus(Constants.HOST);
//            MySelfInfo.getInstance().setJoinRoomWay(false);
//            CurLiveInfo.setHostID(userId);
//            CurLiveInfo.setHostName("平台直播");
//            CurLiveInfo.setHostAvator("");
//            CurLiveInfo.setRoomNum(Integer.parseInt(roomNum));
//            CurLiveInfo.setMembers(0); // 添加自己
//            CurLiveInfo.setAdmires(0);
//            CurLiveInfo.setAddress("");
//            CurLiveInfo.setChatId(chatId);
//            CurLiveInfo.setIsShare(isShare);
//            CurLiveInfo.setShareUrl(shareUrl);
//            CurLiveInfo.setSlogan(slogan);
//            context.startActivity(intent);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

//    private void startVideoDetail(String action) {
//        String[] split = action.split(":");
//        try {
//            String videoId = URLDecoder.decode(split[2], "utf-8");
//            SchoolVideo schoolVideo = new SchoolVideo();
//            schoolVideo.setVideoId(videoId);
//            ToolsUtils.toPlayVideoActivity(context, schoolVideo);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    private void openCustomerChat(String action) {
        String[] split = action.split(":");
        try {
            String name = URLDecoder.decode(split[3], "utf-8");
            String userId = URLDecoder.decode(split[2], "utf-8");
//            RongIM.getInstance().startConversation(context, Conversation.ConversationType.PRIVATE, userId, name);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void cutScreenShare(String action) {
//        CutScreenShareDialog shareDialog = new CutScreenShareDialog(context);
//        shareDialog.setTargetView(getWeb());
//        shareDialog.show();
    }

    private void gotoDiscoverDetail(String action) {
        String[] split = action.split(":");
        try {
            String infoId = URLDecoder.decode(split[2], "utf-8");
            String category = URLDecoder.decode(split[3], "utf-8");
            String title = URLDecoder.decode(split[4], "utf-8");
            String summary = URLDecoder.decode(split[5], "utf-8");
            String likes = URLDecoder.decode(split[6], "utf-8");
            String isLike = URLDecoder.decode(split[7], "utf-8");
            NewsBean newsBean = new NewsBean();
            newsBean.setInfoId(infoId);
            newsBean.setCategory(category);
            newsBean.setTitle(title);
            newsBean.setSummary(summary);
            newsBean.setLikes(Integer.valueOf(likes));
            newsBean.setIsLike(isLike);

            Intent i = new Intent(context, BaseWebViewActivity.class);
            Bundle bundle = new Bundle();
            i.putExtra(WebViewConstant.push_message_title, title);
            String url = CwebNetConfig.touTiaoHao.concat(newsBean.getInfoId()).concat("&category=").concat(newsBean.getCategory());
            System.out.println("------url=" + url);
            i.putExtra(WebViewConstant.push_message_url, url);
//        shareurl = Domain.foundNewshare + newsBean.getInfoId() + "&category=" + newsBean.getCategory() + "&share=2";
//            bundle.putSerializable(FoundNewsDetailActivity.NEW_PARAM_NAME, newsBean);
//            i.putExtras(bundle);
            context.startActivity(i);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private void SignIn() {
//        JSONObject j = new JSONObject();
//        try {
//            j.put("adviserId", MApplication.getUserid());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        /**
//         * 执行签到接口
//         */
//        new SignInTask(context).start(j.toString(), new HttpResponseListener() {
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//                    if (response.has("msg")) {
//                        new MToast(context).show(response.getString("msg"), 0);
//                        web.loadUrl("javascript:signSuccess();");
//                    } else {
//                        /**
//                         * 签到成功提示弹框
//                         */
//                        web.loadUrl("javascript:signSuccess();");
//                        SignDialog d = new SignDialog(context);
//                        MApplication.getUser().setIsSingIn(1);
//                        d.setData(response);
//                        d.show();
//                        DatabaseUtils databaseUtils = new DatabaseUtils(context);
//                        MyTaskBean myTaskBean = databaseUtils.getMyTask("每日签到");
//                        myTaskBean.setState(2);
//                        databaseUtils.updataMyTask(myTaskBean);
//                        EventBus.getDefault().post(new RefreshUserinfo());
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onErrorResponse(String error, int statueCode) {
//                try {
//                    JSONObject js = new JSONObject(error);
//                    new MToast(context).show(js.getString("message"), 0);
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                web.loadUrl("javascript:signError();");
//            }
//        });
//    }

    private void shareDataFriend(String action) {
        // "inviteCust","2",'/apptie/invite_preview_wx.html?site=2'); 1:普通邀请,可分享微信好友和朋友圈、2:精准邀请,只能分享到微信好友
//        String[] split = action.split(":");
//        try {
//            String title = URLDecoder.decode(split[5], "utf-8");
//            String type = URLDecoder.decode(split[2], "utf-8");
//            String content = URLDecoder.decode(split[4], "utf-8");
//            String link = URLDecoder.decode(split[3], "utf-8");
//            link = link.startsWith("/") ? CwebNetConfig.baseParentUrl + link : CwebNetConfig.baseParentUrl + "/" + link;
//            CommonShareBean commonShareBean = new CommonShareBean();
//            commonShareBean.setTitle(title);
//            commonShareBean.setContent(content);
//            commonShareBean.setLink(link);
//            if ("2".equals(type)) {
//                CommonShareDialog commonShareDialog = new CommonShareDialog(context, 1, commonShareBean, null);
//                commonShareDialog.show();
//            } else {
//                CommonShareDialog commonShareDialog = new CommonShareDialog(context, 0, commonShareBean, null);
//                commonShareDialog.show();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
//    private void openWeixin(final String title, final String content, final String url, final int image) {
//        WeiXinShare sh = new WeiXinShare(context, "");
//        sh.shareWeixinWithID(title, content, url, image);
//    }
    //    CommonShareDialog commonShareDialog;

    //1成功0失败 分享出发js通知H5
//    boolean isShowing;isShowingΩ
    String shareJsAction;

    private void shareToC(String actionUrl) {
        String actionDecode = URLDecoder.decode(actionUrl);
        String[] split = actionDecode.split(":");
        String sharePYQtitle = "";

        String mytitle = split[2];
        String subTitle = split[3];
        String imageTitle = split[4];
        String link = split[5];
        shareJsAction = "";
        if (split.length >= 7) {
            sharePYQtitle = split[6];
            shareJsAction = split[6];
        }
//        webview.loadUrl("javascript:resultForSharing(1)");

        boolean isProductShare = actionDecode.contains("product/index.html");
        link = link.startsWith("/") ? BaseWebNetConfig.baseParentUrl + link.substring(0) : BaseWebNetConfig.baseParentUrl + link;
        ShareCommonBean shareCommonBean = new ShareCommonBean(mytitle, subTitle, link, "");
//        if (isShowing) return;
        CommonShareDialog commonShareDialog = new CommonShareDialog(context, isProductShare ? CommonShareDialog.Tag_Style_WeiXin : CommonShareDialog.Tag_Style_WxPyq, shareCommonBean, new CommonShareDialog.CommentShareListener() {
            @Override
            public void completShare(int shareType) {
                if (!BStrUtils.isEmpty(shareJsAction)) {
                    webview.loadUrl("javascript:" + shareJsAction + "(1)");
                }

                try {
                    String decodeUrl = URLDecoder.decode(actionUrl, "utf-8");
                    //分享微信朋友圈成功
                    if (decodeUrl.contains("new_detail_toc.html") || decodeUrl.contains("information/details.html")) { // 资讯分享需要获取云豆和埋点
                        if (!AppManager.isVisitor(context)) {
                            //自选页面分享朋友圈成功
                            TaskInfo.complentTask("分享资讯");
                        }
                        if (CommonShareDialog.SHARE_WXCIRCLE == shareType) {
                            if (context instanceof BaseWebViewActivity) {
                                BaseWebViewActivity baseWebViewActivity = (BaseWebViewActivity) context;
                                DataStatistApiParam.onStatisToCShareInfOnCircle(mytitle, baseWebViewActivity.getTitleName());
                            }
                        }
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                if (isProductShare) { // 产品分享需要获取云豆
                    TaskInfo.complentTask("分享产品");
                }
//                isShowing = false;
            }

            @Override
            public void cancleShare() {
//                isShowing = false;
                if (!BStrUtils.isEmpty(shareJsAction)) {
                    webview.loadUrl("javascript:" + shareJsAction + "(0)");

                }
            }
        });
        commonShareDialog.show();
//        isShowing = true;
    }

    private void backPage(String action) {
        String actionDecode = URLDecoder.decode(action);
        String[] split = actionDecode.split(":");
        int index = Integer.valueOf(split[2]) < 0 ? 0 : Integer.valueOf(split[2]);
        Intent intent = new  Intent();
        intent.putExtra(BaseWebViewActivity.BACK_PARAM, index - 1);
        context.setResult(BaseWebViewActivity.BACK_RESULT_CODE, intent);
        context.finish();
    }

    private void mobClick(String action) {
        if (AppManager.getUserInfo(context).getToC() == null) {
            return;
        }
        String decode = URLDecoder.decode(action);
        String[] split = decode.split(":");
        String args = split[4] + ",$";
        String[] split1 = args.split(",");
        if (split[3].startsWith("1")) {
//            split1[1] = AppManager.getUserInfo(context).getToB().isColorCloud();
//            HashMap<String, String> params = DataStatisticsUtils.getParams(split[2], split[3], split1);
//            DataStatisticsUtils.push(context, params);
        } else {
            split1[1] = AppManager.getUserInfo(context).getToC().getBindTeacher();
            HashMap<String, String> params = DataStatisticsUtils.getParams(split[2], split[3], split1);
            DataStatisticsUtils.push(context, params, true);
        }
    }

    private void startActivityOverridePendingTransition(String action) {
        String[] split = action.split(":");
        String string = split[2];
        String title = split[3];
        string = CwebNetConfig.baseParentUrl + string;
        Intent i = new Intent(context, BaseWebViewActivity.class);
        // i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            i.putExtra(WebViewConstant.push_message_url, URLDecoder.decode(string, "utf-8"));
            i.putExtra(WebViewConstant.push_message_title, URLDecoder.decode(title, "utf-8"));
            if (split.length > 5) {
                i.putExtra(WebViewConstant.PAGE_SHOW_TITLE, Boolean.valueOf(split[5]));
            }
            ((Activity) context).startActivityForResult(i, 300);
            ((Activity) context).overridePendingTransition(R.anim.push_bottom_in, R.anim.push_bottom_out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void overActivityOverridePendingTransition() {
        ((Activity) context).overridePendingTransition(R.anim.push_bottom_out, R.anim.push_bottom_in);
        context.finish();
        //ScreenManager.getScreenManager().popActivity(false);
    }

    private void toQrCode() {

//        NavigationUtils.startActivityByRouter(context, RouteConfig.GOTO_TWO_CODE_ACTIVITY);
        Intent intent3 = new Intent(context, QrMidActivity.class);
        context.startActivity(intent3);
    }

    private void showComfirmDialog(String action) {
        String actionDecode = URLDecoder.decode(action);
        String[] split = actionDecode.split(":");
        if (split.length > 4) {
            DefaultDialog dialog = new DefaultDialog(context, split[3], split[4], split[5]) {
                @Override
                public void left() {
                    dismiss();
                    webview.loadUrl("javascript:Tools.clickedIndex('0')");
                }

                @Override
                public void right() {
                    dismiss();
                    webview.loadUrl("javascript:Tools.clickedIndex('1')");
                }
            };
            dialog.show();
        }
    }

    private String getUrl(String action) {
        String[] split = action.split(":");
        if (split.length > 1) {
            return URLDecoder.decode(split[2]);
        }
        return action;
    }

    private String getValue(String action) {
        String[] split = action.split(":");
        if (split.length > 1) {
            return URLDecoder.decode(split[2]);
        }
        return action;
    }

    private void startImagePage(String action) {
//        ImageSelector selector = ImageSelector.create();
//        selector.single();  // 选择一张图片
//        selector.start(context, BaseWebViewActivity.BACK_CAMERA_CODE);
//        PhotoPickerIntent intent = new PhotoPickerIntent(context);
//        intent.setPhotoCount(1);
//        intent.setShowCamera(false);
//        context.startActivityForResult(intent,  BaseWebViewActivity.BACK_CAMERA_CODE);
    }

    private void recommentFriend() {
//        CommonShareBean shareBean = new CommonShareBean();
//        shareBean.setTitle("推荐理财师好友安装私募云，一起来聚合财富管理力量！\", \"http://www.simuyun.com/invite/invite.html");
//        shareBean.setContent("推荐理财师好友安装私募云，一起来聚合财富管理力量！http://www.simuyun.com/invite/invite.html");
//        CommonShareDialog dialog = new CommonShareDialog(context,1,shareBean,null);
//        dialog.show();
    }

    private void logOutUser() {
        LogOutAccount returnLogin = new LogOutAccount();
        returnLogin.accounttExit(context);
    }

    private void checkRengou(String action) {
        String decode = URLDecoder.decode(action);
        String[] split = decode.split(":");
        String canTakeUp = split[2];
    }

    private void toMessageList() {
        NavigationUtils.startActivityByRouter(context, "immodule_messagelistactivity", new Bundle(), Intent.FLAG_ACTIVITY_NEW_TASK);
//        RxBus.get().post(RxConstant.OPEN_MESSAGE_LIST_PAGE_OBSERVABLE, true);
    }

    private void showToast(String action) {
        String actionDecode = URLDecoder.decode(action);
        String[] split = actionDecode.split(":");
        String content = split[2];
        PromptManager.ShowCustomToast(context, content);
    }

    private void showNewToast(String action) {
        String actionDecode = URLDecoder.decode(action);
        String[] split = actionDecode.split(":");
        String content = split[2];
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
    }

    private void openDialog(String action) {
        String actionDecode = URLDecoder.decode(action);
        String[] split = actionDecode.split(":");
        String content = split[2];
        new DefaultDialog(context, content, "取消", "兑换") {
            @Override
            public void left() {
                cancel();
            }

            @Override
            public void right() {
                webview.loadUrl("javaScript:products.exchange()");
                cancel();
            }
        }.show();
    }

    private void openMallPage(String action) {
        String actionDecode = URLDecoder.decode(action);
        String[] split = actionDecode.split(":");
        String url = split[3];
        String title = split[2];
        if (!url.contains("http")) {
            url = BaseWebNetConfig.baseParentUrl + url;
        }

        Utils.OpenSharePage(context, RouteConfig.GOTO_RIGHT_SHARE_ACTIVITY, title, url, false, false, true);

//        Intent i = new Intent(context, BaseWebViewActivity.class);
//
//        i.putExtra(WebViewConstant.push_message_url, url);
//        i.putExtra(WebViewConstant.push_message_title, title);
//        i.putExtra(WebViewConstant.RIGHT_SAVE, false);
//        i.putExtra(WebViewConstant.RIGHT_SHARE, false);
//        i.putExtra(WebViewConstant.PAGE_INIT, false);
//        if (split.length >= 5) {
//            i.putExtra(WebViewConstant.PAGE_SHOW_TITLE, Boolean.valueOf(split[split.length - 1]));
//        }
//        ((Activity) context).startActivityForResult(i, 300);
    }

    public void toviewpdf(String action) {
        PromptManager.ShowCustomToast(context, "展示PDF事件");
        try {
            String baseParams = URLDecoder.decode(action, "utf-8");
            String[] split = baseParams.split(":");
            String padUrl = split[2];
//            Intent i = new Intent(context, PDFActivity.class);
//            i.putExtra(WebViewConstant.pdf_url, padUrl);
//            i.putExtra("pdfName", URLDecoder.decode(split[3], "utf-8"));
//            i.putExtra("isSecret", 1);
//            context.startActivity(i);
            HashMap<String, Object> map = new HashMap<>();
            map.put("pdfurl", padUrl + ":" + split[3]);
            map.put("pdftitle", split[4]);
            NavigationUtils.startActivityByRouter(context, RouteConfig.GOTOPRODUCTDETAIL, map);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private void drawAlert(String action) {
//        EventBus.getDefault().post(new Redeem("chexiao"));
//    }
//
//    private void commitRedeem(String action) {
//        EventBus.getDefault().post(new Redeem("commit"));
//    }

//    private void jumpOrderList(String action) {
//        String[] split = action.split(":");
//
//        String custId = split[2];
//
//        Intent intent = new Intent(context, CustOrderActivity.class);
//        try {
//            String custName = URLDecoder.decode(custId, "utf-8");
//            intent.putExtra("custId", custName);
//            if (split.length == 4) {
//                String credentialsNumber = split[3];
//                intent.putExtra("credentialsNumber", credentialsNumber);
//            }
//            context.startActivity(intent);
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * 订单详情跳转到产品
     */
//    private void jumpProduct(String action) {
//        String encode = URLDecoder.decode(action);
//        JSONObject j = new JSONObject();
//
//        try {
//            j.put("schemeId", action.split(":")[2]);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        new ProductDetailTask(context).start(j.toString(), new HttpResponseListener() {
//            @Override
//            public void onResponse(JSONObject response) {
//                Gson g = new Gson();
//                ProductBean productBean = g.fromJson(response.toString(), ProductBean.class);
//                Intent i = new Intent(context, ProductDetailActivity.class);
//                i.putExtra(Contant.product, productBean);
//                context.startActivity(i);
//                SPSave.getInstance(context).putString("myProductID", productBean.getSchemeId());
//            }
//
//            @Override
//            public void onErrorResponse(String error, int statueCode) {
//
//            }
//        });
//    }

    /**
     * 产品跳转到视频
     *
     * @param action
     */
    private void playVideo(String action) {
        String[] vas = action.split(":");
        String videoId = action.substring(action.lastIndexOf("Video:") + 6);
        videoId = TextUtils.isEmpty(videoId) ? vas[2] : videoId;
        NavigationUtils.startVidoDetailActivity(context, videoId, null, 2);
    }

    /**
     * 统计h5产品详情点击事件
     *
     * @param action
     */
//    private void mobclick(String action) {
//        String myProductName = SPSave.getInstance(MApplication.mContext).getString("myProductName");
//        HashMap<String, String> umengMap = new HashMap<String, String>();
//        umengMap.put("产品", myProductName);
//        umengMap.put("按钮", action.split(":")[2]);
//        umengMap.put("机构", SPSave.getInstance(MApplication.mContext).getString("organizationName"));
//        MobclickAgent.onEvent(MApplication.mContext, "book_click", umengMap);
//    }

    /**
     * 复制内容到剪切板
     *
     * @param action
     */
    private void copytoclipboard(String action) {
        String[] split = action.split(":");
        ClipboardManager clip = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        try {
            clip.setText(URLDecoder.decode(split[2], "utf-8"));// 复制
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        new MToast(context).show("内容已复制到剪切板", 0);
    }

    /**
     * 从剪切板中获取内容
     *
     * @param action
     */
    private void getContentFromPasteBoard(String action) {
        String[] split = action.split(":");
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboardManager.getText() != null) {
            String content = clipboardManager.getText().toString();
            webview.loadUrl("javascript:setPasteServeCode('" + content + "')");
        }
    }

    /**
     * 修改密码
     *
     * @param action
     */
    private void changepassword(String action) {
        String[] split = action.split(":");
        String password = split[2];
        final String newPassword = URLDecoder.decode(split[3]);
        final String failure = "修改失败";
        ApiClient.modifyPassword(AppManager.getUserInfo(context).getUserName(), MD5Utils.getShortMD5(password), MD5Utils.getShortMD5(newPassword)).subscribe(new RxSubscriber<CommonEntity.Result>() {
            @Override
            protected void onEvent(CommonEntity.Result result) {
                try {
                    String string = result.result;
                    if (!TextUtils.isEmpty(string) && string.contains("suc")) {
                        Toast.makeText(context, "密码修改成功", Toast.LENGTH_SHORT).show();
                        context.finish();
                    } else {
                        Toast.makeText(context, failure, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void onRxError(Throwable error) {
                Toast.makeText(context, failure, Toast.LENGTH_SHORT).show();
                webview.loadUrl("javascript:setData('" + failure + "')");
                context.finish();
            }
        });
    }

    /**
     * 查看pdf
     *
     * @param
     */
//    public void viewpdf(String action) {
//        String[] split = action.split(":");
//        String string = split[2];
//        Intent i = new Intent(context, PDFActivity.class);
//        try {
//            i.putExtra(Contant.pdf_url, repleaseUrl(URLDecoder.decode(string, "utf-8")));
//            i.putExtra("pdfName", URLDecoder.decode(split[3], "utf-8"));
//            i.putExtra("isSecret", 0);
//            context.startActivity(i);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
    private String repleaseUrl(String url) {
        return url.replace("peyunupload//", "peyunupload/");
    }

    /**
     * 关闭页面
     *
     * @param action
     */
    private void closepage(String action) {

        ((Activity) context).finish();
    }

    private void openUrl(String data) {
        try {
            String[] split = data.split(":");
            String string = URLDecoder.decode(split[2], "utf-8");
            int start = string.indexOf("category=");
            String catagory = string.substring(start + 9);
            if (!string.contains("http")) {
                string = BaseWebNetConfig.baseParentUrl + string;
            }
            Intent i = new Intent(context, BaseWebViewActivity.class);
            i.putExtra(WebViewConstant.push_message_url, URLDecoder.decode(string, "utf-8"));
            if (split.length >= 4) {
                i.putExtra(WebViewConstant.push_message_title, URLDecoder.decode(split[3], "utf-8"));
            } else {
                i.putExtra(WebViewConstant.push_message_title, formatCodeToName(catagory));
            }
            i.putExtra(WebViewConstant.RIGHT_SAVE, false);
            i.putExtra(WebViewConstant.RIGHT_SHARE, false);
            i.putExtra(WebViewConstant.PAGE_INIT, false);
            ((Activity) context).startActivity(i);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String formatCodeToName(String catagory) {
        Set<String> set = Constant.NewFoundHashMap.keySet();
        if (set.contains(catagory)) {
            return Constant.NewFoundHashMap.get(catagory);
        }
        return "资讯信息";
    }

    /**
     * 打开新页面
     *
     * @param data url路径
     *             rightSave 右上角是否有保存按钮
     *             initPage 页面是否初始化
     *             rightShare 右边是否有分享
     */
    private void openpage(String data, boolean rightSave, boolean initPage, boolean rightShare) {
        try {
            String baseParams = URLDecoder.decode(data, "utf-8");
            LogUtils.Log("s", "s");
            if (intecepterInvister(baseParams, rightSave, initPage, rightShare)) {
                return;
            }
            String[] split = baseParams.split(":");
            String url = split[2];
            String title = split[3];
            if (url.startsWith("app6.0")) {
                url = "/" + url;
            }
            boolean isHasHttp = false;
            if (url.contains("http")) {
                url = split[2] + ":" + split[3];
                if (split.length >= 5) {
                    title = split[4];
                    isHasHttp = true;
                }
            } else {
                url = BaseWebNetConfig.SERVER_ADD + url;
            }
            Intent i = new Intent(context, BaseWebViewActivity.class);

            i.putExtra(WebViewConstant.push_message_url, url);
            i.putExtra(WebViewConstant.push_message_title, title);
            if (initPage) {
                String pushMessage = isHasHttp ? split[5] : split[4];
                i.putExtra(WebViewConstant.push_message_value, pushMessage);
            }
            i.putExtra(WebViewConstant.RIGHT_SAVE, rightSave);
            i.putExtra(WebViewConstant.RIGHT_SHARE, rightShare);
            i.putExtra(WebViewConstant.PAGE_INIT, initPage);

            if (split.length >= (isHasHttp ? 6 : 5)) {
                i.putExtra(WebViewConstant.PAGE_SHOW_TITLE, Boolean.valueOf(split[split.length - 1]));
            }
            //解决产品详情的产品公告进入不能分享

            //解决产


//            ((Activity) context).startActivityForResult(i, 300);

            if ("产品详情".equals(title) && AppManager.isInvestor(context)) {
//            new RundouTaskManager(context).executeRundouTask("查看产品");
            } else if ("产品公告详情".equals(title)) {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put(WebViewConstant.RIGHT_SHARE, true);
                hashMap.put(WebViewConstant.push_message_title, title);
                hashMap.put(WebViewConstant.push_message_url, url);

                NavigationUtils.startActivityByRouter(context, RouteConfig.GOTO_RIGHT_SHARE_ACTIVITY, hashMap);

            } else {
                ((Activity) context).startActivityForResult(i, 300);
            }
//
//                if (!TextUtils.isEmpty(url) && url.contains("discover/details.html")) {
////            new RundouTaskManager(context).executeRundouTask("查看资讯");
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 监听页面跳转的拦截，拦截成功返回ture, 失败返回false
     *
     * @return
     */
    private boolean intecepterInvister(String actionUrl, boolean rightSave, boolean initPage, boolean rightShare) {
        if (actionUrl.contains(WebViewConstant.IntecepterActivity.RECOMMEND_FRIEND) ||
                actionUrl.contains(WebViewConstant.IntecepterActivity.LIFE_DETAIL) ||
                actionUrl.contains(WebViewConstant.IntecepterActivity.LIFE_SPECIAL) ||
                actionUrl.contains(WebViewConstant.IntecepterActivity.LIFE_RED_LINE) ||
                actionUrl.contains(WebViewConstant.IntecepterActivity.ACTIVITTE_DRAGON_DEATIL) ||
                actionUrl.contains(WebViewConstant.IntecepterActivity.HEALTH_SPECIAL) ||
                actionUrl.contains(WebViewConstant.IntecepterActivity.HEALTH_DETAIL) ||
                actionUrl.contains(WebViewConstant.IntecepterActivity.HEALTH_SPECIAL) ||
                actionUrl.contains(WebViewConstant.IntecepterActivity.PRODUCT_DETAIL)) {

            String[] split = actionUrl.split(":");
            String url = split[2];
            String title = split[3];
            if (url.startsWith("app6.0")) {
                url = "/" + url;
            }
            boolean isHasHttp = false;
            if (url.contains("http")) {
                url = split[2] + ":" + split[3];
                if (split.length >= 5) {
                    title = split[4];
                    isHasHttp = true;
                }
            } else {
                url = BaseWebNetConfig.SERVER_ADD + url;
            }
            HashMap<String, Object> hashMap = new HashMap<>();

            hashMap.put(WebViewConstant.push_message_url, url);
            hashMap.put(WebViewConstant.push_message_title, title);
            if (initPage) {
                String pushMessage = isHasHttp ? split[5] : split[4];
                hashMap.put(WebViewConstant.push_message_value, pushMessage);
            }
            hashMap.put(WebViewConstant.RIGHT_SAVE, rightSave);
            hashMap.put(WebViewConstant.RIGHT_SHARE, rightShare);
            hashMap.put(WebViewConstant.PAGE_INIT, initPage);

            if (split.length >= (isHasHttp ? 6 : 5)) {
                hashMap.put(WebViewConstant.PAGE_SHOW_TITLE, Boolean.valueOf(split[split.length - 1]));
            }
            NavigationUtils.startActivityByRouter(context, RouteConfig.GOTO_RIGHT_SHARE_ACTIVITY, hashMap);
            return true;
        }
        return false;
    }

    /**
     * 报备页面跳转不带参数
     */
//    private void toBaobei() {
//        Intent ii = new Intent(context, BaobeiActivity.class);
//        ii.putExtra(Contant.productID, baobei);
//        context.startActivity(ii);
//    }

    /**
     * 报备页面带参数
     *
     * @param data
     */
//    private void toBaobeiWithdata(String data) {
//
//        String[] split = data.split(":");
//        try {
//
//            Intent ii = new Intent(context, BaobeiActivity.class);
//            ii.putExtra(Contant.productID, URLDecoder.decode(split[2], "utf-8"));
//            context.startActivity(ii);
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//    }


//    /**
//     * 设置cook信息
//     */
//    public static void synCookies(Context context, String url) {
//        CookieSyncManager.createInstance(context);
//        CookieManager cookieManager = CookieManager.getInstance();
//        cookieManager.setAcceptCookie(true);
//        cookieManager.removeSessionCookie();//移除
//        cookieManager.setCookie(url, productCookieInfo(context));//cookies是在HttpClient中获得的cookie
//        CookieSyncManager.getInstance().sync();
//    }

    /**
     * 生成cook信息
     *
     * @param context
     * @return
     */
//    public static String productCookieInfo(Context context) {
//        StringBuffer sb = new StringBuffer();
//        sb.append("path=").append("/;").
//                append("expires=").append(new Date().toString()).append(";").
//                append("token=").append(MApplication.getToken()).append(";").
//                append("uid=").append(MApplication.getUserid()).append(";").
//                append("version=").append(AppInfo.versionCode(context)).append(";").
//                append("role=").append("2;");
//        return sb.toString();
//    }

//    /**
//     * 关联微信
//     */
//    private void guanlianWeixin() {
//        if (!AppInfo.isNetworkConnected(context)) {
//            new MToast(context).show("网络连接失败，请稍候重试", 0);
//            return;
//        }
//        try {
//            if (null == MApplication.getUser()) {
//                return;
//            }
//            String wechatUnionid = MApplication.getUser().getWechatUnionid();
//            if (!TextUtils.isEmpty(wechatUnionid)) {
//                new MToast(context).show("已经关联微信", 0);
//
//                return;
//            }
//
//            // new MToast(getActivity()).show("关联微信", 0);
//
//            final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.login");
//
//            // 添加微信平台
//            UMWXHandler wxHandler = new UMWXHandler(context, Contant.weixin_appID, Contant.weixin_appSecret);
//
//            wxHandler.addToSocialSDK();
//
//            final Context mContext = context;
//
//            mController.doOauthVerify(context, SHARE_MEDIA.WEIXIN, new SocializeListeners.UMAuthListener() {
//                @Override
//                public void onStart(SHARE_MEDIA platform) {
//                    Toast.makeText(mContext, "授权开始", Toast.LENGTH_SHORT).show();
//                }
//
//                @Override
//                public void onError(SocializeException e, SHARE_MEDIA platform) {
//                    Toast.makeText(mContext, "授权错误", Toast.LENGTH_SHORT).show();
//                }
//
//                @Override
//                public void onComplete(Bundle value, SHARE_MEDIA platform) {
//                    Toast.makeText(mContext, "授权完成", Toast.LENGTH_SHORT).show();
//                    // 获取相关授权信息
//                    mController.getPlatformInfo(mContext, SHARE_MEDIA.WEIXIN, new SocializeListeners.UMDataListener() {
//                        @Override
//                        public void onStart() {
//                            Toast.makeText(mContext, "获取平台数据开始...", Toast.LENGTH_SHORT).show();
//                        }
//
//                        /**
//                         *
//                         * {sex=1, nickname=TTK,
//                         * unionid=oA2i4uHM0aFT5I5hcn7BeXjLSObg, province=北京,
//                         * openid=oGyGZs2ys2dHK4Kz5sbMQIX7EJM4, language=zh_CN,
//                         * headimgurl=http://wx.qlogo.cn/mmopen/
//                         * tyicTLW0VS7Brpep65WbKxdNRLEs4sgUBCtXIicoeY4VtToegaiaIlPKoSVAUnSFRJMl4OIGnJiacldjO0iandB43RHMwkm7wDehh
//                         * /0, country=中国, city=}
//                         */
//                        @Override
//                        public void onComplete(int status, Map<String, Object> info) {
//                            if (status == 200 && info != null) {
//                                StringBuilder sb = new StringBuilder();
//                                // Set<String> keys = info.keySet();
//                                // for (String key : keys) {
//                                // sb.append(key
//                                // + "="
//                                // + info.get(key)
//                                // .toString()
//                                // + "\r\n");
//                                // }
//                                try {
//
//                                    weixinLoginPost(info.get("sex") + "", (String) info.get("nickname"),
//                                            (String) info.get("unionid"), (String) info.get("openid"),
//                                            (String) info.get("province"), (String) info.get("headimgurl"));
//
//                                } catch (Exception e) {
////								new MToast(getActivity()).show("关联失败", 0);
//                                    e.printStackTrace();
//                                }
//                                Log.i("TestData", sb.toString());
//                            } else {
//                                Log.i("TestData", "发生错误：" + status);
//                            }
//                        }
//                    });
//                }
//
//                @Override
//                public void onCancel(SHARE_MEDIA platform) {
//                    Toast.makeText(mContext, "授权取消", Toast.LENGTH_SHORT).show();
//                }
//            });
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

//    /**
//     * 微信关联
//     */
//    private void weixinLoginPost(String sex, String nickname, final String unionid, String openid, String address,
//                                 String headimgurl) {
//        JSONObject j = new JSONObject();
//        try {
//
//            j.put("adviserId", MApplication.getUserid());
//            //j.put("sex", sex);
//            j.put("nickname", nickname);
//            j.put("unionid", unionid);
////            j.put("openid", openid);
////            j.put("address", address);
////            j.put("headimgurl", headimgurl);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        new GuanlianweixinTask(context).start(j.toString(), new HttpResponseListener() {
//
//            @Override
//            public void onResponse(JSONObject response) {
//
//                try {
//                    new MToast(context).show("关联成功", 0);
//
//                    UserInfo user = MApplication.getUser();
//                    user.setWechatUnionid(unionid);
//                    MApplication.setUser(user);
//                    getWeb().loadUrl("javascript:localUp(sUserAgg,'unionid','" + unionid + "');");
//
//                    // if (response.has("advisersId")
//                    // && !TextUtils.isEmpty(responseC
//                    // .getString("advisersId"))) {
//                    // MApplication.getUser()
//                    // .setWechatUnionid(object3);
//                    // EventBus.getDefault().post(
//                    // new RefreshUserinfo());
//                    // new MToast(getActivity()).show("关联成功", 0);
//                    // return;
//                    // } else {
//                    //
//                    // String string = response.getString("errmsg");
//                    // new MToast(getActivity()).show(string, 0);
//                    // return;
//                    // }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
////				new MToast(getActivity()).show("关联失败", 0);
//            }
//
//            @Override
//            public void onErrorResponse(String error, int statueCode) {
//                try {
//                    JSONObject jsonObj = new JSONObject(error);
//                    if (null != jsonObj.optString("message")) {
//                        new MToast(context).show(jsonObj.getString("message"), 0);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }
//

    /**
     * 跳转到我的任务页面
     */
    private void goMyTask() {
        NavigationUtils.startActivityByRouter(context, RouteConfig.INVTERSTOR_MAIN_TASK);
    }

    // 判断是否缺少权限
    protected boolean needsPermission(String permission) {
        return ContextCompat.checkSelfPermission(BaseApplication.getContext(), permission) != PackageManager.PERMISSION_GRANTED;
    }

    // 判断权限集合
    protected boolean needPermissions(String... permissions) {
        //判断版本是否兼容
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return false;
        }
        boolean isNeed;
        for (String permission : permissions) {
            isNeed = needsPermission(permission);
            if (isNeed) {
                return true;
            }
        }
        return false;
    }
}
