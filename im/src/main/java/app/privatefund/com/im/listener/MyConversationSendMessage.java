package app.privatefund.com.im.listener;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Message;

/**
 * Created by lee on 2016/4/15.
 */
public class MyConversationSendMessage implements RongIM.OnSendMessageListener {


    @Override
    public Message onSend(Message message) {
        return null;
    }

    @Override
    public boolean onSent(Message message, RongIM.SentMessageErrorCode sentMessageErrorCode) {
        return false;
    }
}
