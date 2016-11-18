package com.cgbsoft.privatefund.mvp.ui.login;

import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.widget.ScrollingImageView;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.mvp.presenter.login.ChoiceIdentityPresenter;
import com.cgbsoft.privatefund.mvp.view.login.ChoiceIdentityView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 选择身份
 * Created by xiaoyu.zhang on 2016/11/17 13:53
 * Email:zhangxyfs@126.com
 *  
 */
public class ChoiceIdentityActivity extends BaseActivity<ChoiceIdentityPresenter>
        implements ChoiceIdentityView, RadioGroup.OnCheckedChangeListener {
    @BindView(R.id.siv_aci_bg)
    ScrollingImageView siv_aci_bg;//背景滚动图

    @BindView(R.id.rg_aci)
    RadioGroup rg_aci;

    @BindView(R.id.rb_aci_inverstor)
    RadioButton rb_aci_inverstor;

    @BindView(R.id.rb_aci_adviser)
    RadioButton rb_aci_adviser;

    @BindView(R.id.btn_aci_next)
    Button btn_aci_next;

    @BindView(R.id.tv_webaddress)
    TextView tv_webaddress;

    private int identity = -1;

    @Override
    protected void before() {
        super.before();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected int layoutID() {
        return R.layout.activity_choice_identity;
    }

    @Override
    protected void init() {

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
        SPreference.saveIdtentify(getApplicationContext(), identity);
    }
}
