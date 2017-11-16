package com.cgbsoft.lib.utils.tools;

import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/11/16-00:02
 */
public class CameraUtils {
    public static boolean getCameraPermission(Context context) {
        boolean isCanUse = false;
        Camera mCamera = null;
        try {
            mCamera = Camera.open();
            Camera.Parameters mParameters = mCamera.getParameters();
            mCamera.setParameters(mParameters);
            isCanUse = true;
        } catch (Exception e) {
            isCanUse = false;
        } finally {
            // 释放相机，这个必须要，必须要，必须要！！！！
            if (mCamera != null) {
                try {
                    mCamera.release();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return isCanUse;
        }
    }

    /**
     * 跳转到设置页面
     * @param context
     */
    public static void toSetCameraPermission(Context context) {
        if (getCameraPermission(context)) {
        } else {
            PromptManager.ShowCustomToast(context, "请设置允许拍照权限");
            toSelfSetting(context);
        }
    }


    public static void toSelfSetting(Context context) {
        Intent mIntent = new Intent();
        mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            mIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            mIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            mIntent.setAction(Intent.ACTION_VIEW);
            mIntent.setClassName("com.android.settings", "com.android.setting.InstalledAppDetails");
            mIntent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
        }
        context.startActivity(mIntent);
    }
}
