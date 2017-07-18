package com.cgbsoft.privatefund.mvp.ui.center;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.base.webview.BaseWebViewActivity;
import com.cgbsoft.lib.base.webview.CwebNetConfig;
import com.cgbsoft.lib.base.webview.WebViewConstant;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.utils.tools.DataStatistApiParam;
import com.cgbsoft.lib.utils.tools.LogOutAccount;
import com.cgbsoft.lib.utils.tools.NavigationUtils;
import com.cgbsoft.lib.widget.SettingItemNormal;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.mvp.contract.center.SettingContract;
import com.cgbsoft.privatefund.mvp.presenter.center.SettingPresenterImpl;
import com.chenenyu.router.annotation.Route;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 设置页面
 * Created by sunfei on 2017/6/28 0028.
 */
@Route(RouteConfig.GOTOCSETTINGACTIVITY)
public class SettingActivity extends BaseActivity<SettingPresenterImpl> implements SettingContract.SettingView {
    @BindView(R.id.title_left)
    protected ImageView back;
    @BindView(R.id.title_mid)
    TextView titleTV;
    @BindView(R.id.sit_gesture_switch)
    SettingItemNormal gestureSwitch;
    @BindView(R.id.sin_change_gesture_psd)
    SettingItemNormal changeGesturePsdLayout;
    @BindView(R.id.sin_change_login_psd)
    SettingItemNormal changeLoginPsd;

    @Override
    protected int layoutID() {
        return R.layout.activity_setting;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        initView(savedInstanceState);
    }

    private void initView(Bundle savedInstanceState) {
        String phoneNum = AppManager.getUserInfo(baseContext).getUserName();
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
        back.setVisibility(View.VISIBLE);
        titleTV.setText(getResources().getString(R.string.setting_title));
        gestureSwitch.setSwitchButtonChangeListener(new SettingItemNormal.OnSwitchButtonChangeListener() {
            @Override
            public void change(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    NavigationUtils.startActivityByRouter(SettingActivity.this, RouteConfig.SET_GESTURE_PASSWORD, "PARAM_CLOSE_PASSWORD", true);
                } else {

                    NavigationUtils.startActivityByRouter(SettingActivity.this, RouteConfig.VALIDATE_GESTURE_PASSWORD, "PARAM_CLOSE_PASSWORD", true);
                }
            }
        });
    }

    private void turnOffGesturePsd() {
        NavigationUtils.startActivityByRouter(baseContext, RouteConfig.VALIDATE_GESTURE_PASSWORD, "PARAM_CLOSE_PASSWORD", true);
    }

    private void turnOnGesturePsd() {
        NavigationUtils.startActivityByRouter(baseContext, RouteConfig.SET_GESTURE_PASSWORD);
//        String valuse = "1".equals(AppManager.getUserInfo(baseContext).getToC().getGestureSwitch()) ? "2" : "1";
//        DataStatistApiParam.onSwitchGesturePassword(valuse);
    }

    @OnClick(R.id.title_left)
    public void clickBack() {
        this.finish();
    }

    @Override
    protected SettingPresenterImpl createPresenter() {
        return new SettingPresenterImpl(getBaseContext(), this);
    }

    @OnClick(R.id.exit_login_out)
    public void LogOut() {
        LogOutAccount returnLogin = new LogOutAccount();
        returnLogin.accounttExit(this);
    }

    /**
     * 跳转到修改登录密码页面
     */
    @OnClick(R.id.sin_change_login_psd)
    public void changeLoginPsd() {
        Intent intent = new Intent(this, ChangeLoginPsdActivity.class);
        startActivity(intent);
    }

    /**
     * 跳转到修改手势密码
     */
    @OnClick(R.id.sin_change_gesture_psd)
    public void changeGesturePsd() {
        NavigationUtils.startActivityByRouter(this, RouteConfig.VALIDATE_GESTURE_PASSWORD, "PARAM_FROM_MODIFY", true);
    }

    /**
     * 跳转到常见问题
     */
    @OnClick(R.id.sin_common_question)
    public void commonQuestion() {
//        Intent intent = new Intent(this, SalonsActivity.class);
//        startActivity(intent);
    }

    /**
     * 跳转到意见反馈
     */
    @OnClick(R.id.sin_feedback)
    public void feedBack() {
        NavigationUtils.startActivityByRouter(baseContext, "investornmain_feedbackctivity");
    }

    /**
     * 跳转到推荐好友
     */
    @OnClick(R.id.sin_recommend_friend)
    public void recommendFriend() {
        Intent intent = new Intent(this, BaseWebViewActivity.class);
        intent.putExtra(WebViewConstant.push_message_url, CwebNetConfig.recommendFriends);
        startActivity(intent);
//        Intent intent = new Intent(this, PersonalInformationActivity.class);
//        startActivity(intent);
//        NavigationUtils.startActivityByRouter(baseContext,RouteConfig.GOTOC_PERSONAL_INFORMATION_ACTIVITY);
    }

    /**
     * 跳转到关于页面
     */
    @OnClick(R.id.sin_about_app)
    public void aboutApp() {

    }
}
