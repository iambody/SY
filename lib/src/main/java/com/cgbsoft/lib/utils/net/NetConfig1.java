package com.cgbsoft.lib.utils.net;

import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.InvestorAppli;
import com.cgbsoft.lib.base.webview.BaseWebNetConfig;
import com.cgbsoft.lib.base.webview.CwebNetConfig;

public class NetConfig1 {
    public static boolean isLocal = true;
    public static String UPLOAD_FILE = "https://upload.simuyun.com/";

    private static String START_APPEND = "https://";
    private static String START_APP = "https://d10-app";

    private static String START_DS = "http://muninubc";
    private static String START_WWW = "http://www";
    private static String BASE = ".simuyun.com";

    public static String SERVER_ADD = START_APP + BASE;
    public static String SERVER_DS = START_DS + BASE;
    public static String SERVER_WWW = START_WWW + BASE;

    public static String SERVER_IP = "http://pv.sohu.com";

    public static String API_URL = "api/v2";
    public static String LIVE_URL = "zhibo/v2";
    public static String AUTH_URL = "auth/v2";
    public static String PROMOTION_URL = "promotion/v2";

    public static String API_URL_V2 = "api/v2";
    public static String LIVE_URL_V2 = "zhibo/v2";
    public static String AUTH_URL_V2 = "auth/v2";

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
    static String LOGIN_URL = AUTH_URL + "/appAuthenticate";
    //App通过该接口可以欢迎图片和AppStore开关以及版本检测
    static String GET_RES_URL = API_URL + "/startup";
    //数据统计埋点
    static String DATASTATISTICS_URL = "simuyun-munin/training";
    //获取ip
    static String GETIP_URL = "cityjson";
    //客户风险评测提交接口
    static String USERAGENT_URL = "/peyunupload/label/userAgree.json";

    static String DOWNLOAD_BASEURL = "https://upload.simuyun.com/android/";

    static String ACTION_POINT = PROMOTION_URL + "/common/availableOp";

    //全局导航栏
    static String NAVIGATION = API_URL_V2 + "/navigation";

    static class API {

        //客户风险评测提交接口
        static String RISK_EVALUTION = API_URL + "/riskEvaluation";

        // 获取群组列表
        static String CHATE_GROUP_LIST = API_URL + "/chat/groupList";

        // 用户手机号码
        static String GROUP_MEMBER_PHONE = API_URL + "/chat/memberPhoneNumber";

        // 群成员
        static String GROUP_MEMBERS = API_URL + "/chat/groupMembers";

        // 群信息
        static String GROUP_INFO = API_URL + "/chat/groupInformation";

        //群成員列表 新接口
        static String GROUP_MEMBER_BY_DATE = API_URL + "/chat/groupMembersByDate";

        // 获取热门搜索列表
        static String HOT_SEARCH_PRODUCT = API_URL + "/products/hotNames";
    }

    static class Auth {
        //获取容云token
        static String GET_RONG_TOKEN = AUTH_URL_V2 + "/rc/gettoken";
        // 获取融云用户信息
        static String RONGYUN_USERINFO = AUTH_URL_V2 + "/rc/userinfo";
        //获取平台客服聊天
        static String PLATFORM_CUSTOMER = AUTH_URL_V2 + "/rc/greetingmessage";
        //获取机构经理的聊天
        static String ORIGNATION_MANAGER = AUTH_URL_V2 + "/rc/managerinfo";
    }

    static class MALL {
        //新增商城收货地址
        static String MALL_ADD_ADDRESS = API_URL_V2 + "/yd/insertydaddress";
        //保存商城收获地址
        static String MALL_SAVE_ADDRESS = API_URL_V2 + "/yd/updateydAddress";
        //删除商城收货地址
        static String MALL_DETELE_ADDRESS = API_URL_V2 + "/yd/deleteaddress";
        //获取商城收货地址列表
        static String MALL_ADDRESS_LIST = API_URL_V2 + "/yd/listydaddress";
        //设置商城默认收货地址
        static String MALL_SET_DEFAULT = API_URL_V2 + "/yd/updatedefaultaddress";
    }

    static class INFORMATION {
        private static String information = "/information";
        //获取学院推荐视频
        static String GET_COLLEGE_RECOMMEND_VIDEO = API_URL + information + "/video/recommend/5.0";
        //获取学院其他视频
        static String GET_COLLEGE_OTHER_VIDEO = API_URL + information + "/videos/5.0";
        //获取视频详情
        static String GET_VIDEO_INFO = API_URL + information + "/video/2c";
        //获取视频详情
//        static String GET_VIDEO_INFO = API_URL_V2 + information + "/video/2c";
        //点赞
        static String TO_LIKE_VIDEO = API_URL + information + "/video/likes/5.0";


        //私享云新增的获取视频列表的V2接口
        static String GET_VIDEO_LIST = API_URL + information + "/videos";
        //私享云新增的财富=》学院模块
        static String GET_VIDEO_ALLINF = API_URL + information + "/video/all";

    }

    //每日任务
    static class TASK {
        private static String task = "/task";
        //任务列表
        static String TASK_LIST = API_URL_V2 + task + "/adviserTaskList";
        //任务完成领云豆
        static String GET_COIN = API_URL_V2 + task + "/addTaskCoin";
    }

    //用户相关
    static class USER {
        private static String user = "/user";
        //获取用户信息
        static String GET_USERINFO_URL = AUTH_URL + user + "/userInfo";
        //验证微信验证unionid是否已存在
        static String WX_UNIONID_CHECK = AUTH_URL + user + "/weChatUnionId";
        // 微信登录
        static String WX_LOGIN_URL = AUTH_URL + user + "/weChatLogin";
        //注册
        static String REGISTER_URL = AUTH_URL + user + "/register";
        //发送验证码
        static String SENDCODE_URL = AUTH_URL + user + "/voiceCaptcha";
        //验证手机号
        static String CHECKCODE_URL = AUTH_URL + user + "/checkCaptcha";
        //重置密码
        static String RESETPWD_URL = AUTH_URL + user + "/resetPassword";
        //合并帐号--验证手机
        static String WXMERGECHECK_URL = AUTH_URL + user + "/wxMergePhone";
        // 合并手机账户－－确认合并
        static String WXMARGECONFIRM_URL = AUTH_URL + user + "/confirmMerge";
        // 签到
        static String SIGNIN_URL = AUTH_URL + user + "/signIn";
        // 修改密码
        static String MODIFY_PASSWORD_URL = AUTH_URL + user + "/updatePassword";
        // 关联资产
        static String RELATED_ASSET_URL = AUTH_URL + user + "/connectedMyAsset";
        // 资产证明
        static String ASSET_PROVET_URL = AUTH_URL + user + "/assetCertificate";
        // 更新用户信息
        static String UPDATE_USER_INFO_URL = AUTH_URL + user + "/updateUserInfo";
        // 验证用户密码
        static String VALIDATE_USER_PASSWORD_URL = AUTH_URL + user + "/checkPassword";
        // 用户反馈
        static String USER_FEED_BACK_URL = AUTH_URL + user + "/problemFeedback";
    }

    //搜索相关
    static class SOUSOU {
        private static String sousou = "/search";
        //产品全局搜索
        static String Get_PRODUCTLS_SOU = API_URL + sousou + "/query";
        //热门搜索
        static String Get_HOT_SOU = API_URL + sousou + "/hot";
    }

    //产品先关的url
    static class PRODUCT {
        private static String product = "/products";
        //获取产品的标签
        static String Get_PRODUCT_TAG = API_URL + product + "/filter";
        //获取产品列表
        static String Get_PRODUCTLS_TAG = API_URL + product + "/filter/get";
        //获取产品详情
        static String Get_PRODUCTDETAIL_URL = API_URL + product + "/single";
    }

    //直播相关
    static class LIVE {
        private static String live = "/live";
        //获取直播签名
        static String GET_LIVE_SIGN = LIVE_URL_V2 + live + "/user/sig";
        //获取直播列表
        static String GET_LIVE_LIST = LIVE_URL_V2 + live + "/rooms";
        //获取房间号
        static String GET_ROOM_NUM = LIVE_URL_V2 + live + "/room/id";
        //发送直播评论
        static String SENT_COMMENT = LIVE_URL_V2 + live + "/room/sendMessage";
        //获取直播附件
        static String GET_LIVE_PDF = LIVE_URL + live + "/room/attachment";
        //主播心跳
        static String LIVE_HOST_HEART = LIVE_URL_V2 + live + "/room/activate";
        //进入房间
        static String CUSTOM_JOIN_ROOM = LIVE_URL_V2 + live + "/room/enter";
        //退出房间
        static String CUSTOM_EXIT_ROOM = LIVE_URL_V2 + live + "/room/exit";
        //主播开房间
        static String HOST_OPEN_LIVE = LIVE_URL_V2 + live + "/room";
        //主播关闭房间
        static String HOST_CLOSE_LIVE = LIVE_URL_V2 + live + "/room/close";
        //获取房间用户
        static String GET_ROOM_MEMBER = LIVE_URL_V2 + live + "/users";
        //直播预告
        static String GET_LIVE_NOTICE = LIVE_URL_V2 + live + "/preview/latest";
    }

    //支付
    static class PAY {
        private static String pay = "/pay";
        //支付配置
        static String GET_PAY_CONFIG = API_URL + pay + "/rechargeinfo";
        //校验支付结果
        static String CHECK_RECHARGE_SIGN = API_URL + pay + "/checksign";
        //云豆充值
        static String YD_RECHARGE = API_URL + pay + "/ydrecharge";
    }

    public static String defaultRemoteLogin = "http://p1.bqimg.com/1949/efd21f89ac519468.png";
    public static String getDefaultRemoteLogin = "https://upload.simuyun.com/live/80983f89-0baf-407f-9bff-a0e297757642.png";

    //视频相关模块
    static class VIDEO {
        private static String video = "/information/video";
        //视频点赞
        static String VIDEO_DIANZAN = API_URL + video + "/likes/5.0";
        //视频评论添加
        static String VIDEO_COMMENT_ADD = API_URL + video + "/2c/comment/add";
        //视频的更多评论
        static String VIDEO_COMMENT_LS = API_URL + video + "/2c/comment";

        //********************私享云的fragment里面的视频初始化信息***********
        static String VIDEO_SCHOOL_ALL_IND = API_URL + video + "/all";
        //获取视频列表
        static String VIDEO_SCHOOL_LS = API_URL + video + "s";


    }

    //用户授权相关 V2********
    static class AUTHOR {
        private static String auth = "/auth";
        //登录前获取publickey公钥
        static String GET_PUBLIC_KEY = auth + "/v2/publicKey";
        //V2登录
        static String LOGIN_V2_URL = auth + "/v2/appAuthenticate";
    }


    /**
     * ****************以下为6.0私享云 新增新增接口的配置路径接口的配置路径*******************
     **/
    /**
     * enjoycloud模块外层接口
     */
    static class SXY {
        private static String sxy = "/enjoycloud";
        //首页
        static String GETHOME = API_URL + sxy + "/apphome";

        //通过手机硬件地址兑换相应的userid和tOKEN
        static String VISITOR_GET_USERID = API_URL + "/visitorAuth";

        //获取全站导航栏信息
        static String GET_NAVIFAITION = API_URL + "navigation";

        //游客登录
        static String VISITORLOGIN = AUTH_URL + "/visitorLogin";

        //签到
        static String SIGNIN = API_URL + sxy + "/signin";
    }

    static class Mine {
        // 我的接口
        static String GET_MINE = API_URL + "/enjoycloud" + "/usercentre";
        // 我的活动
        static String ACTIVITES = API_URL  + "/salons/mine";
        // 提交投资账号
        static String CommitInvisitAccount = API_URL  + "/auth/user/certSubmit";
    }

    /**
     * 健康模块接口
     */
    static class Health {
        static String HEALTH_INTRODUCE_URL = API_URL + "/health/introduce";

        static String HEALTH_GET_URL = API_URL + "/health/commend";

        // 健康免费资讯预约
        static String HEALTH_FREE_BESPEAK_URL = API_URL + "/health/consult";

        // 健康短信验证
        static String HEALTH_INFO_VALIDATE_URL = API_URL + "/health/consult/captcha";
    }

    static class Discovery {
        // 资讯首页数据
        static String DISCOVERY_FIRST_PAGE = API_URL + "/information/home";

        // 资讯列表页面
        static String DISCOVERY_LIST_PAGE = API_URL + "/information/list";
    }

    /**
     * 生活家模块banner&尚品
     */
    static class ELEGANT {
        private static String elegantLiving = "/ydtoc";
        static String GETBANNER = API_URL + elegantLiving + "/listbanners";
        static String GETGOODSFIRST = API_URL + elegantLiving + "/listhotgoods";
        static String GETGOODSMORE = API_URL + elegantLiving + "/category";
    }
    static class ChangePsd{
        static String CHANGE_PSD = AUTH_URL + "/user/updatePassword";
        static String UPDATE_USERINFO = AUTH_URL + "/user/updateUserInfo";
        static String UPDATE_USERICON = AUTH_URL + "/user/uploadUserImage";
    }
    static class Salon{
        static String UPDATE_SALON_AND_CITY = API_URL + "/salons/main";
        static String UPDATE_SALON = API_URL + "/salons";
    }
}
