package com.cgbsoft.privatefund.mvp.ui.center;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.utils.tools.NavigationUtils;
import com.cgbsoft.lib.widget.SettingItemNormal;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.mvp.ui.home.AssetProveActivity;
import com.cgbsoft.privatefund.mvp.ui.home.RelativeAssetActivity;
import com.cgbsoft.privatefund.mvp.ui.home.RiskEvaluationActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author chenlong
 */
public class DatumManageActivity extends BaseActivity {
    @BindView(R.id.title_left)
    protected ImageView back;
    @BindView(R.id.title_mid)
    TextView titleTV;
    @BindView(R.id.datum_manage_risk)
    SettingItemNormal riskLike;
    @BindView(R.id.datum_manage_asset_certify)
    SettingItemNormal assetCertify;
    @BindView(R.id.datum_manage_relative_asset)
    SettingItemNormal assetRelative;

    @Override
    protected int layoutID() {
        return R.layout.activity_datum_manage;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        initView(savedInstanceState);
    }

    @Override
    protected BasePresenterImpl createPresenter() {
        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        riskLike.setTip(AppManager.getUserInfo(this).getToC().getCustomerType());
        assetCertify.setTip(AppManager.getUserInfo(this).getToC().getAssetsCertificationStatus());
        assetRelative.setTip(AppManager.getUserInfo(this).getToC().getStockAssetsStatus());
    }

    private void initView(Bundle savedInstanceState) {
        back.setVisibility(View.VISIBLE);
        titleTV.setText(getResources().getString(R.string.datum_manage_title));
    }
    @OnClick(R.id.title_left)
    public void clickBack(){
        this.finish();
    }

    @OnClick(R.id.datum_manage_account)
    public void gotoAccount(){
    }

    @OnClick(R.id.datum_manage_risk)
    public void gotoRiskComment(){
        NavigationUtils.startActivity(this, RiskEvaluationActivity.class);
    }

    @OnClick(R.id.datum_manage_asset_report)
    public void gotoAssetReport(){
    }

    @OnClick(R.id.datum_manage_asset_certify)
    public void gotoAssetCertify(){
        NavigationUtils.startActivity(this, AssetProveActivity.class);
    }
    @OnClick(R.id.datum_manage_relative_asset)
    public void gotoRelativeAsset(){
        NavigationUtils.startActivity(this, RelativeAssetActivity.class);
    }
}
