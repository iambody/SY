package com.cgbsoft.lib.base.webview;

/**
 * @author chenlong
 */

public class WebViewConstant {
    public static final String push_message_url = "push_message_url";
    public static final String push_message = "push_message";
    public static final String push_message_title = "push_message_title";
    public static final String push_message_value = "push_message_value";
    public static final String right_message_index = "right_message_icon";
    public static final String RIGHT_SAVE = "need_save";
    public static final String PAGE_INIT = "page_init";
    public static final String RIGHT_SHARE = "right_share";
    public static final String PAGE_SHOW_TITLE = "page_show_title";
    public static final String PAGE_SHARE_WITH_EMAIL = "page_share_with_email";
    public static final String PUSH_MESSAGE_OBJECT_NAME = "push_message_object_name";
    public static final String PUSH_MESSAGE_RONGYUN_URL_NAME = "push_message_rongyun_url_name";
    public static final String PUSH_MESSAGE_COME_HERE = "push_message_come_here";
    public static final String Jump_Info_KeyWord = "keywords";
    public static final String pdf_url = "pdf_url";
    public static final String pdf_name = "pdf_name";

    /**
     * 需要回调的指令集
     */
    public class AppCallBack {
        public static final String BUY_NEW = "app:buynow";
        public static final String CAN_BUY = "app:canBuy";
        public static final String LIVE_VIDEO = "app:liveVideo";
        public static final String MODIFY_PASSWORD = "app:changepassword";
        public static final String JUMP_PRODUCT_DETAIL = "app:jumpProduct";
        public static final String INVITE_CUSTOM = "app:inviteCust";
        public static final String INVITE_SHARE = "app:tocShare";
        //        public static final String OPEN_SHAREPAGE ="app:openSharePage";
        public static final String TOC_SHARE = "app:tocShare";
        public static final String TOC_PDF = "app:viewpdf";
        public static final String TOC_GO_PRODUCTLS = "app:jumpProductList";
        public static final String TOC_PRODUCT_TOUTIAO = "app:openInformation";
        public static final String TOC_MALL_STATE = "app:mallHasTitle";
        //风险评测结果
//        public static final String TOC_RISKTEST = "app:riskTest";
    }

    /**
     * 需要拦截跳转到非BaseWebViewActivity页面的url地址集
     */
    public class IntecepterActivity {
        public static final String RECOMMEND_FRIEND = "/settings/recommendation.html";
        public static final String DISCOVER_DETAILS = "/discover/details.html";
        public static final String LIFE_DETAIL = "/biz/life/detail.html";
        public static final String LIFE_SPECIAL = "/biz/life/special.html";
        public static final String ACTIVITTE_DRAGON_DEATIL = "/biz/indexSecond/active_detail.html";

    }

    public class Navigation {
        /**
         * 首页
         */
        public static final int MAIN_PAGE = 10;
        /**
         * 会员页
         */
        public static final int MEMBER_PAGE = 1001;
        /**
         * 充值页
         */
        public static final int RECHARGE_PAGE = 1002;
        /**
         * 任务页
         */
        public static final int TASK_PAGE = 1003;
        /**
         * 云豆乐园
         */
        public static final int YD_ENJOY_PAGE = 1004;
        /**
         * 分享给好友
         */
        public static final int SHARE_FRIEND_PAGE = 1005;
        /**
         * 尊享私行
         */
        public static final int PRIVATE_BANK_PAGE = 20;
        /**
         * 产品
         */
        public static final int PRODUCT_PAGE = 2001;
        /**
         * 资讯
         */
        public static final int INFORMATION_PAGE = 2002;
        /**
         * 学院
         */
        public static final int VIDEO_PAGE = 2003;
        /**
         * 乐享生活
         */
        public static final int LIFE_ENJOY_PAGE = 30;
        /**
         * 生活家
         */
        public static final int LIFT_HOME_PAGE = 3001;
        /**
         * 尚品
         */
        public static final int LIFT_MALL_PAGE = 3002;
        /**
         * 健康
         */
        public static final int HEALTH_PAGE = 40;
        /**
         * 介绍
         */
        public static final int HEALTH_INTRODUCTION_PAGE = 4001;
        /**
         * 检测
         */
        public static final int HEALTH_CHECK_PAGE = 4002;
        /**
         * 医疗
         */
        public static final int HEALTH_MEDICAL_PAGE = 4003;
        /**
         * 我的
         */
        public static final int MINE_PAGE = 50;
        /**
         * 签到
         */
        public static final int MINE_SIGN_PAGE = 5001;
        /**
         * 我的活动
         */
        public static final int MINE_ACTION_PAGE = 5002;
        /**
         * 我的卡卷
         */
        public static final int MINE_CARD_PAGE = 5003;
        /**
         * 我的贺卡
         */
        public static final int MINE_GREETING_CARD_PAGE = 5004;

    }

}
