package app.privatefund.com.im.utils;

import android.content.Context;
import android.text.TextUtils;

import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.InvestorAppli;
import com.cgbsoft.lib.utils.constant.Constant;

import app.privatefund.com.im.Contants;
import app.privatefund.com.im.R;
import app.privatefund.com.im.adapter.RongConversationListAdapter;
import io.rong.imkit.model.UIConversation;

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
//        else if (!AppManager.isInvestor(InvestorAppli.getContext())) {
//            if (senderUserId.equals(Constant.msgTradeInformation)) {
//                return true;
//            }
//        }
        return false;
    }

    public boolean customConversation(String conversationSenderId) {
        if (conversationSenderId.equals(Constant.msgLiveStatus)
                || conversationSenderId.equals(Constant.msgProductStatus)
                || conversationSenderId.equals(Constant.msgMarketingStatus)
                || conversationSenderId.equals(Constant.msgOperationStatus)
//                || conversationSenderId.equals(Constant.msgSystemStatus)
                || conversationSenderId.equals(Constant.msgTradeInformation)) {
            return true;
        }
        return false;
    }

    public boolean customConversationAll(String conversationSenderId) {
        if (conversationSenderId.equals(Constant.msgLiveStatus)
                || conversationSenderId.equals(Constant.msgProductStatus)
                || conversationSenderId.equals(Constant.msgMarketingStatus)
                || conversationSenderId.equals(Constant.msgOperationStatus)
                || conversationSenderId.equals(Constant.msgSystemStatus)
                || conversationSenderId.equals(Constant.msgNoKnowInformation)
                || conversationSenderId.equals(Constant.msgTradeInformation))               {
            return true;
        }
        return false;
    }

    public static void customServerTop(Context context, RongConversationListAdapter rongConversationListAdapter) {
        UIConversation tempServer = null;
        UIConversation bindAdviser = null;
        String bindAdviserId = TextUtils.isEmpty(AppManager.getUserInfo(context).getToC().getBandingAdviserId()) ? "" : AppManager.getUserInfo(context).getToC().getBandingAdviserId();

        int index = 0;
        int bindAdvierIndex = 0;
        for (int i = 0; i < rongConversationListAdapter.getCount(); i++) {
            UIConversation itemConversation = rongConversationListAdapter.getItem(i);
            if (context.getString(R.string.simuyun_server).equals(itemConversation.getUIConversationTitle())) {
                tempServer = itemConversation;
                index = i;
            }
        }
        if (tempServer != null) {
            rongConversationListAdapter.remove(index);
            rongConversationListAdapter.add(tempServer, 0);
        }

        for (int i = 0; i < rongConversationListAdapter.getCount(); i++) {
            UIConversation itemConversation = rongConversationListAdapter.getItem(i);
            if (bindAdviserId.equals(itemConversation.getConversationTargetId())) {
                bindAdvierIndex = i;
                bindAdviser = itemConversation;
                break;
            }
        }

        if (bindAdviser != null) {
            rongConversationListAdapter.remove(bindAdvierIndex);
            rongConversationListAdapter.add(bindAdviser, tempServer != null ? 1 : 0);
        }
    }
}
