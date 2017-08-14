package com.cgbsoft.privatefund.mvp.ui.center;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.utils.constant.Constant;
import com.cgbsoft.lib.utils.dm.Utils.helper.FileUtils;
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

/**
 * Created by fei on 2017/8/12.
 */

public class UploadIndentityCradActivity extends BaseActivity<UploadIndentityPresenterImpl> implements UploadIndentityContract.UploadIndentityView {

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;
    @BindView(R.id.title_mid)
    TextView titleTV;
    @BindView(R.id.iv_upload_crad_first)
    ImageView uploadFirst;
    @BindView(R.id.iv_upload_crad_second)
    ImageView uploadSecond;
    @BindView(R.id.tv_upload_indentity_tip)
    TextView tagTv;
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

    @OnClick(R.id.iv_upload_crad_first)
    public void uploadFirstClick() {
        // 拍照
        takePhotoByCamera(firstPhotoName, FIRST_REQUEST_CARD_CAMERA);
    }

    @OnClick(R.id.iv_upload_crad_second)
    public void uploadSecondClick() {
        // 拍照
        takePhotoByCamera(secondPhotoName, SECOND_REQUEST_CARD_CAMERA);
    }
    @OnClick(R.id.upload_submit)
    public void photoSubmit(){
        LogUtils.Log("aaa","firstPhotoPath==="+firstPhotoPath+"---secondPhotoPath==="+secondPhotoPath);
        List<String> paths = new ArrayList<>();
        if (isIdCard && (TextUtils.isEmpty(firstPhotoPath) || TextUtils.isEmpty(secondPhotoPath))) {
            Toast.makeText(getApplicationContext(), "请正确拍摄图片", Toast.LENGTH_SHORT).show();
            return;
        } else if(TextUtils.isEmpty(firstPhotoPath)){
            Toast.makeText(getApplicationContext(), "请正确拍摄图片", Toast.LENGTH_SHORT).show();
            return;
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
                    if (localPath.contains(Constant.UPLOAD_FEEDBACK_TYPE) || localPath.startsWith("http")||localPath.equals("+")) {
                        continue;
                    }
                    String newTargetFile = FileUtils.compressFileToUpload(localPath, true);
                    String paths = DownloadUtils.postObject(newTargetFile, Constant.UPLOAD_FEEDBACK_TYPE);
                    FileUtils.deleteFile(newTargetFile);
                    if (!TextUtils.isEmpty(paths)) {
                        remoteParams.add(paths);
                    } else {
                        ThreadUtils.runOnMainThread(() -> Toast.makeText(UploadIndentityCradActivity.this, "证明文件上传失败，请重新上传", Toast.LENGTH_SHORT).show());
                        mLoadingDialog.dismiss();
                        return;
                    }
                }
                uploadRemotePaths();
            }
        }.start();
    }

    private void uploadRemotePaths() {
//        getPresenter().uploadIndentity(remoteParams);
    }

    @Override
    public void showLoadDialog() {
        if (mLoadingDialog.isShowing()) {
            return;
        }
        mLoadingDialog.show();
    }

    @Override
    public void hideLoadDialog() {
        mLoadingDialog.dismiss();
    }

    @Override
    public void uploadIndentitySuccess() {

    }

    @Override
    public void uploadIndentityError(Throwable error) {

    }

    @Override
    protected int layoutID() {
        return R.layout.activity_upload_indentity;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        indentityCode = getIntent().getStringExtra("indentityCode");
        String title = getIntent().getStringExtra("title");
        if (TextUtils.isEmpty(indentityCode)) {
            this.finish();
            return;
        }
        switch (indentityCode) {
            case "100101"://身份证
                isIdCard=true;
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
        }
        titleTV.setText(title);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(com.cgbsoft.lib.R.drawable.ic_back_black_24dp);
        toolbar.setNavigationOnClickListener(v -> finish());
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
        if (requestCode == FIRST_REQUEST_CARD_CAMERA) {
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
//                updateLoadIcon();
            }
        }else if (requestCode == SECOND_REQUEST_CROP) {    // 裁剪图片
            if (resultCode == Activity.RESULT_OK) {
                deletePhoto(secondPhotoName);
                Bitmap bitmap = BitmapFactory.decodeFile(secondPhotoPath);//Bimp.revitionImageSizeHalf(filepath);
                bitmap = Bimp.rotateBitmap(bitmap, secondPhotoPath);// 处理某些手机图片旋转问题
                uploadSecond.setImageBitmap(bitmap);
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
