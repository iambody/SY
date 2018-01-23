package com.cgbsoft.privatefund.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.widget.Toast;

import com.cgbsoft.lib.BaseApplication;
import com.cgbsoft.lib.utils.dm.Utils.helper.FileUtils;
import com.cgbsoft.lib.utils.tools.CollectionUtils;
import com.cgbsoft.lib.utils.tools.DownloadUtils;
import com.cgbsoft.lib.utils.tools.ThreadUtils;
import com.cgbsoft.lib.widget.MToast;

import java.util.ArrayList;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

import static android.app.Activity.RESULT_OK;

/**
 * Created by chenlong on 2018/1/23.
 * image file upload class
 */
public class ImageFileUpload {

    private static final String[] PERMISSION_ALL = new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    private static final int PERMISSION_REQUEST_CODE = 2;

    public static void  startPhotoSelect(Activity activity, int requestCode, int maxNum) {
            if (needPermissions(PERMISSION_ALL)) {
                ActivityCompat.requestPermissions(activity, PERMISSION_ALL, PERMISSION_REQUEST_CODE);
            } else {
            MultiImageSelector.create(activity).showCamera(false).count(maxNum == 0 ? 9 : maxNum).
                    multi().start(activity, requestCode);
        }
    }

    public static List<String> onActivityResult(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (data == null) {
                return null;
            }
           return  data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
        }
        return null;
    }

    public static List<String> uploadFile(Activity activity, List<String> fileList, String dirs) {
        List<String> resultParams = new ArrayList<>();
        if (!CollectionUtils.isEmpty(fileList)) {
            for (final String localPath : fileList) {
                String newTargetFile = FileUtils.compressFileToUpload(localPath, true);
                String paths = DownloadUtils.postObject(newTargetFile, dirs); // Constant.UPLOAD_CERTIFICATE_TYPE
                FileUtils.deleteFile(newTargetFile);
                if (!TextUtils.isEmpty(paths)) {
                    resultParams.add(paths);
                } else {
                    resultParams.clear();
                    ThreadUtils.runOnMainThread(() -> new MToast(activity).show("照片上传失败，请重新上传", 0));
                    break;
                }
            }
        }
        return resultParams;
    }

    /**
     * 系统授权回调
     */
    public static void onRequestPermissionsResult(Activity activity, int requestCode, @NonNull int[] grantResults, int maxNum) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (hasAllPermissionsGranted(grantResults)) {
                MultiImageSelector.create(activity).showCamera(false).count(maxNum == 0 ? 9 : maxNum).
                        multi().start(activity, requestCode);
            } else {
                MToast.makeText(activity, "请开启系统相册和存储权限", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private static boolean hasAllPermissionsGranted(@NonNull int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    private static boolean needPermissions(String... permissions) {
        //判断版本是否兼容
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return false;
        }
        boolean isNeed;
        for (String permission : permissions) {
            isNeed = needsPermission(permission);
            if (isNeed) {
                return true;
            }
        }
        return false;
    }

    private List<String> noPermissions(String... permissions) {
        List<String> list = new ArrayList<>();
        for (String permission : permissions) {
            if (needPermissions(permission)) {
                list.add(permission);
            }
        }
        return list;
    }

    private static boolean needsPermission(String permission) {
        return ContextCompat.checkSelfPermission(BaseApplication.getContext(), permission) != PackageManager.PERMISSION_GRANTED;
    }
}
