package com.cgbsoft.privatefund.mvp.ui.login;

import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.utils.constant.RxConstant;
import com.cgbsoft.lib.utils.rxjava.RxBus;
import com.cgbsoft.lib.widget.ScrollingImageView;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.mvp.contract.login.ChoiceIdentityContract;
import com.cgbsoft.privatefund.mvp.presenter.login.ChoiceIdentityPresenter;
import com.cgbsoft.privatefund.utils.MainTabManager;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 选择身份
 * Created by xiaoyu.zhang on 2016/11/17 13:53
 * Email:zhangxyfs@126.com
 *  
 */
public class ChoiceIdentityActivity extends BaseActivity<ChoiceIdentityPresenter>
        implements ChoiceIdentityContract.View, RadioGroup.OnCheckedChangeListener {
    @BindView(R.id.siv_aci_bg)
    ScrollingImageView siv_aci_bg;//背景滚动图

    @BindView(R.id.rg_aci)
    RadioGroup rg_aci;//单选按钮组

    @BindView(R.id.rb_aci_inverstor)
    RadioButton rb_aci_inverstor;//投资人按钮

    @BindView(R.id.rb_aci_adviser)
    RadioButton rb_aci_adviser;//理财师按钮

    @BindView(R.id.btn_aci_next)
    Button btn_aci_next;//下一步按钮

    private int identity = -1;

    private boolean isExit;

    @Override
    protected int layoutID() {
        return R.layout.activity_choice_identity;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }

    @Override
    protected void data() {
        rg_aci.setOnCheckedChangeListener(this);
    }

    @Override
    protected ChoiceIdentityPresenter createPresenter() {
        return new ChoiceIdentityPresenter(this, this);
    }

    @OnClick(R.id.btn_aci_next)
    void nextClick() {
        getPresenter().nextClick(identity);
        toDataStatistics(1001, 10002, "选择进入");
    }

    @Override
    public void finishThis() {
        finish();
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_aci_inverstor:
                identity = IDS_INVERSTOR;
                toDataStatistics(1001, 10000, "投资人");
                break;
            case R.id.rb_aci_adviser:
                identity = IDS_ADVISER;
                toDataStatistics(1001, 10000, "理财师");
                break;
        }
        //保存身份状态
        SPreference.saveIdtentify(getApplicationContext(), identity);
    }

    @Override
    public void onBackPressed() {
        isExit = exitBy2Click();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        MainTabManager.getInstance().destory();

        if (isExit) {
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }
}