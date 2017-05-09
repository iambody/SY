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

    public static HashMap<String, String> NewFoundHashMap;

    static {
        if (NewFoundHashMap == null) {
            NewFoundHashMap = new HashMap<>();
        }
        NewFoundHashMap.put("1", "行业资讯");
        NewFoundHashMap.put("2", "云观察");
        NewFoundHashMap.put("3", "云观点");
        NewFoundHashMap.put("4", "早知道");
    }

}
