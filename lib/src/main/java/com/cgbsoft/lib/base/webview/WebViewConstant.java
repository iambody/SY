package com.cgbsoft.lib.base.webview;

import java.util.HashMap;

/**
 * @author chenlong
 */

public class WebViewConstant {
    public static final String push_message_url = "push_message_url";
    public static final String push_message = "push_message";
    public static final String push_message_title = "push_message_title";
    public static final String push_message_value = "push_message_value";
    public static final String RIGHT_SAVE = "need_save";
    public static final String PAGE_INIT = "page_init";
    public static final String RIGHT_SHARE = "right_share";
    public static final String PAGE_SHOW_TITLE = "page_show_title";
    public static final String PAGE_SHARE_WITH_EMAIL = "page_share_with_email";
    public static final String PUSH_MESSAGE_OBJECT_NAME = "push_message_object_name";
    public static final String PUSH_MESSAGE_RONGYUN_URL_NAME = "push_message_rongyun_url_name";
    public static final String PUSH_MESSAGE_COME_HERE = "push_message_come_here";
    public static final String Jump_Info_KeyWord = "keywords";
    public static final String pdf_url = "pdf_url";
    public static final String pdf_name = "pdf_name";

    /**
     * 需要回调的指令集
     */
    public class AppCallBack {
        public static final String BUY_NEW = "app:buynow";
        public static final String CAN_BUY = "app:canBuy";
        public static final String LIVE_VIDEO = "app:liveVideo";
        public static final String MODIFY_PASSWORD = "app:changepassword";
        public static final String JUMP_PRODUCT_DETAIL = "app:jumpProduct";
        public static final String INVITE_CUSTOM = "app:inviteCust";
        public static final String INVITE_SHARE="app:tocShare";
    }

    /**
     * 需要拦截跳转到非BaseWebViewActivity页面的url地址集
     */
    public class IntecepterActivity {
        public static final String recommend_friend = "/settings/recommendation.html";

    }

}
