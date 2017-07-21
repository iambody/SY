package com.cgbsoft.privatefund.mvp.ui.home;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.cgbsoft.lib.AppInfStore;
import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.base.model.UserInfoDataEntity;
import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.base.webview.BaseWebViewActivity;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.utils.constant.Constant;
import com.cgbsoft.lib.utils.dm.Utils.helper.FileUtils;
import com.cgbsoft.lib.utils.imgNetLoad.Imageload;
import com.cgbsoft.lib.utils.net.NetConfig;
import com.cgbsoft.lib.utils.tools.DownloadUtils;
import com.cgbsoft.lib.utils.tools.MyBitmapUtils;
import com.cgbsoft.lib.utils.tools.NavigationUtils;
import com.cgbsoft.lib.utils.tools.ThreadUtils;
import com.cgbsoft.lib.utils.tools.ViewUtils;
import com.cgbsoft.lib.widget.FixGridLayout;
import com.cgbsoft.lib.widget.dialog.LoadingDialog;
import com.cgbsoft.lib.widget.MToast;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.mvp.contract.home.AssetProveContract;
import com.cgbsoft.privatefund.mvp.presenter.home.AssetProvePresenter;
import com.chenenyu.router.annotation.Route;
import com.jhworks.library.ImageSelector;

import org.json.JSONArray;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 资产证明
 * @author chenlong
 */
@Route("investornmain_proveassetctivity")
public class AssetProveActivity extends BaseActivity<AssetProvePresenter> implements AssetProveContract.View {

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @BindView(R.id.title_mid)
    protected TextView titleMid;

    @BindView(R.id.fix_grid_layout)
    protected FixGridLayout frameLayout;

    @BindView(R.id.commit)
    protected Button commit;

    @BindView(R.id.check_result)
    protected TextView checkResult;

    @BindView(R.id.choose_identify)
    protected TextView identView;

    @BindView(R.id.asset_prove_des)
    protected TextView description;

    @BindView(R.id.radio_group)
    protected ViewGroup viewGroup;

    private static final int SMOTH_CODE = 2;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    private int width;
    private List<String> imagePaths = new ArrayList<>();
    private List<String> remoteParams = new ArrayList<>();
    private LoadingDialog loading;
    private static int state;

    @Override
    protected int layoutID() {
        return R.layout.acitivity_asset_prove;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        width = ViewUtils.getDisplayWidth(this);

        toolbar.setNavigationIcon(com.cgbsoft.lib.R.drawable.ic_back_black_24dp);
        toolbar.setNavigationOnClickListener(v -> finish());
        titleMid.setText("资产证明");

//        ViewUtils.setTextColorAndLink(this, description, R.string.hotline, getResources().getColor(R.color.orange), (v, linkText) -> NavigationUtils.startDialgTelephone(AssetProveActivity.this, "4001888848"));
        ViewUtils.setTextColorAndLink(this, description, R.string.hotline, ContextCompat.getColor(this, R.color.app_golden), (v, linkText) -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
            } else {
                NavigationUtils.startDialgTelephone(AssetProveActivity.this, "4001888848");
            }
        });


        frameLayout.setmCellWidth(width / 4);
        frameLayout.setmCellHeight(width / 4);
        initData();
    }

    @Override
    protected AssetProvePresenter createPresenter() {
        return new AssetProvePresenter(getBaseContext(), this);
    }

    public ImageView addImg(){
        ImageView addImage = new ImageView(getApplicationContext());
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(width/4,width/4);
        addImage.setLayoutParams(layoutParams);
        addImage.setImageResource(R.drawable.ic_asset_prove_upload);
        addImage.setOnClickListener(v -> {
            if (imagePaths.size() >= 10) {
                Toast.makeText(AssetProveActivity.this, "最多上传10张图片", Toast.LENGTH_SHORT).show();
                return;
            }
            NavigationUtils.startSystemImageForResult(AssetProveActivity.this, BaseWebViewActivity.REQUEST_IMAGE);
        });
        return addImage;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_PHONE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    NavigationUtils.startDialgTelephone(AssetProveActivity.this, "4001888848");
                } else {
                    // Permission Denied
                    Toast.makeText(AssetProveActivity.this, "请开启用户拨打电话权限", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void initData() {
        UserInfoDataEntity.ToCBean userInfoC = AppManager.getUserInfo(this).getToC();
        String vas = userInfoC.getAssetsCertificationImage();
        int status = Integer.valueOf(userInfoC.getAssetsCertificationStatus());

        boolean hasStatus = false;
        switch (status) {
            case 1:
                checkResult.setVisibility(View.VISIBLE);
                checkResult.setText("审核状态：待审核");
                commit.setVisibility(View.GONE);
                hasStatus = true;
                break;
            case 2:
                checkResult.setVisibility(View.VISIBLE);
                checkResult.setText("审核状态：已通过");
                commit.setVisibility(View.GONE);
                hasStatus = true;
                break;
            case 3:
                checkResult.setVisibility(View.VISIBLE);
                checkResult.setText("审核状态：被驳回");
                if (TextUtils.isEmpty(vas)) {
                    frameLayout.addView(addImg());
                }
                hasStatus = true;
                break;
            default:
                checkResult.setVisibility(View.GONE);
                frameLayout.addView(addImg());
                identView.setText("选择投资者身份");
                break;
        }

        if (!TextUtils.isEmpty(vas)) {
            try {
                JSONArray jsonArray = new JSONArray(vas);
                initImageListData(jsonArray);
                updateImageViewLayout();
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(AssetProveActivity.class.getName(), e.getMessage());
            }
        }

        if (hasStatus) {
            if ("1".equals(userInfoC.getInvestmentType())) {
                ((RadioButton)viewGroup.getChildAt(0)).setChecked(true);
                if (status != 3) {
                    viewGroup.getChildAt(1).setVisibility(View.GONE);
                }
            } else if ("2".equals(userInfoC.getInvestmentType())) {
                ((RadioButton)viewGroup.getChildAt(1)).setChecked(true);
                if (status != 3) {
                    viewGroup.getChildAt(0).setVisibility(View.GONE);
                }
            }
        }
    }

    private void initImageListData(JSONArray jsonArray) {
        try {
            imagePaths.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                imagePaths.add((String)jsonArray.get(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateImageViewLayout() {
        frameLayout.removeAllViews();
        for (int i=0; i < imagePaths.size(); i++) {
            String bean = imagePaths.get(i);
            ImageView view = new ImageView(this);
            setImgLayoutParams(view);
            if (!TextUtils.isEmpty(bean) && !bean.startsWith("http") && bean.contains(Constant.UPLOAD_CERTIFICATE_TYPE)) {
                bean = NetConfig.UPLOAD_FILE + bean;
                Imageload.display(this, bean, view);
            } else if (bean.startsWith("http")) {
                Imageload.display(this, bean, view);
            } else {
                setImageBitmap(view, bean);
            }
            view.setTag(bean);
            view.setOnClickListener(v -> {
                String url =  (String)v.getTag();
                Intent intent = new Intent(AssetProveActivity.this, SmoothImageActivity.class);
                intent.putExtra(SmoothImageActivity.IMAGE_SAVE_PATH_LOCAL, url);
                intent.putExtra(SmoothImageActivity.IMAGE_RIGHT_DELETE, "[]".equals(SPreference.getToCBean(this).getAssetsCertificationImage()) ||
                        TextUtils.isEmpty(SPreference.getToCBean(this).getAssetsCertificationImage()) ||
                            3 == Integer.valueOf(SPreference.getToCBean(this).getAssetsCertificationStatus()));
                startActivityForResult(intent, SMOTH_CODE);
            });
            frameLayout.addView(view);
        }
        if (0 == Integer.valueOf(SPreference.getToCBean(this).getAssetsCertificationStatus()) ||
                3 == Integer.valueOf(SPreference.getToCBean(this).getAssetsCertificationStatus())) {
            frameLayout.addView(addImg());
        }
    }

    @OnClick(R.id.commit)
    void commitButton() {
        commitProveInfo();
    }

    private boolean isSelectRole() {
        int value =  viewGroup.getChildCount();
        for (int i = 0;i < value; i++) {
            RadioButton radioButton = (RadioButton)viewGroup.getChildAt(i);
            if (radioButton.isChecked()) {
                return true;
            }
        }
        return false;
    }

    public void commitProveInfo() {
        if (!isSelectRole()) {
            Toast.makeText(AssetProveActivity.this, "请选择投资者身份", Toast.LENGTH_SHORT).show();
            return;
        }
        if (imagePaths.size() == 0) {
            Toast.makeText(AssetProveActivity.this, "请先上传资料证明", Toast.LENGTH_SHORT).show();
            return;
        }
        uploadFileProve();
    }

    private void uploadFileProve() {
        if (loading == null) {
            loading = new LoadingDialog(this);
        }
        loading.show();
        new Thread(() -> {
            remoteParams.clear();
            for (final String localPath : imagePaths) {
                if (localPath.contains(Constant.UPLOAD_CERTIFICATE_TYPE) || localPath.startsWith("http")) {
                    continue;
                }
                String newTargetFile = FileUtils.compressFileToUpload(localPath, true);
                String paths = DownloadUtils.postObject(newTargetFile, Constant.UPLOAD_CERTIFICATE_TYPE);
                FileUtils.deleteFile(newTargetFile);
                if (!TextUtils.isEmpty(paths)) {
                    remoteParams.add(paths);
                } else {
                    ThreadUtils.runOnMainThread(() -> new MToast(AssetProveActivity.this).show("证明文件上传失败，请重新上传", 0));
                    loading.dismiss();
                    return;
                }
            }
            uploadInfo();
        }).start();
    }

    private JSONArray getArrayParams(List<String> lists) {
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < lists.size() ; i++) {
            jsonArray.put(lists.get(i).startsWith("http") ? lists.get(i) : NetConfig.UPLOAD_FILE+ lists.get(i));
        }
        return jsonArray;
    }

    @Override
    public void requestSuccess() {
        if (loading != null && loading.isShowing()) {
            loading.dismiss();
        }
        AppInfStore.updateUserAssetCertificationStatus(this, "1");
        AppInfStore.updateUserAssetCertificationImageUrl(this, getArrayParams(remoteParams).toString());
        AppInfStore.updateUserInvestentType(this, ((RadioButton)viewGroup.getChildAt(0)).isChecked() ? "1" : "2");
        new MToast(AssetProveActivity.this).show("提交成功", 0);
//        EventBus.getDefault().post(new RefushUser());
        finish();
    }

    @Override
    public void requestFailure() {
        if (loading != null && loading.isShowing()) {
            loading.dismiss();
        }
        AppInfStore.updateUserAssetCertificationStatus(this, "0");
        Toast.makeText(AssetProveActivity.this, "资产证明失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void requestError(String errorMsg) {
        if (loading != null && loading.isShowing()) {
            loading.dismiss();
        }
        AppInfStore.updateUserAssetCertificationStatus(this, "0");
        Toast.makeText(AssetProveActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
    }

    private void uploadInfo() {
        getPresenter().uploadAssetProveData(getArrayParams(remoteParams) , getSelectType());
    }

    private String getSelectType() {
        RadioButton radioButton = (RadioButton)viewGroup.getChildAt(0);
        if (radioButton != null && radioButton.isChecked()) {
            return "1";
        }
        return "2";
    }

    /**
     * 设置imageview的尺寸
     */
    private void setImgLayoutParams(ImageView img) {
        //ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) img.getLayoutParams();
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, 0);
        lp.width = (int) (ViewUtils.getDisplayWidth(this)/3);
        lp.height = lp.width;
        img.setLayoutParams(lp);
        img.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }

    /**
     * 为imageview设置图片
     */
    private void setImageBitmap(ImageView img, String url){
        FileInputStream fis = null;
        img.setImageBitmap(MyBitmapUtils.getLoacalBitmap(url));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BaseWebViewActivity.REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                ArrayList<String> mSelectPath = data.getStringArrayListExtra(ImageSelector.EXTRA_RESULT);
                if (mSelectPath != null && mSelectPath.size() > 0) {
                    imagePaths.add( mSelectPath.get(0));
                    updateImageViewLayout();
                }
            }
        } else if (requestCode == SMOTH_CODE) {
            if (resultCode == RESULT_OK) {
                String url = data.getStringExtra("deletPath");
                String needVas = null;
                for (String vas : imagePaths) {
                    if (url.contains(vas)) {
                        needVas = vas;
                        break;
                    }
                }
                if (!TextUtils.isEmpty(needVas)) {
                    imagePaths.remove(needVas);
                    updateImageViewLayout();
                }
            }
        }
    }
}
