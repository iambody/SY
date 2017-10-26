package com.cgbsoft.privatefund.widget;

import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.TaskInfo;
import com.cgbsoft.lib.base.webview.BaseWebViewActivity;
import com.cgbsoft.lib.base.webview.CwebNetConfig;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.utils.constant.Constant;
import com.chenenyu.router.annotation.Route;
import com.umeng.analytics.MobclickAgent;

/**
 * @author chenlong
 */
@Route(RouteConfig.GOTO_RIGHT_SHARE_ACTIVITY)
public class RightShareWebViewActivity extends BaseWebViewActivity {

//    CommonShareDialog commonShareDialog;

    @Override
    protected void before() {
        super.before();
        //游客模式下禁止的Api 添加限制条件
//        if (!AppManager.isVisitor(baseContext)) {
        if(!AppManager.isVisitor(baseContext) && (url.contains("new_detail_toc.html") || url.contains("information/details.html"))) { //是资讯页面
            TaskInfo.complentTask("查看资讯");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        MobclickAgent.onResume(this);

        if (CwebNetConfig.BindchiceAdiser.equals(url)) {
            MobclickAgent.onPageStart(Constant.SXY_WSRYHJ);
        }
        if (CwebNetConfig.recommendFriends.equals(url)) {
            MobclickAgent.onPageStart(Constant.SXY_TJHY);
        }
        if (CwebNetConfig.mineAssertOrder.equals(url)) {
            MobclickAgent.onPageStart(Constant.SXY_TZZH);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
//        MobclickAgent.onPause(this);
        if (CwebNetConfig.BindchiceAdiser.equals(url)) {
            MobclickAgent.onPageEnd(Constant.SXY_WSRYHJ);
        }
        if (CwebNetConfig.recommendFriends.equals(url)) {
            MobclickAgent.onPageEnd(Constant.SXY_TJHY);
        }
        if (CwebNetConfig.mineAssertOrder.equals(url)) {
            MobclickAgent.onPageEnd(Constant.SXY_TZZH);
        }
    }
}
