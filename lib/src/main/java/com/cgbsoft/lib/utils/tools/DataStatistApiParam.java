package com.cgbsoft.lib.utils.tools;

import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.BaseApplication;

import java.util.HashMap;

/**
 * @author chenlong on 16/10/13.
 */
public class DataStatistApiParam {

    public static void onVideoPlayHistoryToC() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2027");
        data3.put("act", "20217");
        data3.put("arg1", "历史");
        data3.put("arg2", AppManager.getUserInfo(BaseApplication.getContext()).getToC().getBindTeacher());
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }

    public static void onVideoPlayDownToC() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2027");
        data3.put("act", "20218");
        data3.put("arg1", "缓存");
        data3.put("arg2", AppManager.getUserInfo(BaseApplication.getContext()).getToC().getBindTeacher());
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }

    public static void onClickVideoToC(String videoName, String searizableName) {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2028");
        data3.put("act", "20224");
        data3.put("arg1", videoName);
        data3.put("arg2", AppManager.getUserInfo(BaseApplication.getContext()).getToC().getBindTeacher());
        data3.put("arg4", searizableName);
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }

    public static void onVideoSmootToC(String videoName) {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2029");
        data3.put("act", "20225");
        data3.put("arg1", "缩小");
        data3.put("arg2", AppManager.getUserInfo(BaseApplication.getContext()).getToC().getBindTeacher());
        data3.put("arg4", videoName);
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }

    public static void onVideoDetailDownLoadToC(String liveName) {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2029");
        data3.put("act", "20226");
        data3.put("arg1", "下载");
        data3.put("arg2", AppManager.getUserInfo(BaseApplication.getContext()).getToC().getBindTeacher());
        data3.put("arg4", liveName);
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }

    public static void onVideoShareToC(String liveName) {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2029");
        data3.put("act", "20227");
        data3.put("arg1", "分享");
        data3.put("arg2", AppManager.getUserInfo(BaseApplication.getContext()).getToC().getBindTeacher());
        data3.put("arg4", liveName);
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }

    public static void onClickLiveRoomHeadImageToC(String liveName) {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2031");
        data3.put("act", "20229");
        data3.put("arg1", "头像");
        data3.put("arg2", AppManager.getUserInfo(BaseApplication.getContext()).getToC().getBindTeacher());
        data3.put("arg4", liveName);
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }

    public static void onClickLiveCommentToC(String liveName) {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2031");
        data3.put("act", "20230");
        data3.put("arg1", "评论");
        data3.put("arg2", AppManager.getUserInfo(BaseApplication.getContext()).getToC().getBindTeacher());
        data3.put("arg4", liveName);
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }

    public static void onClickLivePDFToC(String liveName, String pdfName) {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2031");
        data3.put("act", "20231");
        data3.put("arg1", "PDF");
        data3.put("arg2", AppManager.getUserInfo(BaseApplication.getContext()).getToC().getBindTeacher());
        data3.put("arg4", liveName);
        data3.put("arg5", pdfName);
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }

    public static void onClickLiveRoomCloseToC(String liveName) {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2031");
        data3.put("act", "20232");
        data3.put("arg1", "关闭");
        data3.put("arg2", AppManager.getUserInfo(BaseApplication.getContext()).getToC().getBindTeacher());
        data3.put("arg4", liveName);
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }

    public static void onStatisToCMenuCallCustom() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2015");
        data3.put("act", "20074");
        data3.put("arg1", "一键呼叫");
        data3.put("arg2", AppManager.getUserInfo(BaseApplication.getContext()).getToC().getBindTeacher());
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }

    public static void onStatisToCMenuMessage() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2015");
        data3.put("act", "20075");
        data3.put("arg1", "短信");
        data3.put("arg2", AppManager.getUserInfo(BaseApplication.getContext()).getToC().getBindTeacher());
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }

    public static void onStatisToCMenuCallDuihua() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2015");
        data3.put("act", "20076");
        data3.put("arg1", "对话");
        data3.put("arg2", AppManager.getUserInfo(BaseApplication.getContext()).getToC().getBindTeacher());
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }

    public static void onStatisToCMenuZhibo() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2015");
        data3.put("act", "20077");
        data3.put("arg1", "直播");
        data3.put("arg2", AppManager.getUserInfo(BaseApplication.getContext()).getToC().getBindTeacher());
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }

    public static void onStatisToCMenuKefu() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2015");
        data3.put("act", "20078");
        data3.put("arg1", "客服");
        data3.put("arg2", AppManager.getUserInfo(BaseApplication.getContext()).getToC().getBindTeacher());
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }

    public static void onStatisToCViskTest() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2017");
        data3.put("act", "20085");
        data3.put("arg1", "重填");
        data3.put("arg2", AppManager.getUserInfo(BaseApplication.getContext()).getToC().getBindTeacher());
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }

    public static void onSwitchGesturePassword(String value) {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2021");
        data3.put("act", "20125");
        data3.put("arg1", "手势密码开关");
        data3.put("arg2", "手势密码开关");
        data3.put("arg2", AppManager.getUserInfo(BaseApplication.getContext()).getToC().getBindTeacher());
        data3.put("arg4", value);
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }

    public static void onModifyGesturePassword() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2021");
        data3.put("act", "20126");
        data3.put("arg1", "手势密码修改");
        data3.put("arg2", "手势密码修改");
        data3.put("arg2", AppManager.getUserInfo(BaseApplication.getContext()).getToC().getBindTeacher());
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }

    public static void onForgetGesturePassword() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2021");
        data3.put("act", "20127");
        data3.put("arg1", "忘记手势密码");
        data3.put("arg2", "忘记手势密码");
        data3.put("arg2", AppManager.getUserInfo(BaseApplication.getContext()).getToC().getBindTeacher());
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }

    public static void onStatisToCProductDetailMenu() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2012");
        data3.put("act", "20056");
        data3.put("arg1", "云键");
        data3.put("arg2", AppManager.getUserInfo(BaseApplication.getContext()).getToC().getBindTeacher());
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }

    public static void onClickLiveRoomHeadImageToB(String liveName) {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "1023");
        data3.put("act", "10106");
        data3.put("arg1", "头像");
//        data3.put("arg2", AppManager.getUserInfo(BaseApplication.getContext()).getToB().isColorCloud());
//        data3.put("arg3", AppManager.getUserInfo(BaseApplication.getContext()).getToB().getOrganizationName());
        data3.put("arg4", liveName);
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }

    /**
     * B云豆充值 充值云豆按钮点击
     */
    public static void Pay_B_BtClick() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "1034");
        data3.put("act", "10147");
        data3.put("arg1", "充值云豆");
//        data3.put("arg2", MApplication.getUser().getToB().isColorCloud());
//        //data3.put("arg3", MApplication.getUser().getToB().getOrganizationName());
//        DataStatisticsUtils.getInstance().push(context, data3,false);
    }

    /**
     * B云豆充值 充值金额快选按钮
     */
    public static void Pay_B_KuaiXuan(String zengjia) {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "1034");
        data3.put("act", "10148");
        data3.put("arg1", "选择金额");
//        data3.put("arg2", MApplication.getUser().getToB().isColorCloud());
//        //data3.put("arg3", MApplication.getUser().getToB().getOrganizationName());
//        data3.put("arg4", zengjia);
//        DataStatisticsUtils.getInstance().push(context, data3,false);
    }

    /**
     * B云豆充值 充值金额快选按钮
     */
    public static void Pay_B_Method(String payname) {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "1034");
        data3.put("act", "10149");
        data3.put("arg1", "支付方式");
//        data3.put("arg2", MApplication.getUser().getToB().isColorCloud());
//        //data3.put("arg3", MApplication.getUser().getToB().getOrganizationName());
//        data3.put("arg4", payname);
//        DataStatisticsUtils.getInstance().push(context, data3,false);
    }

    /**
     * B云豆充值 充值云豆按钮点击
     */
    public static void Pay_C_BtClick() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2037");
        data3.put("act", "20258");
        data3.put("arg1", "充值云豆");
//        data3.put("arg2", MApplication.getUser().getToC().getBindTeacher());
        DataStatisticsUtils.push(BaseApplication.getContext(), data3,false);
    }

    /**
     * B云豆充值 充值金额快选按钮
     */
    public static void Pay_C_KuaiXuan(String zengjia) {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2037");
        data3.put("act", "20259");
        data3.put("arg1", "选择金额");
//        data3.put("arg2", MApplication.getUser().getToC().getBindTeacher());
        data3.put("arg4", zengjia);
        DataStatisticsUtils.push(BaseApplication.getContext(), data3,false);
    }

    /**
     * B云豆充值 充值金额快选按钮
     */
    public static void Pay_C_Method(String payname) {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2037");
        data3.put("act", "20260");
        data3.put("arg1", "支付方式");
//        data3.put("arg2", MApplication.getUser().getToC().getBindTeacher());
        data3.put("arg4", payname);
        DataStatisticsUtils.push(BaseApplication.getContext(), data3,false);
    }


    public static void liveShareC(String title) {

    }

    public static void liveShareB(String title) {

    }

    public static void onClickLiveRoomCloseToB(String videoName) {

    }


    public static void onClickLivePDFToB(String vaName, String s) {

    }


    public static void onClickLiveCommentToB(String videoName) {

    }

}
