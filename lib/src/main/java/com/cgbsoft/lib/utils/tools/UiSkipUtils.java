package com.cgbsoft.lib.utils.tools;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import com.cgbsoft.lib.AppManager;
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
        String fundinf = publicFundInf.getCustno();//客户号 空=》未开户；非空=》开户
        if (BStrUtils.isEmpty(fundinf) && "0".equals(publicFundInf.getIsHaveCustBankAcct()) && BStrUtils.isEmpty(publicFundInf.getCustRisk())) {//未开户
            //没开户=》跳转到开户页面ton
            NavigationUtils.gotoWebActivity(activity, CwebNetConfig.publicFundRegistUrl, "公募基金开户", false);
        } else if (!BStrUtils.isEmpty(fundinf) && "0".equals(publicFundInf.getIsHaveCustBankAcct())) {
            //没绑定银行卡=》跳转到绑定银行卡页面
            String bankParam = new Gson().toJson(publicFundInf);
            HashMap<String, Object> map = new HashMap<>();
            map.put("tag_parameter", bankParam);
            NavigationUtils.startActivityByRouter(activity, RouteConfig.GOTO_PUBLIC_FUND_BIND_BANK_CARD, map);

        } else if (!BStrUtils.isEmpty(fundinf) && "1".equals(publicFundInf.getIsHaveCustBankAcct()) && BStrUtils.isEmpty(publicFundInf.getCustRisk())) {
            //没风险测评=》跳转到公共的页面
            DefaultDialog dialog = new DefaultDialog(activity, "请先填写调查问卷", "取消", "确定") {
                @Override
                public void left() {
                    dismiss();
                }

                @Override
                public void right() {
                    //去风险测评
                    NavigationUtils.gotoWebActivity(activity, CwebNetConfig.publicFundRiskUrl, "风险测评", false);
                    dismiss();

                }
            };
            dialog.show();
        }

    }

    /**
     * 原生跳转到公募基金申购页面
     */
    public static void toBuyPublicFundFromNative(Activity activity, String fundCode, String risklevel) {
        //需要先判断是否注册绑卡
        PublicFundInf publicFundInf = AppManager.getPublicFundInf(activity.getApplicationContext());
        String fundinf = publicFundInf.getCustno();//客户号 空=》未开户；非空=》开户
        if (BStrUtils.isEmpty(fundinf) && "0".equals(publicFundInf.getIsHaveCustBankAcct()) && BStrUtils.isEmpty(publicFundInf.getCustRisk())) {//未开户
            //没开户=》跳转到开户页面ton
            NavigationUtils.gotoWebActivity(activity, CwebNetConfig.publicFundRegistUrl, "公募基金开户", false);
        } else if (!BStrUtils.isEmpty(fundinf) && "0".equals(publicFundInf.getIsHaveCustBankAcct())) {
            //没绑定银行卡=》跳转到绑定银行卡页面
            String bankParam = new Gson().toJson(publicFundInf);
            HashMap<String, Object> map = new HashMap<>();
            map.put("tag_parameter", bankParam);
            NavigationUtils.startActivityByRouter(activity, RouteConfig.GOTO_PUBLIC_FUND_BIND_BANK_CARD, map);

        } else if (!BStrUtils.isEmpty(fundinf) && "1".equals(publicFundInf.getIsHaveCustBankAcct()) && BStrUtils.isEmpty(publicFundInf.getCustRisk())) {
            //没风险测评=》跳转到公共的页面
            DefaultDialog dialog = new DefaultDialog(activity, "请先填写调查问卷", "取消", "确定") {
                @Override
                public void left() {
                    dismiss();
                }

                @Override
                public void right() {
                    //去风险测评
                    NavigationUtils.gotoWebActivity(activity, CwebNetConfig.publicFundRiskUrl, "风险测评", false);
                    dismiss();
                }
            };
            dialog.show();
        } else if (!BStrUtils.isEmpty(fundinf) && "1".equals(publicFundInf.getIsHaveCustBankAcct()) && !BStrUtils.isEmpty(publicFundInf.getCustRisk())) {
            //开过户并且已经完成绑卡 跳转到数据里面
            // 开过户绑过卡风险测评过后 在跳转到申购之前 需要进行 风险的匹配检测   不匹配时候弹框提示 点击确认风险后就跳转到申购页面
            HashMap<String, Object> maps = new HashMap<>();
            maps.put("tag_fund_code", fundCode);
            maps.put("tag_fund_risk_level", risklevel);
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
}
