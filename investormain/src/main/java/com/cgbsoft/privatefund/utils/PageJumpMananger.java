package com.cgbsoft.privatefund.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.cgbsoft.lib.base.model.VideoInfoEntity;
import com.cgbsoft.lib.base.model.bean.ProductlsBean;
import com.cgbsoft.lib.base.webview.BaseWebViewActivity;
import com.cgbsoft.lib.base.webview.WebViewConstant;
import com.cgbsoft.lib.utils.constant.Constant;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.utils.tools.LogUtils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;

import app.privatefund.com.im.bean.SMMessage;
import app.privatefund.com.im.utils.ReceiveInfoManager;
import app.privatefund.com.vido.VideoNavigationUtils;
import app.product.com.utils.ProductNavigationUtils;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;
import io.rong.push.RongPushClient;
import io.rong.push.notification.PushNotificationMessage;

/**
 * @chenlong on 16/11/2
 * 通知页面跳转
 */
public class PageJumpMananger {

    public static void jumpPageFromToMainActivity(Context context, PushNotificationMessage pushMessage) {
        defineJumpPageByMessage(context, pushMessage);
    }

    /**
     * {"content":"消息内容","extra":{"jumpUrl":"{\"Android\":\"OrdersActivity\",   \"iOS\":\"YTOrderViewController\"}",
     * "dialogTitle":"消息标题","showType":"1","receiverType":"2","dialogSummary":"弹窗详情"},"noticeTitle":"推送标题"}
     *
     * @param context
     * @param pushMessage
     */
    public static void defineJumpPageByMessage(final Context context, PushNotificationMessage pushMessage) {
        String pushContent = pushMessage.getPushData();
        Log.i("PageJumpManager", "pushcontent=" + pushContent + ",---pushData=" + pushMessage.getPushData() + "--------pushTitle=" + pushMessage.getPushTitle());
        try {
            if (!TextUtils.isEmpty(pushContent)) {
                JSONObject pushText = new JSONObject(pushContent);
                String msg = pushText.get("extra").toString();
                String content = pushText.getString("content");
                Gson g = new Gson();
                SMMessage smMessage = g.fromJson(msg, SMMessage.class);
                smMessage.setContent(content);
                String title = smMessage.getDialogTitle();
                String jumpUrl = smMessage.getJumpUrl();
                String needShare = smMessage.getShareType();
                String showType = smMessage.getShowType();
                if (pushMessage.getConversationType() == RongPushClient.ConversationType.GROUP) {
                    RongIM.getInstance().startConversation(context, Conversation.ConversationType.GROUP, pushMessage.getTargetId(), title);
                } else {
                    jumpToPage(context, jumpUrl, title, needShare, smMessage);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void jumpToPage(final Context context, String jumpUrl, String title, String needShare, SMMessage smMessage) throws Exception {
        if ("4".equals(smMessage.getShowType())) {
            Message sendMessage = Message.obtain();
            Bundle bundle = sendMessage.getData();
            sendMessage.what = Constant.RECEIVER_SEND_CODE_NEW_INFO;
            bundle.putSerializable("smMessage", smMessage);
            sendMessage.setData(bundle);
            ReceiveInfoManager.getInstance().getHandler().sendMessage(sendMessage);
            return;
        }

        if (!TextUtils.isEmpty(jumpUrl) && jumpUrl.contains("Android")) {
            JSONObject js = new JSONObject(jumpUrl);
            String android = js.getString("Android");
            if (android.contains("?")) {
                final String jumpActivityName = android.substring(0, android.indexOf("?"));
                final String id = android.substring(android.indexOf("?") + 1);
                JSONObject j = new JSONObject();
                if (jumpActivityName.equals("ProductDetailActivity")) {
//                    j.put("schemeId", id);
                    requestProductDetail(context, id);
                } else if (jumpActivityName.equals("PlayVideoActivity")) {
                    requestVieoDetail(context, id);
                }
            } else {
//                try {
//                    Intent in = new Intent(context, Class.forName("com.cgbsoft.privatefund.activity." + android));
//                    context.startActivity(in);
//                } catch (Exception e) {
//                    e.printStackTrace();
//
//                }
            }
        } else {
            Intent i = new Intent(context, BaseWebViewActivity.class);
            i.putExtra(WebViewConstant.push_message_url, jumpUrl);
            i.putExtra(WebViewConstant.push_message_title, formatCodeToName(context, title));
            i.putExtra(WebViewConstant.PUSH_MESSAGE_COME_HERE, true);
            if ("0".equals(needShare) || TextUtils.isEmpty(needShare)) { // 资讯详情添加分享按钮
                i.putExtra(WebViewConstant.RIGHT_SHARE, false);
            } else {
                i.putExtra(WebViewConstant.RIGHT_SHARE, true);
            }
            i.putExtra(WebViewConstant.RIGHT_SAVE, false);
            context.startActivity(i);
        }
    }

    private static String formatCodeToName(Context context, String title) {
        Set<String> set = Constant.NewFoundHashMap.keySet();
        for (String name : set) {
            if (name.equals(title)) {
                return Constant.NewFoundHashMap.get(name);
            }
        }
        return title;
    }

    private static void requestProductDetail(Context context, String productSchemeId) {
        ApiClient.getProductDetail(productSchemeId).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                JSONObject obj = null;
                try {
                    obj = new JSONObject(s);
                    String vas = obj.getString("result");
                    if (!TextUtils.isEmpty(vas)) {
                        ProductlsBean productlsBean = new Gson().fromJson(vas, ProductlsBean.class);
                        ProductNavigationUtils.startProductDetailActivity(context, productlsBean.schemeId, productlsBean.productName, 100);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void onRxError(Throwable error) {
                LogUtils.Log("s", error.toString());
            }
        });
    }

    private static void requestVieoDetail(Context context, String videoDetailId) {
        ApiClient.getTestVideoInfo(videoDetailId).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                JSONObject obj = null;
                try {
                    obj = new JSONObject(s);
                    String result = obj.getString("result");
                    if (!TextUtils.isEmpty(result)) {
                        VideoInfoEntity.Result videoInfoResult = new Gson().fromJson(result, VideoInfoEntity.Result.class);
                        VideoNavigationUtils.stareVideoDetail((Activity) context, videoInfoResult.videoId, videoInfoResult.rows.coverImageUrl);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void onRxError(Throwable error) {

            }
        });
    }
}
