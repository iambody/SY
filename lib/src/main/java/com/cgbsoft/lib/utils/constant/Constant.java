package com.cgbsoft.lib.utils.constant;

import android.Manifest;

import com.cgbsoft.lib.R;

import java.util.HashMap;

/**
 * 用于存放静态变量 final static
 *  Created by xiaoyu.zhang on 2016/11/10 15:45
 */
public interface Constant {

    int REQUEST_IMAGE = 100;
    // 用户设置信息
    String USER_INFO_BEAN_SP = "user_info_bean_sp";
    String USER_INFO_BEAN_PROPERTY = "user_info_property";
    //上一次更新的应用版本号，如果刚安装则为当前版本号
    String LAST_APP_VERSION = "last_app_version";
    // 微信登陆
    String weixin_login = "weixinlogin";

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

    String VOICE_PHONE = "125909888848";

    String ACTION_LIVE_SEND_MSG = "ACTION_LIVE_SEND_MSG";
    String ACTION_LIVE_SEND_CONTENT = "ACTION_LIVE_SEND_CONTENT";

    String HAS_PUSH_MESSAGE = "push_message_come_in";

    String ISTHISRUN_OPENDOWNLOAD = "isthisrun_opendownload";

    int RECEIVER_SEND_CODE = 99;
    int RECEIVER_SEND_CODE_NEW_INFO = 100;

    String RONG_SERVICE_RECEIVER = "rong_service_receiver";

    String FLOAT_POSITION_X = "float_position_x";

    String FLOAT_POSITION_Y = "float_position_y";

    String RECEIVER_EXIT_ACTION = "com.privateFund.exitLogin";
    String RECEIVER_ERRORCODE = "errorCode";

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
    //震动
    String PERMISSION_VIBRATE = Manifest.permission.VIBRATE;
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

    // 上传文件存放不同的路径
    //上传图片路径类型  用户头像
    public static final String UPLOAD_HEAD_TYPE = "avata/";
    //证件/资产证明
    public static final String UPLOAD_CERTIFICATE_TYPE = "certificates/";
    //打款凭条
    public static final String UPLOAD_SUBSRCIPTION_TYPE = "subscription/";
    //赎回
    public static final String UPLOAD_REDEMPTION_TYPE = "redemption/";
    //意见反馈
    public static final String UPLOAD_FEEDBACK_TYPE = "feedback/";

    // 手势密码
    public static final int POINT_STATE_NORMAL = 0;

    public static final int POINT_STATE_SELECTED = 1;

    public static final int POINT_STATE_WRONG = 2;

    //加载产品列表时候的 条数  默认加载20条数据
    public static final int LOAD_PRODUCT_lIMIT = 20;

    public static final String IMAGE_SAVE_PATH_LOCAL = "relative_asset_local";
    public static final String IMAGE_RIGHT_DELETE = "show_right_delete";
    public static final String IMAGE_THREM_LOCAL = "image_thmb_local";

    //消息模块特殊处理的ID
    //平台客服
    public static final String msgCustomerService = "dd0cc61140504258ab474b8f0a38bb56";
    //直播动态
    public static final String msgLiveStatus = "INTIME40001";
    //产品动态
    public static final String msgProductStatus = "INTIME40002";
    //营销喜报
    public static final String msgMarketingStatus = "INTIME40003";
    //运营公告
    public static final String msgOperationStatus = "INTIME40004";
    //系统公告
    public static final String msgSystemStatus = "INTIME40005";
    //小秘书
    public static final String msgSecretary = "INTIME40006";
    //交易信息
    public static final String msgTradeInformation = "INTIME40007";
    //特殊消息
    public static final String msgNoKnowInformation = "INTIME49999";

    HashMap<String, String> NewFoundHashMap = new HashMap<String, String>() {
        {
            put("1", "行业资讯");
            put("2", "云观察");
            put("3", "云观点");
            put("4", "早知道");
        }
    };
    HashMap<String, Integer> hashMap = new HashMap<String, Integer>() {
        {
            put(msgCustomerService, R.string.info_plamfort_server);
            put(msgLiveStatus, R.string.info_zhibo_video);
            put(msgProductStatus, R.string.info_product_dynic);
            put(msgMarketingStatus, R.string.info_market_reprot);
            put(msgOperationStatus, R.string.info_run_notice);
            put(msgSystemStatus, R.string.info_system_notice);
            put(msgSecretary, R.string.info_small_mishu);
            put(msgTradeInformation, R.string.info_trade);
            put(msgNoKnowInformation, R.string.info_speceil);
        }
    };
}
