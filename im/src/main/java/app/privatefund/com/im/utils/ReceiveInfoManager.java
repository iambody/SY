package app.privatefund.com.im.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import com.cgbsoft.lib.BaseApplication;
import com.cgbsoft.lib.InvestorAppli;
import com.cgbsoft.lib.base.model.bean.MemberDegrade;
import com.cgbsoft.lib.base.model.bean.MemeberInfo;
import com.cgbsoft.lib.base.webview.BaseWebViewActivity;
import com.cgbsoft.lib.base.webview.CwebNetConfig;
import com.cgbsoft.lib.base.webview.WebViewConstant;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.utils.constant.Constant;
import com.cgbsoft.lib.utils.tools.BackgroundManager;
import com.cgbsoft.lib.utils.tools.NavigationUtils;
import com.cgbsoft.lib.widget.MemberDegradeDialog;
import com.cgbsoft.lib.widget.MemberUpdateDialog;
import com.cgbsoft.lib.widget.PushDialog;
import com.cgbsoft.lib.widget.dialog.BaseDialog;
import com.cgbsoft.lib.widget.dialog.DefaultDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import app.privatefund.com.im.bean.SMMessage;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

/**
 * @author chenlong
 *         <p>
 *         消息接受管理
 */
public class ReceiveInfoManager {
    private static ReceiveInfoManager receiveInfoManager;
    private BaseDialog infoDialog;

    private ReceiveInfoManager() {
    }

    public synchronized static ReceiveInfoManager getInstance() {
        if (receiveInfoManager == null) {
            receiveInfoManager = new ReceiveInfoManager();
        }
        return receiveInfoManager;
    }

    private Handler mainHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            Activity mCurrentActivityContext = ((BaseApplication) BaseApplication.getContext()).getBackgroundManager().getCurrentActivity();
            if (("LoginActivity".equals(mCurrentActivityContext.getClass().getSimpleName()) ||
                    "LoadCustomerActivity".equals(mCurrentActivityContext.getClass().getSimpleName()) ||
                    "SplashMovieActivity".equals(mCurrentActivityContext.getClass().getSimpleName()) ||
                    "WelcomeActivity".equals(mCurrentActivityContext.getClass().getSimpleName())) && mainHandler != null) {
                Message message = Message.obtain();
                message.setData(msg.getData());
                message.what = msg.what;
                mainHandler.sendMessageDelayed(message, 1000 * 30);
                return;
            }

            RongIM.getInstance().clearMessages(Conversation.ConversationType.PRIVATE, Constant.msgNoKnowInformation);
            RongIM.getInstance().removeConversation(Conversation.ConversationType.PRIVATE, Constant.msgNoKnowInformation);

            // 进行相应操作
            try {
                Bundle bundle = msg.getData();
                BackgroundManager backgroundManager = ((BaseApplication) BaseApplication.getContext()).getBackgroundManager();
                Activity currentActivity = backgroundManager.getCurrentActivity();
                switch (msg.what) {
                    case Constant.RECEIVER_SEND_CODE:
                        final String title = bundle.getString("title");
                        final String type = bundle.getString("type");
                        final String detail = bundle.getString("detail");
                        final String jumpUrl = bundle.getString("jumpUrl");
                        final String SenderId = bundle.getString("senderId");
                        final String shareType = bundle.getString("shareType");
                        if (detail == null || detail.equals("") || "".equals(title)) {
                            return;
                        }
                        if (infoDialog != null && infoDialog.isShowing() && !currentActivity.isFinishing()) {
                            infoDialog.dismiss();
                            infoDialog = null;
                        }
                        String rightText = type.equals("1") ? "查看" : "知道了";
                        if (!isNewTitle(title)) {
                            infoDialog = new DefaultDialog(currentActivity, title, detail, "返回", rightText) {
                                @Override
                                public void left() {
                                    this.dismiss();
                                }

                                @Override
                                public void right() {
                                    this.dismiss();
                                    if ("1".equals(type)) {
                                        onClickConfirm(jumpUrl, title, shareType);
                                    }
                                }
                            };
                        } else {
                            infoDialog = new PushDialog(currentActivity, title, detail, rightText, "返回", jumpUrl) {
                                @Override
                                public void left() {
                                    dismiss();
                                }

                                @Override
                                public void right() {
                                    dismiss();
                                    if ("1".equals(type)) {
                                        onClickConfirm(jumpUrl, title, shareType);
                                    }
                                }
                            };
                        }
                        if (!infoDialog.isShowing() && !currentActivity.isFinishing()) {
                            infoDialog.show();
                        }
                        break;
                    case Constant.RECEIVER_SEND_CODE_NEW_INFO:
                        final SMMessage smMessage = (SMMessage) bundle.getSerializable("smMessage");
                        Activity mCurrentActivity = ((BaseApplication) BaseApplication.getContext()).getBackgroundManager().getCurrentActivity();
                        if (mCurrentActivity.getClass().getSimpleName().equals("MainPageActivity")) {
                            if (infoDialog != null && infoDialog.isShowing() && !currentActivity.isFinishing()) {
                                infoDialog.dismiss();
                                infoDialog = null;
                            }
                            if (!isNewTitle(smMessage.getButtonTitle())) {
                                infoDialog = new DefaultDialog(mCurrentActivity, smMessage.getButtonTitle(), smMessage.getContent(), "返回", smMessage.getButtonText()) {
                                    @Override
                                    public void left() {
                                        this.dismiss();
                                    }

                                    @Override
                                    public void right() {
                                        this.dismiss();
                                        onClickConfirm(smMessage.getJumpUrl(), smMessage.getButtonTitle(), smMessage.getShareType());
                                    }
                                };
                            } else {
                                infoDialog = new PushDialog(mCurrentActivity, smMessage.getButtonTitle(), smMessage.getContent(), smMessage.getButtonText(), "返回", smMessage.getJumpUrl()) {
                                    @Override
                                    public void left() {
                                        dismiss();
                                    }

                                    @Override
                                    public void right() {
                                        dismiss();
                                        onClickConfirm(smMessage.getJumpUrl(), smMessage.getButtonTitle(), smMessage.getShareType());
                                    }
                                };
                            }
                            if (!infoDialog.isShowing() && !currentActivity.isFinishing()) {
                                infoDialog.show();
                            }
                        } else {
                            Gson gson = new Gson();
                            String saveValue = PushPreference.getPushInfo(InvestorAppli.getContext());
                            List<SMMessage> messageList = new ArrayList<>();
                            if (!TextUtils.isEmpty(saveValue)) {
                                messageList = gson.fromJson(saveValue, new TypeToken<List<SMMessage>>() {
                                }.getType());
                            }
                            messageList.add(smMessage);
                            String values = gson.toJson(messageList, new TypeToken<List<SMMessage>>() {
                            }.getType());
                            PushPreference.savePushInfo(InvestorAppli.getContext(), values);
                        }
                        break;
                    case Constant.RECEIVER_SEND_CODE_MEMBER_UPDATE:
                        final SMMessage myMessage = (SMMessage) bundle.getSerializable("smMessage");
                        Activity currentAct = ((BaseApplication) BaseApplication.getContext()).getBackgroundManager().getCurrentActivity();
                        MemberUpdateDialog memberUpdateDialog = new MemberUpdateDialog(currentAct) {
                            @Override
                            public void buttonClick() {
                                String url = CwebNetConfig.memeberArea;
                                Intent intent = new Intent(getContext(), BaseWebViewActivity.class);
                                intent.putExtra(WebViewConstant.push_message_url, url);
                                intent.putExtra(WebViewConstant.push_message_title, "会员权益");
                                intent.putExtra(WebViewConstant.RIGHT_MEMBER_RULE_HAS, true);
                                currentAct.startActivity(intent);
                                dismiss();
                            }
                        };
                        memberUpdateDialog.show();
                        String vuls = myMessage.getDialogSummary();
                        System.out.println("=--------valis=="+ vuls);
                        MemeberInfo memeberInfo = new Gson().fromJson(vuls, new TypeToken<MemeberInfo>() {}.getType());
                        memberUpdateDialog.updateDialogUi(memeberInfo);
                        break;
                    case Constant.RECEIVER_SEND_CODE_MEMBER_DEGRADE:
                        final SMMessage youMessage1 = (SMMessage) bundle.getSerializable("smMessage");
                        Activity currentAct1 = ((BaseApplication) BaseApplication.getContext()).getBackgroundManager().getCurrentActivity();
                        MemberDegradeDialog memberDegardeDialog = new MemberDegradeDialog(currentAct1) {
                            @Override
                            public void buttonClick() {
                                String url = CwebNetConfig.memeberArea;
                                Intent intent = new Intent(getContext(), BaseWebViewActivity.class);
                                intent.putExtra(WebViewConstant.push_message_url, url);
                                intent.putExtra(WebViewConstant.push_message_title, "会员权益");
                                intent.putExtra(WebViewConstant.RIGHT_MEMBER_RULE_HAS, true);
                                currentAct1.startActivity(intent);
                                dismiss();
                            }
                        };
                        memberDegardeDialog.show();
                        String vuls1 = youMessage1.getDialogSummary();
                        System.out.println("=--------valis=="+ vuls1);
                        MemberDegrade memberDegrade = new Gson().fromJson(vuls1, new TypeToken<MemberDegrade>() {}.getType());
                        memberDegardeDialog.updateDialogUi(memberDegrade);
                        break;
                    default:
                        break;
                }
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
    };

    private boolean isNewTitle(String title) {
        return  TextUtils.equals("早知道", title) ||
                TextUtils.equals("头条号", title) ||
                TextUtils.equals("大视野", title) ||
                TextUtils.equals("名家谈", title) ||
                TextUtils.equals("云观察", title);
    }

    private void onClickConfirm(String jumpUrl, String title, String shareType) {
        if (jumpUrl.contains("Android")) {
            try {
//                EventBus.getDefault().post(new RefreshUserinfo());
                JSONObject js = new JSONObject(jumpUrl);
                String android = js.getString("Android");
                if (!TextUtils.isEmpty(android) && android.contains("?")) {
                    final String jumpActivityName = android.substring(0, android.indexOf("?"));
                    final String id = android.substring(android.indexOf("?") + 1);
                    JSONObject j = new JSONObject();
                    if (jumpActivityName.equals("ProductDetailActivity")) {
                        j.put("schemeId", id);
//                        new ProductDetailTask(context).start(j.toString(), new HttpResponseListener() {
//                            @Override
//                            public void onResponse(JSONObject response) {
//                                Gson g = new Gson();
//                                ProductBean productBean = g.fromJson(response.toString(), ProductBean.class);
//                                Intent in = null;
//                                try {
//                                    in = new Intent(context, Class.forName("com.cgbsoft.privatefund.activity." + jumpActivityName));
//                                    if (id != null) {
//                                        in.putExtra(Contant.product, productBean);
//                                    }
//                                    context.startActivity(in);
//                                } catch (ClassNotFoundException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                            @Override
//                            public void onErrorResponse(String error, int statueCode) {}
//                        });
                    } else if (jumpActivityName.equals("PlayVideoActivity")) {
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("videoId",id);
                        NavigationUtils.startActivityByRouter(InvestorAppli.getContext(), RouteConfig.GOTOVIDEOPLAY, hashMap);
                    } else {
//                    try {
//                        Intent in = new Intent(context, Class.forName("com.cgbsoft.privatefund.activity." + android));
//                        context.startActivity(in);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//
//                    }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put(WebViewConstant.push_message_url, jumpUrl);
            hashMap.put(WebViewConstant.push_message_title, formatCodeToName(InvestorAppli.getContext(), title));
            if ("0".equals(shareType) || TextUtils.isEmpty(shareType)) { // 资讯详情添加分享按钮
                hashMap.put(WebViewConstant.RIGHT_SHARE, false);
                NavigationUtils.startActivityByRouter(InvestorAppli.getContext(), RouteConfig.GOTO_BASE_WEBVIEW, hashMap);
            } else {
                hashMap.put(WebViewConstant.RIGHT_SHARE, true);
                NavigationUtils.startActivityByRouter(InvestorAppli.getContext(), RouteConfig.GOTO_BASE_WITHSHARE_WEBVIEW, hashMap);
            }
        }
        RongIMClient.getInstance().clearMessages(Conversation.ConversationType.PRIVATE, "INTIME49999");
        RongIMClient.getInstance().removeConversation(Conversation.ConversationType.PRIVATE, "INTIME49999");
    }

    private static String formatCodeToName(Context context, String title) {
        Set<String> set = Constant.hashMap.keySet();
        for (String name : set) {
            if (name.equals(title)) {
                return context.getResources().getString(Constant.hashMap.get(name));
            }
        }
        return title;
    }

    public Handler getHandler() {
        return mainHandler;
    }

}
