package com.cgbsoft.lib.utils.net;

/**
 *  * Created by xiaoyu.zhang on 2016/11/8 17:19
 *  
 */
public class NetConfig {
    public static final boolean isLocal = true;

    private static final String START_APP = "https://app";
    private static final String START_DS = "http://muninubc";
    private static final String BASE = ".simuyun.com";

    public static final String SERVER_ADD = START_APP + BASE;
    public static final String SERVER_DS = START_DS + BASE;

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
    public static final String LOGIN_URL = AUTH_URL + "/appAuthenticate";
    //App通过该接口可以欢迎图片和AppStore开关以及版本检测
    public static final String GET_RES_URL = API_URL + "/startup/5.0";
    //数据统计埋点
    public static final String DATASTATISTICS_URL = "simuyun-munin/training";
    //获取ip
    public static final String GETIP_URL = "cityjson";


}
