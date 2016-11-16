package com.cgbsoft.lib.utils.cache;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by xiaoyu.zhang on 2016/11/7 13:56
 */
public class OtherData {
    public static final String AUTHORITY = "com.cgbsoft.cache.other";

    private OtherData() {
    }

    // 内部类
    public static final class Other implements BaseColumns {

        // 构造方法
        private Other() {
        }

        // 访问Uri
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/other");
        //        public static final String DEFAULT_SORT_ORDER = "userinfodata DESC";// 按姓名排序
        // 内容类型
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.amaker.employees";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.amaker.employees";

        // 默认排序常量
        // 表字段常量
        public static final String TITLE = "title";
        public static final String CONTENT = "content";                    // 内容 类型为string
    }
}
