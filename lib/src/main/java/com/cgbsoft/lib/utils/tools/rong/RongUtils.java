package com.cgbsoft.lib.utils.tools.rong;

import android.content.Context;

import com.cgbsoft.lib.utils.tools.rong.msg.NewsMessage;
import com.cgbsoft.lib.utils.tools.rong.msg.PdfMessage;
import com.cgbsoft.lib.utils.tools.rong.msg.ProductMessage;

import io.rong.imkit.RongIM;

/**
 * 融云工具
 * Created by xiaoyu.zhang on 2016/11/25 13:19
 * Email:zhangxyfs@126.com
 *  
 */
public class RongUtils {
    public void init(Context context){
        RongIM.init(context);
        RongIM.registerMessageType(ProductMessage.class);
        RongIM.registerMessageType(NewsMessage.class);
        RongIM.registerMessageType(PdfMessage.class);
//        RongIM.getInstance().registerMessageTemplate(new ProductMessageItemProvider());
//        RongIM.getInstance().registerMessageTemplate(new PdfMessageItemProvider());
//        RongIM.getInstance().registerMessageTemplate(new NewMessageItemProvider());
        RongIM.setOnReceiveMessageListener(new RongRMListener());
//        RongIM.setConversationBehaviorListener(new MyConversationBehaviorListener()); //会话界面监听
//        RongIM.setConversationListBehaviorListener(new MyConversationListBehaviorListener());//会话列表操作监听
    }
}
