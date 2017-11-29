package com.cgbsoft.lib.utils.tools;

import android.content.Context;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/11/29-19:25
 */
public class TrackingDataManger {
    public static void gohome(Context context){
        TrackingDataUtils.save(context,"1001001001","");
    }

    /**
     * 打开头像
     * @param context
     */
    public static void homePersonOpen(Context context){
        TrackingDataUtils.save(context,"1001001001","打开");
    }

    /**
     * 关闭头像
     * @param context
     */
    public static void homePersonClose(Context context){
        TrackingDataUtils.save(context,"1001001001","关闭");
    }
    /**
     * 右上角消息入口
     * @param context
     */
    public static void homeNew(Context context){
        TrackingDataUtils.save(context,"1001001021"," ");
    }
    /**
     * 焦点 banner
     * @param context
     */
    public static void homeBannerFocus(Context context){
        TrackingDataUtils.save(context,"1001001031"," ");
    }
    /**
     * 左滑 banner
     * @param context
     */
    public static void homeBannerleft(Context context,String name){
        TrackingDataUtils.save(context,"1001001032",name);
    }
    /**
     * 右划 banner
     * @param context
     */
    public static void homeBannerRight(Context context,String name){
        TrackingDataUtils.save(context,"1001001033",name);
    }

    /**
     * 尊享私行会员入口
     * @param context
     */
    public static void homeMember(Context context ){
        TrackingDataUtils.save(context,"1001001041","");
    }

    /**
     * 云豆任务
     * @param context
     */
    public static void homeTask(Context context ){
        TrackingDataUtils.save(context,"1001001041","");
    }
    /**
     * 推荐好友
     */
    public static void homeRecommend(Context context ){
        TrackingDataUtils.save(context,"1001001061","");
    }
    /**
     * 贵宾卡充值
     */
    public static void homeRecharge(Context context ){
        TrackingDataUtils.save(context,"1001001071","");
    }
    /**
      * 云豆充值
     */
    public static void homeBeanRecharge(Context context ){
        TrackingDataUtils.save(context,"1001001081","");
    }
    /**
     * 活动中心
     */
    public static void homeActivis(Context context ){
        TrackingDataUtils.save(context,"1001001091","");
    }
    /**
     * 运营左滑
     */
    public static void homeOperateLeft(Context context ){
        TrackingDataUtils.save(context,"1001001102","");
    }
    /**
     * 运营右滑
     */
    public static void homeOperateRight(Context context ){
        TrackingDataUtils.save(context,"1001001103","");
    }
    /**
     * 弹层服务码文字点击
     */
    public static void homeCode(Context context ){
        TrackingDataUtils.save(context,"1001002011","");
    }
    /**
     * 弹层问候语文字点击
     */
    public static void homeGreetings(Context context ){
        TrackingDataUtils.save(context,"1001002021","");
    }
    /**
     * 弹层电话点击
     */
    public static void homePhone(Context context ){
        TrackingDataUtils.save(context,"1001002031","");
    }
    /**
     * 弹层短信点击
     */
    public static void homeNote(Context context ){
        TrackingDataUtils.save(context,"1001002041","");
    }
    /**
     * 弹层对话点击
     */
    public static void homedialogue (Context context ){
        TrackingDataUtils.save(context,"1001002051","");
    }
    /**
     *  tab首页点击
     */
    public static void tabHome (Context context ){
        TrackingDataUtils.save(context,"1002001011","");
    }
    /**
     *  tab尊享私行点击
     */
    public static void tabPrivateBanck (Context context ){
        TrackingDataUtils.save(context,"1002001021","");
    }
    /**
     *  tab乐享生活点击
     */
    public static void tabLife (Context context ){
        TrackingDataUtils.save(context,"1002001031","");
    }
    /**
     *  tab乐享生活点击
     */
    public static void tabHealth (Context context ){
        TrackingDataUtils.save(context,"1002001041","");
    }
    /**
     *  tab我的点击
     */
    public static void tabCenter (Context context ){
        TrackingDataUtils.save(context,"1002001051","");
    }
    /**
     * 云豆任务页面进入
     */
    public static void taskIn (Context context ){
        TrackingDataUtils.save(context,"1005001001","");
    }
    /**
     * 云豆任务页面左上角后退
     */
    public static void taskBack (Context context ){
        TrackingDataUtils.save(context,"1005001011","");
    }
    /**
     * 云豆任务页面Item点击
     */
    public static void taskItem (Context context,String taskName){
        TrackingDataUtils.save(context,"1005001021",taskName);
    }

}
