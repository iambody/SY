package app.privatefund.com.im.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import com.cgbsoft.lib.BaseApplication;
import com.cgbsoft.lib.InvestorAppli;
import com.cgbsoft.lib.base.webview.WebViewConstant;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.utils.constant.Constant;
import com.cgbsoft.lib.utils.tools.BackgroundManager;
import com.cgbsoft.lib.utils.tools.NavigationUtils;
import com.cgbsoft.lib.widget.PushDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import app.privatefund.com.im.bean.SMMessage;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

/**
 * @author chenlong
 *         <p>
 *         消息接受管理
 */
public class ReceiveInfoManager {
    private static ReceiveInfoManager receiveInfoManager;
    private PushDialog infoDialog;

    private ReceiveInfoManager() {}

    public synchronized static ReceiveInfoManager getInstance() {
        if (receiveInfoManager == null) {
            receiveInfoManager = new ReceiveInfoManager();
        }
        return receiveInfoManager;
    }

    private Handler mainHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            Activity mCurrentActivityContext = ((BaseApplication)BaseApplication.getContext()).getBackgroundManager().getCurrentActivity();
            if (("GestureVerifyActivity".equals(mCurrentActivityContext.getClass().getSimpleName()) ||
                    "GestureEditActivity".equals(mCurrentActivityContext.getClass().getSimpleName()) && mainHandler != null )) {
                Message message = Message.obtain();
                message.setData(msg.getData());
                message.what = msg.what;
                mainHandler.sendMessageDelayed(message, 1000 * 30);
                return;
            }
            // 进行相应操作
            try {
                Bundle bundle = msg.getData();
                switch (msg.what) {
                    case Constant.RECEIVER_SEND_CODE:
                        String title = bundle.getString("title");
                        String type = bundle.getString("type");
                        String detail = bundle.getString("detail");
                        String jumpUrl = bundle.getString("jumpUrl");
                        String shareType = bundle.getString("shareType");
                        if ("LoginActivity".equals(mCurrentActivityContext.getClass().getSimpleName()) || "WelcomeActivity".equals(mCurrentActivityContext.getClass().getSimpleName())) {
                            SharedPreferences sharedPreferences = PushPreference.getBase(InvestorAppli.getContext());
                            SharedPreferences.Editor edit = sharedPreferences.edit();
                            int unreadTotal = sharedPreferences.getInt("unreadTotal", 0);
                            edit.putInt("unreadTotal", (unreadTotal + 1));//未读总消息数
                            unreadTotal++;
                            edit.putString("unreadItem" + unreadTotal, bundle.toString());  //未读消息
                            edit.putString("type" + unreadTotal, type);
                            edit.putString("jumpUrl" + unreadTotal, jumpUrl);
                            edit.putString("detail" + unreadTotal, detail);
                            edit.putString("title" + unreadTotal, title);
                            edit.commit();
                            return;
                        }

                        if (detail == null || detail.equals("") || "".equals(title)) {
                            return;
                        }
                        if (infoDialog != null && infoDialog.isShowing()) {
                            infoDialog.dismiss();
                        }
                        String rightText = type.equals("1") ? "查看" : "知道了";
                        BackgroundManager backgroundManager = ((BaseApplication)BaseApplication.getContext()).getBackgroundManager();
                        infoDialog = new PushDialog(backgroundManager.getCurrentActivity(), title, detail, rightText, "返回", jumpUrl) {
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

                        if (!infoDialog.isShowing()) {
                            infoDialog.show();
                        }
                        break;
                    case Constant.RECEIVER_SEND_CODE_NEW_INFO:
                        SMMessage smMessage = (SMMessage) bundle.getSerializable("smMessage");
                        Activity mCurrentActivity= ((BaseApplication)BaseApplication.getContext()).getBackgroundManager().getCurrentActivity();
                        if (mCurrentActivity.getClass().getSimpleName().equals("MainPageActivity")) {
                            if (infoDialog != null && infoDialog.isShowing()) {
                                infoDialog.dismiss();
                            }
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
                            if (!infoDialog.isShowing()) {
                                infoDialog.show();
                            }
                        } else {
                            Gson gson = new Gson();
                            String saveValue = PushPreference.getPushInfo(InvestorAppli.getContext());
                            List<SMMessage> messageList = new ArrayList<>();
                            if (!TextUtils.isEmpty(saveValue)) {
                                messageList = gson.fromJson(saveValue, new TypeToken<List<SMMessage>>() {}.getType());
                            }
                            messageList.add(smMessage);
                            String values = gson.toJson(messageList, new TypeToken<List<SMMessage>>() {}.getType());
                            PushPreference.savePushInfo(InvestorAppli.getContext(), values);
                        }
                        break;
                    default:
                        break;
                }
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
    };

    private void onClickConfirm(String jumpUrl, String title, String shareType) {
        if (jumpUrl.contains("Android")) {
            try {
//                EventBus.getDefault().post(new RefreshUserinfo());
                JSONObject js = new JSONObject(jumpUrl);
                String android = js.getString("Android");
                if (android.contains("RenzhengActivity")) {
//                    Intent intent = new Intent(context, RenzhengActivity.class);
//                    intent.putExtra(Con.ADVISER_STATE, Contant.ADVISER_STATE_SUCCESS);
//                    context.startActivity(intent);
                } else if (android.contains("?")) {
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
                        try {
                            j.put("id", id);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
//                        new VideoDetailTask(context).start(j.toString(), new HttpResponseListener() {
//
//                            @Override
//                            public void onResponse(JSONObject response) {
//                                try {
//                                    Gson g = new Gson();
//                                    JSONObject schoolVideoO = response.getJSONObject("rows");
//                                    String string = schoolVideoO.toString();
//                                    SchoolVideo schoolVideo = g.fromJson(string, SchoolVideo.class);
//                                    ToolsUtils.toPlayVideoActivity(context, schoolVideo);
//                                    RongIM.getInstance().getRongIMClient().clearMessages(Conversation.ConversationType.PRIVATE, "INTIME49999");
//                                    RongIM.getInstance().getRongIMClient().removeConversation(Conversation.ConversationType.PRIVATE, "INTIME49999");
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                            }
//
//                            @Override
//                            public void onErrorResponse(String error, int statueCode) {
//
//                            }
//                        });
//                    }

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
            } else {
                hashMap.put(WebViewConstant.RIGHT_SHARE, true);
            }
            NavigationUtils.startActivityByRouter(InvestorAppli.getContext(), RouteConfig.GOTO_BASE_WEBVIEW, hashMap);
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
