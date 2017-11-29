package app.ocrlib.com.utils;

import android.graphics.Bitmap;

import java.io.FileOutputStream;

/**
 * desc  保存头像到bitmap
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/10/31-09:48
 */
public class BitmapUtils {
    public static String saveBitmap(Bitmap bitmap, String imagename) {
        String path = "/sdcard/" + "face" + System.currentTimeMillis() + imagename + ".png";
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(path);
            if (fos != null) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
                fos.flush();
                fos.close();
            }

            return path;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

}
