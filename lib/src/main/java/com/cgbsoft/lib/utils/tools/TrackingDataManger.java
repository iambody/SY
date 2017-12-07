package com.cgbsoft.lib.utils.tools;

import android.content.Context;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/11/29-19:25
 */
public class TrackingDataManger {
    public static void gohome(Context context) {
        TrackingDataUtils.save(context, "1001001001", "");
    }

    /**
     * 打开头像
     *
     * @param context
     */
    public static void homePersonOpen(Context context) {
        TrackingDataUtils.save(context, "1001001001", "打开");
    }

    /**
     * 关闭头像
     *
     * @param context
     */
    public static void homePersonClose(Context context) {
        TrackingDataUtils.save(context, "1001001001", "关闭");
    }

    /**
     * 右上角消息入口
     *
     * @param context
     */
    public static void homeNew(Context context) {
        TrackingDataUtils.save(context, "1001001021", " ");
    }

    /**
     * 焦点 banner
     *
     * @param context
     */
    public static void homeBannerFocus(Context context,String title) {
        TrackingDataUtils.save(context, "1001001031", title);
    }

    /**
     * 左滑 banner
     *
     * @param context
     */
    public static void homeBannerleft(Context context, String name) {
        TrackingDataUtils.save(context, "1001001032", name);
    }

    /**
     * 右划 banner
     *
     * @param context
     */
    public static void homeBannerRight(Context context, String name) {
        TrackingDataUtils.save(context, "1001001033", name);
    }

    /**
     * 尊享私行会员入口
     *
     * @param context
     */
    public static void homeMember(Context context) {
        TrackingDataUtils.save(context, "1001001041", "");
    }

    /**
     * 云豆任务
     *
     * @param context
     */
    public static void homeTask(Context context) {
        TrackingDataUtils.save(context, "1001001041", "");
    }

    /**
     * 推荐好友
     */
    public static void homeRecommend(Context context) {
        TrackingDataUtils.save(context, "1001001061", "");
    }

    /**
     * 贵宾卡充值
     */
    public static void homeRecharge(Context context) {
        TrackingDataUtils.save(context, "1001001071", "");
    }

    /**
     * 云豆充值
     */
    public static void homeBeanRecharge(Context context) {
        TrackingDataUtils.save(context, "1001001081", "");
    }

    /**
     * 活动中心
     */
    public static void homeActivis(Context context) {
        TrackingDataUtils.save(context, "1001001091", "");
    }

    /**
     * 运营左滑
     */
    public static void homeOperateLeft(Context context) {
        TrackingDataUtils.save(context, "1001001102", "");
    }

    /**
     * 运营左滑
     */
    public static void homeOperateItemClick(Context context, String operateName) {
        TrackingDataUtils.save(context, "1001001051", operateName);
    }

    /**
     * 运营右滑
     */
    public static void homeOperateRight(Context context) {
        TrackingDataUtils.save(context, "1001001103", "");
    }

    /**
     * 弹层服务码文字点击
     */
    public static void homeCode(Context context) {
        TrackingDataUtils.save(context, "1001002011", "");
    }

    /**
     * 弹层问候语文字点击
     */
    public static void homeGreetings(Context context) {
        TrackingDataUtils.save(context, "1001002021", "");
    }

    /**
     * 弹层电话点击
     */
    public static void homePhone(Context context) {
        TrackingDataUtils.save(context, "1001002031", "");
    }

    /**
     * 弹层短信点击
     */
    public static void homeNote(Context context) {
        TrackingDataUtils.save(context, "1001002041", "");
    }

    /**
     * 弹层对话点击
     */
    public static void homedialogue(Context context) {
        TrackingDataUtils.save(context, "1001002051", "");
    }

    /**
     * tab首页点击
     */
    public static void tabHome(Context context) {
        TrackingDataUtils.save(context, "1002001011", "");
    }

    /**
     * tab尊享私行点击
     */
    public static void tabPrivateBanck(Context context) {
        TrackingDataUtils.save(context, "1002001021", "");
    }

    /**
     * tab乐享生活点击
     */
    public static void tabLife(Context context) {
        TrackingDataUtils.save(context, "1002001031", "");
    }

    /**
     * tab乐享生活点击
     */
    public static void tabHealth(Context context) {
        TrackingDataUtils.save(context, "1002001041", "");
    }

    /**
     * tab我的点击
     */
    public static void tabCenter(Context context) {
        TrackingDataUtils.save(context, "1002001051", "");
    }

    /**
     * 云豆任务页面进入
     */
    public static void taskIn(Context context) {
        TrackingDataUtils.save(context, "1005001001", "");
    }

    /**
     * 云豆任务页面左上角后退
     */
    public static void taskBack(Context context) {
        TrackingDataUtils.save(context, "1005001011", "");
    }

    /**
     * 云豆任务页面Item点击
     */
    public static void taskItem(Context context, String taskName) {
        TrackingDataUtils.save(context, "1005001021", taskName);
    }

    /**
     * 我的消息进入
     */
    public static void imIn(Context context) {
        TrackingDataUtils.save(context, "1003001001", "");
    }

    /**
     * 我的消息返回按钮
     */
    public static void imBack(Context context) {
        TrackingDataUtils.save(context, "1003001011", "");
    }

    /**
     * 我的消息搜索文本
     */
    public static void imSearch(Context context) {
        TrackingDataUtils.save(context, "1003001021", "");
    }

    /**
     * 我的消息消息list
     */
    public static void imListItem(Context context, String itemMeg) {
        TrackingDataUtils.save(context, "1003001031", itemMeg);
    }

    /**
     * 云豆充值页面进入
     */
    public static void rechargeIn(Context context) {
        TrackingDataUtils.save(context, "1005003001", "");
    }

    /**
     * 云豆充值退出
     */
    public static void rechargeBack(Context context) {
        TrackingDataUtils.save(context, "1005003011", "");
    }

    /**
     * 云豆充值右上角贵宾卡
     */
    public static void rechargeGuest(Context context) {
        TrackingDataUtils.save(context, "1005003021", "");
    }

    /**
     * 云豆充值数量文本框
     */
    public static void rechargeNumberEd(Context context) {
        TrackingDataUtils.save(context, "1005003031", "");
    }

    /**
     * 云豆充值1000云豆点击
     */
    public static void rechargeThousand(Context context) {
        TrackingDataUtils.save(context, "1005003041", "");
    }

    /**
     * 云豆充值5000云豆点击
     */
    public static void rechargeFiveThousand(Context context) {
        TrackingDataUtils.save(context, "1005003051", "");
    }

    /**
     * 云豆充值10000云豆点击
     */
    public static void rechargeTenThousand(Context context) {
        TrackingDataUtils.save(context, "1005003061", "");
    }

    /**
     * 云豆充值+10云豆点击
     */
    public static void rechargeAddTen(Context context) {
        TrackingDataUtils.save(context, "1005003071", "");
    }

    /**
     * 云豆充值+100云豆点击
     */
    public static void rechargeAddHundred(Context context) {
        TrackingDataUtils.save(context, "1005003081", "");
    }

    /**
     * 云豆充值+1000云豆点击
     */
    public static void rechargeAddThousand(Context context) {
        TrackingDataUtils.save(context, "1005003091", "");
    }

    /**
     * 云豆充值 支付方式-银联卡 按钮
     */
    public static void rechargeCard(Context context) {
        TrackingDataUtils.save(context, "1005003101", "");
    }

    /**
     * 云豆充值 支付按钮
     */
    public static void rechargePay(Context context) {
        TrackingDataUtils.save(context, "1005003111", "");
    }

    /**
     * 支付页面进入
     */
    public static void payIn(Context context) {
        TrackingDataUtils.save(context, "1005004001", "");
    }

    /**
     * 支付页面 后退
     */
    public static void payBack(Context context) {
        TrackingDataUtils.save(context, "1005004011", "");
    }

    /**
     * 支付页面 后退
     */
    public static void payAffirm(Context context) {
        TrackingDataUtils.save(context, "1005004021", "");
    }

    /**
     * 视频详情进入
     */
    public static void videoDetailEnter(Context context) {
        TrackingDataUtils.save(context, "1065001001", "视频详情");
    }

    /**
     * 视频详情左上角返回键
     */
    public static void videoDetailTopBack(Context context) {
        TrackingDataUtils.save(context, "1065001011", "");
    }

    /**
     * 视频详情 右上角分享
     */
    public static void videoDetailRightShare(Context context, String title) {
        TrackingDataUtils.save(context, "1065001021", BStrUtils.isEmpty(title) ? "视频详情" : title);
    }

    /**
     * 视频详情  只听不看按钮
     */
    public static void videoDetailOnlyListener(Context context) {
        TrackingDataUtils.save(context, "1065001031", "");
    }

    /**
     * 视频详情  缓存按钮按钮
     */
    public static void videoDetailCache(Context context) {
        TrackingDataUtils.save(context, "1065001041", "");
    }

    /**
     * 视频详情  我来说一说文本框
     */
    public static void videoDetailToCommont(Context context) {
        TrackingDataUtils.save(context, "1065001051", "");
    }

    /**
     * 视频详情  评论按钮
     */
    public static void videoDetailRecommend(Context context) {
        TrackingDataUtils.save(context, "1065001061", "");
    }

    /**
     * 视频详情  评论取消
     */
    public static void videoDetailRecommendCancle(Context context) {
        TrackingDataUtils.save(context, "1065002011", "");
    }

    /**
     * 视频详情  发送
     */
    public static void videoDetailRecommendSend(Context context) {
        TrackingDataUtils.save(context, "1065002021", "");
    }

    /**
     * 视频首页导航入口
     */
    public static void videoSchoolIn(Context context) {
        TrackingDataUtils.save(context, "1065002021", "");
    }

    /**
     * 视频首页导航 左滑
     */
    public static void videoSchoolLeftScroll(Context context) {
        TrackingDataUtils.save(context, "1010070012", "");
    }

    /**
     * 视频首页导航 左滑
     */
    public static void videoSchoolRightScroll(Context context) {
        TrackingDataUtils.save(context, "1010070013", "");
    }

    /**
     * 视频首页导航  点击item
     */
    public static void videoSchoolItem(Context context, String title) {
        TrackingDataUtils.save(context, "1010070101", BStrUtils.isEmpty(title)?"视频":title);
    }

    /**
     * 视频首页导航 刷新
     */
    public static void videoSchoolRefreash(Context context) {
        TrackingDataUtils.save(context, "1010070102", "");
    }

    /**
     * 视频首页导航 刷新
     */
    public static void videoSchoolMore(Context context) {
        TrackingDataUtils.save(context, "1010070103", "");
    }
    /**
     * 分享进入
     */
    public static void shareIn(Context context,String title) {
        TrackingDataUtils.save(context, "1050001001", BStrUtils.isEmpty(title)?"分享":title);
    }
    /**
     * 分享关闭
     */
    public static void shareClose(Context context  ) {
        TrackingDataUtils.save(context, "1050001011", "");
    }
    /**
     * 分享点击
     */
    public static void shareClick(Context context ,String shareTitle ) {
        TrackingDataUtils.save(context, "1050001021", shareTitle);
    }
    /**
     * 分享点击 朋友圈
     */
    public static void shareClickCricle(Context context   ) {
        TrackingDataUtils.save(context, "1050001031", "");
    }
}
