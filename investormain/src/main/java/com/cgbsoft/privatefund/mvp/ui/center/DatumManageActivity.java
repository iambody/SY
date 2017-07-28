package com.cgbsoft.privatefund.mvp.ui.center;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.base.webview.BaseWebViewActivity;
import com.cgbsoft.lib.base.webview.CwebNetConfig;
import com.cgbsoft.lib.base.webview.WebViewConstant;
import com.cgbsoft.lib.utils.tools.NavigationUtils;
import com.cgbsoft.lib.utils.tools.ViewUtils;
import com.cgbsoft.lib.widget.SettingItemNormal;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.mvp.ui.home.AssetProveActivity;
import com.cgbsoft.privatefund.mvp.ui.home.RelativeAssetActivity;
import com.cgbsoft.privatefund.mvp.ui.home.RiskEvaluationActivity;
import com.cgbsoft.privatefund.widget.RightShareWebViewActivity;

import java.util.HashMap;

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
    private String[] riskResult;
    private String[] assetStatus;

    @Override
    protected int layoutID() {
        return R.layout.activity_datum_manage;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        initView(savedInstanceState);
        riskResult = getResources().getStringArray(R.array.risk_evalate_text);
        assetStatus = getResources().getStringArray(R.array.assert_certify);
    }

    @Override
    protected BasePresenterImpl createPresenter() {
        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        int riskType = TextUtils.isEmpty(AppManager.getUserInfo(this).getToC().getCustomerType()) ? 0 : Integer.valueOf(AppManager.getUserInfo(this).getToC().getCustomerType());
        int certify = TextUtils.isEmpty(AppManager.getUserInfo(this).getToC().getAssetsCertificationStatus()) ? 0 : Integer.valueOf(AppManager.getUserInfo(this).getToC().getAssetsCertificationStatus());
        int relative = TextUtils.isEmpty(AppManager.getUserInfo(this).getToC().getStockAssetsStatus()) ? 0 : Integer.valueOf(AppManager.getUserInfo(this).getToC().getStockAssetsStatus());
        riskLike.setTip(riskType > 0 ? riskResult[riskType - 1] : "");
        assetCertify.setTip(certify > 0 ? assetStatus[certify - 1] : "未上传");
        assetRelative.setTip(relative > 0 ? assetStatus[relative - 1] : "未关联");
        if (relative <= 0)  {
            assetRelative.showUpdateView();
        } else {
            assetRelative.hidepdateView();
        }
//        ViewUtils.createLeftTopRedPoint(this, assetRelative, "0");
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
        NavigationUtils.startActivity(this, InvisiteAccountActivity.class);
    }

    @OnClick(R.id.datum_manage_risk)
    public void gotoRiskComment(){
        NavigationUtils.startActivity(this, RiskEvaluationActivity.class);
    }

    @OnClick(R.id.datum_manage_asset_report)
    public void gotoAssetReport(){
        String url = CwebNetConfig.assetReport;
        Intent intent = new Intent(this, RightShareWebViewActivity.class);
        intent.putExtra(WebViewConstant.push_message_url, url);
        intent.putExtra(WebViewConstant.push_message_title, getString(R.string.mine_assert_report));
        intent.putExtra(WebViewConstant.RIGHT_SHARE, true);
        startActivity(intent);
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
