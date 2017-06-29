package com.cgbsoft.privatefund.mvp.ui.center;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.utils.tools.LogUtils;
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

    @Override
    protected int layoutID() {
        return R.layout.activity_setting;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        initView(savedInstanceState);
    }

    private void initView(Bundle savedInstanceState) {
        back.setVisibility(View.VISIBLE);
        titleTV.setText("设置");
        gestureSwitch.setSwitchCheck(true);
        gestureSwitch.setSwitchButtonChangeListener(new SettingItemNormal.OnSwitchButtonChangeListener() {
            @Override
            public void change(CompoundButton buttonView, boolean isChecked) {
                LogUtils.Log("aaa","ischecked===="+isChecked);
            }
        });
    }
    @OnClick(R.id.title_left)
    public void clickBack(){
        this.finish();
    }
    @Override
    protected SettingPresenterImpl createPresenter() {
        return new SettingPresenterImpl(getBaseContext(),this);
    }
}
