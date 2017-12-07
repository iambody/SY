package com.cgbsoft.lib.utils.tools;

import android.content.Context;

/**
 * @author chenlong
 * 乐享生活
 */
public class TrackingLifeDataStatistics {

    /**
     * 进入乐享生活页面
     *
     * @param context
     */
    public static void goLifeHomePage(Context context) {
        TrackingDataUtils.save(context, "1020001001", "");
    }

    /**
     * 点击私行家
     *
     * @param context
     */
    public static void homeClickAdviser(Context context) {
        TrackingDataUtils.save(context, "1020001011", "");
    }

    /**
     * 右上角消息入口
     *
     * @param context
     */
    public static void homeClickNews(Context context) {
        TrackingDataUtils.save(context, "1020001021", " ");
    }

}
