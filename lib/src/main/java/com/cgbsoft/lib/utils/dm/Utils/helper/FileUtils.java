package com.cgbsoft.lib.utils.dm.Utils.helper;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.text.TextUtils;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * @author chenlong
 */
public class FileUtils {

    public static File create(String folder, String fileName) {
        File file = new File(
                address(folder, fileName));
        Log.d("-------------------", "Create file address: " + address(folder, fileName));
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }

    public static void forceCreate(String folder, String fileName) {
        File dirs = new File(folder);
        dirs.mkdirs();

        File file = new File(
                address(folder, fileName));

        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除该目录下的文件
     */
    public static void deleteFile(String path) {
        if (TextUtils.isEmpty(path)) {
            return;
        }

        File file = new File(path);
        if (file.exists()) {
            boolean result = file.delete();
            if (!result) {
            }
        }
    }

    public static void delete(String folder, String fileName) {
        File file = new File(
                address(folder, fileName));
        file.delete();
    }

    public static long size(String folder, String fileName) {
        File file = new File(
                address(folder, fileName));
        return file.length();
    }

    public static FileOutputStream getOutputStream(String folder, String fileName) {
        FileOutputStream fileOut = null;
        try {
            fileOut = new FileOutputStream(
                    address(folder, fileName));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return fileOut;
    }

    public static FileInputStream getInputStream(String folder, String fileName) {
        FileInputStream fileIn = null;
        try {
            fileIn = new FileInputStream(
                    address(folder, fileName));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return fileIn;
    }

    public static String address(String folder, String file) {
        return folder + "/" + file;
    }

    /**
     * 把图片压缩到900K内
     *
     * @param path 压缩前的图片路径
     * @return 压缩后图片路径
     */
    public static String compressFileToUpload(String path, boolean compress) {
        Bitmap compressBitmap = FileUtils.decodeFile(path); // 压缩图片
        if (compressBitmap == null) {
            return null;
        }
        String fileDir = new File(path).getParent();
        File targetFile = null;
        if (path.lastIndexOf(".png") > 0) {
            targetFile = new File(fileDir, UUID.randomUUID().toString().concat(".png"));
        } else if (path.lastIndexOf(".jpg") > 0) {
            targetFile = new File(fileDir, UUID.randomUUID().toString().concat(".jpg"));
        } else if (path.lastIndexOf(".jpeg") > 0) {
            targetFile = new File(fileDir, UUID.randomUUID().toString().concat(".jpeg"));
        }
        Bitmap newBitmap = ratingImage(path, compressBitmap);
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        int options = 100;
        newBitmap.compress(Bitmap.CompressFormat.JPEG, options, os);

        // 循环判断如果压缩后图片是否大于900kb,大于继续压缩
        while (compress && os.toByteArray().length / 1024 > 900) {
            options -= 5;
            os.reset();
            newBitmap.compress(Bitmap.CompressFormat.JPEG, options, os);
        }
        byte[] bytes = os.toByteArray();
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(targetFile);
            fos.write(bytes, 0, bytes.length);
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (!newBitmap.isRecycled()) {
                newBitmap.recycle();
            }
            if (!compressBitmap.isRecycled()) {
                compressBitmap.recycle();
            }
            try {
                if (os != null) {
                    os.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return targetFile.getAbsolutePath();
    }

    /**
     * 按 800*480 分辨率对图片压缩
     */
    public static Bitmap decodeFile(String fPath) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        opts.inDither = false;
        BitmapFactory.decodeFile(fPath, opts);
        int scale = 1;
        final int REQUIRED_WIDTH_SIZE = 480;
        final int REQUIRED_HEIGHT_SIZE = 800;
        if (opts.outHeight > REQUIRED_HEIGHT_SIZE || opts.outWidth > REQUIRED_WIDTH_SIZE) {
            final int heightRatio = Math.round((float) opts.outHeight / (float) REQUIRED_HEIGHT_SIZE);
            final int widthRatio = Math.round((float) opts.outWidth / (float) REQUIRED_WIDTH_SIZE);
            scale = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        opts.inJustDecodeBounds = false;
        opts.inSampleSize = scale;
        if (BitmapFactory.decodeFile(fPath, opts) == null) {
            return null;
        }
        return BitmapFactory.decodeFile(fPath, opts).copy(Bitmap.Config.ARGB_8888, false);
    }

    private static Bitmap ratingImage(String filePath, Bitmap bitmap) {
        int degree = readPictureDegree(filePath);
        return rotateImageView(degree, bitmap);
    }

    /**
     * 旋转图片
     *
     * @return Bitmap
     */
    public static Bitmap rotateImageView(int angle, Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    private static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

}
