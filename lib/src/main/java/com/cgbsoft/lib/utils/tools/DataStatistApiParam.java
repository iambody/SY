package com.cgbsoft.lib.utils.tools;


import com.cgbsoft.lib.BaseApplication;

import java.util.HashMap;

/**
 * @author chenlong on 16/10/13.
 */
public class DataStatistApiParam{

/*    public static void onStatisToBStartLogin() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "1000");
        data3.put("act", "10121");
        data3.put("arg1", "登录");
        DataStatisticsUtils.push(BaseApplication.getContext(), data3);
    }*/

/*    public static void onStatisToBStartRegeist() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "1000");
        data3.put("act", "10122");
        data3.put("arg1", "注册");
        DataStatisticsUtils.push(BaseApplication.getContext(), data3);
    }*/

    /*public static void onStatisToBPhoto() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "1004");
        data3.put("act", "10025");
        data3.put("arg1", "头像");
        data3.put("arg2", MApplication.getUser().getToB().isColorCloud());
        data3.put("arg3", MApplication.getUser().getToB().getOrganizationName());
        DataStatisticsUtils.push(BaseApplication.getContext(), data3);
    }

    public static void onStatisToBYeji() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "1004");
        data3.put("act", "10026");
        data3.put("arg1", "业绩");
        data3.put("arg2", MApplication.getUser().getToB().isColorCloud());
        data3.put("arg3", MApplication.getUser().getToB().getOrganizationName());
        DataStatisticsUtils.push(BaseApplication.getContext(), data3);
    }

    public static void onStatisToBqiandao() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "1004");
        data3.put("act", "10027");
        data3.put("arg1", "签到");
        data3.put("arg2", MApplication.getUser().getToB().isColorCloud());
        data3.put("arg3", MApplication.getUser().getToB().getOrganizationName());
        DataStatisticsUtils.push(BaseApplication.getContext(), data3);
    }

    public static void onStatisToBRili() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "1004");
        data3.put("act", "10028");
        data3.put("arg1", "日历");
        data3.put("arg2", MApplication.getUser().getToB().isColorCloud());
        data3.put("arg3", MApplication.getUser().getToB().getOrganizationName());
        DataStatisticsUtils.push(BaseApplication.getContext(), data3);
    }

    public static void onStatisToBCustom() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "1004");
        data3.put("act", "10029");
        data3.put("arg1", "我的客户");
        data3.put("arg2", MApplication.getUser().getToB().isColorCloud());
        data3.put("arg3", MApplication.getUser().getToB().getOrganizationName());
        DataStatisticsUtils.push(BaseApplication.getContext(), data3);
    }

    public static void onStatisToBOrder() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "1004");
        data3.put("act", "10030");
        data3.put("arg1", "我的订单");
        data3.put("arg2", MApplication.getUser().getToB().isColorCloud());
        data3.put("arg3", MApplication.getUser().getToB().getOrganizationName());
        DataStatisticsUtils.push(BaseApplication.getContext(), data3);
    }

    public static void onStatisToBYundou() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "1004");
        data3.put("act", "10031");
        data3.put("arg1", "我的云豆");
        data3.put("arg2", MApplication.getUser().getToB().isColorCloud());
        data3.put("arg3", MApplication.getUser().getToB().getOrganizationName());
        DataStatisticsUtils.push(BaseApplication.getContext(), data3);
    }

    public static void onStatisToBBobao() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "1004");
        data3.put("act", "10032");
        data3.put("arg1", "播报");
        data3.put("arg2", MApplication.getUser().getToB().isColorCloud());
        data3.put("arg3", MApplication.getUser().getToB().getOrganizationName());
        DataStatisticsUtils.push(BaseApplication.getContext(), data3);
    }

    public static void onStatisToBTeam() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "1004");
        data3.put("act", "10033");
        data3.put("arg1", "我的团队");
        data3.put("arg2", MApplication.getUser().getToB().isColorCloud());
        data3.put("arg3", MApplication.getUser().getToB().getOrganizationName());
        DataStatisticsUtils.push(BaseApplication.getContext(), data3);
    }

    public static void onStatisToBTask() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "1004");
        data3.put("act", "10034");
        data3.put("arg1", "我的任务");
        data3.put("arg2", MApplication.getUser().getToB().isColorCloud());
        data3.put("arg3", MApplication.getUser().getToB().getOrganizationName());
        DataStatisticsUtils.push(BaseApplication.getContext(), data3);
    }

    public static void onStatisToBCollection() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "1004");
        data3.put("act", "10035");
        data3.put("arg1", "我的收藏");
        data3.put("arg2", MApplication.getUser().getToB().isColorCloud());
        data3.put("arg3", MApplication.getUser().getToB().getOrganizationName());
        DataStatisticsUtils.push(BaseApplication.getContext(), data3);
    }

    public static void onStatisToBCaifuCourse() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "1004");
        data3.put("act", "10036");
        data3.put("arg1", "财富讲堂");
        data3.put("arg2", MApplication.getUser().getToB().isColorCloud());
        data3.put("arg3", MApplication.getUser().getToB().getOrganizationName());
        DataStatisticsUtils.push(BaseApplication.getContext(), data3);
    }

    public static void onStatisToBMineCourse() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "1004");
        data3.put("act", "10037");
        data3.put("arg1", "我的课程");
        data3.put("arg2", MApplication.getUser().getToB().isColorCloud());
        data3.put("arg3", MApplication.getUser().getToB().getOrganizationName());
        DataStatisticsUtils.push(BaseApplication.getContext(), data3);
    }

    public static void onStatisToBSet() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "1004");
        data3.put("act", "10038");
        data3.put("arg1", "设置");
        data3.put("arg2", MApplication.getUser().getToB().isColorCloud());
        data3.put("arg3", MApplication.getUser().getToB().getOrganizationName());
        DataStatisticsUtils.push(BaseApplication.getContext(), data3);
    }

    public static void onStatisToBselectImage() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "1005");
        data3.put("act", "10039");
        data3.put("arg1", "选择照片");
        data3.put("arg2", MApplication.getUser().getToB().isColorCloud());
        data3.put("arg3", MApplication.getUser().getToB().getOrganizationName());
        DataStatisticsUtils.push(BaseApplication.getContext(), data3);
    }

    public static void onStatisToBselectCarme() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "1005");
        data3.put("act", "10040");
        data3.put("arg1", "拍照上传");
        data3.put("arg2", MApplication.getUser().getToB().isColorCloud());
        data3.put("arg3", MApplication.getUser().getToB().getOrganizationName());
        DataStatisticsUtils.push(BaseApplication.getContext(), data3);
    }

    public static void onStatisToBselectUpdateInfo() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "1005");
        data3.put("act", "10041");
        data3.put("arg1", "修改资料");
        data3.put("arg2", MApplication.getUser().getToB().isColorCloud());
        data3.put("arg3", MApplication.getUser().getToB().getOrganizationName());
        DataStatisticsUtils.push(BaseApplication.getContext(), data3);
    }

    public static void onStatisToBselectErweima() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "1005");
        data3.put("act", "10042");
        data3.put("arg1", "二维码");
        data3.put("arg2", MApplication.getUser().getToB().isColorCloud());
        data3.put("arg3", MApplication.getUser().getToB().getOrganizationName());
        DataStatisticsUtils.push(BaseApplication.getContext(), data3);
    }

    public static void onStatisToBselectCloudMind() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "1005");
        data3.put("act", "10043");
        data3.put("arg1", "云口令");
        data3.put("arg2", MApplication.getUser().getToB().isColorCloud());
        data3.put("arg3", MApplication.getUser().getToB().getOrganizationName());
        DataStatisticsUtils.push(BaseApplication.getContext(), data3);
    }

    public static void onStatisToBselectSaomiao() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "1005");
        data3.put("act", "10044");
        data3.put("arg1", "扫描");
        data3.put("arg2", MApplication.getUser().getToB().isColorCloud());
        data3.put("arg3", MApplication.getUser().getToB().getOrganizationName());
        DataStatisticsUtils.push(BaseApplication.getContext(), data3);
    }

    public static void onStatisToCTabMine() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2006");
        data3.put("act", "20013");
        data3.put("arg1", "我的");
        data3.put("arg3", MApplication.getUser().getToC().getBindTeacher());
        DataStatisticsUtils.push(BaseApplication.getContext(), data3);
    }

    public static void onStatisToCTabProduct() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2006");
        data3.put("act", "20014");
        data3.put("arg1", "产品");
        data3.put("arg3", MApplication.getUser().getToC().getBindTeacher());
        DataStatisticsUtils.push(BaseApplication.getContext(), data3);
    }

    public static void onStatisToCTabCloudKey() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2006");
        data3.put("act", "20015");
        data3.put("arg1", "云键");
        data3.put("arg3", MApplication.getUser().getToC().getBindTeacher());
        DataStatisticsUtils.push(BaseApplication.getContext(), data3);
    }

    public static void onStatisToCTabDiscover() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2006");
        data3.put("act", "20016");
        data3.put("arg1", "发现");
        data3.put("arg3", MApplication.getUser().getToC().getBindTeacher());
        DataStatisticsUtils.push(BaseApplication.getContext(), data3);
    }

    public static void onStatisToCTabClub() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2006");
        data3.put("act", "20017");
        data3.put("arg1", "club");
        data3.put("arg3", MApplication.getUser().getToC().getBindTeacher());
        DataStatisticsUtils.push(BaseApplication.getContext(), data3);
    }

    public static void onStatisToCMenuCallCustom() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2015");
        data3.put("act", "20074");
        data3.put("arg1", "一键呼叫");
        data3.put("arg3", MApplication.getUser().getToC().getBindTeacher());
        DataStatisticsUtils.push(BaseApplication.getContext(), data3);
    }

    public static void onStatisToCMenuMessage() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2015");
        data3.put("act", "20075");
        data3.put("arg1", "短信");
        data3.put("arg3", MApplication.getUser().getToC().getBindTeacher());
        DataStatisticsUtils.push(BaseApplication.getContext(), data3);
    }

    public static void onStatisToCMenuCallDuihua() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2015");
        data3.put("act", "20076");
        data3.put("arg1", "对话");
        data3.put("arg3", MApplication.getUser().getToC().getBindTeacher());
        DataStatisticsUtils.push(BaseApplication.getContext(), data3);
    }

    public static void onStatisToCMenuZhibo() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2015");
        data3.put("act", "20077");
        data3.put("arg1", "直播");
        data3.put("arg3", MApplication.getUser().getToC().getBindTeacher());
        DataStatisticsUtils.push(BaseApplication.getContext(), data3);
    }

    public static void onStatisToCMenuKefu() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2015");
        data3.put("act", "20078");
        data3.put("arg1", "客服");
        data3.put("arg3", MApplication.getUser().getToC().getBindTeacher());
        DataStatisticsUtils.push(BaseApplication.getContext(), data3);
    }

    public static void onStatisToCViskTest() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2017");
        data3.put("act", "20085");
        data3.put("arg1", "重填");
        data3.put("arg3", MApplication.getUser().getToC().getBindTeacher());
        DataStatisticsUtils.push(BaseApplication.getContext(), data3);
    }*/

/*    public static void onStaticToCNowStart() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2001");
        data3.put("act", "20001");
        data3.put("arg1", "立即启动");
        DataStatisticsUtils.push(BaseApplication.getContext(), data3);
    }*/

    public static void onStaticToCLoginBack() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2002");
        data3.put("act", "20006");
        data3.put("arg1", "返回");
        DataStatisticsUtils.push(BaseApplication.getContext(), data3);
    }

    public static void onStaticToCRegeistBack() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2003");
        data3.put("act", "20008");
        data3.put("arg1", "返回");
        DataStatisticsUtils.push(BaseApplication.getContext(), data3);
    }

    public static void onStaticToCFindPasswordBack() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2004");
        data3.put("act", "20010");
        data3.put("arg1", "返回");
        DataStatisticsUtils.push(BaseApplication.getContext(), data3);
    }

    public static void onStaticToCSetPasswordNext() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2005");
        data3.put("act", "20011");
        data3.put("arg1", "下一步");
        DataStatisticsUtils.push(BaseApplication.getContext(), data3);
    }

    public static void onStaticToCSetPasswordBack() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2005");
        data3.put("act", "20012");
        data3.put("arg1", "返回");
        DataStatisticsUtils.push(BaseApplication.getContext(), data3);
    }

    /*public static void onStatisToCProductDetailMenu() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "2012");
        data3.put("act", "20056");
        data3.put("arg1", "云键");
        data3.put("arg3", MApplication.getUser().getToC().getBindTeacher());
        DataStatisticsUtils.push(BaseApplication.getContext(), data3);
    }

    public static void onStatisToBDiscoveryBanner(String id, String name) {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "1013");
        data3.put("act", "10087");
        data3.put("arg1", "Banner");
        data3.put("arg2", MApplication.getUser().getToB().isColorCloud());
        data3.put("arg3", MApplication.getUser().getToB().getOrganizationName());
        data3.put("arg4", id);
        data3.put("arg3", name);
        DataStatisticsUtils.push(BaseApplication.getContext(), data3);
    }*/


    /**
     * B云豆充值 充值云豆按钮点击
     */
    public static void Pay_B_BtClick() {
        HashMap<String, String> data3 = new HashMap<>();
        data3.put("grp", "1034");
        data3.put("act", "10147");
        data3.put("arg1", "充值云豆");
//        data3.put("arg2", MApplication.getUser().getToB().isColorCloud());
//        data3.put("arg3", MApplication.getUser().getToB().getOrganizationName());
//        DataStatisticsUtils.getInstance().push(context, data3);
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
//        data3.put("arg3", MApplication.getUser().getToB().getOrganizationName());
//        data3.put("arg4", zengjia);
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
//        data3.put("arg3", MApplication.getUser().getToB().getOrganizationName());
//        data3.put("arg4", payname);
//        DataStatisticsUtils.getInstance().push(context, data3);
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
        DataStatisticsUtils.push(BaseApplication.getContext(), data3);
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
        DataStatisticsUtils.push(BaseApplication.getContext(), data3);
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
        DataStatisticsUtils.push(BaseApplication.getContext(), data3);
    }

}
