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
    public static void onStatisToCVideoDetailShareClick(String videoName) {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2029");
        data3.put("act", "20227");
        data3.put("arg1", "分享");
        data3.put("arg2", BaseApplication.BindAdviserState());
        data3.put("arg4", videoName);
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
        data3.put("act", "200394");
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
        data3.put("act", "200109");
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
        data3.put("act", "200040");
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
        data3.put("act", "200041");
        data3.put("arg1", "返回");
        data3.put("arg2", BaseApplication.BindAdviserState());
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, false);
    }


    /**
     * C端资讯分享到朋友圈
     * type 1标识早知道 2标识大视野
     */
    public static void onStatisToCShareInfOnCircle(String infTitle,String type){
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2016");
        data3.put("act", "200265");
        data3.put("arg1", "转发文章到朋友圈");
        data3.put("arg2", BaseApplication.BindAdviserState());
        data3.put("arg4", infTitle);
        data3.put("arg5", type);
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
     * 进入尚品页面
     */
    public static void intoElegantGoods() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2041");
        data3.put("act", "20279");
        data3.put("arg1", "进入生活家");
        DataStatisticsUtils.push(BaseApplication.getContext(), data3, true);
    }


    public static void onClickLiveRoomCloseToB(String videoName) {

    }


    public static void onClickLivePDFToB(String vaName, String s) {

    }


    public static void onClickLiveCommentToB(String videoName) {

    }
}
