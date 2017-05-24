package app.privatefund.com.im.listener;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.cgbsoft.lib.InvestorAppli;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.utils.constant.Constant;
import com.cgbsoft.lib.utils.tools.ThreadUtils;
import com.google.gson.Gson;

import app.privatefund.com.im.bean.SMMessage;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.CommandMessage;
import io.rong.message.TextMessage;

/**
 * Created by lee on 2016/3/30
 */
public class MyReceiveMessageListener implements RongIMClient.OnReceiveMessageListener, RongIM.OnReceiveUnreadCountChangedListener {
    /**
     * 收到消息的处理。
     *
     * @param message 收到的消息实体。
     * @param left    剩余未拉取消息数目。
     * @return 收到消息是否处理完成，true 表示走自已的处理方式，false 走融云默认处理方式。
     */
    @Override
    public boolean onReceived(Message message, int left) {
        Log.i(this.getClass().getSimpleName(), "messageinfo= " + message.getExtra() + "---content=" + message.getContent().toString());
        //开发者根据自己需求自行处理
        if (message.getSenderUserId().equals("0003fce75cd122ceaf1ac2d721a5f78e")) {
            CommandMessage content = (CommandMessage) message.getContent();
            Log.e("RongReceived", content.getData());
//            MApplication.mContext.sendBroadcast(new Intent(Constant.ACTION_LIVE_SEND_MSG).putExtra(Constant.ACTION_LIVE_SEND_CONTENT, content.getData()));
            return true;
        }

        if (Constant.msgNoKnowInformation.equals(message.getSenderUserId())) {
//            UnReadCMSG unReadCMSG = new UnReadCMSG();
//            unReadCMSG.setUnreadCount(1);
//            EventBus.getDefault().post(unReadCMSG);
            RongIM.getInstance().getRongIMClient().clearMessages(Conversation.ConversationType.PRIVATE, "INTIME49999");
            RongIM.getInstance().getRongIMClient().removeConversation(Conversation.ConversationType.PRIVATE, "INTIME49999");
        }

        if (message.getContent() instanceof TextMessage) {
            TextMessage content = (TextMessage) message.getContent();
            if (!TextUtils.isEmpty(((TextMessage) message.getContent()).getExtra())) {
                String msg = ((TextMessage) message.getContent()).getExtra();
                System.out.println("--------onReceiverMessage==" + msg);
                Gson g = new Gson();
                SMMessage smMessage = g.fromJson(msg, SMMessage.class);
                smMessage.setContent(content.getContent());
                smMessage.setContent(((TextMessage) message.getContent()).getContent());
                String shareType = smMessage.getShareType();
                boolean hasPush = SPreference.getBoolean(InvestorAppli.getContext(), Constant.HAS_PUSH_MESSAGE);
                if (hasPush) {
                    ThreadUtils.runOnMainThreadDelay(new Runnable() {
                        @Override
                        public void run() {
                            SPreference.putBoolean(InvestorAppli.getContext(), Constant.HAS_PUSH_MESSAGE, false);
                        }
                    }, 500);
                    return !hasPush;
                }

//                if (smMessage.getReceiverType().equals("1") && SPSave.getInstance(MApplication.mContext).getString(Contant.identify).equals(Contant.IdentityTouziren)
//                        || (smMessage.getReceiverType().equals("2") && SPSave.getInstance(MApplication.mContext).getString(Contant.identify).equals(Contant.IdentityLicaishi))
//                        || smMessage.getReceiverType().equals("3")) {
                    android.os.Message message2 = android.os.Message.obtain();
                    Bundle bundle2 = new Bundle();
                    switch (smMessage.getShowType()) {
                        case "1": //有弹窗、有跳转
                            message2.what = Constant.RECEIVER_SEND_CODE;
                            bundle2.putString("type", "1");
                            bundle2.putString("jumpUrl", smMessage.getJumpUrl());
                            bundle2.putString("detail", TextUtils.isEmpty(smMessage.getDialogSummary()) ? " " : smMessage.getDialogSummary());
                            bundle2.putString("title", TextUtils.isEmpty(smMessage.getDialogTitle()) ? " " : smMessage.getDialogTitle());
                            bundle2.putString("shareType", shareType);
                            message2.setData(bundle2);
//                            MApplication.myHandler.sendMessage(message2);
                            break;
                        case "2": //有弹窗、无跳转
                            message2.what = Constant.RECEIVER_SEND_CODE;
                            bundle2.putString("type", "2");
                            bundle2.putString("jumpUrl", smMessage.getJumpUrl());
                            bundle2.putString("detail", TextUtils.isEmpty(smMessage.getDialogSummary()) ? " " : smMessage.getDialogSummary());
                            bundle2.putString("title", TextUtils.isEmpty(smMessage.getDialogTitle()) ? " " : smMessage.getDialogTitle());
                            bundle2.putString("shareType", shareType);
                            message2.setData(bundle2);
//                            MApplication.myHandler.sendMessage(message2);
                            break;
                        case "3": //无弹窗、有跳转
                            break;
                        case "4": // 新的消息弹窗
                            message2.what = Constant.RECEIVER_SEND_CODE_NEW_INFO;
                            bundle2.putSerializable("smMessage", smMessage);
                            message2.setData(bundle2);
//                            MApplication.myHandler.sendMessage(message2);
                        default:
                            break;
                    }
//                }
            }
        }
        return false;
    }

    @Override
    public void onMessageIncreased(int i) {
        int intime49999 = RongIMClient.getInstance().getUnreadCount(Conversation.ConversationType.PRIVATE, "INTIME49999");
        Log.e(this.getClass().getSimpleName(), i + "-------intime49999=" + intime49999);
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