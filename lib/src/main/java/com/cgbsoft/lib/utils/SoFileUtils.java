package com.cgbsoft.lib.utils;

import android.content.Context;
import android.text.TextUtils;

import com.cgbsoft.lib.R;
import com.cgbsoft.lib.utils.constant.Constant;
import com.cgbsoft.lib.utils.tools.LogUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by fei on 2017/9/20.
 */

public class SoFileUtils {

    /*
    *
    *1.context.getCacheDir():返回通过Context.openFileOutput()创建和存储的文件系统的绝对路径，应用程序文件，这些文件会在程序被卸载的时候全部删掉。
        /data/data/com.example.qymh/cache
    * 2.context.getFilesDir():返回应用程序指定的缓存目录，这些文件在设备内存不足时会优先被删除掉，所以存放在这里的文件是没有任何保障的，可能会随时丢掉。
        /data/data/com.example.qymh/files
    *3.context.getDir("test.txt", Context.MODE_WORLD_WRITEABLE) )这是一个可以存放你自己应用程序自定义的文件，你可以通过该方法返回的File实例来创建或者访问这个目录，注意该目录下的文件只有你自己的程序可以访问。
        /data/data/com.example.qymh/test.txt
    *4.context.getPackageResourcePath();返回android 安装包的完整路径，这个包是一个ZIP的压缩文件，它包括应用程序的私有资源。
        /data/app/xxx.apk
    * 5.context.getPackageCodePath();返回android 安装包的完整路径，这个包是一个zip的压缩文件，它包括应用程序的代码和assets文件。
        /data/app/xxx.apk
    *6.context.getExternalCacheDir();使用这个方法需要写外部存储的权限“<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />”，调用该方法会返回应用程序的外部文件系统（Environment.getExternalStorageDirectory()）目录的绝对路径，它是用来存放应用的缓存文件，它和getCacheDir目录一样，目录下的文件都会在程序被卸载的时候被清除掉。
        /storage/sdcard0/Android/data/com.example.qymh/cache
    * 7.context.getExternalFilesDir(Environment.DIRECTORY_MUSIC);使用这个方法需要写外部存储的权限“<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />”，这个目录是与应用程序相关的外部文件系统，它和getExternalCacheDir不一样的是只要应用程序存在它就会一直存在，这些文件只属于你的应用，不能被其它人访问。同样，这个目录下的文件在程序被卸载时也会被一同删除。
        /storage/sdcard0/Android/data/com.example.qymh/files/Music
    * 7.context.getDatabasePath(xxx);对应数据库的全路径名
        /data/data/com.example.qymh/databases/xxx.db
    * 8.context.getObbDir();返回应用程序的OBB文件目录（如果有的话），注意如果该应用程序没有任何OBB文件，这个目录是不存在的。
        /storage/sdcard0/Android/data/com.example.qymh
    * 9.context.getSharedPrefsFile();
        Environment.getDataDirectory();  /data
        Environment.getDownloadCacheDirectory();  /cache
        Environment.getExternalStorageDirectory();  /storage/sdcard0
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);  /storage/sdcard0/Music
        Environment.getRootDirectory(); /system
    * */

    private static final String TAG = "aaa";

    /**
     * 把so文件从SD卡拷贝到应用内，或加载so文件
     * @param context 上下文
     * @param formPath SD卡路径
     * @return 0则说明执行的是拷贝操作，1则说明执行的是加载操作，-1则说明执行拷贝时出现异常
     */
    public static int loadSoFile(Context context, String formPath){
        File dir = context.getDir("dynamic", Context.MODE_PRIVATE);
        if (!isLoadSoFile(context)) {
            try {
                copy(formPath, dir.getAbsolutePath());
                return 0;
            } catch (Exception e) {
                e.printStackTrace();
                return -1;
            }
        }
        return 0;
    }

    private static  HashMap<String, String> paths = new HashMap<>();
    public static HashMap<String, String> loadSoToApp(Context context) {
        File dir = context.getDir("dynamic", Context.MODE_PRIVATE);
        paths.clear();
        traverse(dir);
        return paths;
    }

    private static void traverse(File dir) {
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            for (int i=0;i<files.length;i++) {
                if (files[i].isDirectory()) {
                    traverse(files[i]);
                } else {
                    String absolutePath = files[i].getAbsolutePath();
                    if (absolutePath.contains("ijkffmpeg")) {
                        paths.put("ijkffmpeg", absolutePath);
                    }else if (absolutePath.contains("ijksdl")) {
                        paths.put("ijksdl", absolutePath);
                    } else if (absolutePath.contains("ijkplayer")) {
                        paths.put("ijkplayer", absolutePath);
                    }
                }
            }
        } else {
            String absolutePath = dir.getAbsolutePath();
            if (absolutePath.contains("ijkffmpeg")) {
                paths.put("ijkffmpeg", absolutePath);
            }else if (absolutePath.contains("ijksdl")) {
                paths.put("ijksdl", absolutePath);
            } else if (absolutePath.contains("ijkplayer")) {
                paths.put("ijkplayer", absolutePath);
            }
        }
    }

    public static boolean isLoadSoFile(Context context) {
        File dir = context.getDir("dynamic", Context.MODE_PRIVATE);
        paths.clear();
        traverse(dir);
        boolean result=true;
        String[] damicSo = context.getResources().getStringArray(R.array.damic_so);
        for (String key : damicSo) {
            String soPath = paths.get(key);
            if (TextUtils.isEmpty(soPath)) {
                result=false;
                break;
            }
        }
        return result;
    }

    private static int copy(String fromFile, String toFile) throws Exception{
        //要复制的文件目录
        File[] currentFiles;
        File root = new File(fromFile);
        //如同判断SD卡是否存在或者文件是否存在
        //如果不存在则 return出去
        if (!root.exists()) {
            return -1;
        }
        //如果存在则获取当前目录下的全部文件 填充数组
        currentFiles = root.listFiles();

        //目标目录
        File targetDir = new File(toFile);
        //创建目录
        if (!targetDir.exists()) {
            targetDir.mkdirs();
        }
        //遍历要复制该目录下的全部文件
        for (int i = 0; i < currentFiles.length; i++) {
            if (currentFiles[i].isDirectory()) {
                //如果当前项为子目录 进行递归
                copy(currentFiles[i].getPath() + "/", toFile + currentFiles[i].getName() + "/");

            } else {
                //如果当前项为文件则进行文件拷贝
                LogUtils.Log(TAG, "path:" + currentFiles[i].getPath());
                LogUtils.Log(TAG, "name:" + currentFiles[i].getName());
                if (currentFiles[i].getName().contains(".so")) {
                    int id = copySdcardFile(currentFiles[i].getPath(), toFile + File.separator + currentFiles[i].getName());
                    LogUtils.Log(TAG, "id:" + id);
                }
            }
        }
        return 0;
    }


    //文件拷贝
    //要复制的目录下的所有非子目录(文件夹)文件拷贝
    private static int copySdcardFile(String fromFile, String toFile) throws Exception {
        FileInputStream fosfrom = new FileInputStream(fromFile);
        FileOutputStream fosto = new FileOutputStream(toFile);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        while ((len = fosfrom.read(buffer)) != -1) {
            baos.write(buffer, 0, len);
        }
        // 从内存到写入到具体文件
        fosto.write(baos.toByteArray());
        // 关闭文件流
        baos.close();
        fosto.close();
        fosfrom.close();
        return 0;
    }
    /**
     * Makes a directory, including any necessary but nonexistent parent
     * directories. If a file already exists with specified name but it is
     * not a directory then an IOException is thrown.
     * If the directory cannot be created (or does not already exist)
     * then an IOException is thrown.
     *
     * @param directory directory to create, must not be {@code null}
     * @throws NullPointerException if the directory is {@code null}
     * @throws IOException  if the directory cannot be created or the file already exists but is not a directory
     */
    public static void forceMkdir(File directory) throws IOException {
        if (directory.exists()) {
            if (!directory.isDirectory()) {
                String message =
                        "File "
                                + directory
                                + " exists and is "
                                + "not a directory. Unable to create directory.";
                throw new IOException(message);
            }
        } else {
            if (!directory.mkdirs()) {
                // Double-check that some other thread or process hasn't made
                // the directory in the background
                if (!directory.isDirectory()) {
                    String message =
                            "Unable to create directory " + directory;
                    throw new IOException(message);
                }
            }
        }
    }
}
