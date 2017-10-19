package com.cgbsoft.lib.utils;


import android.content.Context;

import com.cgbsoft.lib.BaseApplication;
import com.cgbsoft.lib.utils.dm.Utils.helper.FileUtils;
import com.cgbsoft.lib.utils.tools.LogUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * Created by feigecal on 2017/3/16 0016.
 */

public class ZipUtils {

    /**
     * 解压文件
     *
     * @param zipFile
     */
    public static void unzip(File zipFile, ZipAction callback) throws Exception {
        File dirPath = BaseApplication.getContext().getDir("dynamic", Context.MODE_PRIVATE);
        File[] files = dirPath.listFiles();
        if (null != files) {
            FileUtils.deleteDir(dirPath);
        }
        //目标目录
        File targetDir = new File(dirPath.getAbsolutePath());
        //创建目录
        if (!targetDir.exists()) {
            targetDir.mkdirs();
        }
        //回调解压开始
        callback.star();
        long sumLength = 0;
        // 获取解压之后文件的大小,用来计算解压的进度
        long ziplength = getZipTrueSize(zipFile.getPath());
        ZipInputStream zis = new ZipInputStream(new BufferedInputStream(new FileInputStream(zipFile)));
        try {
            ZipEntry ze;
            int count;
            File file;
            byte[] buffer = new byte[8192];
            while ((ze = zis.getNextEntry()) != null) {
                file = new File(targetDir.getAbsolutePath(), ze.getName());
                LogUtils.Log("aaa","file.getAbsolutePath()==="+file.getAbsolutePath());
                LogUtils.Log("aaa","ze.getName()==="+ze.getName());
                LogUtils.Log("aaa","ze.isDirectory()==="+ze.isDirectory());
                File dir = ze.isDirectory() ? file : file.getParentFile();
                if (!dir.isDirectory() && !dir.mkdirs()) throw new FileNotFoundException("Failed to ensure directory: " + dir.getAbsolutePath());
                if (ze.isDirectory()) {
                    continue;
                }
                FileOutputStream fout = null;
                try {
                    fout = new FileOutputStream(file);
                    while ((count = zis.read(buffer)) != -1) {
                        sumLength += count;
                        int progress = (int) ((sumLength * 100) / ziplength);
                        updateProgress(progress,callback);
                        fout.write(buffer, 0, count);
                        fout.flush();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.error();
                } finally {
                    fout.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            callback.error();
        } finally {
            zis.close();
            callback.end();
        }
    }
    private static int lastProgress=0;
    private static void updateProgress(int progress, ZipAction callback) {
        /** 因为会频繁的刷新,这里我只是进度>1%的时候才去显示 */
        if (progress > lastProgress) {
            lastProgress = progress;
            callback.updateProgress(progress);
        }
    }

    //解压回调方法
    public interface ZipAction {
        void star();
        void updateProgress(int progerss);
        void end();
        void error();
    }
    /**
     * 获取压缩包解压后的内存大小
     *
     * @param filePath 文件路径
     * @return 返回内存long类型的值
     */
    public static long getZipTrueSize(String filePath) throws Exception {
        long size = 0;
        ZipFile f= new ZipFile(filePath);
            Enumeration<? extends ZipEntry> en = f.entries();
            while (en.hasMoreElements()) {
                size += en.nextElement().getSize();
            }
        return size;
    }

    /**
     * 解压 zip 文件
     * @param
     * @throws IOException
     */
    public static boolean unZipFileAtAnyPath(String resourceZip, ZipAction callBack) {
//        destination = destination.endsWith("/") ? destination : destination+ "/";
        byte b[] = new byte[1024 * 4];
        int length;
        ZipFile zipFile;
        try {
            long ziplength = getZipTrueSize(resourceZip);
            zipFile = new ZipFile(new File(resourceZip));
            Enumeration enumeration = zipFile.entries();
            ZipEntry zipEntry = null;
            if (callBack != null) {
                callBack.star();
            }
            long currentProgress = 0;
            while (enumeration.hasMoreElements()) {
                zipEntry = (ZipEntry) enumeration.nextElement();
                File loadFile = new File(zipEntry.getName());
                if (zipEntry.isDirectory()) {
                    loadFile.mkdirs();
                } else {
                    if (!loadFile.getParentFile().exists())
                        loadFile.getParentFile().mkdirs();
                    if (!loadFile.exists()) {
                        loadFile.createNewFile();
                    }
                    BufferedOutputStream outputStream = new BufferedOutputStream(
                            new FileOutputStream(loadFile));
                    BufferedInputStream inputStream = new BufferedInputStream(zipFile.getInputStream(zipEntry));
                    while ((length = inputStream.read(b)) > 0) {
                        outputStream.write(b, 0, length);
                        currentProgress += length;
                        int progress = (int) ((currentProgress * 100) / ziplength);
                        if (callBack != null) {
                            updateProgress(progress,callBack);
                        }
                    }
                    outputStream.flush();
                    outputStream.close();
                    inputStream.close();
                    callBack.end();
                }
            }
            zipFile.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            if (callBack != null) {
                callBack.error();
            }
        } finally {
            if (callBack != null) {
                callBack.end();
            }
        }
        return false;
    }
}
