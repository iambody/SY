package com.cgbsoft.privatefund.mvp.ui.center;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.base.model.UserInfoDataEntity;
import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.privatefund.R;
import com.chenenyu.router.annotation.Route;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by sunfei on 2017/7/13 0013.
 */
@Route(RouteConfig.GOTO_CHANGE_USERGENDER_ACTIVITY)
public class ChangeGenderActivity extends BaseActivity{
//    @BindView(R.id.toolbar)
//    protected Toolbar toolbar;
    @BindView(R.id.title_mid)
    TextView titleTV;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_male_flag)
    ImageView maleFlag;
    @BindView(R.id.iv_female_flag)
    ImageView femaleFlag;
    private UserInfoDataEntity.UserInfo userInfo;

    @OnClick(R.id.ll_male_all)
    public void maleAllClick(){
        maleFlag.setVisibility(View.VISIBLE);
        femaleFlag.setVisibility(View.INVISIBLE);
        Intent dataIntent = new Intent();
        dataIntent.putExtra("gender", "男");
        setResult(RESULT_OK,dataIntent);
        this.finish();
    }
    @OnClick(R.id.ll_female_all)
    public void femaleAllClick(){
        maleFlag.setVisibility(View.INVISIBLE);
        femaleFlag.setVisibility(View.VISIBLE);
        Intent dataIntent = new Intent();
        dataIntent.putExtra("gender", "女");
        setResult(RESULT_OK,dataIntent);
        this.finish();
    }
    @Override
    protected int layoutID() {
        return R.layout.activity_changegender;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        initView(savedInstanceState);
    }

    private void initView(Bundle savedInstanceState) {
        userInfo = AppManager.getUserInfo(baseContext);
        titleTV.setText(getResources().getString(R.string.personal_information_title_changegender));
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        setSupportActionBar(toolbar);
//        toolbar.setNavigationIcon(com.cgbsoft.lib.R.drawable.ic_back_black_24dp);
//        toolbar.setNavigationOnClickListener(v -> finish());
        if (null != userInfo) {
            String gender = userInfo.getSex();
            if (!TextUtils.isEmpty(gender)) {
                if (gender.equals("男")) {
                    maleFlag.setVisibility(View.VISIBLE);
                    femaleFlag.setVisibility(View.INVISIBLE);
                } else {
                    maleFlag.setVisibility(View.INVISIBLE);
                    femaleFlag.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    protected BasePresenterImpl createPresenter() {
        return null;
    }
}
