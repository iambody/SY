package com.cgbsoft.lib.contant;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/5/9-09:15
 */
public interface AppinfConstant {

    String USER_SHARE_PREFERENCE_SET = "sxy_info_prefrence";

    /**
     * 存储理财师标识的sp的名字
     */
    String IsAdviser_Tage = "isadviser_tag";

    /**
     * 存储用户信息
     */
    String User_Tage = "user_tag";

    /**
     * 存储用户id
     */
    String USERIDSP = "userid_sp";

    /**
     * 存储用户账号
     */
    String USERACCOUNT = "useraccount_sp";

    /**
     * 存储用户token
     */
    String USERTOKENSP = "usertokensp";
    /**
     * 存储用户的v2token
     */
    String USERV2TOKEN = "userv2token";
    /**
     * 是否登录
     */
    String ISLOGIN = "isloginsp";

    /**
     * 融云token
     */
    String RONGYUN_TOKEN = "rongyun_tokoen_sp";

    /**
     * 聊天名称
     */
    String CHAT_NAME = "chatName";

    /**
     * 资源文件名称
     */
    String RESOURCE_ZIP_FILE_OLD = "resource_zip_file_old";

    /**
     * 资源文件名称
     */
    String RESOURCE_ZIP_FILE_SERVER = "resource_zip_file_server";

    /**
     * 资源版本下载地址
     */
    String RESOURCE_DOWNLOAD_ADDRESS = "resource_download_address";

    /**
     * 是否有新的资源版本
     */
    String RESOURCE_VERSION_HAS = "resource_version_has";

    /**
     * 未读消息
     */
    String UNREAD_INFOMATION = "unread_infomation";

    /**
     * 聊天名称
     */
    String CONVERSATION_TYPE = "conversationType";

    /**
     * 融云token有效期
     */
    String RONGYUN_TOKEN_EXPIRED = "rongyun_token_expired_sp";

    /**
     * 用户是否首次登录 和用户名称绑定
     */
    String USER_FIRST_LOGIN = "user_first_login";

    //产品搜搜记录
    String SOUSOUHISTORY = "historysp";

    // 机构经理ID
    String ORG_MANAGER_UID = "managerUid";

    // 机构经理电话
    String ORG_MANAGER_MOBILE = "managerMobile";

    // 团队长ID
    String TEAM_MANAGER_UID = "teamManagerUid";

    // 是否有团队长
    String HAS_TEAM_MANAGER = "hasTeamManage";

    // 是否有机构
    String HAS_ORG_MANAGER = "hasManage";

    // 是否有用户所属群
    String HAS_USER_GROUP = "hasGroup";

    // 设置/验证手势密码最后时间
    String LAST_SET_VALIDATE_TIME = "last_set_validate_time";

    // 选择地址
    String SELECT_ADDRESS = "select_address";

    // 显示资产
    String SHOW_ASSERT_STATUS = "show_asset_status";

    //登录前的token
    String PUBLIC_KEY = "publickey";

    //游客模式存储的key
    String VISITOR_KEY = "isvisitor";

    // 密码校验错误
    String GetsureValidateError = "gesurePasswordvalidateError";

    //首页游客数据
    String VISITERHOME = "homevisiter";

    //首页正常数据
    String NORMALHOME = "homenormal";

    //学院数据游客
    String VIDEOSCHOOLVISTER="videoschoolvisiter";

    //学院数据正常
    String VIDEOSCHOOLNORMAL="videoschoolNORMAL";

    // 资讯数据
    String DISCOVERLISTFIRSTDATA = "discoverlistfirstdata";

    //首页公募基金的缓存
    String PUBIC_FUND_CACAHEA="publicfundcache";
    //公募基金的信息
    String PUBLIC_FUND_INF="publicfundinf";

    //latest的手机号
    String LATEST_PHONE_NUMBER="latestphonenumber";
}
