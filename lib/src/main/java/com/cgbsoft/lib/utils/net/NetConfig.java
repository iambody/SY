package com.cgbsoft.lib.utils.net;

import android.text.TextUtils;

import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.BaseApplication;
import com.cgbsoft.lib.InvestorAppli;
import com.cgbsoft.lib.R;
import com.cgbsoft.lib.base.webview.BaseWebNetConfig;
import com.cgbsoft.lib.base.webview.CwebNetConfig;

public class NetConfig {
    public static boolean isLocal = true;
    public static String UPLOAD_FILE = "https://upload.simuyun.com/";
    //    public static String UPLOAD_SECRET_FILE = "https://upload.simuyun.com/";
    public static String UPLOAD_SECRET_FILE = "https://secret.simuyun.com/";
    public static String START_APPEND = "https://";

    //        private static String START_APP = "https://app";
//    public static String START_APP = "https://d8-app";//sim
    public static String START_APP = BaseApplication.getContext().getResources().getString(R.string.URL_BASE);//sim
    private static String START_DS = "http://muninubc";
    private static String START_WWW = "http://www";
    private static String BASE = ".simuyun.com";

    public static String SERVER_ADD = START_APP + BASE;
    public static String SERVER_DS = START_DS + BASE;
    public static String SERVER_WWW = START_WWW + BASE;

    public static String T_SERVER_DS = "http://d6-muninubc.simuyun.com";

    public static String SERVER_IP = "http://pv.sohu.com";

    public final static String API_URL = "api/v2";
    public final static String LIVE_URL = "zhibo/v2";
    public final static String AUTH_URL = "auth/v2";
    public final static String PROMOTION_URL = "promotion/v2";

    public final static String API_URL_V2 = "api/v2";
    public final static String API_URL_V3 = "api/v3";
    public final static String LIVE_URL_V2 = "zhibo/v2";
    public final static String AUTH_URL_V2 = "auth/v2";
    public final static String AUTH_URL_V3 = "auth/v3";
    public final static String API_NOV2_URL = "api/";
    public final static String TRACKING_V2 = "ubc/v2";

    static {
        if (!TextUtils.isEmpty(AppManager.getSelectAddress(InvestorAppli.getContext()))) {
            START_APP = START_APPEND.concat(AppManager.getSelectAddress(InvestorAppli.getContext()));
            SERVER_ADD = START_APP + BASE;
        }
    }

    public static void updateRequestUrl() {
        START_APP = START_APPEND.concat(AppManager.getSelectAddress(InvestorAppli.getContext()));
        SERVER_ADD = START_APP + BASE;
        BaseWebNetConfig.updateRequestUrl();
        CwebNetConfig.updateRequestUrl();
        OKHTTP.updateRequestUrl();
    }

    // 基本传输结构
    public static class DefaultParams {
        public static String client = "client";
        public static String token = "token";
        public static String uid = "adviserId";
        public static String deviceId = "deviceId";
        public static String appVersion = "version";
        public static String appPlatform = "appPlatform";
        public static String dev = "dev";
        public static String mid = "mid";
    }

    //登录
    final static String LOGIN_URL = AUTH_URL + "/appAuthenticate";
    //App通过该接口可以欢迎图片和AppStore开关以及版本检测
    final static String GET_RES_URL = API_URL + "/startup";
    //数据统计埋点
    final static String DATASTATISTICS_URL = "simuyun-munin/training";
    //获取ip
    final static String GETIP_URL = "cityjson";
    //客户风险评测提交接口
    final static String USERAGENT_URL = API_URL + "/useragree/sixiangyun";

    final static String DOWNLOAD_BASEURL = "https://upload.simuyun.com/android/";

    //
    final static String ACTION_POINT = PROMOTION_URL + "/common/availableOp";

    //全局导航栏
    final static String NAVIGATION = API_URL_V3 + "/navigation";

    // 全站三级导航
    final static String NAVIGATION_THREE = API_URL_V2 + "/navigation/third";

    //红包雨
    final static String RED_PACKET = API_URL + "/redpacket";

    static class API {

        //客户风险评测提交接口
        final static String RISK_EVALUTION = API_URL + "/riskEvaluation";

        // 获取群组列表
        final static String CHATE_GROUP_LIST = API_URL + "/chat/groupList";

        // 用户手机号码
        final static String GROUP_MEMBER_PHONE = API_URL + "/chat/memberPhoneNumber";

        // 群成员
        final static String GROUP_MEMBERS = API_URL + "/chat/groupMembers";

        // 群信息
        final static String GROUP_INFO = API_URL + "/chat/groupInformation";

        //群成員列表 新接口
        final static String GROUP_MEMBER_BY_DATE = API_URL + "/chat/groupMembersByDate";

        // 获取热门搜索列表
        final static String HOT_SEARCH_PRODUCT = API_URL + "/products/hotNames";
    }

    static class Auth {
        //获取容云token
        final static String GET_RONG_TOKEN = AUTH_URL_V2 + "/rc/gettoken";
        // 获取融云用户信息
        final static String RONGYUN_USERINFO = AUTH_URL_V2 + "/rc/userinfo";
        //获取平台客服聊天
        final static String PLATFORM_CUSTOMER = AUTH_URL_V2 + "/rc/greetingmessage";
        //获取机构经理的聊天
        final static String ORIGNATION_MANAGER = AUTH_URL_V2 + "/rc/managerinfo";
    }

    //合规
    static class Compliance {
        final static String compliance = "/compliance";
        //活体检测的sign获取
        final static String COMPLIANCE_LIVING_SIGN = API_URL + compliance + "/livingsign";
        //活体检测的反馈(慕夏处理)
        final static String COMPLIANCE_LIVING_RESULT = API_URL + compliance + "/queryResult";
        //活体检测的反馈结果(茼筒处理)
        final static String COMPLIANCE_BEAN_RESULT = "/auth/v3/credential/livingbody";//API_URL + compliance +"/recognition";
        //公共的人脸锁的server反馈结果
        final static String COMPLIANCE_BEAN_COMMENT_RESULT = "/auth/v3/credential/livingbody/common";//PI_URL + compliance +"/recognition/common";//
        //OCR获取sign
        final static String COMPLIANCE_OCR_SIGN = API_URL + compliance + "/ocrsign";
        //自定义的ocr
        final static String COMPLIANCE_OCR = API_URL + compliance + "/ocr";
        //人脸照片对比person照片库compare
        final static String COMPLIANCE_PERSON_COMPARE = AUTH_URL_V3 + "/credential/comparepersonimage";///auth/v3/credential/comparepersonimage
    }

    static class MALL {
        //新增商城收货地址
        final static String MALL_ADD_ADDRESS = API_URL_V2 + "/yd/insertydaddress";
        //保存商城收获地址
        final static String MALL_SAVE_ADDRESS = API_URL_V2 + "/yd/updateydAddress";
        //删除商城收货地址
        final static String MALL_DETELE_ADDRESS = API_URL_V2 + "/yd/deleteaddress";
        //获取商城收货地址列表
        final static String MALL_ADDRESS_LIST = API_URL_V2 + "/yd/listydaddress";
        //设置商城默认收货地址
        final static String MALL_SET_DEFAULT = API_URL_V2 + "/yd/updatedefaultaddress";
    }

    static class INFORMATION {
        private final static String information = "/information";
        //获取学院推荐视频
        final static String GET_COLLEGE_RECOMMEND_VIDEO = API_URL + information + "/video/recommend/5.0";
        //获取学院其他视频
        final static String GET_COLLEGE_OTHER_VIDEO = API_URL + information + "/videos/5.0";
        //获取视频详情
        final static String GET_VIDEO_INFO = API_URL + information + "/video/2c";
        //获取视频详情
//        static String GET_VIDEO_INFO = API_URL_V2 + information + "/video/2c";
        //点赞
        final static String TO_LIKE_VIDEO = API_URL + information + "/video/likes/5.0";


        //私享云新增的获取视频列表的V2接口
        final static String GET_VIDEO_LIST = API_URL + information + "/videos";
        //私享云新增的财富=》学院模块
        final static String GET_VIDEO_ALLINF = API_URL + information + "/video/all";

    }

    //每日任务
    static class TASK {
        private final static String task = "/task";
        //任务列表
        final static String TASK_LIST = API_URL_V2 + task + "/adviserTaskList";
        //任务完成领云豆
        final static String GET_COIN = API_URL_V2 + task + "/addTaskCoin";
    }

    //用户相关
    static class USER {
        private final static String user = "/user";
        //获取用户信息
        final static String GET_USERINFO_URL = AUTH_URL + user + "/userInfo";
        //验证微信验证unionid是否已存在
        final static String WX_UNIONID_CHECK = AUTH_URL + user + "/weChatUnionId";
        // 微信登录
        final static String WX_LOGIN_URL = AUTH_URL + user + "/weChatLogin";
        //注册
        final static String REGISTER_URL = AUTH_URL + user + "/register";
        //发送验证码
        final static String SENDCODE_URL = AUTH_URL + user + "/voiceCaptcha";
        //验证手机号
        final static String CHECKCODE_URL = AUTH_URL + user + "/checkCaptcha";
        //重置密码
        final static String RESETPWD_URL = AUTH_URL + user + "/resetPassword";
        //合并帐号--验证手机
        final static String WXMERGECHECK_URL = AUTH_URL + user + "/wxMergePhone";
        // 合并手机账户－－确认合并
        final static String WXMARGECONFIRM_URL = AUTH_URL + user + "/confirmMerge";
        // 签到
        final static String SIGNIN_URL = AUTH_URL + user + "/signIn";
        // 修改密码
        final static String MODIFY_PASSWORD_URL = AUTH_URL + user + "/updatePassword";
        // 关联资产
        final static String RELATED_ASSET_URL = AUTH_URL + user + "/connectedMyAsset";
        // 资产证明
        final static String ASSET_PROVET_URL = AUTH_URL + user + "/assetCertificate";
        // 更新用户信息
        final static String UPDATE_USER_INFO_URL = AUTH_URL + user + "/updateUserInfo";
        // 验证用户密码
        final static String VALIDATE_USER_PASSWORD_URL = AUTH_URL + user + "/checkPassword";
        // 用户反馈
        final static String USER_FEED_BACK_URL = AUTH_URL + user + "/problemFeedback";
    }

    //搜索相关
    static class SOUSOU {
        private final static String sousou = "/search";
        //产品全局搜索
        final static String Get_PRODUCTLS_SOU = API_URL + sousou + "/query";
        //热门搜索
        final static String Get_HOT_SOU = API_URL + sousou + "/hot";
    }

    //产品先关的url
    static class PRODUCT {
        private final static String product = "/products";
        //获取产品的标签
        final static String Get_PRODUCT_TAG = API_URL + product + "/filter";
        //获取产品列表
        final static String Get_PRODUCTLS_TAG = API_URL + product + "/filter/get";
        //获取产品详情
        final static String Get_PRODUCTDETAIL_URL = API_URL + product + "/single";
    }

    //直播相关
    static class LIVE {
        private final static String live = "/live";
        //获取直播签名
        final static String GET_LIVE_SIGN = LIVE_URL_V2 + live + "/user/sig";
        //获取直播预告
        final static String GET_PRO_LIST = LIVE_URL_V2 + live + "/rooms/news";
        //获取直播列表
        final static String GET_LIVE_LIST = LIVE_URL_V2 + live + "/rooms";
        //获取房间号
        final static String GET_ROOM_NUM = LIVE_URL_V2 + live + "/room/id";
        //发送直播评论
        final static String SENT_COMMENT = LIVE_URL_V2 + live + "/room/sendMessage";
        //获取直播附件
        final static String GET_LIVE_PDF = LIVE_URL + live + "/room/attachment";
        //主播心跳
        final static String LIVE_HOST_HEART = LIVE_URL_V2 + live + "/room/activate";
        //进入房间
        final static String CUSTOM_JOIN_ROOM = LIVE_URL_V2 + live + "/room/enter";
        //退出房间
        final static String CUSTOM_EXIT_ROOM = LIVE_URL_V2 + live + "/room/exit";
        //主播开房间
        final static String HOST_OPEN_LIVE = LIVE_URL_V2 + live + "/room";
        //主播关闭房间
        final static String HOST_CLOSE_LIVE = LIVE_URL_V2 + live + "/room/close";
        //获取房间用户
        final static String GET_ROOM_MEMBER = LIVE_URL_V2 + live + "/users";
        //直播预告
        final static String GET_LIVE_NOTICE = LIVE_URL_V2 + live + "/preview/latest";
    }

    //支付
    static class PAY {
        private final static String pay = "/pay";
        //支付配置
        final static String GET_PAY_CONFIG = API_URL + pay + "/rechargeinfo";
        //校验支付结果
        final static String CHECK_RECHARGE_SIGN = API_URL + pay + "/checksign";
        //云豆充值
        final static String YD_RECHARGE = API_URL + pay + "/ydrecharge";
    }

    public static String defaultRemoteLogin = "http://simuyun-upload.oss-cn-beijing.aliyuncs.com/information/logo.png";
    public static String noticeRemoteLogin = "http://simuyun-upload.oss-cn-beijing.aliyuncs.com/information/notice.png";
    public static String systemRemoteLogin = "http://simuyun-upload.oss-cn-beijing.aliyuncs.com/information/system.png";

    // 获取资源文件的相关信息
    public final static String RESOURCE_FILE_INFO = "http://upload.simuyun.com/app/h5-storage/healthZipConfig.json";
    // 腾讯视频内容校验文件
    public final static String TENCENT_VIDEO_URL = "http://1251892263.vod2.myqcloud.com/9dbfd9a6vodgzp1251892263/14f16f914564972818450529832/wW2YHIkwbNoA.mp4";

    static class TRACKDATA {
        final static String TRACKING = TRACKING_V2 + "/track/app";
        final static String CONFIG = TRACKING_V2 + "/track/config";
    }

    //视频相关模块
    static class VIDEO {
        private final static String video = "/information/video";
        //视频点赞
        final static String VIDEO_DIANZAN = API_URL + video + "/likes/5.0";
        //视频评论添加
        final static String VIDEO_COMMENT_ADD = API_URL + video + "/2c/comment/add";
        //视频的更多评论
        final static String VIDEO_COMMENT_LS = API_URL + video + "/2c/comment";

        //********************私享云的fragment里面的视频初始化信息***********
        final static String VIDEO_SCHOOL_ALL_IND = API_URL + video + "/all";
        //获取视频列表
        final static String VIDEO_SCHOOL_LS = API_URL + video + "s";
    }

    //用户授权相关 V2********
    static class AUTHOR {
        private final static String auth = "/auth";
        //登录前获取publickey公钥
        final static String GET_PUBLIC_KEY = auth + "/v2/publicKey";
        //V2登录
        final static String LOGIN_V2_URL = auth + "/v2/appAuthenticate";
    }

    /**
     * ****************以下为6.0私享云 新增新增接口的配置路径接口的配置路径*******************
     **/
    /**
     * enjoycloud模块外层接口
     */
    static class SXY {
        private final static String sxy = "/enjoycloud";
        //首页
        final static String GETHOME = API_URL_V3 + sxy + "/apphome";

        //通过手机硬件地址兑换相应的userid和tOKEN
        final static String VISITOR_GET_USERID = API_URL + "/visitorAuth";

        //获取全站导航栏信息
        final static String GET_NAVIFAITION = API_URL + "navigation";

        //游客登录
        final static String VISITORLOGIN = AUTH_URL + "/visitorLogin";

        //签到
        final static String SIGNIN = API_URL + sxy + "/signin";
    }

    static class Mine {
        // 我的接口
        final static String GET_MINE = API_URL + "/enjoycloud" + "/usercentre";
        // 我的活动
        final static String ACTIVITES = API_URL + "/salons/mine";
        // 提交投资账号
        final static String CommitInvisitAccount = AUTH_URL_V2 + "/user/certSubmit";
        // 我的金融资产
        final static String MINE_FININCIAL_ASSERT = AUTH_URL_V2 + "/user/finincialAssert";
    }

    /**
     * 健康模块接口
     */
    static class Health {
        final static String HEALTH_INTRODUCE_URL = API_URL + "/health/introduce";

        final static String HEALTH_GET_URL = API_URL + "/health/commend";

        final static String HEALTH_PROJECT_LIST = API_URL + "/health/healthprojectlist";

        final static String HEALTH_COURSE_GET_URL = API_URL + "/health/healthCourseList";

        // 健康免费资讯预约
        final static String HEALTH_FREE_BESPEAK_URL = API_URL + "/health/consult";

        // 健康短信验证
        final static String HEALTH_INFO_VALIDATE_URL = API_URL + "/health/consult/captcha";

    }

    static class Discovery {
        // 资讯首页数据
        final static String DISCOVERY_FIRST_PAGE = API_URL + "/information/home";

        // 资讯列表页面
        final static String DISCOVERY_LIST_PAGE = API_URL + "/information/list";

        // 股票指数
        final static String DISCOVERY_STOCK_INDEX =  "/api/stockindex";
    }

    /**
     * 生活家模块banner&尚品
     */
    static class ELEGANT {
        private final static String elegantLiving = "/ydtoc";
        static final String GETBANNER = API_URL + elegantLiving + "/listbanners";
        static final String GETGOODSFIRST = API_URL + elegantLiving + "/listhotgoods";
        static final String GETGOODSMORE = API_URL + elegantLiving + "/category";
    }

    static class ChangePsd {
        static final String CHANGE_PSD = AUTH_URL + "/user/updatePassword";
        static final String UPDATE_USERINFO = AUTH_URL + "/user/updateUserInfo";
        static final String UPDATE_USERICON = AUTH_URL + "/user/uploadUserImage";
    }

    static class Salon {
        static final String UPDATE_SALON_AND_CITY = API_URL + "/salons/main";
        static final String UPDATE_SALON = API_URL + "/salons";
    }

    static class Indentity {
        static final String GET_INDENTITY_TYPE_LIST = AUTH_URL_V2 + "/credential/config";
        static final String VERIFY_INDENTITY = AUTH_URL_V2 + "/credential/info";
        //新版获取证件信息
        static final String VERIFY_INDENTITY_V3 = AUTH_URL_V3 + "/credential/baseinfo";
        static final String CREDENTIALS_DETILS = AUTH_URL_V3 + "/credential/detail";
        static final String GET_INDENTITY_LIST = AUTH_URL_V2 + "/credentials";
        static final String UPLOAD_REMOTE_PATHS = AUTH_URL_V2 + "/credential/detail";
        static final String UPLOAD_OTHER_CREDENTIALS = AUTH_URL_V3 + "/credential/upload";
        static final String GET_INDENTITY_LIST_ADD = AUTH_URL_V2 + "/subcredentials";
        static final String GET_LIVING_STATE = AUTH_URL_V3 + "/credential/livingbody/history";
    }

    /**
     * 公募基金的API
     */
    static class PUBLIC_FUND {
        static final String HOME_RECOMMEND = API_URL_V2 + "/kz/proxy/sixiangbao";
        static  final  String PRIVATE_FUND_INF=API_URL_V2+"/kz/userinfo";
    }

    public static class SoDown {
        public static final String DOWN_RUL = UPLOAD_FILE + "android_so/armeabi-v7a.zip";
    }
}
