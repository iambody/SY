package com.cgbsoft.privatefund.mvp.ui.center;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.base.webview.CwebNetConfig;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.utils.tools.BStrUtils;
import com.cgbsoft.lib.utils.tools.NavigationUtils;
import com.cgbsoft.lib.utils.tools.Utils;
import com.cgbsoft.lib.widget.SettingItemNormal;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.bean.product.PublicFundInf;
import com.cgbsoft.privatefund.bean.product.PublishFundRecommendBean;
import com.cgbsoft.privatefund.mvp.contract.center.PublicFundSettingContract;
import com.cgbsoft.privatefund.mvp.presenter.center.PublicFundSettingPresenterImpl;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.MessageFormat;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

public class PublicFundSettingActivity extends BaseActivity<PublicFundSettingPresenterImpl> implements PublicFundSettingContract.PublicFundSettingView {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.title_mid)
    TextView titleTV;
    @BindView(R.id.sin_public_fund_account_status)
    SettingItemNormal publicFundAccountStatus;
    @BindView(R.id.sin_public_fund_bankcard)
    SettingItemNormal publicFundBankCarkInfo;
    @BindView(R.id.sin_public_fund_trade_password)
    SettingItemNormal publicFundTradePasswordModify;
    @BindView(R.id.sin_public_fund_setting_risk)
    SettingItemNormal publicFundRisk;

    @Override
    protected void onResume() {
        super.onResume();
        initPublicFund();
    }

    @Override
    protected int layoutID() {
        return R.layout.activity_public_fund_setting;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        initView();
    }

    private void initView() {
        titleTV.setText(getResources().getString(R.string.setting_item_public_fund));
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected PublicFundSettingPresenterImpl createPresenter() {
        return new PublicFundSettingPresenterImpl(getBaseContext(), this);
    }

    private void initPublicFund() {
        PublicFundInf publicFundInf = AppManager.getPublicFundInf(this);
        boolean existAccount = !TextUtils.isEmpty(publicFundInf.getCustNo());
        boolean bindCard = TextUtils.equals("1", publicFundInf.getIsHaveCustBankAcct());
        boolean isWhiteFlag = Utils.isWhiteUserFlag(this);

        publicFundAccountStatus.setTitle(existAccount ? getString(R.string.public_fund_setting_account_info) : getString(R.string.public_fund_setting_account_create));
        publicFundBankCarkInfo.setTitle(bindCard ? getString(R.string.public_fund_setting_bankcard_info) : getString(R.string.public_fund_setting_bind_bankcard));
        publicFundTradePasswordModify.setTitle(getString(R.string.public_fund_setting_modify_public_fund_password));
        publicFundAccountStatus.setVisibility(isWhiteFlag ? View.VISIBLE : View.GONE);
        publicFundBankCarkInfo.setVisibility((isWhiteFlag && existAccount) ? View.VISIBLE : View.GONE);
        publicFundTradePasswordModify.setVisibility((isWhiteFlag && existAccount) ? View.VISIBLE : View.GONE);

        publicFundRisk.setVisibility((isWhiteFlag && existAccount && !BStrUtils.isEmpty(publicFundInf.getCustRisk())) ? View.VISIBLE : View.GONE);

    }

    @OnClick(R.id.sin_public_fund_setting_risk)
    void gotoRiskResult() {
        PublicFundInf publicFundInf = AppManager.getPublicFundInf(this);
        publicFundInf.getCustRisk();
        NavigationUtils.gotoWebActivity(this, MessageFormat.format("{0}?custrisk={1}", CwebNetConfig.publicFundRiskUrl, publicFundInf.getCustRisk()), "风险测评", false);
    }

    @OnClick(R.id.sin_public_fund_account_status)
    void gotoCreatePublicFundAccount() {
        PublicFundInf publicFundInf = AppManager.getPublicFundInf(this);
        if (TextUtils.isEmpty(publicFundInf.getCustNo())) {
            NavigationUtils.gotoWebActivity(this, CwebNetConfig.publicFundRegistUrl, getResources().getString(R.string.public_fund_regist), false);
        } else {
            NavigationUtils.startActivityByRouter(this, RouteConfig.GOTO_PUBLIC_FUND_INFO_ACTIVITY);
        }
    }

    @OnClick(R.id.sin_public_fund_bankcard)
    void gotoPublicFundBankCard() {
        PublicFundInf publicFundInf = AppManager.getPublicFundInf(this);
        boolean bindCard = TextUtils.equals("1", publicFundInf.getIsHaveCustBankAcct());
        if (bindCard) {
            NavigationUtils.startActivityByRouter(this, RouteConfig.GOTO_BIND_BANK_CARD_ACTIVITY_INFO);
        } else {
            PublicFundInf publicFundInf1 = AppManager.getPublicFundInf(this);
            PublishFundRecommendBean publishFundRecommendBean = AppManager.getPubliFundRecommend(this);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("trantype", "bgAddCard");
                jsonObject.put("custno", publicFundInf1.getCustNo());
                jsonObject.put("authenticateflag", "1");
                jsonObject.put("certificateno", publishFundRecommendBean.getCertificateNo());
                jsonObject.put("certificatetype", publishFundRecommendBean.getCertificateType());
                jsonObject.put("depositacct", publishFundRecommendBean.getDepositAcct());
                jsonObject.put("depositacctname", publishFundRecommendBean.getDepositacctName());
                jsonObject.put("depositname", publishFundRecommendBean.getDepositacctName());
                jsonObject.put("depositcity", "");
                jsonObject.put("depositprov", "");
                jsonObject.put("operorg", "9999");
                jsonObject.put("tpasswd", "");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            HashMap<String, Object> map = new HashMap<>();
            map.put("tag_parameter", jsonObject.toString());
            NavigationUtils.startActivityByRouter(this, RouteConfig.GOTO_PUBLIC_FUND_BIND_BANK_CARD, map);
        }
    }

    @OnClick(R.id.sin_public_fund_trade_password)
    void gotoPublicFundTradePasswordModify() {
        NavigationUtils.startActivityByRouter(this, RouteConfig.GOTO_PUBLIC_FUND_TRADE_PWD_MODIFY_ACTIVITY);
    }

}
