package com.cgbsoft.lib.utils.net;

/**
 *  * Created by xiaoyu.zhang on 2016/11/8 17:19
 *  
 */
public class NetConfig {
    public static final boolean isLocal = true;

    private static final String START_APP = "https://app";
    private static final String START_DS = "http://muninubc";
    private static final String START_WWW = "http://www";
    private static final String BASE = ".simuyun.com";

    public static final String SERVER_ADD = START_APP + BASE;
    public static final String SERVER_DS = START_DS + BASE;
    public static final String SERVER_WWW = START_WWW + BASE;

    public static final String SERVER_IP = "http://pv.sohu.com";

    public static final String API_URL = "api";
    public static final String LIVE_URL = "zhibo";
    public static final String AUTH_URL = "auth";

    // 基本传输结构
    public static class DefaultParams {
        public static final String token = "token";
        public static final String uid = "adviserId";
        public static final String deviceId = "deviceId";
        public static final String appVersion = "version";
        public static final String appPlatform = "appPlatform";
    }


    //登陆
    static final String LOGIN_URL = AUTH_URL + "/appAuthenticate";
    //App通过该接口可以欢迎图片和AppStore开关以及版本检测
    static final String GET_RES_URL = API_URL + "/startup/5.0";
    //数据统计埋点
    static final String DATASTATISTICS_URL = "simuyun-munin/training";
    //获取ip
    static final String GETIP_URL = "cityjson";
    //获取容云token
    static final String GET_RONG_TOKEN = AUTH_URL + "/rc/gettoken";
    //客户风险评测提交接口
    public static final String USERAGENT_URL = "/peyunupload/label/userAgree.json";


    //用户相关
    static class USER {
        private static final String user = "/user";
        //获取用户信息
        static final String GET_USERINFO_URL = AUTH_URL + user + "/userInfo";
        //验证微信验证unionid是否已存在
        static final String WX_UNIONID_CHECK = AUTH_URL + user + "/weChatUnionId";
        // 微信登陆
        static final String WX_LOGIN_URL = AUTH_URL + user + "/weChatLogin";
        //注册
        static final String REGISTER_URL = AUTH_URL + user + "/register";
        //发送验证码
        static final String SENDCODE_URL = AUTH_URL + user + "/voiceCaptcha";
        //验证手机号
        static final String CHECKCODE_URL = AUTH_URL + user + "/checkCaptcha";
        //重置密码
        static final String RESETPWD_URL = AUTH_URL + user + "/resetPassword";
    }


}
