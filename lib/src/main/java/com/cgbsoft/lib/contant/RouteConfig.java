package com.cgbsoft.lib.contant;

/**
 * desc   记录的状态是   module跳转到app  && module跳转到module 使用   App跳转module不需要使用！！  有问题@wyk
 * 命名规则是 module名字+ _ + 名字
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/5/4-17:02
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

    // 设置手势密码
    public static final String SET_GESTURE_PASSWORD = "investornmain_gestureeditactivity";

    // 验证手势密码
    public static final String VALIDATE_GESTURE_PASSWORD = "investornmain_gestureverifyactivity";

    //我的任务
    public static final String INVTERSTOR_MAIN_TASK = "invister_main_task_activity";

    //充值
    public static final String MALL_PAY = "mall_pay";
}
