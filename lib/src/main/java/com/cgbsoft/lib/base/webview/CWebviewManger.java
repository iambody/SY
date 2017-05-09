package com.cgbsoft.lib.base.webview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.ClipboardManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.cgbsoft.lib.BaseApplication;
import com.cgbsoft.lib.R;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.utils.tools.CacheDataManager;
import com.cgbsoft.lib.utils.tools.NavigationUtils;
import com.cgbsoft.lib.utils.tools.Utils;
import com.cgbsoft.lib.utils.ui.MyDogDialog;
import com.cgbsoft.lib.utils.ui.OtheriOSDialog;
import com.cgbsoft.lib.widget.IOSDialog;
import com.cgbsoft.lib.widget.MToast;
import com.tencent.smtt.sdk.WebView;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Set;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/5/4-17:58
 */
public class CWebviewUtil {

    private Activity context;
    private WebView webview;
    private boolean headRefreshing; // 头部是否有刷新
    private boolean loadMoreing; // 加载更大

    private static CWebviewUtil instance = null;

    private CWebviewUtil(Activity activity) {
        super();
        this.context = activity;
    }

    public static synchronized CWebviewUtil getInstance(Activity bacActivity) {
        if (instance == null) {
            instance = new CWebviewUtil(bacActivity);
        }
        return instance;
    }

    public boolean isHeadRefreshing() {
        return headRefreshing;
    }

    public void setHeadRefreshing(boolean headRefreshing) {
        this.headRefreshing = headRefreshing;
    }

    public void setWeb(WebView webViews) {
        this.webview = webViews;
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

        if (action.contains("filingdata")) {
//            toBaobeiWithdata(action);
        } else if (action.contains("filing")) {
//            toBaobei();
        } else if (action.contains("openMytask")) {//C端新加需求 需要首页有我的任务 跳转到我的任务
//            goMyTask();
        } else if (action.contains("openpage")) {
            openpage(action, false, false, false);
        } else if (action.contains("openSingleTaskPage")) {
//            openSingleTaskPage(action, false, false, true);
        } else if (action.contains("closepage") || action.contains("closePage")) {
            closepage(action);
        } else if (action.contains("secretviewpdf")) {
//            secretviewpdf(action);
        } else if (action.contains("viewpdf")) {
//            viewpdf(action);
        } else if (action.contains("changepassword")) {
//            changepassword(action);
        } else if (action.contains("copytoclipboard")) {
            copytoclipboard(action);
        } else if (action.contains("clickPasteServeCode")) {
            getContentFromPasteBoard(action);
        } else if (action.contains("playVideo")) {
//            playVideo(action);
        } else if (action.contains("jumpProductList")) {

        } else if (action.contains("jumpProduct")) {
//            jumpProduct(action);
        } else if (action.contains("custbyorder")) {
//            jumpOrderList(action);
//        } else if (action.contains("commitRedeem")) {
//            commitRedeem(action);
        } else if (action.contains("withdrawAlert")) {
//            drawAlert(action);
        } else if (action.contains("secrecyPage")) {
            //TODO
        } else if (action.contains("submitQuestionnaire")) {
//            toRiskRusult(action);
        } else if (action.contains("openMallPage")) {
//            openMallPage(action);
        } else if (action.contains("openDialog")) {
            openDialog(action);
        } else if (action.contains("telephone")) {
            Utils.telHotline(context);
        } else if (action.contains("openMallMain")) {
        } else if (action.contains("submitQuestionnaire")) {
        } else if (action.contains("iftakeup")) {
//            checkRengou(action);
        } else if (action.contains("saveSuccess")) { // CHEN
            Intent intent = new Intent();
            intent.putExtra(PushMsgActivity.SAVE_PARAM, getValue(action));
            context.setResult(1, intent);
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
//            startImagePage(action);
        } else if (action.contains("scanning")) {
//            toQrCode();
        } else if (action.contains("toastStatus") || action.equals("toastSuccess")) {
            showToast(action);
        } else if (action.contains("showAlert")) {
//            showComfirmDialog(action);
        } else if (action.contains("openMessage")) {
//            toMessageList();
        } else if (action.contains("cloudAnimate")) {
            //TODO 云动画
        } else if (action.contains("practiseAbility")) {
//            String adv2 = SPreference.getUser().getAdviserState();
//            if (adv2.equals("1")) {
//                NavigationUtils.startActivityForResult(context, PlannerActivity.class, Contant.CERTIFY_RENZHENG);
//            } else if (adv2.equals("2")) {
//                Intent intent = new Intent(context, RenzhengActivity.class);
//                intent.putExtra(Contant.ADVISER_STATE, Contant.ADVISER_STATE_UNDER_REVIEW);
//                context.startActivity(intent);
//            } else if (adv2.equals("3")) {
//            } else if (adv2.equals("4")) {
//                Intent intent1 = new Intent(context, CheckFailureActivity.class);
//                intent1.putExtra("type", 2);
//                context.startActivity(intent1);
//            }
        } else if (action.contains("relationAssets")) {//关联资产动作
           // NavigationUtils.startActivityForResult(context, RelativeAssetActivity.class, PushMsgActivity.RELATIVE_ASSERT);
        } else if (action.contains("pssetsProve")) {
//            NavigationUtils.startActivityForResult(context, AssetProveActivity.class, PushMsgActivity.ASSERT_PROVE);
        } else if (action.contains("selectedInvestment")) {
           // EventBus.getDefault().post(new Qrcode());
        } else if (action.contains("recommendFriend")) {
//            recommentFriend();
        } else if (action.contains("h5-native")) {
//            VersonUpdate();
        } else if (action.contains("logOut")) {
            IOSDialog dialog = new IOSDialog(context, "", "确认要退出账号吗？", "取消", "确认", false) {
                @Override
                public void left() {
                    dismiss();
                }

                @Override
                public void right() {
                    dismiss();
//                    logOutUser();
                }
            };
            dialog.show();
        } else if (action.contains("backPage")) {
            backPage(action);
        } else if (action.contains("toast") || action.contains("toastInfo")) {
            showToast(action);
        } else if (action.contains("mobClick")) {
//            mobClick(action);
        } else if (action.contains("presentPage")) {
//            startActivityOverridePendingTransition(action);
        } else if (action.contains("dismissPage")) {
//            overActivityOverridePendingTransition();
        } else if (action.contains("inviteCust")) {
//            shareDataFriend(action);
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
//            shareToC(action);
        } else if (action.contains("riskTest")) {
//            Intent intent = new Intent(context, RiskResultToCActivity.class);
//            String[] split = action.split(":");
//            intent.putExtra("level", split[2]);
//            context.startActivity(intent);
//            context.finish();
            // ScreenManager.getScreenManager().popActivity(false);
        } else if (action.contains("tel:")) {
            NavigationUtils.startDialgTelephone(context, "4001888848");
        } else if (action.contains("openSharePage")) {
            openpage(action, false, false, true);
        } else if (action.contains("checkVersion")) {
//            if (MApplication.getAppUpdate() != null) {
//                String newVersion = MApplication.getAppUpdate().getVersion();
//                String oldVersion = String.valueOf(AppInfo.versionCode(context));
//                String language = "javascript:newVersion('" + oldVersion + "','" + (TextUtils.isEmpty(newVersion) ? "0" : newVersion) + "'," + values + ")";
//                getWeb().loadUrl(language);
//            } else {
//                String oldVersion = String.valueOf(AppInfo.versionCode(context));
//                String language = "javascript:newVersion('" + oldVersion + "',0," + values + ")";
//                getWeb().loadUrl(language);
//            }
        } else if (action.contains("updated")) {
//            VersonUpdate();
        } else if (action.contains("feedback")) {//意见反馈跳转
//            context.startActivity(new Intent(context, FeedbackActivity.class));
        } else if (action.contains("signEnt")) {
//            SignIn();
        } else if (action.contains("setGestruePassword")) { //设置手势密码
//            Intent intent = new Intent(context, GestureEditActivity.class);
//            context.startActivity(intent);
//            String valuse = "1".equals(MApplication.getUser().getToC().getGestureSwitch()) ? "2" : "1";
        } else if (action.contains("modifyGestruePassword")) { // 修改手势密码
//            Intent intent = new Intent(context, GestureVerifyActivity.class);
//            intent.putExtra(GestureEditActivity.PARAM_FROM_MODIFY, true);
//            context.startActivity(intent);
//            context.finish();
        } else if (action.contains("closeGestruePassword")) { // 关闭手势密码
//            Intent intent = new Intent(context, GestureVerifyActivity.class);
//            intent.putExtra(GestureVerifyActivity.PARAM_CLOSE_PASSWORD, true);
//            context.startActivity(intent);
        } else if (action.contains("openInformation")) {
//            gotoDiscoverDetail(action);
        } else if (action.contains("shareAchievement")) {
            cutScreenShare(action);
        } else if (action.contains("openCustomerChat")) {
            openCustomerChat(action);
        } else if (action.contains("LivePrompt")) { // 直播提醒
//            livePrompt(action);
//        } else if (action.contains("toVideoDetail")) { // 视频详情
//            //startVideoDetail(action);
        } else if (action.contains("toVideoLive")) { // 视频直播
//            startVideoLive(action);
        } else if (action.contains("onlineVisite")) { // 我的投顾上线通知
            isTouGuOnline(action);
        } else if (action.contains("callLiCaiShi")) { // 呼叫理财师
            callLiCaiShi(action);
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
//            Intent intent = new Intent(context, CaptureActivity.class);
//            intent.putExtra("isCallBack", "Y");
//            context.startActivity(intent);
        } else if (action.contains("sharePoster")) {
//            sharePoster();
        } else if (action.contains("ydPay")) {
//            Intent intent = new Intent(context, PayActivity.class);
//            context.startActivity(intent);
        }
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
//            string = CwebNetConfig. + string;
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
//
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
     * @return
     */
//    private BShare GetShareData(String action) {
//        //获取需要的参数 &&一坨从原分享代码拷贝过来 照搬就行**************************************
//        String[] split = action.split(":");
//        String title = null;
//        try {
//            title = URLDecoder.decode(split[2], "utf-8");
//            String content = URLDecoder.decode(split[3], "utf-8");// split[3];
//            String url = URLDecoder.decode(split[5], "utf-8");
//            ;//split[5];
//            url = Domain.baseWebsite + url + "&advisertob=" + (UserInfManger.IsAdviser(context) ? MApplication.getUserid() : "");
//            //获取需要的参数 ***********************************************************
//            //初始化bean &&构造函数按照原分享代码的参数照搬过来
//            BShare bShare = new BShare(title, content, R.drawable.logoshare, url);//(title, content, R.drawable.logoshare, url, url, title, content, url, null, "", productType, schemesId);
//            return bShare;
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

    private void showContactImDialog(String action) {
        String[] split = action.split(":");
        try {
            String targetId = URLDecoder.decode(split[2], "utf-8");
            String title = URLDecoder.decode(split[3], "utf-8");
            String content = URLDecoder.decode(split[4], "utf-8");
            String btnText = URLDecoder.decode(split[5], "utf-8");
            new MyDogDialog(context, title, content, "", btnText) {
                public void left() {
                    this.cancel();
                }

                public void right() {
                    RongIM.getInstance().startPrivateChat(context, "dd0cc61140504258ab474b8f0a38bb56", "平台客服");
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
            RongIM.getInstance().startConversation(context, Conversation.ConversationType.PRIVATE, targetId, name);
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

    private void callLiCaiShi(String action) {
        String[] split = action.split(":");
        try {
            final String telephone = URLDecoder.decode(split[2], "utf-8");
            String name = URLDecoder.decode(split[3], "utf-8");

            if (TextUtils.isEmpty(telephone)) {
                MToast.makeText(context, context.getResources().getString(R.string.no_phone_number), Toast.LENGTH_LONG).show();
                return;
            }
            OtheriOSDialog dialog = new OtheriOSDialog(context, "", "呼叫投资顾问".concat(name).concat("电话") + "\n" + telephone.concat(" ?"), "取消", "呼叫", false) {
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
        Log.i("touguxinxi", "isTouGuOnline");
        String[] split = action.split(":");
        try {
            String value = URLDecoder.decode(split[2], "utf-8");
            ((BaseApplication)BaseApplication.getContext()).setTouGuOnline("1".equals(value) ? true : false);
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

//    private void livePrompt(String action) {
//        String[] split = action.split(":");
//        try {
//            String title = URLDecoder.decode(split[2], "utf-8");
//            String address = URLDecoder.decode(split[3], "utf-8");
//            String startTime = URLDecoder.decode(split[4], "utf-8");
//            String content = URLDecoder.decode(split[5], "utf-8");
//            CalendarListBean.CalendarBean calendarListBean = new CalendarListBean.CalendarBean();
//            calendarListBean.setTitle(title);
//            calendarListBean.setAddress(address);
//            calendarListBean.setStartTime(startTime);
//            calendarListBean.setEndTime(String.valueOf(Long.parseLong(startTime) + 1000 * 60 * 30));
//            calendarListBean.setContent(content);
//            calendarListBean.setAlert("10");
//            String eventId = String.valueOf(CalendarManamger.insertSystemCalendar(context, calendarListBean));
//            String laun = "javascript:Tools.saveSuccess('" + (TextUtils.isEmpty(eventId) ? 0 : 1) + "','" + title + "');";
//            getWeb().loadUrl(laun);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    private void openCustomerChat(String action) {
        String[] split = action.split(":");
        try {
            String name = URLDecoder.decode(split[3], "utf-8");
            String userId = URLDecoder.decode(split[2], "utf-8");
            RongIM.getInstance().startConversation(context, Conversation.ConversationType.PRIVATE, userId, name);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void cutScreenShare(String action) {
//        CutScreenShareDialog shareDialog = new CutScreenShareDialog(context);
//        shareDialog.setTargetView(getWeb());
//        shareDialog.show();
    }

//    private void gotoDiscoverDetail(String action) {
//        String[] split = action.split(":");
//        try {
//            String infoId = URLDecoder.decode(split[2], "utf-8");
//            String category = URLDecoder.decode(split[3], "utf-8");
//            String title = URLDecoder.decode(split[4], "utf-8");
//            String summary = URLDecoder.decode(split[5], "utf-8");
//            String likes = URLDecoder.decode(split[6], "utf-8");
//            String isLike = URLDecoder.decode(split[7], "utf-8");
//            NewsBean newsBean = new NewsBean();
//            newsBean.setInfoId(infoId);
//            newsBean.setCategory(category);
//            newsBean.setTitle(title);
//            newsBean.setSummary(summary);
//            newsBean.setLikes(Integer.valueOf(likes));
//            newsBean.setIsLike(isLike);
//
//            Intent i = new Intent(context, FoundNewsDetailActivity.class);
//            Bundle bundle = new Bundle();
//            bundle.putSerializable(FoundNewsDetailActivity.NEW_PARAM_NAME, newsBean);
//            i.putExtras(bundle);
//            context.startActivity(i);
//            String[] param = new String[]{newsBean.getCategory(), MApplication.getUser().getToB().isColorCloud(), MApplication.getUser().getToB().getOrganizationName(), newsBean.getTitle()};
//            if (Utils.isVisteRole(context)) {
//                new RundouTaskManager(context).executeRundouTask("查看资讯");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

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

//    private void shareDataFriend(String action) {
//        // "inviteCust","2",'/apptie/invite_preview_wx.html?site=2'); 1:普通邀请,可分享微信好友和朋友圈、2:精准邀请,只能分享到微信好友
//        String[] split = action.split(":");
//        try {
//            String title = URLDecoder.decode(split[5], "utf-8");
//            String type = URLDecoder.decode(split[2], "utf-8");
//            String content = URLDecoder.decode(split[4], "utf-8");
//            String link = URLDecoder.decode(split[3], "utf-8");
//            link = link.startsWith("/") ? Domain.baseWebsite + link : Domain.baseWebsite + "/" + link;
//            if ("2".equals(type)) {
//                // openWeixin(title, content, link, R.drawable.logoshare);
//                UMWXHandler wxHandler = new UMWXHandler(context,
//                        Contant.weixin_appID, Contant.weixin_appSecret);
//                wxHandler.addToSocialSDK();
//                ShareYaoqinDialog shareDialog = new ShareYaoqinDialog(context, 0, "");
//                shareDialog.shareWeixin(title, content, R.drawable.logoshare, link);
//            } else {
//                ShareYaoqinDialog shareDialog = new ShareYaoqinDialog(context, 0, "");
//                shareDialog.setData(title, content, R.drawable.logoshare, link, content, content, content, link);
//                shareDialog.show();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void openWeixin(final String title, final String content, final String url, final int image) {
//        WeiXinShare sh = new WeiXinShare(context, "");
//        sh.shareWeixinWithID(title, content, url, image);
//    }

//    private void shareToC(String action) {
//        // sendCommand(’tocShare’,'proName','子标题',,'tocShareProductImg','/apptie/detail.html?schemeId='123456789'');
//        String actionDecode = URLDecoder.decode(action);
//        String[] split = actionDecode.split(":");
//        String sharePYQtitle = "";
//        try {
//            String title = split[2];
//            String subTitle = split[3];
//            String imageTitle = split[4];
//            String link = split[5];
//            if (split.length >= 7) {
//                sharePYQtitle = split[6];
//            }
//            link = link.startsWith("/") ? Domain.baseWebsite + link : Domain.baseWebsite + "/" + link;
//            ShareYaoqinDialog shareDialog;
//            if (shareWithEmail) {
//                shareDialog = new ShareYaoqinDialog(context, 1, "");
//            } else {
//                shareDialog = new ShareYaoqinDialog(context, 0, "");
//            }
//            String shareType = link.contains("apptie/detail.html") ? "chanpin" : link.contains("discover/details.html") ? "zixun" : "";
////            https://app.simuyun.com/app5.0/apptie/detail.html?schemeId=04c9dff066ab41499e2a189052ca6d94&type=1&share=1
////            https://app.simuyun.com/app5.0/discover/details.html?id=cf2e0d629cf143c0b3e5a6f0b2415ded&category=4&share=1
//            if (imageTitle.contains("greeteng")) {
//                shareDialog.setData(title, subTitle, sharePYQtitle, 1, link, title, title, title, link, shareType);
//            } else {
//                shareDialog.setData(title, subTitle, sharePYQtitle, R.drawable.logoshare, link, title, title, title, link, shareType);
//            }
//            shareDialog.show();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    private void backPage(String action) {
        String actionDecode = URLDecoder.decode(action);
        String[] split = actionDecode.split(":");
        int index = Integer.valueOf(split[2]) < 0 ? 0 : Integer.valueOf(split[2]);
        Intent intent = new Intent();
        intent.putExtra(PushMsgActivity.BACK_PARAM, index);
        context.setResult(PushMsgActivity.BACK_RESULT_CODE, intent);
        context.finish();
    }

//    private void mobClick(String action) {
//        if (MApplication.getUser().getToC() == null) {
//            return;
//        }
//        String decode = URLDecoder.decode(action);
//        String[] split = decode.split(":");
//        String args = split[4] + ",$";
//        String[] split1 = args.split(",");
//        if (split[3].startsWith("1")) {
//            split1[1] = MApplication.getUser().getToB().isColorCloud();
//            HashMap<String, String> params = DataStatisticsUtils.getParams(split[2], split[3], split1);
//            DataStatisticsUtils.push(context, params);
//        } else {
//            split1[1] = MApplication.getUser().getToC().getBindTeacher();
//            HashMap<String, String> params = DataStatisticsUtils.getParams(split[2], split[3], split1);
//            DataStatisticsUtils.push(context, params);
//        }
//    }

//    private void startActivityOverridePendingTransition(String action) {
//        String[] split = action.split(":");
//        String string = split[2];
//        String title = split[3];
//        string = Domain.baseWebsite + string;
//        Intent i = new Intent(context, PushMsgActivity.class);
//        // i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        try {
//            i.putExtra(Contant.push_message_url, URLDecoder.decode(string, "utf-8"));
//            i.putExtra(Contant.push_message_title, URLDecoder.decode(title, "utf-8"));
//            if (split.length > 5) {
//                i.putExtra(Contant.PAGE_SHOW_TITLE, Boolean.valueOf(split[5]));
//            }
//            ((Activity) context).startActivityForResult(i, 300);
//            ((Activity) context).overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

//    private void overActivityOverridePendingTransition() {
//        ((Activity) context).overridePendingTransition(R.anim.slide_out, R.anim.slide_in);
//        context.finish();
//        //ScreenManager.getScreenManager().popActivity(false);
//    }
//
//    private void toQrCode() {
//        Intent intent3 = new Intent(context, CaptureActivity.class);
//        context.startActivity(intent3);
//    }
//
//    private void showComfirmDialog(String action) {
//        String actionDecode = URLDecoder.decode(action);
//        String[] split = actionDecode.split(":");
//        if (split.length > 4) {
//            iOSDialog dialog = new iOSDialog(context, split[2], split[3], split[4], split[5], true) {
//                @Override
//                public void left() {
//                    dismiss();
//                    getWeb().loadUrl("javascript:Tools.clickedIndex('0')");
//                }
//
//                @Override
//                public void right() {
//                    dismiss();
//                    getWeb().loadUrl("javascript:Tools.clickedIndex('1')");
//                }
//            };
//            dialog.show();
//        }
//    }

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

//    private void VersonUpdate() {
//        if (MApplication.getAppUpdate() != null && !TextUtils.isEmpty(MApplication.getAppUpdate().getVersion())
//                && !MApplication.getAppUpdate().getVersion().equals(new AppInfo().versionName(context))) {
//            new AppUpdateDialog(context).show();
//        }
//    }

//    private void startImagePage(String action) {
//        Intent ii = new Intent(context, MultiImageSelectorActivity.class);
//        // 是否显示拍摄图片
//        ii.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
//        ii.putExtra("clear", 1);
//        // 最大可选择图片数量
//        // ii.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 5);
//        // 选择模式
//        ii.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_SINGLE);
//        ii.putExtra("camerasensortype", 2);
//        // 默认选择
//        // if (mSelectPath != null && mSelectPath.size() > 0) {
//        // ii.putExtra(
//        // MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST,
//        // mSelectPath);
//        // }
//        context.startActivityForResult(ii, Contant.REQUEST_IMAGE);
//    }

//    private void recommentFriend() {
//        /**
//         * 推荐私募云
//         */
//        ShareDialog shareDialog = new ShareDialog(context);
//        shareDialog.setData("推荐理财师好友安装私募云，一起来聚合财富管理力量！", "http://www.simuyun.com/invite/invite.html", R.drawable.logoshare,
//                "http://www.simuyun.com/invite/invite.html", "推荐理财师好友安装私募云，一起来聚合财富管理力量！http://www.simuyun.com/invite/invite.html",
//                "推荐理财师好友安装私募云，一起来聚合财富管理力量！", "推荐理财师好友安装私募云，一起来聚合财富管理力量！http://www.simuyun.com/invite/invite.html",
//                "http://www.simuyun.com/invite/invite.html");
//        shareDialog.show();
//    }
//
//    private void logOutUser() {
//        ReturnLogin returnLogin = new ReturnLogin();
//        returnLogin.tokenExit(context);
//    }
//
//    private void checkRengou(String action) {
//        String decode = URLDecoder.decode(action);
//        String[] split = decode.split(":");
//        String canTakeUp = split[2];
//        TakeUp takeUp = new TakeUp(canTakeUp);
//        EventBus.getDefault().post(takeUp);
//    }

//    private void toMessageList() {
//        Intent in3 = new Intent(context, MessageListActivity.class);
//        in3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(in3);
//    }

    private void showToast(String action) {
        String actionDecode = URLDecoder.decode(action);
        String[] split = actionDecode.split(":");
        String content = split[2];
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
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
        new IOSDialog(context, "", content, "取消", "兑换") {
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

//    private void openMallPage(String action) {
//        String actionDecode = URLDecoder.decode(action);
//        String[] split = actionDecode.split(":");
//        String s = split[1];
//        String name = split[2];
//        String url = split[3];
//        Intent intent = new Intent(context, MallActivity.class);
//        intent.putExtra("url", url);
//        intent.putExtra("title", name);
//        context.startActivity(intent);
//    }

//    public void secretviewpdf(String action) {
//        String[] split = action.split(":");
//        String string = split[2];
//        Intent i = new Intent(context, PDFActivity.class);
//        try {
//            i.putExtra(Contant.pdf_url, URLDecoder.decode(string, "utf-8"));
//            i.putExtra("pdfName", URLDecoder.decode(split[3], "utf-8"));
//            i.putExtra("isSecret", 1);
//            context.startActivity(i);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

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
//    private void playVideo(String action) {
//        String[] vas = action.split(":");
//        String videoId = action.substring(action.lastIndexOf("Video:") + 6);
//        videoId = TextUtils.isEmpty(videoId) ? vas[2] : videoId;
//        ToolsUtils.toPlayVideoActivity(context, videoId);
//        if (Utils.isVisteRole(context)) {
//            DataStatistApiParam.onClickVideoToC("", "");
//        } else {
//            DataStatistApiParam.onClickVideoToB("", "");
//        }
//    }

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
        String content = clipboardManager.getText().toString();
        webview.loadUrl("javascript:setPasteServeCode('" + content + "')");
    }

    /**
     * 修改密码
     *
     * @param action
     */
//    private void changepassword(String action) {
//        String[] split = action.split(":");
//        String password = split[2];
//        final String newPassword = URLDecoder.decode(split[3]);
//        JSONObject j = new JSONObject();
//        try {
//            j.put("userName", MApplication.getUser().getUserName());
//            j.put("oldPassword", MD5.getShortMD5(password));
//            j.put("newPassword", MD5.getShortMD5(newPassword));
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        final String failure = "修改失败";
//        new ChangePasswordTask(context).start(j.toString(), new HttpResponseListener() {
//
//            @Override
//            public void onResponse(JSONObject response) {
//                Intent i = new Intent();
//                try {
//                    String string = response.getString("result");
//                    boolean contains = string.contains("success");
//                    if (contains) {
//                        string = "";
//                        SPSave.getInstance(context).putString(Contant.password, newPassword);
////                         web.loadUrl("javascript:setData('null')");
//                    } else {
//                        // web.loadUrl("javascript:setData('" + string + "')");
//                    }
//
//                    i.putExtra(Contant.msg, string);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    // web.loadUrl("javascript:setData('" + failure +
//                    // "')");
//                    i.putExtra(Contant.msg, failure);
//                }
//
//                ((Activity) context).setResult(0, i);
//                ((Activity) context).finish();
//            }
//
//            @Override
//            public void onErrorResponse(String error, int statueCode) {
//                web.loadUrl("javascript:setData('" + failure + "')");
//                Intent i = new Intent();
//                i.putExtra(Contant.msg, failure);
//                ((Activity) context).setResult(0, i);
//                ((Activity) context).finish();
//            }
//        });
//    }

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
        // ScreenManager.getScreenManager().popActivity(false);
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
            Intent i = new Intent(context, PushMsgActivity.class);
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
        Set<String> set = WebViewConstant.NewFoundHashMap.keySet();
        if (set.contains(catagory)) {
            return WebViewConstant.NewFoundHashMap.get(catagory);
        }
        return "资讯信息";
    }

    /**
     * 打开新页面
     *
     * @param data url路径
     *             rightSave 右上角是否有保存按钮
     *             initPage 页面是否初始化
     */
    private void openpage(String data, boolean rightSave, boolean initPage, boolean rightShare) {
        String[] split = data.split(":");
        String string = split[2];
        String title = split[3];
        String decodeTitle = "";
        String decodeUrl = "";
        if (!string.contains("http")) {
            string = BaseWebNetConfig.baseParentUrl + string;
        }
        Intent i = new Intent(context, PushMsgActivity.class);
        // i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            decodeTitle = URLDecoder.decode(title, "utf-8");
            decodeUrl = URLDecoder.decode(string, "utf-8");

            i.putExtra(WebViewConstant.push_message_url, URLDecoder.decode(string, "utf-8"));
            i.putExtra(WebViewConstant.push_message_title, URLDecoder.decode(title, "utf-8"));
            if (initPage) {
                i.putExtra(WebViewConstant.push_message_value, URLDecoder.decode(split[4], "utf-8"));
            }
            i.putExtra(WebViewConstant.RIGHT_SAVE, rightSave);
            i.putExtra(WebViewConstant.RIGHT_SHARE, rightShare);
            i.putExtra(WebViewConstant.PAGE_INIT, initPage);
            if (split.length >= 5) {
                i.putExtra(WebViewConstant.PAGE_SHOW_TITLE, Boolean.valueOf(split[split.length - 1]));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ((Activity) context).startActivityForResult(i, 300);
        if ("产品详情".equals(decodeTitle) && SPreference.isVisitorRole(context)) {
//            new RundouTaskManager(context).executeRundouTask("查看产品");
        } else if (!TextUtils.isEmpty(decodeUrl) && decodeUrl.contains("discover/details.html")) {
//            new RundouTaskManager(context).executeRundouTask("查看资讯");
        }
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

//    private void toRiskRusult(String action) {
//        try {
//            action = action + ":1";
//            String[] split = action.split(":");
//            Intent intent = new Intent(context, RiskResultActivity.class);
//            intent.putExtra("result", URLDecoder.decode(split[2], "utf-8"));
//            intent.putExtra("riskEvaluationName", URLDecoder.decode(split[3], "utf-8"));
//            intent.putExtra("riskEvaluationIdnum", URLDecoder.decode(split[4], "utf-8"));
//            intent.putExtra("riskEvaluationPhone", URLDecoder.decode(split[5], "utf-8"));
//
//            context.startActivity(intent);
//        } catch (Exception e) {
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
//    /**
//     * 跳转到我的任务页面
//     */
//    private void goMyTask() {
//        HashMap<String, String> parms = new HashMap<>();
//        parms.put("fromc", "1");
//        UiHelper.toNextActivity(context, DayTaskActivity.class, parms);
//
//    }
}