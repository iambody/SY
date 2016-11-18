package com.cgbsoft.lib.utils.cache;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by win8 on 2016/4/18.
 */
public class UserData {
    public static final String AUTHORITY = "com.cgbsoft.cache.user";

    private UserData() {}

    // 内部类
    public static final class user implements BaseColumns {

        // 构造方法
        private user() {}

        // 访问Uri
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/user");
        public static final String DEFAULT_SORT_ORDER = "userinfodata DESC";// 按姓名排序
        // 内容类型
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.amaker.employees";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.amaker.employees";

        // 默认排序常量
        // 表字段常量
        public static final String USER = "userinfodata";         // 用户信息
        public static final String TOKEN= "token";                // token
        public static final String LOGINFLAG= "loginflag";        //登陆状态
    }
}
