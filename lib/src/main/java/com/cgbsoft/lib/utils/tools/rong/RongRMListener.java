package com.cgbsoft.lib.utils.tools.rong;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.cgbsoft.lib.BaseApplication;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.utils.constant.Constant;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.CommandMessage;
import io.rong.message.TextMessage;
import rx.Observable;

/**
 * Created by lee on 2016/3/30
 */
public class RongRMListener implements RongIMClient.OnReceiveMessageListener, RongIM.OnReceiveUnreadCountChangedListener, Constant {

    /**
     * 收到消息的处理。
     *
     * @param message 收到的消息实体。
     * @param left    剩余未拉取消息数目。
     * @return 收到消息是否处理完成，true 表示走自已的处理方式，false 走融云默认处理方式。
     */
    @Override
    public boolean onReceived(Message message, int left) {
        Context context = BaseApplication.getContext();
        Log.i(this.getClass().getName(), "extra = " + message.getExtra() + "---content=" + message.getContent().toString());
        //开发者根据自己需求自行处理
        if (TextUtils.equals("0003fce75cd122ceaf1ac2d721a5f78e", message.getSenderUserId())) {
            CommandMessage content = (CommandMessage) message.getContent();
            Log.e("RongReceived", content.getData());
            context.sendBroadcast(new Intent(ACTION_LIVE_SEND_MSG).putExtra(ACTION_LIVE_SEND_CONTENT, content.getData()));
            return true;
        }

        if (TextUtils.equals(MSG_NOKNOW_INFORMATION, message.getSenderUserId())) {
            RongIM.getInstance().getRongIMClient().clearMessages(Conversation.ConversationType.PRIVATE, "INTIME49999");
            RongIM.getInstance().getRongIMClient().removeConversation(Conversation.ConversationType.PRIVATE, "INTIME49999");
        }

        if (message.getContent() instanceof TextMessage) {
            TextMessage content = (TextMessage) message.getContent();
            if (!TextUtils.isEmpty(((TextMessage) message.getContent()).getExtra())) {
                String msg = ((TextMessage) message.getContent()).getExtra();
                Gson g = new Gson();
                SMMessage smMessage = g.fromJson(msg, SMMessage.class);
                smMessage.setContent(content.getContent());
                smMessage.setContent(((TextMessage) message.getContent()).getContent());

                boolean hasPush = SPreference.getHasPushMsg(context);
                if (hasPush) {
                    Observable.just(true).delay(500, TimeUnit.MILLISECONDS).subscribe(new RxSubscriber<Boolean>() {
                        @Override
                        protected void onEvent(Boolean aBoolean) {
                            SPreference.saveHasPushMsg(context, false);
                        }

                        @Override
                        protected void onRxError(Throwable error) {

                        }
                    });
                    return !hasPush;
                }
                int ids = SPreference.getIdtentify(context);
                if (TextUtils.equals("1", smMessage.getReceiverType()) && ids == IDS_INVERSTOR
                        || TextUtils.equals("2", smMessage.getReceiverType()) && ids == IDS_ADVISER
                        || TextUtils.equals("3", smMessage.getReceiverType())) {

                    android.os.Message message2 = android.os.Message.obtain();
                    Bundle bundle2 = new Bundle();
                    switch (smMessage.getShowType()) {
                        case "1": //有弹窗、有跳转
                            message2.what = RECEIVER_SEND_CODE;
                            bundle2.putString("type", "1");
                            bundle2.putString("jumpUrl", smMessage.getJumpUrl());
                            bundle2.putString("detail", smMessage.getDialogSummary());
                            bundle2.putString("title", smMessage.getDialogTitle());
                            message2.setData(bundle2);
//                            MApplication.myHandler.sendMessage(message2);
                            break;
                        case "2": //有弹窗、无跳转
                            message2.what = RECEIVER_SEND_CODE;
                            bundle2.putString("type", "2");
                            bundle2.putString("jumpUrl", smMessage.getJumpUrl());
                            bundle2.putString("detail", smMessage.getDialogSummary());
                            bundle2.putString("title", smMessage.getDialogTitle());
                            message2.setData(bundle2);
//                            MApplication.myHandler.sendMessage(message2);
                            break;
                        case "3": //无弹窗、有跳转

                            break;
                        default:
                            break;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void onMessageIncreased(int i) {
        Log.e("msgcount", i + "");
        //b346 c7
        int intime40003 = RongIMClient.getInstance().getUnreadCount(Conversation.ConversationType.PRIVATE, "INTIME40003");
        int intime40004 = RongIMClient.getInstance().getUnreadCount(Conversation.ConversationType.PRIVATE, "INTIME40004");
        int intime40006 = RongIMClient.getInstance().getUnreadCount(Conversation.ConversationType.PRIVATE, "INTIME40006");
        int intime40007 = RongIMClient.getInstance().getUnreadCount(Conversation.ConversationType.PRIVATE, "INTIME40007");

        int cUnread = i - intime40003 - intime40004 - intime40006;
        int bUnread = i - intime40007;

//        UnReadBMSG unReadBMSG = new UnReadBMSG();
//        unReadBMSG.setUnreadCount(bUnread);
//        UnReadCMSG unReadCMSG = new UnReadCMSG();
//        unReadCMSG.setUnreadCount(cUnread);
//        EventBus.getDefault().post(unReadBMSG);
//        EventBus.getDefault().post(unReadCMSG);
    }
}