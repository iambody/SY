package com.cgbsoft.lib.contant;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/5/9-09:15
 */
public interface AppinfConstant {

    String USER_SHARE_PREFERENCE_SET = "user_flag_info_prefrence";

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
    String USERV2TOKEN="userv2token";
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

    // 最后返回时间
    String LAST_EXIT_BACK_TIME = "last_exit_back_time";


    //登录前的token
    String PUBLIC_KEY="publickey";
}
