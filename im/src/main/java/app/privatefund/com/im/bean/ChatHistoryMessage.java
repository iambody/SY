package app.privatefund.com.im.bean;

import android.content.Context;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import io.rong.imkit.RongContext;
import io.rong.imkit.utils.RongDateUtils;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.TextMessage;

/**
 * @author chenlong
 *
 * 聊天历史消息
 */
public class ChatHistoryMessage implements Parcelable {

    public String id;

    private String messageId;

    private String tagerId;

    private String senderId;

    private int count = 1;

    private Conversation.ConversationType type;

    private boolean polymerize;

    private Uri imageUrl;

    private String strName;

    private String content;

    private String showContent;

    private String flag;

    private String time;

    private int typeIndex;

    public ChatHistoryMessage() {};


    protected ChatHistoryMessage(Parcel in) {
        id = in.readString();
        messageId = in.readString();
        tagerId = in.readString();
        senderId = in.readString();
        count = in.readInt();
        typeIndex = in.readInt();
        polymerize = in.readByte() != 0;
        imageUrl = in.readParcelable(Uri.class.getClassLoader());
        strName = in.readString();
        content = in.readString();
        showContent = in.readString();
        flag = in.readString();
        time = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(messageId);
        dest.writeString(tagerId);
        dest.writeString(senderId);
        dest.writeInt(count);
        dest.writeInt(typeIndex);
        dest.writeByte((byte) (polymerize ? 1 : 0));
        dest.writeParcelable(imageUrl, flags);
        dest.writeString(strName);
        dest.writeString(content);
        dest.writeString(showContent);
        dest.writeString(flag);
        dest.writeString(time);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ChatHistoryMessage> CREATOR = new Creator<ChatHistoryMessage>() {
        @Override
        public ChatHistoryMessage createFromParcel(Parcel in) {
            return new ChatHistoryMessage(in);
        }

        @Override
        public ChatHistoryMessage[] newArray(int size) {
            return new ChatHistoryMessage[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTagerId() {
        return tagerId;
    }

    public void setTagerId(String tagerId) {
        this.tagerId = tagerId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public Conversation.ConversationType getType() {
        return type;
    }

    public void setType(Conversation.ConversationType type) {
        this.type = type;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Uri getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(Uri imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getStrName() {
        return strName;
    }

    public void setStrName(String strName) {
        this.strName = strName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public boolean isPolymerize() {
        return polymerize;
    }

    public void setPolymerize(boolean polymerize) {
        this.polymerize = polymerize;
    }

    public String getShowContent() {
        return showContent;
    }

    public void setShowContent(String showContent) {
        this.showContent = showContent;
    }

    public int getTypeIndex() {
        return typeIndex;
    }

    public void setTypeIndex(int typeIndex) {
        this.typeIndex = typeIndex;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ChatHistoryMessage) {
            ChatHistoryMessage chatHistoryMessage = (ChatHistoryMessage) o;
            return this.strName.equals(chatHistoryMessage.getStrName());
        }
        return super.equals(o);
    }

    @Override
    public String toString() {
        return "ChatHistoryMessage [name=" + strName + "]";
    }

//    public static void formatMessageToChatHistoryMessage(List<ChatHistoryMessage> historyMessageList, Conversation conversation, Context context) {
//        List<Message> listmessage = RongIMClient.getInstance().getHistoryMessages(conversation.getConversationType(), conversation.getTargetId(), -1, Integer.MAX_VALUE);
//        ChatHistoryMessage chatHistoryMessage = new ChatHistoryMessage();
//        if (!CollectionUtils.isEmpty(listmessage)) {
//            if (listmessage.size() > 2) {
//                chatHistoryMessage.setPolymerize(true);
//                chatHistoryMessage.setContent(listmessage.size() + "条相关记录消息");
//                chatHistoryMessage.setType(conversation.getConversationType());
//                chatHistoryMessage.setTagerId(conversation.getTargetId());
//                chatHistoryMessage.setName(RongContext.getInstance().getConversationTemplate(conversation.getConversationType().getName()).getTitle(conversation.getTargetId()));
//                chatHistoryMessage.setImageUrl(RongContext.getInstance().getConversationTemplate(conversation.getConversationType().getName()).getPortraitUri(conversation.getTargetId()));
//            } else {
//                Message message = listmessage.get(0);
//                chatHistoryMessage = formatMessageToChatHistoryMessage(message, context);
//            }
//            historyMessageList.add(chatHistoryMessage);
//        }
//    }

    public static ChatHistoryMessage formatMessageToChatHistoryMessage(Message message, Context context) {
        ChatHistoryMessage chatHistoryMessage = new ChatHistoryMessage();
        chatHistoryMessage.setMessageId(String.valueOf(message.getMessageId()));
        chatHistoryMessage.setSenderId(message.getSenderUserId());
        chatHistoryMessage.setContent(message.getContent() != null ? ((TextMessage) message.getContent()).getContent() : "");
        chatHistoryMessage.setType(message.getConversationType());
        chatHistoryMessage.setTypeIndex(message.getConversationType().getValue());
        chatHistoryMessage.setTagerId(message.getTargetId());
        chatHistoryMessage.setStrName(RongContext.getInstance().getConversationTemplate(message.getConversationType().getName()).getTitle(message.getTargetId()));
        chatHistoryMessage.setImageUrl(RongContext.getInstance().getConversationTemplate(message.getConversationType().getName()).getPortraitUri(message.getTargetId()));
        chatHistoryMessage.setTime(Message.MessageDirection.SEND == message.getMessageDirection() ? RongDateUtils.getConversationFormatDate(message.getReceivedTime(), context) :
                RongDateUtils.getConversationFormatDate(message.getSentTime(), context));
        return chatHistoryMessage;
    }

}
