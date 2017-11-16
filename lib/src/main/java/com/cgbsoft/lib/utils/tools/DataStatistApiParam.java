package com.cgbsoft.lib.utils.tools;

import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.BaseApplication;

import java.util.HashMap;

/**
 * @author chenlong on 16/10/13.
 */
public class DataStatistApiParam {

    //    public static void onVideoPlayHistoryToC() {
//        HashMap<String, String> data3 = new HashMap<>();
//        data3.put("grp", "1000");
//        data3.put("act", "10121");
//        data3.put("arg1", "登录");
//        DataStatisticsUtils.push(BaseApplication.getContext(), data3, false);
//    }
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

    public static void onStatisToBStartRegeist() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "1000");
        data3.put("act", "10122");
        data3.put("arg1", "注册");
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


    public static void onStatisToBPhoto() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "1004");
        data3.put("act", "10025");
        data3.put("arg1", "头像");
//        data3.put("arg2", MApplication.getUser().getToB().isColorCloud());
//        //data3.put("arg3", MApplication.getUser().getToB().getOrganizationName());
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, false);
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

    public static void onStatisToBYeji() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "1004");
        data3.put("act", "10026");
        data3.put("arg1", "业绩");
//        data3.put("arg2", MApplication.getUser().getToB().isColorCloud());
//        //data3.put("arg3", MApplication.getUser().getToB().getOrganizationName());
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, false);
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

    public static void onStatisToBqiandao() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "1004");
        data3.put("act", "10027");
        data3.put("arg1", "签到");
//        data3.put("arg2", MApplication.getUser().getToB().isColorCloud());
//        //data3.put("arg3", MApplication.getUser().getToB().getOrganizationName());
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, false);
    }

    public static void onStatisToBRili() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "1004");
        data3.put("act", "10028");
        data3.put("arg1", "日历");
//        data3.put("arg2", MApplication.getUser().getToB().isColorCloud());
//        //data3.put("arg3", MApplication.getUser().getToB().getOrganizationName());
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, false);
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

    public static void onStatisToBCustom() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "1004");
        data3.put("act", "10029");
        data3.put("arg1", "我的客户");
//        data3.put("arg2", MApplication.getUser().getToB().isColorCloud());
//        //data3.put("arg3", MApplication.getUser().getToB().getOrganizationName());
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, false);
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

    public static void onStatisToBOrder() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "1004");
        data3.put("act", "10030");
        data3.put("arg1", "我的订单");
//        data3.put("arg2", MApplication.getUser().getToB().isColorCloud());
//        //data3.put("arg3", MApplication.getUser().getToB().getOrganizationName());
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, false);
    }

    public static void onStatisToBYundou() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "1004");
        data3.put("act", "10031");
        data3.put("arg1", "我的云豆");
//        data3.put("arg2", MApplication.getUser().getToB().isColorCloud());
//        //data3.put("arg3", MApplication.getUser().getToB().getOrganizationName());
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, false);
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

    public static void onStatisToBBobao() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "1004");
        data3.put("act", "10032");
        data3.put("arg1", "播报");
//        data3.put("arg2", MApplication.getUser().getToB().isColorCloud());
//        //data3.put("arg3", MApplication.getUser().getToB().getOrganizationName());
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, false);
    }

    public static void onStatisToCMenuCallCustom() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2015");
        data3.put("act", "20074");
        data3.put("arg1", "一键呼叫");
        data3.put("arg2", AppManager.getUserInfo(BaseApplication.getContext()).getToC().getBindTeacher());
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }

    public static void onStatisToBTeam() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "1004");
        data3.put("act", "10033");
        data3.put("arg1", "我的团队");
//        data3.put("arg2", MApplication.getUser().getToB().isColorCloud());
//        //data3.put("arg3", MApplication.getUser().getToB().getOrganizationName());
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, false);
    }

    public static void onStatisToBTask() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "1004");
        data3.put("act", "10034");
        data3.put("arg1", "我的任务");
//        data3.put("arg2", MApplication.getUser().getToB().isColorCloud());
//        //data3.put("arg3", MApplication.getUser().getToB().getOrganizationName());
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, false);
    }

    public static void onStatisToBCollection() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "1004");
        data3.put("act", "10035");
        data3.put("arg1", "我的收藏");
//        data3.put("arg2", MApplication.getUser().getToB().isColorCloud());
//        //data3.put("arg3", MApplication.getUser().getToB().getOrganizationName());
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, false);
    }

    public static void onStatisToBCaifuCourse() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "1004");
        data3.put("act", "10036");
        data3.put("arg1", "财富讲堂");
//        data3.put("arg2", MApplication.getUser().getToB().isColorCloud());
//        //data3.put("arg3", MApplication.getUser().getToB().getOrganizationName());
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, false);
    }

    public static void onStatisToBMineCourse() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "1004");
        data3.put("act", "10037");
        data3.put("arg1", "我的课程");
//        data3.put("arg2", MApplication.getUser().getToB().isColorCloud());
//        //data3.put("arg3", MApplication.getUser().getToB().getOrganizationName());
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, false);
    }

    public static void onStatisToBSet() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "1004");
        data3.put("act", "10038");
        data3.put("arg1", "设置");
//        data3.put("arg2", MApplication.getUser().getToB().isColorCloud());
//        //data3.put("arg3", MApplication.getUser().getToB().getOrganizationName());
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, false);
    }

    public static void onStatisToBselectImage() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "1005");
        data3.put("act", "10039");
        data3.put("arg1", "选择照片");
//        data3.put("arg2", MApplication.getUser().getToB().isColorCloud());
//        //data3.put("arg3", MApplication.getUser().getToB().getOrganizationName());
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, false);
    }

    public static void onStatisToBselectCarme() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "1005");
        data3.put("act", "10040");
        data3.put("arg1", "拍照上传");
//        data3.put("arg2", MApplication.getUser().getToB().isColorCloud());
//        //data3.put("arg3", MApplication.getUser().getToB().getOrganizationName());
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, false);
    }

    public static void onStatisToBselectUpdateInfo() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "1005");
        data3.put("act", "10041");
        data3.put("arg1", "修改资料");
//        data3.put("arg2", MApplication.getUser().getToB().isColorCloud());
//        //data3.put("arg3", MApplication.getUser().getToB().getOrganizationName());
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, false);
    }

    public static void onStatisToBselectErweima() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "1005");
        data3.put("act", "10042");
        data3.put("arg1", "二维码");
//        data3.put("arg2", MApplication.getUser().getToB().isColorCloud());
//        //data3.put("arg3", MApplication.getUser().getToB().getOrganizationName());
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, false);
    }

    public static void onStatisToBselectCloudMind() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "1005");
        data3.put("act", "10043");
        data3.put("arg1", "云口令");
//        data3.put("arg2", MApplication.getUser().getToB().isColorCloud());
//        //data3.put("arg3", MApplication.getUser().getToB().getOrganizationName());
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, false);
    }

    public static void onStatisToBselectSaomiao() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "1005");
        data3.put("act", "10044");
        data3.put("arg1", "扫描");
//        data3.put("arg2", MApplication.getUser().getToB().isColorCloud());
//        //data3.put("arg3", MApplication.getUser().getToB().getOrganizationName());
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, false);
    }

    public static void onStatisToCTabMine() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2006");
        data3.put("act", "20013");
        data3.put("arg1", "我的");
//        //data3.put("arg3", MApplication.getUser().getToC().getBindTeacher());
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, false);
    }

    public static void onStatisToCTabProduct() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2006");
        data3.put("act", "20014");
        data3.put("arg1", "产品");
//        //data3.put("arg3", MApplication.getUser().getToC().getBindTeacher());
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, false);
    }

    public static void onStatisToCTabCloudKey() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2006");
        data3.put("act", "20015");
        data3.put("arg1", "云键");
        //data3.put("arg3", MApplication.getUser().getToC().getBindTeacher());
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, false);
    }

    public static void onStatisToCTabDiscover() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2006");
        data3.put("act", "20016");
        data3.put("arg1", "发现");
        //data3.put("arg3", MApplication.getUser().getToC().getBindTeacher());
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, false);
    }

    public static void onStatisToCTabClub() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2006");
        data3.put("act", "20017");
        data3.put("arg1", "club");
        //data3.put("arg3", MApplication.getUser().getToC().getBindTeacher());
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, false);
    }


    public static void onStatisToCMenuMessage() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2015");
        data3.put("act", "20075");
        data3.put("arg1", "短信");
        //data3.put("arg3", MApplication.getUser().getToC().getBindTeacher());
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, false);
    }

    public static void onStatisToCMenuCallDuihua() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2015");
        data3.put("act", "20076");
        data3.put("arg1", "对话");
        //data3.put("arg3", MApplication.getUser().getToC().getBindTeacher());
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, false);
    }

    public static void onStatisToCMenuZhibo() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2015");
        data3.put("act", "20077");
        data3.put("arg1", "直播");
        //data3.put("arg3", MApplication.getUser().getToC().getBindTeacher());
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, false);
    }

    public static void onStatisToCMenuKefu() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2015");
        data3.put("act", "20078");
        data3.put("arg1", "客服");
        //data3.put("arg3", MApplication.getUser().getToC().getBindTeacher());
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, false);
    }

    public static void onStatisToCViskTest() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2017");
        data3.put("act", "20085");
        data3.put("arg1", "重填");
        //data3.put("arg3", MApplication.getUser().getToC().getBindTeacher());
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, false);
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

    public static void onStaticToCNowStart() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2001");
        data3.put("act", "20001");
        data3.put("arg1", "立即启动");
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, false);
    }

    public static void onStaticToCLoginBack() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2002");
        data3.put("act", "20006");
        data3.put("arg1", "返回");
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, false);
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

    //点击注册按钮
    public static void onStaticToCRegeistClick() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2003");
        data3.put("act", "20007");
        data3.put("arg1", "注册");
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }

    public static void onStaticToCRegeistBack() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2003");
        data3.put("act", "20008");
        data3.put("arg1", "返回");
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, false);
    }

    /**
     * 忘记密码 点击下一步
     */
    public static void onStaticToCFindPasswordNext() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2004");
        data3.put("act", "20009");
        data3.put("arg1", "返回");
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, false);
    }

    /**
     * 忘记密码 点击返回
     */
    public static void onStaticToCFindPasswordBack() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2004");
        data3.put("act", "20010");
        data3.put("arg1", "返回");
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, false);
    }

    /**
     * C端 设置密码 点击下一步
     */
    public static void onStaticToCSetPasswordNext() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2005");
        data3.put("act", "20011");
        data3.put("arg1", "下一步");
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, false);
    }

    /**
     * C端 设置密码 点击返回
     */
    public static void onStaticToCSetPasswordBack() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2005");
        data3.put("act", "20012");
        data3.put("arg1", "返回");
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }

    public static void onStatisToCProductDetailMenu() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2012");
        data3.put("act", "20056");
        data3.put("arg1", "云键");
        //data3.put("arg3", MApplication.getUser().getToC().getBindTeacher());
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, false);
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

    public static void onStatisToBDiscoveryBanner(String id, String name) {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "1013");
        data3.put("act", "10087");
        data3.put("arg1", "Banner");
//        data3.put("arg2", MApplication.getUser().getToB().isColorCloud());
        //data3.put("arg3", MApplication.getUser().getToB().getOrganizationName());
        data3.put("arg4", id);
        //data3.put("arg3", name);
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, false);
    }

    //c端登录 点击登录按钮
    public static void onStatisToCLoginClick() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2002");
        data3.put("act", "20002");
        data3.put("arg1", "登录");

//        data3.put("arg3", "登录");
//        data3.put("arg4", "登录");
//        data3.put("arg5", "登录");
        //data3.put("arg3", MApplication.getUser().getToC().getBindTeacher());
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }

    //c端登录 点击注册按钮
    public static void onStatisToCRegistClick() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2002");
        data3.put("act", "20003");
        data3.put("arg1", "新用户");
        //data3.put("arg3", MApplication.getUser().getToC().getBindTeacher());
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }

    //c端登录 点击忘记密码按钮
    public static void onStatisToForgetPwdClick() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2002");
        data3.put("act", "20004");
        data3.put("arg1", "忘记密码");
        //data3.put("arg3", MApplication.getUser().getToC().getBindTeacher());
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, false);
    }

    //c端登录  点击微信登录
    public static void onStatisToWXLoginClick() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2002");
        data3.put("act", "20005");
        data3.put("arg1", "微信");
        //data3.put("arg3", MApplication.getUser().getToC().getBindTeacher());
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }


    /**
     * C端点击观看历史按钮点击
     */
    public static void onStatisToCLookSchool() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2027");
        data3.put("act", "20217");
        data3.put("arg1", "历史");
        data3.put("arg2", BaseApplication.BindAdviserState());
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, false);
    }

    /**
     * C端点击缓存按钮点击
     */
    public static void onStatisToCLookHistory() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2027");
        data3.put("act", "20218");
        data3.put("arg1", "缓存");
        data3.put("arg2", BaseApplication.BindAdviserState());
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, false);
    }

    /**
     * C端点击视频详情中的缓存按钮
     */
    public static void onStatisToCVideoDetailDownLoadClick(String videoName) {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2029");
        data3.put("act", "20226");
        data3.put("arg1", "下载");
        data3.put("arg2", BaseApplication.BindAdviserState());
        data3.put("arg4", videoName);
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, false);
    }

    /**
     * C端点击视频详情中的分享按钮点击事件
     */
    public static void onStatisToCVideoDetailShareClick(String videoName, String videoType) {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2029");
        data3.put("act", "20227");
        data3.put("arg1", "分享");
        data3.put("arg2", BaseApplication.BindAdviserState());
        data3.put("arg4", videoName);
        data3.put("arg5", videoType);
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, false);
    }

    /**
     * C端点击视频详情中的缩小点击事件
     */
    public static void onStatisToCVideoDetailZoomClick(String videoName) {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2029");
        data3.put("act", "20225");
        data3.put("arg1", "缩小");
        data3.put("arg2", BaseApplication.BindAdviserState());
        data3.put("arg4", videoName);
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, false);

    }

    /**
     * C端点击视频详情视频关闭的事件
     */
    public static void onStatisToCVideoDetailClose(String videoName, long time) {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2028");
        data3.put("act", "20225");
        data3.put("arg1", "视频关闭");
        data3.put("arg2", BaseApplication.BindAdviserState());
        data3.put("arg4", videoName);
        data3.put("arg5", time + "");
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, false);

    }

    /**
     * C端点击视频详情视频关闭的事件
     */
    public static void onStatisToCLookVideoDetail(String videoName) {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2028");
        data3.put("act", "20224");
        data3.put("arg1", "视频查看");
        data3.put("arg2", BaseApplication.BindAdviserState());
        data3.put("arg4", videoName);

        DataStatisticsUtils.push(BaseApplication.getContext(), data3, false);

    }


    /**
     * C产品列表点击顶部各个Tab页标签
     */
    public static void onStatisToCProductTabTag(String seriseName) {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2011");
        data3.put("act", "20394");
        data3.put("arg1", "产品系列");
        data3.put("arg2", BaseApplication.BindAdviserState());
        data3.put("arg4", seriseName);

        DataStatisticsUtils.push(BaseApplication.getContext(), data3, false);

    }

    /**
     * C端产品点击产品顶部搜索的事件
     */
    public static void onStatisToCProductItemClick(String productId, String productName, boolean isHot) {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2011");
        data3.put("act", "20109");
        data3.put("arg1", "产品详情");
        data3.put("arg2", BaseApplication.BindAdviserState());
        data3.put("arg4", productId);
        data3.put("arg5", productName);
        data3.put("arg6", isHot ? "热销产品" : "非热销产品");
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }

    /**
     * C端产品列表点击item的记录事件
     */
    public static void onStatisToCProductSearchClick() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2011");
        data3.put("act", "20040");
        data3.put("arg1", "搜索");
        data3.put("arg2", BaseApplication.BindAdviserState());
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }

    /**
     * C端产品详情的返回事件
     */
    public static void onStatisToCProductDetailBack() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2012");
        data3.put("act", "20041");
        data3.put("arg1", "返回");
        data3.put("arg2", BaseApplication.BindAdviserState());
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, false);
    }


    /**
     * C端资讯分享到朋友圈
     * type 1标识早知道 2标识大视野
     */
    public static void onStatisToCShareInfOnCircle(String infTitle, String typeName) {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2016");
        data3.put("act", "20265");
        data3.put("arg1", "转发文章到朋友圈");
        data3.put("arg2", BaseApplication.BindAdviserState());
        data3.put("arg4", infTitle);
        data3.put("arg5", typeName);
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
     * B直播分享
     */
    public static void liveShareC(String sortType) {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2031");
        data3.put("act", "20257");
        data3.put("arg1", "分享");
        data3.put("arg2", BaseApplication.BindAdviserState());
        data3.put("arg4", sortType);
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }

    /**
     * C直播分享
     */
    public static void liveShareB(String sortType) {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "1023");
        data3.put("act", "10141");
        data3.put("arg1", "分享");
//        data3.put("arg2", MApplication.getUser().getToB().isColorCloud());
//        data3.put("arg3", MApplication.getUser().getToB().getOrganizationName());
        data3.put("arg4", sortType);
//        DataStatisticsUtils.getInstance().push(context, data3);
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
        data3.put("arg2", BaseApplication.BindAdviserState());
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, false);
    }

    /**
     * B云豆充值 充值金额快选按钮
     */
    public static void Pay_C_KuaiXuan(String zengjia) {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2037");
        data3.put("act", "20259");
        data3.put("arg1", "选择金额");
        data3.put("arg2", BaseApplication.BindAdviserState());
        data3.put("arg4", zengjia);
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, false);
    }

    /**
     * B云豆充值 充值金额快选按钮
     */
    public static void Pay_C_Method(String payname) {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2037");
        data3.put("act", "20260");
        data3.put("arg1", "支付方式");
        data3.put("arg2", BaseApplication.BindAdviserState());
        data3.put("arg4", payname);
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, false);
    }

    /**
     * 进入生活家页面
     */
    public static void intoElegantLiving() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2041");
        data3.put("act", "20279");
        data3.put("arg1", "进入生活家");
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }

    /**
     * 点击尚品按钮
     */
    public static void clickElegantGoodsButton(String tabName) {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2041");
        data3.put("act", "20280");
        data3.put("arg1", "tab点击");
        data3.put("arg3", tabName);
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }

    /**
     * 进入尚品页面
     */
    public static void intoElegantGoods() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2042");
        data3.put("act", "20284");
        data3.put("arg1", "进入尚品");
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }

    /**
     * 生活家页面点击私行家
     */
    public static void clickFPInElegantPage() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2041");
        data3.put("act", "20281");
        data3.put("arg1", "私行家");
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }

    /**
     * 生活家页面点击消息中心
     */
    public static void clickMsgCenterInElegantPage() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2041");
        data3.put("act", "20282");
        data3.put("arg1", "消息中心");
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }

    /**
     * 生活家页面点击专题图，参数-专题名称
     */
    public static void clickElegantLivingBanner(String bannerName) {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2041");
        data3.put("act", "20283");
        data3.put("arg1", "专题图");
        data3.put("arg3", bannerName);
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }

    /**
     * 热门物品点击事件
     */
    public static void clickHotProduct(String productName) {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2042");
        data3.put("act", "20285");
        data3.put("arg1", "热门物品");
        data3.put("arg3", productName);
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }

    /**
     * 全新上架物品点击事件
     */
    public static void clickNormalProduct(String productName) {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2042");
        data3.put("act", "20286");
        data3.put("arg1", "全新上架物品");
        data3.put("arg3", productName);
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }

    /**
     * 尚品类型点击事件
     */
    public static void clickCategory(String categoryName) {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2042");
        data3.put("act", "20285");
        data3.put("arg1", "尚品类型");
        data3.put("arg3", categoryName);
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }
    /**
     * 商品点击事件
     */
    public static void clickProduct(String productName,String categoryName,String hotOrNew) {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2042");
        data3.put("act", "20286");
        data3.put("arg1", "商品");
        data3.put("arg3", productName);
        data3.put("arg4", categoryName);
        data3.put("arg5", hotOrNew);
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }
    /**
     * 尚品分类的点击事件
     */
    public static void clickCategoryEat(String categoryName) {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2043");
        data3.put("act", "20287");
        data3.put("arg1", "尚品好吃");
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }

    /**
     * 进入修改登录密码页面
     */
    public static void changePsdPage() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2072");
        data3.put("act", "20381");
        data3.put("arg1", "修改密码");
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }

    /**
     * 进入修改登录密码页面提交按钮
     */
    public static void changePsdPageSubmit() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2072");
        data3.put("act", "20382");
        data3.put("arg1", "确定");
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }

    /**
     * 签到
     */
    public static void signInEveryDay() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2078");
        data3.put("act", "20397");
        data3.put("arg1", "签到");
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }

    /**
     * 设置
     */
    public static void intoSettingPage() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2081");
        data3.put("act", "20408");
        data3.put("arg1", "设置");
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }

    /**
     * 点击推荐给好友
     */
    public static void recommendFriend() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2081");
        data3.put("act", "20410");
        data3.put("arg1", "推荐给朋友");
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }

    /**
     * 点击帮助和反馈
     */
    public static void clickFeedBack() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2081");
        data3.put("act", "20411");
        data3.put("arg1", "帮助与反馈");
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }

    /**
     * 点击关于本应用
     */
    public static void aboutApp() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2081");
        data3.put("act", "20412");
        data3.put("arg1", "关于本应用");
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }

    /**
     * 点击手势密码
     */
    public static void clickGesture() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2081");
        data3.put("act", "20413");
        data3.put("arg1", "手势密码");
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }

    /**
     * 点击修改手势密码
     */
    public static void clickChangeGesture() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2081");
        data3.put("act", "20416");
        data3.put("arg1", "修改手势密码");
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }

    /**
     * 点击修改登录密码
     */
    public static void clickChangePsd() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2081");
        data3.put("act", "20414");
        data3.put("arg1", "修改登录密码");
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }


    /**
     * 首页
     */
    public static void gohome() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2073");
        data3.put("act", "20383");
        data3.put("arg1", "首页");
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }

    /**
     * 首页直播点击
     *
     * @param
     */
    public static void homeliveclick() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2073");
        data3.put("act", "20384");
        data3.put("arg1", "直播");
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }

    /**
     * 首页运营位点击
     *
     * @param
     */
    public static void operateBannerClick(String s) {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2073");
        data3.put("act", "20385");
        data3.put("arg3", s);
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }
    /**
     * 首页banner点击
     *
     * @param
     */

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    public static void  HomeBannerClick(String bannerTitle) {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2075");
        data3.put("act", "20390");
        data3.put("arg1", "banner");
        data3.put("arg4",BStrUtils.isEmpty(bannerTitle)?"首页banner":bannerTitle);
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }
    /**
     * 手势密码开关
     *
     * @param
     */
    public static void switchGestureClick(String switchStr) {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2040");
        data3.put("act", "20271");
        data3.put("arg1", "手势密码开关");
        data3.put("arg3", switchStr);
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }

    /**
     * 健康监测
     *
     * @param
     */
    public static void operateHealthCheckClick() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2059");
        data3.put("act", "20341");
        data3.put("arg1", "监测");
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }

    /**
     * 健康医疗
     *
     * @param
     */
    public static void operateHealthMedcialClick() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2059");
        data3.put("act", "20342");
        data3.put("arg1", "医疗");
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }


    /**
     * 消息中心
     *
     * @param
     */
    public static void operateMessageCenterClick() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2059");
        data3.put("act", "20343");
        data3.put("arg1", "消息中心");
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }

    /**
     * 进入健康推荐
     *
     * @param
     */
    public static void operateHealthIntroduceClick(String pageName) {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2060");
        data3.put("act", "20344");
        data3.put("arg1", "健康推荐");
        data3.put("arg3", pageName);
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }

    /**
     * 我的健康
     *
     * @param
     */
    public static void operateMineHealthClick() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2063");
        data3.put("act", "20357");
        data3.put("arg1", "我的健康");
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }

    /**
     * 财富通用小人
     */
    public static void operatePrivateBankPersonalClick() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2064");
        data3.put("act", "20359");
        data3.put("arg1", "小人");
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }


    /**
     * 财富通用消息
     */
    public static void operatePrivateBankMessageClick() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2065");
        data3.put("act", "20360");
        data3.put("arg1", "消息");
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }

    /**
     * 财富通用搜索
     */
    public static void operatePrivateBankSearchClick() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2066");
        data3.put("act", "20361");
        data3.put("arg1", "搜索");
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }

    /**
     * 真实搜索
     */
    public static void operatePrivateBankRealSearchClick(String searchContent) {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2066");
        data3.put("act", "20362");
        data3.put("arg1", "搜索详情");
        data3.put("arg3", searchContent);
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }

    /**
     * 资讯进入banner
     *
     * @param bannerTitle
     */
    public static void operatePrivateBankDiscoverClick(String bannerTitle) {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2067");
        data3.put("act", "20363");
        data3.put("arg1", "banner");
        data3.put("arg3", bannerTitle);
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }

    /**
     * banner刷新
     */
    public static void operatePrivateBankDiscoverUpRefrushClick() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2067");
        data3.put("act", "20365");
        data3.put("arg1", "上方下拉刷新");
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }

    /**
     * 下拉刷新
     */
    public static void operatePrivateBankDiscoverDownLoadClick() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2067");
        data3.put("act", "20366");
        data3.put("arg1", "下方上拉刷新");
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }

    /**
     * 全部订单
     */
    public static void operateMineOrderAllClick() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2055");
        data3.put("act", "20328");
        data3.put("arg1", "我的订单");
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }

    /**
     * 待发货订单
     */
    public static void operateWaitSendClick() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2056");
        data3.put("act", "20332");
        data3.put("arg1", "待发货");
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }

    /**
     * 待收货订单
     */
    public static void operateWaitReceiveClick() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2057");
        data3.put("act", "20333");
        data3.put("arg1", "待收货");
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }

    /**
     * 进入财富专题
     */
    public static void operateDiscoverDetailClick() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2068");
        data3.put("act", "20367");
        data3.put("arg1", "进入财富专题");
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }

    /**
     * 我的活动
     */
    public static void operateMineActivityClick() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2079");
        data3.put("act", "20400");
        data3.put("arg1", "我的活动");
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }

    /**
     * 我的卡券
     */
    public static void operateMineCardQuanClick() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2080");
        data3.put("act", "20403");
        data3.put("arg1", "卡券");
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }

    /**
     * 投资账号
     */
    public static void operateInvestorAccountClick() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2081");
        data3.put("act", "20409");
        data3.put("arg1", "投资账号");
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }

    public static void RechargeButton() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2070");
        data3.put("act", "20376");
        data3.put("arg1", "充值云豆");
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }


    public static void RechargeNum(String num) {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2069");
        data3.put("act", "20373");
        data3.put("arg1", num);
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }


    public static void AddAddress() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2053");
        data3.put("act", "20323");
        data3.put("arg1", "填写地址");
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }


    public static void AddAddressBack() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2053");
        data3.put("act", "20324");
        data3.put("arg1", "返回");
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }


    public static void editAddress() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2054");
        data3.put("act", "20325");
        data3.put("arg1", "编辑地址");
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }


    public static void editAddressBack() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2054");
        data3.put("act", "20326");
        data3.put("arg1", "返回");
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }

    /**
     * 尊享私行中产品/资讯/学院tab点击埋点
     */
    public static void honourPBitemClick(String tabName) {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2068");
        data3.put("act", "20368");
        data3.put("arg1", "tab点击");
        data3.put("arg3", tabName);
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }

    /**
     * 永享健康中介绍/检测/医疗tab点击埋点
     */
    public static void everHealthClick(String tabName) {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2059");
        data3.put("act", "20340");
        data3.put("arg1", "tab点击");
        data3.put("arg3", tabName);
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }

    public static void editAddressSave() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2054");
        data3.put("act", "20327");
        data3.put("arg1", "保存");
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }

    /**
     * 点击短信
     */
    public static void homeClickNote() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2015");
        data3.put("act", "20075");
        data3.put("arg1", "短信");
        data3.put("arg2", BaseApplication.BindAdviserState());
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }

    /**
     * 点击对话
     */
    public static void homeClickDuiHua() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2015");
        data3.put("act", "20076");
        data3.put("arg1", "对话");
        data3.put("arg2", BaseApplication.BindAdviserState());
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }

    /**
     * 点击客服
     */
    public static void homeClickKeFu() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2015");
        data3.put("act", "20078");
        data3.put("arg1", "客服");
        data3.put("arg2", BaseApplication.BindAdviserState());
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }

    /**
     * 点击首页小人
     */
    public static void homeCliclIv() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2064");
        data3.put("act", "20359");
        data3.put("arg1", "小人");
        data3.put("arg2", BaseApplication.BindAdviserState());
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }

    /**
     * 首页消息
     */
    public static void homeClickNew() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2065");
        data3.put("act", "20360");
        data3.put("arg1", "消息");
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }


    /**
     * 进入资讯详情
     *
     * @param
     */
    public static void operatePrivateBankDiscoverDetailClick(String title, String categoryType) {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2067");
        data3.put("act", "20364");
        data3.put("arg1", "文章");
        data3.put("arg3", title);
        data3.put("arg4", categoryType);
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }

    /**
     * 修改手势密码
     */
    public static void realModifyGesturePassword() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2040");
        data3.put("act", "20272");
        data3.put("arg1", "修改手势密码");
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }

    /**
     * 忘记手势密码
     */
    public static void forgetGesturePassword() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2040");
        data3.put("act", "20273");
        data3.put("arg1", "忘记手势密码");
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }

    /**
     * 消息免打扰
     */
    public static void operateMessageFreeAccessClick() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2081");
        data3.put("act", "20415");
        data3.put("arg1", "消息免打扰");
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }

    /**
     * 首页点击直播
     */
    public static void homeLiveClick() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2073");
        data3.put("act", "20384");
        data3.put("arg1", "直播");
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }

    /**
     * 首页点击消息
     * @param
     */
    public static void homeNewClick() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2065");
        data3.put("act", "20360");
        data3.put("arg1", "消息");
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }


    public static void productLogin() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2068");
        data3.put("act", "20367");
        data3.put("arg1", "登录后查看");
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }

    /**
     * 视频详情
     * @param
     */
    public static void openVideoDetailActivityClick(String videoName, String videoType) {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2085");
        data3.put("act", "20426");
        data3.put("arg1", "进入视频");
        data3.put("arg4", videoName);
        data3.put("arg5", videoType);
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }


    /**
     * 视频详情
     * @param
     */
    public static void openHealthCourseActivityClick(String courseName) {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2060");
        data3.put("act", "20428");
        data3.put("arg1", "进入健康课堂");
        data3.put("arg4", courseName);
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }

    /**
     * 视频详情
     * @param
     */
    public static void openShareHealthCourseActivityClick(String courseName) {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2060");
        data3.put("act", "20430");
        data3.put("arg1", "分享健康课堂");
        data3.put("arg4", courseName);
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }


    /**
     * 活体检测结果
     * @param
     */
    public static void livingBodyCodeResult(String faceCode) {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2050");
        data3.put("act", "20503");
        data3.put("arg1", "活体检测结果");
        data3.put("arg2", faceCode);
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }

    /**
     * 证件夹点击
     * @param from
     */
    public static void cardCollect(String from){
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2050");
        data3.put("act", "20500");
        data3.put("arg1", "点击证件夹");
        data3.put("arg2", from);
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }

    /**
     * 证件夹列表点击
     * @param code
     */
    public static void cardCollectClick(String code){
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2050");
        data3.put("act", "20501");
        data3.put("arg1", "点击证件列表");
        data3.put("arg2", code);
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }

    /**
     * 证件夹列表点击
     */
    public static void cardCollectPlus(){
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2050");
        data3.put("act", "20502");
        data3.put("arg1", "点击加号");
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }

    /**
     * 证件夹列表点击
     */
    public static void otherCardUpload(String code){
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2050");
        data3.put("act", "20504");
        data3.put("arg1", "详情非身份证上传");
        data3.put("arg2",code);
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }

    /**
     * 敏感操作验身
     */
    public static void sensitiveBodyExam(String code,String isSuccess,String type){
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2050");
        data3.put("act", "20505");
        data3.put("arg1", "敏感操作验身");
        data3.put("arg2",code);
        data3.put("arg3",isSuccess);
        data3.put("type",type);
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }

    /**
     * 点击关联资产
     */
    public static void mineAssectClick(String stateStr){
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2050");
        data3.put("act", "20506");
        data3.put("arg1", "我的页面按钮点击");
        data3.put("arg2",stateStr);
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }

    /**
     *
     * @param
     */
    public static void mineAssectGroup(){
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2050");
        data3.put("act", "20507");
        data3.put("arg1", "资产组合");
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }

    /**
     *
     * @param
     */
    public static void investmentCalendar(){
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2050");
        data3.put("act", "20508");
        data3.put("arg1", "投资日历");
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }

    /**
     *
     * @param
     */
    public static void dataManager(){
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2050");
        data3.put("act", "20509");
        data3.put("arg1", "资料管理");
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }









    public static void onClickLiveRoomCloseToB(String videoName) {

    }


    public static void onClickLivePDFToB(String vaName, String s) {

    }


    public static void onClickLiveCommentToB(String videoName) {

    }
}
