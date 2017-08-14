package com.cgbsoft.privatefund.utils;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 文件存储工具（外部存储）
 * @author Belmen
 *
 */
public class StorageKit {

	public static final String TAG = "StorageKit";
	
	private static final String APP_ROOT_PATH = Environment.getExternalStorageDirectory() + "/rc_external_path/";
	private static final String TEMP_FILE_PATH = APP_ROOT_PATH + "temp/";
	private static final String IMAGE_FILE_PATH = APP_ROOT_PATH + "images/";
	private static final String UPDATE_APK_FILE_PATH = APP_ROOT_PATH + "update/";
	private static final String DPQ_IMAGE_FILE_PATH = APP_ROOT_PATH + "dpqimage/";
	
	static {
		initPaths();
	}
	
	/**
	 * 初始化创建所有文件夹
	 */
	private static void initPaths() {
		initPath(APP_ROOT_PATH);
		initPath(TEMP_FILE_PATH);
		initPath(IMAGE_FILE_PATH);
		initPath(UPDATE_APK_FILE_PATH);
		initPath(DPQ_IMAGE_FILE_PATH);
	}
	
	private static void initPath(String path) {
		if(!isExternalStorageWritable()) {
			return;
		}
		File f = new File(path);
		if(!f.exists()) {
			if(!f.mkdirs()) {
			}
		}
	}
	
	/**
	 * 测试外部存储器-SD卡是否可用
	 * @return
	 */
	public static boolean isExternalStorageAvailable() {
	    boolean state = false;
	    String extStorageState = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
	        state = true;
	    } else {
	    }
	    return state;
	}
	
	/**
	 * 测试外部存储器-SD卡是否只读
	 * @return
	 */
	public static boolean isExternalStorageReadOnly() {
	    boolean state = false;
	    String extStorageState = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
	        state = true;
	    }
	    return state;
	}
	
	/**
	 * 测试外部存储器-SD卡是否可写
	 * @return
	 */
	public static boolean isExternalStorageWritable() {
		return isExternalStorageAvailable() && !isExternalStorageReadOnly();
	}
	
	/**
	 * 返回应用外部存储根路径
	 * @return
	 */
	public static String getAppRootPath() {
		return APP_ROOT_PATH;
	}
	/**
	 * 返回应用外部存储临时文件路径
	 * @return
	 */
	public static String getTempFilePath() {
		return TEMP_FILE_PATH;
	}
	/**
	 * 返回应用外部存储图片文件路径
	 * @return
	 */
	public static String getImageFilePath() {
		return IMAGE_FILE_PATH;
	}
	
	/**
	 * 返回应用外部存储下载的更新APK文件路径
	 * @return
	 */
	public static String getUpdateApkFilePath() {
		return UPDATE_APK_FILE_PATH;
	}
	
	/**
	 * 返回应用外部存储点评圈图片文件路径
	 * @return
	 */
	public static String getDpqImageFilePath() {
		return DPQ_IMAGE_FILE_PATH;
	}
	
	/**
	 * 返回相册中图片的路径
	 * @param activity
	 * @param uri
	 * @return
	 */
	public static String getPhotoPathFromAlbum(Activity activity, Uri uri) {
		String[] proj = { MediaStore.Images.Media.DATA };
	    Cursor cursor = activity.managedQuery(uri, proj, null, null, null);
	    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	    cursor.moveToFirst();
	    return cursor.getString(column_index);
	}
	
	/**
	 * 保存文件
	 * @param content
	 * @param path
	 * @param filename
	 * @return
	 */
	public static boolean saveFile(byte[] content, String path, String filename) {
		if(!isExternalStorageWritable() || content == null || filename == null) {
			return false;
		}
		
		FileOutputStream fos = null;
		File file = new File(path, filename);
		try {
			fos = new FileOutputStream(file);
			fos.write(content);
		} catch (Exception e) {
		} finally {
			try {
				if(fos != null) {
					fos.close();
				}
			} catch (IOException e) {
			}
		}
		return false;
	}
	
	/**
	 * 将位图保存至外部文件，默认压缩为JPEG格式，质量85
	 * @param bitmap
	 * @param filename
	 * @return
	 */
	public static boolean saveBitmapFileToJpeg(Bitmap bitmap, String filename, int quality) {
		if(!isExternalStorageWritable() || bitmap == null) {
			return false;
		}
		
		FileOutputStream fos = null;
		File file = new File(getImageFilePath(), filename);
		try {
			fos = new FileOutputStream(file);
			return bitmap.compress(CompressFormat.JPEG, quality, fos);
		} catch (FileNotFoundException e) {
		} finally {
			try {
				if(fos != null) {
					fos.close();
				}
			} catch (IOException e) {
			}
		}
		return false;
	}
	
	/**
	 * 从外部存储读取图片
	 * @param filename
	 * @return
	 */
	public static Bitmap loadImageFile(String filename) {
		return loadImageFile(filename, null);
	}
	
	/**
	 * 从外部存储读取图片
	 * @param filename
	 * @param options 自定义解码设置
	 * @return
	 */
	public static Bitmap loadImageFile(String filename, BitmapFactory.Options options) {
		if(!isExternalStorageAvailable() || filename == null) {
			return null;
		}
		FileInputStream fis = null;
		File file = new File(getImageFilePath(), filename);
		try {
			if(file != null && file.exists()) {
				fis = new FileInputStream(file);
				return BitmapFactory.decodeStream(fis, null, options);
			}
		} catch (FileNotFoundException e) {
		} finally {
			try {
				if(fis != null) {
					fis.close();
				}
			} catch (IOException e2) {
			}
		}
		return null;
	}
	
	/**
	 * 删除文件
	 * @param filename 文件名
	 */
	public static boolean deleteFile(String filename) {
		if(TextUtils.isEmpty(filename)) {
			return false;
		}
		return deleteFile(new File(filename));
	}
	
	/**
	 * 删除文件
	 * @param file 文件
	 */
	public static boolean deleteFile(File file) {
		boolean status = false;
		if (!isExternalStorageWritable()) {
			return status;
		}
		try {
			if (file.exists() && file.isFile()) {
				status = file.delete();
			}
		} catch (Exception e) {
		}
		return status;
	}
	
	// 删除指定文件夹下所有文件
	// param path 文件夹完整绝对路径
	public static boolean delAllFile(String path) {
		boolean flag = false;
		File file = new File(path);
		if (!file.exists()) {
			return flag;
		}
		if (!file.isDirectory()) {
			return flag;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
				delFolder(path + "/" + tempList[i]);// 再删除空文件夹
				flag = true;
			}
		}
		return flag;
	}
		
	public static void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); // 删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			File myFilePath = new File(filePath);
			myFilePath.delete(); // 删除空文件夹
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
