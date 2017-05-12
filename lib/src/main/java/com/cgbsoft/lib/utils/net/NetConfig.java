package com.cgbsoft.lib.utils.net;

/**
 *  * Created by xiaoyu.zhang on 2016/11/8 17:19
 *  
 */
public class NetConfig {
    public static final boolean isLocal = true;

    public static final String UPLOAD_FILE = "https://upload.simuyun.com/";

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


    //登录
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
    static final String USERAGENT_URL = "/peyunupload/label/userAgree.json";

    static final String DOWNLOAD_BASEURL = "https://upload.simuyun.com/android/";

    static class MALL {
        //新增商城收货地址
        static final String MALL_ADD_ADDRESS = API_URL + "/ydInsertAddress";
        //保存商城收获地址
        static final String MALL_SAVE_ADDRESS = API_URL + "/ydUpdateAddress";
        //删除商城收货地址
        static final String MALL_DETELE_ADDRESS = API_URL + "/YdDeleteAddress";
        //获取商城收货地址列表
        static final String MALL_ADDRESS_LIST = API_URL + "/getYdAddressList";
        //设置商城默认收货地址
        static final String MALL_SET_DEFAULT = API_URL + "/ydDefaultAddress";
    }

    static class INFORMATION {
        private static final String information = "/information";
        //获取学院推荐视频
        static final String GET_COLLEGE_RECOMMEND_VIDEO = API_URL + information + "/video/recommend/5.0";
        //获取学院其他视频
        static final String GET_COLLEGE_OTHER_VIDEO = API_URL + information + "/videos/5.0";
        //获取视频详情
        static final String GET_VIDEO_INFO = API_URL + information + "/video/5.0";
        //点赞
        static final String TO_LIKE_VIDEO = API_URL + information + "/video/likes/5.0";
    }

    //用户相关
    static class USER {
        private static final String user = "/user";
        //获取用户信息
        static final String GET_USERINFO_URL = AUTH_URL + user + "/userInfo";
        //验证微信验证unionid是否已存在
        static final String WX_UNIONID_CHECK = AUTH_URL + user + "/weChatUnionId";
        // 微信登录
        static final String WX_LOGIN_URL = AUTH_URL + user + "/weChatLogin";
        //注册
        static final String REGISTER_URL = AUTH_URL + user + "/register";
        //发送验证码
        static final String SENDCODE_URL = AUTH_URL + user + "/voiceCaptcha";
        //验证手机号
        static final String CHECKCODE_URL = AUTH_URL + user + "/checkCaptcha";
        //重置密码
        static final String RESETPWD_URL = AUTH_URL + user + "/resetPassword";
        //合并帐号--验证手机
        static final String WXMERGECHECK_URL = AUTH_URL + user + "/wxMergePhone";
        // 合并手机账户－－确认合并
        static final String WXMARGECONFIRM_URL = AUTH_URL + user + "/confirmMerge";
        // 签到
        static final String SIGNIN_URL = AUTH_URL + user + "/signIn";
        // 修改密码
        static final String MODIFY_PASSWORD_URL = AUTH_URL + user + "/updatePassword";
    }

  //搜索相关
    static class SOUSOU{
      private static final String sousou = "/search";
      //产品全局搜索
      static  final String Get_PRODUCTLS_SOU=API_URL +sousou+"/query";
      //热门搜索
      static  final String Get_HOT_SOU=API_URL +sousou+"/hot";
  }

    //产品先关的url
    static class PRODUCT {
        private static final String product = "/products";
        //获取产品的标签
        static  final String Get_PRODUCT_TAG=API_URL+product+"/filter";
        //获取产品列表
        static  final String Get_PRODUCTLS_TAG=API_URL+product+"/filter/get";

    }

}
