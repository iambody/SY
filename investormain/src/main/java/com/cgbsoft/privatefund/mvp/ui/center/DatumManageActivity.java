package com.cgbsoft.privatefund.mvp.ui.center;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.cgbsoft.privatefund.bean.living.FaceInf;
import com.cgbsoft.privatefund.bean.living.LivingResultData;
import com.cgbsoft.privatefund.bean.living.PersonCompare;
import com.cgbsoft.privatefund.model.CredentialModel;
import com.cgbsoft.lib.base.model.bean.CredentialStateMedel;
import com.cgbsoft.privatefund.mvp.contract.center.DatumManageContract;
import com.cgbsoft.privatefund.mvp.presenter.center.DatumManagePresenterImpl;
import com.cgbsoft.privatefund.mvp.ui.home.AssetProveActivity;
import com.cgbsoft.privatefund.mvp.ui.home.CrenditralGuideActivity;
import com.cgbsoft.privatefund.mvp.ui.home.RiskEvaluationActivity;
import com.cgbsoft.privatefund.widget.RightShareWebViewActivity;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import app.ocrlib.com.LivingManger;
import app.ocrlib.com.LivingResult;
import app.ocrlib.com.facepicture.FacePictureActivity;
import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;

/**
 * @author chenlong
 */
public class DatumManageActivity extends BaseActivity<DatumManagePresenterImpl> implements DatumManageContract.DatumManageView {
    public static final String TAG = "DatumManageActivity";
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
    private boolean isClickRisk;
    private LivingManger livingMangerPrivate;
    private Observable<FaceInf> complianceFaceupCallBack;
    private Observable<PersonCompare> register;
    private CredentialModel credentialModel;

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
        complianceFaceupCallBack = RxBus.get().register(RxConstant.COMPLIANCE_FACEUP, FaceInf.class);
        complianceFaceupCallBack.subscribe(new RxSubscriber<FaceInf>() {
            @Override
            protected void onEvent(FaceInf faceInf) {
                if (TAG.equals(faceInf.getPageTage())) {
                    List<String> remoteParams = new ArrayList<String>();
                    remoteParams.add(credentialModel.getImageUrl().get(0).getUrl());
                    getPresenter().uploadOtherCrendtial(remoteParams, credentialModel.getCode().substring(0, 4), credentialModel.getCode(), faceInf.getFaceRemotePath());
                }
            }

            @Override
            protected void onRxError(Throwable error) {

            }
        });
        startMatchLiving();
        register = RxBus.get().register(RxConstant.COMPLIANCE_PERSON_COMPARE, PersonCompare.class);
        register.subscribe(new RxSubscriber<PersonCompare>() {
            @Override
            protected void onEvent(PersonCompare personCompare) {
                //0代表成功 1代表失败  int值
                if (TAG.equals(personCompare.getCurrentPageTag())) {
                    if (0 == personCompare.getResultTage()) {
                        if (isClickRisk)
                            NavigationUtils.startActivity(DatumManageActivity.this, RiskEvaluationActivity.class);
                        Toast.makeText(baseContext,"身份验证通过",Toast.LENGTH_LONG).show();
                        DataStatistApiParam.sensitiveBodyExam(credentialModel.getCode(),"成功","拍照");

                    } else {
                        Toast.makeText(baseContext, "识别失败，请点击重试", Toast.LENGTH_LONG).show();
                        DataStatistApiParam.sensitiveBodyExam(credentialModel.getCode(),"失败","拍照");
                    }
                }
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
        RxBus.get().post(RxConstant.REFRESH_CREDENTIAL_INFO, 0);
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
        isClickRisk = true;
        goToDatumCollect();
    }

    private void goToDatumCollect() {
        if ("".equals(credentialStateMedel.getCustomerIdentity())) {
            NavigationUtils.startActivity(this, RiskEvaluationActivity.class);
        } else {
            if ("10".equals(credentialStateMedel.getCustomerType())) {
                if ("1001".equals(credentialStateMedel.getCustomerIdentity())) {
                    if ("50".equals(credentialStateMedel.getIdCardState())) {
                        if ("1".equals(credentialStateMedel.getCustomerLivingbodyState())) {
                            getPresenter().getLivingCount();
                        } else {
                            getDetial(credentialStateMedel.getCredentialDetailId());
                        }
                    } else if ("5".equals(credentialStateMedel.getIdCardState()) || "45".equals(credentialStateMedel.getCredentialState())) {
                        Intent intent = new Intent(baseContext, CrenditralGuideActivity.class);
                        intent.putExtra("credentialStateMedel", credentialStateMedel);
                        startActivity(intent);
                    } else if ("10".equals(credentialStateMedel.getIdCardState())) {
                        Toast.makeText(this, "证件正在审核中，通过后方可修改问卷", Toast.LENGTH_LONG).show();
                    } else {
                        Intent intent = new Intent(this, UploadIndentityCradActivity.class);
                        intent.putExtra("credentialStateMedel", credentialStateMedel);
                        startActivity(intent);
                    }
                } else {
                    if ("50".equals(credentialStateMedel.getCredentialState())) {
                        if ("1".equals(credentialStateMedel.getCustomerImageState())) {
                            startMatchImg();
                        } else {
                            getDetial(credentialStateMedel.getCredentialDetailId());
                        }
                    } else if ("5".equals(credentialStateMedel.getCredentialState()) || "45".equals(credentialStateMedel.getCredentialState())) {
                        Intent intent = new Intent(baseContext, CrenditralGuideActivity.class);
                        intent.putExtra("credentialStateMedel", credentialStateMedel);
                        startActivity(intent);
                    } else if ("10".equals(credentialStateMedel.getCredentialState())) {
                        Toast.makeText(this, "证件正在审核中，通过后方可修改问卷", Toast.LENGTH_LONG).show();
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

    private void getDetial(String credentialCode) {
        getPresenter().getCredentialDetial(credentialCode);
    }

    private void startMatchImg() {
        startActivity(new Intent(this, FacePictureActivity.class).putExtra(FacePictureActivity.TAG_NEED_PERSON, true).putExtra(FacePictureActivity.PAGE_TAG, TAG));
    }

    private void startMatchLiving() {
        livingManger = new LivingManger(this, "100101", "1001", new LivingResult() {
            @Override
            public void livingSucceed(LivingResultData resultData) {
                switch (resultData.getRecognitionCode()) {
                    case "0":
                        NavigationUtils.startActivity(DatumManageActivity.this, RiskEvaluationActivity.class);
                        DataStatistApiParam.sensitiveBodyExam(credentialModel.getCode(),"成功","活体");
                        break;
                    case "1":
                        Toast.makeText(baseContext, "识别失败。", Toast.LENGTH_LONG).show();
//                        Toast.makeText(baseContext, "识别成功进入客服审核。", Toast.LENGTH_LONG).show();
//                                NavigationUtils.startActivity(DatumManageActivity.this, RiskEvaluationActivity.class);
                        DataStatistApiParam.sensitiveBodyExam(credentialModel.getCode(),"失败","活体");
                        break;
                    case "2":
                        DataStatistApiParam.sensitiveBodyExam(credentialModel.getCode(),"失败","活体");
                        break;
                    case "3":
                        Toast.makeText(baseContext, "识别失败。", Toast.LENGTH_LONG).show();
                        DataStatistApiParam.sensitiveBodyExam(credentialModel.getCode(),"失败","活体");
                        break;
                }
            }

            @Override
            public void livingFailed(LivingResultData resultData) {

            }
        });

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
        isClickRisk = false;
        if (showAssert) {
            goToCardCollect();
        } else {
            GestureManager.showGroupGestureManage(this, GestureManager.RELATIVE_ASSERT_IN_DATDMANAGE);
        }
        DataStatistApiParam.cardCollect("资料管理");
    }

    private void credentialJump() {
        if (!TextUtils.isEmpty(credentialStateMedel.getCustomerIdentity())) {
            if ("1001".equals(credentialStateMedel.getCustomerIdentity())) {  //身份证
                if ("5".equals(credentialStateMedel.getIdCardState()) || "45".equals(credentialStateMedel.getIdCardState()) || ("50".equals(credentialStateMedel.getIdCardState()) && "0".equals(credentialStateMedel.getCustomerLivingbodyState()))) {
                    jumpGuidePage();
                } else if ("10".equals(credentialStateMedel.getIdCardState()) || "30".equals(credentialStateMedel.getIdCardState())) {
                    replenishCards();
                } else {  //已通过 核身成功
                    Intent intent = new Intent(baseContext, CardCollectActivity.class);
                    intent.putExtra("indentityCode", credentialStateMedel.getCustomerIdentity());
                    startActivity(intent);
                }
            } else {//  非大陆去证件列表
                Intent intent = new Intent(baseContext, CardCollectActivity.class);
                intent.putExtra("indentityCode", credentialStateMedel.getCustomerIdentity());
                startActivity(intent);
            }
        } else {//无身份
            Intent intent = new Intent(baseContext, SelectIndentityActivity.class);
            startActivity(intent);
        }
    }

    private void goToCardCollect() {
        credentialJump();
//        if ("".equals(credentialStateMedel.getCredentialCode())) {
//            Intent intent = new Intent(this, SelectIndentityActivity.class);
//            startActivity(intent);
//        } else {
//            if ("1001".equals(credentialStateMedel.getCustomerIdentity())) {
//                if ("50".equals(credentialStateMedel.getIdCardState()) && "1".equals(credentialStateMedel.getCustomerLivingbodyState())) {
//                    Intent intent1 = new Intent(this, CardCollectActivity.class);
//                    intent1.putExtra("indentityCode", credentialStateMedel.getCustomerIdentity());
//                    startActivity(intent1);
//                } else if (!("10".equals(credentialStateMedel.getIdCardState()) || "50".equals(credentialStateMedel))) {
//                    jumpGuidePage();
//                } else {
//                    replenishCards();
//                }
//            } else {
//                Intent intent1 = new Intent(this, CardCollectActivity.class);
//                intent1.putExtra("indentityCode", credentialStateMedel.getCustomerIdentity());
//                startActivity(intent1);
//            }
//        }

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
        assetRelative.setTip(credentialStateMedel.getCredentialStateName());
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
            //”0”:已过期。”1”:未过期。“2”：无历史 注意：没有活体验身历史的情况，返回空字符串。
            String validCode = result.getString("validCode");
            if ("0".equals(validCode)) {
                if ("3".equals(failCount)) {
                    Toast.makeText(this, "非常抱歉，您今日的人脸核身次数超过限制，请明日尝试", Toast.LENGTH_LONG).show();
                } else {
                    livingManger.startLivingMatch();
                }
            } else if ("1".equals(validCode)) {
                startMatchImg();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void getLivingCountError(Throwable error) {

    }

    @Override
    public void getCredentialDetialSuccess(CredentialModel credentialModel) {
        this.credentialModel = credentialModel;
        startMatch(credentialModel);
    }

    private void startMatch(CredentialModel credentialModel) {
        if (credentialModel.getCode().startsWith("1001")) {
            if (null != credentialModel) {
                List<String> urls = new ArrayList<>();
                String url = credentialModel.getImageUrl().get(0).getUrl();
                urls.add(url);
                if (credentialModel.getImageUrl().size() == 2) {
                    urls.add(credentialModel.getImageUrl().get(1).getUrl());
                }
                livingMangerPrivate = new LivingManger(baseContext, credentialModel.getCustomerName(), credentialModel.getNumberTrue(), credentialModel.getPeriodValidity(), credentialStateMedel.getCredentialCode(), "1001", "", "", "10", urls, new LivingResult() {
                    @Override
                    public void livingSucceed(LivingResultData resultData) {
                        Log.i("活体living", "开始回调监听接口！！！" + resultData.toString());
                        switch (resultData.getRecognitionCode()) {
                            //0 成功 1客服审核 2ocr错误 3标识失败
                            case "0":
                                NavigationUtils.startActivity(DatumManageActivity.this, RiskEvaluationActivity.class);
                                break;
                            case "1":
                                Toast.makeText(baseContext, "识别成功进入客服审核。", Toast.LENGTH_LONG).show();
//                                NavigationUtils.startActivity(DatumManageActivity.this, RiskEvaluationActivity.class);
                                break;
                            case "2":
                                break;
                            case "3":
                                Toast.makeText(baseContext, "识别失败。", Toast.LENGTH_LONG).show();
                                break;
                        }
                    }

                    @Override
                    public void livingFailed(LivingResultData resultData) {
                        Log.i("活体living", "开始回调监听接口失败了！" + resultData.toString());
                        LivingResultData resultData1 = resultData;
                        Toast.makeText(baseContext, resultData.getRecognitionMsg(), Toast.LENGTH_LONG).show();
                    }
                });
                livingMangerPrivate.startLivingMatch();
            }
        } else {
            startActivity(new Intent(baseContext, FacePictureActivity.class).putExtra(FacePictureActivity.PAGE_TAG, TAG));
        }
    }

    @Override
    public void getCredentialDetialError(Throwable error) {

    }

    @Override
    public void uploadOtherCrendtialSuccess(String s) {
        NavigationUtils.startActivity(DatumManageActivity.this, RiskEvaluationActivity.class);
    }

    @Override
    public void uploadOtherCrendtialError(Throwable error) {

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
        if (null != livingMangerPrivate) {
            livingMangerPrivate.destory();
        }
        if (null != complianceFaceupCallBack) {
            RxBus.get().unregister(RxConstant.COMPLIANCE_FACEUP, complianceFaceupCallBack);
        }
        if (null != register) {
            RxBus.get().unregister(RxConstant.COMPLIANCE_PERSON_COMPARE, register);
        }
    }
}
