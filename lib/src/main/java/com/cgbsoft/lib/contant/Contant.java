package com.cgbsoft.lib.contant;

import com.cgbsoft.lib.AppInfStore;
import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.BaseApplication;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/5/8-17:00
 */
public class Contant {
    //加载产品列表时候的 条数  默认加载20条数据
    public static final int LOAD_PRODUCT_lIMIT = 20;
    //视频评论默认条数
    public static final int VIDEO_COMMENT_LIMIT = 20;

    public static final String CUR_LIVE_ROOM_NUM = "CUR_LIVE_ROOM_NUM";

    //
    public static final String VISITE_LOOK_NAVIGATION = "VISITE_LOOK_NAVIGATION";

    public static final String LIVE_SHARE_TITLE = String.format("盈泰财富云财富大讲堂正在直播，%s邀请你来一起看！",
            AppManager.getUserInfo(BaseApplication.getContext()).getNickName() != null ? AppManager.getUserInfo(BaseApplication.getContext()).getNickName() : "私享云用户");

    public static final String MEMBER_CENTER_TITLE = "会员中心";

    public static final String red_packet_url = "red_packet_url";

}
