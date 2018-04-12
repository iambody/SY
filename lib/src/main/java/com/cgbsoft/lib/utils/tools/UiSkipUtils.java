package com.cgbsoft.lib.utils.tools;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.BaseApplication;
import com.cgbsoft.lib.base.webview.CwebNetConfig;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.widget.dialog.DefaultDialog;
import com.cgbsoft.privatefund.bean.product.PublicFundInf;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * desc  activity等ui跳转的工具类
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 17/4/7-18:39
 */
public class UiSkipUtils {
    /**
     * @param packageContext
     * @param action         含操作的Intent
     * @Description: 隐式启动, 跳转
     */
    public static void startActivityIntentSafe(Context packageContext,
                                               Intent action) {
        // Verify it resolves
        PackageManager packageManager = packageContext.getPackageManager();
        List activities = packageManager.queryIntentActivities(action,
                PackageManager.MATCH_DEFAULT_ONLY);
        boolean isIntentSafe = activities.size() > 0;

        if (isIntentSafe) {
            packageContext.startActivity(action);
        }

    }

    /**
     * @param packageContext from,一般传XXXActivity.this
     * @param cls            to,一般传XXXActivity.class
     * @Description: 跳转
     */
    public static void toNextActivity(Context packageContext, Class<?> cls) {
        Intent i = new Intent(packageContext, cls);
        packageContext.startActivity(i);
    }


    /**
     * @param packageContext
     * @param cls
     * @param keyvalues      需要传进去的String参数{{key1,values},{key2,value2}...}
     * @Description: 跳转, 带参数的方法;需要其它的数据类型,再继续重载吧
     */
    public static void toNextActivityWithParamsMap(Context packageContext, Class<?> cls,
                                                   HashMap<String, String> keyvalues) {
        Intent i = new Intent(packageContext, cls);
        Set<Map.Entry<String, String>> set = keyvalues.entrySet();
        Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry<String, String>) iterator.next();
            i.putExtra(entry.getKey(), entry.getValue());
        }
        packageContext.startActivity(i);
    }

    /**
     * 直接处理intent
     *
     * @param packageContext
     * @param intent
     */

    public static void toNextActivityWithIntent(Context packageContext, Intent intent) {
        packageContext.startActivity(intent);
    }

    /**
     * 跳转到公募基金开户页面的公用方法
     * H5
     *
     * @param activity
     */
    public static void toPublicFundRegist(Activity activity) {

        PublicFundInf publicFundInf = AppManager.getPublicFundInf(activity.getApplicationContext());
        String fundinf = publicFundInf.getCustNo();//客户号 空=》未开户；非空=》开户
        if (BStrUtils.isEmpty(fundinf) && (BStrUtils.isEmpty(publicFundInf.getIsHaveCustBankAcct()) || "0".equals(publicFundInf.getIsHaveCustBankAcct())) && BStrUtils.isEmpty(publicFundInf.getCustRisk())) {//未开户
            //没风险测评=》跳转到公共的页面
//            DefaultDialog dialog = new DefaultDialog(activity, "您还未开户，马上去开户吧～", "取消", "确定") {
//                @Override
//                public void left() {
//                    dismiss();
//                }
//
//                @Override
//                public void right() {

            //没开户=》跳转到开户页面ton
            NavigationUtils.gotoWebActivity(activity, CwebNetConfig.publicFundRegistUrl, "公募基金开户", false);
//                    dismiss();
//                }
//            };
//            dialog.show();

        } else if (!BStrUtils.isEmpty(fundinf) && (BStrUtils.isEmpty(publicFundInf.getIsHaveCustBankAcct()) || "0".equals(publicFundInf.getIsHaveCustBankAcct()))) {
//
//            DefaultDialog dialog = new DefaultDialog(activity, "您还未绑卡，马上去绑卡吧～", "取消", "确定") {
//                @Override
//                public void left() {
//                    dismiss();
//                }
//
//                @Override
//                public void right() {
//                    //没绑定银行卡=》跳转到绑定银行卡页面
//                    String bankParam = new Gson().toJson(publicFundInf);
//                    HashMap<String, Object> map = new HashMap<>();
//                    map.put("tag_parameter", bankParam);
//                    NavigationUtils.startActivityByRouter(activity, RouteConfig.GOTO_PUBLIC_FUND_BIND_BANK_CARD, map);
//                    dismiss();
//
//                }
//            };
//            dialog.show();
            //没绑定银行卡=》跳转到绑定银行卡页面
            String bankParam = new Gson().toJson(publicFundInf);
            HashMap<String, Object> map = new HashMap<>();
            map.put("tag_parameter", bankParam);
            NavigationUtils.startActivityByRouter(activity, RouteConfig.GOTO_PUBLIC_FUND_BIND_BANK_CARD, map);
        } else if (!BStrUtils.isEmpty(fundinf) && "1".equals(publicFundInf.getIsHaveCustBankAcct()) && BStrUtils.isEmpty(publicFundInf.getCustRisk())) {
//            //没风险测评=》跳转到公共的页面
//            DefaultDialog dialog = new DefaultDialog(activity, "您还未进行风险测评，马上去开展测评吧～", "取消", "确定") {
//                @Override
//                public void left() {
//                    dismiss();
//                }
//
//                @Override
//                public void right() {
//                    //去风险测评
//                    UiSkipUtils.gotoPublicFundRisk(activity);
//                    dismiss();
//
//                }
//            };
//            dialog.show();
            //去风险测评
            UiSkipUtils.gotoPublicFundRisk(activity);
        }

    }

    /**
     * 原生跳转到公募基金申购页面
     */
    public static void toBuyPublicFundFromNative(Activity activity, String fundCode, String fundName, String fundType, String risklevel) {
        //需要先判断是否注册绑卡
        PublicFundInf publicFundInf = AppManager.getPublicFundInf(activity.getApplicationContext());
        String fundinf = publicFundInf.getCustNo();//客户号 空=》未开户；非空=》开户

        HashMap<String, Object> ffmaps = new HashMap<>();
        ffmaps.put("tag_fund_code", fundCode);
        ffmaps.put("tag_fund_name", fundName);
        ffmaps.put("tag_fund_type", fundType);
        ffmaps.put("tag_fund_risk_level", risklevel);
        ((BaseApplication) activity.getApplication()).setPublicBuyMaps(ffmaps);



        if (BStrUtils.isEmpty(fundinf) && (BStrUtils.isEmpty(publicFundInf.getIsHaveCustBankAcct()) || "0".equals(publicFundInf.getIsHaveCustBankAcct())) && BStrUtils.isEmpty(publicFundInf.getCustRisk())) {//未开户
            //没开户=》跳转到开户页面ton
            DefaultDialog dialog = new DefaultDialog(activity, "您还未开户，马上去开户吧～", "取消", "确定") {
                @Override
                public void left() {
                    dismiss();
                }

                @Override
                public void right() {
                    //没开户=》跳转到开户页面ton
                    NavigationUtils.gotoWebActivity(activity, CwebNetConfig.publicFundRegistUrl, "公募基金开户", false);
                    dismiss();
                }
            };
            dialog.show();
            //没开户=》跳转到开户页面ton
//            NavigationUtils.gotoWebActivity(activity, CwebNetConfig.publicFundRegistUrl, "公募基金开户", false);
        } else if (!BStrUtils.isEmpty(fundinf) && (BStrUtils.isEmpty(publicFundInf.getIsHaveCustBankAcct()) || "0".equals(publicFundInf.getIsHaveCustBankAcct()))) {
//            //没绑定银行卡=》跳转到绑定银行卡页面


            DefaultDialog dialog = new DefaultDialog(activity, "您还未绑卡，马上去绑卡吧～", "取消", "确定") {
                @Override
                public void left() {
                    dismiss();
                }

                @Override
                public void right() {
                    //没绑定银行卡=》跳转到绑定银行卡页面
                    String bankParam = new Gson().toJson(publicFundInf);
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("tag_parameter", bankParam);
                    NavigationUtils.startActivityByRouter(activity, RouteConfig.GOTO_PUBLIC_FUND_BIND_BANK_CARD, map);
                    dismiss();
                }
            };
            dialog.show();
            //没绑定银行卡=》跳转到绑定银行卡页面
//            String bankParam = new Gson().toJson(publicFundInf);
//            HashMap<String, Object> map = new HashMap<>();
//            map.put("tag_parameter", bankParam);
//            NavigationUtils.startActivityByRouter(activity, RouteConfig.GOTO_PUBLIC_FUND_BIND_BANK_CARD, map);
        } else if (!BStrUtils.isEmpty(fundinf) && "1".equals(publicFundInf.getIsHaveCustBankAcct()) && BStrUtils.isEmpty(publicFundInf.getCustRisk())) {
//            //没风险测评=》跳转到公共的页面
            DefaultDialog dialog = new DefaultDialog(activity, "您还未进行风险测评，马上去开展测评吧～", "取消", "确定") {
                @Override
                public void left() {
                    dismiss();
                }

                @Override
                public void right() {
                    //去风险测评
                    UiSkipUtils.gotoPublicFundRisk(activity);
                    dismiss();
                }
            };
            dialog.show();
            //去风险测评
//            UiSkipUtils.gotoPublicFundRisk(activity);
        } else if (!BStrUtils.isEmpty(fundinf) && "1".equals(publicFundInf.getIsHaveCustBankAcct()) && !BStrUtils.isEmpty(publicFundInf.getCustRisk())) {
            //开过户并且已经完成绑卡 跳转到数据里面
            // 开过户绑过卡风险测评过后 在跳转到申购之前 需要进行 风险的匹配检测   不匹配时候弹框提示 点击确认风险后就跳转到申购页面
            //判断风险等级
            if (!isMatchRisk(activity, risklevel)) {
                buyRiskShow(activity, fundCode, fundName, fundType, risklevel);
                return;
            }

            HashMap<String, Object> maps = new HashMap<>();
            maps.put("tag_fund_code", fundCode);
            maps.put("tag_fund_name", fundName);
            maps.put("tag_fund_type", fundType);
            maps.put("tag_fund_risk_level", risklevel);
            ((BaseApplication) activity.getApplication()).setPublicBuyMaps(maps);
            NavigationUtils.startActivityByRouter(activity, RouteConfig.GOTO_PUBLIC_FUND_BUY, maps);


        }
    }

    /**
     * 跳转到公募基金赎回页面
     */
    public static void gotoRedeemFund(Activity activity, String action) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("tag_param", action);
        NavigationUtils.startActivityByRouter(activity, RouteConfig.GOTO_PUBLIC_FUND_REDEMPTION, map);

    }

    /**
     * 跳转到风险测评
     */
    public static void gotoPublicFundRisk(Activity activity) {
        NavigationUtils.gotoWebActivity(activity, CwebNetConfig.publicFundRiskUrl + "?custno=" + AppManager.getPublicFundInf(activity.getApplicationContext()).getCustNo(), "公募基金开户", false);

    }

    /**
     * 赎回成功之后需要跳转到一个H5网页（赎回结果页）
     * pageType(0 私享宝) allMoney(赎回份额/卖出金额) appsheetserialno confirmeddate operdate opertime redeemrefunddate transactiondate
     */
    public static void gotoRedeemResult(Activity activity, String allMoney, String serialNo,boolean isWallet) {
        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("pageType", "2");
        paramMap.put("serialNo", serialNo);
        if(isWallet){ // 钱包
            paramMap.put("wallet", "1");
            paramMap.put("allMoney", allMoney);
            NavigationUtils.gotoWebActivity(activity, getUrl(CwebNetConfig.publicFundRedeemResult, paramMap), "交易结果", false);
        }else {
            paramMap.put("allShare", allMoney);
            paramMap.put("wallet", "0");
            NavigationUtils.gotoWebActivity(activity, getUrl(CwebNetConfig.publicFundBuyOrSell, paramMap), "交易结果", false);
        }
    }

    /**
     * 盈泰钱包赎回结果页
     *
     */
    /**
     * @param buyOrBuy 1:买入,2:卖出
     * @param fungType 基金类型  0:FOF型基金,1:货币基金,2:QDll基金,3:股票型，债券型，混合型，指数型基金
     * @param allMoney 买入钱数
     * @param serialNo 订单流水号
     *                 https://t4-app.simuyun.com/app6.0/biz/publicfund/deal_prompt.html?pageType=2&fundType=0&allMoney=2000
     */
    public static void gotoNewFundResult(Activity activity, int buyOrBuy, String fungType, String allMoney, String serialNo) {
        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("pageType", buyOrBuy + "");
        paramMap.put("fundType", fungType);
        paramMap.put("serialNo", serialNo);
        if (buyOrBuy == 1) {
            paramMap.put("allMoney", allMoney);
        } else if (buyOrBuy == 2) {
            paramMap.put("allShare", allMoney);
        }
//        NavigationUtils.gotoWebActivity(activity, getUrl(CwebNetConfig.publicFundBuyOrSell, paramMap), "交易结果", false);
        NavigationUtils.gotoNavWebActivity(activity, getUrl(CwebNetConfig.publicFundBuyOrSell, paramMap), "交易结果");

    }

    /**
     * 跳转购买结果页
     * @param activity
     * @param fundType
     * @param allMoney
     * @param serialNo
     * @param isWallet  是否是钱包
     */
    public static void gotoBuyFundResult(Activity activity,String fundType,String allMoney,String serialNo,boolean isWallet){
        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("pageType",  "1");
        paramMap.put("fundType", fundType);
        paramMap.put("allMoney", allMoney);
        paramMap.put("serialNo", serialNo);
        if(isWallet){
            paramMap.put("wallet", "1");
        }else {
            paramMap.put("wallet", "0");
        }
        NavigationUtils.gotoNavWebActivity(activity, getUrl(CwebNetConfig.publicFundBuyOrSell, paramMap), "交易结果");
    }

    public static String getUrl(String host, HashMap<String, String> params) {
        String url = host;
        // 添加url参数
        if (params != null) {
            Iterator<String> it = params.keySet().iterator();
            StringBuffer sb = null;
            while (it.hasNext()) {
                String key = it.next();
                String value = params.get(key);
                if (sb == null) {
                    sb = new StringBuffer();
                    sb.append("?");
                } else {
                    sb.append("&");
                }
                sb.append(key);
                sb.append("=");
                sb.append(value);
            }
            url += sb.toString();
        }
        return url;
    }

    /**
     * 产品风险和用户风险的等级
     */
    public static boolean isMatchRisk(Context context, String fundRisk) {
        PublicFundInf publicFundInf = AppManager.getPublicFundInf(context.getApplicationContext());
        if (BStrUtils.isEmpty(fundRisk)) {
            return true;
        }
        if (BStrUtils.isEmpty(publicFundInf.getCustRisk())) {
            return true;
        }
        int customRisk = Integer.parseInt(publicFundInf.getCustRisk());
        int publicFundRisk = Integer.parseInt(fundRisk);
        if (customRisk >= publicFundRisk) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * 只有风险不匹配时候显示弹出框  匹配时候不用显示
     */
    public static void buyRiskShow(final Activity acontext, String fundCode, String fundName, String fundType, String fundRisk) {
        PublicFundInf publicFundInf = AppManager.getPublicFundInf(acontext.getApplicationContext());

        String riskNote = String.format("该产品风险等级为【%s】，与您的风险评测【%s】不匹配。购买后可能面临风险匹配不适当，给您的投资带来不确定性风险因素。\n 您确定购买么？", fundRisk(fundRisk), customRiskToStr(acontext));
        DefaultDialog dialog = new DefaultDialog(acontext, riskNote, "取消", "确定") {
            @Override
            public void left() {
                dismiss();
            }

            @Override
            public void right() {
                //去风险测评a
                HashMap<String, Object> maps = new HashMap<>();
                maps.put("tag_fund_code", fundCode);
                maps.put("tag_fund_name", fundName);
                maps.put("tag_fund_type", fundType);
                maps.put("tag_fund_risk_level", fundRisk);
                NavigationUtils.startActivityByRouter(acontext, RouteConfig.GOTO_PUBLIC_FUND_BUY, maps);
                dismiss();
            }
        };
        dialog.show();
    }

    /**
     * 客户的风险测评
     *
     * @param
     * @return
     */
    public static String customRiskToStr(Context context) {//
        PublicFundInf publicFundInf = AppManager.getPublicFundInf(context.getApplicationContext());
        switch (publicFundInf.getCustRisk()) {
            case "01":
                return "保守型";

            case "02":
                return "稳健型";

            case "03":
                return "平衡型";

            case "04":
                return "成长型";

            case "05":
                return "进取型";

        }
        return "";
    }

    /**
     * 基金的类型
     *
     * @param risk
     * @return
     */
    public static String fundRisk(String risk) {
        switch (risk) {
            case "01":
                return "低风险";

            case "02":
                return "中低风险";

            case "03":
                return "中风险";

            case "04":
                return "中高风险";

            case "05":
                return "高风险";

        }
        return "";
    }
}
