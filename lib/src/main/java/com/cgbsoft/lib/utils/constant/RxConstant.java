package com.cgbsoft.lib.utils.constant;

/**
 * 用于rxbus 的tag
 * Created by xiaoyu.zhang on 2016/11/16 14:46
 * Email:zhangxyfs@126.com
 *  
 */
public interface RxConstant {
    // 当改变身份的时候，向BottomNavigationBar发送个消息
    String BOTTOM_CHANGE_IDTENTIFY = "bottom_change_idtentify";

    String MAIN_BOTTOM_NAVIGATION_DOUBLE_CLICK_LEFT_FIRST = "main_bottom_navigation_double_click_left_first";

    String MAIN_BOTTOM_NAVIGATION_DOUBLE_CLICK_LEFT_SEC = "main_bottom_navigation_double_click_left_sec";

    String MAIN_BOTTOM_NAVIGATION_DOUBLE_CLICK_RIGHT_FIRST = "main_bottom_navigation_double_click_right_first";

    String MAIN_BOTTOM_NAVIGATION_DOUBLE_CLICK_RIGHT_SEC = "main_bottom_navigation_double_click_right_sec";

    String WELCOME_FINISH_OBSERVABLE = "welcome_finish_observable";

    String VIDEO_PLAY5MINUTES_OBSERVABLE = "video_play5minutes_observable";

    String VIDEO_LOCAL_REF_ONE_OBSERVABLE = "video_local_ref_one_observable";

    String VIDEO_DOWNLOAD_REF_ONE_OBSERVABE = "video_download_ref_one_observabe";

    String NOW_PLAY_VIDEOID_OBSERVABLE = "now_play_videoid_observable";

    String SET_PAGE_SWITCH_BUTTON = "set_page_switch_button_observable";

    String IS_PLAY_VIDEO_LOCAL_DELETE_OBSERVABLE = "is_play_video_local_delete_observable";

    String DOWNLOAD_TO_LIST_OBSERVABLE = "download_to_list_observable";

    String CLOSE_MAIN_OBSERVABLE = "close_main_observable";

    String LOOK_TWO_CODE_OBSERVABLE = "twocode_look_observable";

    String START_CONVERSATION_OBSERVABLE = "start_conversation_observable";
    //在产品模块 我的排序的dialog点击item时候传递给fragment数据消息
    String PRODUCT_ORDERBY_TO_FRAGMENT = "product_oderby_to_fragment";
    //在产品模块 我的筛选的dialog点击确定时候传递给fragment数据消息
    String PRODUCT_FILTER_TO_FRAGMENT = "product_filtr_to_fragment";
    //在首页接受需要刷新web的信息配置（游客去登录进入登录需要哦刷新）
    String MAIN_FRESH_WEB_CONFIG = "mainfreshwebconfig";
    //游客等鹿后刷新首页数据
    String MAIN_FRESH_LAY = "mainfreshwebconfig";
    String REFRUSH_WEBVIEW_OBSERVABLE = "refrush_webview_observable";

    String REFRUSH_UNREADER_INFO_NUMBER_OBSERVABLE = "refrush_unread_number_observable";

    String REFRUSH_UNREADER_NUMBER_RESULT_OBSERVABLE = "refrush_unread_result_observable";

    String ON_ACTIVITY_RESUME_OBSERVABLE = "on_activity_resume_observable";

    String RC_CONNECT_STATUS_OBSERVABLE = "rc_connect_status_observable";

    String REFRUSH_USER_INFO_OBSERVABLE = "refrush_user_info_observable";

    String REFRUSH_GESTURE_OBSERVABLE = "refrush_gesture_password_observable";

    String OPEN_MESSAGE_LIST_PAGE_OBSERVABLE = "open_message_list_activity_observable";

    String MALL_CHOICE_ADDRESS = "choice_address";

    String SWITCH_ASSERT_SHOW = "switch_assert_show_hint";

    String SWITCH_GROUP_SHOW = "switch_group_show_hint";

    String INVERSTOR_MAIN_PAGE = "inverstor_main_page";

    String ZHIBO_STATUES = "zhibo_status";

    String SHARE_PRODUCT_SEND = "share_product_send";

    String REFRUSH_UNREAD_INFOMATION = "refrush_unread_infomation";

    String Open_PAGE_LIVE_OBSERVABLE = "open_page_live_observable";

    //直播获取pdf请求
    String GET_LIVE_PDF_LIST_TASK = "get_live_pdf_list_task";

    //直播获取pdf成功
    String LIVE_PDF_SUC = "lice_pdf_suc";
    //跳转到个人中心页面
    String GOTO_MYCENTER_OBSERVABLE = "goto_mycenter_observable";
    //个人信息页面用到
    String GOTO_PERSONAL_INFORMATION = "personal_information";

    String GOTO_SWITCH_CENTIFY_DIR= "switch_cendify_dir";

    //全局跳转
    String JUMP_INDEX = "junp_index";

    String PAUSR_HEALTH_VIDEO = "pause_health_video";

    String UNREAD_MESSAGE_OBSERVABLE = "unread_message_observable";

    //商城删除地址
    String DELETE_ADDRESS = "delete_address";

    String RefreshRiskState = "refresh_risk_state";

    String LOGIN_KILL = "login_kill";
    String LOGIN_STATUS_DISABLE_OBSERVABLE = "login_status_disable_observable";
    String MAIN_PAGE_KILL = "mainpagekill";

    String MAIN_PAGE_KILL_START = "mainpagekillstart";

    String REFRESH_LIVE_DATA = "REFRESH_LIVE_DATA";

    //绑定理财师
    String BindAdviser = "bindadviser";

    String SCHOOL_VIDEO_PAUSE = "SCHOOL_VIDEO_PAUSE";

    String SWIPT_CODE_RESULT = "SWIPT_CODE_RESULT";
}
