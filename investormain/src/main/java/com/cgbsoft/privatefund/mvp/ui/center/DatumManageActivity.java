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
import com.cgbsoft.privatefund.bean.living.LivingResultData;
import com.cgbsoft.privatefund.model.CredentialStateMedel;
import com.cgbsoft.privatefund.mvp.contract.center.DatumManageContract;
import com.cgbsoft.privatefund.mvp.presenter.center.DatumManagePresenterImpl;
import com.cgbsoft.privatefund.mvp.ui.home.AssetProveActivity;
import com.cgbsoft.privatefund.mvp.ui.home.CrenditralGuideActivity;
import com.cgbsoft.privatefund.mvp.ui.home.RiskEvaluationActivity;
import com.cgbsoft.privatefund.widget.RightShareWebViewActivity;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import app.ocrlib.com.LivingManger;
import app.ocrlib.com.LivingResult;
import app.ocrlib.com.facepicture.FacePictureActivity;
import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;

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
    private CredentialStateMedel credentialStateMedel;
    private LivingManger livingManger;

    @Override
    protected int layoutID() {
        return R.layout.activity_datum_manage;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        initView(savedInstanceState);
        riskResult = getResources().getStringArray(R.array.risk_evalate_text);
        assetStatus = getResources().getStringArray(R.array.assert_certify);
        credentialStateMedel = new CredentialStateMedel();
        initCallBack();
    }

    private void initCallBack() {
        Observable<Integer> register = RxBus.get().register(RxConstant.COMPLIANCE_PERSON_COMPARE, Integer.class);
        register.subscribe(new RxSubscriber<Integer>() {
            @Override
            protected void onEvent(Integer integer) {
                NavigationUtils.startActivity(DatumManageActivity.this, RiskEvaluationActivity.class);
            }

            @Override
            protected void onRxError(Throwable error) {

            }
        });
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
        getPresenter().verifyIndentityV3();
        int riskType = TextUtils.isEmpty(AppManager.getUserInfo(this).getToC().getCustomerType()) ? 0 : Integer.valueOf(AppManager.getUserInfo(this).getToC().getCustomerType());
        int certify = TextUtils.isEmpty(AppManager.getUserInfo(this).getToC().getAssetsCertificationStatus()) ? 0 : Integer.valueOf(AppManager.getUserInfo(this).getToC().getAssetsCertificationStatus());
        int relative = TextUtils.isEmpty(AppManager.getUserInfo(this).getToC().getStockAssetsStatus()) ? 0 : Integer.valueOf(AppManager.getUserInfo(this).getToC().getStockAssetsStatus());
        riskLike.setTip(riskType > 0 ? riskResult[riskType - 1] : "");
        assetCertify.setTip(certify > 0 ? assetStatus[certify - 1] : "");
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
        goToDatumCollect();

    }

    private void goToDatumCollect() {
        if ("".equals(credentialStateMedel.getCustomerIdentity())) {
            NavigationUtils.startActivity(this, RiskEvaluationActivity.class);
        } else {
            if ("10".equals(credentialStateMedel.getCustomerType())) {
                if ("1001".equals(credentialStateMedel.getCustomerIdentity())) {
                    if ("50".equals(credentialStateMedel.getIdCardState()) && "0".equals(credentialStateMedel.getCustomerLivingbodyState())) {
                        startMatchLiving();
                    } else {
                        Intent intent = new Intent(this, UploadIndentityCradActivity.class);
                        intent.putExtra("credentialStateMedel", credentialStateMedel);
                        startActivity(intent);
                    }
                } else {
                    if ("50".equals(credentialStateMedel.getCredentialState()) && "0".equals(credentialStateMedel.getCustomerImageState())) {
                        startMatchImg();
                    } else {
                        Intent intent = new Intent(this, UploadIndentityCradActivity.class);
                        intent.putExtra("credentialStateMedel", credentialStateMedel);
                        startActivity(intent);
                    }
                }
            } else {
                NavigationUtils.startActivity(this, RiskEvaluationActivity.class);
            }
        }

    }

    private void startMatchImg() {
        startActivity(new Intent(this, FacePictureActivity.class).putExtra(FacePictureActivity.TAG_NEED_PERSON, true));
    }

    private void startMatchLiving() {
        livingManger = new LivingManger(this, "100101", "1001", new LivingResult() {
            @Override
            public void livingSucceed(LivingResultData resultData) {
                NavigationUtils.startActivity(DatumManageActivity.this, RiskEvaluationActivity.class);
            }

            @Override
            public void livingFailed(LivingResultData resultData) {

            }
        });
        getPresenter().getLivingCount();

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
        if ("".equals(credentialStateMedel.getCredentialCode())) {
            Intent intent = new Intent(this, SelectIndentityActivity.class);
            startActivity(intent);
        } else {
            if ("1001".equals(credentialStateMedel.getCustomerIdentity())) {
                //TODO 第二个状态变成0
                if ("50".equals(credentialStateMedel.getIdCardState()) && "1".equals(credentialStateMedel.getCustomerLivingbodyState())) {
                    Intent intent1 = new Intent(this, CardCollectActivity.class);
                    intent1.putExtra("indentityCode", credentialStateMedel.getCustomerIdentity());
                    startActivity(intent1);
                } else {
                    jumpGuidePage();
                }
            } else {
                Intent intent1 = new Intent(this, CardCollectActivity.class);
                intent1.putExtra("indentityCode", credentialStateMedel.getCustomerIdentity());
                startActivity(intent1);
            }
        }

//
//        if (credentialStateMedel != null) {
////            toInvestorCarlendarActivity();
//            if (null == credentialStateMedel.getCredentialState()) {
//                isClickBack = true;
//                getPresenter().verifyIndentityV3();
//            } else {
//                isClickBack = false;
//                //90：存量已有证件号已上传证件照待审核
//                if ("45".equals(credentialStateMedel.getIdCardState())) {//存量用户已有证件号码未上传证件照；
//                    replenishCards();
//                } else {
//                    toInvestorCarlendarActivity();
//                }
//            }
//        }

//        if (null == status) {
//            isClickBack = true;
////            getPresenter().verifyIndentity();
//            getPresenter().verifyIndentityV3();
//        } else {
//            isClickBack = false;
//            if (hasIndentity) {
//
//                CredentialStateMedel credentialStateMedel = (CredentialStateMedel) getIntent().getSerializableExtra("credentialStateMedel");
//                if (hasUpload) {//去证件列表
//                    Intent intent = new Intent(this, CardCollectActivity.class);
//                    intent.putExtra("indentityCode", credentialStateMedel.getCustomerIdentity());
//                    startActivity(intent);
//                } else {//去上传证件照
//                    Intent intent = new Intent(this, UploadIndentityCradActivity.class);
//                    intent.putExtra("credentialStateMedel", credentialStateMedel);
//                    startActivity(intent);
//                }
//            } else {//无身份
//                Intent intent = new Intent(this, SelectIndentityActivity.class);
//                startActivity(intent);
//            }
//        }
    }

    private void toInvestorCarlendarActivity() {

    }

    private void replenishCards() {
        Intent intent = new Intent(this, UploadIndentityCradActivity.class);
        intent.putExtra("credentialStateMedel", credentialStateMedel);
        startActivity(intent);
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
        assetRelative.setTip(status);
        if (isClickBack) {
            isClickBack = false;
            CredentialStateMedel credentialStateMedel = (CredentialStateMedel) getIntent().getSerializableExtra("credentialStateMedel");
            if (hasIndentity) {
                if (hasUpload) {//去证件列表
                    Intent intent = new Intent(this, CardCollectActivity.class);
                    intent.putExtra("indentityCode", credentialStateMedel.getCustomerIdentity());
                    startActivity(intent);
                } else {//去上传证件照
                    Intent intent = new Intent(this, UploadIndentityCradActivity.class);
                    intent.putExtra("credentialStateMedel", credentialStateMedel);
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

    /**
     * 跳转到引导页面
     */
    private void jumpGuidePage() {
        Intent intent = new Intent(this, CrenditralGuideActivity.class);
        intent.putExtra("credentialStateMedel", credentialStateMedel);
        startActivity(intent);
    }

    @Override
    public void verifyIndentitySuccessV3(CredentialStateMedel credentialStateMedel) {
        String stateCode;
        String stateName;
        this.credentialStateMedel = credentialStateMedel;
        if ("1001".equals(credentialStateMedel.getCustomerIdentity())) {
            stateCode = credentialStateMedel.getIdCardState();
            stateName = credentialStateMedel.getIdCardStateName();
        } else {
            stateCode = credentialStateMedel.getCredentialState();
            stateName = credentialStateMedel.getCredentialStateName();
        }
    }

    @Override
    public void getLivingCountSuccess(String s) {
        try {
            JSONObject js = new JSONObject(s);
            JSONObject result = js.getJSONObject("result");
            String failCount = result.getString("failCount");
            String validCode = result.getString("validCode");
            if ("1".equals(validCode)) {
                if ("3".equals(failCount)) {
                    Toast.makeText(this, "失败次数过多，", Toast.LENGTH_LONG).show();
                } else{
                    livingManger.startLivingMatch();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void getLivingCountError(Throwable error) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (swtichRelativeAssetObservable != null) {
            RxBus.get().unregister(RxConstant.GOTO_SWITCH_RELATIVE_ASSERT_IN_DATAMANAGE, swtichRelativeAssetObservable);
        }
        if (null != livingManger) {
            livingManger.destory();
        }
    }
}
