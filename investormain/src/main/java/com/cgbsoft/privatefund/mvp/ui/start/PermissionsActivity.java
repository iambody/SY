//package com.cgbsoft.privatefund.mvp.ui.start;
//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.net.Uri;
//import android.os.Bundle;
//import android.provider.Settings;
//import android.support.annotation.NonNull;
//import android.support.v4.app.ActivityCompat;
//import android.text.TextUtils;
//import android.view.KeyEvent;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//
//import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
//import com.cgbsoft.lib.utils.tools.Utils;
//import com.cgbsoft.privatefund.R;
//import com.cgbsoft.privatefund.mvp.contract.start.PermissionContract;
//import com.cgbsoft.privatefund.mvp.presenter.start.PermissionPersenter;
//
//import java.util.List;
//
//import butterknife.BindView;
//
///**
// * 权限页面
// * Created by xiaoyu.zhang on 2016/5/17.
// * Email:zhangxyfs@126.com
// */
//public class PermissionsActivity extends BaseActivity<PermissionPersenter> implements PermissionContract.View {
//    @BindView(R.id.ap_bottom_iv)
//    ImageView ap_bottom_iv;
//
//    public static final int PERMISSIONS_GRANTED = 0; // 权限授权
//    public static final int PERMISSIONS_DENIED = 1; // 权限拒绝
//
//    private static final int PERMISSION_REQUEST_CODE = 0; // 系统权限管理页面的参数
//
//    private static final String EXTRA_PERMISSIONS = "com.busap.myvideo.permission.extra_permission"; // 权限参数
//    private static final String PACKAGE_URL_SCHEME = "package:"; // 方案
//
//    private boolean isRequireCheck; // 是否需要系统权限检测
//    private AlertDialog.Builder alertBuilder;
//    private boolean isAlertDialogShow;
//
//    // 启动当前权限页面的公开接口
//    public static void startActivityForResult(Activity activity, int requestCode, String... permissions) {
//        Intent intent = new Intent(activity, PermissionsActivity.class);
//        intent.putExtra(EXTRA_PERMISSIONS, permissions);
//        ActivityCompat.startActivityForResult(activity, intent, requestCode, null);
//    }
//
//    @Override
//    public void before() {
//        super.before();
//        if (getIntent() == null || !getIntent().hasExtra(EXTRA_PERMISSIONS)) {
//            throw new RuntimeException("PermissionsActivity需要使用静态startActivityForResult方法启动!");
//        }
//    }
//
//    @Override
//    public int layoutID() {
//        return R.layout.activity_permission;
//    }
//
//    @Override
//    public void init(Bundle savedInstanceState) {
//        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) ap_bottom_iv.getLayoutParams();
//        lp.width = Utils.getScreenWidth(this);
//        lp.height = Utils.getScreenWidth(this) * 464 / 1242;
//        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//        ap_bottom_iv.setLayoutParams(lp);
//
//
//        isRequireCheck = true;
//        alertBuilder = new AlertDialog.Builder(this);
//        alertBuilder.setTitle(R.string.permission_help);
//        alertBuilder.setCancelable(false);
//
//        // 拒绝, 退出应用
//        alertBuilder.setNegativeButton(R.string.permission_quit, (dialog, which) -> {
//            setResult(PERMISSIONS_DENIED);
//            isAlertDialogShow = false;
//            finish();
//        });
//
//        alertBuilder.setPositiveButton(R.string.permission_setting, (dialog, which) -> {
//            isAlertDialogShow = false;
//            dialog.dismiss();
//            startAppSettings();
//        });
//
//        alertBuilder.setOnKeyListener((dialog, keyCode, event) -> {
//            if (keyCode == KeyEvent.KEYCODE_BACK) {
//                return true;
//            }
//            return false;
//        });
//    }
//
//    @Override
//    protected PermissionPersenter createPresenter() {
//        return new PermissionPersenter(this, this);
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (isRequireCheck && !isAlertDialogShow) {
//            String[] permissions = getPermissions();
//            if (needPermissions(permissions)) {
//                requestPermissions(permissions); // 请求权限
//            } else {
//                allPermissionsGranted(); // 全部权限都已获取
//            }
//        } else {
//            isRequireCheck = true;
//        }
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//    }
//
//    // 返回传递的权限参数
//    private String[] getPermissions() {
//        return getIntent().getStringArrayExtra(EXTRA_PERMISSIONS);
//    }
//
//    // 请求权限兼容低版本
//    private void requestPermissions(String... permissions) {
//        ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE);
//    }
//
//    // 全部权限均已获取
//    private void allPermissionsGranted() {
//        setResult(PERMISSIONS_GRANTED);
//        finish();
//    }
//
//    /**
//     * 用户权限处理,
//     * 如果全部获取, 则直接过.
//     * 如果权限缺失, 则提示Dialog.
//     *
//     * @param requestCode  请求码
//     * @param permissions  权限
//     * @param grantResults 结果
//     */
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        if (requestCode == PERMISSION_REQUEST_CODE && hasAllPermissionsGranted(grantResults)) {
//            isRequireCheck = true;
//            allPermissionsGranted();
//        } else {
//            isRequireCheck = false;
//            showMissingPermissionDialog();
//        }
//    }
//
//    // 含有全部的权限
//    private boolean hasAllPermissionsGranted(@NonNull int[] grantResults) {
//        for (int grantResult : grantResults) {
//            if (grantResult == PackageManager.PERMISSION_DENIED) {
//                return false;
//            }
//        }
//        return true;
//    }
//
//    // 显示缺失权限提示
//    private void showMissingPermissionDialog() {
//        String insertStr = "";
//        List<String> list = noPermissions(getPermissions());
//        for (String str : list) {
//            if (TextUtils.equals(str, PERMISSION_AUDIO)) {
//                insertStr += getString(R.string.permission_audio_str);
//            } else if (TextUtils.equals(str, PERMISSION_CAMERA)) {
//                insertStr += getString(R.string.permission_camera_str);
//            } else if (TextUtils.equals(str, PERMISSION_WRITE_CONTANCTS)) {
//                insertStr += getString(R.string.permission_contancts_str);
//            } else if (TextUtils.equals(str, PERMISSION_SEND_SMS)) {
//                insertStr += getString(R.string.permission_sms_str);
//            } else if (TextUtils.equals(str, PERMISSION_WRITE_STORAGE)) {
//                insertStr += getString(R.string.permission_sdcard_str);
//            } else if (TextUtils.equals(str, PERMISSION_READ_PHONE_STATE)) {
//                insertStr += getString(R.string.permission_phone_str);
//            } else if (TextUtils.equals(str, PERMISSION_LOCATION)) {
//                insertStr += getString(R.string.permission_location_str);
//            }
//        }
//        alertBuilder.setMessage(getString(R.string.permission_help_text, insertStr));
//        alertBuilder.show();
//        isAlertDialogShow = true;
//    }
//
//    // 启动应用的设置
//    private void startAppSettings() {
//        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//        intent.setData(Uri.parse(PACKAGE_URL_SCHEME + getPackageName()));
//        startActivity(intent);
//    }
//
//
//    @Override
//    public void onBackPressed() {
//    }
//
//
//}
