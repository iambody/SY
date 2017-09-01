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
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.permission.MyPermissionsActivity;
import com.cgbsoft.lib.permission.MyPermissionsChecker;
import com.cgbsoft.lib.utils.constant.Constant;
import com.cgbsoft.lib.utils.dm.Utils.helper.FileUtils;
import com.cgbsoft.lib.utils.imgNetLoad.Imageload;
import com.cgbsoft.lib.utils.net.NetConfig;
import com.cgbsoft.lib.utils.rxjava.RxBus;
import com.cgbsoft.lib.utils.tools.DownloadUtils;
import com.cgbsoft.lib.utils.tools.LogUtils;
import com.cgbsoft.lib.utils.tools.ThreadUtils;
import com.cgbsoft.lib.widget.dialog.LoadingDialog;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.mvp.contract.center.UploadIndentityContract;
import com.cgbsoft.privatefund.mvp.presenter.center.UploadIndentityPresenterImpl;
import com.cgbsoft.privatefund.mvp.ui.home.FeedbackActivity;
import com.cgbsoft.privatefund.utils.Bimp;
import com.cgbsoft.privatefund.utils.StorageKit;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.cgbsoft.lib.utils.constant.RxConstant.SELECT_INDENTITY;
import static com.cgbsoft.lib.utils.constant.RxConstant.SELECT_INDENTITY_ADD;

/**
 * Created by fei on 2017/8/12.
 */

public class UploadIndentityCradActivity extends BaseActivity<UploadIndentityPresenterImpl> implements UploadIndentityContract.UploadIndentityView {

//    @BindView(R.id.toolbar)
//    protected Toolbar toolbar;
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
    private String indentityCode;
    private boolean isIdCard;
    private List<String> remoteParams = new ArrayList<>();
    private String credentialCode;
    private MyPermissionsChecker mPermissionsChecker;
    private String[] PERMISSIONS = new String[]{Manifest.permission.CAMERA};
    private int REQUEST_CODE_PERMISSON_FIRST = 2000; // 请求码
    private int REQUEST_CODE_PERMISSON_SECOND = 2002; // 请求码
    private boolean isFromSelectIndentity;
    private String depict;

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
        takePhotoByCamera(firstPhotoName, FIRST_REQUEST_CARD_CAMERA);
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
        takePhotoByCamera(firstPhotoName, FIRST_REQUEST_CARD_CAMERA);
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
        takePhotoByCamera(secondPhotoName, SECOND_REQUEST_CARD_CAMERA);
    }
    @OnClick(R.id.upload_submit)
    public void photoSubmit(){
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
            File fileFirst = new File(firstPhotoPath);
            File fileSecond = new File(secondPhotoPath);
            if (!fileFirst.exists()&&!fileSecond.exists()) {
                Toast.makeText(getApplicationContext(), "请点击拍摄证件照照片", Toast.LENGTH_SHORT).show();
                return;
            }
            if (fileFirst.exists() && !fileSecond.exists()) {
                Toast.makeText(getApplicationContext(), "请点击拍摄反面证件照照片", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!fileFirst.exists()&&fileSecond.exists()) {
                Toast.makeText(getApplicationContext(), "请点击拍摄正面证件照照片", Toast.LENGTH_SHORT).show();
                return;
            }
        } else{
            if(TextUtils.isEmpty(firstPhotoPath)){
                Toast.makeText(getApplicationContext(), "请点击拍摄证件照照片", Toast.LENGTH_SHORT).show();
                return;
            }
            File fileFirst = new File(firstPhotoPath);
            if (!fileFirst.exists()) {
                Toast.makeText(getApplicationContext(), "请点击拍摄证件照照片", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if (isIdCard) {
            paths.add(firstPhotoPath);
            paths.add(secondPhotoPath);
        } else {
            paths.add(firstPhotoPath);
        }
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(this);
        }
        mLoadingDialog.show();
        new Thread() {
            @Override
            public void run() {
                super.run();
                remoteParams.clear();
                for (final String localPath : paths) {
                    String newTargetFile = FileUtils.compressFileToUpload(localPath, true);
                    String paths = DownloadUtils.postObject(newTargetFile, "credential/"+credentialCode+"/");
                    FileUtils.deleteFile(newTargetFile);
                    if (!TextUtils.isEmpty(paths)) {
                        remoteParams.add(NetConfig.UPLOAD_FILE.concat(paths));
                    } else {
                        ThreadUtils.runOnMainThread(() -> Toast.makeText(UploadIndentityCradActivity.this, "证件上传失败，请重新上传", Toast.LENGTH_SHORT).show());
                        mLoadingDialog.dismiss();
                        return;
                    }
                }
                uploadRemotePaths();
            }
        }.start();
    }

    private void uploadRemotePaths() {
        getPresenter().uploadIndentity(remoteParams,indentityCode,credentialCode);
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
    public void uploadIndentitySuccess(String result) {
        if (TextUtils.isEmpty(result)) {
            Toast.makeText(getApplicationContext(), "上传成功!", Toast.LENGTH_SHORT).show();
            RxBus.get().post(SELECT_INDENTITY, 0);
            RxBus.get().post(SELECT_INDENTITY_ADD, 0);
            if (isFromSelectIndentity) {
                Intent intent = new Intent(this, CardCollectActivity.class);
                intent.putExtra("indentityCode", indentityCode);
                startActivity(intent);
            }
            this.finish();
        } else {
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void uploadIndentityError(Throwable error) {
        Toast.makeText(getApplicationContext(),"上传失败,请稍后重试!",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected int layoutID() {
        return R.layout.activity_upload_indentity;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
//        toolbar.setTitle("");
//        setSupportActionBar(toolbar);
//        toolbar.setNavigationIcon(com.cgbsoft.lib.R.drawable.ic_back_black_24dp);
//        toolbar.setNavigationOnClickListener(v -> finish());
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        credentialCode = getIntent().getStringExtra("credentialCode");
        indentityCode = getIntent().getStringExtra("indentityCode");
        isFromSelectIndentity = getIntent().getBooleanExtra("isFromSelectIndentity", false);
        depict = getIntent().getStringExtra("depict");
        String customerName = getIntent().getStringExtra("customerName");
        String customerNum = getIntent().getStringExtra("customerNum");
        String title = getIntent().getStringExtra("title");
        if (TextUtils.isEmpty(indentityCode)||TextUtils.isEmpty(credentialCode)) {
            this.finish();
            return;
        }
        String stateCode = getIntent().getStringExtra("stateCode");//证件审核状态code码：5：未上传；10：审核中；30：已驳回；50：已通过；70：已过期
        if (TextUtils.isEmpty(stateCode) || "5".equals(stateCode)) {//未上传
            uploadFirst.setEnabled(true);
            uploadSecond.setEnabled(true);
            submit.setVisibility(View.VISIBLE);
            tagTv.setVisibility(View.VISIBLE);
            switch (credentialCode) {
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
            if ("30".equals(stateCode)||"70".equals(stateCode)) {
                uploadFirst.setEnabled(true);
                uploadSecond.setEnabled(true);
                submit.setVisibility(View.VISIBLE);
                if ("30".equals(stateCode)) {//30：已驳回
                    defeatAll.setVisibility(View.VISIBLE);
                    tagIv.setVisibility(View.VISIBLE);
                    tagTv.setVisibility(View.VISIBLE);
                    tagIv.setImageDrawable(getResources().getDrawable(R.drawable.upload_indentity_error_tag));
                    tagTv.setText("审核失败");
                    defeatTitle.setText("失败原因:");
                    defeatDepict.setText(TextUtils.isEmpty(depict)?"":depict);
                }
                if ("70".equals(stateCode)) {//70：已过期
                    defeatAll.setVisibility(View.VISIBLE);
                    tagTv.setVisibility(View.VISIBLE);
                    tagIv.setVisibility(View.VISIBLE);
                    tagIv.setImageDrawable(getResources().getDrawable(R.drawable.upload_indentity_error_tag));
                    tagTv.setText("当前证件已过期");
                    uploadFirstCover.setVisibility(View.VISIBLE);
                    defeatTitle.setText("失败原因:");
                    defeatDepict.setText(TextUtils.isEmpty(depict)?"":depict);
                }
            }
            if ("10".equals(stateCode)) {
                tagTv.setText("审核中");
            }
            if ("50".equals(stateCode)) {
                tagTv.setText("已通过");
            }
            if ("45".equals(stateCode)) {
                replenishAll.setVisibility(View.VISIBLE);
                replenishName.setText(customerName);
                replenishNum.setText(customerNum);
                uploadFirst.setEnabled(true);
                uploadSecond.setEnabled(true);
                submit.setVisibility(View.VISIBLE);
                uploadFirst.setImageDrawable(getResources().getDrawable(R.drawable.upload_id_card_front));
                switch (credentialCode) {
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
                String firstUrl = getIntent().getStringExtra("firstUrl");
                String secondUrl = getIntent().getStringExtra("secondUrl");
                Imageload.display(this,firstUrl,uploadFirst);
                if (!TextUtils.isEmpty(secondUrl)) {
                    uploadSecond.setVisibility(View.VISIBLE);
                    Imageload.display(this,secondUrl,uploadSecond);
                    if ("70".equals(stateCode)) {
                        uploadSecondCover.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
        titleTV.setText(title);
        initView(savedInstanceState);
    }

    private void initView(Bundle savedInstanceState) {
        mLoadingDialog = LoadingDialog.getLoadingDialog(this, "", false, false);
//        CARD_WIDTH = CARD_HEIGHT *(32/22);
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
        }else if (requestCode == REQUEST_CODE_PERMISSON_SECOND){
            uploadSecondClick();
        }else if (requestCode == FIRST_REQUEST_CARD_CAMERA) {
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
        }else if (requestCode == SECOND_REQUEST_CROP) {    // 裁剪图片
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
                secondPhotoPath=f.getAbsolutePath();
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
        if ((FIRST_REQUEST_CROP==requestCropCode&&firstPhotoPath == null)||(SECOND_REQUEST_CROP==requestCropCode&&secondPhotoPath==null)) {
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
            } else if (SECOND_REQUEST_CROP==requestCropCode) {
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
