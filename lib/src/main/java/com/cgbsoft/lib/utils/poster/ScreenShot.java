package com.cgbsoft.lib.utils.poster;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Environment;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/10/26-15:13
 */
public class ScreenShot {
    // 获取内置SD卡路径
    private static String sdCardPath = Environment.getExternalStorageDirectory().getPath();
    // 图片文件路径
    private static String filePath = sdCardPath + File.separator + "screenshot.png";

    /**
     * 获取和保存当前屏幕的截图
     * 因为硬件问题可能存在失败概率 失败就返回null 分享时候需要进行判断
     */
    public static String GetandSaveCurrentImage(Activity activity) {
        //1.构建Bitmap
        View dView = activity.getWindow().getDecorView();
        dView.setDrawingCacheEnabled(true);
        dView.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(dView.getDrawingCache());
        if (bitmap != null) {
            try {
                File file = new File(filePath);
                if (!file.exists()) {
                    file.mkdirs();
                }
                if (!file.exists()) {
                    file.createNewFile();
                }
                FileOutputStream os = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
                os.flush();
                os.close();
                return filePath;

            } catch (Exception e) {
                return null;
            }
        } else {
            return null;
        }
    }
//
//    /**
//     * 获取SDCard的文件夹路径功能
//     *
//     * @return
//     */
//    private static String getSDCardPath() {
//        File sdcardDir = null;
//        //推断SDCard是否存在
//        boolean sdcardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
//        if (sdcardExist) {
//            sdcardDir = Environment.getExternalStorageDirectory();
//        }
//        return sdcardDir.toString();
//    }


}
