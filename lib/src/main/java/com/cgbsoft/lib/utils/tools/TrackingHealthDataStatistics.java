package com.cgbsoft.lib.utils.tools;

import android.content.Context;

/**
 * @author chenlong
 * 健康统计
 */
public class TrackingHealthDataStatistics {

    /**
     * 进入健康首页
     *
     * @param context
     */
    public static void goHealthHomePage(Context context) {
        TrackingDataUtils.save(context, "1030001001", "");
    }

    /**
     * 点击私行家
     *
     * @param context
     */
    public static void homeClickAdviser(Context context) {
        TrackingDataUtils.save(context, "1030001011", "关闭");
    }

    /**
     * 右上角消息入口
     *
     * @param context
     */
    public static void homeClickNews(Context context) {
        TrackingDataUtils.save(context, "1030001021", " ");
    }

    /**
     * tab页签点击
     * @param context
     */
    public static void homeClickTagFlag(Context context, String tagName) {
        TrackingDataUtils.save(context, "1030001031", tagName);
    }

    /**
     * 点击介绍滑动导航标签
     * @param context
     */
    public static void introduceClickFlag(Context context, String tagName) {
        TrackingDataUtils.save(context, "1030002011", tagName);
    }

    /**
     * 介绍标签左滑动
     *
     * @param context
     */
    public static void introduceLeftScroll(Context context) {
        TrackingDataUtils.save(context, "1030002021", "");
    }

    /**
     * 介绍标签右滑动
     *
     * @param context
     */
    public static void introduceRightScroll(Context context) {
        TrackingDataUtils.save(context, "1030002031", "");
    }

    /**
     * 进入项目标签
     *
     * @param context
     */
    public static void gotoProjectTagPage(Context context) {
        TrackingDataUtils.save(context, "1030010001", "");
    }

    /**
     * 项目上拉加载
     */
    public static void projectUpload(Context context) {
        TrackingDataUtils.save(context, "1030010011", "");
    }

    /**
     * 项目下拉刷新
     */
    public static void projectDownRefresh(Context context) {
        TrackingDataUtils.save(context, "1030010021", "");
    }

    /**
     * 项目列表图片点击
     */
    public static void projectListItemImage(Context context, String projectName) {
        TrackingDataUtils.save(context, "1030010031", projectName);
    }

    /**
     * 项目列表文字点击
     */
    public static void projectListItemText(Context context, String projectName) {
        TrackingDataUtils.save(context, "1030010041", projectName);
    }

    /**
     * 项目列表评价点击
     */
    public static void projectListItemEvaluate(Context context, String projectName) {
        TrackingDataUtils.save(context, "1030010051", projectName);
    }

    /**
     * 项目列表评价更多点击
     */
    public static void projectListItemEvaluateMore(Context context, String operateName) {
        TrackingDataUtils.save(context, "1030010061", operateName);
    }

    /**
     * 进入项目详情页
     */
    public static void gotoProjectDetailPage(Context context) {
        TrackingDataUtils.save(context, "1030011001", "");
    }

    /**
     * 左上角后退
     */
    public static void projectDetailLeftBack(Context context) {
        TrackingDataUtils.save(context, "1030011011", "");
    }

    /**
     * 右上角分享
     */
    public static void projectDetailRightShare(Context context) {
        TrackingDataUtils.save(context, "1030011021", "");
    }

    /**
     * 免费咨询左边返回
     */
    public static void freeConsultLeftBack(Context context) {
        TrackingDataUtils.save(context, "1030012011", "");
    }

    /**
     * 进入健康课堂
     */
    public static void gotoHealthCouresePage(Context context) {
        TrackingDataUtils.save(context, "1030020001", "");
    }

    /**
     * 健康课堂下拉刷新
     */
    public static void healthCoureseDownRefrush(Context context) {
        TrackingDataUtils.save(context, "1030020021", "");
    }

    /**
     * 健康课堂上拉加载更多
     */
    public static void healthCoureseUploadMore(Context context) {
        TrackingDataUtils.save(context, "1030020011", "");
    }

    /**
     * 点击健康堂课图片
     */
    public static void clickHealthCoureseItemImage(Context context, String discoverName) {
        TrackingDataUtils.save(context, "1030020031", discoverName);
    }

    /**
     * 点击健康堂课标题
     */
    public static void clickHealthCoureseItemTitle(Context context,String discoverName) {
        TrackingDataUtils.save(context, "1030020041", discoverName);
    }
}
