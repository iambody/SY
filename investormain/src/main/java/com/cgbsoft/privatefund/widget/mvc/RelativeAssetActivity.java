package com.cgbsoft.privatefund.widget.mvc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
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
import com.cgbsoft.lib.widget.LoadingDialog;
import com.cgbsoft.privatefund.R;
import com.chenenyu.router.annotation.Route;
import com.jhworks.library.ImageSelector;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * 关联我的资产
 *
 * @author chenlong
 */
@Route("investornmain_relativeassetctivity")
public class RelativeAssetActivity extends BaseActivity implements View.OnClickListener {

    private static final int WAIT_CHECK = 1;
    private static final int CHECK_PAST = 2;
    private static final int CHECK_FAILURE = 3;
    private static final int SMOTH_CODE = 4;
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
    protected ImageView resultImage;

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

        addImage.setOnClickListener(this);
        commitBtn.setOnClickListener(this);
        showImage.setOnClickListener(this);
        ViewUtils.setTextColorAndLink(this, description, R.string.hotline, getResources().getColor(R.color.orange), new ViewUtils.OnClickHyperlinkListener() {
            @Override
            public void onClick(View v, String linkText) {
                NavigationUtils.startDialgTelephone(RelativeAssetActivity.this, "4001888848");
            }
        });
    }

    @Override
    protected void data() {
        if (WAIT_CHECK == Integer.valueOf(SPreference.getToCBean(this).getStockAssetsStatus())) {
            resultImage.setImageResource(R.drawable.shenhezhong);
            resultImage.setVisibility(View.VISIBLE);
            commitBtn.setVisibility(View.GONE);
            addImage.setVisibility(View.GONE);
            relative_asset_up_shouchizhaopian_txt.setVisibility(View.GONE);
            initImage();
        } else if (CHECK_PAST == Integer.valueOf(SPreference.getToCBean(this).getStockAssetsStatus())) {
            resultImage.setImageResource(R.drawable.chenggong);
            resultImage.setVisibility(View.VISIBLE);
            commitBtn.setVisibility(View.GONE);
            addImage.setVisibility(View.GONE);
            relative_asset_up_shouchizhaopian_txt.setVisibility(View.GONE);

            initImage();
        } else if (CHECK_FAILURE == Integer.valueOf(SPreference.getToCBean(this).getStockAssetsStatus())) {
            resultImage.setImageResource(R.drawable.shibai);
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
    protected BasePresenterImpl createPresenter() {
        return null;
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.relative_add:
                if (isloading) {
                    return;
                }
//                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                if (cameraIntent.resolveActivity(getPackageManager()) != null) {
//                    // 设置系统相机拍照后的输出路径
//                    File mTmpFile = FileUtils.createTmpFile(this);
//                    imagePath = mTmpFile.getPath();
//                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mTmpFile));
//                    startActivityForResult(cameraIntent, Contant.REQUEST_CAMERA);
//                } else {
//                    Toast.makeText(this, R.string.msg_no_camera, Toast.LENGTH_SHORT).show();
//                }
                ImageSelector selector = ImageSelector.create();
                selector.single();  // 选择一张图片
                selector.start(this, BaseWebViewActivity.BACK_CAMERA_CODE);
                break;
            case R.id.show_result_image:
                if (isloading) {
                    return;
                }
//                if (IsShowing) {
//                    Intent intent = new Intent(this, SmoothImageActivity.class);
//                    if (!TextUtils.isEmpty(imageId)) {
//                        intent.putExtra(SmoothImageActivity.IMAGE_SAVE_PATH_LOCAL, imageId);
//                        intent.putExtra(SmoothImageActivity.IMAGE_RIGHT_DELETE, false);
//                        startActivityForResult(intent, SMOTH_CODE);
//                    } else if (!TextUtils.isEmpty(imagePath) && new File(imagePath).length() > 0) {
//                        intent.putExtra(SmoothImageActivity.IMAGE_SAVE_PATH_LOCAL, imagePath);
//                        intent.putExtra(SmoothImageActivity.IMAGE_RIGHT_DELETE, true);
//                        startActivityForResult(intent, SMOTH_CODE);
//                    } else {
//                        Intent startIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                        if (startIntent.resolveActivity(getPackageManager()) != null) {
//                            // 设置系统相机拍照后的输出路径
//                            File mTmpFile = com.cgbsoft.privatefund.rongcould.picture.FileUtils.createTmpFile(this);
//                            imagePath = mTmpFile.getPath();
//                            startIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mTmpFile));
//                            startActivityForResult(startIntent, Contant.REQUEST_CAMERA);
//                        } else {
//                            Toast.makeText(this, me.nereo.multi_image_selector.R.string.msg_no_camera, Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                    return;
//                }
                if (CHECK_FAILURE == Integer.valueOf(SPreference.getToCBean(this).getStockAssetsStatus())) {
//                    Intent camenIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    if (camenIntent.resolveActivity(getPackageManager()) != null) {
//                        // 设置系统相机拍照后的输出路径
//                        imagePath = mTmpFile.getPath();
//                        startActivityForResult(camenIntent, Contant.REQUEST_CAMERA);
//                    } else {
//                        Toast.makeText(this, me.nereo.multi_image_selector.R.string.msg_no_camera, Toast.LENGTH_SHORT).show();
//                    }
                    ImageSelector cameraSelect = ImageSelector.create();
                    cameraSelect.single();  // 选择一张图片
                    cameraSelect.start(this, BaseWebViewActivity.BACK_CAMERA_CODE);
                    return;
                }

//                Intent intent = new Intent(this, SmoothImageActivity.class);
//                if (!TextUtils.isEmpty(imageId)) {
//                    intent.putExtra(SmoothImageActivity.IMAGE_SAVE_PATH_LOCAL, imageId);
//                    intent.putExtra(SmoothImageActivity.IMAGE_RIGHT_DELETE, false);
//                    startActivityForResult(intent, SMOTH_CODE);
//                } else if (!TextUtils.isEmpty(imagePath) && new File(imagePath).length() > 0) {
//                    intent.putExtra(SmoothImageActivity.IMAGE_SAVE_PATH_LOCAL, imagePath);
//                    intent.putExtra(SmoothImageActivity.IMAGE_RIGHT_DELETE, true);
//                    startActivityForResult(intent, SMOTH_CODE);
//                } else {
//                    Intent startIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    if (startIntent.resolveActivity(getPackageManager()) != null) {
//                        // 设置系统相机拍照后的输出路径
//                        File mTmpFile = com.cgbsoft.privatefund.rongcould.picture.FileUtils.createTmpFile(this);
//                        imagePath = mTmpFile.getPath();
//                        startIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mTmpFile));
//                        startActivityForResult(startIntent, Contant.REQUEST_CAMERA);
//                    } else {
//                        Toast.makeText(this, me.nereo.multi_image_selector.R.string.msg_no_camera, Toast.LENGTH_SHORT).show();
//                    }
//                }
                break;
            case R.id.commit:
                if (isloading) {
                    return;
                }
                relativeAsset();
                break;
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

//                if (imagePath != null && imagePath.length() > 0) {
//                    imagePath = com.cgbsoft.privatefund.utils.FileUtils.compressFileToUpload(imagePath, true);
//                    addImage.setVisibility(View.GONE);
//                    showImage.setImageBitmap(MyBitmapUtils.getLoacalBitmap(imagePath));
//                    IsShowing = true;
//                    imageId = null;
//                    resultImage.setVisibility(View.GONE);
//                    checkFailure.setVisibility(View.GONE);
//                    relative_asset_up_shouchizhaopian_txt.setVisibility(View.GONE);
//                } else {
//                    new MToast(this).show("拍照失败", 0);
//                }

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
        loading.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
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
            }
        }).start();
    }

    private void uploadCertification() {
//        String param = ApiParams.requestParamRelativeAsset(MApplication.getUserid(), Domain.urlStr + imageId);
//        new RelativeAssetTask(this).start(param, new HttpResponseListener() {
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//                    String results = response.get("result").toString();
//                    if ("suc".equals(results)) {
//                        showImage.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                commitBtn.setEnabled(true);
//                                if (loading != null) {
//                                    loading.dismiss();
//                                }
//                                MApplication.getUser().getToC().setStockAssetsStatus(1);
//                                new MToast(RelativeAssetActivity.this).show("提交成功，我们将在工作日当天完成审核", 0);
//                                MApplication.getUser().getToC().setStockAssetsImage(imageId);
//                                EventBus.getDefault().post(new RefushUser());
//                                finish();
//                            }
//                        });
//                    } else {
//                        showImage.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                commitBtn.setEnabled(true);
//                                if (loading != null) {
//                                    loading.dismiss();
//                                }
//                                MApplication.getUser().getToC().setStockAssetsStatus(3);
//                                new MToast(RelativeAssetActivity.this).show("关联资产失败", 0);
//                            }
//                        });
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onErrorResponse(final String error, int statueCode) {
//                showImage.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        new MToast(RelativeAssetActivity.this).show(error, 0);
//                        MApplication.getUser().getToC().setStockAssetsStatus(3);
//                        commitBtn.setEnabled(true);
//                        if (loading != null) {
//                            loading.dismiss();
//                        }
//                    }
//                });
//            }
//        });
    }
}
