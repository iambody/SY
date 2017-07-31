package com.cgbsoft.privatefund.mvp.ui.home;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cgbsoft.lib.AppInfStore;
import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.base.webview.BaseWebViewActivity;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.utils.constant.Constant;
import com.cgbsoft.lib.utils.imgNetLoad.Imageload;
import com.cgbsoft.lib.utils.net.NetConfig;
import com.cgbsoft.lib.utils.tools.DownloadUtils;
import com.cgbsoft.lib.utils.tools.MyBitmapUtils;
import com.cgbsoft.lib.utils.tools.NavigationUtils;
import com.cgbsoft.lib.utils.tools.ViewUtils;
import com.cgbsoft.lib.widget.dialog.LoadingDialog;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.mvp.contract.home.RelativeAssetContract;
import com.cgbsoft.privatefund.mvp.presenter.home.RelatedAssetPresenter;
import com.chenenyu.router.annotation.Route;
import com.jhworks.library.ImageSelector;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 关联我的资产
 *
 * @author chenlong
 */
@Route("investornmain_relativeassetctivity")
public class RelativeAssetActivity extends BaseActivity<RelatedAssetPresenter> implements RelativeAssetContract.View {

    private static final int WAIT_CHECK = 1;
    private static final int CHECK_PAST = 2;
    private static final int CHECK_FAILURE = 3;
    private static final int SMOTH_CODE = 4;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    private static final int MY_PERMISSIONS_REQUEST_CAMALE = 2;
    private String imagePath;
    private String imageId;
    private boolean isloading;
    private LoadingDialog loading;
    private boolean IsShowing;

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @BindView(R.id.title_mid)
    protected TextView titleMid;

    @BindView(R.id.relative_add)
    protected ImageView addImage;

    @BindView(R.id.guanlian_des)
    protected TextView description;

    @BindView(R.id.check_failure)
    protected LinearLayout checkFailure;

    @BindView(R.id.check_failure_reason_text)
    protected TextView checkFailureReason;

    @BindView(R.id.commit)
    protected Button commitBtn;

    @BindView(R.id.check_result_flag)
    protected TextView resultImage;

    @BindView(R.id.show_result_image)
    protected ImageView showImage;

    @BindView(R.id.relative_asset_up_shouchizhaopian_txt)
    protected TextView relative_asset_up_shouchizhaopian_txt;

    @Override
    protected int layoutID() {
        return R.layout.acitivity_relative_asset;
    }

    @Override
    protected void before() {
        super.before();
        imageId = SPreference.getToCBean(this).getStockAssetsImage();
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        toolbar.setNavigationIcon(com.cgbsoft.lib.R.drawable.ic_back_black_24dp);
        toolbar.setNavigationOnClickListener(v -> finish());
        titleMid.setText("关联我的资产");
        ViewUtils.setTextColorAndLink(this, description, R.string.hotline, ContextCompat.getColor(this, R.color.app_golden), (v, linkText) -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
            } else {
                NavigationUtils.startDialgTelephone(RelativeAssetActivity.this, "4001888848");
            }
        });
    }


    @Override
    protected void data() {
        if (WAIT_CHECK == Integer.valueOf(SPreference.getToCBean(this).getStockAssetsStatus())) {
            resultImage.setText(R.string.relative_asset_doing);
            resultImage.setVisibility(View.VISIBLE);
            commitBtn.setVisibility(View.GONE);
            addImage.setVisibility(View.GONE);
            relative_asset_up_shouchizhaopian_txt.setVisibility(View.GONE);
            initImage();
        } else if (CHECK_PAST == Integer.valueOf(SPreference.getToCBean(this).getStockAssetsStatus())) {
            resultImage.setText(R.string.relative_asset_success);
            resultImage.setVisibility(View.VISIBLE);
            commitBtn.setVisibility(View.GONE);
            addImage.setVisibility(View.GONE);
            relative_asset_up_shouchizhaopian_txt.setVisibility(View.GONE);

            initImage();
        } else if (CHECK_FAILURE == Integer.valueOf(SPreference.getToCBean(this).getStockAssetsStatus())) {
            resultImage.setText(R.string.relative_asset_failure);
            resultImage.setVisibility(View.VISIBLE);
            addImage.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(SPreference.getToCBean(this).getCheckFailureReason())) {
                checkFailure.setVisibility(View.VISIBLE);
                checkFailureReason.setText(SPreference.getToCBean(this).getCheckFailureReason());
            }
            initImage();
        } else {
            resultImage.setVisibility(View.GONE);
            addImage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_PHONE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    NavigationUtils.startDialgTelephone(RelativeAssetActivity.this, "4001888848");
                } else {
                    // Permission Denied
                    Toast.makeText(RelativeAssetActivity.this, "请开启用户拨打电话权限", Toast.LENGTH_SHORT).show();
                }
                break;
            case MY_PERMISSIONS_REQUEST_CAMALE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent camenIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (camenIntent.resolveActivity(getPackageManager()) != null) {
                        ImageSelector selectSec = ImageSelector.create();
                        selectSec.single();  // 选择一张图片
                        selectSec.showCamera(false);
                        selectSec.start(this, BaseWebViewActivity.BACK_CAMERA_CODE);
                    } else {
                        Toast.makeText(this, R.string.no_camera_device, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RelativeAssetActivity.this, "请开启应该拍照权限", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected RelatedAssetPresenter createPresenter() {
        return new RelatedAssetPresenter(getBaseContext(), this);
    }

    private void initImage() {
        String imageUrL = SPreference.getToCBean(this).getStockAssetsImage();
        if (!TextUtils.isEmpty(imageUrL)) {

            if (!TextUtils.isEmpty(imageUrL) && !imageUrL.startsWith("http")) {
                imageUrL = NetConfig.UPLOAD_FILE + imageUrL;
            }

            Imageload.display(this, imageUrL, 0, 0, 0, showImage, R.drawable.ic_guanlian_bg, R.drawable.ic_guanlian_bg);
        }
    }

    @OnClick(R.id.relative_add)
    void addRelativeClick() {
        if (isloading) {
            return;
        }
        startCameraActivity();
    }

    @OnClick(R.id.show_result_image)
    void showResultClick() {
        if (isloading) {
            return;
        }

        if (CHECK_FAILURE == Integer.valueOf(SPreference.getToCBean(this).getStockAssetsStatus())) {
            startCameraActivity();
            return;
        }

        Intent intent = new Intent(this, SmoothImageActivity.class);
        if (!TextUtils.isEmpty(imageId)) {
            intent.putExtra(SmoothImageActivity.IMAGE_SAVE_PATH_LOCAL, imageId);
            intent.putExtra(SmoothImageActivity.IMAGE_RIGHT_DELETE, false);
            startActivityForResult(intent, SMOTH_CODE);
        } else if (!TextUtils.isEmpty(imagePath) && new File(imagePath).length() > 0) {
            intent.putExtra(SmoothImageActivity.IMAGE_SAVE_PATH_LOCAL, imagePath);
            intent.putExtra(SmoothImageActivity.IMAGE_RIGHT_DELETE, true);
            startActivityForResult(intent, SMOTH_CODE);
        } else {
            startCameraActivity();
        }
    }

    @OnClick(R.id.commit)
    void commitDataClick() {
        if (isloading) {
            return;
        }
        relativeAsset();
    }

    private void startCameraActivity() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMALE);
        } else {
            Intent camenIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (camenIntent.resolveActivity(getPackageManager()) != null) {
                ImageSelector selectSec = ImageSelector.create();
//                selectSec.single();  // 选择一张图片
                selectSec.openCameraOnly(true);
                selectSec.start(this, BaseWebViewActivity.BACK_CAMERA_CODE);
            } else {
                Toast.makeText(this, R.string.no_camera_device, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BaseWebViewActivity.BACK_CAMERA_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                ArrayList<String> mSelectPath = data.getStringArrayListExtra(ImageSelector.EXTRA_RESULT);
                System.out.println("-----select1=" + mSelectPath.get(0));
                imagePath = com.cgbsoft.lib.utils.dm.Utils.helper.FileUtils.compressFileToUpload(mSelectPath.get(0), true);
                addImage.setVisibility(View.GONE);
                showImage.setImageBitmap(MyBitmapUtils.getLoacalBitmap(imagePath));
                IsShowing = true;
                imageId = null;
                resultImage.setVisibility(View.GONE);
                checkFailure.setVisibility(View.GONE);
                relative_asset_up_shouchizhaopian_txt.setVisibility(View.GONE);
            } else {
                imagePath = "";
            }
        } else if (requestCode == SMOTH_CODE) {
            if (resultCode == RESULT_OK) {
                addImage.setVisibility(View.VISIBLE);
                showImage.setImageResource(R.drawable.ic_guanlian_bg);
                imagePath = null;
                relative_asset_up_shouchizhaopian_txt.setVisibility(View.VISIBLE);
            }
        }
    }

    private void relativeAsset() {
        if (TextUtils.isEmpty(imagePath)) {
            Toast.makeText(this, CHECK_FAILURE == Integer.valueOf(SPreference.getToCBean(this).getStockAssetsStatus()) ? "请重新上传身份证" : "请上传身份证文件", Toast.LENGTH_SHORT).show();
            return;
        }
        commitBtn.setEnabled(false);
        if (loading == null) {
            loading = new LoadingDialog(this);
        }
        if (!loading.isShowing()) {
            loading.show();
        }
        new Thread(() -> {
            isloading = true;
            String newTargetFile = com.cgbsoft.lib.utils.dm.Utils.helper.FileUtils.compressFileToUpload(imagePath, false);
            imageId = DownloadUtils.postObject(newTargetFile, Constant.UPLOAD_CERTIFICATE_TYPE);
            com.cgbsoft.lib.utils.dm.Utils.helper.FileUtils.deleteFile(newTargetFile);
            if (!TextUtils.isEmpty(imageId)) {
                uploadCertification();
            } else {
                showImage.post(new Runnable() {
                    @Override
                    public void run() {
                        if (loading != null) {
                            loading.dismiss();
                        }
                        commitBtn.setEnabled(true);
                        Toast.makeText(RelativeAssetActivity.this, "上传文件失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            isloading = false;
        }).start();
    }

    @Override
    public void requestSuccess() {
        commitBtn.setEnabled(true);
        if (loading != null) {
            loading.dismiss();
        }
        Toast.makeText(RelativeAssetActivity.this, "提交成功，我们将在工作日当天完成审核", Toast.LENGTH_SHORT).show();
        AppInfStore.updateUserStockAssetsStatus(this, "1");
        AppInfStore.updateUserStockAssetsImageUrl(this, imageId);
//        EventBus.getDefault().post(new RefushUser());
        finish();
    }

    @Override
    public void requestFailure() {
        Toast.makeText(RelativeAssetActivity.this, "关联资产失败", Toast.LENGTH_SHORT).show();
        AppInfStore.updateUserStockAssetsStatus(this, "3");
        commitBtn.setEnabled(true);
        if (loading != null) {
            loading.dismiss();
        }
    }

    @Override
    public void requestError(String mssage) {
        Toast.makeText(RelativeAssetActivity.this, mssage, Toast.LENGTH_SHORT).show();
        AppInfStore.updateUserStockAssetsStatus(this, "3");
        commitBtn.setEnabled(true);
        if (loading != null) {
            loading.dismiss();
        }
    }

    private void uploadCertification() {
        getPresenter().uploadAssetRelatedFile(NetConfig.UPLOAD_FILE + imageId);
    }

    protected void onDestroy() {
        if(loading != null) {
            loading.dismiss();
        }
        super.onDestroy();
    }

}
