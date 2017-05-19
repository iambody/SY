package com.cgbsoft.privatefund;

import android.content.Context;
import android.support.multidex.MultiDex;

import com.cgbsoft.lib.InvestorAppli;
import com.cgbsoft.lib.utils.tools.DeviceUtils;
import com.cgbsoft.privatefund.utils.SimuyunUncaughtExceptionHandler;

import app.privatefund.com.im.bean.NewsMessage;
import app.privatefund.com.im.bean.PdfMessage;
import app.privatefund.com.im.bean.ProductMessage;
import app.privatefund.com.im.listener.MyConversationBehaviorListener;
import app.privatefund.com.im.listener.MyConversationListBehaviorListener;
import app.privatefund.com.im.listener.MyReceiveMessageListener;
import app.privatefund.com.im.listener.NewMessageItemProvider;
import app.privatefund.com.im.listener.PdfMessageItemProvider;
import app.privatefund.com.im.listener.ProductMessageItemProvider;
import io.rong.imkit.RongIM;

/**
 * @author chenlong
 */

public class InitApplication  extends InvestorAppli {

    @Override
    public void onCreate() {
        super.onCreate();
//        Contexts.init(this);
        Thread.setDefaultUncaughtExceptionHandler(new SimuyunUncaughtExceptionHandler(this));

        /**
         * OnCreate 会被多个进程重入，这段保护代码，确保只有您需要使用 RongIM 的进程和 Push 进程执行了 init。
         * io.rong.push 为融云 push 进程名称，不可修改。
         */
        if (getApplicationInfo().packageName.equals(DeviceUtils.getCurProcessName(getApplicationContext())) ||
                "io.rong.push".equals(DeviceUtils.getCurProcessName(getApplicationContext()))) {

            /**
             * IMKit SDK调用第一步 初始化
             */
            RongIM.init(this);
            RongIM.registerMessageType(ProductMessage.class);
            RongIM.registerMessageType(PdfMessage.class);
            RongIM.registerMessageType(NewsMessage.class);
            RongIM.getInstance().registerMessageTemplate(new ProductMessageItemProvider());
            RongIM.getInstance().registerMessageTemplate(new PdfMessageItemProvider());
            RongIM.getInstance().registerMessageTemplate(new NewMessageItemProvider());
            RongIM.setOnReceiveMessageListener(new MyReceiveMessageListener());
            RongIM.setConversationBehaviorListener(new MyConversationBehaviorListener()); //会话界面监听
            RongIM.setConversationListBehaviorListener(new MyConversationListBehaviorListener());//会话列表操作监听
        }

    }
}
