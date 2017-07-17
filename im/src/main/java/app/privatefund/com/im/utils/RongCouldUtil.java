package app.privatefund.com.im.utils;

import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.InvestorAppli;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.utils.constant.Constant;

/**
 * Created by lee on 2016/9/17.
 */
public class RongCouldUtil {
    private static RongCouldUtil rongCouldUtil = new RongCouldUtil();

    private RongCouldUtil() {
    }
//
//    public void init(Context context) {
//        RongIM.init(context);
//        RongIM.registerMessageType(ProductMessage.class);
//        RongIM.registerMessageType(NewsMessage.class);
//        RongIM.registerMessageType(PdfMessage.class);
////        RongIM.filtergetInstance().registerMessageTemplate(new ProductMessageItemProvider());
////        RongIM.getInstance().registerMessageTemplate(new PdfMessageItemProvider());
////        RongIM.getInstance().registerMessageTemplate(new NewMessageItemProvider());
//        RongIM.setOnReceiveMessageListener(new RongRMListener());
////        RongIM.setConversationBehaviorListener(new MyConversationBehaviorListener()); //会话界面监听
////        RongIM.setConversationListBehaviorListener(new MyConversationListBehaviorListener());//会话列表操作监听
//    }

    public static RongCouldUtil getInstance() {
        return rongCouldUtil;
    }

    public boolean hideConversation(String senderUserId) {
        if (AppManager.isInvestor(InvestorAppli.getContext())) {
            if (senderUserId.equals(Constant.msgMarketingStatus) || senderUserId.equals(Constant.msgOperationStatus) || senderUserId.equals(Constant.msgSecretary)) {
                return true;
            }
        } else if (!AppManager.isInvestor(InvestorAppli.getContext())) {
            if (senderUserId.equals(Constant.msgTradeInformation)) {
                return true;
            }
        }
        return false;
    }

    public boolean customConversation(String conversationSenderId) {
        if (conversationSenderId.equals(Constant.msgLiveStatus)
                || conversationSenderId.equals(Constant.msgProductStatus)
                || conversationSenderId.equals(Constant.msgMarketingStatus)
                || conversationSenderId.equals(Constant.msgOperationStatus)
                || conversationSenderId.equals(Constant.msgSystemStatus)
                || conversationSenderId.equals(Constant.msgTradeInformation)) {
            return true;
        }
        return false;
    }
}
