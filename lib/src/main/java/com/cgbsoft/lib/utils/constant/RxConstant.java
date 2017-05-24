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

    String IS_PLAY_VIDEO_LOCAL_DELETE_OBSERVABLE = "is_play_video_local_delete_observable";

    String DOWNLOAD_TO_LIST_OBSERVABLE = "download_to_list_observable";

    String CLOSE_MAIN_OBSERVABLE = "close_main_observable";

    String LOOK_TWO_CODE_OBSERVABLE = "twocode_look_observable";
    //在产品模块 我的排序的dialog点击item时候传递给fragment数据消息
     String PRODUCT_ORDERBY_TO_FRAGMENT="product_oderby_to_fragment";
    //在产品模块 我的筛选的dialog点击确定时候传递给fragment数据消息
    String PRODUCT_FILTER_TO_FRAGMENT="product_filtr_to_fragment";

    String REFRUSH_WEBVIEW_OBSERVABLE = "refrush_webview_observable";

    String ON_ACTIVITY_RESUME_OBSERVABLE = "on_activity_resume_observable";

    String RC_CONNECT_STATUS_OBSERVABLE = "rc_connect_status_observable";

    String REFRUSH_GESTURE_OBSERVABLE = "refrush_gesture_password_observable";

    String OPEN_MESSAGE_LIST_PAGE_OBSERVABLE = "open_message_list_activity_observable";

}
