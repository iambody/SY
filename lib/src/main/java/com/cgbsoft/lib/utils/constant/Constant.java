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
    int IDS_INVERSTOR = 2;
    //理财师ids
    int IDS_ADVISER = 1;
    //身份 key
    String IDS_KEY = "identify";

    String WEIXIN_APPID = "wx7259d65d4382e566";
    String WEIXIN_APPSECRET = "6e1deaa7b9a8c1380bd69e3de47fcc21";

    String NIGHT_MODE = "appcompat_night_mode";


    //----------------权限管理参数(危险权限)------------------

    int REQUEST_CODE_ASK_PERMISSIONS = 0x0101;
    //危险权限,需要在运行时请求.注意: 危险权限是按组来分的,所以,当你申请了多个同组的危险权限时,运行时只需要申请一个就行
    // 联系人 
    String PERMISSION_WRITE_CONTANCTS = Manifest.permission.WRITE_CONTACTS;
    String PERMISSION_GET_CONTANCTS = Manifest.permission.GET_ACCOUNTS;
    String PERMISSION_READ_CONTANCTS = Manifest.permission.READ_CONTACTS;
    //电话
    String PERMISSION_READ_CALL_LOG = Manifest.permission.READ_CALL_LOG;
    String PERMISSION_READ_PHONE_STATE = Manifest.permission.READ_PHONE_STATE;
    String PERMISSION_CALL_PHONE = Manifest.permission.CALL_PHONE;
    String PERMISSION_WRITE_CALL_LOG = Manifest.permission.WRITE_CALL_LOG;
    String PERMISSION_USE_SIP = Manifest.permission.USE_SIP;
    String PERMISSION_PROCESS_CALLS = Manifest.permission.PROCESS_OUTGOING_CALLS;
    String PERMISSION_ADD_VICEMAIL = Manifest.permission.ADD_VOICEMAIL;
    //日历
    String PERMISSION_READ_CALENDAR = Manifest.permission.READ_CALENDAR;
    String PERMISSION_WRITE_CALENDAR = Manifest.permission.WRITE_CALENDAR;
    // 相机 
    String PERMISSION_CAMERA = Manifest.permission.CAMERA;
    //传感器
    String PERMISSION_BODY_SENSORS = Manifest.permission.BODY_SENSORS;
    //定位
    String PERMISSION_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    String PERMISSION_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    // 存储
    String PERMISSION_READ_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
    String PERMISSION_WRITE_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    // 录音 
    String PERMISSION_AUDIO = Manifest.permission.RECORD_AUDIO;
    // 短信 
    String PERMISSION_SEND_SMS = Manifest.permission.SEND_SMS;
    String PERMSSION_READ_SMS = Manifest.permission.READ_SMS;
    String PERMISSION_RECEIVE_MMS = Manifest.permission.RECEIVE_MMS;
    String PERMISSION_RECEIVE_SMS = Manifest.permission.RECEIVE_SMS;
    String PERMISISON_WAP_PUSH = Manifest.permission.RECEIVE_WAP_PUSH;

}
