package com.cgbsoft.privatefund.mvp.ui.center;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.base.webview.WebViewConstant;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.permission.MyPermissionsActivity;
import com.cgbsoft.lib.permission.MyPermissionsChecker;
import com.cgbsoft.lib.utils.constant.RxConstant;
import com.cgbsoft.lib.utils.dm.Utils.helper.FileUtils;
import com.cgbsoft.lib.utils.imgNetLoad.Imageload;
import com.cgbsoft.lib.utils.rxjava.RxBus;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.utils.tools.DownloadUtils;
import com.cgbsoft.lib.utils.tools.NavigationUtils;
import com.cgbsoft.lib.utils.tools.ThreadUtils;
import com.cgbsoft.lib.utils.tools.UiSkipUtils;
import com.cgbsoft.lib.widget.dialog.LoadingDialog;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.bean.living.FaceInf;
import com.cgbsoft.privatefund.bean.living.IdentityCard;
import com.cgbsoft.privatefund.bean.living.LivingResultData;
import com.cgbsoft.privatefund.model.CredentialModel;
import com.cgbsoft.privatefund.model.CredentialStateMedel;
import com.cgbsoft.privatefund.mvp.contract.center.UploadIndentityContract;
import com.cgbsoft.privatefund.mvp.presenter.center.UploadIndentityPresenterImpl;
import com.cgbsoft.privatefund.utils.Bimp;
import com.cgbsoft.privatefund.utils.StorageKit;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.ocrlib.com.LivingManger;
import app.ocrlib.com.LivingResult;
import app.ocrlib.com.facepicture.FacePictureActivity;
import app.ocrlib.com.identitycard.IdentityCardActivity;
import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;

import static com.cgbsoft.lib.utils.constant.RxConstant.SELECT_INDENTITY;
import static com.cgbsoft.lib.utils.constant.RxConstant.SELECT_INDENTITY_ADD;

/**
 * Created by fei on 2017/8/12.
 */

public class UploadIndentityCradActivity extends BaseActivity<UploadIndentityPresenterImpl> implements UploadIndentityContract.UploadIndentityView {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.title_mid)
    TextView titleTV;
    @BindView(R.id.iv_upload_crad_first)
    ImageView uploadFirst;
    @BindView(R.id.iv_upload_crad_second)
    ImageView uploadSecond;
    @BindView(R.id.tv_upload_indentity_tip)
    TextView tagTv;
    @BindView(R.id.upload_submit)
    Button submit;
    @BindView(R.id.iv_upload_tag)
    ImageView tagIv;
    @BindView(R.id.ll_defeat_all)
    LinearLayout defeatAll;
    @BindView(R.id.tv_defeat_title)
    TextView defeatTitle;
    @BindView(R.id.tv_defeat_depict)
    TextView defeatDepict;
    @BindView(R.id.iv_upload_card_first_cover)
    ImageView uploadFirstCover;
    @BindView(R.id.iv_upload_card_second_cover)
    ImageView uploadSecondCover;
    @BindView(R.id.rl_replenish_card_all)
    RelativeLayout replenishAll;
    @BindView(R.id.tv_replenish_name)
    TextView replenishName;
    @BindView(R.id.tv_replenish_identitynum)
    TextView replenishNum;
    @BindView(R.id.ll_recognition_name_edit)
    LinearLayout recognitionNameLinearLayout;
    @BindView(R.id.ll_recognition_idcard_num)
    LinearLayout recognitionNumLinearLayout;
    @BindView(R.id.ll_recognition_validDate)
    LinearLayout recognitionValidLinearLayout;
    @BindView(R.id.recognition_name_edit)
    TextView recognitionNameEdit;
    @BindView(R.id.recognition_idcard_num)
    TextView recognitionIdCardNum;
    @BindView(R.id.recognition_validDate)
    TextView recognitionValidDate;
    @BindView(R.id.rl_tip)
    RelativeLayout rlTip;
    @BindView(R.id.bt_recognition_name_edit)
    Button btRecognitionNameEdit;
    @BindView(R.id.recognition_num_text)
    TextView recognitionNumText;
    @BindView(R.id.recognition_name_text)
    TextView recognitionNameText;
    @BindView(R.id.recognition_result_text)
    TextView recognitionResultText;
    @BindView(R.id.mini_result_ll)
    LinearLayout miniResultLinear;
    @BindView(R.id.mini_msg_layout)
    LinearLayout miniMsgLyout;
    @BindView(R.id.upload_demo)
    TextView uploadDemo;
    @BindView(R.id.rl_recognition_card)
    LinearLayout RecognitionCardRelative;
    @BindView(R.id.reject_result_title)
    TextView rejectResultTitle;
    @BindView(R.id.divide_line1)
    View divideLine1;
    @BindView(R.id.divide_line2)
    View divideLine2;

    private LoadingDialog mLoadingDialog;
    private String firstPhotoPath;
    private String secondPhotoPath;
    /**
     * 更新头像，拍照REQUEST_CODE
     */
    private static final int FIRST_REQUEST_CARD_CAMERA = 1440;
    private static final int FIRST_REQUEST_CROP = 1443;
    private static final int SECOND_REQUEST_CARD_CAMERA = 1441;
    private static final int SECOND_REQUEST_CROP = 1442;
    private static int CARD_WIDTH = 320;//宽度
    private static int CARD_HEIGHT = 220;//高度
    private final String firstPhotoName = "first.jpg";
    private final String secondPhotoName = "second.jpg";
    private final String firstCropPhotoName = "firstCrop.jpg";
    private final String secondCropPhotoName = "secondCrop.jpg";
    private boolean isIdCard;
    private List<String> remoteParams = new ArrayList<>();
    private MyPermissionsChecker mPermissionsChecker;
    private String[] PERMISSIONS = new String[]{Manifest.permission.CAMERA};
    private int REQUEST_CODE_PERMISSON_FIRST = 2000; // 请求码
    private int REQUEST_CODE_PERMISSON_SECOND = 2002; // 请求码
    private boolean isFromSelectIndentity;
    //身份证正面拍照回调
    private Observable<IdentityCard> registerFrontCallBack;
    //身份证反面拍照回调
    private Observable<IdentityCard> registerBackCallBack;
    //身份证+活体识别回调
    private Observable<LivingResultData> registerPrivateLivingCallBack;
    //普通活体识别回调
    private Observable<LivingResultData> registerCommonLivingCallBack;
    private CredentialStateMedel credentialStateMedel;
    private CredentialModel credentialModel;
    private IdentityCard identityCard;
    private LivingManger livingManger;
    private Observable<FaceInf> complianceFaceupCallBack;

    private void startPermissionsActivity(int permissionCode) {
        MyPermissionsActivity.startActivityForResult(this, permissionCode, PERMISSIONS);
    }

    @OnClick(R.id.iv_upload_crad_first)
    public void uploadFirstClick() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (null == mPermissionsChecker) {
                mPermissionsChecker = new MyPermissionsChecker(this);
            }
            // 缺少权限时, 进入权限配置页面
            if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
                startPermissionsActivity(REQUEST_CODE_PERMISSON_FIRST);
                return;
            }
        }
        // 拍照
        if (credentialModel.getCode().equals("100101"))
            UiSkipUtils.toNextActivityWithIntent(this, new Intent(this, IdentityCardActivity.class).putExtra(IdentityCardActivity.CARD_FACE, IdentityCardActivity.FACE_FRONT));
        else
            takePhotoByCamera(firstPhotoName, FIRST_REQUEST_CARD_CAMERA);
    }

    @OnClick(R.id.bt_recognition_name_edit)
    public void editName() {
        recognitionNameEdit.setFocusable(true);
    }

    @OnClick(R.id.upload_demo)
    public void uploadDemo() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(WebViewConstant.push_message_url, "/app5.0/order/help.html");
        hashMap.put(WebViewConstant.push_message_title, "上传示例");
        NavigationUtils.startActivityByRouter(this, RouteConfig.GOTO_BASE_WEBVIEW, hashMap);
    }

    @OnClick(R.id.iv_upload_card_first_cover)
    public void uploadFirstCoverClick() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (null == mPermissionsChecker) {
                mPermissionsChecker = new MyPermissionsChecker(this);
            }
            // 缺少权限时, 进入权限配置页面
            if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
                startPermissionsActivity(REQUEST_CODE_PERMISSON_FIRST);
                return;
            }
        }
        // 拍照
        if (credentialModel.getCode().equals("100101"))
            UiSkipUtils.toNextActivityWithIntent(this, new Intent(this, IdentityCardActivity.class).putExtra(IdentityCardActivity.CARD_FACE, IdentityCardActivity.FACE_FRONT));
        else
            takePhotoByCamera(secondPhotoName, FIRST_REQUEST_CARD_CAMERA);
    }

    @OnClick(R.id.iv_upload_crad_second)
    public void uploadSecondClick() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (null == mPermissionsChecker) {
                mPermissionsChecker = new MyPermissionsChecker(this);
            }
            // 缺少权限时, 进入权限配置页面
            if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
                startPermissionsActivity(REQUEST_CODE_PERMISSON_SECOND);
                return;
            }
        }
        // 拍照
        if (credentialModel.getCode().equals("100101"))
            UiSkipUtils.toNextActivityWithIntent(this, new Intent(this, IdentityCardActivity.class).putExtra(IdentityCardActivity.CARD_FACE, IdentityCardActivity.FACE_BACK));
        else
            takePhotoByCamera(secondPhotoName, SECOND_REQUEST_CARD_CAMERA);
    }

    @OnClick(R.id.iv_upload_card_second_cover)
    public void uploadSecondCoverClick() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (null == mPermissionsChecker) {
                mPermissionsChecker = new MyPermissionsChecker(this);
            }
            // 缺少权限时, 进入权限配置页面
            if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
                startPermissionsActivity(REQUEST_CODE_PERMISSON_SECOND);
                return;
            }
        }
        // 拍照
        if (credentialModel.getCode().equals("100101"))
            UiSkipUtils.toNextActivityWithIntent(this, new Intent(this, IdentityCardActivity.class).putExtra(IdentityCardActivity.CARD_FACE, IdentityCardActivity.FACE_BACK));
        else
            takePhotoByCamera(secondPhotoName, SECOND_REQUEST_CARD_CAMERA);
    }

    @OnClick(R.id.upload_submit)
    public void photoSubmit() {
        submit.setEnabled(false);
        List<String> paths = new ArrayList<>();
        if (isIdCard) {
            if (TextUtils.isEmpty(firstPhotoPath) && TextUtils.isEmpty(secondPhotoPath)) {
                Toast.makeText(getApplicationContext(), "请点击拍摄证件照照片", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!TextUtils.isEmpty(firstPhotoPath) && TextUtils.isEmpty(secondPhotoPath)) {
                Toast.makeText(getApplicationContext(), "请点击拍摄反面证件照照片", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(firstPhotoPath) && !TextUtils.isEmpty(secondPhotoPath)) {
                Toast.makeText(getApplicationContext(), "请点击拍摄正面证件照照片", Toast.LENGTH_SHORT).show();
                return;
            }
            JSONArray idcardImages = new JSONArray();
            JSONObject frontImage = new JSONObject();
            JSONObject backImage = new JSONObject();
            try {
                frontImage.put("name", "frontImage");
                frontImage.put("url", firstPhotoPath);
                backImage.put("name", "backImage");
                backImage.put("url", secondPhotoPath);
                idcardImages.put(frontImage);
                idcardImages.put(backImage);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            List<Map<String, Object>> cardmaps = new ArrayList<>();
            Map<String, Object> mapfront = new HashMap();
            mapfront.put("name", "frontImage");
            mapfront.put("url", firstPhotoPath);
            Map<String, Object> mapback = new HashMap();
            mapback.put("name", "backImage");
            mapback.put("url", secondPhotoPath);
            cardmaps.add(mapfront);
            cardmaps.add(mapback);
            List<String> remotePths = new ArrayList<>();
            remotePths.add(firstPhotoPath);
            remotePths.add(secondPhotoPath);
            //0 成功 1客服审核 2ocr错误 3标识失败
            livingManger = new LivingManger(baseContext, identityCard.getIdCardName(), identityCard.getIdCardNum(), identityCard.getValidDate(), credentialStateMedel.getCredentialCode(), "1001", identityCard.getSex(), identityCard.getBirth(), "10", remotePths, new LivingResult() {
                @Override
                public void livingSucceed(LivingResultData resultData) {
                    Log.i("活体living", "开始回调监听接口！！！" + resultData.toString());
                    switch (resultData.getRecognitionCode()) {
                        //0 成功 1客服审核 2ocr错误 3标识失败
                        case "0":
                            submit.setVisibility(View.GONE);
                            tagIv.setVisibility(View.GONE);
                            tagTv.setText("审核通过");
                            RecognitionCardRelative.setVisibility(View.GONE);
                            miniResultLinear.setVisibility(View.VISIBLE);
                            miniMsgLyout.setVisibility(View.VISIBLE);
                            recognitionNameText.setText(identityCard.getIdCardName());
                            recognitionNumText.setText(identityCard.getIdCardNum());
                            recognitionResultText.setText("审核成功");
                            rejectResultTitle.setText("审核结果");
                            break;
                        case "1":
                            submit.setVisibility(View.GONE);
                            tagIv.setVisibility(View.GONE);
                            tagTv.setText("审核中");
                            RecognitionCardRelative.setVisibility(View.GONE);
                            miniResultLinear.setVisibility(View.VISIBLE);
                            miniMsgLyout.setVisibility(View.VISIBLE);
                            recognitionNameText.setText(identityCard.getIdCardName());
                            recognitionNumText.setText(identityCard.getIdCardNum());
                            recognitionResultText.setText("审核中");
                            rejectResultTitle.setText("审核结果");
                            finish();
                            break;
                        case "2":
                            Toast.makeText(baseContext, resultData.getRecognitionMsg(), Toast.LENGTH_LONG).show();
                            break;
                        case "3":
                            Toast.makeText(baseContext, resultData.getRecognitionMsg(), Toast.LENGTH_LONG).show();
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
            livingManger.startLivingMatch();
            return;

//            File fileFirst = new File(firstPhotoPath);
//            File fileSecond = new File(secondPhotoPath);
//            if (!fileFirst.exists() && !fileSecond.exists()) {
//                Toast.makeText(getApplicationContext(), "请点击拍摄证件照照片", Toast.LENGTH_SHORT).show();
//                return;
//            }
//            if (fileFirst.exists() && !fileSecond.exists()) {
//                Toast.makeText(getApplicationContext(), "请点击拍摄反面证件照照片", Toast.LENGTH_SHORT).show();
//                return;
//            }
//            if (!fileFirst.exists() && fileSecond.exists()) {
//                Toast.makeText(getApplicationContext(), "请点击拍摄正面证件照照片", Toast.LENGTH_SHORT).show();
//                return;
//            }
        } else {
            if (!"50".equals(credentialModel.getStateCode())) {
                if (TextUtils.isEmpty(firstPhotoPath)) {
                    Toast.makeText(getApplicationContext(), "请点击拍摄证件照照片", Toast.LENGTH_SHORT).show();
                    return;
                }
                File fileFirst = new File(firstPhotoPath);
                if (!fileFirst.exists()) {
                    Toast.makeText(getApplicationContext(), "请点击拍摄证件照照片", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }
        if (isIdCard) {
            if ("50".equals(credentialModel.getStateCode())){
                remoteParams.clear();
                remoteParams.add(firstPhotoName);
                remoteParams.add(secondPhotoPath);
            }else {
                paths.add(firstPhotoPath);
                paths.add(secondPhotoPath);
            }
        } else {
            if ("50".equals(credentialModel.getStateCode())){
                remoteParams.clear();
                remoteParams.add(firstPhotoName);
            }else {
                paths.add(firstPhotoPath);
            }
        }
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(this);
        }
        if ((!"1001".equals(credentialStateMedel.getCustomerIdentity())) && "10".equals(credentialStateMedel.getCustomerType())) {
            mLoadingDialog.show();
            if (remoteParams.size()!=0){
                startActivity(new Intent(baseContext, FacePictureActivity.class));
                return;
            }
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    remoteParams.clear();
                    for (final String localPath : paths) {
                        String newTargetFile = FileUtils.compressFileToUpload(localPath, true);
                        String paths = DownloadUtils.postSecretObject(newTargetFile, "credential/" + credentialModel.getCode() + "/");
                        FileUtils.deleteFile(newTargetFile);
                        if (!TextUtils.isEmpty(paths)) {
                            remoteParams.add(paths);
                        } else {
                            ThreadUtils.runOnMainThread(() -> Toast.makeText(UploadIndentityCradActivity.this, "证件上传失败，请重新上传", Toast.LENGTH_SHORT).show());
                            submit.setEnabled(true);
                            mLoadingDialog.dismiss();
                            return;
                        }
                    }
                    ThreadUtils.runOnMainThread(() -> startActivity(new Intent(baseContext, FacePictureActivity.class)));
                }
            }.start();
            return;
        }
        mLoadingDialog.show();
        new Thread() {
            @Override
            public void run() {
                super.run();
                remoteParams.clear();
                for (final String localPath : paths) {
                    String newTargetFile = FileUtils.compressFileToUpload(localPath, true);
                    String paths = DownloadUtils.postSecretObject(newTargetFile, "credential/" + credentialModel.getCode() + "/");
                    FileUtils.deleteFile(newTargetFile);
                    if (!TextUtils.isEmpty(paths)) {
                        remoteParams.add(paths);
                    } else {
                        ThreadUtils.runOnMainThread(() -> Toast.makeText(UploadIndentityCradActivity.this, "证件上传失败，请重新上传", Toast.LENGTH_SHORT).show());
                        submit.setEnabled(true);
                        mLoadingDialog.dismiss();
                        return;
                    }
                }
                uploadRemotePaths();
            }
        }.start();
    }

    private void uploadRemotePaths() {
        getPresenter().uploadIndentity(remoteParams, credentialModel.getCode().substring(0, 3), credentialModel.getCode());
    }

    @Override
    public void showLoadDialog() {
        try {
            if (mLoadingDialog.isShowing()) {
                return;
            }
            submit.setEnabled(false);
            mLoadingDialog.show();
        } catch (Exception e) {
        }
    }

    @Override
    public void hideLoadDialog() {
        submit.setEnabled(true);
        mLoadingDialog.dismiss();
    }

    @Override
    public void uploadIndentitySuccess(String result) {
        if (TextUtils.isEmpty(result)) {
            Toast.makeText(getApplicationContext(), "上传成功!", Toast.LENGTH_SHORT).show();
            RxBus.get().post(SELECT_INDENTITY, 0);
            RxBus.get().post(SELECT_INDENTITY_ADD, 0);
            if (isFromSelectIndentity) {
                Intent intent = new Intent(this, CardCollectActivity.class);
                intent.putExtra("indentityCode", credentialModel.getCode().substring(0, 3));
                startActivity(intent);
            }
            this.finish();
        } else {
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void uploadIndentityError(Throwable error) {
        Toast.makeText(getApplicationContext(), "上传失败,请稍后重试!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void credentialDetialSuccess(String s) {
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONObject result = jsonObject.getJSONObject("result");
            Gson g = new Gson();
            credentialModel = g.fromJson(result.toString(), CredentialModel.class);
            init(credentialModel);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void init(CredentialModel credentialModel) {
        identityCard = new IdentityCard();
        String stateCode = credentialModel.getStateCode();
        if (TextUtils.isEmpty(stateCode) || "5".equals(stateCode)) {//未上传
            uploadFirst.setEnabled(true);
            uploadSecond.setEnabled(true);
            submit.setVisibility(View.VISIBLE);
            tagTv.setVisibility(View.VISIBLE);
            switch (credentialModel.getCode()) {
                case "100101"://身份证
                    isIdCard = true;
                    uploadSecond.setVisibility(View.VISIBLE);
                    uploadFirst.setImageDrawable(getResources().getDrawable(R.drawable.upload_id_card_front));
                    uploadSecond.setImageDrawable(getResources().getDrawable(R.drawable.upload_id_card_back));
                    tagTv.setText("请拍摄实体身份证");
                    break;
                case "100102"://中国护照
                    uploadFirst.setImageDrawable(getResources().getDrawable(R.drawable.upload_china_passport));
                    tagTv.setText("请拍摄实体护照");
                    break;
                case "100401"://外籍护照
                    tagTv.setText("请拍摄实体护照");
                    uploadFirst.setImageDrawable(getResources().getDrawable(R.drawable.upload_foreign_passport));
                    break;
                case "100201"://港澳来往内地通行证
                    uploadFirst.setImageDrawable(getResources().getDrawable(R.drawable.upload_province_gangao_to_inland));
                    tagTv.setText("请拍摄实体通行证");
                    break;
                case "100301"://台湾来往内地通行证
                    uploadFirst.setImageDrawable(getResources().getDrawable(R.drawable.upload_province_taiwan_to_inland));
                    tagTv.setText("请拍摄实体通行证");
                    break;
                case "100103"://军官证
                    uploadFirst.setImageDrawable(getResources().getDrawable(R.drawable.upload_officer_card));
                    tagTv.setText("请拍摄实体军官证");
                    break;
                case "100104"://士兵证
                    uploadFirst.setImageDrawable(getResources().getDrawable(R.drawable.upload_soldier_card));
                    tagTv.setText("请拍摄实体士兵证");
                    break;
                case "200101"://营业执照
                    uploadFirst.setImageDrawable(getResources().getDrawable(R.drawable.upload_business_license));
                    tagTv.setText("请拍摄实体营业执照");
                    break;
                case "200102"://组织机构代码证
                    uploadFirst.setImageDrawable(getResources().getDrawable(R.drawable.upload_institution_card));
                    tagTv.setText("请拍摄实体组织机构代码证");
                    break;
                case "100105"://港澳通行证
                    uploadFirst.setImageDrawable(getResources().getDrawable(R.drawable.upload_provinde_inland_to_gangao));
                    tagTv.setText("请拍摄实体通行证");
                    break;
            }
        } else {//已上传，显示详情
            tagTv.setVisibility(View.VISIBLE);
            uploadFirst.setEnabled(false);
            uploadSecond.setEnabled(false);
            submit.setVisibility(View.GONE);
            if ("100101".equals(credentialModel.getCode())) {
                isIdCard = true;
            }
            if ("30".equals(stateCode) || "70".equals(stateCode)) {
                if ("100101".equals(credentialModel.getCode())) {
                    isIdCard = true;
                }
                uploadFirst.setEnabled(true);
                uploadSecond.setEnabled(true);
                submit.setVisibility(View.VISIBLE);
                if ("30".equals(stateCode)) {//30：已驳回
                    defeatAll.setVisibility(View.VISIBLE);
                    if (TextUtils.isEmpty(credentialModel.getNumber())) {
                        defeatTitle.setText(TextUtils.isEmpty(credentialModel.getComment()) ? "" : "失败原因:");
                        defeatDepict.setText(TextUtils.isEmpty(credentialModel.getComment()) ? "" : credentialModel.getComment());
                        miniMsgLyout.setVisibility(View.VISIBLE);
                        rlTip.setVisibility(View.GONE);
                        recognitionNumText.setText(credentialModel.getNumber());
                        recognitionNameText.setText(credentialModel.getCustomerName());
                        recognitionResultText.setText(credentialModel.getComment());
                    } else {
                        miniMsgLyout.setVisibility(View.VISIBLE);
                        recognitionNameText.setText(credentialModel.getCustomerName());
                        recognitionNumText.setText(credentialModel.getNumber());
                        recognitionResultText.setText(credentialModel.getComment());
                    }
                }
                if ("70".equals(stateCode)) {//70：已过期
                    defeatAll.setVisibility(View.VISIBLE);
                    tagTv.setVisibility(View.VISIBLE);
                    tagIv.setVisibility(View.VISIBLE);
                    tagIv.setImageDrawable(getResources().getDrawable(R.drawable.upload_indentity_error_tag));
                    tagTv.setText(TextUtils.isEmpty(credentialModel.getStateName()) ? "" : credentialModel.getStateName());
                    uploadFirstCover.setVisibility(View.VISIBLE);
                    defeatTitle.setText(TextUtils.isEmpty(credentialModel.getComment()) ? "" : "失败原因:");
                    defeatDepict.setText(TextUtils.isEmpty(credentialModel.getComment()) ? "" : credentialModel.getComment());
                }
            }
            if ("10".equals(stateCode)) {
                tagTv.setText("审核中");
                miniResultLinear.setVisibility(View.GONE);
                recognitionNumText.setText(credentialModel.getNumber());
                recognitionNameText.setText(credentialModel.getCustomerName());
                submit.setVisibility(View.GONE);
            }
            if ("50".equals(stateCode)) {
                tagTv.setText("已通过");
                miniMsgLyout.setVisibility(View.VISIBLE);
                rlTip.setVisibility(View.GONE);
                miniResultLinear.setVisibility(View.GONE);
                recognitionNumText.setText(credentialModel.getNumber());
                recognitionNameText.setText(credentialModel.getCustomerName());
            }
            if ("45".equals(stateCode)) {
                miniMsgLyout.setVisibility(View.VISIBLE);
                rlTip.setVisibility(View.GONE);
                miniResultLinear.setVisibility(View.GONE);
                recognitionNumText.setText(credentialModel.getNumber());
                recognitionNameText.setText(credentialModel.getCustomerName());
//                replenishAll.setVisibility(View.VISIBLE);
                replenishName.setText(credentialModel.getCustomerName());
                replenishNum.setText(credentialModel.getNumber());
                uploadFirst.setEnabled(true);
                uploadSecond.setEnabled(true);
                submit.setVisibility(View.VISIBLE);
                uploadFirst.setImageDrawable(getResources().getDrawable(R.drawable.upload_id_card_front));
                switch (credentialModel.getCode()) {
                    case "100101"://身份证
                        isIdCard = true;
                        uploadSecond.setVisibility(View.VISIBLE);
                        uploadFirst.setImageDrawable(getResources().getDrawable(R.drawable.upload_id_card_front));
                        uploadSecond.setImageDrawable(getResources().getDrawable(R.drawable.upload_id_card_back));
                        tagTv.setText("请拍摄实体身份证");
                        break;
                    case "100102"://中国护照
                        uploadFirst.setImageDrawable(getResources().getDrawable(R.drawable.upload_china_passport));
                        tagTv.setText("请拍摄实体护照");
                        break;
                    case "100401"://外籍护照
                        tagTv.setText("请拍摄实体护照");
                        uploadFirst.setImageDrawable(getResources().getDrawable(R.drawable.upload_foreign_passport));
                        break;
                    case "100201"://港澳来往内地通行证
                        uploadFirst.setImageDrawable(getResources().getDrawable(R.drawable.upload_province_gangao_to_inland));
                        tagTv.setText("请拍摄实体通行证");
                        break;
                    case "100301"://台湾来往内地通行证
                        uploadFirst.setImageDrawable(getResources().getDrawable(R.drawable.upload_province_taiwan_to_inland));
                        tagTv.setText("请拍摄实体通行证");
                        break;
                    case "100103"://军官证
                        uploadFirst.setImageDrawable(getResources().getDrawable(R.drawable.upload_officer_card));
                        tagTv.setText("请拍摄实体军官证");
                        break;
                    case "100104"://士兵证
                        uploadFirst.setImageDrawable(getResources().getDrawable(R.drawable.upload_soldier_card));
                        tagTv.setText("请拍摄实体士兵证");
                        break;
                    case "200101"://营业执照
                        uploadFirst.setImageDrawable(getResources().getDrawable(R.drawable.upload_business_license));
                        tagTv.setText("请拍摄实体营业执照");
                        break;
                    case "200102"://组织机构代码证
                        uploadFirst.setImageDrawable(getResources().getDrawable(R.drawable.upload_institution_card));
                        tagTv.setText("请拍摄实体组织机构代码证");
                        break;
                    case "100105"://港澳通行证
                        uploadFirst.setImageDrawable(getResources().getDrawable(R.drawable.upload_provinde_inland_to_gangao));
                        tagTv.setText("请拍摄实体通行证");
                        break;
                }
            } else {
                replenishAll.setVisibility(View.GONE);
                if (null != credentialModel.getImageUrl()) {
                    String firstUrl = credentialModel.getImageUrl().get(0).getUrl();
                    firstPhotoPath = firstUrl;
                    Imageload.display(this, firstUrl, uploadFirst);
                    if (credentialModel.getImageUrl().size() == 2) {
                        String secondUrl = credentialModel.getImageUrl().get(1).getUrl();
                        if (!TextUtils.isEmpty(secondUrl)) {
                            uploadSecond.setVisibility(View.VISIBLE);
                            Imageload.display(this, secondUrl, uploadSecond);
                            secondPhotoPath = secondUrl;

                        }
                        if ("70".equals(stateCode)) {
                            uploadSecondCover.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        }
        titleTV.setText(credentialModel.getName());
        if ("50".equals(credentialModel.getStateCode()) && "2".equals(credentialModel.getValidCode()) && credentialModel.getCode().startsWith("10")) {
            submit.setText("下一步");
            submit.setVisibility(View.VISIBLE);
            miniMsgLyout.setVisibility(View.VISIBLE);
            rlTip.setVisibility(View.GONE);
            identityCard.setIdCardNum(credentialModel.getNumberTrue());
            identityCard.setIdCardName(credentialModel.getCustomerName());
        }

    }

    @Override
    public void credentialDetialError(Throwable error) {
        Toast.makeText(getApplicationContext(), "获取证件详情失败！", Toast.LENGTH_SHORT).show();
        this.finish();
    }

    @Override
    public void uploadOtherCrenditral(String result) {
        if (!TextUtils.isEmpty(result)) {
            Toast.makeText(getApplicationContext(), "上传成功!", Toast.LENGTH_SHORT).show();
            RxBus.get().post(SELECT_INDENTITY, 0);
            RxBus.get().post(SELECT_INDENTITY_ADD, 0);
            submit.setVisibility(View.GONE);
            tagTv.setText("审核中");
            tagIv.setVisibility(View.GONE);
//            Intent intent = new Intent(this, CardCollectActivity.class);
//            intent.putExtra("indentityCode", credentialModel.getCode().substring(0, 4));
//            startActivity(intent);
            this.finish();
        } else {
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected int layoutID() {
        return R.layout.activity_upload_indentity;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initView();
        initIdCardSelCallBack();
        recognitionNameEdit.setFocusable(false);
        credentialStateMedel = (CredentialStateMedel) getIntent().getSerializableExtra("credentialStateMedel");
        if (!TextUtils.isEmpty(credentialStateMedel.getCredentialDetailId())) {
            getCredentialInfo(credentialStateMedel.getCredentialDetailId());
        } else {
            credentialModel = new CredentialModel("", null,
                    credentialStateMedel.getCredentialStateName(),
                    credentialStateMedel.getCredentialTypeName(),
                    credentialStateMedel.getCredentialState(),
                    "",
                    "",
                    credentialStateMedel.getCredentialCode(),
                    "", "", "", "");
            init(credentialModel);
        }
        isFromSelectIndentity = getIntent().getBooleanExtra("isFromSelectIndentity", false);
    }

    private void getCredentialInfo(String credentialId) {
        getPresenter().getCredentialInfo(credentialId);
    }

    private void initIdCardSelCallBack() {
        registerFrontCallBack = RxBus.get().register(RxConstant.COMPLIANCE_CARD_FRONT, IdentityCard.class);
        registerBackCallBack = RxBus.get().register(RxConstant.COMPLIANCE_CARD_BACK, IdentityCard.class);
        complianceFaceupCallBack = RxBus.get().register(RxConstant.COMPLIANCE_FACEUP, FaceInf.class);
        complianceFaceupCallBack.subscribe(new RxSubscriber<FaceInf>() {
            @Override
            protected void onEvent(FaceInf faceInf) {
                Log.i("PersonCompare", "我没进行对比接受到了通知"  );
                getPresenter().uploadOtherCrenditial(remoteParams, credentialModel.getCode().substring(0, 4), credentialModel.getCode(), faceInf.getFaceRemotePath());
            }

            @Override
            protected void onRxError(Throwable error) {

            }
        });
        registerFrontCallBack.subscribe(new RxSubscriber<IdentityCard>() {
            @Override
            protected void onEvent(IdentityCard identityCard) {
                Imageload.display(UploadIndentityCradActivity.this, identityCard.getRemotPath(), uploadFirst);
                firstPhotoPath = identityCard.getRemotPath();
                if ("1".equals(identityCard.getAnalysisType())) {
                    UploadIndentityCradActivity.this.identityCard.setAddress(identityCard.getAddress());
                    UploadIndentityCradActivity.this.identityCard.setBirth(identityCard.getBirth());
                    UploadIndentityCradActivity.this.identityCard.setIdCardName(identityCard.getIdCardName());
                    UploadIndentityCradActivity.this.identityCard.setIdCardNum(identityCard.getIdCardNum());
                    UploadIndentityCradActivity.this.identityCard.setSex(identityCard.getSex());
                    recognitionNameEdit.setText(identityCard.getIdCardName());
                    recognitionIdCardNum.setText(identityCard.getIdCardNum());
                    recognitionNameLinearLayout.setVisibility(View.VISIBLE);
                    recognitionNumLinearLayout.setVisibility(View.VISIBLE);
                    rlTip.setVisibility(View.GONE);
                    divideLine1.setVisibility(View.VISIBLE);
                    if (!TextUtils.isEmpty(identityCard.getValidDate())) {
                        divideLine2.setVisibility(View.VISIBLE);
                    }
                    miniMsgLyout.setVisibility(View.GONE);
                } else {
                    recognitionNameLinearLayout.setVisibility(View.GONE);
                    recognitionNumLinearLayout.setVisibility(View.GONE);
                }

            }

            @Override
            protected void onRxError(Throwable error) {

            }
        });

        registerBackCallBack.subscribe(new RxSubscriber<IdentityCard>() {
            @Override
            protected void onEvent(IdentityCard identityCard) {
                Imageload.display(UploadIndentityCradActivity.this, identityCard.getRemotPath(), uploadSecond);
                secondPhotoPath = identityCard.getRemotPath();
                if ("1".equals(identityCard.getAnalysisType())) {
                    UploadIndentityCradActivity.this.identityCard.setValidDate(identityCard.getValidDate());
                    recognitionValidLinearLayout.setVisibility(View.VISIBLE);
                    recognitionValidDate.setText(identityCard.getValidDate());
                    rlTip.setVisibility(View.GONE);
                    if (!TextUtils.isEmpty(identityCard.getIdCardNum())) {
                        divideLine2.setVisibility(View.VISIBLE);
                    }
                    miniMsgLyout.setVisibility(View.GONE);
                } else {
                    recognitionValidLinearLayout.setVisibility(View.GONE);
                }
            }

            @Override
            protected void onRxError(Throwable error) {

            }
        });
    }

    private void initView() {
        mLoadingDialog = LoadingDialog.getLoadingDialog(this, "", false, false);
    }

    @Override
    protected UploadIndentityPresenterImpl createPresenter() {
        return new UploadIndentityPresenterImpl(this, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PERMISSON_FIRST) {
            uploadFirstClick();
        } else if (requestCode == REQUEST_CODE_PERMISSON_SECOND) {
            uploadSecondClick();
        } else if (requestCode == FIRST_REQUEST_CARD_CAMERA) {
            if (resultCode == Activity.RESULT_OK) {
                handlePhotoResult(firstCropPhotoName, FIRST_REQUEST_CROP);
            }
        } else if (requestCode == SECOND_REQUEST_CARD_CAMERA) {
            if (resultCode == Activity.RESULT_OK) {
                handlePhotoResult(secondCropPhotoName, SECOND_REQUEST_CROP);
            }
        } else if (requestCode == FIRST_REQUEST_CROP) {    // 裁剪图片
            if (resultCode == Activity.RESULT_OK) {
                deletePhoto(firstPhotoName);
                Bitmap bitmap = BitmapFactory.decodeFile(firstPhotoPath);//Bimp.revitionImageSizeHalf(filepath);
                bitmap = Bimp.rotateBitmap(bitmap, firstPhotoPath);// 处理某些手机图片旋转问题
                uploadFirst.setImageBitmap(bitmap);
                tagTv.setText(getResources().getString(R.string.camera_success));
                tagIv.setVisibility(View.VISIBLE);
                tagIv.setImageDrawable(getResources().getDrawable(R.drawable.upload_indentity_success_tag));
                uploadFirstCover.setVisibility(View.GONE);
//                updateLoadIcon();
            }
        } else if (requestCode == SECOND_REQUEST_CROP) {    // 裁剪图片
            if (resultCode == Activity.RESULT_OK) {
                deletePhoto(secondPhotoName);
                Bitmap bitmap = BitmapFactory.decodeFile(secondPhotoPath);//Bimp.revitionImageSizeHalf(filepath);
                bitmap = Bimp.rotateBitmap(bitmap, secondPhotoPath);// 处理某些手机图片旋转问题
                uploadSecond.setImageBitmap(bitmap);
                tagTv.setText(getResources().getString(R.string.camera_success));
                tagIv.setVisibility(View.VISIBLE);
                tagIv.setImageDrawable(getResources().getDrawable(R.drawable.upload_indentity_success_tag));
                uploadSecondCover.setVisibility(View.GONE);
//                updateLoadIcon();
            }
        }
    }

    /**
     * 相机拍摄图片
     */
    private void takePhotoByCamera(String photoName, int requesqtCode) {
        String action = MediaStore.ACTION_IMAGE_CAPTURE;
        if (!isIntentAvailable(this, action)) {
            Toast.makeText(getApplicationContext(), "您的手机不支持相机拍摄", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent takePictureIntent = new Intent(action);
        File f = getPhotoFile(photoName);
        switch (requesqtCode) {
            case FIRST_REQUEST_CARD_CAMERA:
                firstPhotoPath = f.getAbsolutePath();
                break;
            case SECOND_REQUEST_CARD_CAMERA:
                secondPhotoPath = f.getAbsolutePath();
                break;
            default:
                firstPhotoPath = f.getAbsolutePath();
                break;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        startActivityForResult(takePictureIntent, requesqtCode);
    }

    /**
     * 检查是否有对应ACTION的intent
     *
     * @param context
     * @param action
     * @return
     */
    public static boolean isIntentAvailable(Context context, String action) {
        final PackageManager packageManager = context.getPackageManager();
        final Intent intent = new Intent(action);
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    private static File getPhotoFile(String photoName) {
        return new File(StorageKit.getTempFilePath(), photoName);
    }

    private static File getCroppedHeadFile(String cropPhotoName) {
        return new File(StorageKit.getTempFilePath(), cropPhotoName);
    }

    private void deletePhoto(String photoName) {    // 删除相机拍摄的照片
        StorageKit.deleteFile(getPhotoFile(photoName));
    }

    private void handlePhotoResult(String photoName, int requestCropCode) {
        dispatchCropImage(photoName, requestCropCode);    // 照片拍摄需要裁剪
//        if (requestCode == FIRST_REQUEST_CARD_CAMERA) {
//        }
    }

    /**
     * 进行裁剪图片操作
     */
    private void dispatchCropImage(String cropName, int requestCropCode) {
        if ((FIRST_REQUEST_CROP == requestCropCode && firstPhotoPath == null) || (SECOND_REQUEST_CROP == requestCropCode && secondPhotoPath == null)) {
            return;
        }
        try {
            Intent intent = new Intent("com.android.camera.action.CROP");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());
            }
            File f = getCroppedHeadFile(cropName);
            if (FIRST_REQUEST_CROP == requestCropCode) {
                intent.setDataAndType(Uri.fromFile(new File(firstPhotoPath)), "image/*");
                putCropIntentExtras(intent);
                firstPhotoPath = f.getAbsolutePath();
            } else if (SECOND_REQUEST_CROP == requestCropCode) {
                intent.setDataAndType(Uri.fromFile(new File(secondPhotoPath)), "image/*");
                putCropIntentExtras(intent);
                secondPhotoPath = f.getAbsolutePath();
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
            startActivityForResult(intent, requestCropCode);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "您的手机不支持裁剪图片", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        livingManger.destory();
    }

    private static void putCropIntentExtras(Intent intent) {
        if (intent == null) {
            return;
        }
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 0.1);
        intent.putExtra("aspectY", 0.1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", CARD_WIDTH);
        intent.putExtra("outputY", CARD_HEIGHT);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
    }
}
