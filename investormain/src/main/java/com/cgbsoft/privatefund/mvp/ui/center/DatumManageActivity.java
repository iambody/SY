package com.cgbsoft.privatefund.mvp.ui.center;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.base.webview.CwebNetConfig;
import com.cgbsoft.lib.base.webview.WebViewConstant;
import com.cgbsoft.lib.listener.listener.GestureManager;
import com.cgbsoft.lib.utils.constant.Constant;
import com.cgbsoft.lib.utils.constant.RxConstant;
import com.cgbsoft.lib.utils.rxjava.RxBus;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.utils.tools.DataStatistApiParam;
import com.cgbsoft.lib.utils.tools.NavigationUtils;
import com.cgbsoft.lib.widget.SettingItemNormal;
import com.cgbsoft.lib.widget.dialog.LoadingDialog;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.mvp.contract.center.DatumManageContract;
import com.cgbsoft.privatefund.mvp.presenter.center.DatumManagePresenterImpl;
import com.cgbsoft.privatefund.mvp.ui.home.AssetProveActivity;
import com.cgbsoft.privatefund.mvp.ui.home.RiskEvaluationActivity;
import com.cgbsoft.privatefund.widget.RightShareWebViewActivity;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;

/**
 * @author chenlong
 */
public class DatumManageActivity extends BaseActivity<DatumManagePresenterImpl> implements DatumManageContract.DatumManageView {
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
    private boolean showAssert;
    private LoadingDialog mLoadingDialog;
    private Observable<Boolean> swtichRelativeAssetObservable;
    private boolean isClickBack;
    private boolean hasIndentity;
    private boolean hasUpload;
    private String indentityCode;
    private String title;
    private String credentialCode;
    private String status;

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
    protected DatumManagePresenterImpl createPresenter() {
        return new DatumManagePresenterImpl(this, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        MobclickAgent.onPageEnd(Constant.SXY_ZLGL);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart(Constant.SXY_ZLGL);
        getPresenter().verifyIndentity();
        int riskType = TextUtils.isEmpty(AppManager.getUserInfo(this).getToC().getCustomerType()) ? 0 : Integer.valueOf(AppManager.getUserInfo(this).getToC().getCustomerType());
        int certify = TextUtils.isEmpty(AppManager.getUserInfo(this).getToC().getAssetsCertificationStatus()) ? 0 : Integer.valueOf(AppManager.getUserInfo(this).getToC().getAssetsCertificationStatus());
        int relative = TextUtils.isEmpty(AppManager.getUserInfo(this).getToC().getStockAssetsStatus()) ? 0 : Integer.valueOf(AppManager.getUserInfo(this).getToC().getStockAssetsStatus());
        riskLike.setTip(riskType > 0 ? riskResult[riskType - 1] : "");
//        assetCertify.setTip(certify > 0 ? assetStatus[certify - 1] : "未上传");
//        assetRelative.setTip(relative > 0 ? assetStatus[relative - 1] : "未关联");
//        if (relative <= 0)  {
//            assetRelative.showUpdateView();
//        } else {
//            assetRelative.hidepdateView();
//        }
//        ViewUtils.createLeftTopRedPoint(this, assetRelative, "0");
    }

    private void initView(Bundle savedInstanceState) {
        mLoadingDialog = LoadingDialog.getLoadingDialog(baseContext, "", false, false);
        back.setVisibility(View.VISIBLE);
        titleTV.setText(getResources().getString(R.string.datum_manage_title));
        swtichRelativeAssetObservable = RxBus.get().register(RxConstant.GOTO_SWITCH_RELATIVE_ASSERT_IN_DATAMANAGE, Boolean.class);
        swtichRelativeAssetObservable.subscribe(new RxSubscriber<Boolean>() {
            @Override
            protected void onEvent(Boolean boovalue) {
//                getPresenter().verifyIndentity();
                goToCardCollect();
            }

            @Override
            protected void onRxError(Throwable error) {

            }
        });
    }

    @OnClick(R.id.title_left)
    public void clickBack() {
        this.finish();
    }

    @OnClick(R.id.datum_manage_account)
    public void gotoAccount() {
        NavigationUtils.startActivity(this, InvisiteAccountActivity.class);
        DataStatistApiParam.operateInvestorAccountClick();
    }

    @OnClick(R.id.datum_manage_risk)
    public void gotoRiskComment() {
        NavigationUtils.startActivity(this, RiskEvaluationActivity.class);
    }

    @OnClick(R.id.datum_manage_asset_report)
    public void gotoAssetReport() {
        String url = CwebNetConfig.assetReport;
        Intent intent = new Intent(this, RightShareWebViewActivity.class);
        intent.putExtra(WebViewConstant.push_message_url, url);
        intent.putExtra(WebViewConstant.push_message_title, getString(R.string.mine_assert_report));
        intent.putExtra(WebViewConstant.RIGHT_SHARE, true);
        startActivity(intent);
    }

    @OnClick(R.id.datum_manage_asset_certify)
    public void gotoAssetCertify() {
        NavigationUtils.startActivity(this, AssetProveActivity.class);
    }

    @OnClick(R.id.datum_manage_relative_asset)
    public void gotoRelativeAsset() {
        if (showAssert) {
            goToCardCollect();
        } else {
            GestureManager.showGroupGestureManage(this, GestureManager.RELATIVE_ASSERT_IN_DATDMANAGE);
        }
    }

    private void goToCardCollect() {
        if (null == status) {
            isClickBack = true;
            getPresenter().verifyIndentity();
        } else {
            isClickBack = false;
            if (hasIndentity) {
                if (hasUpload) {//去证件列表
                    Intent intent = new Intent(this, CardCollectActivity.class);
                    intent.putExtra("indentityCode", indentityCode);
                    startActivity(intent);
                } else {//去上传证件照
                    Intent intent = new Intent(this, UploadIndentityCradActivity.class);
                    intent.putExtra("credentialCode", credentialCode);
                    intent.putExtra("indentityCode", indentityCode);
                    intent.putExtra("title", title);
                    startActivity(intent);
                }
            } else {//无身份
                Intent intent = new Intent(this, SelectIndentityActivity.class);
                startActivity(intent);
            }
        }
    }

    @Override
    protected void before() {
        super.before();
        showAssert = AppManager.isShowAssert(this);
    }

    @Override
    public void showLoadDialog() {
        try {
            if (mLoadingDialog.isShowing()) {
                return;
            }
            mLoadingDialog.show();
        } catch (Exception e) {
        }
    }

    @Override
    public void hideLoadDialog() {
        mLoadingDialog.dismiss();
    }

    @Override
    public void verifyIndentitySuccess(boolean hasIndentity, boolean hasUpload, String indentityCode, String title, String credentialCode, String status, String statsCode) {
        this.hasIndentity = hasIndentity;
        this.hasUpload = hasUpload;
        this.indentityCode = indentityCode;
        this.title = title;
        this.credentialCode = credentialCode;
        this.status = status;
        assetRelative.setTip(status);
        if (isClickBack) {
            isClickBack = false;
            if (hasIndentity) {
                if (hasUpload) {//去证件列表
                    Intent intent = new Intent(this, CardCollectActivity.class);
                    intent.putExtra("indentityCode", indentityCode);
                    startActivity(intent);
                } else {//去上传证件照
                    Intent intent = new Intent(this, UploadIndentityCradActivity.class);
                    intent.putExtra("credentialCode", credentialCode);
                    intent.putExtra("indentityCode", indentityCode);
                    intent.putExtra("title", title);
                    startActivity(intent);
                }
            } else {//无身份
                Intent intent = new Intent(this, SelectIndentityActivity.class);
                startActivity(intent);
            }
        }
    }

    @Override
    public void verifyIndentityError(Throwable error) {
        if (isClickBack) {
            isClickBack = false;
            Toast.makeText(getApplicationContext(), "服务器忙,请稍后再试!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (swtichRelativeAssetObservable != null) {
            RxBus.get().unregister(RxConstant.GOTO_SWITCH_RELATIVE_ASSERT_IN_DATAMANAGE, swtichRelativeAssetObservable);
        }
    }
}
