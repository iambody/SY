package com.cgbsoft.lib.contant;

/**
 * desc   记录的状态是   module跳转到app  && module跳转到module 使用   App跳转module不需要使用！！  有问题@wyk
 * 命名规则是 module名字+ _ + 名字
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/5/4-17:02]
 */
public class RouteConfig {
    //跳转到C端首页的url
    public static final String GOTOCMAINHONE="investornmain_mainpageactivity";
    //跳转到全局搜索页面
    public static final String GODTOSOUSOU="product_sousouactivity";
    //跳转到视频播放页面
    public static final String GOTOVIDEOPLAY="video_videoplay";

    // 欢迎页面
    public static final String GOTOWELCOMEPAGE="investornmain_welcomeativity";

    // 忘记密码
    public static final String FORGAT_PASSWORD = "investornmain_forgetpasswordctivity";

    // 设置手势密码
    public static final String SET_GESTURE_PASSWORD = "investornmain_gestureeditactivity";

    // 搜索解决过
    public static final String SEARCH_RESULT_ACTIVITY = "PRODUCT_SEARCH_RESULT_ACTIVITY";

    public static final String VALIDATE_GESTURE_PASSWORD = "investornmain_gestureverifyactivity";

    //我的任务
    public static final String INVTERSTOR_MAIN_TASK = "invister_main_task_activity";

    // 云健菜单
    public static final String GOTO_CLOUD_MENU_ACTIVITY = "investormain_cloudmenuactivity";

    // 选择产品
    public static final String GOTO_SELECT_PRODUCT = "product_select_product_activity";

    // 基本webview页面
    public static final String GOTO_BASE_WEBVIEW = "lib_basewebviewactivity";

    //充值
    public static final String MALL_PAY = "mall_pay";

    //跳转到产品详情详情的路由
    public static final String GOTOPRODUCTDETAIL = "product_pproductdetail";
    //app moudle中的风险测评
    public static final String GOTO_APP_RISKEVALUATIONACTIVITY = "app_riskevaluate";
    //video的module的information资讯页
    public static final String GOTO_VIDEO_INFORMATIOON = "video_information";

    // 二维码
    public static final String GOTO_TWO_CODE_ACTIVITY = "qrcode_look_activity";

    // 带分享的webview
    public static final String GOTO_BASE_WITHSHARE_WEBVIEW = "goto_base_withshare_webview";

    // 未绑定页面
    public static final String GOTO_NO_BIND_TOUGU_ACTIVITY = "investormain_bindvisiteactivity";

    //进入直播
    public static final String GOTOLIVE = "live_join";
}
