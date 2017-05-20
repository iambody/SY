package app.privatefund.com.im.listener;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.FragmentActivity;
import android.text.ClipboardManager;
import android.view.View;

import com.cgbsoft.lib.utils.constant.Constant;
import com.cgbsoft.lib.utils.tools.NavigationUtils;

import java.util.HashMap;

import io.rong.common.RLog;
import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.userInfoCache.RongUserInfoManager;
import io.rong.imkit.widget.ArraysDialogFragment;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.PublicServiceProfile;
import io.rong.imlib.model.UserInfo;
import io.rong.message.ImageMessage;
import io.rong.message.TextMessage;

/**
 * Created by lee on 2016/3/23.
 */
public class MyConversationBehaviorListener implements RongIM.ConversationBehaviorListener {

    /**
     * 当点击用户头像后执行。
     *
     * @param context          上下文。
     * @param conversationType 会话类型。
     * @param userInfo         被点击的用户的信息。
     * @return 如果用户自己处理了点击后的逻辑，则返回 true，否则返回 false，false 走融云默认处理方式。
     */
    @Override
    public boolean onUserPortraitClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
//            RongIM.getInstance().startConversation(context, Conversation.ConversationType.PRIVATE, userInfo.getUserId(), userInfo.getName());
//            return true;
//        }
//        return false;
        if (conversationType == Conversation.ConversationType.GROUP) {
            RongIM.getInstance().startConversation(context, Conversation.ConversationType.PRIVATE, userInfo.getUserId(), userInfo.getName());
        }
        return true;
    }

    /**
     * 当长按用户头像后执行。
     *
     * @param context          上下文。
     * @param conversationType 会话类型。
     * @param userInfo         被点击的用户的信息。
     * @return 如果用户自己处理了点击后的逻辑，则返回 true，否则返回 false，false 走融云默认处理方式。
     */
    @Override
    public boolean onUserPortraitLongClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
        return false;
    }

    /**
     * 当点击消息时执行。
     *
     * @param context 上下文。
     * @param view    触发点击的 View。
     * @param message 被点击的消息的实体信息。
     * @return 如果用户自己处理了点击后的逻辑，则返回 true， 否则返回 false, false 走融云默认处理方式。
     */
    @Override
    public boolean onMessageClick(Context context, View view, Message message) {
        if (message.getContent() instanceof ImageMessage) {
            ImageMessage imageMessage = (ImageMessage) message.getContent();
            String local = imageMessage.getLocalUri() == null ? "null" : imageMessage.getLocalUri().toString();
            String remote = imageMessage.getRemoteUri() == null ? "null" : imageMessage.getRemoteUri().toString();
            String thumb = imageMessage.getThumUri() == null ? "null" : imageMessage.getThumUri().toString();
            String userInfo = imageMessage.getJSONUserInfo() == null ? "null" : imageMessage.getJSONUserInfo().toString();
            System.out.println("-----------localUrl=" + local + "-------remoteUrl=" + remote + "---thumbnail=" + thumb + "---jsonUserInfo=" + userInfo);
//            Intent intent  = new Intent(context, RongPhotoActivity.class);
//            intent.putExtra("photo", imageMessage.getLocalUri() == null ? imageMessage.getRemoteUri() : imageMessage.getLocalUri());
//            intent.putExtra(RongPhotoActivity.MESSAGE_PROCELABLE, message);
//            if (imageMessage.getThumUri() != null){
//                intent.putExtra("thumbnail",imageMessage.getThumUri());
//            }
//            context.startActivity(intent);

            HashMap<String ,Object> hashMap = new HashMap<>();
            hashMap.put(Constant.IMAGE_SAVE_PATH_LOCAL, imageMessage.getLocalUri() == null ? imageMessage.getRemoteUri().toString() : imageMessage.getLocalUri().getPath());
            hashMap.put(Constant.IMAGE_RIGHT_DELETE, false);
            hashMap.put(Constant.IMAGE_THREM_LOCAL, imageMessage.getThumUri().toString());
            NavigationUtils.startActivityByRouter(context, "investornmain_smoothimageactivity", hashMap);

            return true;
        }
        return false;
    }

    /**
     * 当长按消息时执行。
     *
     * @param context 上下文。
     * @param view    触发点击的 View。
     * @param message 被长按的消息的实体信息。
     * @return 如果用户自己处理了长按后的逻辑，则返回 true，否则返回 false，false 走融云默认处理方式。
     */
    @Override
    public boolean onMessageLongClick(Context context, final View view, Message message) {
        onLongClickProcess(context, view, message);
        return true;
    }

    /**
     * 当点击链接消息时执行。
     *
     * @param context 上下文。
     * @param link    被点击的链接。
     * @return 如果用户自己处理了点击后的逻辑处理，则返回 true， 否则返回 false, false 走融云默认处理方式。
     */
    @Override
    public boolean onMessageLinkClick(Context context, String link) {
        return false;
    }

    private void onLongClickProcess(final Context context, final View view, final Message message) {
        String sendId = message.getSenderUserId();
        UserInfo items1 = RongUserInfoManager.getInstance().getUserInfo(sendId);
        String name1 = null;
        if (!message.getConversationType().getName().equals(Conversation.ConversationType.APP_PUBLIC_SERVICE.getName()) && !message.getConversationType().getName().equals(Conversation.ConversationType.PUBLIC_SERVICE.getName())) {
//            if(message.getSenderUserId() != null) {
//                if(items1 == null || items1.getName() == null) {
//                    items1 = RongUserInfoManager.getInstance().getUserInfo(message.getSenderUserId());
//                }
//
//                if(items1 != null) {
//                    name1 = items1.getName();
//                }
//            }
        } else if (items1 != null) {
            name1 = items1.getName();
        } else {
            Conversation.PublicServiceType items = Conversation.PublicServiceType.setValue(message.getConversationType().getValue());
            PublicServiceProfile deltaTime = RongUserInfoManager.getInstance().getPublicServiceProfile(items, message.getTargetId());
            if (deltaTime != null) {
                name1 = deltaTime.getName();
            }
        }

        long deltaTime1 = RongIM.getInstance().getDeltaTime();
        long normalTime = System.currentTimeMillis() - deltaTime1;
        boolean enableMessageRecall = false;
        int messageRecallInterval = -1;
        boolean hasSent = !message.getSentStatus().equals(Message.SentStatus.SENDING) && !message.getSentStatus().equals(Message.SentStatus.FAILED);

        try {
            enableMessageRecall = RongContext.getInstance().getResources().getBoolean(io.rong.imkit.R.bool.rc_enable_message_recall);
            messageRecallInterval = RongContext.getInstance().getResources().getInteger(io.rong.imkit.R.integer.rc_message_recall_interval);
        } catch (Resources.NotFoundException var16) {
            RLog.e("TextMessageItemProvider", "rc_message_recall_interval not configure in rc_config.xml");
            var16.printStackTrace();
        }

        String[] items2;
        if (hasSent && enableMessageRecall && normalTime - message.getSentTime() <= (long) (messageRecallInterval * 1000) && message.getSenderUserId().equals(RongIM.getInstance().getCurrentUserId())) {
            items2 = new String[]{view.getContext().getResources().getString(io.rong.imkit.R.string.rc_dialog_item_message_copy), view.getContext().getResources().getString(io.rong.imkit.R.string.rc_dialog_item_message_delete), view.getContext().getResources().getString(io.rong.imkit.R.string.rc_dialog_item_message_recall)};
        } else {
            items2 = new String[]{view.getContext().getResources().getString(io.rong.imkit.R.string.rc_dialog_item_message_copy), view.getContext().getResources().getString(io.rong.imkit.R.string.rc_dialog_item_message_delete)};
        }

        ArraysDialogFragment.newInstance(name1, items2).setArraysDialogItemListener((dialog, which) -> {
            if (which == 0) {
                ClipboardManager clipboard = (ClipboardManager) view.getContext().getSystemService("clipboard");
                MessageContent messageContent = message.getContent();
                if (messageContent instanceof TextMessage) {
                    TextMessage content = (TextMessage) messageContent;
                    clipboard.setText(content.getContent());
                }
            } else if (which == 1) {
                RongIM.getInstance().deleteMessages(new int[]{message.getMessageId()}, (RongIMClient.ResultCallback) null);
            } else if (which == 2) {
                RongIM.getInstance().recallMessage(message);
            }

        }).show(((FragmentActivity) view.getContext()).getSupportFragmentManager());
    }
}