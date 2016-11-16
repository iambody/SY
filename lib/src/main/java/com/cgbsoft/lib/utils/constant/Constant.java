package com.cgbsoft.lib.utils.constant;

/**
 * 用于存放静态变量 final static
 *  Created by xiaoyu.zhang on 2016/11/10 15:45
 *  
 */
public interface Constant {
    // 用户设置信息
    String USER_SETTING = "user_setting";
    //上一次更新的应用版本号，如果刚安装则为当前版本号
    String LAST_APP_VERSION = "last_app_version";

    // 用于判断后台到前台
    String IS_CURRENTRUNNINGFOREGROUND = "isCurrentRunningForeground";

    // mColors用于SwipeRefreshLayout
    int[] mColors = {0xFFF8698F, 0xFFFF4081, 0xFFF5F5F5};
}
