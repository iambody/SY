package com.cgbsoft.lib.utils.constant;

import android.Manifest;

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
    //投资人ids
    String IDS_INVERSTOR = "1";
    //理财师ids
    String IDS_ADVISER = "2";
    //身份 key
    String IDS_KEY = "identify";


    //----------------权限管理参数------------------

    int REQUEST_CODE_ASK_PERMISSIONS = 0x0101;

    String PERMISSION_CAMERA = Manifest.permission.CAMERA;

    String PERMISSION_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;

    String PERMISSION_AUDIO = Manifest.permission.RECORD_AUDIO;

    String PERMISSION_CONTANCTS = Manifest.permission.WRITE_CONTACTS;//读取联系人

    String PERMISSION_SMS = Manifest.permission.SEND_SMS;

    String PERMISSION_PHONE = Manifest.permission.READ_PHONE_STATE;//读取电话的状态

    String PERMISSION_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;// 允许应用访问范围性的定位
}
