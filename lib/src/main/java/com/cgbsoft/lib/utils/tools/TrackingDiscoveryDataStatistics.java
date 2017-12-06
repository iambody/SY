package com.cgbsoft.lib.utils.tools;

import android.content.Context;

/**
 * @author chenlong
 * 资讯统计
 */
public class TrackingDiscoveryDataStatistics {

//    /**
//     * 点击私行家
//     *
//     * @param context
//     */
//    public static void homeClickAdviser(Context context) {
//        TrackingDataUtils.save(context, "1030001011", "关闭");
//    }
//
//    /**
//     * 右上角消息入口
//     *
//     * @param context
//     */
//    public static void homeClickNews(Context context) {
//        TrackingDataUtils.save(context, "1030001021", " ");
//    }

    /**
     * 点击介绍标签
     * @param context
     */
    public static void discoveryClickFlag(Context context, String tagName) {
        TrackingDataUtils.save(context, "1010040011", tagName);
    }

    /**
     * 资讯标签左滑动
     *
     * @param context
     */
    public static void discoveryLeftScroll(Context context) {
        TrackingDataUtils.save(context, "1010040012", "");
    }

    /**
     * 资讯标签右滑动
     *
     * @param context
     */
    public static void discoveryRightScroll(Context context) {
        TrackingDataUtils.save(context, "1010040013", "");
    }

    /**
     * 资料列表点击
     *
     * @param context
     */
    public static void goDiscoveryDetailPage(Context context, String discoverType) {
        TrackingDataUtils.save(context, "1010040101", discoverType);
    }

    /**
     * 资讯上拉加载
     */
    public static void discoveryUpload(Context context) {
        TrackingDataUtils.save(context, "1010040103", "");
    }

    /**
     * 资讯下拉刷新
     */
    public static void discoveryDownRefresh(Context context) {
        TrackingDataUtils.save(context, "1010040102", "");
    }

    /**
     * 进入资讯详情
     */
    public static void gotoDiscoveryDetail(Context context, String discoveryTitle) {
        TrackingDataUtils.save(context, "1060001001", "");
    }

    /**
     * 资讯左上返回
     */
    public static void leftBack(Context context, String discoveryTitle) {
        TrackingDataUtils.save(context, "1060001011", "");
    }

    /**
     * 资讯右上分享
     */
    public static void rightShare(Context context, String discoveryTitle) {
        TrackingDataUtils.save(context, "1060001021", discoveryTitle);
    }

    /**
     * 视频劫持埋点
     */
    public static void videoUrlIntercept(Context context,String videoName) {
        TrackingDataUtils.save(context, "1065001991", videoName);
    }
}
