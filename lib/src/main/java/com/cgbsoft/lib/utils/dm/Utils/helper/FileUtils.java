package com.cgbsoft.lib.utils.dm.Utils.helper;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.cgbsoft.lib.BaseApplication;
import com.cgbsoft.lib.utils.ZipUtils;
import com.cgbsoft.lib.utils.constant.Constant;
import com.cgbsoft.lib.utils.tools.LogUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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

    public static File getTempFile(String fileName) {
        File file = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
            file = new File(BaseApplication.getContext().getExternalCacheDir().getPath(), fileName);
        } else {
            file = new File(BaseApplication.getContext().getCacheDir().getPath(), fileName);
        }
        return file;
    }

        public static File createResourceLocalTempFile(String dir, String fileName) {
        File file = null;
        if ((Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) && BaseApplication.getContext().getExternalCacheDir() != null) {
            file = new File(BaseApplication.getContext().getExternalCacheDir().getPath() + File.separator + dir, fileName);
        } else {
            file = new File(BaseApplication.getContext().getCacheDir().getPath() +  File.separator + dir, fileName);
        }

         if (file != null && file.exists()) {
             file.delete();
         }

         if (!file.getParentFile().exists()) {
             file.getParentFile().mkdirs();
         }
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }

    public static File getResourceLocalTempFile(String dir, String fileName) {
        File file = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
            file = new File(BaseApplication.getContext().getExternalCacheDir().getPath() + File.separator + dir, fileName);
        } else {
            file = new File(BaseApplication.getContext().getCacheDir().getPath() +  File.separator + dir, fileName);
        }

        return file;
    }

    /**
     * 找出指定文件在目录下的全路径
     * @param dir
     * @param fileName
     * @return
     */
    public static String isExsitFileInFileDir(String dir, String fileName) {
        String resultPath = "";
        if (!TextUtils.isEmpty(dir)) {
            File dirFile = new File(dir);
            File[] files = dirFile.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        String subResult = isExsitFileInFileDir(file.getAbsolutePath(), fileName);
                         if (!TextUtils.isEmpty(subResult)) {
                             resultPath = subResult;
                             break;
                         }
                    } else {
                        String name = file.getName();
                        System.out.println("-----down  name="  +name);
                        if (TextUtils.equals(name, fileName)) {
                            resultPath = file.getAbsolutePath();
                            break;
                        }
                    }
                }
            }
        }
        return resultPath;
    }

    /**
     * 删除文件或文件夹
     * @param
     */
    public static void deleteDir(File file) {
        if (null == file || !file.exists()) {
            return;
        }
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    deleteDir(files[i]);
                } else {
                    files[i].delete();
                }
            }
            file.delete();
        } else {
            file.delete();
        }
    }
    public static File createTempFile(String fileName) {
        File file = deleteTempFile(fileName);
        if (null == file) {
            return null;
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return file;
    }

    public static File deleteTempFile(String fileName) {
        File file = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
            file = new File(BaseApplication.getContext().getExternalCacheDir().getPath(), fileName);
        } else {
            file = new File(BaseApplication.getContext().getCacheDir().getPath(), fileName);
        }
        if (file != null && file.exists()) {
            file.delete();
        }
        return file;
    }

    public static void doUnzip(final File origiFile, File targetFile, String fileName, String dirName, final UnZipCallback callBack) {
//        File h5Res = new File(MyApplication.getInstance().getH5NativePath());
//        FileUtil.deleteDir(h5Res);
        new Thread(new Runnable() {
            public File zipFile;
            @Override
            public void run() {
                BufferedInputStream bis = null;
                BufferedOutputStream bos = null;
                InputStream fileDescriptor = null;
                int BUFFER = 10240;
                zipFile = new File(targetFile.getParent() , fileName);
//                File dir = BaseApplication.getContext().getDir("dynamic", Context.MODE_PRIVATE);
//                zipFile = new File(dir.getParent() , Constant.SO_ZIP_NAME);
                try {
//                    if (null == origiFile || !origiFile.exists()) {
//                        fileDescriptor = MyApplication.getInstance().getResources().getAssets().open("home.zip");
//                    } else {
                    fileDescriptor = new FileInputStream(origiFile);
//                    }
                    bis = new BufferedInputStream(fileDescriptor, BUFFER);
                    bos = new BufferedOutputStream(new FileOutputStream(zipFile), BUFFER);
                    byte[] buffer = new byte[BUFFER];
                    int read = 0;
                    while ((read = bis.read(buffer)) > 0) {// 循环从输入流读取
                        bos.write(buffer, 0, read);// 将读取的输入流写入到输出流
                    }
                    closeStream(bis, bos);
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
                try {
                    ZipUtils.unzip(zipFile, dirName, new ZipUtils.ZipAction() {
                        @Override
                        public void star() {
                            callBack.beginUnZip();
                        }

                        @Override
                        public void updateProgress(int progress) {
                            callBack.updateProgress(progress);
                        }

                        @Override
                        public void end() {
                            if (zipFile != null && zipFile.exists()) {
                                zipFile.delete();
                            }
                            callBack.endUnZip();
                            LogUtils.Log("aaa", "****************************************************end unzip");
                        }

                        @Override
                        public void error() {
                            callBack.failed();
                        }
                    });
                } catch (Exception e) {
                    callBack.failed();
                    e.printStackTrace();
                    return;
                }
                callBack.success();
            }
        }).start();
    }
    public static void closeStream(BufferedInputStream bis, BufferedOutputStream bos) {
        try {
            bos.flush();// 刷新缓冲区
            bis.close();
            bos.close();
        } catch (Exception e) {
            try {
                bos.flush();// 刷新缓冲区
                bis.close();
                bos.close();
            } catch (Exception e1) {
                e1.printStackTrace();
                return;
            }
        }
    }
    public interface UnZipCallback {
        void success();

        void beginUnZip();

        void updateProgress(int progerss);

        void endUnZip();
        void failed();
    }
}
