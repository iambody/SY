package com.cgbsoft.privatefund.mvp.ui.center;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.base.model.AppResourcesEntity;
import com.cgbsoft.lib.base.model.bean.OtherInfo;
import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.base.webview.BaseWebViewActivity;
import com.cgbsoft.lib.base.webview.CwebNetConfig;
import com.cgbsoft.lib.base.webview.WebViewConstant;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.utils.constant.RxConstant;
import com.cgbsoft.lib.utils.db.DBConstant;
import com.cgbsoft.lib.utils.db.DaoUtils;
import com.cgbsoft.lib.utils.rxjava.RxBus;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.utils.tools.DataStatistApiParam;
import com.cgbsoft.lib.utils.tools.LogOutAccount;
import com.cgbsoft.lib.utils.tools.NavigationUtils;
import com.cgbsoft.lib.utils.tools.TrackingDataManger;
import com.cgbsoft.lib.utils.tools.Utils;
import com.cgbsoft.lib.widget.SettingItemNormal;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.bean.product.PublicFundInf;
import com.cgbsoft.privatefund.mvp.contract.center.SettingContract;
import com.cgbsoft.privatefund.mvp.presenter.center.SettingPresenterImpl;
import com.cgbsoft.privatefund.mvp.ui.home.FeedbackActivity;
import com.cgbsoft.privatefund.widget.RightShareWebViewActivity;
import com.chenenyu.router.annotation.Route;
import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;


import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;

/**
 * 设置页面
 * Created by sunfei on 2017/6/28 0028.
 */
@Route(RouteConfig.GOTOCSETTINGACTIVITY)
public class SettingActivity extends BaseActivity<SettingPresenterImpl> implements SettingContract.SettingView {
    //    @BindView(R.id.toolbar)
//    protected Toolbar toolbar;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.title_mid)
    TextView titleTV;
    @BindView(R.id.sit_gesture_switch)
    SettingItemNormal gestureSwitch;
    @BindView(R.id.sin_change_gesture_psd)
    SettingItemNormal changeGesturePsdLayout;
    @BindView(R.id.sin_change_login_psd)
    SettingItemNormal changeLoginPsd;
    @BindView(R.id.sin_public_fund)
    SettingItemNormal publicFund;
    @BindView(R.id.sin_about_app)
    SettingItemNormal aboutApp;
    private Observable<Boolean> switchButton;
    private Observable<Boolean> closeSetting;
    private DaoUtils daoUtils;

    @Override
    protected int layoutID() {
        return R.layout.activity_setting;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        daoUtils = new DaoUtils(baseContext, DaoUtils.W_OTHER);
        initView(savedInstanceState);
        showView();
        DataStatistApiParam.intoSettingPage();
    }

    private void showView() {
        switchButton = RxBus.get().register(RxConstant.SET_PAGE_SWITCH_BUTTON, Boolean.class);
        switchButton.subscribe(new RxSubscriber<Boolean>() {
            @Override
            protected void onEvent(Boolean aBoolean) {
                changeGesturePsdLayout.setVisibility(aBoolean ? View.VISIBLE : View.GONE);
                gestureSwitch.setSwitchCheck(aBoolean);
            }

            @Override
            protected void onRxError(Throwable error) {// ignored
            }
        });
        closeSetting = RxBus.get().register(RxConstant.CLOSE_SETTING_ACTIVITY_OBSERVABE, Boolean.class);
        closeSetting.subscribe(new RxSubscriber<Boolean>() {
            @Override
            protected void onEvent(Boolean aBoolean) {
                finish();
            }

            @Override
            protected void onRxError(Throwable error) {// ignored
            }
        });
    }

    private void initView(Bundle savedInstanceState) {
        titleTV.setText(getResources().getString(R.string.setting_title));
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        setSupportActionBar(toolbar);
//        toolbar.setNavigationIcon(com.cgbsoft.lib.R.drawable.ic_back_black_24dp);
//        toolbar.setNavigationOnClickListener(v -> finish());
//        gestureSwitch.setSwitchButtonChangeListener((buttonView, isChecked) -> {
//            boolean gestureFlag = AppManager.getGestureFlag(baseContext);
//            if (isChecked) {
//                LogUtils.Log("aaa","setpsd");
//                NavigationUtils.startActivityByRouter(SettingActivity.this, RouteConfig.SET_GESTURE_PASSWORD, "PARAM_FROM_SET_GESTURE", true);
//            } else {
//                LogUtils.Log("aaa","valpsd");
//                NavigationUtils.startActivityByRouter(SettingActivity.this, RouteConfig.VALIDATE_GESTURE_PASSWORD, "PARAM_CLOSE_PASSWORD", true);
//            }
//        });
        OtherInfo otherInfo = daoUtils.getOtherInfo(DBConstant.APP_UPDATE_INFO);
        if (otherInfo != null) {
            String json = otherInfo.getContent();
            AppResourcesEntity.Result result = new Gson().fromJson(json, AppResourcesEntity.Result.class);
            if (null != result) {
                if (TextUtils.isEmpty(result.version) || TextUtils.equals(result.version, Utils.getVersionName(baseContext))) {//无更新
                    aboutApp.setTip("");
                } else {//有更新
                    aboutApp.setTip("有更新");
                }
            }
        }
        if (AppManager.getUserInfo(baseContext) != null) {
            String phoneNum = AppManager.getUserInfo(baseContext).getPhoneNum();
            if (TextUtils.isEmpty(phoneNum)) {//无电话号码说明是微信登录用户，隐藏修改密码功能
                changeLoginPsd.setVisibility(View.GONE);
            } else {
                changeLoginPsd.setVisibility(View.VISIBLE);
            }
            boolean gestureFlag = AppManager.getGestureFlag(baseContext);
            gestureSwitch.setSwitchCheck(gestureFlag);
            if (gestureFlag) {//开
                changeGesturePsdLayout.setVisibility(View.VISIBLE);
            } else {
                changeGesturePsdLayout.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean gestureFlag = AppManager.getGestureFlag(baseContext);
        gestureSwitch.setSwitchCheck(gestureFlag);
        if (gestureFlag) {//开
            changeGesturePsdLayout.setVisibility(View.VISIBLE);
        } else {
            changeGesturePsdLayout.setVisibility(View.GONE);
        }
        initPublicFund();
    }

    private void initPublicFund() {
        PublicFundInf publicFundInf = AppManager.getPublicFundInf(this);
        boolean existAccount = !TextUtils.isEmpty(publicFundInf.getCustno());
        boolean bindCard = TextUtils.equals("1", publicFundInf.getIsHaveCustBankAcct());
        boolean isWhiteFlag = Utils.isWhiteUserFlag(this);
        publicFund.setVisibility((isWhiteFlag && existAccount) ? View.VISIBLE : View.GONE);
    }


    private void turnOffGesturePsd() {
        NavigationUtils.startActivityByRouter(baseContext, RouteConfig.VALIDATE_GESTURE_PASSWORD, "PARAM_CLOSE_PASSWORD", true);
    }

    private void turnOnGesturePsd() {
        NavigationUtils.startActivityByRouter(baseContext, RouteConfig.SET_GESTURE_PASSWORD);
//        String valuse = "1".equals(AppManager.getUserInfo(baseContext).getToC().getGestureSwitch()) ? "2" : "1";
//        DataStatistApiParam.onSwitchGesturePassword(valuse);
    }

    @Override
    protected SettingPresenterImpl createPresenter() {
        return new SettingPresenterImpl(getBaseContext(), this);
    }

    @OnClick(R.id.exit_login_out)
    public void LogOut() {
        MobclickAgent.onProfileSignOff();
        LogOutAccount returnLogin = new LogOutAccount();
        returnLogin.accounttExit(this);
        DataStatistApiParam.existLogout();
        TrackingDataManger.existLoginout(this);
    }

    @OnClick(R.id.view_switch_clickarea)
    public void switchGesture() {
        DataStatistApiParam.clickGesture();
        boolean gestureFlag = AppManager.getGestureFlag(baseContext);
        if (gestureFlag) {
//            DataStatistApiParam.switchGestureClick("关");
            NavigationUtils.startActivityByRouter(SettingActivity.this, RouteConfig.VALIDATE_GESTURE_PASSWORD, "PARAM_CLOSE_PASSWORD", true);
        } else {
//            DataStatistApiParam.switchGestureClick("开");
            NavigationUtils.startActivityByRouter(SettingActivity.this, RouteConfig.SET_GESTURE_PASSWORD, "PARAM_FROM_SET_GESTURE", true);
        }
    }

    /**
     * 跳转到公募基金设置页面
     */
    @OnClick(R.id.sin_public_fund)
    public void publicFundSetting(){
        Intent intent = new Intent(this, PublicFundSettingActivity.class);
        startActivity(intent);
    }
    /**
     * 跳转到修改登录密码页面
     */
    @OnClick(R.id.sin_change_login_psd)
    public void changeLoginPsd() {
        DataStatistApiParam.clickChangePsd();
        Intent intent = new Intent(this, ChangeLoginPsdActivity.class);
        startActivity(intent);
    }

    /**
     * 跳转到修改手势密码
     */
    @OnClick(R.id.sin_change_gesture_psd)
    public void changeGesturePsd() {
        DataStatistApiParam.clickChangeGesture();
        NavigationUtils.startActivityByRouter(this, RouteConfig.VALIDATE_GESTURE_PASSWORD, "PARAM_FROM_MODIFY", true);
    }

    /**
     * 跳转到常见问题
     */
    @OnClick(R.id.sin_common_question)
    public void commonQuestion() {
        Intent intent = new Intent(this, BaseWebViewActivity.class);
        intent.putExtra(WebViewConstant.push_message_url, CwebNetConfig.common_problem);
        intent.putExtra(WebViewConstant.push_message_title, getResources().getString(R.string.setting_item_common_question));
        startActivity(intent);
    }

    /**
     * 跳转到意见反馈
     */
    @OnClick(R.id.sin_feedback)
    public void feedBack() {
        DataStatistApiParam.clickFeedBack();
        Intent intent = new Intent(this, FeedbackActivity.class);
        startActivity(intent);
//        NavigationUtils.startActivityByRouter(baseContext, "investornmain_feedbackctivity");
    }

    /**
     * 跳转到推荐好友
     */
    @OnClick(R.id.sin_recommend_friend)
    public void recommendFriend() {
        DataStatistApiParam.recommendFriend();
        Intent intent = new Intent(this, RightShareWebViewActivity.class);
        intent.putExtra(WebViewConstant.push_message_url, CwebNetConfig.recommendFriends);
        intent.putExtra(WebViewConstant.push_message_title, getResources().getString(R.string.setting_item_recommend));
        startActivity(intent);
//        Intent intent = new Intent(this, PersonalInformationActivity.class);
//        startActivity(intent);
//        NavigationUtils.startActivityByRouter(baseContext,RouteConfig.GOTOC_PERSONAL_INFORMATION_ACTIVITY);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (switchButton != null) {
            RxBus.get().unregister(RxConstant.SET_PAGE_SWITCH_BUTTON, switchButton);
        }
        if (closeSetting != null) {
            RxBus.get().unregister(RxConstant.CLOSE_SETTING_ACTIVITY_OBSERVABE, closeSetting);
        }
    }

    /**
     * 跳转到关于页面
     */
    @OnClick(R.id.sin_about_app)
    public void aboutApp() {
        DataStatistApiParam.aboutApp();
        Intent intent = new Intent(this, BaseWebViewActivity.class);
        intent.putExtra(WebViewConstant.push_message_url, CwebNetConfig.aboutapp);
        intent.putExtra(WebViewConstant.push_message_title, getResources().getString(R.string.setting_item_about_app));
        startActivity(intent);
    }
}
