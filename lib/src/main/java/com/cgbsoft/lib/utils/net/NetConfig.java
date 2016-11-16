package com.cgbsoft.lib.utils.net;

/**
 *  * Created by xiaoyu.zhang on 2016/11/8 17:19
 *  
 */
public class NetConfig {
    public static final boolean isLocal = true;
    public static final String SERVER_ADD = "https://app.simuyun.com";
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
    public static final String LOGIN_URL = AUTH_URL + "/appAuthenticate";
    //App通过该接口可以欢迎图片和AppStore开关以及版本检测
    public static final String GET_RES_URL = API_URL + "/startup/5.0";
}
